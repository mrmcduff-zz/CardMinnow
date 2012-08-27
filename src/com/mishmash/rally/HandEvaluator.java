package com.mishmash.rally;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * This is the all-knowing, all-powerful evaluation class of CardMinnow. Allowing 
 * a wild card and hands that can be as large as a whole deck introduces more difficulty
 * to picking out a five card hand and requires more read-throughs of the cards. Sorting
 * saves us some work and allows some optimization.
 * 
 * @author mrmcduff
 *
 */
public class HandEvaluator {
    
    public final int SCORING_HAND_SIZE = 5;
    
    /**
     * A struct-type class to hold all the cards of a hand in an easier-to-use object.
     * Once items are stored here, we can pick through the organized lists easily to find
     * the scoring hand.
     * 
     * @author mrmcduff
     *
     */
    public class SimplifiedHand {
        // This inner class is essentially a struct, so all member variables are public.
        public boolean hasJoker = false;
        public boolean isValid = false;
        public Hashtable<Integer, List<Card>> pairLists = new Hashtable<Integer, List<Card>>();
        public Hashtable<Card.Suit, List<Card>> flushLists = new Hashtable<Card.Suit, List<Card>>();
        public Hashtable<Card, List<Card>> straightLists = new Hashtable<Card, List<Card>>();
    }
    
    /**
     * A struct-type class representing a scoring hand.
     * 
     * @author mrmcduff
     *
     */
    public class FiveCardHand {
        public Hand.HandType type = Hand.HandType.HIGH_CARD;
        public boolean hasJoker = false;
        public List<Card> importantList = new ArrayList<Card>();
        public List<Card> otherList = new ArrayList<Card>();
    }
    
    /**
     * Method to evaluate a hand. This is the 'master' method of this class.
     * Checks each of the possible hand types to see what the input hand contains
     * and picks the best. It sets its data in the input hand.
     * 
     * @param hand
     * The hand to be evaluated. We evaluate by using hand.setType().
     */
    public void evaluate(Hand hand) {
        SimplifiedHand simpleHand = sortHand(hand);
        FiveCardHand bestFive = new FiveCardHand();
        FiveCardHand tempHand = new FiveCardHand();
        
        if (simpleHand.isValid) {
            // We have to start somewhere.
            bestFive = getBestCollection(simpleHand);
            
            // If we already have five of a kind, we're done.
            if (bestFive.type.compareTo(Hand.HandType.FIVE_OF_A_KIND) < 0) {
                tempHand = getBestStraightFlush(simpleHand);
                if (tempHand.type.compareTo(bestFive.type) > 0) {
                    // This is only possible if we found a straight flush
                    bestFive = tempHand;
                } else {
                    // If we have a full house or four of a kind, we don't need
                    // to check anything else.
                    if (bestFive.type.compareTo(Hand.HandType.FULL_HOUSE) < 0) {
                        tempHand = getBestFlush(simpleHand);
                        if (tempHand.type.compareTo(bestFive.type) > 0) {
                            // Then we must have found a flush
                            bestFive = tempHand;
                        } else {
                            tempHand = getBestStraight(simpleHand);
                            if (tempHand.type.compareTo(bestFive.type) > 0) {
                                // Then we must have found a straight.
                                bestFive = tempHand;
                            }
                            
                            // We don't need to check any more, because the hands
                            // Three of a kind, two pair, and one pair are all taken
                            // care of in our first step (getBestCollection).
                            // Once we're here, bestFive must be the best possible hand.
                        } // end else for finding a flush
                    } // end if lower than a full house.
                } // end else where we haven't found a straight flush
            } // end if for lower than five of a kind.
            
            // bestFive is now the best possible hand.
            hand.setHandType(bestFive.type);
            hand.setEvaluated(true);
            
            // There is one special case, where the hand contains only a joker, which
            // we haven't covered.
            if (bestFive.type == Hand.HandType.HIGH_CARD && bestFive.importantList.isEmpty()) {
                bestFive.importantList.add(new Card());
            }
            hand.setImportantCards(bestFive.importantList);
            hand.setSecondImportantCards(bestFive.otherList);
            
        } // end if we have a valid hand
    }
    
