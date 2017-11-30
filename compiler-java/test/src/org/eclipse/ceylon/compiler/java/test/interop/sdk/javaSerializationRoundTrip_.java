/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
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