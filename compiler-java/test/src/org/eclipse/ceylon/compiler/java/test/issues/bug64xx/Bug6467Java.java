package org.eclipse.ceylon.compiler.java.test.issues.bug64xx;

import java.util.List;

@interface NotNull {}

public abstract class Bug6467Java<T extends Bug6467PsiElement> {
    public Bug6467Java() {
    }

    @NotNull
    public abstract String getInformationHint(@NotNull T var1);
}
