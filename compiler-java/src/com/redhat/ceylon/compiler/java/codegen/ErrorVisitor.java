package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisWarning;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;

public class ErrorVisitor extends Visitor implements NaturalVisitor {

    private boolean allowWarnings;

    public ErrorVisitor(Context context) {
        super();
        Options options = Options.instance(context);
        this.allowWarnings = options.get(OptionName.CEYLONALLOWWARNINGS) != null;
        String option = System.getProperty("ceylon.typechecker.warnings");
        if ("warn".equals(option)) {
            this.allowWarnings = true;
        } else if ("error".equals(option)) {
            this.allowWarnings = false;
        }
    }

    public boolean hasErrors(Node target) {
        try{
            visitAny(target);
            return false;
        }catch(HasErrorException x){
            return true;
        }
    }

    @Override
    public void handleException(Exception e, Node that) {
        // rethrow
        throw (RuntimeException)e;
    }
    
    @Override
    public void visitAny(Node that) {
        // fast termination
        if(hasError(that))
            throw new HasErrorException();
        super.visitAny(that);
    }

    private boolean hasError(Node that) {
        if (allowWarnings) {
            // skip warnings
            for(Message message : that.getErrors())
                if(!(message instanceof AnalysisWarning))
                    return true;
        } else {
            // don't skip warnings
            if(!that.getErrors().isEmpty())
                return true;
        }
        return false;
    }
}