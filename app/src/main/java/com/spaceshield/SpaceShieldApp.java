package com.spaceshield;

import com.spaceshield.model.*;
import com.spaceshield.service.*;
import java.util.Scanner;

public class SpaceShieldApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AuthService authService = new AuthService();
        Monitoring system = new Monitoring();

        User admin = authService.registerUser("Admin FIAP", "admin@spaceshield.com", "1234");

        system.registerSatellite(new Satellite(1, admin.getId(), "Starlink-BR1", "Internet"));
        system.registerSatellite(new Satellite(2, admin.getId(), "GeoSync-Defense", "Militar"));

        boolean rodando = true;
        User usuarioAtual = null;

        System.out.println("==================================================");
        System.out.println("      BEM-VINDO AO TERMINAL SPACESHIELD           ");
        System.out.println("==================================================");

        while (usuarioAtual == null && rodando) {
            System.out.println("\n[1] Fazer Login");
            System.out.println("[2] Sair do Sistema");
            System.out.println("[3] Criar Conta");
            System.out.print("Escolha uma opção: ");
            String opcaoAuth = scanner.nextLine();

            if (opcaoAuth.equals("1")) {
                System.out.print("Email (dica: admin@spaceshield.com): ");
                String email = scanner.nextLine();
                System.out.print("Senha (dica: 1234): ");
                String senha = scanner.nextLine();

                usuarioAtual = authService.login(email, senha); // Puxa o objeto!

                if (usuarioAtual != null) {
                    System.out.println("\n>>> ACESSO CONCEDIDO. Bem-vindo, " + usuarioAtual.getName() + ".");
                } else {
                    System.out.println("\n[!] ACESSO NEGADO. Credenciais incorretas.");
                }
            } else if (opcaoAuth.equals("2")) {
                rodando = false;
                System.out.println("Encerrando sistema...");
            } else if (opcaoAuth.equals("3")) {
                System.out.println("\n>>> CADASTRO DE NOVO USUÁRIO:");
                System.out.print("Nome completo: ");
                String novoNome = scanner.nextLine();
                System.out.print("Email: ");
                String novoEmail = scanner.nextLine();
                System.out.print("Senha: ");
                String novaSenha = scanner.nextLine();

                User userCriado = authService.registerUser(novoNome, novoEmail, novaSenha);
                
                if (userCriado != null) {
                    System.out.println("[+] Conta criada com sucesso! Você já pode fazer login.");
                } else {
                    System.out.println("[!] Falha ao criar conta.");
                }
            } else {
                System.out.println("Opção inválida.");
            }
        }

        while (usuarioAtual != null && rodando) {
            System.out.println("\n================ DASHBOARD PRINCIPAL ================");
            System.out.println("[1] Ver Satélites Monitorados");
            System.out.println("[2] Cadastrar Novo Satélite");
            System.out.println("[3] Simular Tentativa de Invasão (Gerar Evento)");
            System.out.println("[4] Ver Alertas de Segurança");
            System.out.println("[5] Gerar Relatório de um Satélite");
            System.out.println("[0] Logout / Sair");
            System.out.print("Comando: ");
            String opcaoMenu = scanner.nextLine();

            switch (opcaoMenu) {
                case "1":
                    System.out.println("\n>>> SATÉLITES ATIVOS:");
                    if (system.getSatellites().isEmpty()) {
                        System.out.println("Nenhum satélite cadastrado no sistema.");
                    } else {
                        for (Satellite s : system.getSatellites()) {
                            String propriedade = (s.getOwnerId() == usuarioAtual.getId()) ? " (Seu Satélite)" : "";
                            System.out.println("- ID: " + s.getId() + " | Nome: " + s.getName() + " | Função: " + s.getFunction() + " | Risco: " + s.getRiskLevel() + propriedade);
                        }
                    }
                    break;

                case "2":
                    System.out.println("\n>>> CADASTRO DE SATÉLITE:");
                    try {
                        System.out.print("ID do Satélite (Número inteiro): ");
                        int idSat = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nome (Ex: Starlink-X): ");
                        String nomeSat = scanner.nextLine();
                        System.out.print("Função (Ex: GPS, Defesa): ");
                        String funcSat = scanner.nextLine();

                        // O Satélite agora é criado atrelado ao ID do usuário atual!
                        system.registerSatellite(new Satellite(idSat, usuarioAtual.getId(), nomeSat, funcSat));
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Erro: O ID deve ser um número inteiro.");
                    }
                    break;

                case "3":
                    System.out.println("\n>>> SIMULAR EVENTO DE SEGURANÇA:");
                    try {
                        System.out.print("ID do Satélite Alvo: ");
                        int alvoId = Integer.parseInt(scanner.nextLine());
                        Satellite alvo = system.findSatellite(alvoId);

                        if (alvo != null) {
                            System.out.print("Tipo do Evento (Ex: Invasão, Falha de Autenticação): ");
                            String tipoEvento = scanner.nextLine();
                            System.out.print("Descrição técnica: ");
                            String descEvento = scanner.nextLine();

                            AccessEvent novoEvento = new AccessEvent((int)(Math.random()*1000), alvo, tipoEvento, descEvento);
                            system.logEvent(novoEvento);
                        } else {
                            System.out.println("[!] Satélite não encontrado na base de dados.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Erro: O ID deve ser um número inteiro.");
                    }
                    break;

                case "4":
                    System.out.println("\n>>> ALERTAS CRÍTICOS:");
                    if (system.getActiveAlerts().isEmpty()) {
                        System.out.println("Nenhum alerta ativo. O espaço está seguro.");
                    } else {
                        for (SecurityAlert alert : system.getActiveAlerts()) {
                            System.out.println(alert.toString());
                        }
                    }
                    break;

                case "5":
                    System.out.print("\nDigite o ID do Satélite para ver o histórico: ");
                    try {
                        int relatorioId = Integer.parseInt(scanner.nextLine());
                        Satellite satRelatorio = system.findSatellite(relatorioId);
                        if (satRelatorio != null) {
                            Report.generateIncidentHistory(satRelatorio);
                        } else {
                            System.out.println("[!] Satélite não encontrado.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[!] Erro: O ID deve ser um número inteiro.");
                    }
                    break;

                case "0":
                    System.out.println("\nDesconectando do servidor SpaceShield...");
                    usuarioAtual = null;
                    break;

                default:
                    System.out.println("\n[!] Comando não reconhecido.");
                    break;
            }
        }

        scanner.close();
        System.out.println("Processo finalizado.");
    }
}
