#include <stdlib.h>
#include <array>
#include <fstream>
#include <sstream>
#include "Game.h"

static bool notAWinner;

void runGame(ostream &outputStream, const list <string> &playersList);

void testGame();

void saveGoldenMaster(string path);

void compareFiles(string goldenMasterPath, string outputPath);

basic_stringstream<char, std::char_traits<char>, std::allocator<char>>::__string_type readFileToString(
        const string &filePath);

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
    //saveGoldenMaster("./"); // save initial golden master

    saveGoldenMaster("/output/");

    compareFiles("./", "output/");
}

void compareFiles(string goldenMasterPath, string outputPath) {
    for (int i = 1; i < 256; i++) {
        const string &goldenMasterFilePath = goldenMasterPath + "Golden" + to_string(i) + ".txt";
        const string &outputFilePath = outputPath + to_string(i) + ".txt";

        string goldenMasterContent = readFileToString(goldenMasterFilePath);
        string outputContent = readFileToString(outputFilePath);

        cout << "Checking " << goldenMasterFilePath << " with " << outputFilePath;
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

void saveGoldenMaster(string path) {
    for (int randomSeed = 1; randomSeed < 256; randomSeed++) {
        srand(randomSeed);
        list <string> list = {"Chet", "Pat", "Sue"};

        ofstream goldenMaster;
        goldenMaster.open(path + "Golden" + to_string(randomSeed) + ".txt");
        runGame(goldenMaster, list);
        goldenMaster.close();
    }
}

