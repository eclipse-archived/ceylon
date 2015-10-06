package com.redhat.ceylon.compiler.java.test.interop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.Class;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@com.redhat.ceylon.compiler.java.metadata.Method
public class javaSerializationRoundTrip_ {
    
    private javaSerializationRoundTrip_() {}
    
    @Ignore
    public static void main(String[] a) throws Exception {
        javaSerializationRoundTrip();
    }
    
    public static void javaSerializationRoundTrip() throws Exception {
        Class cls = Class.forName("com.redhat.ceylon.compiler.java.test.interop.javaSerialization_");
        Method meth = cls.getMethod("javaSerialization");
        Object o = meth.invoke(new Object[0]);
        System.err.println(o);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(o);
        oos.close();
        
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(is);
        Object read = ois.readObject();
        System.err.println(read);
        assert(o.equals(read));
    }
}