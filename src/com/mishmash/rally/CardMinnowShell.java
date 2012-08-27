package com.mishmash.rally;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * This class handles the interactive shell prompt that is the interface for the game.
 * The only logic handled here is whether or not to show an advanced error message, whether
 * to quit, or to display the rules.
 * 
 * @author mrmcduff
 *
 */
public class CardMinnowShell {
    
    private final String INTRODUCTION = "Welcome to CardMinnow, an interactive command " +
            "line game that evaluates poker hands.\n" +
            "Just input a poker hand, like " +
            "'ah, js, 10d, 9c, 7d' to have it evaluated.\n" +
            "You can use 'w' (wild) for a joker.\n" +
            "A hand can only contain one of each card.\n" +
            "If you'd like to know more about the rules, just " +
            "type 'rules' or 'help'. \n" +
            "If you'd like to quit, type 'quit' or 'exit'.\n";
            
    private final String STANDARD_PROMPT = "Input hand or command:\n";
    private final String ERR_PROMPT = "Try again:\n";
    private final String MANY_ERR_PROMPT = "It seems like you're having trouble with your input. \n" +
            "If you'd like to know more about the rules of CardMinnow, just type 'rules' or 'help'. \n" +
            "I'm only a simple program and can't understand everything that users can. Have mercy.\n";
    
    private final String HELP = "help";
    private final String RULES = "rules";
    private final String EXIT = "exit";
    private final String QUIT = "quit";
    private final String GOODBYE = "Goodbye, and thanks for playing CardMinnow.\n";
    private final String YOU_HAVE = "Your best hand is: ";
    private final String INVALID_HAND = "That's an invalid hand. " +
    		"Remember, you're not allowed to have duplicates.\n";
    
    private final String EXPLAIN = 
            "CardMinnow can evaluate any set of cards for its optimal 5-card poker hand.\n" +
    		"Every card must be unique, however, so you can't have two aces of spades, for instance.\n" +
    		"I can understand something like 12h, qh, or Qh to be the Queen of Hearts.\n" +
    		"I'm bad at reading, though, so I won't understand you if you type 'Queen of Hearts'.\n" +
    		"No matter how many cards you enter, I evaluate for the best five card hand.\n" +
    		"If you enter 'as, ks, qs, js, 10s, 9s, 8s', I'll find the royal flush.\n" +
    		"Jokers don't count towards evaluation except in a single-card hand, \n" +
    		"so 'w ks qs js 10s' is a king-high straight flush, not a royal flush.\n" +
    		"Separate your card entries by spaces, commas, or semicolons.\n";
    
    // This is the only error value that means "quit now"
    private final int EXIT_VALUE = -1;
    
    /**
     * Default constructor. Requires no args.
     */
    public CardMinnowShell() {}
    
    /**
     * Runs the interactive shell that is the interface for the game. This method
     * runs until the user types 'exit' or 'quit'.
     */
    public void runShell() {
        boolean exitRequested = false;
        
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter buffout = new BufferedWriter(new OutputStreamWriter(System.out));
        int numErrors = 0;
        try {
            introduce(buffout);
            while(!exitRequested) {
                prompt(buffout, numErrors);
                String input = buff.readLine();
                numErrors = evaluateInput(input, buffout, numErrors);
                if (numErrors == EXIT_VALUE) {
                    exitRequested = true;
                }
            }
        } catch (IOException ioe) {
            System.out.println("Sorry, but an IO Exception has occured. Please try restarting the program.");
        }
    }
    
    /**
     * Prints the introduction to the game into the given buffered writer.
     * @param bw
     * A BufferedWriter used to write output to the user.
     * 
     * @throws IOException
     * If one is thrown by the BufferedWriter.
     */
    private void introduce(BufferedWriter bw) throws IOException{
        if (bw != null) {
            bw.write(INTRODUCTION);
            bw.flush();
        }
    }
    
    /**
     * Propmts the user for input.
     * @param bw
     * A buffered writer into which output is pumped.
     * 
     * @param numErrors
     * The number of consecutive 'bad input' lines from the user.
     * 
     * @throws IOException
     * If the BufferedWriter throws one.
     */
    private void prompt(BufferedWriter bw, int numErrors) throws IOException {
        if (bw != null) {
            if (numErrors == 0) {
                bw.write(STANDARD_PROMPT);
                bw.flush();
            } else if (numErrors < 2) {
                bw.write(ERR_PROMPT);
                bw.flush();
            } else {
                bw.write(MANY_ERR_PROMPT);
                bw.flush();
            }
        }
    }
    
    /**
     * Not <i>much</i> evaluation is done here. We check for keywords, then pass the string on
     * to the Interpreter. If we get exceptions, we catch them and relay the information back
     * to the user. We do keep track of how many 'bad' items in a row the user inputs to give
     * slightly different feedback.
     * 
     * @param input
     * The input string from the user.
     * 
     * @param bw
     * A BufferedWriter into which to write output.
     * 
     * @param oldErrors
     * The number of errors in a row that the user has entered.
     * 
     * @return
     * The new number of errors, usually old+1 or 0.
     * 
     * @throws IOException
     * If the BufferedWriter throws one.
     */
    private int evaluateInput(String input, BufferedWriter bw, int oldErrors) throws IOException {
        String trimmed = input.trim();
        int numErrors = oldErrors;
        if (trimmed.equalsIgnoreCase(HELP) || trimmed.equalsIgnoreCase(RULES)) {
            bw.write(EXPLAIN);
            bw.flush();
            numErrors = 0;
        } else if (trimmed.equalsIgnoreCase(EXIT) || trimmed.equalsIgnoreCase(QUIT)) {
            bw.write(GOODBYE);
            bw.flush();
            // This is the only return value that means "quit now."
            numErrors = EXIT_VALUE;
        } else {
            try {
                Hand hand = new Hand(Interpreter.interpret(trimmed));
                if (hand.isValid()) {
                    bw.write(YOU_HAVE);
                    bw.write(hand.getDescription());
                    bw.write("\n");
                    numErrors = 0;
                } else {
                    bw.write(INVALID_HAND);
                    numErrors = oldErrors +1;
                }
                bw.flush();
            } catch (IllegalArgumentException iae) {
                bw.write(iae.getMessage() + '\n');
                numErrors = oldErrors + 1;
            }
        }
        return numErrors;
    }
    

}
