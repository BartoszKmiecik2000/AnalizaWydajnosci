def generate_subkeys(key):
    # P-Box and S-Boxes initialization

    S = [[0xd1310ba6, 0x98dfb5ac, 0x2ffd72db, 0xd01adfb7, 0xb8e1afed, 0x6a267e96, 0xba7c9045, 0xf12c7f99, 0x24a19947,
          0xb3916cf7, 0x0801f2e2, 0x858efc16, 0x636920d8, 0x71574e69, 0xa458fea3, 0xf4933d7e],
         [0xd83e1fb, 0x831318fe, 0x452f8787, 0xabc8f15d, 0x21714dbb, 0xd6d6a3e6, 0x7ebcaeae, 0xc656398d,
          0x9eef926d, 0xf50e9147, 0xcadc3dae, 0x8a0138f, 0xf3b8b667, 0xc950c433, 0x042b8812, 0x0a750a08],
         [0x07f6351d, 0x93332551, 0xf56dbefa, 0xcc0c59c6, 0x641714f2, 0x9fbfe4a5, 0x0993b6eb, 0x8b051a60,
          0x779ed3fe, 0x8e20faa7, 0xd70dd2ee, 0x4e048354, 0xc3c3e3e, 0x4eae3f28, 0x8557eee4, 0xf6c5be84],
         [0x91d07c1f, 0x32a5d3ef, 0x77e5d6bb, 0x0436facc, 0xf1290dc7, 0x84472f6c, 0x13b81ae9, 0x8f67c93a,
          0x0772c9d, 0x0ae1551a, 0x64fb23dc, 0x73f0a28d, 0x05f12ef3, 0xf8a5b9c, 0x982e7f65, 0xd9a28f05],
         [0x679f25fe, 0xfcefa3f8, 0x08f0df74, 0xd7e4c4c3, 0xb2e5e37, 0x25b47ec7, 0x2b1c89c, 0xfbcbfb50,
          0x81101aeb, 0x63147b4, 0x05d55a0b, 0xa1db3fae, 0x2e5eb660, 0x909a848b, 0x1b0c6e5, 0x9b886d45],
         [0x7d8a0604, 0xfdae5c1c, 0x09a5bcdd, 0xf9a2bc24, 0x2d15a2e5, 0x12db7491, 0xaf721306, 0xc3614e4c,
          0x76779d29, 0x07c1475, 0xa9f739fa, 0xe8e7d44e, 0xf22dfeb1, 0x260fe765, 0xf8a948e9, 0x4931eae3],
         [0x484c5ed5, 0x69257e4f, 0xd3a3b717, 0xa86a62d3, 0x477e4d63, 0xff58bb9d, 0x6b6d5ae, 0xf19c0a1d,
          0xbcd5a14b, 0x139d6b10, 0x920ac0a4, 0x59f4dfc, 0x3bcfccb, 0x9376bdc5, 0xb5092ed9, 0x54f03a5e],
         [0x67715b96, 0xc56581d6, 0x8ed1f83, 0x33266e54, 0x3d12f564, 0x4110c2d0, 0xd7e14156, 0x21bdf24,
          0x34e90cf0, 0x44554793, 0xb7d24432, 0xbe68ef2f, 0xd906ff5, 0x24d5a537, 0x7a244895, 0xf20c102e]
         ]

    P = [0x243f6a88, 0x85a308d3, 0x13198a2e, 0x03707344,
         0xa4093822, 0x299f31d0, 0x082efa98, 0xec4e6c89,
         0x452821e6, 0x38d01377, 0xbe5466cf, 0x34e90c6c,
         0xc0ac29b7, 0xc97c50dd, 0x3f84d5b5, 0xb5470917,
         0x9216d5d9, 0x8979fb1b]

    # Convert key to 32-bit integers
    key = [int.from_bytes(key[i:i + 4], byteorder='big') for i in range(0, len(key), 4)]

    # XOR key with P-Box
    for i in range(len(P)):
        P[i] ^= key[i % len(key)]

    # Initial permutation of P-Box
    P = list(map(lambda x: ((x << 24) & 0xFFFFFFFF) | ((x >> 8) & 0x00FFFFFF), P))

    # Generate subkeys
    subkeys = [word for word in P]

    for i in range(16):
        subkeys[i] = ((subkeys[i] + F(subkeys[(i + 1) % 2])) & 0xFFFFFFFF)

    return subkeys


def F(x):
    # Blowfish F function
    return ((S[0][x >> 24] + S[1][(x >> 16) & 0xFF]) ^ S[2][(x >> 8) & 0xFF]) + S[3][x & 0xFF]


def encrypt_block(left, right, subkeys):
    # Initial permutation
    for i in range(16):
        left ^= subkeys[i % 2]
        right ^= F(left)
        left, right = right, left

    # Swap
    left, right = right, left

    # Final permutation
    left ^= subkeys[1]
    right ^= subkeys[0]

    return left, right


def encrypt(plain_text, key):
    global S
    subkeys = generate_subkeys(key)

    # Pad the plaintext if its length is not a multiple of 8
    if len(plain_text) % 8 != 0:
        plain_text += b'\x00' * (8 - len(plain_text) % 8)

    # Convert plaintext to 64-bit blocks
    blocks = [int.from_bytes(plain_text[i:i + 8], byteorder='big') for i in range(0, len(plain_text), 8)]

    # Encrypt each block
    cipher_text = b''
    for block in blocks:
        left, right = encrypt_block((block >> 32) & 0xFFFFFFFF, block & 0xFFFFFFFF, subkeys)
        cipher_text += (left.to_bytes(4, byteorder='big') + right.to_bytes(4, byteorder='big'))

    return cipher_text


def decrypt_block(left, right, subkeys):
    # Initial permutation
    for i in range(16):
        left ^= subkeys[0]
        right ^= F(left)
        left, right = right, left

    # Swap
    left, right = right, left

    # Final permutation
    left ^= subkeys[1]
    right ^= subkeys[0]

    return left, right


def decrypt(cipher_text, key):
    global S
    subkeys = generate_subkeys(key)

    # Convert ciphertext to 64-bit blocks
    blocks = [int.from_bytes(cipher_text[i:i + 8], byteorder='big') for i in range(0, len(cipher_text), 8)]

    # Decrypt each block
    decrypted_text = b''
    for block in blocks:
        left, right = decrypt_block((block >> 32) & 0xFFFFFFFF, block & 0xFFFFFFFF, subkeys)
        decrypted_text += (left.to_bytes(4, byteorder='big') + right.to_bytes(4, byteorder='big'))

    return decrypted_text.rstrip(b'\x00')


# Example usage:
key = b'SecretKey'
plaintext = b'This is a test'

# Encryption
cipher_text = encrypt(plaintext, key)
print("Cipher Text:", cipher_text.hex())

# Decryption
decrypted_text = decrypt(cipher_text, key)
print("Decrypted Text:", decrypted_text.decode('utf-8'))
