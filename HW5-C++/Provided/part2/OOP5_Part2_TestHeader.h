#ifndef OOP5_PART2_TESTHEADER_H
#define OOP5_PART2_TESTHEADER_H

#include "CellType.h"
#include "Direction.h"
#include "BoardCell.h"
#include "GameBoard.h"
#include "List.h"

template<typename, typename>
struct Equals;

template<typename A, typename B>
struct Equals<GameBoard<A>, GameBoard<B>>{
    constexpr static bool value = Equals<A, B>::value;
};

template<typename HeadA, typename HeadB, typename... TailA, typename... TailB>
struct Equals<List<HeadA, TailA...>, List<HeadB, TailB...>>{
    constexpr static bool value = Equals<HeadA, HeadB>::value && Equals<List<TailA...>, List<TailB...>>::value;
};

template<>
struct Equals<List<>, List<>>{
    constexpr static bool value = true;
};

template<typename... TT>
struct Equals<List<TT...>, List<>>{
    constexpr static bool value = false;
};

template<typename... TT>
struct Equals<List<>, List<TT...>>{
    constexpr static bool value = false;
};

template<CellType Type, Direction DirA, Direction DirB, int Size>
struct Equals<BoardCell<Type, DirA, Size>, BoardCell<Type, DirB, Size>>{
    constexpr static bool value = true;
};

template<CellType TypeA, CellType TypeB, Direction DirA, Direction DirB, int AmA, int AmB>
struct Equals<BoardCell<TypeA, DirA, AmA>, BoardCell<TypeB, DirB, AmB>>{
    constexpr static bool value = false;
};

#endif //OOP5_PART2_TESTHEADER_H
