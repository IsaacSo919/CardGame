Card Game
Software Development CA1: Multi-threaded Java Card Game (Paired Programming Project).

General Rules
Can be played by 2 or more players.
Game is played with cards which have only a numerical face value and no suit.
8n cards are used in the game (where n is the number of players).
The face value of the cards is from 1 to n. (i.e 1,2,3,..., n)
Aim of the game is to obtain a hand of 4 of the same cards.
Cards are dealt from the pack, starting with the players, in a round-robin fashion, until each player has 4 cards.
Cards are then dealt to an equal number of decks, which also contain 4 cards, again in a round-robin fashion with the remaining cards.
All players play simultaneously.
Player Actions
Draw a card from the top of their deck.
Discard a chosen card from their now 5-card hand onto the bottom of the next player's deck.
Keep going until 4 equal cards are obtained by one of the players. This player wins and all other players lose.
How to run test?
Using JUnit 4. command java -cp .:path/to/junit.jar:path/to/hamcrest.jar org.junit.runner.JUnitCore YourTestClassName (CardGameTest.java, ClassTest.java, PlayerTest.java, DeckTest.java)
