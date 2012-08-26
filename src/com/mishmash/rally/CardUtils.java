package com.mishmash.rally;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Hashtable;

public class CardUtils {
    
    public static String capitalizeWord(String originalWord) {
        // Using plus-equals on strings is usually bad, but 
        // it just felt like overkill to use a StringBuilder for this.
        String answer = "";
        if (originalWord.length() >= 1) {
            answer += Character.toUpperCase(originalWord.charAt(0));
            if (originalWord.length() > 1) {
                answer += originalWord.substring(1);
            }
        }
        return answer;
    }
    
    public static List<Card> getWholeDeck() {
        List<Card> deck = new ArrayList<Card>();
        deck.addAll(getSuit(Card.Suit.SPADES));
        deck.addAll(getSuit(Card.Suit.HEARTS));
        deck.addAll(getSuit(Card.Suit.DIAMONDS));
        deck.addAll(getSuit(Card.Suit.CLUBS));
        Collections.sort(deck, Collections.reverseOrder());
        return deck;
    }
    
    public static List<Card> getSuit(Card.Suit suit) {
        List<Card> wholeSuit = new ArrayList<Card>();
        
        if (suit != Card.Suit.JOKER) {
            Card a = new Card(14, suit);
            Card k = new Card(13, suit);
            Card q = new Card(12, suit);
            Card j = new Card(11, suit);
            Card ten = new Card(10, suit);
            Card nine = new Card(9, suit);
            Card eight = new Card(8, suit);
            Card seven = new Card(7, suit);
            Card six = new Card(6, suit);
            Card five = new Card(5, suit);
            Card four = new Card(4, suit);
            Card three = new Card(3, suit);
            Card two = new Card(2, suit);
            
            Card [] suitArray = { a, k, q, j, ten, nine,
                    eight, seven, six, five, four, three, 
                    two };
            
            wholeSuit = Arrays.asList(suitArray);
        } else {
            // Just give it a joker.
            wholeSuit.add(new Card());
        }
        return wholeSuit;
    }
    
    public static List<Card> getTopNCards(List<Card> entryList, int n) {
        List<Card> retVal = new ArrayList<Card>();
        if (entryList != null && entryList.size() >= n) {
            retVal = entryList.subList(0, n);
        }
        return retVal;
    }
    

}
