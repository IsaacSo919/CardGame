package Test;
import org.junit.Before;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;
    private Deck leftDeck;
    private Deck rightDeck;
    private CardGame game;

    @Before
    public void setUp() {
       leftDeck = new Deck(1);
       rightDeck = new Deck(2);
        game = new CardGame(10);
        player = new Player(1, leftDeck, rightDeck, game);
    }

    @Test
    public void testAddCardToPlayerHand() {
        Card card = new Card(5); // Create a card with face value 5
        player.add_card_to_playerHand(card);
        ArrayList<Card> playerHand = player.getPlayerHand();
        assertTrue(playerHand.contains(card));
    }

    @Test
    public void testHasWinningHand() {
        // Create a winning hand (all cards with the same face value)
        Card card1 = new Card(5);
        Card card2 = new Card(5);
        Card card3 = new Card(5);
        player.add_card_to_playerHand(card1);
        player.add_card_to_playerHand(card2);
        player.add_card_to_playerHand(card3);

        assertTrue(player.hasWinningHand());
    }

    @Test
    public void testHasNonWinningHand() {
        // Create a non-winning hand (cards with different face values)
        Card card1 = new Card(5);
        Card card2 = new Card(3);
        Card card3 = new Card(5);
        player.add_card_to_playerHand(card1);
        player.add_card_to_playerHand(card2);
        player.add_card_to_playerHand(card3);

        assertFalse(player.hasWinningHand());
    }

    // Add more test methods to cover other functionality of the Player class.
}
