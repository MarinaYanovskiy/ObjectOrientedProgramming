#include "OOP5_Part2_TestHeader.h"
#include "GameBoard.h"
#include "RushHour.h"

void testSolved1(){
    typedef GameBoard< List<
            List < BoardCell< X , RIGHT , 2>, BoardCell< X , LEFT , 2>, BoardCell< EMPTY , RIGHT , 0> >,
            List < BoardCell< EMPTY , RIGHT , 0>, BoardCell< EMPTY , RIGHT , 0>, BoardCell< A , RIGHT , 1> >
    > > gameBoard;

    static_assert(CheckWin<gameBoard>::result, "Fail");
}

int main(){
    testSolved1();
    return 0;
}

