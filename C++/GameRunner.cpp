#include <stdlib.h>
#include <array>
#include "Game.h"

static bool notAWinner;

void runGame(ostream &outputStream, const list <string> &playersList);

int main() {
    list <string> list = {"Chet", "Pat", "Sue"};
    runGame(cout, list);

}

void runGame(ostream &outputStream, const list <string> &playersList) {
    Game aGame(outputStream);

    for (string player : playersList) {
        aGame.add(player);
    }

    do {
        aGame.roll(rand() % 5 + 1);

        if (rand() % 9 == 7) {
            notAWinner = aGame.wrongAnswer();
        } else {
            notAWinner = aGame.wasCorrectlyAnswered();
        }
    } while (notAWinner);
}