#include <iostream>
#include <chrono>
#include <vector>

uint32_t fibonacci(uint8_t n) {
    if (n <= 1) {
        return n;
    }

    std::vector<uint32_t> fib(n + 1);
    fib[0] = 0;
    fib[1] = 1;

    for (uint8_t i = 2; i <= n; i++) {
        fib[i] = fib[i - 1] + fib[i - 2];
    }

    return fib[n];
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