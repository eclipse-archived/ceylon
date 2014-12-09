package com.redhat.ceylon.compiler.js;

import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class Destructurer extends Visitor {

    private final GenerateJsVisitor gen;
    private final JsIdentifierNames names;
    private final String expvar;
    private final Set<Declaration> directAccess;

    public Destructurer(final Tree.Pattern that, final GenerateJsVisitor gen,
            final Set<Declaration> directAccess, final String expvar) {
        this.gen = gen;
        names = gen.getNames();
        this.directAccess = directAccess;
        this.expvar = expvar;
        that.visit(this);
    }

    public void visit(final Tree.TuplePattern that) {
        int idx=0;
        for (Tree.Pattern p : that.getPatterns()) {
            if (p instanceof Tree.VariablePattern) {
                p.visit(this);
                if (((Tree.VariablePattern)p).getVariable().getType() instanceof Tree.SequencedType) {
                    gen.out(".spanFrom(");
                } else {
                    gen.out(".$_get(");
                }
                gen.out(Integer.toString(idx++), ")");
            } else {
                new Destructurer(p, gen, directAccess, expvar+".$_get("+(idx++)+")");
            }
        }
    }

    public void visit(final Tree.KeyValuePattern that) {
        if (that.getKey() instanceof Tree.VariablePattern) {
            that.getKey().visit(this);
            gen.out(".key");
        } else {
            new Destructurer(that.getKey(), gen, directAccess, expvar+".item");
        }
        if (that.getValue() instanceof Tree.VariablePattern) {
            that.getValue().visit(this);
            gen.out(".item");
        } else {
            new Destructurer(that.getValue(), gen, directAccess, expvar+".item");
        }
    }

    public void visit(final Tree.VariablePattern that) {
        directAccess.add(that.getVariable().getDeclarationModel());
        gen.out(",", names.name(that.getVariable().getDeclarationModel()), "=",expvar);
    }

}
