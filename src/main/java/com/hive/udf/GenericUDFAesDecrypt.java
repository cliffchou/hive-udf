package com.hive.udf;

/**
 * Created by Administrator on 2018/8/23.
 */
import javax.crypto.Cipher;

import org.apache.hadoop.hive.ql.exec.Description;

/**
 * GenericUDFAesDecrypt.
 *
 */
@Description(name = "aes_decrypt", value = "_FUNC_(input binary, key string/binary) - Decrypt input using AES.",
        extended = "AES (Advanced Encryption Standard) algorithm. "
                + "Key lengths of 128, 192 or 256 bits can be used. 192 and 256 bits keys can be used if "
                + "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files are installed. "
                + "If either argument is NULL or the key length is not one of the permitted values, the return value is NULL.\n"
                + "Example: > SELECT _FUNC_(unbase64('y6Ss+zCYObpCbgfWfyNWTw=='), '1234567890123456');\n 'ABC'")
public class GenericUDFAesDecrypt extends GenericUDFAesBase {

    @Override
    protected int getCipherMode() {
        return Cipher.DECRYPT_MODE;
    }

    @Override
    protected boolean canParam0BeStr() {
        return false;
    }

    @Override
    protected String getFuncName() {
        return "aes_decrypt";
    }
}

