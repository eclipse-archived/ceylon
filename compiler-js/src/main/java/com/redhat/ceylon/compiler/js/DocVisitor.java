package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MemberOrTypeExpression;
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

    @Override
    public void visit(MemberOrTypeExpression that) {
        for (Annotation ann : that.getDeclaration().getAnnotations()) {
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
                locs.put(that.getLocation(), idx);
            }
        }
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
