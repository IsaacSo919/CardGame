package Thegame;
import org.junit.Before;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeckTest {

    private Deck deck;

    @Before
    public void setUp() {
        deck = new Deck(1);
    }

    @Test
    public void testAddToDeck() {
        Card card = new Card(5);
        deck.addToDeck(card);
        assertEquals(1, deck.getDecksSize());
    }

    @Test
    public void testGetValueTopCard() {
        Card card = new Card(7);
        deck.addToDeck(card);
        assertEquals(7, deck.GetValueTopCard());
    }

    @Test
    public void testDrawCardTopCard() {
        Card card1 = new Card(3);
        Card card2 = new Card(8);
        deck.addToDeck(card1);
        deck.addToDeck(card2);
        Card drawnCard = deck.drawCardTopCard();
        assertEquals(2, deck.getDecksSize()); // After drawing, there should be one card left in the deck
        assertEquals(3, drawnCard.getFaceValue()); // The drawn card should have a face value of 3
    }

    @Test
    public void testGetDeckContentInString() {
        Card card1 = new Card(2);
        Card card2 = new Card(4);
        deck.addToDeck(card1);
        deck.addToDeck(card2);
        StringBuilder expected = new StringBuilder("2 4 ");
        assertEquals(expected.toString(), deck.getDeckContentInString().toString());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(deck.isEmpty());
        Card card = new Card(6);
        deck.addToDeck(card);
        assertFalse(deck.isEmpty());
    }

    @Test
    public void testGetDecksSize() {
        assertEquals(0, deck.getDecksSize());
        Card card1 = new Card(1);
        Card card2 = new Card(5);
        deck.addToDeck(card1);
        deck.addToDeck(card2);
        assertEquals(2, deck.getDecksSize());
    }
}
