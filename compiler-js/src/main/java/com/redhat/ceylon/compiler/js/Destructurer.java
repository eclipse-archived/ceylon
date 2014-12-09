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
    private boolean first;

    public Destructurer(final Tree.Pattern that, final GenerateJsVisitor gen,
            final Set<Declaration> directAccess, final String expvar, boolean first) {
        this.gen = gen;
        names = gen == null ? null : gen.getNames();
        this.directAccess = directAccess;
        this.expvar = expvar;
        this.first=first;
        that.visit(this);
    }

    public void visit(final Tree.TuplePattern that) {
        int idx=0;
        for (Tree.Pattern p : that.getPatterns()) {
            if (p instanceof Tree.VariablePattern) {
                p.visit(this);
                if (gen != null) {
                    if (((Tree.VariablePattern)p).getVariable().getType() instanceof Tree.SequencedType) {
                        gen.out(".spanFrom(");
                    } else {
                        gen.out(".$_get(");
                    }
                    gen.out(Integer.toString(idx++), ")");
                }
            } else {
                new Destructurer(p, gen, directAccess, expvar+".$_get("+(idx++)+")", first && idx==0);
            }
        }
    }

    public void visit(final Tree.KeyValuePattern that) {
        if (that.getKey() instanceof Tree.VariablePattern) {
            that.getKey().visit(this);
            if (gen != null) {
                gen.out(".key");
            }
        } else {
            new Destructurer(that.getKey(), gen, directAccess, expvar+".item", first);
        }
        if (that.getValue() instanceof Tree.VariablePattern) {
            that.getValue().visit(this);
            if (gen != null) {
                gen.out(".item");
            }
        } else {
            new Destructurer(that.getValue(), gen, directAccess, expvar+".item", false);
        }
    }

    public void visit(final Tree.VariablePattern that) {
        directAccess.add(that.getVariable().getDeclarationModel());
        if (first) {
            first=false;
        } else if (gen != null) {
            gen.out(",");
        }
        if (gen != null) {
            gen.out(names.name(that.getVariable().getDeclarationModel()), "=",expvar);
        }
    }

}
