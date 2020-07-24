package com.polykhel.banking;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class CardService {

    private final CardRepository repository;
    private final Random random;
    private static final String BANK_IDENTIFIER = "400000";

    public CardService(String fileName) {
        this.repository = new CardRepository(fileName);
        this.random = new Random();
    }

    public void createAccount() {
        String accountIdentifierNumber = BANK_IDENTIFIER + generateAccountIdentifier();
        String number = accountIdentifierNumber + getCheckSum(accountIdentifierNumber);
        String pin = String.valueOf(random.nextInt(10000));

        Card newCard = new Card(number, pin);
        this.repository.insertAccount(newCard);

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(newCard.getNumber());
        System.out.println("Your card PIN:");
        System.out.println(newCard.getPin());
        System.out.println();
    }

    public Card getCardByNumberAndPin(String number, String pin) {
        Optional<Card> card = this.repository.findCardByNumberAndPin(number, pin);
        if (card.isPresent()) {
            return card.get();
        } else {
            System.out.println("\nWrong card number or PIN!\n");
            return null;
        }
    }

    public Card getCardByNumber(String number) {
        Optional<Card> card = this.repository.findCardByNumber(number);
        if (card.isPresent()) {
            return card.get();
        } else {
            System.out.println("Such a card does not exist.");
            return null;
        }
    }

    public void updateBalance(int id, long amount) {
        this.repository.updateBalance(id, amount);
    }

    public void closeCard(int id) {
        this.repository.deleteCard(id);
    }

    public boolean verifyValidNumber(String creditCardNumber) {
        if (creditCardNumber.length() != 16) {
            return false;
        }

        if (!creditCardNumber.matches("^[0-9]*$")) {
            return false;
        }

        int checkSum = getCheckSum(creditCardNumber.substring(0, 15));
        return checkSum == Integer.parseInt(creditCardNumber.substring(15));
    }

    private String generateAccountIdentifier() {
        return random.ints(9, 0, 9)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    private int getCheckSum(String accountIdentifierNumber) {
        int[] digits = accountIdentifierNumber.chars()
                .map(Character::getNumericValue)
                .toArray();
        for (int i = 0; i < digits.length; i += 2) {
            digits[i] *= 2;
        }
        for (int i = 0; i < digits.length; i++) {
            if (digits[i] > 9) {
                digits[i] -= 9;
            }
        }
        int controlNumber = Arrays.stream(digits).sum();
        int checkSum = 0;
        while ((controlNumber + checkSum) % 10 != 0) {
            checkSum++;
        }
        return checkSum;
    }

}
