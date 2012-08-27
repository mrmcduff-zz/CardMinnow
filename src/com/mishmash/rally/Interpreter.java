package com.mishmash.rally;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {
    public final static String ERROR_POLITE = "I can't understand what you mean by ";
    public final static char SPADES_CHAR = 's';
    public final static char HEARTS_CHAR = 'h';
    public final static char DIAMONDS_CHAR = 'd';
    public final static char CLUBS_CHAR = 'c';
    public final static String ACE_STRING = "a";
    public final static String KING_STRING = "k";
    public final static String QUEEN_STRING = "q";
    public final static String JACK_STRING = "j";
    public final static char JOKER_CHAR = 'w';
    
    public final static String[] FACE_ARRAY = { ACE_STRING, KING_STRING, QUEEN_STRING,
            JACK_STRING };
    public final static List<String> FACE_LIST = Arrays.asList( FACE_ARRAY );
    public final static char[] SUIT_ARRAY = { SPADES_CHAR, HEARTS_CHAR, DIAMONDS_CHAR, CLUBS_CHAR, JOKER_CHAR };
    
    
    public static List<Card> interpret(String line) throws IllegalArgumentException{
        List<Card> cardsWritten = new ArrayList<Card>();
        String [] tokens = tokenize(line);
        
        List<String> badTokens = getBadTokens(tokens);
        // These are the bad tokens that are simply incorrect formatting.
        if (badTokens.size() > 0) {
            throw new IllegalArgumentException(getErrorString(badTokens));
        }
        
        badTokens.clear();
        // Remember that this line can throw as well.
        cardsWritten = convertStringsToCards(tokens, badTokens);
        // These tokens have the right look, but don't make sense as cards.
        if (badTokens.size() > 0) {
            throw new IllegalArgumentException(getErrorString(badTokens));
        }
        
        return cardsWritten;
    }
    
    public static String[] tokenize(String rawInput) {
        String[] splitValues = rawInput.trim().split("\\s*(;|,|\\s)+\\s*");
        return splitValues;
    }
    
    public static List<String> getBadTokens(String[] tokenizedInput) {
        List<String> badTokens = new ArrayList<String>();
        Pattern pattern = Pattern.compile("((\\d*|[akqj])w)|((\\d+|[akqj])[shdcw])", Pattern.CASE_INSENSITIVE);
        for ( String token : tokenizedInput ) {
            Matcher matcher = pattern.matcher(token);
            if (!matcher.matches()) {
                badTokens.add(token);
            }
        }
        return badTokens;
    }
    
    public static List<Card> convertStringsToCards(String[] goodTokens, List<String> badTokens) throws IllegalArgumentException {
        List<Card> cards = new ArrayList<Card>();
        
        for (String token : goodTokens) {
            // This line can throw
            Card card = convertTokenToCard(token);
            if (card.isValid()) {
                cards.add(card);
            } else {
                badTokens.add(token);
            }
        }
        return cards;
    }
    
    public static Card convertTokenToCard(String token) throws IllegalArgumentException {
        // Right now it's a joker.
        Card answer = new Card();
        
        String littleToken = token.toLowerCase();
        char suitChar = littleToken.charAt(token.length() - 1);

        String suitString = String.copyValueOf(SUIT_ARRAY);
        if (suitString.indexOf(suitChar) < 0) {
            throw new IllegalArgumentException("'" + token + "'" +
                    " doesn't contain enough data for me to understand it. I need a value and a suit.");
        } else {
            if (suitChar != JOKER_CHAR) {
                String valuePart = littleToken.substring(0, token.length() - 1);
                int value = 0;
                
                if (FACE_LIST.contains(valuePart)) {
                    value = getFaceValue(valuePart);
                } else {
                    try {
                        value = Integer.parseInt(valuePart);
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException(token + 
                                " was an invalid token that either passed validation or was never validated.");
                    }
                }
                // This line throws if you've given me a bad suit.
                Card.Suit suit = getSuitFromChar(suitChar);
                answer = new Card(value, suit);
            } 
        }
        
        return answer;
    }
    
    private static int getFaceValue(String face) {
        int value = 0;
        if (face.equalsIgnoreCase(ACE_STRING)) {
            value = 14;
        } else if (face.equalsIgnoreCase(KING_STRING)) {
            value = 13;
        } else if (face.equalsIgnoreCase(QUEEN_STRING)) {
            value = 12;
        } else if (face.equalsIgnoreCase(JACK_STRING)) {
            value = 11;
        }
        return value;
    }
    
    private static Card.Suit getSuitFromChar(char c) {
        switch(c) {
        case SPADES_CHAR:
            return Card.Suit.SPADES;
        case HEARTS_CHAR:
            return Card.Suit.HEARTS;
        case DIAMONDS_CHAR:
            return Card.Suit.DIAMONDS;
        case CLUBS_CHAR:
            return Card.Suit.CLUBS;
        case JOKER_CHAR:
            return Card.Suit.JOKER;
        default:
            throw new IllegalArgumentException(c +
                    " is not a valid suit character and either wasn't validated or " +
                    "passed validation when it shoudln't have.");
        }
    }
    
    public static String getErrorString(List<String> badTokens) {
        StringBuilder sb = new StringBuilder();
        sb.append(ERROR_POLITE);
        int count = 0;
        for (String s : badTokens) {
            
            if (count > 0 && badTokens.size() > 2) {
                sb.append(", ");
            }
            
            if (count >= 1 && count == badTokens.size() - 1) {
                if (count == 1) {
                    sb.append(" ");
                }
                sb.append("or ");
            }
            sb.append("'");
            sb.append(s);
            sb.append("'");
            if (count == badTokens.size() - 1) {
                sb.append(".");
            }
            ++count;
        }
        return sb.toString();
    }
    

}
