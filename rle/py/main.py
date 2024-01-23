import time


def rle(s):
    n = len(s)
    outcome = ""
    i = 0
    while i < n:
        counter = 1
        while i < n - 1 and s[i] == s[i + 1]:
            counter += 1
            i += 1
        outcome += s[i] + str(counter)
        i += 1

    return outcome


def main():
    s = "aaaaaaaaaaaaaaaabbbbbbbbbbbbbbccccccccccccccccccccddddddddddddddddeeeeeeeeeeeeeeeeffffffffffffff"

    start = int(time.time() * 1000)

    outcome = rle(s)

    stop = int(time.time() * 1000)

    print("Dane wejsciowe:", s)
    print("Dane wyjsciowe:", outcome)
    print("Czas wykonania programu to", stop - start, "ms.")


if __name__ == "__main__":
    main()
