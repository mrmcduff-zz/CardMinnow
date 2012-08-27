/**
 * 
 */
package com.mishmash.rally;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import com.mishmash.rally.Card;
import com.mishmash.rally.Hand;

/**
 * Tests some commonly used methods from the Hand class. The most interesting stuff goes on
 * in HandEvaluator, so that test class is considerably larger, more thorough, and more complicated.
 * HandEvaluator is also where a lot of the testing of smaller stuff in Hand will happen by default.
 * 
 * @author mrmcduff
 *
 */
public class HandTest {

    /**
     * Test method for {@link com.mishmash.rally.Hand#isValid()}.
     */
    @Test
    public void testIsValid() {
        Hand h1 = new Hand();
        assertFalse(h1.isValid());
        
        // Normal cards.
        Card c1 = new Card(5, Card.Suit.HEARTS);
        // Both c2 and c3 are the Ace of Spades.
        Card c2 = new Card(14, Card.Suit.SPADES);
        Card c3 = new Card(14, Card.Suit.SPADES);
        Card c4 = new Card(2, Card.Suit.DIAMONDS);
        Card c5 = new Card(11, Card.Suit.CLUBS);
        Card c6 = new Card(9, Card.Suit.DIAMONDS);
        Card c7 = new Card(3, Card.Suit.SPADES);
        
        Card[] goodArray = {c1, c2, c4, c5, c6};
        Card[] badArray = {c1, c2, c3, c4, c5};
        Card[] bigArray = {c1, c2, c4, c5, c6, c7};
        Card[] smallArray = {c1};
        
        Hand h2 = new Hand(Arrays.asList(goodArray));
        Hand h3 = new Hand(Arrays.asList(badArray));
        Hand h4 = new Hand(Arrays.asList(bigArray));
        Hand h5 = new Hand(Arrays.asList(smallArray));
        // This is the simple true case
        assertTrue(h2.isValid());
        // The only way to be invalid with these cards is to have two of the same
        assertFalse(h3.isValid());
        // There's nothing wrong with having lots of cards.
        // Nobody said we were playing five card draw.
        assertTrue(h4.isValid());
        // There's nothing wrong with having a small hand
        assertTrue(h5.isValid());
        
        
        // Invalid cards
        Card i1 = new Card(30, Card.Suit.SPADES);
        Card i2 = new Card();
        i2.setSuit(Card.Suit.CLUBS);
        
        Card[] smallBad = {i1};
        Card[] manyBad = {i1, i2};
        Card[] badAndGood = {c1, c2, c5, c4, c7, i1};
        
        Hand h6 = new Hand(Arrays.asList(smallBad));
        Hand h7 = new Hand(Arrays.asList(manyBad));
        Hand h8 = new Hand(Arrays.asList(badAndGood));
        
        assertFalse(h6.isValid());
        assertFalse(h7.isValid());
        assertFalse(h8.isValid());
        
        // Jokers
        Card j1 = new Card();
        Card j2 = new Card(5, Card.Suit.JOKER);
        Card[] okayJokers = {j1, c1};
        Hand h9 = new Hand(Arrays.asList(okayJokers));
        Card[] badJokers = {j1, j2};
        Hand h10 = new Hand( badJokers );
        assertTrue(h9.isValid());
        assertFalse(h10.isValid());
        
    }

    /**
     * Test method for {@link com.mishmash.rally.Hand#getHandSize()}.
     */
    @Test
    public void testGetHandSize() {
        Hand h1 = new Hand();
        assertEquals(h1.getHandSize(), 0);
        
        List<Card> myCards = new ArrayList<Card>();
        Hand h2 = new Hand(myCards);
        assertEquals(h2.getHandSize(), 0);
        
        myCards.add(new Card());
        assertEquals(h2.getHandSize(), 1);
        myCards.add(new Card());
        assertEquals(h2.getHandSize(), 2);
    }

}
