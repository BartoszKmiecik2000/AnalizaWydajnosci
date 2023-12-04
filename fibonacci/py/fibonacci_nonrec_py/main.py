import time


def fibonacci(n):
    if n <= 1:
        return n

    fib = [0] * (n + 1)
    fib[0] = 0
    fib[1] = 1

    for i in range(2, n + 1):
        fib[i] = fib[i - 1] + fib[i - 2]

    return fib[n]


if __name__ == "__main__":
    n = 30
    start = int(round(time.time() * 1000))
    result = fibonacci(n)
    stop = int(round(time.time() * 1000))
    print(f"Liczba ciagu Fibonacciego o numerze 30 to: {result}")
    print(f"Czas wykonania programu to {stop - start} ms.")
