package test;

import org.junit.Test;
import Thegame.Player;
import Thegame.Card;
import Thegame.CardGame;
import Thegame.Deck;

import javax.lang.model.type.DeclaredType;

import static org.junit.Assert.*;

public class PlayerTest {
    CardGame cardGame = new CardGame(10);
    Card card1 = new Card(1);
    Card card2 = new Card(2);
    Card card3 = new Card(3);
    Card card4 = new Card(4);
    Deck rightDeck = new Deck(2);
    Deck leftDeck = new Deck(1);
    public void setup(){
        leftDeck.addToDeck(card1);
    }
    @Test
    public void TestGetPlayerId() {

        Player player = new Player(10,leftDeck,rightDeck,cardGame);
        System.out.println(player.getPlayerId());
    }

//    @Test
//    public void TestGetHasWon() {
//
//    }
//
//    @Test
//    public void getPlayerHand() {
//    }
//
//    @Test
//    public void add_card_to_playerHand() {
//    }
//
//    @Test
//    public void drawCard() {
//    }
//
//    @Test
//    public void discardCard() {
//    }
//
//    @Test
//    public void hasWinningHand() {
//    }
//
//    @Test
//    public void declareWinning() {
//    }
//
//    @Test
//    public void declareOthers() {
//    }
}