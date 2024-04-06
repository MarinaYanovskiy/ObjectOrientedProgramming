#ifndef OOP5_CHECKWIN_H
#define OOP5_CHECKWIN_H

#include "GameBoard.h"
#include "BoardCell.h"
#include "Utilities.h"
#include "Direction.h"
#include "CellType.h"

// Structure to check if the game is won
template<typename Board>
struct CheckWin {
private:
    // Helper structure to check if the red car is in the correct position to exit
    template<CellType RedCarType, int RedCarRow, int RedCarLength>
    struct CheckRedCarPosition {
        static constexpr bool value = (RedCarType == X) && (RedCarRow == (Board::length - RedCarLength));
    };

    // Helper structure to find the row where the red car is located
    template<typename Cells>
    struct FindRedCar;

    // Recursive case: continue searching for the red car in the remaining rows
    template<typename Head, typename... Tail>
    struct FindRedCar<List<Head, Tail...>> {
        static constexpr int row = Board::length - (1 + sizeof...(Tail)); // Calculate current row index
        static constexpr bool value = (Head::type == X) || FindRedCar<List<Tail...>>::value; // Check if the red car is found in this row or in subsequent rows
    };

    // Base case: reached the end of the rows
    template<typename Head>
    struct FindRedCar<List<Head>> {
        static constexpr bool value = (Head::type == X); // Check if the red car is found in the last row
        static constexpr int row = Board::length - 1; // Last row index
    };

    // Helper structure to find the row index where the red car is located
    template<int Row, typename BoardCells>
    struct FindRedCarRow;

    // Recursive case: continue searching for the red car in the remaining rows
    template<int Row, typename Head, typename... Tail>
    struct FindRedCarRow<Row, List<Head, Tail...>> {
        static constexpr int value = ConditionalInteger<Row, FindRedCarRow<(Row - 1), List<Tail...>>::value, (Head::type == X)>::value; // If the red car is found in this row, return the row index, otherwise continue searching
    };

    // Base case: reached the end of the rows
    template<int Row, typename Head>
    struct FindRedCarRow<Row, List<Head>> {
        static constexpr int value = ConditionalInteger<Row, (Head::type == X)>::value; // If the red car is found in this row, return the row index
    };

    // Helper structure to find the length of the red car at a specific row
    template<int Row, typename BoardCells>
    struct RedCarLengthAtRow;

    // Recursive case: continue searching for the red car in the remaining cells of the row
    template<int Row, typename Head, typename... Tail>
    struct RedCarLengthAtRow<Row, List<Head, Tail...>> {
        static constexpr int value = ConditionalInteger<(Head::type == X), Head::length, RedCarLengthAtRow<Row, List<Tail...>>::value>::value; // If the red car is found in this cell, return its length, otherwise continue searching
    };

    // Base case: reached the end of the row
    template<int Row, typename Head>
    struct RedCarLengthAtRow<Row, List<Head>> {
        static constexpr int value = ConditionalInteger<(Head::type == X), Head::length, 0>::value; // If the red car is found in this cell, return its length
    };

public:
    static constexpr int redCarRow = FindRedCar<Board>::row; // Find the row index where the red car is located
    static constexpr bool result = CheckRedCarPosition<X, redCarRow, RedCarLengthAtRow<redCarRow, typename Board::board>::value>::value; // Check if the red car is in the correct position to exit
};

#endif // OOP5_CHECKWIN_H
