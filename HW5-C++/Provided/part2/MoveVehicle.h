#ifndef OOP5_MOVEVEHICLE_H
#define OOP5_MOVEVEHICLE_H

#include "CellType.h"
#include "Direction.h"
#include "GameBoard.h"
#include "Utilities.h"
#include "TransposeList.h"
#include <iostream>
#include "Printer.h"

template<CellType t_type, Direction d_dir, int a_amount>
struct Move {
    static_assert(a_amount>=0, "Invalid move: amount of steps must be non negative");
    static_assert((t_type != EMPTY), "Invalid move: EMPTY CellType cannot be moved");
    static constexpr CellType type = t_type;
    static constexpr Direction direction = d_dir;
    static constexpr int amount = a_amount;
};



using namespace std;

//=================================================FIND CAR====================================================

// Find_Car_Helper Class Declaration
// This class recursively iterates over the board's cells and finds the coordinates of a cell containing an end (the front or back) of the car "type" (note that the first end to be found depends on the direction of the search).
//we will search from laft to right - meaning we find the top-left corner.
// type - the car to find
// type2 - the car on the current cell
// (row, col) - the coordinates of the current cell
// done - search is done
// B - main list of the board
template <CellType type, CellType type2, int row, int col, bool done, typename B>
struct Find_Car_Helper{
    typedef typename GameBoard<B>::board mainList;
    static constexpr bool last_row = (mainList::size == row + 1);

    static constexpr bool found = (type==type2);
    static constexpr bool last_cell_in_board = (last_row && (col + 1 == GameBoard<B>::width));

    static_assert(!(!found && last_cell_in_board), "Type was not found!");

    static constexpr int next_row = ConditionalInteger<col + 1 == GameBoard<B>::width, row + 1, row>::value; // this is the next cell's row
    static constexpr int next_col = ConditionalInteger<col + 1 == GameBoard<B>::width, 0, col + 1>::value; // this is the next cell's column

    typedef typename GetAtIndex<next_row, typename GameBoard<B>::board>::value next_row_list;
    typedef typename GetAtIndex<next_col, next_row_list>::value next_cell;
    typedef Find_Car_Helper<type, typename next_cell::type, next_row, next_col, found, B> next_helper;

    static constexpr int X_row = ConditionalInteger<found, row, typename next_helper::X_row >::value;
    static constexpr int X_col = ConditionalInteger<found, col, typename next_helper::X_col >::value;
};

// Find_Car_Helper Specialization - stopping condition
template <CellType type, int row, int col, typename B>
struct Find_Car_Helper<type, type, row, col, true, B>{
    static constexpr int X_row = row;
    static constexpr int X_col = col;
};

// FindCar Class Declaration
// (uses Find_Car_Helper to find car "type" in board "Bo" - read Find_Car_Helper for more info)
template<CellType type, typename Bo>
struct FindCar{
    typedef Bo game_board;
    typedef typename game_board::board mainList;

    typedef typename mainList::head::head first_cell;

    typedef Find_Car_Helper<type,typename first_cell::type,0,0,false,mainList> car_loc;
    static constexpr int X_row_idx = car_loc::X_row;
    static constexpr int X_col_idx = car_loc::X_col;
};




//===============================================DIR=============================================================

// Dir Class Declaration
// This class computes the further end of a car respect to "c" given the end found using FindCar.
// (e.g. if "c"=RIGHT, the further end of the car is the right end...)
// c - a direction
// (Ro, Col) - a beginning of the car
// len - the car's length
template<Direction c,int Ro,int Col,int len>

struct Dir{};

// Dir Specialization (you need to implement more specializations similarly)
template< int Ro,int Col,int len>
struct Dir<RIGHT,Ro,Col,len> {
    static constexpr int row_i = Ro;
    static constexpr int col_i = Col + (Length - 1);
};

// Specialization for LEFT
template<int Ro, int Col, int len>
struct Dir<LEFT, Ro, Col, len> {
    static constexpr int row_i = Ro;
    static constexpr int col_i = Col + Length - 1;
};

// Specialization for UP
template<int Ro, int Col, int len>
struct Dir<UP, Ro, Col, len> {
    static constexpr int row_i = Ro + Length - 1;
    static constexpr int col_i = Col;
};

// Specialization for DOWN
template<int Ro, int Col, int len>
struct Dir<DOWN, Ro, Col, len> {
    static constexpr int row_i = Ro + (Length - 1);
    static constexpr int col_i = Col;
};

//====================================================DIRECT=========================================================
// direct Class Declaration
// This class recursively moves a car "Am" steps on the board in a certain direction
// d - the direction of the movement
// counter - recursive counter (remaining amount of steps)
// myL - main list of the board
// my_cell - a cell on the current board containing the car to be moved
// (Ro1, Co1) - coordinates of the beggining of the car
// (Ro2, Co2) - coordinates of the end of the car
template<Direction d,int counter,typename myL,typename my_cell,int Co1,int Ro1,int Co2,int Ro2>
struct direct{};

// direct Specialization (you need to implement more specializations similarly)
template<int counter,typename myL,typename my_cell,int Co1,int Ro1, int Co2,int Ro2>
struct direct<RIGHT,counter,myL,my_cell,Co1,Ro1,Co2,Ro2>{
    typedef typename direct<RIGHT, counter - 1, myL, my_cell,Co1,Ro1, Co2, Ro2>::moved mainList; // main list of the board after we moved the car "count"-1 steps
    typedef typename GetAtIndex<Ro1,mainList>::value subList ;
    typedef typename GetAtIndex<(Co2+counter),subList>::value celli;  // this is the closer end (respect to "d") after the #"count" step
    //typedef typename GetAtIndex<(Co2+counter-1),GetAtIndex<Ro1,mainList>::value>::value to_celli; // this is the further end (respect to "d") before the #"count" step (after the #("count"-1) step)
    static_assert(celli::type!=EMPTY, "Error,Collision cell MoveVehicle");
    typedef typename SetAtIndex<(Co2+counter),my_cell,subList>::list first; //set cell where moved to the car.
    typedef typename SetAtIndex<(Co1+counter-1),BoardCell<EMPTY,RIGHT,1>,first>::list second;//set prev cell to "Empty"
    typedef typename SetAtIndex<Ro1,second,mainList>::list LL; //update the board
    typedef LL moved;
};

