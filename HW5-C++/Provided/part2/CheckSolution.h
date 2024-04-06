#ifndef OOP5_CHECKSOLUTION_H
#define OOP5_CHECKSOLUTION_H

#include "CheckWin.h"

template<typename PrevBoard, typename NextBoard>
struct CheckSolution {
    static constexpr bool value = true; // Initialize with true, unless we find an invalid move
};

// Specialization for MoveVehicle
template<typename B, int R, int C, Direction D, int A>
struct CheckSolution<GameBoard<B>, typename MoveVehicle<GameBoard<B>, R, C, D, A>::board> {
    static constexpr bool value = CheckMoveValidity<B, R, C, D, A>::value &&
                                   CheckWin<typename MoveVehicle<GameBoard<B>, R, C, D, A>::PrevBoard::template CarAt<R, C>::type,
                                            R, C, typename MoveVehicle<GameBoard<B>, R, C, D, A>::board>::value;
};

#endif // OOP5_CHECKSOLUTION_H
