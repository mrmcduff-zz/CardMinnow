Hello, and welcome to CardMinnow! Please take a few moments to read this document to better understand the program.

CardMinnow is a command-line interface that allows you to evaluate poker hands. It is written in Java and compatible with JREs of version 1.6 and above.

---Setup---
1. Check the java version on the system you're using. To do this, open a terminal or command prompt and type "java -version". If the command prompt reports that it doesn't understand 'java', then you may need to install a runtime environment from http://www.java.com/getjava/ . If you have java installed, but still can't get a version number to come up, check your PATH variable and ensure than the java/bin folder is on the path.
2. Download the CardMinnow.jar file from https://github.com/mrmcduff/CardMinnow/downloads . In your command window / terminal, navigate to the folder containing CardMinnow.jar and type "java -jar CardMinnow.jar".
3. The program is now running.

---Operation---
CardMinnow is a simple command-line shell tool that evaluates lists of cards for their optimal five-card poker hand. There is no minimum or maximum on the number of cards in a hand, and you can put a joker in your hand.

- Input - 
CardMinnow accepts a list of cards separated by spaces, commas, semicolons, or any combination thereof. A card is represented by a value part and a suit part. The value is any number between 2 and 14, though you can use the letters J, Q, K, and A to represent 11, 12, 13, and 14 respectively. The suit part is a one letter abbreviation of the four poker suits, S for spades, H for hearts, D for diamonds, and C for clubs. To enter a joker, type W (for wild). Capitalization does not matter. There should not be a space between the value part and the suit part (spaces separate tokens).

- Examples of acceptable input -
> 10h, 11s, AD; 2h      3c
> 2h;2s,,,,,,w
> 7s 8s 9s 10s js qs ks as ah kh qh jh 10h 9h 8h 7h w

If you give CardMinnow bad input, it will tell you which items it doesn't understand and ask you to try again. 

- Examples of bad tokens -
> Ace of hearts, ten of diamonds
> a h 10 d
> ah:10d
> 92h

A hand is not allowed to have duplicate cards, and CardMinnow will reject such hands because you're only allowed to play poker with one deck. This does effectively limit hand size to fifty-three (the deck plus a joker).

- Examples of bad hands -
> Ah, 9h, w, Ah
> W, 10S, 9S, W

If you'd like an explanation of the rules, simply type 'rules' or 'help'. Both will bring up the in-game rules explanation. If you'd like to quit, type 'exit' or 'quit' (or just Ctrl-C out, but then you don't get the goodbye message). Again, capitalization does not matter to CardMinnow.

- Scoring -
A hand is scored as the best possible five card hand that can be created using the given cards. If you have two cards, the best you can get is a pair. If you have three cards, the best you can get is three of a kind. Flushes and straights must be five cards long. CardMinnow picks out the best possible scoring hand if there is more than one possible score.

For instance, the hand 'ah kh qh jh 10h 10s 10d' would be scored as a royal flush instead of three of a kind because the royal flush is higher.

As a reminder, the rank of poker hands in descending order is:
Five of a kind (only possible with a wild card)
Straight flush
Four of a kind
Full House (three of one card and two of another)
Flush
Straight
Three of a kind
Two pair
Pair
High Card

- Scoring with a joker -
While a joker can be used to give you a better hand class, it is never used in the scoring of the actual hand.
 
For instance, the hand ' w, ks, qs, js, 10h' is a king-high straight, not an ace-high straight. If you were playing against someone with an equal hand, the player with the best natural hand wins. (CardMinnow does not support interactive poker. It just evaluates hands.) In fact, a joker is never used in the evaluation of the hand, except for one specific hand: 'w'. Just a joker is a joker-high hand. If we were ranking it, joker-high would lose to 2-high. Both hands are impossible, of course, unless you're only playing with one card.

- Scoring suits -
Although suits are not commonly used to determine victors, they do have a ranking. As this program was written in North Carolina, it uses the North American standard of ranking the suits in reverse alphabetical order, so Spades > Hearts > Diamonds > Clubs. This only comes into effect when CardMinnow is picking the best possible hand and there are two otherwise equal hands of different suits.

For instance, '10h, 9h, 7h, 4h, 2h, 10d, 9d, 7d, 4d, 2d' would result in a 10-high flush of hearts, because hearts are greater than diamonds. If your hand consisted of the whole deck, it would score as a royal flush of spades.

---Source Files---
All files can be found at https://github.com/mrmcduff/CardMinnow . CardMinnow was built using Eclipse in standard package format using com.mishmash.rally as the package. You can clone the repository and import the project into your workspace if you'd like to build. You will need JDK 1.6 or above and JUnit 4 if you'd like to run the tests. The 'doc' folder contains the javadoc html files documenting all public methods of all classes. The 'src' folder contains the source code, and the 'test' folder contains the JUnit tests.

CardMinnow was written using abbreviated TDD, where most tests were written prior to development and most methods are tested. A few regression tests for particularly tricky bugs were created and left in the test code. Some methods were left without automated tests due to time constraints. Despite the fact that CardMinnow is a small program, care was taken to optimize operations where possible and productive, and the code is commented to reflect any areas where I deliberately chose not to optimize for the sake of clarity (and because it would only net around twenty operations).

