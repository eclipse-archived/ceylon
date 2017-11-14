/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.language;

import org.eclipse.ceylon.common.NonNull;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Method;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Method
@NativeAnnotation$annotation$(backends={})
final class arrayToTuple_ {
    @SuppressWarnings("unchecked")
    @NonNull
    @TypeParameters(@TypeParameter("Element"))
    @TypeInfo("ceylon.language::Sequence<Element>")
    public static <Element> Sequence<? extends Element> arrayToTuple(
            @Ignore TypeDescriptor $reifiedElement,
            @Name("array") @NonNull
            @TypeInfo("ceylon.language::Array<Element>")
            Array<Element> array) {
        if (array.getEmpty()) {
            throw new AssertionError("array must not be empty");
        }
        return (Sequence<? extends Element>) 
                Tuple.instance($reifiedElement, 
                        array.toObjectArray());
    }
    
    private arrayToTuple_(){}
}
