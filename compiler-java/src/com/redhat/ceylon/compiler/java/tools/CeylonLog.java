/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.java.tools;

import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.java.codegen.CeylonFileObject;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.DiagnosticSource;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticType;
import com.sun.tools.javac.util.Log;

public class CeylonLog extends Log {

    /** The number of errors reported by the typechecker */
    private int numCeylonAnalysisErrors;
    /** The numer of exceptions throw by the code generator */
    private int numCeylonCodegenException;
    /** The numer of erroneous messages (i.e. bugs) issued by the code generator */
    private int numCeylonCodegenErroneous;
    /** The numer of non-bug error messages issued by the code generator */
    private int numCeylonCodegenErrors;
    /** The number of errors reported by the javac (=> bugs in codegen) */
    private int numNonCeylonErrors;
    
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
            @Override
            public Log make(Context c) {
                return new CeylonLog(c);
            }
        });
    }

    private boolean majorVersionWarning = false;

    protected CeylonLog(Context context) {
        super(context);
    }

    @Override
    public void report(JCDiagnostic diagnostic) {
        String messageKey = diagnostic.getCode();
        if (messageKey != null) {
            if (messageKey.startsWith("compiler.err.ceylon.codegen.exception")) {
                numCeylonCodegenException++;
            } else if (messageKey.startsWith("compiler.err.ceylon.codegen.erroneous")) {
                numCeylonCodegenErroneous++;
            } else if (messageKey.startsWith("compiler.err.ceylon.codegen.error")) {
                numCeylonCodegenErrors++;
            } else if (messageKey.startsWith("compiler.err.ceylon")) {
                numCeylonAnalysisErrors++;
            } else if (Context.isCeylon()) {
                numNonCeylonErrors++;
            }
        } else if (Context.isCeylon()) {
            numNonCeylonErrors++;
        }
        DiagnosticSource source = diagnostic.getDiagnosticSource();
        if(source != null){
            JavaFileObject file = source.getFile();
            if(file instanceof CeylonFileObject && diagnostic.getType() == DiagnosticType.ERROR){
                ((CeylonFileObject)file).addError(diagnostic);
            }
        }
        super.report(diagnostic);
    }
    
    @Override
    public void note(JavaFileObject file, String key, Object... args) {
        // Ignore lint warnings
    }

    @Override
    public void mandatoryNote(JavaFileObject file, String key, Object... args) {
        // Ignore lint warnings
    }

    @Override
    public void warning(String key, Object... args) {
        // limit the number of warnings for Java 7 classes
        if("big.major.version".equals(key)){
            // change the key to a more helpful message
            key = "ceylon.big.major.version";
            if(!majorVersionWarning )
                majorVersionWarning = true;
            else // we already warned once
                return;
        }
        super.warning(key, args);
    }

    /** 
     * The number of errors logged due to uncaught exceptions or makeErroneous() 
     * calls during ceylon codegen.  
     */
    public int getCeylonCodegenBugCount() {
        return numCeylonCodegenException + numCeylonCodegenErroneous;
    }
    
    /** 
     * The number of errors logged due to uncaught exceptions
     * calls during ceylon codegen.  
     */
    public int getCeylonCodegenExceptionCount() {
        return numCeylonCodegenException;
    }
    
    /** 
     * The number of errors logged due to {@link com.redhat.ceylon.compiler.java.codegen.AbstractTransformer#makeError()}  
     * calls during ceylon codegen.  
     */
    public int getCeylonCodegenErrorCount() {
        return numCeylonCodegenErrors;
    }
    
    /** 
     * The number of errors logs due to messageful makeErroneous() 
     * calls during ceylon codegen.  
     */
    public int getCeylonCodegenErroneousCount() {
        return numCeylonCodegenErroneous;
    }
    
    /**
     * A count of the number of non-ceylon errors
     */
    public int getNonCeylonErrorCount() {
        return numNonCeylonErrors;
    }
    
    /**
     * A count of the number of Ceylon errors
     */
    public int getCeylonErrorCount(){
        return numCeylonAnalysisErrors;
    }
}
