const SpaceShieldDB = {
    init() {
        if (!localStorage.getItem('ss_users')) {
            const defaultUsers = [
                { id: 1, name: "Admin FIAP", email: "admin@spaceshield.com", password: "1234" }
            ];
            localStorage.setItem('ss_users', JSON.stringify(defaultUsers));

            const defaultSatellites = [
                { id: 1845, ownerId: 1, name: "NavSat-Global", focus: "Infraestrutura de GPS", status: "Operacional", riskLevel: "Low", events: [] },
                { id: 7290, ownerId: 1, name: "GeoSync-BR", focus: "Comunicações", status: "Operacional", riskLevel: "Low", events: [] }
            ];
            localStorage.setItem('ss_satellites', JSON.stringify(defaultSatellites));

            localStorage.setItem('ss_alerts', JSON.stringify([]));
        }
    },

    login(email, password) {
        const users = JSON.parse(localStorage.getItem('ss_users'));
        const user = users.find(u => u.email === email && u.password === password);
        if (user) {
            localStorage.setItem('ss_currentUser', JSON.stringify(user));
            return true;
        }
        return false;
    },

    registerUser(name, email, password) {
        const users = JSON.parse(localStorage.getItem('ss_users'));
        
        if (users.find(u => u.email === email)) {
            return { success: false, msg: "[ERRO] Este e-mail já possui uma credencial ativa." };
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            return { success: false, msg: "[ERRO] Formato de e-mail inválido." };
        }

        const newUser = {
            id: Math.floor(100 + Math.random() * 900),
            name: name,
            email: email,
            password: password
        };
        
        users.push(newUser);
        localStorage.setItem('ss_users', JSON.stringify(users));
        return { success: true, msg: "Credencial registrada com sucesso! Você já pode fazer o login no painel ao lado." };
    },

    logout() {
        localStorage.removeItem('ss_currentUser');
        window.location.href = "login.html";
    },

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('ss_currentUser'));
    },

    getSatellites() {
        const user = this.getCurrentUser();
        if (!user) return [];
        const allSats = JSON.parse(localStorage.getItem('ss_satellites'));
        return allSats.filter(sat => sat.ownerId === user.id);
    },

    registerSatellite(name, focus) {
        const user = this.getCurrentUser();
        const sats = JSON.parse(localStorage.getItem('ss_satellites'));
        const newSat = {
            id: Math.floor(1000 + Math.random() * 9000),
            ownerId: user.id,
            name: name,
            focus: focus,
            status: "Operacional",
            riskLevel: "Low",
            events: []
        };
        sats.push(newSat);
        localStorage.setItem('ss_satellites', JSON.stringify(sats));
    },

    logEvent(satId, eventType, description) {
        const sats = JSON.parse(localStorage.getItem('ss_satellites'));
        const satIndex = sats.findIndex(s => s.id == satId);
        
        if (satIndex === -1) return { success: false, msg: "Satélite não encontrado na frota." };
        if (sats[satIndex].ownerId !== this.getCurrentUser().id) return { success: false, msg: "Acesso Negado: Este satélite pertence a outra organização." };

        const now = new Date();
        const timeStr = now.toLocaleTimeString('pt-BR');
        const dateStr = now.toLocaleDateString('pt-BR');
        const newEvent = {
            id: Math.floor(1000 + Math.random() * 9000),
            satelliteId: satId,
            eventType: eventType,
            description: description,
            timestamp: `${dateStr} às ${timeStr}`
        };

        sats[satIndex].events.push(newEvent);

        const eventCount = sats[satIndex].events.length;
        if (eventCount > 5) sats[satIndex].riskLevel = "High";
        else if (eventCount > 2) sats[satIndex].riskLevel = "Medium";
        else sats[satIndex].riskLevel = "Low";

        localStorage.setItem('ss_satellites', JSON.stringify(sats));

        const typeLower = eventType.toLowerCase();
        if (typeLower.includes("unauthorized") || typeLower.includes("invasão") || typeLower.includes("suspicious") || typeLower.includes("spoofing")) {
            this.generateAlert(newEvent, "Critical");
            return { success: true, alertGenerated: true };
        }

        return { success: true, alertGenerated: false };
    },

    generateAlert(accessEvent, severity) {
        const alerts = JSON.parse(localStorage.getItem('ss_alerts'));
        const newAlert = {
            id: Math.floor(1000 + Math.random() * 9000),
            event: accessEvent,
            severity: severity,
            resolutionStatus: "Active"
        };
        alerts.unshift(newAlert);
        localStorage.setItem('ss_alerts', JSON.stringify(alerts));
    },

    getAlerts() {
        const user = this.getCurrentUser();
        if (!user) return [];
        const mySatIds = this.getSatellites().map(s => s.id);
        const allAlerts = JSON.parse(localStorage.getItem('ss_alerts'));
        return allAlerts.filter(a => mySatIds.includes(Number(a.event.satelliteId)));
    },

    resolveAlert(alertId) {
        const alerts = JSON.parse(localStorage.getItem('ss_alerts'));
        const alertIndex = alerts.findIndex(a => a.id == alertId);
        if (alertIndex !== -1) {
            alerts[alertIndex].resolutionStatus = "Resolved";
            localStorage.setItem('ss_alerts', JSON.stringify(alerts));
        }
    }
};