    /**
     * Sorts through all the cards of a hand to make it much easier
     * and faster to determine its value.
     * @param hand
     * The 'unfiltered' hand to be sorted.
     * 
     * @return
     * The SimplifiedHand struct storing the Hand's data in a more convenient way.
     */
    public SimplifiedHand sortHand(Hand hand) {
        SimplifiedHand simpleHand = new SimplifiedHand();
        if (hand.isValid()) {
            simpleHand.isValid = true;
            List<Card> cards = hand.getCards();
            
            // This will make our lives much easier,
            // and save us operations because we always know
            // the highest cards are the first ones in the order.
            Collections.sort(cards, Collections.reverseOrder());
            Card currentStraightCard = null;
            int value = 0;
            for (Card card : cards) {
                if (Card.Suit.JOKER == card.getSuit()) {
                    simpleHand.hasJoker = true;
                    continue;
                } else {
                    if ( value == card.getValue() ) {
                        List<Card> valueList = simpleHand.pairLists.get(Integer.valueOf(value));
                        valueList.add(card);
                    } else {
                        if ( (value - 1) == card.getValue() ) {
                            if (currentStraightCard != null) {
                                List<Card> straightList = simpleHand.straightLists.get(currentStraightCard);
                                straightList.add(card);
                            } else {
                                // This situation should not be possible.
                                assert(false);
                            }
                        } else {
                            // In this case we have changed values from one card to the next
                            // and the values are nonsequential. Hence, we start a new sequence.
                            currentStraightCard = card;
                            List<Card> startingStraightList = new ArrayList<Card>();
                            startingStraightList.add(card);
                            simpleHand.straightLists.put(card, startingStraightList);
                        }
                        
                        List<Card> nextValueList = new ArrayList<Card>();
                        nextValueList.add(card);
                        simpleHand.pairLists.put(Integer.valueOf(card.getValue()), nextValueList);
                    }
                    
                    // Always increment the value, even if we're just making an x = x assignment.
                    value = card.getValue();
                    
                    List<Card> currentFlushList = simpleHand.flushLists.get(card.getSuit());
                    if (currentFlushList == null) {
                        currentFlushList = new ArrayList<Card>();
                        simpleHand.flushLists.put(card.getSuit(), currentFlushList);
                    }
                    currentFlushList.add(card);
                }
            }
            
            // At this point we know whether or not the hand contains a joker, and we have 
            // lists of all collections { Ah, As, Ac }, { 2c, 2d } indexed by their value,
            // lists of all flushes { Ah, Qh, 9h }, { 3s } indexed by their suit, and
            // lists of all straights indexed by their starting (highest) card
        } 
        return simpleHand;
    }
    
    /**
     * Gets the best 'collection' hand available. Collection hands are those made up of n-tuples,
     * so pair, two pair, three of a kind, full house, four of a kind, and five of a kind. High card
     * falls into this category by default.
     * 
     * @param simpleHand
     * The SimplifiedHand to be checked.
     * 
     * @return
     * A FiveCardHand with the best possible collection.
     */
    public FiveCardHand getBestCollection(SimplifiedHand simpleHand) {
        FiveCardHand fch = new FiveCardHand();
        fch.hasJoker = simpleHand.hasJoker;
        
        fch.importantList = getBestNaturalCollection(simpleHand.pairLists);
        
        int compliment = SCORING_HAND_SIZE - fch.importantList.size();
        if (simpleHand.hasJoker) {
            // Because the best set just got better.
            --compliment;
        }
        
        // Clone only returns an object, so we have to cast here.
        // I don't usually suppress warnings, but for this project it felt
        // overkill to verify that this cast worked, given that SimplifiedHand
        // is an inner class of HandEvaluator.
        @SuppressWarnings("unchecked")
        Hashtable<Integer, List<Card>> copyTable = 
                (Hashtable<Integer, List<Card>>) simpleHand.pairLists.clone();
        
        switch(compliment) {
        case 4:
            // At the moment, we don't evaluate the second highest card for 
            // high card hands. We simply describe them as "You have king high."
            fch.type = Hand.HandType.HIGH_CARD;
            break;
        case 3:
            // Then the best hand we can have is two pair.
            
            if (copyTable != null) {
                // just a sanity check, then i'm removing the best set from the 
                // copy table
                copyTable.remove(Integer.valueOf(fch.importantList.get(0).getValue()));
                fch.otherList = getBestNaturalCollection(copyTable);
            }
            
            if (fch.otherList.size() == 2) {
                fch.type = Hand.HandType.TWO_PAIR;
            } else {
                fch.type = Hand.HandType.PAIR;
            }
            break;
        case 2:
            // then we could, in theory, have a full house.
            if (copyTable != null) {
                copyTable.remove(Integer.valueOf(fch.importantList.get(0).getValue()));
                fch.otherList = getBestNaturalPair(copyTable);
            }
            
            if (fch.otherList.size() == 2) {
                fch.type = Hand.HandType.FULL_HOUSE;
            } else {
                fch.type = Hand.HandType.THREE_OF_A_KIND;
                fch.otherList.clear();
            }
            break;
        case 1:
            // Then we must have four of a kind, rendering the 'kicker' irrelevant.
            fch.type = Hand.HandType.FOUR_OF_A_KIND;
            break;
        case 0:
            // Then a joker must be in play and we have five of a kind
            fch.type = Hand.HandType.FIVE_OF_A_KIND;
            break;
        default:
            // Then we must have been playing with an invalid hand. It's impossible to have
            // a collection of size greater than four because duplicate cards are illegal
            // and there are only four suits plus a joker.
            break;
        }
        
        return fch;
    }
    
