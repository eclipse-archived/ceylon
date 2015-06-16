package com.redhat.ceylon.compiler.js;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.js.util.JsIdentifierNames;
import com.redhat.ceylon.compiler.js.util.JsWriter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

/** Recursively generates code for a destructuring pattern that may contain other
 * patterns.
 * 
 * @author Enrique Zamudio
 */
public class Destructurer extends Visitor {

    private final GenerateJsVisitor gen;
    private final JsWriter jsw;
    private final JsIdentifierNames names;
    private final String expvar;
    private final Set<Declaration> directAccess;
    private boolean first;
    private final Set<Tree.Variable> added = new HashSet<>();
    private final Set<Value> attribs = new HashSet<>();
    private final Set<Value> caps = new HashSet<>();

    /** Generate the code for the specified pattern. If null is passed instead of a
     * generator, no code is output but the patterns are still visited and their
     * declarations gathered.
     * @param that The pattern to visit.
     * @param gen The generator to output the code.
     * @param directAccess the set in which to store the declarations for direct access.
     * @param expvar the name of the variable storing the expression for destructuring.
     * @param first If false, a comma is output before the very first declaration is output. */
    public Destructurer(final Tree.Pattern that, final GenerateJsVisitor gen,
            final Set<Declaration> directAccess, final String expvar, boolean first) {
        this.gen = gen;
        jsw = gen == null ? null : gen.out;
        names = gen == null ? null : gen.getNames();
        this.directAccess = directAccess;
        this.expvar = expvar;
        this.first=first;
        that.visit(this);
        if (jsw != null && !attribs.isEmpty()) {
            for (Value attr : attribs) {
                jsw.write(";", names.self((TypeDeclaration)attr.getContainer()), ".",
                        names.name(attr), "=", names.name(attr));
            }
        }
    }

    public void visit(final Tree.TuplePattern that) {
        int idx=0;
        for (Tree.Pattern p : that.getPatterns()) {
            if (p instanceof Tree.VariablePattern) {
                p.visit(this);
                if (jsw != null) {
                    if (((Tree.VariablePattern)p).getVariable().getType() instanceof Tree.SequencedType) {
                        jsw.write(".spanFrom(");
                    } else {
                        jsw.write(".$_get(");
                    }
                    jsw.write(Integer.toString(idx++), ")");
                }
            } else {
                added.addAll(new Destructurer(p, gen, directAccess, expvar+".$_get("+(idx++)+")",
                        first && idx==0).getVariables());
            }
        }
    }

    public void visit(final Tree.KeyValuePattern that) {
        if (that.getKey() instanceof Tree.VariablePattern) {
            that.getKey().visit(this);
            if (jsw != null) {
                jsw.write(".key");
            }
        } else {
            added.addAll(new Destructurer(that.getKey(), gen, directAccess, expvar+".item", first).getVariables());
        }
        if (that.getValue() instanceof Tree.VariablePattern) {
            that.getValue().visit(this);
            if (jsw != null) {
                jsw.write(".item");
            }
        } else {
            added.addAll(new Destructurer(that.getValue(), gen, directAccess, expvar+".item", false).getVariables());
        }
    }

    public void visit(final Tree.VariablePattern that) {
        Tree.Variable v = that.getVariable();
        final Value d = v.getDeclarationModel();
        if (directAccess != null) {
            directAccess.add(d);
        }
        if (d.isClassOrInterfaceMember()) {
            attribs.add(d);
        }
        if (d.isCaptured()) {
            caps.add(d);
        }
        added.add(v);
        if (first) {
            first=false;
        } else if (jsw != null) {
            jsw.write(",");
        }
        if (jsw != null) {
            jsw.write(names.name(d), "=",expvar);
        }
    }

    /** Returns the declarations gathered by this Destructurer. */
    public Set<Value> getDeclarations() {
        final HashSet<Value> decs = new HashSet<>(added.size());
        for (Tree.Variable v : added) {
            decs.add(v.getDeclarationModel());
        }
        return decs;
    }
    /** Returns the variables declared inside the destructuring. */
    public Set<Tree.Variable> getVariables() {
        return added;
    }

    public Set<Value> getCapturedValues() {
        return caps;
    }
}
