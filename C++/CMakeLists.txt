cmake_minimum_required(VERSION 3.7)
project(C__)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES
        Game.cpp
        Game.h
        GameRunner.cpp)

add_executable(C__ ${SOURCE_FILES})