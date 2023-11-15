package Thegame;
import java.util.ArrayList;
import java.util.List;

public class Deck {
	  private List<Card> cards;

	  public Deck() {
	      this.cards = new ArrayList<>();
	  }

	  public Card drawCard() {
	      return cards.remove(0);
	  }

	  public void addCard(Card card) {
	      cards.add(card);
	  }

	  public int size() {
	      return cards.size();
	  }
	  public List<Card> getCards() {
		    return new ArrayList<>(cards);
		}
	  //comment 1 test source tree
	}
