package com.redhat.ceylon.compiler.js.util;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.js.GenerateJsVisitor;

public class RetainedVars {

    private final List<String> retainedVars = new ArrayList<String>();

    /** Replaces the list of retained vars with the specified list, returning the old list.
     * If no list is specified then a new empty one is used. */
    public List<String> reset(List<String> replace) {
        ArrayList<String> copy = new ArrayList<String>(retainedVars.size());
        copy.addAll(retainedVars);
        retainedVars.clear();
        if (replace != null) {
            retainedVars.addAll(replace);
        }
        return copy;
    }

    /** Writes all retained variables to the output of the specified generator, then clears them. */
    public void emitRetainedVars(GenerateJsVisitor gen) {
        if (!retainedVars.isEmpty()) {
            gen.out("var ");
            boolean first = true;
            for (String varName : retainedVars) {
                if (!first) { gen.out(","); }
                first = false;
                gen.out(varName);
            }
            gen.endLine(true);
            retainedVars.clear();
        }
    }

    /** Adds a variable to the list, to be emitted later. */
    public void add(String vname) {
        retainedVars.add(vname);
    }

}
