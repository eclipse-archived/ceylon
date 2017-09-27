package org.eclipse.ceylon.compiler.java.test.interop.sdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.Class;

import org.eclipse.ceylon.compiler.java.metadata.Ignore;

@org.eclipse.ceylon.compiler.java.metadata.Method
public class javaSerializationRoundTrip_ {
    
    private javaSerializationRoundTrip_() {}
    
    @Ignore
    public static void main(String[] a) throws Exception {
        javaSerializationRoundTrip();
    }
    
    public static void javaSerializationRoundTrip() throws Exception {
        Class cls = Class.forName("org.eclipse.ceylon.compiler.java.test.interop.sdk.javaSerialization_");
        Method meth = cls.getMethod("javaSerialization");
        final Object o = meth.invoke(null);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(o);
        oos.close();
        System.err.println("wrote: "+o);
        
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(is);
        final Object read = ois.readObject();
        System.err.println("read: " +read);
        cls = Class.forName("org.eclipse.ceylon.compiler.java.test.interop.sdk.javaSerializationCompare_");
        meth = cls.getMethod("javaSerializationCompare", Object.class, Object.class);
        meth.invoke(null, o, read);
    }
}