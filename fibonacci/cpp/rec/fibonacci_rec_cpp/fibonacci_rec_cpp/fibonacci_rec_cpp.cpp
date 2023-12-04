#include <iostream>
#include <chrono>

uint32_t fibonacci(uint8_t n) {
    if (n <= 1) {
        return n;
    }
    else {
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}

int main() {
    uint8_t n = 30;
    uint64_t start = std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now().time_since_epoch()).count();
    uint32_t result = fibonacci(n);
    uint64_t stop = std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now().time_since_epoch()).count();
    std::cout << "Liczba ciagu Fibonacciego o numerze 30 to: " << result << std::endl;
    std::cout << "Czas wykonania programu to " << stop - start << " ms.";
    return 0;
}