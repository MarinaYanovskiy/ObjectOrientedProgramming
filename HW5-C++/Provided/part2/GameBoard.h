#ifndef OOP5_GAMEBOARD_H
#define OOP5_GAMEBOARD_H

#include "List.h"
#include "BoardCell.h"

template<typename L>
struct GameBoard {
    List board = L; // TODO: Ask later
    static constexpr int width = L::head::size;
    static constexpr int length = L::size;
    
};

#endif // OOP5_GAMEBOARD_H