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
		def printPlayerGetsOutOfPenaltyBoxFunction = this.&printPlayerGetsOutOfPenaltyBox.curry(printFunction, currentPlayerNameFunction)
		def movePlayerFunction = this.&movePlayer.curry(roll, this.places[currentPlayer])
		def printNewLocationFunction =
				this.&printNewLocation.curry(printFunction, currentPlayerNameFunction, movePlayerFunction)
		def currentCategoryFunction = this.&currentCategory.curry(places[currentPlayer])
		def printCurrentCategoryFunction = this.&printCurrentCategory.curry(printFunction, currentCategoryFunction)
		def askQuestionFunction = this.&askQuestion

		def newPlace
		def printIsNotGettingOutOfPenaltyBoxFunction = this.&printIsNotGettingOutOfPenaltyBox.curry(printFunction, currentPlayerNameFunction)
		def currentPlace = this.places[currentPlayer]

		def rollOddAndPlayerInPenaltyBoxFunction = this.&rollOddAndPlayerInPenaltyBox.curry(
				printPlayerGetsOutOfPenaltyBoxFunction,
				printNewLocationFunction,
				printCurrentCategoryFunction,
				askQuestionFunction,
				movePlayerFunction
		)

		def playerInPenaltyBoxAndRollEvenFunction = this.&playerInPenaltyBoxAndRollEven.curry(
				printIsNotGettingOutOfPenaltyBoxFunction,
				currentPlace
		)

		def playerNotInPenaltyBoxFunction = this.&playerNotInPenaltyBox.curry(
				printNewLocationFunction,
				printCurrentCategoryFunction,
				askQuestionFunction,
				isGettingOutOfPenaltyBox,
				movePlayerFunction
		)


		(isGettingOutOfPenaltyBox, newPlace) = pure_Roll(
				currentPlayerInPenaltyBoxFunction,
				printCurrentPlayerNameFunction,
				printRollFunction,
				isRollOddFunction,
				rollOddAndPlayerInPenaltyBoxFunction,
				playerInPenaltyBoxAndRollEvenFunction,
				playerNotInPenaltyBoxFunction
		)

		this.places[currentPlayer] = newPlace
	}

	private static pure_Roll(
			final currentPlayerInPenaltyBoxFunction,
			final printCurrentPlayerNameFunction,
			final printRollFunction,
			final isRollOddFunction,
			final rollOddAndPlayerInPenaltyBoxFunction,
			final playerInPenaltyBoxAndRollEvenFunction,
			final playerNotInPenaltyBoxFunction
	) {
		printCurrentPlayerNameFunction()
		printRollFunction()

		if (currentPlayerInPenaltyBoxFunction()) {
			if (isRollOddFunction()) {
				return rollOddAndPlayerInPenaltyBoxFunction()
			} else {
				return playerInPenaltyBoxAndRollEvenFunction()
			}
		} else {
			return playerNotInPenaltyBoxFunction()
		}

	}

	private static playerNotInPenaltyBox(
			printNewLocationFunction,
			printCurrentCategoryFunction,
			askQuestionFunction,
			final boolean isGettingOutOfPenaltyBox,
			movePlayerFunction) {
		printNewLocationFunction()
		printCurrentCategoryFunction()
		askQuestionFunction()
		return [isGettingOutOfPenaltyBox, movePlayerFunction()]
	}

	private static playerInPenaltyBoxAndRollEven(printIsNotGettingOutOfPenaltyBoxFunction, int currentPlace) {
		printIsNotGettingOutOfPenaltyBoxFunction()
		return [false, currentPlace]
	}

	private static rollOddAndPlayerInPenaltyBox(
			printPlayerGetsOutOfPenaltyBoxFunction,
			printNewLocationFunction,
			printCurrentCategoryFunction,
			askQuestionFunction,
			movePlayerFunction) {
		printPlayerGetsOutOfPenaltyBoxFunction()
		printNewLocationFunction()
		printCurrentCategoryFunction()
		askQuestionFunction()
		return [true, movePlayerFunction()]
	}

	private static printIsNotGettingOutOfPenaltyBox(final printFunction, final currentPlayerNameFunction) {
		printFunction currentPlayerNameFunction() + " is not getting out of the penalty box"
	}

	private static printCurrentCategory(final printFunction, final currentCategoryFunction) {
		printFunction "The category is " + currentCategoryFunction()
	}

	private static printNewLocation(final printFunction, final currentPlayerNameFunction, final currentPlaceFunction) {
		printFunction "${currentPlayerNameFunction()}'s new location is ${currentPlaceFunction()}"
	}

	private static movePlayer(final int roll, final int place) {
		int newPlace = place
		newPlace = newPlace + roll
		if (newPlace > 11) newPlace = newPlace - 12
		return newPlace
	}

	private static printPlayerGetsOutOfPenaltyBox(final printFunction, final currentPlayerNameFunction) {
		printFunction currentPlayerNameFunction() + " is getting out of the penalty box"
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
		def newPopQuestions
		def newScienceQuestions
		def newSportsQuestions
		def newRockQuestions

		def currentCategoryFunction = this.&currentCategory.curry(places[currentPlayer])
		def printFunction = Console.&println

		(newPopQuestions, newScienceQuestions, newSportsQuestions, newRockQuestions) =
				pure_askQuestion(
						currentCategoryFunction,
						printFunction,
						popQuestions.asImmutable(),
						scienceQuestions.asImmutable(),
						sportsQuestions.asImmutable(),
						rockQuestions.asImmutable()
				)

		this.popQuestions = newPopQuestions
		this.scienceQuestions = newScienceQuestions
		this.sportsQuestions = newSportsQuestions
		this.rockQuestions = newRockQuestions
	}

	private static pure_askQuestion(
			final currentCategoryFunction,
			final printFunction,
			final popQuestions,
			final scienceQuestions,
			final sportsQuestions,
			final rockQuestions) {
		def newPopQuestions = popQuestions
		def newScienceQuestions = scienceQuestions
		def newSportsQuestions = sportsQuestions
		def newRockQuestions = rockQuestions

		if (currentCategoryFunction() == "Pop") {
			newPopQuestions = printAndRemoveFirstValueFromCollection(newPopQuestions, printFunction)
		}
		if (currentCategoryFunction() == "Science") {
			newScienceQuestions = printAndRemoveFirstValueFromCollection(newScienceQuestions, printFunction)
		}

		if (currentCategoryFunction() == "Sports") {
			newSportsQuestions = printAndRemoveFirstValueFromCollection(newSportsQuestions, printFunction)
		}
		if (currentCategoryFunction() == "Rock") {
			newRockQuestions = printAndRemoveFirstValueFromCollection(newRockQuestions, printFunction)
		}

		return [newPopQuestions, newScienceQuestions, newSportsQuestions, newRockQuestions]
	}

	private static printAndRemoveFirstValueFromCollection(final collection, final printFunction) {
		def value
		def newCollection
		(value, newCollection) = immutable_removeFirst(collection)
		printFunction value
		return newCollection
	}

	private static immutable_removeFirst(final collection) {
		def value = collection.first()
		return [value, collection.drop(1)]
	}

	private static currentCategory(final int currentPlace) {
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
