package com.mishmash.rally;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
            
    private final String STANDARD_PROMPT = "Input hand or command: \n";
    private final String ERR_PROMPT = "Try again:\n";
    private final String MANY_ERR_PROMPT = "It seems like you're having trouble with your input. " +
            "If you'd like to know more about the rules of CardMinnow, just type 'rules' or 'help'. " +
            "I'm only a simple program and can't understand everything that you users can.\n";
    
    private final String HELP = "help";
    private final String RULES = "rules";
    private final String EXIT = "exit";
    private final String QUIT = "quit";
    private final String GOODBYE = "Goodbye, and thanks for playing CardMinnow.\n";
    private final String YOU_HAVE = "Your best hand is: ";
    
    private final String EXPLAIN = "explain\n";
    private final int EXIT_VALUE = -1;
    
    public CardMinnowShell() {}
    
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
    
    
    private void introduce(BufferedWriter bw) throws IOException{
        if (bw != null) {
            bw.write(INTRODUCTION);
            bw.flush();
        }
    }
    
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
                HandEvaluator he = new HandEvaluator();
                Hand hand = new Hand(Interpreter.interpret(trimmed));
                he.evaluate(hand);
                bw.write(YOU_HAVE);
                bw.write(hand.getDescription());
                bw.write("\n");
                bw.flush();
                numErrors = 0;
            } catch (IllegalArgumentException iae) {
                bw.write(iae.toString());
                numErrors = oldErrors + 1;
            }
        }
        return numErrors;
    }
    

}