document.addEventListener('DOMContentLoaded', () => {
    SpaceShieldDB.init();

    if (!window.location.pathname.includes('login.html') && !SpaceShieldDB.getCurrentUser()) {
        window.location.href = 'login.html';
        return;
    }

    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value;
            const pass = document.getElementById('password').value;
            if (SpaceShieldDB.login(email, pass)) {
                window.location.href = 'index.html';
            } else {
                alert("Acesso Negado: Credenciais incorretas.");
            }
        });
    }

    const registerOpForm = document.getElementById('register-operator-form');
    if (registerOpForm) {
        registerOpForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const name = document.getElementById('reg-name').value;
            const email = document.getElementById('reg-email').value;
            const pass = document.getElementById('reg-pass').value;

            const result = SpaceShieldDB.registerUser(name, email, pass);
            
            alert(result.msg);
            
            if (result.success) {
                registerOpForm.reset();
            }
        });
    }

    const userNameDisplay = document.getElementById('logged-user-name');
    if (userNameDisplay) userNameDisplay.innerText = SpaceShieldDB.getCurrentUser().name;

    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) logoutBtn.addEventListener('click', () => SpaceShieldDB.logout());

    const satTableBody = document.getElementById('satellite-table-body');
    if (satTableBody) {
        const renderSats = () => {
            satTableBody.innerHTML = '';
            SpaceShieldDB.getSatellites().forEach(sat => {
                let badgeColor = "badge-success";
                if (sat.riskLevel === "Medium") badgeColor = "badge-warning";
                if (sat.riskLevel === "High") badgeColor = "badge-danger";

                satTableBody.innerHTML += `
                    <tr>
                        <td><strong>#${sat.id}</strong></td>
                        <td>${sat.name}</td>
                        <td>${sat.focus}</td>
                        <td><span class="badge ${badgeColor}">${sat.riskLevel}</span></td>
                        <td>${sat.events.length} logs</td>
                    </tr>
                `;
            });
        };
        renderSats();

        const registerForm = document.getElementById('register-form');
        if (registerForm) {
            registerForm.addEventListener('submit', (e) => {
                e.preventDefault();
                SpaceShieldDB.registerSatellite(
                    document.getElementById('sat-name').value, 
                    document.getElementById('sat-focus').value
                );
                renderSats();
                e.target.reset();
            });
        }
    }

    const simForm = document.getElementById('simulation-form');
    if (simForm) {
        const updateCounters = () => {
            const activeAlerts = SpaceShieldDB.getAlerts().filter(a => a.resolutionStatus === "Active").length;
            document.getElementById('alert-counter').innerText = activeAlerts;
            document.getElementById('sat-counter').innerText = SpaceShieldDB.getSatellites().length;
        };
        updateCounters();

        simForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const result = SpaceShieldDB.logEvent(
                document.getElementById('sim-id').value,
                document.getElementById('sim-type').value,
                document.getElementById('sim-desc').value
            );

            if (!result.success) {
                alert(result.msg);
                return;
            }

            if (result.alertGenerated) {
                alert("Evento Crítico! AccessEvent registrado e SecurityAlert gerado.");
            } else {
                alert("Evento registrado no AccessEvent history do satélite (Nenhum alerta gerado).");
            }
            
            updateCounters();
            simForm.reset();
        });
    }

    const containerAlertas = document.getElementById('container-alertas');
    if (containerAlertas) {
        window.resolveAlertUI = function(alertId) {
            SpaceShieldDB.resolveAlert(alertId);
            window.location.reload();
        };

        const renderAlerts = () => {
            containerAlertas.innerHTML = '';
            const alerts = SpaceShieldDB.getAlerts();
            
            if(alerts.length === 0) {
                containerAlertas.innerHTML = '<p>Nenhum alerta ativo para a sua frota.</p>';
                return;
            }

            alerts.forEach(alerta => {
                const isActive = alerta.resolutionStatus === "Active";
                const border = isActive ? 'var(--danger)' : 'var(--success)';
                const badge = isActive ? '<span class="badge badge-danger">Crítico / Ativo</span>' : '<span class="badge badge-success">Resolvido</span>';
                const actionBtn = isActive 
                    ? `<button onclick="resolveAlertUI(${alerta.id})" class="btn-primary" style="background:var(--success);">Marcar como Resolvido</button>`
                    : ``;

                containerAlertas.innerHTML += `
                    <div class="card" style="border-left: 4px solid ${border};">
                        <div style="display: flex; justify-content: space-between; align-items: flex-start; flex-wrap: wrap; gap: 12px;">
                            <div>
                                ${badge}
                                <h2 style="margin: 8px 0 4px 0;">Ameaça: ${alerta.event.eventType}</h2>
                                <p><strong>Satélite:</strong> #${alerta.event.satelliteId} &bull; <strong>Data/Hora:</strong> ${alerta.event.timestamp}</p>
                                <p style="margin-top: 12px; max-width: 600px;">${alerta.event.description}</p>
                            </div>
                            ${actionBtn}
                        </div>
                    </div>
                `;
            });
        };
        renderAlerts();
    }

    const reportSelect = document.getElementById('report-sat-select');
    if (reportSelect) {
        const sats = SpaceShieldDB.getSatellites();
        sats.forEach(sat => {
            reportSelect.innerHTML += `<option value="${sat.id}">${sat.name} (#${sat.id})</option>`;
        });

        window.generateReportUI = function() {
            const selectedId = reportSelect.value;
            if(!selectedId) return;
            
            const sat = SpaceShieldDB.getSatellites().find(s => s.id == selectedId);
            const historyDiv = document.getElementById('report-history');
            
            let html = `<h2 style="margin-bottom:10px;">=== INCIDENT REPORT: ${sat.name} ===</h2>`;
            html += `<p style="margin-bottom:20px;"><strong>Current Risk Level:</strong> <span class="badge ${sat.riskLevel === 'High' ? 'badge-danger' : (sat.riskLevel === 'Medium' ? 'badge-warning' : 'badge-success')}">${sat.riskLevel}</span></p>`;
            
            if (sat.events.length === 0) {
                html += `<p>Nenhum evento registrado (Acesso limpo).</p>`;
            } else {
                sat.events.forEach(e => {
                    html += `<div style="padding: 10px; border-bottom: 1px solid var(--border);">
                                <p><strong>[${e.eventType}]</strong> - ${e.timestamp}</p>
                                <p class="text-muted">${e.description}</p>
                             </div>`;
                });
            }
            historyDiv.innerHTML = html;
        };
    }
});
