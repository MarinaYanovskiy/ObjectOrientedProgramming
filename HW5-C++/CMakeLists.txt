cmake_minimum_required(VERSION 3.24.2)
project(HW5-C++)

set(CMAKE_CXX_STANDARD 11)
        
include_directories(Provided/part1)
include_directories(Provided/part2)

add_executable(HW5-C++
        Provided/part1/Observer.h
        Provided/part1/OOP5EventException.h
        Provided/part1/Subject.h
        
        Provided/part2/Printer.h
        Provided/part2/TransposeList.h
        Provided/part2/List.h
        Provided/part2/Utilities.h
        Provided/part2/CellType.h
        Provided/part2/Direction.h
        Provided/part2/BoardCell.h
        Provided/part2/GameBoard.h
        Provided/part2/MoveVehicle.h
        Provided/part2/RushHour.h
        
        Provided/part1/Part1Examples.cpp
#        Provided/part2/Part2Examples.cpp

        Provided/part1/Observer.h)
