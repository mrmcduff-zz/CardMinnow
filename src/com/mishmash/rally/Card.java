package com.mishmash.rally;

public class Card implements Comparable<Card>{
    
    public enum Suit {
        CLUBS(1),
        DIAMONDS(2),
        HEARTS(3), 
        SPADES(4), 
        JOKER(5);
        
        private int suitValue;
        private Suit(int suitValue) {
            this.suitValue = suitValue;
        }
        
        public int getSuitValue() {
            return this.suitValue;
        }
        
        @Override
        public String toString() {
            switch(this) {
            case CLUBS:
                return "clubs";
            case DIAMONDS:
                return "diamonds";
            case HEARTS:
                return "hearts";
            case SPADES:
                return "spades";
            case JOKER:
                return "joker";
            default:
                return "invalid";
            }
        }
    }
    
    public static final String TWO_STRING = "deuce";
    public static final String THREE_STRING = "three";
    public static final String FOUR_STRING = "four";
    public static final String FIVE_STRING = "five";
    public static final String SIX_STRING = "six";
    public static final String SEVEN_STRING = "seven";
    public static final String EIGHT_STRING = "eight";
    public static final String NINE_STRING = "nine";
    public static final String TEN_STRING = "ten";
    public static final String JACK_STRING = "jack";
    public static final String QUEEN_STRING = "queen";
    public static final String KING_STRING = "king";
    public static final String ACE_STRING = "ace";
    
    public static final int MIN_CARD_VALUE = 2;
    public static final int MAX_CARD_VALUE = 14;
    
    private int value = 0;
    private Suit suit = Suit.JOKER;
    
    public Card() {
        
    }
    
    public Card(int value, Suit suit) {
        setValue(value);
        setSuit(suit);
    }
    
    public int getValue () {
        return this.value;
    }
    
    public Suit getSuit () {
        return this.suit;
    }
    
    public boolean setValue(int value) {
        if (value >= MIN_CARD_VALUE && value <= MAX_CARD_VALUE) {
            this.value = value;
            return true;
        } else {
            return false;
        }
    }
    
    public void setSuit(Suit suit) {
        this.suit = suit;
    }
    
    public boolean isValid() {
        if (this.suit == Suit.JOKER) {
            return true;
        } else if ( this.value >= MIN_CARD_VALUE && this.value <= MAX_CARD_VALUE ) {
            return true;
        } else {
            return false;
        }        
    }
    
    public String getValueString() {
        if (this.isValid()) {
            if (this.suit == Suit.JOKER) {
                return this.suit.toString();
            }
            switch(this.value) {
            case 2:
                return TWO_STRING;
            case 3:
                return THREE_STRING;
            case 4:
                return FOUR_STRING;
            case 5:
                return FIVE_STRING;
            case 6:
                return SIX_STRING;
            case 7:
                return SEVEN_STRING;
            case 8:
                return EIGHT_STRING;
            case 9:
                return NINE_STRING;
            case 10:
                return TEN_STRING;
            case 11:
                return JACK_STRING;
            case 12:
                return QUEEN_STRING;
            case 13:
                return KING_STRING;
            case 14:
                return ACE_STRING;
            }
        } else {
            return "Invalid";
        }
        return "";
    }

    @Override
    public int compareTo(Card other) {
        if (this.getSuit() == Suit.JOKER) {
            if (other.getSuit() == Suit.JOKER) { 
                return 0;
            } else {
                // For sorting purposes, the joker is the highest card.
                return 1;
            }
        } else if (other.getSuit() == Suit.JOKER) {
            // We've already taken care of the case where THIS is the joker, so 
            // we know THIS is less than OTHER
            return -1;
        } else {
            // a positive number if THIS is a higher card, and 
            // a negative number if THIS is a lower card.
            int valueDifference = this.getValue() - other.getValue();
            if (valueDifference != 0) {
                return valueDifference;
            } else {
                // There is technically an ordering on suits. The main reason I'm making a 
                // difference in compareTo is so that it plays well with sorting and equals.
                return this.getSuit().getSuitValue() - other.getSuit().getSuitValue();
            }
        }
    }
    
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } 
        
        if (that instanceof Card) {
            Card thatCard = (Card) that;
            if (this.getSuit() == Suit.JOKER) {
                // All jokers are equal.
                return thatCard.getSuit() == Suit.JOKER;
            } else {
                return (this.getValue() == thatCard.getValue())
                        && (this.getSuit() == thatCard.getSuit());
            }
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        final int goodEnoughForHorseshoesPrime = 37;
        if (this.suit == Suit.JOKER) {
            return Suit.JOKER.hashCode();
        } else {
            return this.suit.hashCode() + goodEnoughForHorseshoesPrime * this.value;
        }
    }
    
    @Override
    public String toString() {
        return getValueString() + " " + getSuit().toString();
    }
    
    
    
    
    

}
