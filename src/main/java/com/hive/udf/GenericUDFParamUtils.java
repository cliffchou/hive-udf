package com.hive.udf;

/**
 * Created by Administrator on 2018/8/23.
 */
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF.DeferredObject;
import org.apache.hadoop.hive.serde2.objectinspector.ConstantObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorConverters;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorConverters.Converter;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

/**
 * Generic UDF params utility class
 */
public class GenericUDFParamUtils {

    private GenericUDFParamUtils() {
    }

    public static BytesWritable getBinaryValue(DeferredObject[] arguments, int i,
                                               Converter[] converters) throws HiveException {
        Object obj;
        if ((obj = arguments[i].get()) == null) {
            return null;
        }
        Object writableValue = converters[i].convert(obj);
        return (BytesWritable) writableValue;
    }

    public static Text getTextValue(DeferredObject[] arguments, int i, Converter[] converters)
            throws HiveException {
        Object obj;
        if ((obj = arguments[i].get()) == null) {
            return null;
        }
        Object writableValue = converters[i].convert(obj);
        return (Text) writableValue;
    }

    public static void obtainBinaryConverter(ObjectInspector[] arguments, int i,
                                             PrimitiveCategory[] inputTypes, Converter[] converters) throws UDFArgumentTypeException {
        PrimitiveObjectInspector inOi = (PrimitiveObjectInspector) arguments[i];
        PrimitiveCategory inputType = inOi.getPrimitiveCategory();

        Converter converter = ObjectInspectorConverters.getConverter(arguments[i],
                PrimitiveObjectInspectorFactory.writableBinaryObjectInspector);
        converters[i] = converter;
        inputTypes[i] = inputType;
    }

    public static BytesWritable getConstantBytesValue(ObjectInspector[] arguments, int i) {
        Object constValue = ((ConstantObjectInspector) arguments[i]).getWritableConstantValue();
        return (BytesWritable) constValue;
    }
}
