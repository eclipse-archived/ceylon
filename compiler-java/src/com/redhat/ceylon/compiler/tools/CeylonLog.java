package com.redhat.ceylon.compiler.tools;

import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.codegen.CeylonFileObject;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;

public class CeylonLog extends Log {

    /** Get the Log instance for this context. */
    public static Log instance(Context context) {
        Log instance = context.get(logKey);
        if (instance == null)
            instance = new CeylonLog(context);
        return instance;
    }

    /**
     * Register a Context.Factory to create a JavacFileManager.
     */
    public static void preRegister(final Context context) {
        context.put(logKey, new Context.Factory<Log>() {
            public Log make() {
                return new CeylonLog(context);
            }
        });
    }

    protected CeylonLog(Context context) {
        super(context);
    }

    @Override
    public void report(JCDiagnostic diagnostic) {
        JavaFileObject file = diagnostic.getDiagnosticSource().getFile();
        if(file instanceof CeylonFileObject){
            ((CeylonFileObject)file).errors++;
        }
        super.report(diagnostic);
    }
}
