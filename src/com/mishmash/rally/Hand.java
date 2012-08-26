package com.mishmash.rally;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hand {
    
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
    private List<Card> importantCards = new ArrayList<Card>();
    private List<Card> secondImportantCards = new ArrayList<Card>();
    private List<Card> cards = new ArrayList<Card>();
    private List<Card> badCards = new ArrayList<Card>();
    public static final int FIVE_CARD_DRAW_HAND_SIZE = 5;
    private HandEvaluator evaluator = new HandEvaluator();

    public Hand() {}
    
    public Hand(List<Card> cards) {
        this.cards = cards;
    }
    
    public Hand(Card [] cards) {
        this.cards = Arrays.asList(cards);
    }
    
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
                    this.badCards.add(card);
                }
                
                // There should be no duplicates.
                if (!cardSet.add(card)) {
                    answer = false;
                    this.badCards.add(card);
                }
            }
        }
        
        return answer;
    }
    
    public int getHandSize() {
        return this.cards.size();
    }
    
    public HandType getHandType() {
        if (this.isEvaluated) {
            return this.type;
        } else {
            evaluator.evaluate(this);
            return this.type;
        }
    }
    
    public List<Card> getCards() {
        // I want to make a shallow copy to prevent other classes from altering
        // this hand's cards with this method.
        ArrayList<Card> cardArrayList = new ArrayList<Card>(this.cards);
        return cardArrayList;
    }
    
    public void setHandType(HandType handType) {
        this.type = handType;
    }
    
    public void setEvaluated(boolean evaluated) {
        this.isEvaluated = evaluated;
    }
    
    public List<Card> getImportantCards() {
        return this.importantCards;
    }
    
    public void setImportantCards(List<Card> importantCards) {
        if (importantCards != null && listIsInOrder(importantCards)) {
            this.importantCards = importantCards;
        }
    }
    
    public List<Card> getSecondImportantCards() {
        return this.secondImportantCards;
    }
    
    public void setSecondImportantCards(List<Card> secondImportantCards) {
        if (secondImportantCards != null) {
            this.secondImportantCards = secondImportantCards;
        }
    }
    
    private boolean listIsInOrder(List<Card> inputList) {
        Card oldCard = null;
        for (Card card : inputList) {
            // All cards must be valid, and a joker can never be part of
            // a proper list of cards (used for determining rank).
            if (!card.isValid() && card.getSuit() == Card.Suit.JOKER) {
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
                final String lucky = ". Someone's feeling lucky";

                switch(myType) {
                case HIGH_CARD:
                    sb.append(CardUtils.capitalizeWord(highCard.getValueString()));
                    sb.append(space);
                    sb.append(myType);
                    break;
                case PAIR:
                    sb.append(CardUtils.capitalizeWord(myType.toString()));
                    sb.append(of);
                    sb.append(highCard.getValueString());
                    sb.append(pluralizer);
                    break;
                case TWO_PAIR:
                    if (otherCard != null) {
                        sb.append(CardUtils.capitalizeWord(myType.toString()));
                        sb.append(comma);
                        sb.append(highCard.getValueString());
                        sb.append(pluralizer);
                        sb.append(andString);
                        sb.append(otherCard.getValueString());
                        sb.append(pluralizer);
                    } else {
                        String twoPairErrorString = "Invalid two pair created. ";
                        throw new IllegalStateException(twoPairErrorString);
                    }
                    break;
                case THREE_OF_A_KIND:
                    sb.append(CardUtils.capitalizeWord(myType.toString()));
                    sb.append(space);
                    sb.append(highCard.getValueString());
                    sb.append(pluralizer);
                    break;
                case STRAIGHT:
                    sb.append(CardUtils.capitalizeWord(highCard.getValueString()));
                    sb.append(myType.toString());
                    break;
                case FLUSH:
                    sb.append(CardUtils.capitalizeWord(highCard.getValueString()));
                    sb.append(myType.toString());
                    sb.append(of);
                    sb.append(highCard.getSuit());
                    break;
                case FULL_HOUSE:
                    if (otherCard != null) {
                        sb.append(CardUtils.capitalizeWord(myType.toString()));
                        sb.append(comma);
                        sb.append(highCard.getValueString());
                        sb.append(pluralizer);
                        sb.append(over);
                        sb.append(otherCard.getValueString());
                        sb.append(pluralizer);
                    } else {
                        String fullHouseErrorString = "Invalid full house created. ";
                        throw new IllegalStateException(fullHouseErrorString);
                    }
                    break;
                case FOUR_OF_A_KIND:
                    sb.append(CardUtils.capitalizeWord(myType.toString()));
                    sb.append(space);
                    sb.append(highCard.getValueString());
                    sb.append(pluralizer);
                    break;
                case STRAIGHT_FLUSH:
                    if (highCard.getValue() == Card.MAX_CARD_VALUE) {
                        return royalFlush;
                    } else {
                        sb.append(CardUtils.capitalizeWord(highCard.getValueString()));
                        sb.append(highString);
                        sb.append(myType.toString());
                        sb.append(of);
                        sb.append(highCard.getSuit());
                    }
                    break;
                case FIVE_OF_A_KIND:
                    sb.append(CardUtils.capitalizeWord(myType.toString()));
                    sb.append(space);
                    sb.append(highCard.getValueString());
                    sb.append(pluralizer);
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