// direct Specialization (you need to implement more specializations similarly)
template<typename myL,typename my_cell,int Co1,int Ro1,int Co2,int Ro2>
struct direct<RIGHT,0,myL,my_cell,Co1,Ro1,Co2,Ro2> {
    typedef myL moved;
};


template<int counter,typename myL,typename my_cell,int Co1,int Ro1, int Co2,int Ro2>
struct direct<LEFT,counter,myL,my_cell,Co1,Ro1,Co2,Ro2>{
    typedef typename direct<RIGHT, counter - 1, myL, my_cell,Co1,Ro1, Co2, Ro2>::moved mainList; // main list of the board after we moved the car "count"-1 steps
    typedef typename GetAtIndex<Ro1,mainList>::value subList ;
    typedef typename GetAtIndex<(Co1-counter),subList>::value celli;  // this is the closer end (respect to "d") after the #"count" step
    //typedef typename GetAtIndex<(Co2+counter-1),GetAtIndex<Ro1,mainList>::value>::value to_celli; // this is the further end (respect to "d") before the #"count" step (after the #("count"-1) step)
    static_assert(celli::type!=EMPTY, "Error,Collision cell MoveVehicle");
    typedef typename SetAtIndex<(Co1-counter),my_cell,subList>::list first; //set cell where moved to the car.
    typedef typename SetAtIndex<(Co2-counter+1),BoardCell<EMPTY,LEFT,1>,first>::list second;//set prev cell to "Empty"
    typedef typename SetAtIndex<Ro1,second,mainList>::list LL; //update the board
    typedef LL moved;
};

// direct Specialization (you need to implement more specializations similarly)
template<typename myL,typename my_cell,int Co1,int Ro1,int Co2,int Ro2>
struct direct<LEFT,0,myL,my_cell,Co1,Ro1,Co2,Ro2> {
    typedef myL moved; //contains a list of lists
};



template<int counter,typename myL,typename my_cell,int Co1,int Ro1, int Co2,int Ro2>
struct direct<DOWN,counter,myL,my_cell,Co1,Ro1,Co2,Ro2>{
    typedef typename Transpose<myL>::matrix transposed;
    typedef typename direct<RIGHT,counter,transposed ,my_cell,Ro1,Co1,Ro2,Co2>::moved trans_moved;
    typedef typename Transpose<trans_moved>::matrix moved;
};

// direct Specialization (you need to implement more specializations similarly)
template<typename myL,typename my_cell,int Co1,int Ro1,int Co2,int Ro2>
struct direct<DOWN,0,myL,my_cell,Co1,Ro1,Co2,Ro2> {
    typedef myL moved;
};


template<int counter,typename myL,typename my_cell,int Co1,int Ro1, int Co2,int Ro2>
struct direct<UP,counter,myL,my_cell,Co1,Ro1,Co2,Ro2>{
    typedef typename Transpose<myL>::matrix transposed;
    typedef typename direct<LEFT,counter,transposed L,my_cell,Ro1,Co1,Ro2,Co2>::moved trans_moved;
    typedef typename Transpose<trans_moved>::matrix moved;
};

// direct Specialization (you need to implement more specializations similarly)
template<typename myL,typename my_cell,int Co1,int Ro1,int Co2,int Ro2>
struct direct<UP,0,myL,my_cell,Co1,Ro1,Co2,Ro2> {
    typedef myL moved;
};


//==================================================MOVE VEHICLE==================================================
// MoveVehicle Class Declaration
template<typename gameBoard, int R, int C, Direction D, int A>
struct MoveVehicle{};

// MoveVehicle Specialization
template<typename B, int R1, int C1, Direction Dl, int A>
struct MoveVehicle<GameBoard<B>,R1,C1,Dl,A>{

    typedef GameBoard<B> PrevBoard;
    typedef typename PrevBoard::board mainList;
    typedef GetAtIndex<R1,mainList> subList;
    typedef GetAtIndex<C1,typename subList::value> cell;
    typedef typename cell::value my_cell;

    static_assert(R1<PrevBoard::length, "Error Row,Move");
    static_assert(C1<PrevBoard::width, "Error column,Move");
    static_assert(my_cell::type!=EMPTY, "Error,empty cell MoveVehicle");
    static_assert((((Dl==UP||Dl==DOWN)&&(my_cell::direction==UP||my_cell::direction ==DOWN))
                ||((Dl==LEFT||Dl==RIGHT)&&(my_cell::direction==LEFT||my_cell::direction ==RIGHT))),
            "Error,direction cell MoveVehicle");
    static constexpr int R2= FindCar<typename my_cell::type,PrevBoard>::X_row_idx;
    static constexpr int C2= FindCar<typename my_cell::type,PrevBoard>::X_col_idx;
    // the further end:
    static constexpr int R3= Dir<Dl,R2,C2,typename my_cell::length>::row_i;
    static constexpr int C3= Dir<Dl,R2,C2,typename my_cell::length>::col_i;

    typedef typename direct<Dl,A,B,my_cell,C2,R2,C3,R3>::moved o1;
    typedef GameBoard<o1> board;

};

#endif // OOP5_MOVEVEHICLE_H




