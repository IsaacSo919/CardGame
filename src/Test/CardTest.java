package Test;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    private Card card;

    @Before
    public void setUp() {
        card = new Card(5);
    }

    @Test
    public void testGetFaceValue() {
        assertEquals(5, card.getFaceValue());
    }
}
