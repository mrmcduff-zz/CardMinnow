package com.mishmash.rally;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This represents the fundamental purpose of the program, a 'hand' of cards.
 * A hand is not limited in size, though it must have at least one card and 
 * contain no duplicates (which effectively limits its size to 53, but if we
 * change the properties of a deck this will not be the case).
 * 
 * @author mrmcduff
 *
 */
public class Hand {
    
    /**
     * An enumeration for the different types of hands, given value
     * according to increasing rank. Has a pretty-printing toString override.
     * 
     * @author mrmcduff
     *
     */
    public enum HandType {
        HIGH_CARD(0),
        PAIR(1),
        TWO_PAIR(2),
        THREE_OF_A_KIND(3),
        STRAIGHT(4),
        FLUSH(5),
        FULL_HOUSE(6),
        FOUR_OF_A_KIND(7),
        STRAIGHT_FLUSH(8),
        FIVE_OF_A_KIND(9);
        
        private int value;
        private HandType(int value) {
            this.value = value;
        }
        
        public int getValue () {
            return this.value;
        }
        
        @Override
        public String toString() {
            switch(this) {
            case HIGH_CARD:
                return "high";
            case PAIR:
                return "pair";
            case TWO_PAIR:
                return"two pair";
            case THREE_OF_A_KIND:
                return"three";
            case STRAIGHT:
                return"-high straight";
            case FLUSH:
                return"-high flush";
            case FULL_HOUSE:
                return"full house";
            case FOUR_OF_A_KIND:
                return"four";
            case STRAIGHT_FLUSH:
                return"straight flush";
            case FIVE_OF_A_KIND:
                return"five";
            default:
                // This is an impossible case, as we are switching on an enum
                return "";
            }
        }
    }
    
    private HandType type = HandType.HIGH_CARD;
    private boolean isEvaluated = false;

    /**
     * Each hand must have a set of important cards that define it. For instance,
     * a PAIR hand would have the defining pair as its most important cards.
     */
    private List<Card> importantCards = new ArrayList<Card>();

    /**
     * Some hands require two sets of important cards, and this is the lesser of the
     * two. For instance, a FULL_HOUSE has three of a kind and a pair. This would 
     * correspond to the pair.
     */
    private List<Card> secondImportantCards = new ArrayList<Card>();
    
    /**
     * The actual set of cards for this hand.
     */
    private List<Card> cards = new ArrayList<Card>();
    
    /**
     * No matter how many cards are in the hand, 
     * a hand is scored by its best five cards. If there are less
     * than five cards in the hand, it is impossible to make a 
     * straight or a flush, but you could still have a pair (or two), high card, 
     * or three or four of a kind.
     */
    public static final int FIVE_CARD_DRAW_HAND_SIZE = 5;
    
    /**
     * The class that evaluates all the different possibilities.
     */
    private HandEvaluator evaluator = new HandEvaluator();

    /**
     * A default constructor for an empty hand.
     */
    public Hand() {}
    
    /**
     * Constructor setting the list of cards. 
     * 
     * @param cards
     * The cards that make up this hand.
     */
    public Hand(List<Card> cards) {
        this.cards = cards;
    }
    
    /**
     * A convenience function that lets you make the cards out of an array.
     * Primarily used in testing.
     * 
     * @param cards
     * The cards that make up this hand.
     */
    public Hand(Card [] cards) {
        this.cards = Arrays.asList(cards);
    }
    
    /**
     * Checks to see whether or not this is a valid hand.
     * 
     * @return
     * True if the hand is nonempty, contains no invalid cards, and 
     * contains no duplicates.
     */
    public boolean isValid() {
        boolean answer = true;
        
        Set<Card> cardSet = new HashSet<Card>();
        if (this.getHandSize() == 0) {
            // No need to check everything else if we don't have
            // the right number of cards.
            return false;
        } else {
            for (Card card : cards) {
                
                // All cards must be valid.
                if (!card.isValid()) {
                    answer = false;
                }
                
                // There should be no duplicates.
                if (!cardSet.add(card)) {
                    answer = false;
                }
            }
        }
        
        return answer;
    }
    
    /**
     * Getter for the number of cards in this hand.
     * 
     * @return
     * The number of cards in this hand.
     */
    public int getHandSize() {
        return this.cards.size();
    }
    
