/**
 * 
 */
package com.mishmash.rally;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mishmash.rally.Card;
import com.mishmash.rally.CardUtils;
import com.mishmash.rally.Hand;
import com.mishmash.rally.HandEvaluator;
import com.mishmash.rally.Interpreter;

/**
 * @author mrmcduff
 *
 */
public class HandEvaluatorTest {
    

    private HandEvaluator he;
    
    private final Card[] emptyArray = {};

//    public Dictionary<Integer, List<Card>> pairLists;
//    public Dictionary<Card.Suit, List<Card>> flushLists; 
//    public Dictionary<Card, List<Card>> straightLists;
    
    

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        he = new HandEvaluator();
    }
    
    
    
    /**
     * Test method for {@link com.mishmash.rally.HandEvaluator#evaluate(com.mishmash.rally.Hand)}.
     */
    @Test
    public void testEvaluate() {
        List<Card> emptyList = new ArrayList<Card>();
        
        List<Card> highCardList = Interpreter.interpret("8c, 2d");
        List<Card> highCardImportant = Interpreter.interpret("8c");
        
        List<Card> pairList = Interpreter.interpret("w, 2c");
        List<Card> pairImportant = Interpreter.interpret("2c");
        
        List<Card> twoPairList = Interpreter.interpret("as, 3c, 3s, 2h, 2d");
        List<Card> twoPairImportant = Interpreter.interpret("3s, 3c");
        List<Card> twoPairOther = Interpreter.interpret("2h, 2d");
        
        List<Card> threeKindList = Interpreter.interpret("w, ah, jd, 10h, 10c, 9h");
        List<Card> threeKindImportant = Interpreter.interpret("10h, 10c");
        
        List<Card> straightList = Interpreter.interpret("kh, kd, 7s, 6h, 5d, 4c, 3s, 2s, 2h");
        List<Card> straightImportant = Interpreter.interpret("7s, 6h, 5d, 4c, 3s");
        
        List<Card> flushList = Interpreter.interpret("w, As, 7s, 6h, 5d, 4c, 3s, 2s, 2h");
        List<Card> flushImportant = Interpreter.interpret("As, 7s, 3s, 2s");
        
        List<Card> fullHouseList = Interpreter.interpret("w, As, Ah, 7s, 6h, 5d, 4c, 3s, 2s, 2h");
        List<Card> fullHouseImportant = Interpreter.interpret("As, Ah");
        List<Card> fullHouseOther = Interpreter.interpret("2s, 2h");
        
        List<Card> fourKindList = Interpreter.interpret("w, As, Ah, 7s, 6h, 5d, 4c, 3s, 2s, 2h, 2d");
        List<Card> fourKindImportant = Interpreter.interpret("2s, 2h, 2d");
        
        List<Card> straightFlushList = Interpreter.interpret("w, As, Ah, 7s, 6s, 5s, 4s, 3s, 2s, 2h, 2d");
        List<Card> straightFlushImportant = Interpreter.interpret("7s, 6s, 5s, 4s");
        
        List<Card> fiveKindList = Interpreter.interpret("w, As, Ah, 7s, 6s, 5s, 4s, 3s, 2s, 2h, 2d, 2c");
        List<Card> fiveKindImportant = Interpreter.interpret("2s, 2h, 2d, 2c");
        
        List<Card> deckList = CardUtils.getWholeDeck();
        List<Card> deckImportant = Interpreter.interpret("As, Ks, Qs, Js, 10s");
        
        List<Card> deckWithJokerList = new ArrayList<Card>();
        List<Card> deckWithJokerImportant = Interpreter.interpret("As, Ah, Ad, Ac");
        deckWithJokerList.add(new Card());
        deckWithJokerList.addAll(deckList);
        
        Hand emptyHand = new Hand(emptyList);
        Hand highCardHand = new Hand(highCardList);
        Hand pairHand = new Hand(pairList);
        Hand twoPairHand = new Hand(twoPairList);
        Hand threeKindHand = new Hand(threeKindList);
        Hand straightHand = new Hand(straightList);
        Hand flushHand = new Hand(flushList);
        Hand fullHouseHand = new Hand(fullHouseList);
        Hand fourKindHand = new Hand(fourKindList);
        Hand straightFlushHand = new Hand(straightFlushList);
        Hand fiveKindHand = new Hand(fiveKindList);
        Hand deckHand = new Hand(deckList);
        Hand deckWithJokerHand = new Hand(deckWithJokerList);
        
        Hand[] handArray = { emptyHand, highCardHand, pairHand, twoPairHand, threeKindHand, straightHand,
                flushHand, fullHouseHand, fourKindHand, straightFlushHand, fiveKindHand, 
                deckHand, deckWithJokerHand };
        
        Hand.HandType[] handTypeArray = { Hand.HandType.HIGH_CARD, Hand.HandType.HIGH_CARD, 
                Hand.HandType.PAIR, Hand.HandType.TWO_PAIR, Hand.HandType.THREE_OF_A_KIND, 
                Hand.HandType.STRAIGHT, Hand.HandType.FLUSH, Hand.HandType.FULL_HOUSE, 
                Hand.HandType.FOUR_OF_A_KIND, Hand.HandType.STRAIGHT_FLUSH, Hand.HandType.FIVE_OF_A_KIND,
                Hand.HandType.STRAIGHT_FLUSH, Hand.HandType.FIVE_OF_A_KIND };
        HashMap<Hand, List<Card>> importantMap = new HashMap<Hand, List<Card>>();
        importantMap.put(emptyHand, emptyList);
        importantMap.put(highCardHand, highCardImportant);
        importantMap.put(pairHand, pairImportant);
        importantMap.put(twoPairHand, twoPairImportant);
        importantMap.put(threeKindHand, threeKindImportant);
        importantMap.put(straightHand, straightImportant);
        importantMap.put(flushHand, flushImportant);
        importantMap.put(fullHouseHand, fullHouseImportant);
        importantMap.put(fourKindHand, fourKindImportant);
        importantMap.put(straightFlushHand, straightFlushImportant);
        importantMap.put(fiveKindHand, fiveKindImportant);
        importantMap.put(deckHand, deckImportant);
        importantMap.put(deckWithJokerHand, deckWithJokerImportant);
        // Sanity check
        assertEquals(handArray.length, handTypeArray.length);
        
        for (int i = 0; i < handArray.length; ++i) {
            he.evaluate(handArray[i]);
            assertEquals("Failed on iteration " + i + ". ", 
                    handTypeArray[i], handArray[i].getHandType());
            assertArrayEquals("Failed on iteration " + i + " with type " + handTypeArray[i],
                   importantMap.get(handArray[i]).toArray(), 
                   handArray[i].getImportantCards().toArray());
        }
        
        assertArrayEquals(twoPairHand.getSecondImportantCards().toArray(), twoPairOther.toArray());
        assertArrayEquals(fullHouseHand.getSecondImportantCards().toArray(), fullHouseOther.toArray());
        
        
    }

    /**
     * Test method for {@link com.mishmash.rally.HandEvaluator#sortHand(com.mishmash.rally.Hand)}.
     */
    @Test
    public void testSortHand() {
        
        Hashtable<Integer, List<Card>> pairLists = new Hashtable<Integer, List<Card>>();
        Hashtable<Card.Suit, List<Card>> flushLists = new Hashtable<Card.Suit, List<Card>>();
        Hashtable<Card, List<Card>> straightLists = new Hashtable<Card, List<Card>>();
        List<Card> twoList = Interpreter.interpret("2s, 2h");
        List<Card> threeList = Interpreter.interpret("3s, 3h, 3d");
        List<Card> spadeList = Interpreter.interpret("3s, 2s");
        List<Card> heartList = Interpreter.interpret("3h, 2h");
        List<Card> diamondList = Interpreter.interpret("3d");
        List<Card> straightList = Interpreter.interpret("3s, 2s");
        
        pairLists.put(Integer.valueOf(2), twoList);
        pairLists.put(Integer.valueOf(3), threeList);
        flushLists.put(Card.Suit.SPADES, spadeList);
        flushLists.put(Card.Suit.HEARTS, heartList);
        flushLists.put(Card.Suit.DIAMONDS, diamondList);
        straightLists.put(Interpreter.convertTokenToCard("3s"), straightList);
        
        List<Card> combinedList = Interpreter.interpret("2s, 3d, 3s, 2h, 3h");
        // This will first be sorted to be { 3s, 3h, 3d, 2s, 2h }, so the only 
        // constructed straight should be { 3s, 2s }
        HandEvaluator.SimplifiedHand testHand = he.sortHand(new Hand(combinedList));
        
        assertFalse(testHand.hasJoker);
        assertTrue(testHand.isValid);
        // pair
        assertEquals(pairLists.size(), testHand.pairLists.size());
        assertArrayEquals(pairLists.get(Integer.valueOf(2)).toArray(), 
                testHand.pairLists.get(Integer.valueOf(2)).toArray());
        assertArrayEquals(pairLists.get(Integer.valueOf(3)).toArray(),
                testHand.pairLists.get(Integer.valueOf(3)).toArray());
        
        // flushes
        assertEquals(flushLists.size(), testHand.flushLists.size());
        assertArrayEquals(flushLists.get(Card.Suit.SPADES).toArray(), 
                testHand.flushLists.get(Card.Suit.SPADES).toArray());
        assertArrayEquals(flushLists.get(Card.Suit.HEARTS).toArray(), 
                testHand.flushLists.get(Card.Suit.HEARTS).toArray());
        assertArrayEquals(flushLists.get(Card.Suit.DIAMONDS).toArray(), 
                testHand.flushLists.get(Card.Suit.DIAMONDS).toArray());
        assertNull(testHand.flushLists.get(Card.Suit.CLUBS));
        
        // straights
        assertEquals(straightLists.size(), testHand.straightLists.size());
        Card leadCard = new Card(3, Card.Suit.SPADES);
        assertNotNull(testHand.straightLists.get(leadCard));
        assertArrayEquals(straightLists.get(leadCard).toArray(), testHand.straightLists.get(leadCard).toArray());
        
    }
    
    @Test
    public void testSortBadHand() {
        HandEvaluator.SimplifiedHand testHand = he.sortHand(new Hand(Interpreter.interpret("3h, 3h, 2s")));
        assertFalse(testHand.isValid);
        assertFalse(testHand.hasJoker);
        assertEquals(0, testHand.pairLists.size());
        assertEquals(0, testHand.straightLists.size());
        assertEquals(0, testHand.flushLists.size());
    }
    
    @Test 
    public void testSortComplicatedHand() {
        // I'm sorting this by myself first because otherwise it would be difficult to figure out
        // how the straight lists would be constructed.
        // Also, I tested Card.compareTo separately.
        List<Card> fullList = Interpreter.interpret("W, ah, ks, qs, js, jh, 10h, 9s, 7d, 4c, 3d, 3c, 2d");
        HandEvaluator.SimplifiedHand testHand = he.sortHand(new Hand(fullList));
        List<Card> heartList = Interpreter.interpret("ah, jh, 10h");
        List<Card> spadeList = Interpreter.interpret("ks, qs, js, 9s");
        List<Card> diamondList = Interpreter.interpret("7d, 3d, 2d");
        List<Card> clubList = Interpreter.interpret("4c, 3c");
        List<Card> jackList = Interpreter.interpret("js, jh");
        List<Card> aceList = Interpreter.interpret("ah");
        List<Card> threeList = Interpreter.interpret("3d, 3c");
        List<Card> aceStraightList = Interpreter.interpret("ah, ks, qs, js, 10h, 9s");
        List<Card> fourStraightList = Interpreter.interpret("4c, 3d, 2d");
        assertTrue(testHand.hasJoker);
        assertTrue(testHand.isValid);
        
        // Straights
        assertEquals(3, testHand.straightLists.size());
        Card aceHearts = new Card(14, Card.Suit.HEARTS);
        Card fourClubs = new Card(4, Card.Suit.CLUBS);
        Card sevenDiamonds = new Card(7, Card.Suit.DIAMONDS);
        Card[] singletonStraight = {sevenDiamonds};
        assertArrayEquals(aceStraightList.toArray(), testHand.straightLists.get(aceHearts).toArray());
        assertArrayEquals(fourStraightList.toArray(), testHand.straightLists.get(fourClubs).toArray());
        assertArrayEquals(singletonStraight, testHand.straightLists.get(sevenDiamonds).toArray());
        
        // Flush sets
        assertArrayEquals(heartList.toArray(), testHand.flushLists.get(Card.Suit.HEARTS).toArray());
        assertArrayEquals(spadeList.toArray(), testHand.flushLists.get(Card.Suit.SPADES).toArray());
        assertArrayEquals(diamondList.toArray(), testHand.flushLists.get(Card.Suit.DIAMONDS).toArray());
        assertArrayEquals(clubList.toArray(), testHand.flushLists.get(Card.Suit.CLUBS).toArray());
        
        // Collection
        assertArrayEquals(aceList.toArray(), testHand.pairLists.get(Integer.valueOf(14)).toArray());
        assertArrayEquals(jackList.toArray(), testHand.pairLists.get(Integer.valueOf(11)).toArray());
        assertArrayEquals(threeList.toArray(), testHand.pairLists.get(Integer.valueOf(3)).toArray());
        assertArrayEquals(singletonStraight, testHand.pairLists.get(Integer.valueOf(7)).toArray());
    }

    /**
     * Test method for {@link com.mishmash.rally.HandEvaluator#getBestCollection(com.mishmash.rally.HandEvaluator.SimplifiedHand)}.
     */
    @Test
    public void testGetBestCollection() {
        List<Card> handList = Interpreter.interpret("ah, as, kc, jd, 10d, 10c, 10s, 2d, 2h, 2s");
        HandEvaluator.SimplifiedHand testHand = he.sortHand(new Hand(handList));
        List<Card> tens = Interpreter.interpret("10s, 10d, 10c");
        List<Card> aces = Interpreter.interpret("as, ah");
        
        HandEvaluator.FiveCardHand testFch = he.getBestCollection(testHand);
        assertEquals(Hand.HandType.FULL_HOUSE, testFch.type);
        assertFalse(testFch.hasJoker);
        assertArrayEquals(tens.toArray(), testFch.importantList.toArray());
        assertArrayEquals(aces.toArray(), testFch.otherList.toArray());
        
        List<Card> jokerHandList = Interpreter.interpret("w, js, 10s, 10h");
        List<Card> tenList = Interpreter.interpret("10s, 10h");
        HandEvaluator.FiveCardHand jokerFch = he.getBestCollection(he.sortHand(new Hand(jokerHandList)));
        assertEquals(Hand.HandType.THREE_OF_A_KIND, jokerFch.type);
        assertTrue(jokerFch.hasJoker);
        assertArrayEquals(tenList.toArray(), jokerFch.importantList.toArray());
        assertArrayEquals(emptyArray, jokerFch.otherList.toArray());
        
        List<Card> fullHouseList = Interpreter.interpret("w, 8s, 8d, 7d, 7c");
        List<Card> eightList = Interpreter.interpret("8s, 8d");
        List<Card> sevenList = Interpreter.interpret("7d, 7c");
        HandEvaluator.FiveCardHand fullHouseFch = he.getBestCollection(he.sortHand(new Hand(fullHouseList)));
        assertEquals(Hand.HandType.FULL_HOUSE, fullHouseFch.type);
        assertTrue(fullHouseFch.hasJoker);
        assertArrayEquals(eightList.toArray(), fullHouseFch.importantList.toArray());
        assertArrayEquals(sevenList.toArray(), fullHouseFch.otherList.toArray());
        
        List<Card> fourList = Interpreter.interpret("4s, 4h, 4d, 4c");
        HandEvaluator.FiveCardHand fourKind = he.getBestCollection(he.sortHand(new Hand(fourList)));
        assertEquals(Hand.HandType.FOUR_OF_A_KIND, fourKind.type);
        assertFalse(fourKind.hasJoker);
        assertArrayEquals(fourList.toArray(), fourKind.importantList.toArray());
        assertArrayEquals(emptyArray, fourKind.otherList.toArray());
        
    }

    /**
     * Test method for {@link com.mishmash.rally.HandEvaluator#getBestStraight(com.mishmash.rally.HandEvaluator.SimplifiedHand)}.
     */
    @Test
    public void testGetBestStraight() {
        List<Card> noStraightList = Interpreter.interpret("w, as, ks, kh, qs");
        HandEvaluator.FiveCardHand noStraightHand = he.getBestStraight(he.sortHand(new Hand(noStraightList)));
        // The getBestStraight function isn't looking for pairs, so it will ignore the three of
        // a kind in this hand.
        assertEquals(Hand.HandType.HIGH_CARD, noStraightHand.type);
        assertTrue(noStraightHand.hasJoker);
        
        List<Card> listWithHole = Interpreter.interpret("10s, 9h, 8d, 6c, 3s");
        HandEvaluator.FiveCardHand holeHand = he.getBestStraight(he.sortHand(new Hand(listWithHole)));
        assertEquals(Hand.HandType.HIGH_CARD, holeHand.type);
        assertFalse(holeHand.hasJoker);
        assertArrayEquals(emptyArray, holeHand.importantList.toArray());
        assertArrayEquals(emptyArray, holeHand.otherList.toArray());
        
        List<Card> simpleStraightList = Interpreter.interpret("jh, 10s, 9d, 8c, 7h");
        HandEvaluator.FiveCardHand simpleStraight = he.getBestStraight(he.sortHand(new Hand(simpleStraightList)));
        assertEquals(Hand.HandType.STRAIGHT, simpleStraight.type);
        assertArrayEquals(simpleStraightList.toArray(), simpleStraight.importantList.toArray());
        
        List<Card> competingStraightList = Interpreter.interpret("ad, ks, qh, jh, 10s, 8d, 7c, 6h, 5h, 4s");
        HandEvaluator.FiveCardHand competingStraight = he.getBestStraight(he.sortHand(new Hand(competingStraightList)));
        List<Card> competingWinner = Interpreter.interpret("ad, ks, qh, jh, 10s");
        assertEquals(Hand.HandType.STRAIGHT, competingStraight.type);
        assertArrayEquals(competingWinner.toArray(), competingStraight.importantList.toArray());
        
        List<Card> twoPathList = Interpreter.interpret("ad, ks, kh, kd, qs, jd, 10c, 6h, 5h, 4s");
        HandEvaluator.FiveCardHand twoPathStraight = he.getBestStraight(he.sortHand(new Hand(twoPathList)));
        List<Card> twoPathWinner = Interpreter.interpret("ad, ks, qs, jd, 10c");
        assertEquals(Hand.HandType.STRAIGHT, twoPathStraight.type);
        assertArrayEquals(twoPathWinner.toArray(), twoPathStraight.importantList.toArray());

    }
    
    @Test
    public void testWildStaights() {
        List<Card> outsideStraightList = Interpreter.interpret("w, 10s, 9d, 8c, 7h");
        HandEvaluator.FiveCardHand outsideStraight = he.getBestStraight(he.sortHand(new Hand(outsideStraightList)));
        // The getBestStraight function isn't looking for pairs, so it will ignore the three of
        // a kind in this hand.
        List<Card> noJokerList = Interpreter.interpret("10s, 9d, 8c, 7h");
        assertEquals(Hand.HandType.STRAIGHT, outsideStraight.type);
        assertArrayEquals(noJokerList.toArray(), outsideStraight.importantList.toArray());
        
        List<Card> insideStraightList = Interpreter.interpret("w, js, 9d, 8c, 7h");
        HandEvaluator.FiveCardHand insideStraight = he.getBestStraight(he.sortHand(new Hand(insideStraightList)));
        List<Card> inJokerList = Interpreter.interpret("js, 9d, 8c, 7h");
        assertEquals(Hand.HandType.STRAIGHT, insideStraight.type);
        assertArrayEquals(inJokerList.toArray(), insideStraight.importantList.toArray());
        
        // This one has two paths as well as two competing straights
        List<Card> competingStraightList = Interpreter.interpret("w, ah, ks, kh, jh, 10s, 6d, 5c, 4h, 3c");
        HandEvaluator.FiveCardHand competingStraight = he.getBestStraight(he.sortHand(new Hand(competingStraightList)));
        List<Card> competingWinner = Interpreter.interpret("ah, ks, jh, 10s");
        assertEquals(Hand.HandType.STRAIGHT, competingStraight.type);
        assertArrayEquals(competingWinner.toArray(), competingStraight.importantList.toArray());
        
     // This one has two paths as well as two competing straights
        List<Card> twoHoleStraightList = Interpreter.interpret("w, ah, ks, kh, jh, 9s, 8d, 7c, 6d, 5c, 4h, 3c");
        HandEvaluator.FiveCardHand twoHoleStraight = he.getBestStraight(he.sortHand(new Hand(twoHoleStraightList)));
        List<Card> twoHoleWinner = Interpreter.interpret("jh, 9s, 8d, 7c");
        assertEquals(Hand.HandType.STRAIGHT, twoHoleStraight.type);
        assertArrayEquals(twoHoleWinner.toArray(), twoHoleStraight.importantList.toArray());
    }
    
    @Test
    public void testJokerGapRegression() {
        List<Card> bugList = Interpreter.interpret("w, ah, 7s, 3h, 2s");
        HandEvaluator.FiveCardHand noStraight = 
                he.getBestStraight(he.sortHand(new Hand(bugList)));
        assertEquals(Hand.HandType.HIGH_CARD, noStraight.type);
    }
    
    @Test
    public void testGetCorrectStraightFlushRegression() {
        List<Card> bugList = Interpreter.interpret("w, As, 7s, 6s, 5s, 4s, 3s, 2s");
        List<Card> bestSfList = Interpreter.interpret("7s, 6s, 5s, 4s");
        HandEvaluator.FiveCardHand sfHand = 
                he.getBestStraightFlush(he.sortHand(new Hand(bugList)));
        assertEquals(Hand.HandType.STRAIGHT_FLUSH, sfHand.type);
        assertArrayEquals(bestSfList.toArray(), sfHand.importantList.toArray());
    }

    /**
     * Test method for {@link com.mishmash.rally.HandEvaluator#getBestFlush(com.mishmash.rally.HandEvaluator.SimplifiedHand)}.
     */
    @Test
    public void testGetBestFlush() {
        List<Card> noFlushList = Interpreter.interpret("w, ah, 3h, 3d, 2c");
        HandEvaluator.FiveCardHand noFlushHand = he.getBestFlush(he.sortHand(new Hand(noFlushList)));
        // get best flush should not set the hand to anything other than the default.
        assertEquals((he.new FiveCardHand()).type, noFlushHand.type);
        
        List<Card> oneFlushList = Interpreter.interpret("as, kh, jh, 10s, 4s, 3s, 2s");
        HandEvaluator.FiveCardHand oneFlushHand = he.getBestFlush(he.sortHand(new Hand(oneFlushList)));
        assertEquals(Hand.HandType.FLUSH, oneFlushHand.type);
        List<Card> spadeList = Interpreter.interpret("as, 10s, 4s, 3s, 2s");
        assertArrayEquals(spadeList.toArray(), oneFlushHand.importantList.toArray());
        assertArrayEquals(emptyArray, oneFlushHand.otherList.toArray());
        
        List<Card> twoFlushList = Interpreter.interpret("kd, jh, 10h, 10d, 9h, 9d, 8h, 8d, 7h, 7d");
        HandEvaluator.FiveCardHand twoFlushHand = he.getBestFlush(he.sortHand(new Hand(twoFlushList)));
        List<Card> diamondList = Interpreter.interpret("kd, 10d, 9d, 8d, 7d");
        assertEquals(Hand.HandType.FLUSH, twoFlushHand.type);
        // Note that getBestFlush does not look for straight flushes, so the diamond flush 
        // is better here.
        assertArrayEquals(diamondList.toArray(), twoFlushHand.importantList.toArray());
        
        List<Card> wildFlushList = Interpreter.interpret("w, ks, kd, js, jd, 10s, 10d, 9s, 9d, 8s, 8d");
        HandEvaluator.FiveCardHand wildFlushHand = he.getBestFlush(he.sortHand(new Hand(wildFlushList)));
        List<Card> spadeFlushList = Interpreter.interpret("ks, js, 10s, 9s");
        assertEquals(Hand.HandType.FLUSH, wildFlushHand.type);
        // Spades should win the tiebreak as the higher suit, AND we should only count four cards
        // because the wild is in there.
        assertArrayEquals(spadeFlushList.toArray(), wildFlushHand.importantList.toArray());
        
    }

    /**
     * Test method for {@link com.mishmash.rally.HandEvaluator#getBestStraightFlush(com.mishmash.rally.HandEvaluator.SimplifiedHand)}.
     */
    @Test
    public void testGetBestStraightFlush() {
        List<Card> noStraightList = Interpreter.interpret("w, as, ks, kh, qs");
        HandEvaluator.FiveCardHand noStraightHand = he.getBestStraightFlush(he.sortHand(new Hand(noStraightList)));
        // The getBestStraight function isn't looking for pairs, so it will ignore the three of
        // a kind in this hand.
        assertEquals(Hand.HandType.HIGH_CARD, noStraightHand.type);
        assertTrue(noStraightHand.hasJoker);
        
        List<Card> wholeDeck = CardUtils.getWholeDeck();
        HandEvaluator.FiveCardHand deckHand = he.getBestStraightFlush(he.sortHand(new Hand(wholeDeck)));
        assertEquals(Hand.HandType.STRAIGHT_FLUSH, deckHand.type);
        List<Card> superRoyalFlush = Interpreter.interpret("as, ks, qs, js, 10s");
        assertFalse(deckHand.hasJoker);
        assertArrayEquals(superRoyalFlush.toArray(), deckHand.importantList.toArray());
        assertArrayEquals(emptyArray, deckHand.otherList.toArray());
        
        List<Card> trickyList = Interpreter.interpret("w, 5d, 4d, 3d, 2d, 2c" );
        HandEvaluator.FiveCardHand trickyHand = he.getBestStraightFlush(he.sortHand(new Hand(trickyList)));
        List<Card> trickySf = Interpreter.interpret("5d, 4d, 3d, 2d");
        assertEquals(Hand.HandType.STRAIGHT_FLUSH, trickyHand.type);
        assertArrayEquals(trickySf.toArray(), trickyHand.importantList.toArray());
        
    }
    
}
