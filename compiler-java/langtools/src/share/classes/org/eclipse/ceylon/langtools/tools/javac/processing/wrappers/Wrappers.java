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
package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import org.eclipse.ceylon.javax.annotation.processing.Processor;
import org.eclipse.ceylon.javax.lang.model.SourceVersion;
import org.eclipse.ceylon.javax.lang.model.element.AnnotationValueVisitor;
import org.eclipse.ceylon.javax.lang.model.element.ElementVisitor;
import org.eclipse.ceylon.javax.lang.model.element.UnknownAnnotationValueException;
import org.eclipse.ceylon.javax.lang.model.element.UnknownElementException;
import org.eclipse.ceylon.javax.lang.model.type.TypeKind;
import org.eclipse.ceylon.javax.lang.model.type.TypeVisitor;
import org.eclipse.ceylon.javax.lang.model.type.UnknownTypeException;
import org.eclipse.ceylon.javax.tools.Diagnostic;
import org.eclipse.ceylon.javax.tools.JavaFileObject;

public class Wrappers {

    public static SourceVersion wrap(javax.lang.model.SourceVersion sourceVersion) {
        return SourceVersion.valueOf(sourceVersion.name());
    }
    
    public static Diagnostic.Kind wrap(javax.tools.Diagnostic.Kind kind) {
        return Diagnostic.Kind.valueOf(kind.name());
    }

    public static <R, P> ElementVisitor<R, P> wrap(javax.lang.model.element.ElementVisitor<R, P> v) {
        return new ElementVisitorWrapper<R, P>(v);
    }

    public static <R, P> TypeVisitor<R, P> wrap(javax.lang.model.type.TypeVisitor<R, P> v) {
        return new TypeVisitorWrapper<R, P>(v);
    }

    public static <R, P> AnnotationValueVisitor<R, P> wrap(javax.lang.model.element.AnnotationValueVisitor<R, P> v) {
        return new AnnotationValueVisitorWrapper<R, P>(v);
    }

    public static JavaFileObject.Kind wrap(javax.tools.JavaFileObject.Kind kind) {
        return JavaFileObject.Kind.valueOf(kind.name());
    }

    public static UnknownElementException wrap(javax.lang.model.element.UnknownElementException x) {
        return new UnknownElementException(Facades.unfacade(x.getUnknownElement()), x.getArgument());
    }


    public static UnknownAnnotationValueException wrap(javax.lang.model.element.UnknownAnnotationValueException x) {
        return new UnknownAnnotationValueException(Facades.unfacade(x.getUnknownAnnotationValue()), x.getArgument());
    }

    public static UnknownTypeException wrap(javax.lang.model.type.UnknownTypeException x) {
        return new UnknownTypeException(Facades.unfacade(x.getUnknownType()), x.getArgument());
    }

    public static TypeKind wrap(javax.lang.model.type.TypeKind arg0) {
        return TypeKind.valueOf(arg0.name());
    }

    public static Class<?> unwrapProcessorClass(Processor processor) {
        if(processor instanceof ProcessorWrapper)
            return ((ProcessorWrapper)processor).d.getClass();
        return processor.getClass();
    }
}
