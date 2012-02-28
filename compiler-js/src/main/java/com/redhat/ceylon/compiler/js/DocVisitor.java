package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyAttribute;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyMethod;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/** A Visitor that gathers ceylondoc info for each node's declaration.
 * 
 * @author Enrique Zamudio
 */
public class DocVisitor extends Visitor {

    /** Here we store all the docs, as they are encountered */
    private List<String> docs = new ArrayList<String>();
    /** Here we store all the refs to the docs (index in the 'docs' list), keyed by location. */
    private Map<String, Integer> locs = new HashMap<String, Integer>();

    private boolean retrieveDocs(List<Annotation> annotations, String location) {
        boolean rval = false;
        for (Annotation ann : annotations) {
            if ("doc".equals(ann.getName()) && !ann.getPositionalArguments().isEmpty()) {
                String doc = ann.getPositionalArguments().get(0);
                if (doc.charAt(0) == '"' && doc.charAt(doc.length()-1) == '"') {
                    doc = doc.substring(1, doc.length()-1);
                }
                int idx = docs.indexOf(doc);
                if (idx < 0) {
                    idx = docs.size();
                    docs.add(doc);
                }
                locs.put(location, idx);
                rval = true;
            }
        }
        return rval;
    }

    //This catches function calls, method/attribute calls, object refs, constructors.
    @Override
    public void visit(MemberOrTypeExpression that) {
        /*if (that.getTypeModel() != null && !that.getTypeModel().getTypeArgumentList().isEmpty()) {
            for (ProducedType pt : that.getTypeModel().getTypeArgumentList()) {
                retrieveDocs(pt.getDeclaration().getAnnotations(), that.getLocation());
            }
        }*/
        String loc = that.getLocation();
        if (that.getDeclaration() != null && loc != null) {
            if (that instanceof QualifiedMemberOrTypeExpression) {
                QualifiedMemberOrTypeExpression qme = (QualifiedMemberOrTypeExpression)that;
                loc = qme.getIdentifier().getLocation();
            }
            retrieveDocs(that.getDeclaration().getAnnotations(), loc);
        }
        super.visit(that);
    }
    @Override
    public void visit(AnyAttribute that) {
        //local vars
        retrieveDocs(that.getType().getTypeModel().getDeclaration().getAnnotations(), that.getType().getLocation());
        super.visit(that);
    }

    @Override
    public void visit(AnyMethod that) {
        retrieveDocs(that.getDeclarationModel().getType().getDeclaration().getAnnotations(), that.getType().getLocation());
        super.visit(that);
    }
    @Override
    public void visit(BaseType that) {
        retrieveDocs(that.getDeclarationModel().getType().getDeclaration().getAnnotations(), that.getLocation());
        super.visit(that);
    }

    /** Returns a list of all the doc values found, in the order they were found. */
    public List<String> getDocs() {
        return docs;
    }
    /** Returns a map of the references to the docs; the keys are the locations in the source code
     * and the values are the index of the doc in the docs list. */
    public Map<String, Integer> getLocations() {
        return locs;
    }

}
