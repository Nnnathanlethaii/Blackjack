import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BlackjackGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int gameScore = 100;
        final int ROUNDS = 5;

        System.out.println("Welcome to the 5-Round Blackjack-Style Game!");
        System.out.println("You start with a game score of " + gameScore + ".");
        System.out.println();

        for (int round = 1; round <= ROUNDS; round++) {
            System.out.println("=== Round " + round + " ===");
            Deck deck = new Deck();          // fresh deck each round
            deck.shuffle();

            int roundScore = 0;
            // initial hand: draw two cards
            System.out.println("Drawing your initial two cards...");
            for (int i = 0; i < 2; i++) {
                Card card = deck.drawCard();
                roundScore += card.getValue();
                System.out.println("  You drew: " + card);
            }
            System.out.println("Your round score is: " + roundScore);

            // allow hits
            while (true) {
                if (roundScore > 21) {
                    System.out.println("Bust! Your round score (" + roundScore + ") exceeds 21.");
                    break;
                }
                System.out.print("Do you want to (H)it or (S)tay? ");
                String choice = scanner.nextLine().trim().toUpperCase();
                if (choice.equals("H")) {
                    Card card = deck.drawCard();
                    roundScore += card.getValue();
                    System.out.println("  You drew: " + card);
                    System.out.println("  New round score: " + roundScore);
                } else if (choice.equals("S")) {
                    System.out.println("You chose to stay at " + roundScore + ".");
                    break;
                } else {
                    System.out.println("Invalid input. Please enter H or S.");
                }
            }

            // adjust game score
            if (roundScore > 21) {
                System.out.println("Since you busted, 21 points will be subtracted from your game score.");
                gameScore -= 21;
            } else {
                int diff = Math.abs(21 - roundScore);
                System.out.println("Difference from 21: " + diff + ". Subtracting from game score...");
                gameScore -= diff;
            }
            System.out.println("End of Round " + round + ". Your total game score is now: " + gameScore);
            System.out.println();
        }

        // final ranking
        System.out.println("=== Game Over ===");
        System.out.println("Your final game score: " + gameScore);
        char rank;
        if (gameScore >= 90)       rank = 'A';
        else if (gameScore >= 80)  rank = 'B';
        else if (gameScore >= 70)  rank = 'C';
        else if (gameScore >= 60)  rank = 'D';
        else                        rank = 'F';
        System.out.println("Your rank: " + rank);

        System.out.println("Thanks for playing!");
        scanner.close();
    }

    // --- Helper classes: Card and Deck ---

    static class Card {
        private final String rank;
        private final String suit;
        private final int value;

        public Card(String rank, String suit, int value) {
            this.rank = rank;
            this.suit = suit;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return rank + " of " + suit + " (value: " + value + ")";
        }
    }

    static class Deck {
        private final List<Card> cards = new ArrayList<>();
        private final Random rand = new Random();

        private static final String[] SUITS = { "Hearts", "Diamonds", "Clubs", "Spades" };
        private static final String[] RANKS = {
            "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"
        };

        public Deck() {
            // create 4 of each rank (one per suit)
            for (String suit : SUITS) {
                for (int i = 0; i < RANKS.length; i++) {
                    String rank = RANKS[i];
                    int value;
                    if (i == 0)           value = 1;   // Ace
                    else if (i >= 10)     value = 10;  // Face cards
                    else                  value = i + 1; // 2–10
                    cards.add(new Card(rank, suit, value));
                }
            }
        }

        public void shuffle() {
            // simple Fisher–Yates
            for (int i = cards.size() - 1; i > 0; i--) {
                int j = rand.nextInt(i + 1);
                Card tmp = cards.get(i);
                cards.set(i, cards.get(j));
                cards.set(j, tmp);
            }
        }

        public Card drawCard() {
            if (cards.isEmpty()) {
                throw new IllegalStateException("No cards left in the deck");
            }
            // remove and return a random remaining card
            return cards.remove(rand.nextInt(cards.size()));
        }
    }
}
