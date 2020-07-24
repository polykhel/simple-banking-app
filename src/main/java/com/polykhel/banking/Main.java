package com.polykhel.banking;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CardService bank = new CardService(args[1]);
        MAIN_MENU:
        do {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            int mainMenuCmd = Integer.parseInt(scanner.nextLine());
            System.out.println();

            if (mainMenuCmd == 0) {
                break;
            } else if (mainMenuCmd == 1) {
                bank.createAccount();
            } else if (mainMenuCmd == 2) {
                System.out.println("Enter your card number:");
                String cardNumber = scanner.nextLine();
                System.out.println("Enter your PIN:");
                String PIN = scanner.nextLine();

                Card loggedInCard = bank.getCardByNumberAndPin(cardNumber, PIN);
                if (loggedInCard != null) {
                    System.out.println("You have successfully logged in!");

                    ACCOUNT_MENU:
                    do {
                        System.out.println("\n1. Balance");
                        System.out.println("2. Add income");
                        System.out.println("3. Do transfer");
                        System.out.println("4. Close card");
                        System.out.println("5. Log out");
                        System.out.println("0. Exit");
                        int cmd = Integer.parseInt(scanner.nextLine());

                        switch (cmd) {
                            case 0:
                                System.out.println("\nBye!\n");
                                break MAIN_MENU;
                            case 1:
                                System.out.println("\nBalance: " + loggedInCard.getBalance());
                                break;
                            case 2:
                                try {
                                    System.out.println("\nEnter income:");
                                    long income = Long.parseLong(scanner.nextLine());
                                    bank.updateBalance(loggedInCard.getId(), loggedInCard.getBalance() + income);
                                    loggedInCard = bank.getCardByNumberAndPin(loggedInCard.getNumber(), loggedInCard.getPin());
                                    System.out.println("Income was added!");
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid income!");
                                }
                                break;
                            case 3:
                                System.out.println("\nTransfer");
                                System.out.println("Enter card number:");
                                String cardNumberToTransferTo = scanner.nextLine();
                                if (bank.verifyValidNumber(cardNumberToTransferTo)) {
                                    Card recipientCard = bank.getCardByNumber(cardNumberToTransferTo);
                                    if (recipientCard != null) {
                                        System.out.println("Enter how much money you want to transfer:");
                                        try {
                                            long transferAmount = Long.parseLong(scanner.nextLine());
                                            if (transferAmount > loggedInCard.getBalance()) {
                                                System.out.println("Not enough money");
                                            } else {
                                                bank.updateBalance(loggedInCard.getId(), -transferAmount);
                                                bank.updateBalance(recipientCard.getId(), transferAmount);
                                                loggedInCard = bank.getCardByNumberAndPin(loggedInCard.getNumber(), loggedInCard.getPin());
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid amount!");
                                        }
                                    }
                                } else {
                                    System.out.println("Probably you made mistake in the card number. Please try again!");
                                }
                                break;
                            case 4:
                                bank.closeCard(loggedInCard.getId());
                                System.out.println("Your card has been closed!");
                                break ACCOUNT_MENU;
                            case 5:
                                System.out.println("\nYou have successfully logged out!\n");
                                break ACCOUNT_MENU;
                        }
                    } while (true);
                }
            }
        } while (true);
    }
}




