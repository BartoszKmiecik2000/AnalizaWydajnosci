import time

def fibonacci(n):
    if n <= 1:
        return n
    else:
        return fibonacci(n - 1) + fibonacci(n - 2)

if __name__ == "__main__":
    n = 30
    start = int(round(time.time() * 1000))
    result = fibonacci(n)
    stop = int(round(time.time() * 1000))
    print(f"Liczba ciagu Fibonacciego o numerze 30 to: {result}")
    print(f"Czas wykonania programu to {stop - start} ms.")
