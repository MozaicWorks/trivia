package com.adaptionsoft.games.uglytrivia

class Game {
	ArrayList players = new ArrayList()
	int[] places = new int[6]
	int[] purses = new int[6]
	boolean[] inPenaltyBox = new boolean[6]

	LinkedList popQuestions = new LinkedList()
	LinkedList scienceQuestions = new LinkedList()
	LinkedList sportsQuestions = new LinkedList()
	LinkedList rockQuestions = new LinkedList()

	int currentPlayer = 0
	boolean isGettingOutOfPenaltyBox

	Game() {
		50.times { def i ->
			popQuestions.addLast("Pop Question " + i)
			scienceQuestions.addLast(("Science Question " + i))
			sportsQuestions.addLast(("Sports Question " + i))
			rockQuestions.addLast(createRockQuestion(i))
		}
	}

	String createRockQuestion(int index) {
		return "Rock Question " + index
	}

	boolean isPlayable() {
		return (howManyPlayers() >= 2)
	}

	boolean add(String playerName) {
		players.add(playerName)
		places[howManyPlayers()] = 0
		purses[howManyPlayers()] = 0
		inPenaltyBox[howManyPlayers()] = false

		println playerName + " was added"
		println "They are player number " + players.size()
		return true
	}

	int howManyPlayers() {
		return players.size()
	}

	void roll(int roll) {
		def printFunction = Console.&println
		def currentPlayerNameFunction = this.&currentPlayerFromList.curry(this.players, this.currentPlayer)
		def currentPlayerInPenaltyBoxFunction = this.&isInPenaltyBox.curry(this.inPenaltyBox, this.currentPlayer)
		def printCurrentPlayerNameFunction = this.&printCurrentPlayerName.curry(currentPlayerNameFunction, printFunction)
		def printRollFunction = this.&printRoll.curry(roll, Console.&println)
		def isRollOddFunction = this.&isRollOdd.curry(roll)

		pure_Roll(
				roll,
				currentPlayerNameFunction,
				currentPlayerInPenaltyBoxFunction,
				printCurrentPlayerNameFunction,
				printRollFunction,
				isRollOddFunction
		)
	}

	private pure_Roll(final int roll,
	                  final currentPlayerFromListFunction,
	                  final currentPlayerInPenaltyBoxFunction,
	                  final printCurrentPlayerNameFunction,
	                  final printRollFunction,
	                  final isRollOddFunction) {
		printCurrentPlayerNameFunction()
		printRollFunction()

		if (currentPlayerInPenaltyBoxFunction()) {
			if (isRollOddFunction()) {
				isGettingOutOfPenaltyBox = true

				println players[currentPlayer] + " is getting out of the penalty box"
				places[currentPlayer] = places[currentPlayer] + roll
				if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12

				println "${currentPlayerFromListFunction()}'s new location is ${places[currentPlayer]}"
				println "The category is " + currentCategory()
				askQuestion()
			} else {
				println currentPlayerFromListFunction() + " is not getting out of the penalty box"
				isGettingOutOfPenaltyBox = false
			}

		} else {

			places[currentPlayer] = places[currentPlayer] + roll
			if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12

			println "${currentPlayerFromListFunction()}'s new location is ${places[currentPlayer]}"
			println "The category is " + currentCategory()
			askQuestion()
		}
	}

	private static isRollOdd(final int roll) {
		return roll % 2 != 0
	}

	private static printRoll(final int roll, printFunction) {
		printFunction "They have rolled a " + roll
	}

	private static printCurrentPlayerName(currentPlayerFromListFunction, printFunction) {
		printFunction currentPlayerFromListFunction() + " is the current player"
	}

	private static isInPenaltyBox(boolean[] inPenaltyBox, int currentPlayer) {
		inPenaltyBox[currentPlayer]
	}

	private static currentPlayerFromList(ArrayList players, int currentPlayer) {
		players[currentPlayer]
	}

	private void askQuestion() {
		if (currentCategory() == "Pop")
			println popQuestions.removeFirst()
		if (currentCategory() == "Science")
			println scienceQuestions.removeFirst()
		if (currentCategory() == "Sports")
			println sportsQuestions.removeFirst()
		if (currentCategory() == "Rock")
			println rockQuestions.removeFirst()
	}


	private String currentCategory() {
		if (places[currentPlayer] == 0) return "Pop"
		if (places[currentPlayer] == 4) return "Pop"
		if (places[currentPlayer] == 8) return "Pop"
		if (places[currentPlayer] == 1) return "Science"
		if (places[currentPlayer] == 5) return "Science"
		if (places[currentPlayer] == 9) return "Science"
		if (places[currentPlayer] == 2) return "Sports"
		if (places[currentPlayer] == 6) return "Sports"
		if (places[currentPlayer] == 10) return "Sports"
		return "Rock"
	}

	boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]) {
			if (isGettingOutOfPenaltyBox) {
				println "Answer was correct!!!!"
				purses[currentPlayer]++
				println "${players[currentPlayer]} now has ${purses[currentPlayer]} Gold Coins."

				boolean winner = didPlayerWin()
				currentPlayer++
				if (currentPlayer == players.size()) currentPlayer = 0

				return winner
			} else {
				currentPlayer++
				if (currentPlayer == players.size()) currentPlayer = 0
				return true
			}


		} else {

			println "Answer was corrent!!!!"
			purses[currentPlayer]++
			println "${players[currentPlayer]} now has ${purses[currentPlayer]} Gold Coins."

			boolean winner = didPlayerWin()
			currentPlayer++
			if (currentPlayer == players.size()) currentPlayer = 0

			return winner
		}
	}

	boolean wrongAnswer() {
		println "Question was incorrectly answered"
		println players[currentPlayer] + " was sent to the penalty box"
		inPenaltyBox[currentPlayer] = true

		currentPlayer++
		if (currentPlayer == players.size()) currentPlayer = 0
		return true
	}


	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6)
	}
}
