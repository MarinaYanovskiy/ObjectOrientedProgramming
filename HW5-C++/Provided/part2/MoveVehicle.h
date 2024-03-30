#ifndef OOP5_MOVEVEHICLE_H
#define OOP5_MOVEVEHICLE_H

#include "CellType.h"
#include "Direction.h"

template<CellType t_type, Direction d_dir, int a_amount>
struct Move {
    static_assert((t_type != EMPTY), "Invalid move: EMPTY CellType cannot be moved");
    static constexpr CellType type = t_type;
    static constexpr Direction direction = d_dir;
    static constexpr int amount = a_amount;
};

// TODO: Continue implementing MoveVehicle

#endif // OOP5_MOVEVEHICLE_H