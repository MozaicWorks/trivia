#include <stdlib.h>
#include <array>
#include <fstream>
#include <sstream>
#include "Game.h"

static bool notAWinner;

string goldenMasterFileName(int randomSeed);

string goldenMasterFilePath(string path, int randomSeed);

void runGame(ostream &outputStream, const list <string> &playersList);

void testGame();

void saveOutput(string path);

void compareFiles(string goldenMasterPath, string outputPath);

string readFileToString(const string &filePath);

void assertEquals(string expected, string actual, string message);

int main() {
    testGame();

//    list <string> list = {"Chet", "Pat", "Sue"};
//    runGame(cout, list);
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

void testGame() {
    string goldenMasterPath("./");
    string outputPath("");
//    saveOutput(goldenMasterPath); // save initial golden master

    saveOutput(outputPath);

    compareFiles(goldenMasterPath, outputPath);
}

void compareFiles(string goldenMasterPath, string outputPath) {
    for (int i = 1; i < 256; i++) {
        const string theGoldenMasterFilePath = goldenMasterFilePath(goldenMasterPath, i);
        string goldenMasterContent = readFileToString(goldenMasterFilePath(goldenMasterPath, i));

        const string theOutputFilePath = goldenMasterFilePath(outputPath, i);
        string outputContent = readFileToString(theOutputFilePath);

        cout << "Checking " << theGoldenMasterFilePath << " with " << theOutputFilePath;
        assertEquals(goldenMasterContent, outputContent, "Failed for " + to_string(i));
    }
}

void assertEquals(string expected, string actual, string message) {
    if (expected == actual) {
        cout << "..." << "Equal" << endl;
    } else {
        cout << "..." << "Failed: " << message << endl;
        throw message;
    }
}

string readFileToString(const string &filePath) {
    ifstream file(filePath);
    return string((istreambuf_iterator<char>(file)), istreambuf_iterator<char>());
}

void saveOutput(string path) {
    for (int randomSeed = 1; randomSeed < 256; randomSeed++) {
        srand(randomSeed);
        list <string> list = {"Chet", "Pat", "Sue"};

        ofstream goldenMaster(goldenMasterFilePath(path, randomSeed));
        runGame(goldenMaster, list);
        goldenMaster.close();
    }
}

string goldenMasterFileName(int randomSeed) {
    return "Golden" + to_string(randomSeed) + ".txt";
}

string goldenMasterFilePath(string path, int randomSeed) {
    return path + goldenMasterFileName(randomSeed);
}
