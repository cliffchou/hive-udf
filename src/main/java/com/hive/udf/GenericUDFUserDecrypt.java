package com.hive.udf;

/**
 * Created by Administrator on 2018/8/23.
 */
import javax.crypto.Cipher;

import org.apache.hadoop.hive.ql.exec.Description;

/**
 * GenericUDFUserDecrypt.
 *
 */
@Description(name = "decrypt", value = "_FUNC_(input binary, key string/binary) - Split input, Decrypt encrypted key using AES, Decrypt ciphertext using AES.",
        extended = "AES (Advanced Encryption Standard) algorithm. "
                + "Key lengths of 128, 192 or 256 bits can be used. 192 and 256 bits keys can be used if "
                + "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files are installed. "
                + "If either argument is NULL or the key length is not one of the permitted values, the return value is NULL.\n"
                + "Example: > SELECT _FUNC_(unbase64('QdHaYlHCsxjpItX84zcccRWVbRI4wo01byAwqF1pNvUFAYegzeWphyy6sJGrc+VT'), '1234567890123456');\n 'ABC'")
public class GenericUDFUserDecrypt extends GenericUDFUserBase {

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
        return "decrypt";
    }
}
