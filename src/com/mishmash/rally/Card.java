package com.mishmash.rally;

/**
 * Our basic item in a card game, and the building blocks of our hands.
 * A card has a numeric value and a suit, and can verify that it is valid.
 * 
 * @author mrmcduff
 *
 */
public class Card implements Comparable<Card>{
    
    /**
     * This enumeration describes a suit. It contains methods for getting
     * the 'value' of a suit, which creates an ordering. It also overrides
     * the toString method so you can print your suit.
     * 
     * @author mrmcduff
     * 
     */
    public enum Suit {
        CLUBS(1),
        DIAMONDS(2),
        HEARTS(3), 
        SPADES(4), 
        JOKER(5);
        
        private int suitValue;
        
        /**
         * Private constructor for the enum.
         * 
         * @param suitValue Sets the suit value.
         */
        private Suit(int suitValue) {
            this.suitValue = suitValue;
        }
        
        /**
         * Getter for suit value.
         * @return the suitValue field.
         */
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
    
    // There is no card lower than a two
    public static final int MIN_CARD_VALUE = 2;
    // There is no card higher than an ace
    public static final int MAX_CARD_VALUE = 14;
    
    // The numeric value of the card.
    private int value = 0;
    
    // The suit of the card.
    private Suit suit = Suit.JOKER;
    
    /**
     * No-arg constructor for a card. Useful as a quick way to
     * create a joker.
     */
    public Card() {}
    
    /**
     * Standard constructor for a card.
     * 
     * @param value
     * Numeric value, which must be between 2 and 14, inclusive,
     * to be valid.
     * 
     * @param suit
     * The card's suit.
     */
    public Card(int value, Suit suit) {
        setValue(value);
        setSuit(suit);
    }
    
    /**
     * Getter for the numeric value of the card.
     * 
     * @return The value field.
     */
    public int getValue () {
        return this.value;
    }
    
    /**
     * Getter for the suit of the card.
     * 
     * @return The suit field.
     */
    public Suit getSuit () {
        return this.suit;
    }
    
    /**
     * Setter for the numeric value of the card. Defends
     * against invalid values.
     * 
     * @param value 
     * The new numeric value.
     * 
     * @return True if the value was correctly set. Otherwise false.
     */
    public boolean setValue(int value) {
        if (value >= MIN_CARD_VALUE && value <= MAX_CARD_VALUE) {
            this.value = value;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Setter for the suit.
     * 
     * @param suit
     * The suit to set.
     */
    public void setSuit(Suit suit) {
        this.suit = suit;
    }
    
    /**
     * Checks whether the card is valid. The seventeen of clubs, for instance
     * is invalid. Any numeric value is valid for a joker.
     * 
     * @return True if the card has a valid value-suit combination.
     */
    public boolean isValid() {
        if (this.suit == Suit.JOKER) {
            return true;
        } else if ( this.value >= MIN_CARD_VALUE && this.value <= MAX_CARD_VALUE ) {
            return true;
        } else {
            return false;
        }        
    }
    
    /**
     * Gets a pretty-printable string for the value of the card.
     * 
     * @return A printable string for the card's value.
     */
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

    /**
     * Override for the compareTo method which will allow us to sort
     * cards. Note that the suits have an order, all jokers are equal, and
     * jokers are higher than all other cards.
     * 
     * @param Card 
     * The other card to which to compare <b>this</b>.
     * 
     * @return
     * A positive value if <b>this</b> is greater than <b>other</b>, 
     * negative if <b>this</b> is lesser, and zero if this.equals(other).
     */
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
    
    /**
     * Overriding the equals method.
     */
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
    
    /**
     * We overrode equals, so we have to override hashCode.
     */
    @Override
    public int hashCode() {
        final int goodEnoughForHorseshoesPrime = 37;
        if (this.suit == Suit.JOKER) {
            return Suit.JOKER.hashCode();
        } else {
            return this.suit.hashCode() + goodEnoughForHorseshoesPrime * this.value;
        }
    }
    
    /**
     * A toString override.
     */
    @Override
    public String toString() {
        return getValueString() + " " + getSuit().toString();
    }
    
    
    
    
    

}
