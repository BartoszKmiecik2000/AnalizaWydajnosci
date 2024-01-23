import time


def encoding(s1):
    print("Encoding")
    table = {chr(i): i for i in range(256)}

    p = ""
    c = ""
    p += s1[0]
    code = 256
    output_code = []

    print("String\tOutput_Code\tAddition")
    for i in range(len(s1)):
        if i != len(s1) - 1:
            c += s1[i + 1]
        if p + c in table:
            p = p + c
        else:
            print(f"{p}\t{table[p]}\t\t{p + c}\t{code}")
            output_code.append(table[p])
            table[p + c] = code
            code += 1
            p = c
        c = ""

    print(f"{p}\t{table[p]}")
    output_code.append(table[p])
    return output_code


def decoding(op):
    print("\nDecoding")
    table = {i: chr(i) for i in range(256)}

    old = op[0]
    n = 0
    s = table[old]
    c = ""
    c += s[0]
    print(s, end="")

    count = 256
    for i in range(len(op) - 1):
        n = op[i + 1]
        if n not in table:
            s = table[old]
            s = s + c
        else:
            s = table[n]
        print(s, end="")
        c = ""
        c += s[0]
        table[count] = table[old] + c
        count += 1
        old = n


if __name__ == "__main__":
    s = "WYS*WYGWYS*WYSWYSG"
    start_time = time.time()
    output_code = encoding(s)

    print("Output Codes are:", end=" ")
    for i in range(len(output_code)):
        print(output_code[i], end=" ")

    print()
    decoding(output_code)

    end_time = time.time()
    execution_time = (end_time - start_time) * 1000  # Convert to milliseconds
    print(f"Czas wykonania programu to {execution_time} ms.")
