package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.compiler.typechecker.tree.Node;

public interface Transformer<R, T extends Node> {
    public R transform(T tree);

}
