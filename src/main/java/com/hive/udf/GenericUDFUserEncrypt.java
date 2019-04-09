package com.hive.udf;

/**
 * Created by Administrator on 2018/8/23.
 */
import javax.crypto.Cipher;

import org.apache.hadoop.hive.ql.exec.Description;

/**
 * GenericUDFUserEncrypt.
 *
 */
@Description(name = "encrypt", value = "_FUNC_(input string/binary, key string/binary) - Encrypt input using AES, Encrypt real_key using AES, connect to out.",
        extended = "AES (Advanced Encryption Standard) algorithm. "
                + "Key lengths of 128, 192 or 256 bits can be used. 192 and 256 bits keys can be used if "
                + "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files are installed. "
                + "If either argument is NULL or the key length is not one of the permitted values, the return value is NULL.\n"
                + "Example: > SELECT base64(_FUNC_('ABC', '1234567890123456'));\n 'QdHaYlHCsxjpItX84zcccRWVbRI4wo01byAwqF1pNvUFAYegzeWphyy6sJGrc+VT'")
public class GenericUDFUserEncrypt extends GenericUDFUserBase {

    @Override
    protected int getCipherMode() {
        return Cipher.ENCRYPT_MODE;
    }

    @Override
    protected boolean canParam0BeStr() {
        return true;
    }

    @Override
    protected String getFuncName() {
        return "encrypt";
    }
}
