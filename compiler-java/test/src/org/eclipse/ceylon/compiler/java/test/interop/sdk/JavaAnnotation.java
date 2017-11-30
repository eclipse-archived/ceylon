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

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@interface JavaAnnotationPrimitives {
    boolean b();
    byte o();
    short s();
    int i() default 1;
    long l();
    float f();
    double d();
    String str();
    
    boolean[] ba();
    byte[] oa();
    short[] sa();
    int[] ia() default {1, 2};
    long[] la();
    float[] fa();
    double[] da();
    String[] stra() default {"a"};
}

@interface JavaAnnotationEnum {
    java.lang.Thread.State threadState();
    java.lang.Thread.State[] threadStates();
}

@interface JavaAnnotationClass {
    java.lang.Class<?> clas();
    java.lang.Class classRaw();
    java.lang.Class<? extends java.lang.Throwable> classWithBound();
    java.lang.Class<java.lang.String> classExact();
    java.lang.Class<?>[] classes();
    java.lang.Class[] classesRaw();
    java.lang.Class<? extends java.lang.Throwable>[] classesWithBound();
    java.lang.Class<java.lang.String>[] classesExact();
}

@interface JavaAnnotationAnnotation {

    JavaAnnotationEnum annotation();
    JavaAnnotationEnum[] annotations();
}

@interface JAVAAnnotationAcronym {}
@interface javaAnnotationLowercase {}

@interface JavaAnnotationDefaultTarget {}
@Target({})
@interface JavaAnnotationNoTarget {}
@Target({
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.FIELD,
    ElementType.LOCAL_VARIABLE,
    ElementType.METHOD,
    ElementType.PACKAGE,
    ElementType.PARAMETER,
    ElementType.TYPE,
})
@interface JavaAnnotationOnEveryTarget {}

@Target({ElementType.TYPE})
@interface JavaAnnotationTypeTarget {}
@Target({ElementType.CONSTRUCTOR})
@interface JavaAnnotationCtorTarget {}
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR})
@interface JavaAnnotationTypeCtorTarget {}

@Target({ElementType.FIELD})
@interface JavaAnnotationFieldTarget {}
@Target({ElementType.FIELD, ElementType.METHOD})
@interface JavaAnnotationFieldMethodTarget {}
@Target({ElementType.METHOD})
@interface JavaAnnotationMethodTarget {}

@interface JavaAnnotationClass2 {
    java.lang.Class<?> clas();
}

@interface JavaAnnotationSequencedArgs {
    String[] value() default {};
}
@interface JavaAnnotationSequencedArgs2 {
    String[] foo() default {};
    JavaAnnotationSequencedArgs[] bar() default {};
}