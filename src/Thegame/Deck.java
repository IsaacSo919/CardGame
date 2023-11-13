package Thegame;
import java.util.LinkedList;

public class Deck {
	private final LinkedList<Card> cards;

    public Deck() {
        this.cards = new LinkedList<>();
    }

    public synchronized Card drawCard() {
        return cards.pollFirst();
    }

    public synchronized void addCard(Card card) {
        cards.addLast(card);
    }
    //comment 1
}
