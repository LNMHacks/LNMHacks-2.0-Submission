//This is a header for data management in excel sheet

#include <iostream>
#include <string>
#include <stdio.h>
#include <time.h>

using namespace std;

class data
{
    

};

// Get current date/time, format is YYYY-MM-DD.HH:mm:ss
const std::string currentDateTime() {
    time_t     now = time(0);
    struct tm  tstruct;
    char       buf[80];
    tstruct = *localtime(&now);
    // Visit http://en.cppreference.com/w/cpp/chrono/c/strftime
    // for more information about date/time format
    strftime(buf, sizeof(buf), "%Y-%m-%d.%X", &tstruct);

    return buf;
}

int main() {
    std::cout << "currentDateTime()=" << currentDateTime() << std::endl;
    getchar();  // wait for keyboard input
}