    /**
     * Getter for the type of this hand. Note that it
     * defaults to HandType.HIGH_CARD and doesn't change
     * without the help of an evaluator. If the hand has not
     * already been evaluated, it calls its own evaluator to 
     * get that value before returning an answer.
     * 
     * @return
     * The <b>type</b> field of this hand.
     */
    public HandType getHandType() {
        if (this.isEvaluated) {
            return this.type;
        } else {
            evaluator.evaluate(this);
            return this.type;
        }
    }
    
    /**
     * Getter for the set of all cards contained in this hand.
     * 
     * @return
     * A shallow copy of the set of all cards.
     */
    public List<Card> getCards() {
        // I want to make a shallow copy to prevent other classes from altering
        // this hand's cards with this method.
        ArrayList<Card> cardArrayList = new ArrayList<Card>(this.cards);
        return cardArrayList;
    }
    
    /**
     * A setter for the handType of this hand.
     * 
     * @param handType
     * The new value to set to the <b>type</b> field.
     */
    public void setHandType(HandType handType) {
        this.type = handType;
    }
    
    /**
     * Setter for the <b>isEvaluated</b> field.
     * 
     * @param evaluated
     */
    public void setEvaluated(boolean evaluated) {
        this.isEvaluated = evaluated;
    }
    
    /**
     * Getter for the set of important cards.
     * 
     * @return
     * The most important cards in determining the value of this hand.
     */
    public List<Card> getImportantCards() {
        return this.importantCards;
    }
    
    /**
     * Setter for the important cards.
     * 
     * @param importantCards
     * The most important cards in determining the value of this hand.
     */
    public void setImportantCards(List<Card> importantCards) {
        if (importantCards != null && listIsInOrder(importantCards)) {
            this.importantCards = importantCards;
        }
    }
    
    /**
     * Getter for the set of second most important cards.
     * This list can be empty.
     * 
     * @return
     * The second most important cards in determining the value of this hand.
     */
    public List<Card> getSecondImportantCards() {
        return this.secondImportantCards;
    }
    
    /**
     * Setter for the second most important cards.
     * 
     * @param secondImportantCards
     * The second most important cards in determining the value of this hand.
     */
    public void setSecondImportantCards(List<Card> secondImportantCards) {
        if (secondImportantCards != null && listIsInOrder(secondImportantCards)) {
            this.secondImportantCards = secondImportantCards;
        }
    }
    