    /**
     * Gets the best 'natural' (as in no wild cards) collection from the given table.
     * 
     * @param table
     * The Integer-List table pairing card values to the number of cards found at that value.
     * 
     * @return
     * A list containing the cards of the best collection, or an empty list if no such
     * collection exists.
     * 
     */
    private List<Card> getBestNaturalCollection(Hashtable<Integer, List<Card>> table) {
        List<Card> bestSet = new ArrayList<Card>();
        List<Integer> sortList = Collections.list(table.keys());
        Collections.sort(sortList, Collections.reverseOrder());
        for (Integer i : sortList) {
            List<Card> tempSet = table.get(i);
            if (null != tempSet) {
                if (tempSet.size() > bestSet.size()) {
                    bestSet = tempSet;
                } else if ((tempSet.size() == bestSet.size()) && (tempSet.size() > 0) ) {
                    Card tempTest = tempSet.get(0);
                    Card bestTest = bestSet.get(0);
                    if (tempTest.getValue() > bestTest.getValue()) {
                        bestSet = tempSet;
                    }
                }
            }
        }
        return bestSet;
    }
    
    /**
     * Gets the best natural pair available from the table. Useful to find
     * a secondary pair. This won't ever find three-of-a-kinds, four-of-a-kinds, etc.
     * @param table
     * The Integer-List of cards table to search.
     * 
     * @return
     * The best pair available, or an empty list if there are no pairs.
     */
    private List<Card> getBestNaturalPair(Hashtable<Integer, List<Card>> table) {
        List<Card> bestPair = new ArrayList<Card>();
        List<Integer> sortList = Collections.list(table.keys());
        Collections.sort(sortList, Collections.reverseOrder());

        for (Integer i: sortList) {
            boolean addNewPair = false;
            List<Card> tempSet = table.get(i);
            if (null != tempSet) {
                if (bestPair.size() == 2) {
                    if (tempSet.size() >= 2) {
                        addNewPair = tempSet.get(0).getValue() > bestPair.get(0).getValue();
                    }
                } else {
                    // In this case, our best either hasn't been set yet or is
                    // of size 1
                    addNewPair = (tempSet.size() >= bestPair.size());
                }
                
                if (addNewPair) {
                    bestPair.clear();
                    bestPair.add(tempSet.get(0));
                    if (tempSet.size() == 2) {
                        bestPair.add(tempSet.get(1));
                    }
                }
            }
        }
        return bestPair;
    }

    /**
     * Gets the best available straight from the given SimplifiedHand.
     * 
     * @param simpleHand
     * The simplified hand to search
     * 
     * @return
     * A FiveCardHand struct-class with the best straight hand, or HIGH_CARD if there
     * is no straight.
     */
    public FiveCardHand getBestStraight(SimplifiedHand simpleHand) {
        FiveCardHand fch = new FiveCardHand();
        fch.hasJoker = simpleHand.hasJoker;
        List<Card> amalgamatedList = new ArrayList<Card>();
        for(Card card : simpleHand.straightLists.keySet()) {
            amalgamatedList.addAll(simpleHand.straightLists.get(card));
        }
        Collections.sort(amalgamatedList, Collections.reverseOrder());
        List<Card> straightList = getStraightFromOrderedListWithoutDuplicates(amalgamatedList, 
                simpleHand.hasJoker);

        if (straightList.size() > 0) {
            fch.importantList = straightList;
            fch.type = Hand.HandType.STRAIGHT;
        }
        return fch;
    }
    
