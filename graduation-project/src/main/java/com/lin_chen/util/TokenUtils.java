package com.lin_chen.util;

import com.lin_chen.filter.Token;

/**
 * token序列化反序列化工具
 **/
public class TokenUtils {
    public static String encryptionToken(Token token) {
        return Encryptor.input(token.getUserId()).aes(Token.DEFAULT_SALT).hex();
    }

    public static Token decryptionToken(String token) {
        return new Token(Encryptor.inputHex(token).unaes(Token.DEFAULT_SALT).string());
    }
}