    /**
     * Checks to see if a list of cards is allowable as a set of scoring/important
     * cards. The cards should always be sorted in descending order and there should
     * be no joker (unless the list contains ONLY a joker).
     * 
     * @param inputList
     * The list to check.
     * 
     * @return
     * True if and only if this list is properly formatted.
     */
    private boolean listIsInOrder(List<Card> inputList) {
        Card oldCard = null;
        for (Card card : inputList) {
            // All cards must be valid, and a joker can never be part of
            // a proper list of cards unless it is a length one list (used for determining rank).
            if (!card.isValid() || ( (card.getSuit() == Card.Suit.JOKER) && (inputList.size() > 1) )) {
                return false;
            } else {
                // In this case, we're just starting and 
                // no comparison is needed.
                if (oldCard == null) {
                    oldCard = card;
                } else {
                    // These should always be sorted in descending order
                    if (oldCard.compareTo(card) < 0) {
                        return false;
                    } else {
                        oldCard = card;
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * Gets a pretty-printable string describing the hand based on its contents.
     * 
     * @return
     * A string suitable for user consumption describing the hand.
     * 
     * @throws IllegalStateException
     * If this hand is in an invalid state for printing. This includes
     * if the important cards are not set, or if the second important cards are
     * required but not set.
     */
    public String getDescription() throws IllegalStateException {
        HandType myType = this.getHandType();
        StringBuilder sb = new StringBuilder();
        Card highCard = null;
        Card otherCard = null;
        if (this.isValid()) {
            if (this.importantCards != null && this.secondImportantCards != null &&
                    this.importantCards.size() > 0) {
                // henceforth, highCard can never be null.
                highCard = this.importantCards.get(0);
                
                if (this.secondImportantCards.size() > 0) {
                    // otherCard, however, can remain null.
                    otherCard = this.secondImportantCards.get(0);
                }
                
                final String space = " ";
                final String of = " of ";
                final String pluralizer = "s";
                final String comma = ", ";
                final String andString = " and ";
                final String over = " over ";
                final String royalFlush = "**ROYAL FLUSH**";
                final String highString = "-high ";
                final String lucky = ". Someone's feeling lucky.";
                final String period = ".";
                final String exclamation = "!";
                // Six is the only card whose plural requires affixing 'es' rather
                // than just 's'
                final String sixPluralizer = "es";
                // Because I'm going to check this a lot, I'll go ahead and do it now
                
                String highPlural = (highCard.getValue() == 6) ? sixPluralizer : pluralizer;
                String otherPlural = (otherCard != null && otherCard.getValue() == 6) ? sixPluralizer : pluralizer;
                switch(myType) {
                case HIGH_CARD:
                    sb.append(CardUtils.capitalizeWord(highCard.getValueString()));
                    sb.append(space);
                    sb.append(myType);
                    sb.append(period);
                    break;
                case PAIR:
                    sb.append(CardUtils.capitalizeWord(myType.toString()));
                    sb.append(of);
                    sb.append(highCard.getValueString());
                    sb.append(highPlural);
                    sb.append(period);
                    break;
                case TWO_PAIR:
                    if (otherCard != null) {
                        sb.append(CardUtils.capitalizeWord(myType.toString()));
                        sb.append(comma);
                        sb.append(highCard.getValueString());
                        sb.append(highPlural);
                        sb.append(andString);
                        sb.append(otherCard.getValueString());
                        sb.append(otherPlural);
                        sb.append(period);
                    } else {
                        String twoPairErrorString = "Invalid two pair created. ";
                        throw new IllegalStateException(twoPairErrorString);
                    }
                    break;
                case THREE_OF_A_KIND:
                    sb.append(CardUtils.capitalizeWord(myType.toString()));
                    sb.append(space);
                    sb.append(highCard.getValueString());
                    sb.append(highPlural);
                    sb.append(period);
                    break;
                case STRAIGHT:
                    sb.append(CardUtils.capitalizeWord(highCard.getValueString()));
                    sb.append(myType.toString());
                    sb.append(period);
                    break;
                case FLUSH:
                    sb.append(CardUtils.capitalizeWord(highCard.getValueString()));
                    sb.append(myType.toString());
                    sb.append(of);
                    sb.append(highCard.getSuit());
                    sb.append(period);
                    break;
                case FULL_HOUSE:
                    if (otherCard != null) {
                        sb.append(CardUtils.capitalizeWord(myType.toString()));
                        sb.append(comma);
                        sb.append(highCard.getValueString());
                        sb.append(highPlural);
                        sb.append(over);
                        sb.append(otherCard.getValueString());
                        sb.append(otherPlural);
                        sb.append(period);
                    } else {
                        String fullHouseErrorString = "Invalid full house created. ";
                        throw new IllegalStateException(fullHouseErrorString);
                    }
                    break;
                case FOUR_OF_A_KIND:
                    sb.append(CardUtils.capitalizeWord(myType.toString()));
                    sb.append(space);
                    sb.append(highCard.getValueString());
                    sb.append(highPlural);
                    sb.append(exclamation);
                    break;
                case STRAIGHT_FLUSH:
                    if (highCard.getValue() == Card.MAX_CARD_VALUE) {
                        sb.append(royalFlush);
                        sb.append(space);
                        sb.append(of);
                        sb.append(CardUtils.capitalizeWord(highCard.getSuit().toString()));
                        sb.append(exclamation);
                    } else {
                        sb.append(CardUtils.capitalizeWord(highCard.getValueString()));
                        sb.append(highString);
                        sb.append(myType.toString());
                        sb.append(of);
                        sb.append(highCard.getSuit());
                        sb.append(exclamation);
                    }
                    break;
                case FIVE_OF_A_KIND:
                    sb.append(CardUtils.capitalizeWord(myType.toString()));
                    sb.append(space);
                    sb.append(highCard.getValueString());
                    sb.append(highPlural);
                    sb.append(exclamation);
                    sb.append(lucky);
                    break;
                default:
                    throw new IllegalStateException("Switched to an invalid handtype enum value.");
                }
            } else {
                if (this.importantCards == null) {
                    throw new IllegalStateException("ImportantCards were null");
                } else if (this.importantCards.size() == 0){
                    throw new IllegalStateException("ImportantCards were an empty set. This hand" +
                            " should be invalid.");
                } else {
                    throw new IllegalStateException("The SecondImportantCards were null.");
                }
            }
        } else {
            throw new IllegalStateException("Trying to evaluate an invalid hand.");
        }
        
        return sb.toString();
    }
    

    
}
