package com.hive.udf;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.apache.hadoop.hive.ql.exec.Description;
import java.io.IOException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorConverter;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorConverter.StringConverter;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.BaseCharTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;

@Description(name = "jdbc", value = "_FUNC_(input binary) - JDBC test",
        extended = "JDBC test, initialize evaluate close\n"
                + "Example: > SELECT _FUNC_('a');\n ")
public class GenericUDFJDBC extends GenericUDF {
    private transient PrimitiveObjectInspector argumentOI;
    private transient StringConverter stringConverter;
    private transient GenericUDFUtils.StringHelper returnHelper;

    protected int test = 0;
    private PrimitiveCategory returnType;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        if (arguments.length != 1) {
            throw new UDFArgumentLengthException(
                    "UPPER requires 1 argument, got " + arguments.length);
        }
        if (arguments[0].getCategory() != Category.PRIMITIVE) {
            throw new UDFArgumentException(
                    "UPPER only takes primitive types, got " + argumentOI.getTypeName());
        }
        argumentOI = (PrimitiveObjectInspector) arguments[0];
        stringConverter = new PrimitiveObjectInspectorConverter.StringConverter(argumentOI);
        PrimitiveCategory inputType = argumentOI.getPrimitiveCategory();
        ObjectInspector outputOI = null;
        BaseCharTypeInfo typeInfo;
        
        switch (inputType) {
            case CHAR:
                // return type should have same length as the input.
                returnType = inputType;
                typeInfo = TypeInfoFactory.getCharTypeInfo(
                        GenericUDFUtils.StringHelper.getFixedStringSizeForType(argumentOI));
                outputOI = PrimitiveObjectInspectorFactory.getPrimitiveWritableObjectInspector(
                        typeInfo);
                break;
            case VARCHAR:
                // return type should have same length as the input.
                returnType = inputType;
                typeInfo = TypeInfoFactory.getVarcharTypeInfo(
                        GenericUDFUtils.StringHelper.getFixedStringSizeForType(argumentOI));
                outputOI = PrimitiveObjectInspectorFactory.getPrimitiveWritableObjectInspector(
                        typeInfo);
                break;
            default:
                returnType = PrimitiveCategory.STRING;
                outputOI = PrimitiveObjectInspectorFactory.writableStringObjectInspector;
                break;
        }
        returnHelper = new GenericUDFUtils.StringHelper(returnType);

        System.out.println(getClass().getName()+" initialize()");
        System.out.println(++test);
        return outputOI;
    }
    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        String val = null;
        if (arguments[0] != null) {
            val = (String) stringConverter.convert(arguments[0].get());
        }
        if (val == null) {
            return null;
        }
        val = val.toUpperCase();

        System.out.println(getClass().getName()+" evaluate()");
        System.out.println(test);

        return returnHelper.setReturnValue(val);
    }
    @Override
    public String getDisplayString(String[] children) {
        System.out.println(getClass().getName()+" getDisplayString()");
        return getStandardDisplayString(getFuncName(), children);
    }
    @Override
    public void close() throws IOException {
        System.out.println(getClass().getName()+" close()");
    }
}
