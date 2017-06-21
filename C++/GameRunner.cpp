#include <stdlib.h>
#include "Game.h"

static bool notAWinner;

void runGame();

int main()
{
	runGame();

}

void runGame() {
	Game aGame(cout);

	aGame.add("Chet");
	aGame.add("Pat");
	aGame.add("Sue");

	do
	{

		aGame.roll(rand() % 5 + 1);

		if (rand() % 9 == 7)
		{
			notAWinner = aGame.wrongAnswer();
		}
		else
		{
			notAWinner = aGame.wasCorrectlyAnswered();
		}
	} while (notAWinner);
}
