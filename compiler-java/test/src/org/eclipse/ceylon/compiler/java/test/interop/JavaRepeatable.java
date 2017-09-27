package org.eclipse.ceylon.compiler.java.test.interop;

import java.lang.annotation.Repeatable;


@interface JavaRepeatableContainer {
    JavaRepeatable[] value();
}
@Repeatable(JavaRepeatableContainer.class)
@interface JavaRepeatable {
    String value();
}
