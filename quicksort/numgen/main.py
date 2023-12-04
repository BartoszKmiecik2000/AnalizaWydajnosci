import random

def generate_random_array(size, low, high):
    return [random.randint(low, high) for _ in range(size)]

def save_to_txt_file(numbers, filename):
    with open(filename, 'w') as file:
        file.write(','.join(map(str, numbers)))

# Generate an array of 500 random numbers in the range from 1 to 1000
random_numbers = generate_random_array(500, 1, 1500)

# Save the generated array to a text file with numbers separated by a comma
save_to_txt_file(random_numbers, 'random_numbers_1500.txt')