    /**
     * Gets a straight from an ordered list with no duplicate <b>values</b>.
     * 
     * @param cards
     * The cards to search.
     * 
     * @param withJoker
     * Whether or not we're working with a wild card, and thus need to 
     * evaluate straights with gaps.
     * 
     * @return
     * A list with the cards involved in the best possible straight (not
     * taking into acount straight flushes) from this set. The wild card
     * is never included in this set.
     */
    private List<Card> getStraightFromOrderedListWithoutDuplicates(List<Card> cards, boolean withJoker) {
        List<Card> answer = new ArrayList<Card>();
        int minStraightSize = withJoker ? SCORING_HAND_SIZE - 1 : SCORING_HAND_SIZE;
        if (cards == null || cards.size() < minStraightSize)
        {
            // No need to jump through hoops if there aren't enough cards
            // to make a straight.
            return answer;
        }
        
        int currentStartingValue = 0;
        int lastValue = 0;
        int jokerGapIndex = 0;
        int split = 0;
        boolean passedGapAlready = false;
        
        if (cards != null) {
            for (int i = 0; i < cards.size(); ++i) {
                Card card = cards.get(i);
                if (lastValue == 0 || answer.isEmpty()) {
                    currentStartingValue = card.getValue();
                } else if (lastValue - 1 == card.getValue()){
                    // This is the "normal" counting down case. The last card
                    // was a king, and this one is a queen, for example.
                } else if ( (lastValue - 2 == card.getValue()) && !passedGapAlready && withJoker) {
                    // Here we're using the joker to complete an inside straight.
                    passedGapAlready = true;
                    // We can only get to this case if i >= 1, so this assignment is okay.
                    jokerGapIndex = i-1;
                } else {
                    // The chain is broken, so we start over with the current card if there
                    // are no jokers, or the card after the gap if there is a joker.
                    answer.clear();
                    if (withJoker && passedGapAlready) {
                        if (jokerGapIndex > 0) {
                            // We have to ensure we've moved forward
                            // before we move back.
                            i = jokerGapIndex;
                        }
                        jokerGapIndex = 0;
                        split = 0;
                        lastValue = 0;
                        passedGapAlready = false;
                        // Continuing here to skip the answer.addCard() and lastValue assignment steps
                        continue;
                    } else {
                        currentStartingValue = card.getValue();
                    }
                }
                
                lastValue = card.getValue();
                answer.add(card);
                // Why add 1 in this calculation? Well, consider the A, K, Q, J, 10 straight.
                // Starting value is 14, end value is 10, so 14 - 10 = 4. And we want a straight 
                // to be the entire length of a scoring hand, so we add one to that total.
                split = currentStartingValue - lastValue;
                if ( (split + 1 == SCORING_HAND_SIZE) || 
                        ( (split + 2 == SCORING_HAND_SIZE) && withJoker && !passedGapAlready ) ){
                    // Then we have a 5-long straight in the first case, or a 4-long straight with
                    // wild cards, so we have an outside straight.
                    break;
                }
            }
            
            int straightSize = answer.size();
            boolean answerIsOkay = false;
            if( (straightSize == SCORING_HAND_SIZE) || 
                    ( (straightSize + 1 == SCORING_HAND_SIZE) && withJoker ) ) {
                // The only ways for this to be okay is for the list to have
                // 5 items or 4 items and there is a wild card.
                answerIsOkay = true;
            }
            
            if(!answerIsOkay) {
             // We want to return an empty list if there is no straight
                answer.clear();
            }
        }
        return answer;
    }
    
