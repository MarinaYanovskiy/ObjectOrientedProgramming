#ifndef OOP5_CHECKSOLUTION_H
#define OOP5_CHECKSOLUTION_H

#include "GameBoard.h"
#include "List.h"
#include "CheckWin.h"
#include "MoveVehicle.h"

// Structure to check if the given sequence of moves solves the game
template<typename Board, typename Moves>
struct CheckSolution {
private:
    // Helper structure to apply the sequence of moves to the game board
    template<typename BoardType, typename MovesList>
    struct ApplyMoves;

    // Recursive case: apply the first move to the board and continue applying the remaining moves
    template<typename BoardType, typename Head, typename... Tail>
    struct ApplyMoves<BoardType, List<Head, Tail...>> {
        typedef typename MoveVehicle<BoardType, Head::type, Head::direction, Head::amount>::board newBoard; // Apply the move to the board
        typedef typename ApplyMoves<newBoard, List<Tail...>>::result result; // Recursively apply the remaining moves
    };

    // Base case: reached the end of the moves
    template<typename BoardType>
    struct ApplyMoves<BoardType, List<>> {
        typedef BoardType result; // Return the final board after applying all moves
    };

public:
    typedef typename ApplyMoves<Board, Moves>::result newBoard; // Apply the sequence of moves to the initial board
    static constexpr bool result = CheckWin<newBoard>::result; // Check if the game is won after applying the moves
};

#endif // OOP5_CHECKSOLUTION_H
