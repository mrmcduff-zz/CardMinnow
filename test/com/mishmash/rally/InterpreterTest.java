/**
 * 
 */
package com.mishmash.rally;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mishmash.rally.Card;
import com.mishmash.rally.Interpreter;

/**
 * @author mrmcduff
 *
 */
public class InterpreterTest {

    
    /**
     * Test method for {@link com.mishmash.rally.Interpreter#interpret(java.lang.String)}.
     */
    @Test
    public void testInterpret() {
        String greenString = "2h, 3s, AH, JD, JH";
        Card c1 = new Card(2, Card.Suit.HEARTS);
        Card c2 = new Card(3, Card.Suit.SPADES);
        Card c3 = new Card(14, Card.Suit.HEARTS);
        Card c4 = new Card(11, Card.Suit.DIAMONDS);
        Card c5 = new Card(11, Card.Suit.HEARTS);
        Card[] cardArray = { c1, c2, c3, c4, c5 };
        assertArrayEquals(cardArray, Interpreter.interpret(greenString).toArray());
        
        String badString = "This shouldn't pass";
        boolean thrown = false;
        try {
            Interpreter.interpret(badString);
        } catch (IllegalArgumentException iae) {
            thrown = true;
        }
        assertTrue(thrown);
        
        // Remember that nothing says an interpreter needs to return a list of
        // VALID cards, just a list of cards.
        String weirdString = "w W 0h";
        Card j1 = new Card();
        Card i1 = new Card();
        i1.setSuit(Card.Suit.HEARTS);
        Card[] weirdArray = { j1, j1, i1 };
        assertArrayEquals(weirdArray, Interpreter.interpret(weirdString).toArray());

    }

    /**
     * Test method for {@link com.mishmash.rally.Interpreter#tokenize(java.lang.String)}.
     */
    @Test
    public void testTokenize() {
        String raw1 = "   a b   \t   \n    c";
        String raw2 = "I am the very model of a modern major general";
        String raw3 = "One,Two,\t Three";
        String raw4 = "User,, hits , , commas  too; much;;and semicolons";
        
        String [] split1 = { "a", "b", "c" };
        String [] split2 = {"I", "am", "the", "very", "model", "of", "a", "modern", "major", "general"};
        String [] split3 = { "One", "Two", "Three" };
        String [] split4 = { "User", "hits", "commas", "too", "much", "and", "semicolons"};
        
        assertArrayEquals(split1, Interpreter.tokenize(raw1));
        assertArrayEquals(split2, Interpreter.tokenize(raw2));
        assertArrayEquals(split3, Interpreter.tokenize(raw3));
        assertArrayEquals(split4, Interpreter.tokenize(raw4));

    }

    /**
     * Test method for {@link com.mishmash.rally.Interpreter#getBadTokens(java.lang.String[])}.
     */
    @Test
    public void testGetBadTokens() {
        String[] goodTokens = { "155h", "0s", "5H", "w", "W", "11c", "ad", "As", "jC", "kH", "qd" };
        String[] badTokens = { "Huh?", "kk10", "ahjs", "2h2", "6hh" };
        String[] goodAndBad = { "10s", "badness", "jd", "w", "what?" };
        
        String[] badFromMixed = { "badness", "what?" };
        List<String> emptyList = new ArrayList<String>();
        assertEquals(emptyList, Interpreter.getBadTokens(goodTokens));
        assertArrayEquals(badTokens, Interpreter.getBadTokens(badTokens).toArray());
        assertArrayEquals(badFromMixed, Interpreter.getBadTokens(goodAndBad).toArray());
    }

    /**
     * Test method for {@link com.mishmash.rally.Interpreter#convertTokenToCard(java.lang.String)}.
     */
    @Test
    public void testConvertTokenToCard() {
        String[] goodTokens = { "155h", "0s", "5H", "44w", "W", "11c", "As", "jC", "kH", "qd" };
        Card c1 = new Card();
        c1.setSuit(Card.Suit.HEARTS);
        Card c2 = new Card();
        c2.setSuit(Card.Suit.SPADES);
        Card c3 = new Card(5, Card.Suit.HEARTS);
        Card c4 = new Card();
        Card c5 = new Card();
        Card c6 = new Card(11, Card.Suit.CLUBS);
        Card c7 = new Card(14, Card.Suit.SPADES);
        Card c8 = new Card(11, Card.Suit.CLUBS);
        Card c9 = new Card(13, Card.Suit.HEARTS);
        Card c10 = new Card(12, Card.Suit.DIAMONDS);
        Card[] cards = {c1, c2, c3, c4, c5, c6, c7, c8, c9, c10 };
        
        for (int i  = 0; i < goodTokens.length; ++i) {
            assertEquals(cards[i], Interpreter.convertTokenToCard(goodTokens[i]));
        }
        
        String [] badTokens = { "3h3", "x", "7x", "hh" };
        for ( String bt : badTokens ) {
            try {
                Interpreter.convertTokenToCard(bt);
                fail("Failed to throw an exception when expected");
            } catch (IllegalArgumentException iae) {
                assertTrue("Threw the right type of exception", true);
            }
        }

    }
    
    /**
     * Test method for {@link com.mishmash.rally.Interpreter#getErrorString(java.lang.String[])}.
     */    
    @Test
    public void testGetErrorString() {
        String[] badTokens = { "Huh?", "kk10", "ahjs", "2h2", "6hh" };
        String handFormattedError = Interpreter.ERROR_POLITE + "Huh?, kk10, ahjs, 2h2, or 6hh";
        
        String[] bt2 = { "xxx", "y" };
        String hfe2 = Interpreter.ERROR_POLITE + "xxx or y";
        assertEquals(handFormattedError, Interpreter.getErrorString(Arrays.asList(badTokens)));
        assertEquals(hfe2, Interpreter.getErrorString(Arrays.asList(bt2)));
    }

}
