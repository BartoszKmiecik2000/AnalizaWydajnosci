#include <iostream>
#include <chrono>
#include <string>

std::string rle(std::string str)
{
    int n = str.length();
    std::string outcome;
    for (int i = 0; i < n; i++) {
        int counter = 1;
        while (i < n - 1 && str[i] == str[i + 1]) {
            counter++;
            i++;
        }
        outcome.append(str.begin() + i, str.begin() + i + 1);
        outcome.append(std::to_string(counter));
    }

    return outcome;
}

int main()
{
    std::string str = "aaaaaaaaaaaaaaaabbbbbbbbbbbbbbccccccccccccccccccccddddddddddddddddeeeeeeeeeeeeeeeeffffffffffffff";
    uint64_t start = std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now().time_since_epoch()).count();
    std::string outcome = rle(str);
    uint64_t stop = std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now().time_since_epoch()).count();
    std::cout << "Dane wejsciowe: " << str << std::endl;
    std::cout << "Dane wyjsciowe: " << outcome << std::endl;
    std::cout << "Czas wykonania programu to " << stop - start << " ms.";
    return 0;
}