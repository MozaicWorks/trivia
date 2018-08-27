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
		def currentPlayerNameFunction = this.&currentPlayerName.curry(this.players, this.currentPlayer)
		def currentPlayerInPenaltyBoxFunction = this.&isInPenaltyBox.curry(this.inPenaltyBox, this.currentPlayer)
		def printCurrentPlayerNameFunction = this.&printCurrentPlayerName.curry(currentPlayerNameFunction, printFunction)
		def printRollFunction = this.&printRoll.curry(roll, Console.&println)
		def isRollOddFunction = this.&isRollOdd.curry(roll)
		def printPlayerGetsOutOfPenaltyBoxFunction = this.&printPlayerGetsOutOfPenaltyBox.curry(currentPlayerNameFunction)
		def printNewLocationFunction = this.&printNewLocation.curry(currentPlayerNameFunction)
		def currentCategoryFunction = this.&pure_currentCategory.curry(places[currentPlayer])
		def printCurrentCategoryFunction = this.&printCurrentCategory.curry(currentCategoryFunction)
		def askQuestionFunction = this.&askQuestion

		int newPlace
		(isGettingOutOfPenaltyBox, newPlace) = pure_Roll(
				roll,
				isGettingOutOfPenaltyBox,
				currentPlayerNameFunction,
				currentPlayerInPenaltyBoxFunction,
				printCurrentPlayerNameFunction,
				printRollFunction,
				isRollOddFunction,
				printPlayerGetsOutOfPenaltyBoxFunction,
				this.places[currentPlayer],
				printNewLocationFunction,
				printCurrentCategoryFunction,
				askQuestionFunction
		)

		this.places[currentPlayer] = newPlace
	}

	private pure_Roll(final int roll,
	                  final boolean isGettingOutOfPenaltyBox,
	                  final currentPlayerNameFunction,
	                  final currentPlayerInPenaltyBoxFunction,
	                  final printCurrentPlayerNameFunction,
	                  final printRollFunction,
	                  final isRollOddFunction,
	                  final printPlayerGetsOutOfPenaltyBoxFunction,
	                  final int currentPlace, printNewLocationFunction, printCurrentCategoryFunction, askQuestionFunction) {
		printCurrentPlayerNameFunction()
		printRollFunction()

		boolean newIsGettingOutOfPenaltyBox = isGettingOutOfPenaltyBox
		int newPlace = currentPlace

		if (currentPlayerInPenaltyBoxFunction()) {
			if (isRollOddFunction()) {
				newIsGettingOutOfPenaltyBox = true

				printPlayerGetsOutOfPenaltyBoxFunction()
				newPlace = movePlayer(roll, currentPlace)

				printNewLocationFunction(newPlace)
				printCurrentCategoryFunction()
				askQuestionFunction()
			} else {
				println currentPlayerNameFunction() + " is not getting out of the penalty box"
				newIsGettingOutOfPenaltyBox = false
			}

		} else {
			newPlace = movePlayer(roll, currentPlace)

			printNewLocationFunction(newPlace)
			printCurrentCategoryFunction()
			askQuestionFunction()
		}

		return [newIsGettingOutOfPenaltyBox, newPlace]
	}

	private static printCurrentCategory(final currentCategoryFunction) {
		println "The category is " + currentCategoryFunction()
	}

	private static printNewLocation(final currentPlayerNameFunction, final int currentPlace) {
		println "${currentPlayerNameFunction()}'s new location is ${currentPlace}"
	}

	private static movePlayer(final int roll, final int place) {
		int newPlace = place
		newPlace = newPlace + roll
		if (newPlace > 11) newPlace = newPlace - 12
		return newPlace
	}

	private static printPlayerGetsOutOfPenaltyBox(final currentPlayerNameFunction) {
		println currentPlayerNameFunction() + " is getting out of the penalty box"
	}

	private static isRollOdd(final int roll) {
		return roll % 2 != 0
	}

	private static printRoll(final int roll, final printFunction) {
		printFunction "They have rolled a " + roll
	}

	private static printCurrentPlayerName(final currentPlayerFromListFunction, final printFunction) {
		printFunction currentPlayerFromListFunction() + " is the current player"
	}

	private static isInPenaltyBox(final boolean[] inPenaltyBox, final int currentPlayer) {
		inPenaltyBox[currentPlayer]
	}

	private static currentPlayerName(final players, final int currentPlayer) {
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
		return pure_currentCategory(places[currentPlayer])
	}

	private static pure_currentCategory(final int currentPlace) {
		if (currentPlace == 0) return "Pop"
		if (currentPlace == 4) return "Pop"
		if (currentPlace == 8) return "Pop"
		if (currentPlace == 1) return "Science"
		if (currentPlace == 5) return "Science"
		if (currentPlace == 9) return "Science"
		if (currentPlace == 2) return "Sports"
		if (currentPlace == 6) return "Sports"
		if (currentPlace == 10) return "Sports"
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
