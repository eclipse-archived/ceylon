package org.eclipse.ceylon.langtools.source.tree;

public interface LetTree extends Tree {
    java.util.List<? extends StatementTree> getStatements();
    Tree getExpressio();
}
