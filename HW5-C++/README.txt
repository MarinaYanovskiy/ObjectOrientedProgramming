To run the tests, add all files from this zip to the folder with your part 2 code, then compile using:
g++ -std=c++11 TestMain.cpp
if the compilation succeeds, you've passed all tests!
To test fail conditions (like illegal moves), edit FailTest.h by removing the comment from each line seperately and see that the compilation fails via the correct static_assert.