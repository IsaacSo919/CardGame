package Thegame;

import java.util.Scanner;	
import java.util.ArrayList;
import java.util.List;

public class CardGame {
    public static void main(String[] args) {
    	

/////asdasdasd////
        int numberOfPlayers = 0;
        String inputPackLocation;

        try {
        	 Scanner scanner = new Scanner(System.in);

             System.out.print("Enter the number of players: ");
             numberOfPlayers = scanner.nextInt();
             
             scanner.nextLine();
             
             System.out.print("Please enter the location of the pack to load: ");
             inputPackLocation = scanner.nextLine();
             
             
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of players. Must be an integer.");
            System.exit(1);
        } 
        
        List<Player> players = new ArrayList<>();
        List<Deck> decks = new ArrayList<>();
        
         for (int i = 0; i < numberOfPlayers; i++) {
            decks.add(new Deck());
            Deck leftDeck = decks.get(i);
            Deck rightDeck = i < numberOfPlayers - 1 ? new Deck() : decks.get(0);
            players.add(new Player(i + 1, leftDeck, rightDeck));
        };
           
        


        // Continue with the rest of your program here...
    }
}
