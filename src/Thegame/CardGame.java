package Thegame;

import java.io.*;
import java.util.*;

public class CardGame {
    private int numberofPlayers;


    public CardGame(int numberofPlayers){
        this.numberofPlayers = numberofPlayers;
    }


    // Function that asks how many players are in the game
    private static int askForPlayers() {
        int numberOfPlayers = 0; // Initialize to a default value

        try {
            // New Scanner input
            Scanner scanner = new Scanner(System.in);

            // Asking user for number of players
            System.out.print("Enter the number of players: \n");
            numberOfPlayers = scanner.nextInt();
            // Clear the newline character from the buffer
            scanner.nextLine();

            // Asking users for the location of a valid plain input_text file
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of players. Must be an integer.");
            System.exit(1);
        } 
        return numberOfPlayers;
    }
    
    private static String askForPacklocation(int numberOfPlayers) {
    	
    	
    	Scanner scanner = new Scanner(System.in);
    	System.out.print("Please enter the location of the pack to load: ");
        String inputPackLocation = scanner.nextLine();
        // Check if file exists
	        try {
	        File file = new File(inputPackLocation);
	        if(!file.exists()){
	            System.out.println("File does not exist. Generating a valid card pack...");
	            generateCardPack(numberOfPlayers,inputPackLocation);
	        }else{
	            System.out.println("Checking if input-pack is valid ");
	            ispackvalid(numberOfPlayers,inputPackLocation);
	        }
	        // Close the scanner when you are done using it
	        scanner.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return (inputPackLocation);
    }
    private static boolean ispackvalid(int numberofPlayers, String inputpacklocation) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(inputpacklocation))){
            // row count must be equal to 8*n;
            int rowcount = 0;
            String line;
            while((line = reader.readLine()) != null){
                try{
                    int cardValue = Integer.parseInt(line.trim());
                    if(cardValue < 0){
                        System.out.println("Invalid card value: " + cardValue);
                    }
                } catch(NumberFormatException e){
                    System.out.println("Invalid format. Each row should contain a non-negative integer value.");
                    return false;
                }
                rowcount++;
            }
            // Check if the total number of rows is 8n
            if(rowcount != 8 * numberofPlayers){
                System.out.println("Invalid number of rows. The file should have 8n rows");
                return false;
            }
            return true;
        }
    }

    private static void generateCardPack(int numberOfPlayers, String filename) {
        // Checks whether there is a valid number of players
        if (numberOfPlayers <= 0) {
            System.out.println("Invalid number of players. Please enter a valid number of players");
            return;
        }

        int maxCardValue = numberOfPlayers + 1; // Maximum face value is the number of players
        int totalCards = 8 * numberOfPlayers; // Total number of cards
        int remainingCards = totalCards % maxCardValue; // Calculate remaining cards

        try (FileWriter fileWriter = new FileWriter(filename)) {
            // Create a list to hold the card values
            List<Integer> cardValues = new ArrayList<>();

            // Generate cards with the specified distribution of face values
            for (int i = 1; i <= maxCardValue; i++) {
                int cardCount = totalCards / maxCardValue;
                if (remainingCards > 0 && i <= remainingCards) {
                    cardCount++;
                }
                for (int j = 0; j < cardCount; j++) {
                    cardValues.add(i);
                }
            }

            // Shuffle the list of card values
            Collections.shuffle(cardValues);

            // Write shuffled values to the file
            for (Integer cardValue : cardValues) {
                fileWriter.write(cardValue + "\n"); // Each row contains a single non-negative integer value
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void initialize_a_game(int numberOfPlayers, String filename) {
    	
    	List<Card> deck = new ArrayList<>();
    	try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
    	    String line;
    	    while ((line = br.readLine()) != null) {
    	        int value = Integer.parseInt(line.trim());
    	        deck.add(new Card(value));  
    	        
    	    }
    	    for (int i = 0; i < deck.size(); i++) {
    	        Card card = deck.get(i);
    	        System.out.println(card.getValue());
    	    }
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    	
    }


    public static void main(String[] args) {
        int numberofplayers = askForPlayers();
        String location = askForPacklocation(numberofplayers);
//        System.out.println("location:"+location);// test for location returned
        System.out.println("Number of players in game : " + numberofplayers + " players");
        String outputfilepath = "card_pack.txt";
        generateCardPack(numberofplayers,outputfilepath);
        
        initialize_a_game(numberofplayers,location);
    }
}