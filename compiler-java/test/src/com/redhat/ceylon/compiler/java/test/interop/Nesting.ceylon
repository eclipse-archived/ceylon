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
import com.redhat.ceylon.compiler.java.test.interop { 
    JavaNestingInterface {
        JNI_InnerStatic = InnerStatic { JNI_IS_InnerStatic2 = InnerStatic2 }
        ,
        JNI_InnerInterface = InnerInterface
        { 
            JNI_II_I2 = Inner2
            { JNI_II_I2_InnerStatic3 = InnerStatic3 }
        }
    },
    JavaNesting {
        JN_InnerStatic = InnerStatic
         { JN_IS_InnerStatic2 = InnerStatic2, JN_IS_InnerInterface = InnerInterface }
        ,
        JN_InnerInterface = InnerInterface
         { JN_II_Inner2 = Inner2 { JN_II_I2_InnerStatic3 = InnerStatic3 }}
    }
}

class JNI_Impl() satisfies JavaNestingInterface<String> {}
class JNI_InnerInterface_Impl() satisfies JNI_InnerInterface<String> {}
class JN_IS_InnerInterface_Impl() satisfies JN_IS_InnerInterface<String> {}
class JN_InnerInterface_Impl() satisfies JN_InnerInterface<String> {}

@noanno
@error
void nesting() {
    JavaNestingInterface<String> jni = JNI_Impl();
    
    value jni_is = JNI_InnerStatic<String>();
    value jni_is_i2 = jni_is.Inner2<Integer>();
    value jni_is_i2_i3 = jni_is_i2.Inner3<Boolean>();
    
    value jni_is_is2 = JNI_IS_InnerStatic2<Integer>();
    value jni_is_is2_i3 = jni_is_is2.Inner3<Boolean>();
    
    JNI_InnerInterface<String> jni_ii = JNI_InnerInterface_Impl();
    value jni_ii_i2 = JNI_II_I2<Integer>();
    value jni_ii_i2_i3 = jni_ii_i2.Inner3<Boolean>();
    value jni_ii_i2_is3 = JNI_II_I2_InnerStatic3<Boolean>();
    
    value jn = JavaNesting<String>();
    value jn_i = jn.Inner<Integer>();
    value jn_i_i2 = jn_i.Inner2<Boolean>();
    
    value jn_is = JN_InnerStatic<String>();
    value jn_is_i2 = jn_is.Inner2<Integer>();
    value jn_is_i2_i3 = jn_is_i2.Inner3<Boolean>();
    
    value jn_is_is2 = JN_IS_InnerStatic2<String>();
    value jn_is_is2_i3 = jn_is_is2.Inner3<Integer>();
    
    JN_IS_InnerInterface<String> jn_is_ii = JN_IS_InnerInterface_Impl();
    
    JN_InnerInterface<String> jn_ii = JN_InnerInterface_Impl();
    
    value jn_ii_i2 = JN_II_Inner2<String>();
    value jn_ii_i2_i3 = jn_ii_i2.Inner3<Integer>();
    value jn_ii_i2_is3 = JN_II_I2_InnerStatic3<Boolean>();
}
