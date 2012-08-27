/**
 * 
 */
package com.mishmash.rally;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.mishmash.rally.Card;

/**
 * Tests methods in the Card class.
 * @author mrmcduff
 *
 */
public class CardTest {

    /**
     * Test method for {@link com.mishmash.rally.Card#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Card c1 = new Card(5, Card.Suit.CLUBS);
        // The function should be deterministic
        assertEquals(c1.hashCode(), c1.hashCode());
        // The five of clubs should be the same as the five of clubs.
        Card c2 = new Card(5, Card.Suit.CLUBS);
        assertEquals(c1.hashCode(), c2.hashCode());
        
        Card c3 = new Card(13, Card.Suit.CLUBS);
        assertFalse(c1.hashCode() == c3.hashCode());
        
        Card j1 = new Card(7, Card.Suit.JOKER);
        // Cards default to being a joker.
        Card j2 = new Card();
        
        // All jokers are equal, and thus all jokers have the same hash code.
        assertEquals(j1.hashCode(), j2.hashCode());
    }

    /**
     * Test method for {@link com.mishmash.rally.Card#isValid()}.
     */
    @Test
    public void testIsValid() {
        // Green test.
        Card c1 = new Card(4, Card.Suit.DIAMONDS);
        assertTrue(c1.isValid());
        
        Card c2 = new Card();
        // Default card is a joker with value 0, but jokers are valid.
        assertTrue(c2.isValid());
        
        Card badCard = new Card();
        badCard.setSuit(Card.Suit.DIAMONDS);
        // You can't have a 0 of diamonds.
        // This is the only way to create a bad card.
        assertFalse(badCard.isValid());
        
    }
    
    /**
     * Test method for {@link com.mishmash.rally.Card#setValue(int)}.
     */
    @Test
    public void testSetValue() {
        //Green test
        Card c1 = new Card(5, Card.Suit.SPADES);
        // 7 is a fine value.
        assertTrue(c1.setValue(7));
        // 155 is not.
        assertFalse(c1.setValue(155));
        // Trying to set to 155 should not have changed the value of the card.
        assertEquals(c1.getValue(), 7);
    }

    /**
     * Test method for {@link com.mishmash.rally.Card#getValueString()}.
     */
    @Test
    public void testGetValueString() {
        Card c1 = new Card(11, Card.Suit.HEARTS);
        assertEquals(c1.getValueString(), "jack");
        
        Card j1 = new Card();
        assertEquals(j1.getValueString(), "joker");
        
        // A joker's value is joker, no matter what it's 'value' is.
        j1.setValue(5);
        assertEquals(j1.getValueString(), "joker");
        
        j1.setSuit(Card.Suit.HEARTS);
        // Now j1 isn't a joker any more, so it has a value.
        assertEquals(j1.getValueString(), "five");
        
        Card j2 = new Card();
        // Remember that this is the only way to create a bad card.
        j2.setSuit(Card.Suit.CLUBS);
        assertEquals(j2.getValueString(), "Invalid");
    }

    /**
     * Test method for {@link com.mishmash.rally.Card#compareTo(com.mishmash.rally.Card)}.
     */
    @Test
    public void testCompareTo() {
        Card c1 = new Card(2, Card.Suit.HEARTS);
        Card c2 = new Card(2, Card.Suit.HEARTS);
        Card c3 = new Card(2, Card.Suit.SPADES);
        Card j1 = new Card();
        Card j2 = new Card(9, Card.Suit.JOKER);
        Card c4 = new Card(13, Card.Suit.HEARTS);
        Card c5 = new Card(14, Card.Suit.SPADES);
        
        // The compareTo method only needs to return values
        // greater than, less than, or equal to zero, as opposed to
        // specific integers. Hence, I don't test for integer equality
        // except in the zero case.
        assertTrue(c1.compareTo(c1) == 0);
        assertTrue(c1.compareTo(c2) == 0);
        // Suits have order too! In ascending order, it goes:
        // clubs, diamonds, hearts, spades.
        assertTrue(c1.compareTo(c3) < 0);
        assertTrue(c1.compareTo(c4) < 0);
        assertTrue(c3.compareTo(c2) > 0);
        assertTrue(c4.compareTo(c1) > 0);
       
        
        // Recall that a joker is higher than everything,
        // even the Ace of spades, which is otherwise the highest card.
        assertTrue(c5.compareTo(j1) < 0);
        assertTrue(j1.compareTo(j1) == 0);
        
        // All jokers are equal.
        assertTrue(j1.compareTo(j2) == 0);
        assertTrue(j2.compareTo(c5) > 0);
        
        // Make sure this works for a list.
        Card[] cardArray = { c1, c4, j1, c3 };
        List<Card> cardList = Arrays.asList(cardArray);
        Card[] sortedCardArray = { c1, c3, c4, j1 };
        Collections.sort(cardList);
        assertArrayEquals(sortedCardArray, cardList.toArray());
        // Just making sure I'm sane here.
        assertFalse(cardArray.equals(sortedCardArray));
    }

    /**
     * Test method for {@link com.mishmash.rally.Card#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        Card c1 = new Card(5, Card.Suit.CLUBS);
        // The function should be deterministic
        assertEquals(c1, c1);
        
        // The five of clubs should be the same as the five of clubs.
        Card c2 = new Card(5, Card.Suit.CLUBS);
        assertEquals(c1, c2);
        
        Card c3 = new Card(13, Card.Suit.CLUBS);
        assertFalse(c1.equals(c3));
        
        Card j1 = new Card(7, Card.Suit.JOKER);
        // Cards default to being a joker.
        Card j2 = new Card();
        
        // All jokers are equal, and thus all jokers have the same hash code.
        assertEquals(j1, j2);
        // Jokers are never equal to non-jokers.
        assertFalse(j1.equals(c2));
        assertFalse(c3.equals(j2));
    }

}
