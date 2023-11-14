package Thegame;

import java.io.*;
import java.util.*;
public class CardGame {
    private int numberofPlayers;


    public CardGame(int numberofPlayers){
        this.numberofPlayers = numberofPlayers;
    }


    // Function that asks how many players are in the game
    private static int askForPlayers_and_packlocation() {
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
            System.out.print("Please enter the location of the pack to load: ");
            String inputPackLocation = scanner.nextLine();
            // Check if file exists

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
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of players. Must be an integer.");
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return numberOfPlayers;
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

        int x = 6; // You can adjust the value of x as needed

        try (FileWriter fileWriter = new FileWriter(filename)) {
            int maxCardValue = 8 * numberOfPlayers / 6;
            int isThereAnyRemainder = numberOfPlayers % 6;

            // Create a list to hold the card values
            List<Integer> cardValues = new ArrayList<>();

            for (int i = 1; i <= maxCardValue; i++) {
                for (int j = 0; j < 4; j++) { // Repeat each card value four times
                    cardValues.add(i);
                }
            }

            // If there is a remainder, add additional cards with different values
            if (isThereAnyRemainder > 0) {
                for (int i = 1; i <= isThereAnyRemainder; i++) {
                    for (int j = 0; j < 4; j++) {
                        cardValues.add((maxCardValue + 1) * x);
                    }
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


    public static void main(String[] args) {
        int numberofplayers = askForPlayers_and_packlocation();
        System.out.println("Number of players in game : " + numberofplayers + " players");
        String outputfilepath = "card_pack.txt";
        generateCardPack(numberofplayers,outputfilepath);
    }
}