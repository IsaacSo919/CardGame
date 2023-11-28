package Thegame;
import org.junit.Before;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import java.io.*;

public class CardGameTest {

    private CardGame cardGame;

    @Before
    public void setUp() {
        int numberOfPlayers = 4; // Set the number of players for testing
        cardGame = new CardGame(numberOfPlayers);
    }

    @Test
    public void testInitializeGame() {
        assertNotNull(cardGame.getPlayers());
        assertNotNull(cardGame.getDecks());
        assertEquals(4, cardGame.getPlayers().size());
        assertEquals(4, cardGame.getDecks().size());
    }

    @Test
    public void testGenerateCardPack() {
        int numberOfPlayers = 3;
        String filename = "test_card_pack.txt";
        CardGame.generateCardPack(numberOfPlayers, filename);

        // Check if the file was created
        assertTrue(new File(filename).exists());

        // Clean up the test file after the test
        File testFile = new File(filename);
        testFile.delete();
    }

    // Add more test methods to cover other functionality of the CardGame class.
}
