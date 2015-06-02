package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Node;

public interface Transformer<R, T extends Node> {
    public R transform(T tree);

}