    /**
     * Gets the best flush from a simplified hand, not taking into account
     * whether or not the flush is a straight flush. This sorts by highest
     * card (or highest first two cards, or first three cards, and so on)
     * in the flush set.
     * 
     * @param simpleHand
     * The SimplifiedHand to search.
     * 
     * @return
     * A FiveCardHand representing the best flush, or a HIGH_CARD hand if
     * there is no flush.
     */
    public FiveCardHand getBestFlush(SimplifiedHand simpleHand) {
        FiveCardHand fch = new FiveCardHand();
        fch.hasJoker = simpleHand.hasJoker;
        // Fun with the ternary operator :)
        int minFlushSize = simpleHand.hasJoker ? SCORING_HAND_SIZE - 1 : SCORING_HAND_SIZE;
        
        List<Card> spadeFlushList = 
                CardUtils.getTopNCards(simpleHand.flushLists.get(Card.Suit.SPADES), 
                        minFlushSize);
        List<Card> heartFlushList = 
                CardUtils.getTopNCards(simpleHand.flushLists.get(Card.Suit.HEARTS), 
                        minFlushSize);
        List<Card> diamondFlushList = 
                CardUtils.getTopNCards(simpleHand.flushLists.get(Card.Suit.DIAMONDS), 
                        minFlushSize);
        List<Card> clubFlushList = 
                CardUtils.getTopNCards(simpleHand.flushLists.get(Card.Suit.CLUBS), 
                        minFlushSize);
        
        // Note that these lists must all be the same length or empty.
        List<Card> bestList = spadeFlushList;
        // This process could be optimized to run in 4*n time instead of 6*n time, 
        // but it would be needlessly complicated unless the scoring hand size was
        // large. These lists are either 0, 4, or 5 cards long. I could save about
        // twenty operations as is, and the code would get much more complicated.
        bestList = compareFlushesAndReturnBestOrFirst(bestList, heartFlushList);
        bestList = compareFlushesAndReturnBestOrFirst(bestList, diamondFlushList);
        bestList = compareFlushesAndReturnBestOrFirst(bestList, clubFlushList);
        
        if (bestList.size() > 0) {
            fch.type = Hand.HandType.FLUSH;
            fch.importantList = bestList;
        }
        
        return fch;
    }
    
    /**
     * Compares two flush sets and returns the best available by value, or the first one
     * if they are equal.
     * @param first
     * The first flush set.
     * @param second
     * The second flush set.
     * 
     * @return
     * The best of the two inputs, or the first if they are equal.
     */
    private List<Card> compareFlushesAndReturnBestOrFirst(List<Card> first, List<Card> second) {
        List<Card> winner = first;
        if (first != null) {
            if (second != null) {
                if (first.size() != 0) {
                    if (second.size() != first.size()) {
                        // The two lists should be the same size or empty
                        // If not, just return the first one.
                        winner = first;
                    } else {
                        for (int i = 0; i < first.size(); ++i) {
                            if (first.get(i).getValue() > second.get(i).getValue()) {
                                winner = first;
                                break;
                            } else if (first.get(i).getValue() < second.get(i).getValue()) {
                                winner = second;
                                break;
                            } else { // The cards are of equal value
                                continue;
                            }
                        }
                    }
                    
                } else { // first's size is zero
                    if (second.size() > 0) {
                        winner = second;
                    } else {
                        // By default
                        winner = first;
                    }
                }
            } else { // second is null.
                winner = first;
            }
        } else { // first is null
            // Either second will be null, in which case we're returning the same thing
            // or we're returning a non-null second, which is at minimum marginally better.
            winner = second;
        }
        return winner;
    }

    /**
     * Gets the best straight flush from the input SimplifiedHand.
     * 
     * @param simpleHand
     * The SimplifiedHand to search for a straight flush.
     * 
     * @return
     * The highest ranking straight flus in the input hand, or a HIGH_CARD hand
     * if no straight flush exists.
     */
    public FiveCardHand getBestStraightFlush(SimplifiedHand simpleHand) {
        FiveCardHand fch = new FiveCardHand();
        fch.hasJoker = simpleHand.hasJoker;
        List<Card> spadeSf = getStraightFromOrderedListWithoutDuplicates(
                simpleHand.flushLists.get(Card.Suit.SPADES), simpleHand.hasJoker);
        List<Card> heartSf = getStraightFromOrderedListWithoutDuplicates(
                simpleHand.flushLists.get(Card.Suit.HEARTS), simpleHand.hasJoker);
        List<Card> diamondSf = getStraightFromOrderedListWithoutDuplicates(
                simpleHand.flushLists.get(Card.Suit.DIAMONDS), simpleHand.hasJoker);
        List<Card> clubSf = getStraightFromOrderedListWithoutDuplicates(
                simpleHand.flushLists.get(Card.Suit.CLUBS), simpleHand.hasJoker);
        // Just as with the flush case, I'm avoiding optimization for code complexity purposes.
        // Should parameters change such that there are more than the traditional number of suits
        // or the straight flush length increases in size, alterations should be made here.
        List<Card> bestSf = spadeSf;
        bestSf = compareFlushesAndReturnBestOrFirst(bestSf, heartSf);
        bestSf = compareFlushesAndReturnBestOrFirst(bestSf, diamondSf);
        bestSf = compareFlushesAndReturnBestOrFirst(bestSf, clubSf);
        if (bestSf.size() > 0) {
            fch.type = Hand.HandType.STRAIGHT_FLUSH;
            fch.importantList = bestSf;
        }
        
        return fch;
    }
    
}
