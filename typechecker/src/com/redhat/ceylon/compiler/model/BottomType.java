package com.redhat.ceylon.compiler.model;

import java.util.Collections;
import java.util.List;

public class BottomType extends TypeDeclaration {

    @Override
    public List<Declaration> getMembers() {
        return Collections.emptyList();
    }

}
