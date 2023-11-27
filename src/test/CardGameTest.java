package test;

import Thegame.CardGame;
import Thegame.Player;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class CardGameTest {
    CardGame cardGame = new CardGame(10);
//    @Test
//    public void TestGetPlayers() {
//        assert(cardGame.getPlayers();)
//    }
//
//    @Test
//    public void getDecks() {
//    }
//
    @Test
    public void TestCreatePlayers() {
        CardGame game = new CardGame(10);
        assertTrue(game.getPlayers().isEmpty()); //is valid if there are no players in the game
        game.createPlayers();
        assertFalse(game.getPlayers().isEmpty()); //is valid if there are players in the game

    }
//
//    @Test
//    public void distribute_card_to_playerHand() {
//    }
//
//    @Test
//    public void distribute_card_to_deck() {
//    }
//    public void shouldInitialize_a_game(){
//
//    }

}