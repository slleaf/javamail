package com.atchihaya.util.HashUtil;

public interface Algorithm {
    //冗余算法
    String CRC32 = "CRC32";
    String CRC32C = "CRC32C";

    //MD摘要
    String MD2 = "MD2";
    String MD5 = "MD5";

    //SHA1摘要
    /*String SHA = "SHA";*/
    String SHA_1 = "SHA-1";

    //SHA2摘要
    String SHA_224 = "SHA-224";
    String SHA_256 = "SHA-256";
    String SHA_384 = "SHA-384";
    String SHA_512 = "SHA-512";
    String SHA_512_224 = "SHA-512/224";
    String SHA_512_256 = "SHA-512/256";

    //SHA3摘要
    String SHA3_224 = "SHA3-224";
    String SHA3_256 = "SHA3-256";
    String SHA3_384 = "SHA3-384";
    String SHA3_512 = "SHA3-512";
}

