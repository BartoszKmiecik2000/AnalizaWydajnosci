#include <iostream>
#include <cstdint>
#include <cstring>

class Blowfish {
private:
    uint32_t P[18];
    uint32_t S[4][256];

    void Initialize(const uint8_t* key, size_t keyLength);
    uint32_t F(uint32_t x) const;
    void EncryptBlock(uint32_t& left, uint32_t& right) const;
    void DecryptBlock(uint32_t& left, uint32_t& right) const;

public:
    Blowfish(const uint8_t* key, size_t keyLength);
    void Encrypt(const uint8_t* input, uint8_t* output, size_t length) const;
    void Decrypt(const uint8_t* input, uint8_t* output, size_t length) const;
};

Blowfish::Blowfish(const uint8_t* key, size_t keyLength) {
    Initialize(key, keyLength);
}

void Blowfish::Initialize(const uint8_t* key, size_t keyLength) {
    const uint32_t P_init[18] = {
        0x243f6a88, 0x85a308d3, 0x13198a2e, 0x03707344,
        0xa4093822, 0x299f31d0, 0x082efa98, 0xec4e6c89,
        0x452821e6, 0x38d01377, 0xbe5466cf, 0x34e90c6c,
        0xc0ac29b7, 0xc97c50dd, 0x3f84d5b5, 0xb5470917,
        0x9216d5d9, 0x8979fb1b
    };

    const uint32_t S_init[4][256] = {
        // S1
        { 0xd1310ba6, 0x98dfb5ac, 0x2ffd72db, 0xd01adfb7 },
        // S2
        { 0x76dc4190, 0x98d220bc, 0xefa8c1d7, 0xf61e2562 },
        // S3
        { 0x76dc4190, 0x98d220bc, 0xefa8c1d7, 0xf61e2562 },
        // S4
        { 0x76dc4190, 0x98d220bc, 0xefa8c1d7, 0xf61e2562 }
    };

    std::memcpy(P, P_init, sizeof(P));
    std::memcpy(S, S_init, sizeof(S));

    size_t keyIndex = 0;
    for (size_t i = 0; i < 18; ++i) {
        P[i] ^= (key[keyIndex++] << 24) | (key[keyIndex++] << 16) | (key[keyIndex++] << 8) | key[keyIndex++];
        if (keyIndex >= keyLength) {
            keyIndex = 0;
        }
    }

    uint32_t left = 0, right = 0;
    for (size_t i = 0; i < 18; i += 2) {
        EncryptBlock(left, right);
        P[i] = left;
        P[i + 1] = right;
    }

    for (size_t i = 0; i < 4; ++i) {
        for (size_t j = 0; j < 256; j += 2) {
            EncryptBlock(left, right);
            S[i][j] = left;
            S[i][j + 1] = right;
        }
    }
}

uint32_t Blowfish::F(uint32_t x) const {
    uint8_t a, b, c, d;
    d = x & 0xFF;
    x >>= 8;
    c = x & 0xFF;
    x >>= 8;
    b = x & 0xFF;
    x >>= 8;
    a = x & 0xFF;

    return (S[0][a] + S[1][b]) ^ S[2][c] + S[3][d];
}

void Blowfish::EncryptBlock(uint32_t& left, uint32_t& right) const {
    for (int i = 0; i < 16; i += 2) {
        left ^= P[i];
        right ^= F(left);
        right ^= P[i + 1];
        left ^= F(right);
    }
    left ^= P[16];
    right ^= P[17];

    std::swap(left, right);
}

void Blowfish::DecryptBlock(uint32_t& left, uint32_t& right) const {
    for (int i = 16; i > 0; i -= 2) {
        left ^= P[i + 1];
        right ^= F(left);
        right ^= P[i];
        left ^= F(right);
    }
    left ^= P[1];
    right ^= P[0];

    std::swap(left, right);
}

void Blowfish::Encrypt(const uint8_t* input, uint8_t* output, size_t length) const {
    for (size_t i = 0; i < length; i += 8) {
        uint32_t left = (input[i] << 24) | (input[i + 1] << 16) | (input[i + 2] << 8) | input[i + 3];
        uint32_t right = (input[i + 4] << 24) | (input[i + 5] << 16) | (input[i + 6] << 8) | input[i + 7];

        EncryptBlock(left, right);

        output[i] = left >> 24;
        output[i + 1] = (left >> 16) & 0xFF;
        output[i + 2] = (left >> 8) & 0xFF;
        output[i + 3] = left & 0xFF;

        output[i + 4] = right >> 24;
        output[i + 5] = (right >> 16) & 0xFF;
        output[i + 6] = (right >> 8) & 0xFF;
        output[i + 7] = right & 0xFF;
    }
}

void Blowfish::Decrypt(const uint8_t* input, uint8_t* output, size_t length) const {
    for (size_t i = 0; i < length; i += 8) {
        uint32_t left = (input[i] << 24) | (input[i + 1] << 16) | (input[i + 2] << 8) | input[i + 3];
        uint32_t right = (input[i + 4] << 24) | (input[i + 5] << 16) | (input[i + 6] << 8) | input[i + 7];

        DecryptBlock(left, right);

        output[i] = left >> 24;
        output[i + 1] = (left >> 16) & 0xFF;
        output[i + 2] = (left >> 8) & 0xFF;
        output[i + 3] = left & 0xFF;

        output[i + 4] = right >> 24;
        output[i + 5] = (right >> 16) & 0xFF;
        output[i + 6] = (right >> 8) & 0xFF;
        output[i + 7] = right & 0xFF;
    }
}

int main() {
    const uint8_t key[] = "ThisIsASecretKey";
    const uint8_t plaintext[] = "HelloBlowfish";
    uint8_t ciphertext[16];
    uint8_t decryptedText[16];

    Blowfish blowfish(key, sizeof(key) - 1);
    blowfish.Encrypt(plaintext, ciphertext, sizeof(plaintext) - 1);
    blowfish.Decrypt(ciphertext, decryptedText, sizeof(ciphertext));

    std::cout << "Oryginalny tekst: " << plaintext << std::endl;
    std::cout << "Zaszyfrowany tekst: ";
    for (int i = 0; i < sizeof(ciphertext); ++i) {
        std::cout << std::hex << (int)ciphertext[i] << " ";
    }
    std::cout << std::endl;

    std::cout << "Rozszyfrowany tekst: " << decryptedText << std::endl;

    return 0;
}
