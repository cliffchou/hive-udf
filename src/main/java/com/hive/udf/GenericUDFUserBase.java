package com.hive.udf;

/**
 * Created by Administrator on 2018/8/23.
 */
import static org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils.PrimitiveGrouping.BINARY_GROUP;
import static org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils.PrimitiveGrouping.STRING_GROUP;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ConstantObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorConverters.Converter;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import java.util.Random;
import java.util.Arrays;

/**
 * GenericUDFUserBase.
 *
 */
public abstract class GenericUDFUserBase extends GenericUDF {
    protected transient Converter[] converters = new Converter[2];
    protected transient PrimitiveCategory[] inputTypes = new PrimitiveCategory[2];
    protected final BytesWritable output = new BytesWritable();
    protected transient boolean isStr0;
    protected transient boolean isStr1;
    protected transient boolean isKeyConstant;
    protected transient Cipher cipher;
    protected transient SecretKey secretKey;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        checkArgsSize(arguments, 2, 2);

        checkArgPrimitive(arguments, 0);
        checkArgPrimitive(arguments, 1);

        // the function should support both string and binary input types
        if (canParam0BeStr()) {
            checkArgGroups(arguments, 0, inputTypes, STRING_GROUP, BINARY_GROUP);
        } else {
            checkArgGroups(arguments, 0, inputTypes, BINARY_GROUP);
        }
        checkArgGroups(arguments, 1, inputTypes, STRING_GROUP, BINARY_GROUP);

        if (isStr0 = PrimitiveObjectInspectorUtils.getPrimitiveGrouping(inputTypes[0]) == STRING_GROUP) {
            obtainStringConverter(arguments, 0, inputTypes, converters);
        } else {
            GenericUDFParamUtils.obtainBinaryConverter(arguments, 0, inputTypes, converters);
        }

        isKeyConstant = arguments[1] instanceof ConstantObjectInspector;
        byte[] key = null;
        int keyLength = 0;

        if (isStr1 = PrimitiveObjectInspectorUtils.getPrimitiveGrouping(inputTypes[1]) == STRING_GROUP) {
            if (isKeyConstant) {
                String keyStr = getConstantStringValue(arguments, 1);
                if (keyStr != null) {
                    key = keyStr.getBytes();
                    keyLength = key.length;
                }
            } else {
                obtainStringConverter(arguments, 1, inputTypes, converters);
            }
        } else {
            if (isKeyConstant) {
                BytesWritable keyWr = GenericUDFParamUtils.getConstantBytesValue(arguments, 1);
                if (keyWr != null) {
                    key = keyWr.getBytes();
                    keyLength = keyWr.getLength();
                }
            } else {
                GenericUDFParamUtils.obtainBinaryConverter(arguments, 1, inputTypes, converters);
            }
        }

        if (key != null) {
            secretKey = getSecretKey(key, keyLength);
        }

        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        ObjectInspector outputOI = PrimitiveObjectInspectorFactory.writableBinaryObjectInspector;
        return outputOI;
    }
    public byte[] getRandomKey() {
        Random random = new Random();
        byte[] keys = new byte[16];
        random.nextBytes(keys);
        return keys;
    }
    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        byte[] input;
        int inputLength;

        if (isStr0) {
            Text n = GenericUDFParamUtils.getTextValue(arguments, 0, converters);
            if (n == null) {
                return null;
            }
            input = n.getBytes();
            inputLength = n.getLength();
        } else {
            BytesWritable bWr = GenericUDFParamUtils.getBinaryValue(arguments, 0, converters);
            if (bWr == null) {
                return null;
            }
            input = bWr.getBytes();
            inputLength = bWr.getLength();
        }

        if (input == null) {
            return null;
        }

        SecretKey secretKey;
        if (isKeyConstant) {
            secretKey = this.secretKey;
        } else {
            byte[] key;
            int keyLength;
            if (isStr1) {
                Text n = GenericUDFParamUtils.getTextValue(arguments, 1, converters);
                if (n == null) {
                    return null;
                }
                key = n.getBytes();
                keyLength = n.getLength();
            } else {
                BytesWritable bWr = GenericUDFParamUtils.getBinaryValue(arguments, 1, converters);
                if (bWr == null) {
                    return null;
                }
                key = bWr.getBytes();
                keyLength = bWr.getLength();
            }
            secretKey = getSecretKey(key, keyLength);
        }

        if (secretKey == null) {
            return null;
        }
/*
    byte[] res = aesFunction(input, inputLength, secretKey);
    if (res == null) {
      return null;
    }

    output.set(res, 0, res.length);
*/
/*************begin***************/
        System.out.println("input:"+new String(input)+" len:"+inputLength);
        SecretKey new_secretKey;
        byte[] real_key = new byte[16];
        byte[] encrypted_key = new byte[32];
        int cipherMode = getCipherMode();
        if (cipherMode == Cipher.ENCRYPT_MODE) {
            real_key = getRandomKey();
            System.out.println("real_key:"+ new String(real_key));
            new_secretKey = getSecretKey(real_key, real_key.length);

            byte[] enc_res = aesFunction(input, inputLength, new_secretKey);
            System.out.println("enc_res:"+ new String(enc_res));
            if (enc_res == null)
                return null;
            // 使用key加密随机生成的加密密钥real_key得到encrypted_key
            encrypted_key = aesFunction(real_key, real_key.length, secretKey);
            if (encrypted_key == null)
                return null;

            byte[] out = new byte[enc_res.length + encrypted_key.length];
            System.arraycopy(enc_res, 0, out, 0, enc_res.length);
            System.arraycopy(encrypted_key, 0, out, enc_res.length, encrypted_key.length);

            output.set(out, 0, out.length);
        } else {
            byte[] dec_in = new byte[inputLength-encrypted_key.length];
            System.arraycopy(input, 0, dec_in, 0, dec_in.length);
            System.out.println("dec_in:"+new String(dec_in));
            System.arraycopy(input, inputLength-encrypted_key.length, encrypted_key, 0, encrypted_key.length);
            // 使用key解密数据中的加密的加密密钥encrypted_key得到real_key
            real_key = aesFunction(encrypted_key, encrypted_key.length, secretKey);
            if (real_key == null)
                return null;
            System.out.println("real_key:"+new String(real_key));
            new_secretKey = getSecretKey(real_key, real_key.length);

            byte[] dec_res = aesFunction(dec_in, dec_in.length, new_secretKey);
            if (dec_res == null)
                return null;
            System.out.println("dec_res:"+ new String(dec_res));

            output.set(dec_res, 0, dec_res.length);
        }
/*************end***********/
        return output;
    }

    protected SecretKey getSecretKey(byte[] key, int keyLength) {
        if (keyLength == 16 || keyLength == 32 || keyLength == 24) {
            return new SecretKeySpec(key, 0, keyLength, "AES");
        }
        return null;
    }

    protected byte[] aesFunction(byte[] input, int inputLength, SecretKey secretKey) {
        try {
            cipher.init(getCipherMode(), secretKey);
            byte[] res = cipher.doFinal(input, 0, inputLength);
            return res;
        } catch (GeneralSecurityException e) {
            return null;
        }
    }

    abstract protected int getCipherMode();

    abstract protected boolean canParam0BeStr();

    @Override
    public String getDisplayString(String[] children) {
        return getStandardDisplayString(getFuncName(), children);
    }
}
