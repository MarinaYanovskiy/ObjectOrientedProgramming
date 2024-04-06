#ifndef OOP5_CHECKWIN_H
#define OOP5_CHECKWIN_H

#include "GameBoard.h"
#include "MoveVehicle.h"

template<CellType WinCarType, int WinRow, int WinCol, typename Board>
struct CheckWin {
    static constexpr bool value = (FindCar<WinCarType, Board>::X_row_idx == WinRow &&
                                   FindCar<WinCarType, Board>::X_col_idx == WinCol);
};

#endif // OOP5_CHECKWIN_H
