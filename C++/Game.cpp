#include "Game.h"
#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <sstream>

using namespace std;


Game::Game(ostream& theOutStream) : outStream(theOutStream), currentPlayer(0), places{}, purses{} {
    for (int i = 0; i < 50; i++) {

        ostringstream oss(ostringstream::out);
        oss << "Pop Question " << i;

        popQuestions.push_back(oss.str());

        char str[255];
        sprintf(str, "Science Question %d", i);
        scienceQuestions.push_back(string(str));

        char str1[255];
        sprintf(str1, "Sports Question %d", i);
        sportsQuestions.push_back(string(str1));

        rockQuestions.push_back(createRockQuestion(i));
    }
}

string Game::createRockQuestion(int index) {
    char indexStr[127];
    sprintf(indexStr, "Rock Question %d", index);
    return indexStr;
}

bool Game::isPlayable() {
    return (howManyPlayers() >= 2);
}

bool Game::add(string playerName) {
    players.push_back(playerName);
    places[howManyPlayers()] = 0;
    purses[howManyPlayers()] = 0;
    inPenaltyBox[howManyPlayers()] = false;

    outStream << playerName << " was added" << endl;
    outStream << "They are player number " << players.size() << endl;
    return true;
}

int Game::howManyPlayers() {
    return players.size();
}

void Game::roll(int roll) {
    outStream << players[currentPlayer] << " is the current player" << endl;
    outStream << "They have rolled a " << roll << endl;

    if (inPenaltyBox[currentPlayer]) {
        if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;

            outStream << players[currentPlayer] << " is getting out of the penalty box" << endl;
            places[currentPlayer] = places[currentPlayer] + roll;
            if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

            outStream << players[currentPlayer] << "'s new location is " << places[currentPlayer] << endl;
            outStream << "The category is " << currentCategory() << endl;
            askQuestion();
        } else {
            outStream << players[currentPlayer] << " is not getting out of the penalty box" << endl;
            isGettingOutOfPenaltyBox = false;
        }

    } else {

        places[currentPlayer] = places[currentPlayer] + roll;
        if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

        outStream << players[currentPlayer] << "'s new location is " << places[currentPlayer] << endl;
        outStream << "The category is " << currentCategory() << endl;
        askQuestion();
    }

}

void Game::askQuestion() {
    if (currentCategory() == "Pop") {
        outStream << popQuestions.front() << endl;
        popQuestions.pop_front();
    }
    if (currentCategory() == "Science") {
        outStream << scienceQuestions.front() << endl;
        scienceQuestions.pop_front();
    }
    if (currentCategory() == "Sports") {
        outStream << sportsQuestions.front() << endl;
        sportsQuestions.pop_front();
    }
    if (currentCategory() == "Rock") {
        outStream << rockQuestions.front() << endl;
        rockQuestions.pop_front();
    }
}


string Game::currentCategory() {
    if (places[currentPlayer] == 0) return "Pop";
    if (places[currentPlayer] == 4) return "Pop";
    if (places[currentPlayer] == 8) return "Pop";
    if (places[currentPlayer] == 1) return "Science";
    if (places[currentPlayer] == 5) return "Science";
    if (places[currentPlayer] == 9) return "Science";
    if (places[currentPlayer] == 2) return "Sports";
    if (places[currentPlayer] == 6) return "Sports";
    if (places[currentPlayer] == 10) return "Sports";
    return "Rock";
}

bool Game::wasCorrectlyAnswered() {
    if (inPenaltyBox[currentPlayer]) {
        if (isGettingOutOfPenaltyBox) {
            outStream << "Answer was correct!!!!" << endl;
            purses[currentPlayer]++;
            outStream << players[currentPlayer]
                 << " now has "
                 << purses[currentPlayer]
                 << " Gold Coins." << endl;

            bool winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
        } else {
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;
            return true;
        }


    } else {

        outStream << "Answer was corrent!!!!" << endl;
        purses[currentPlayer]++;
        outStream << players[currentPlayer]
             << " now has "
             << purses[currentPlayer]
             << " Gold Coins." << endl;

        bool winner = didPlayerWin();
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;

        return winner;
    }
}

bool Game::wrongAnswer() {
    outStream << "Question was incorrectly answered" << endl;
    outStream << players[currentPlayer] + " was sent to the penalty box" << endl;
    inPenaltyBox[currentPlayer] = true;

    currentPlayer++;
    if (currentPlayer == players.size()) currentPlayer = 0;
    return true;
}


bool Game::didPlayerWin() {
    return !(purses[currentPlayer] == 6);
}
