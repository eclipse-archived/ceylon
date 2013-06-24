package com.redhat.ceylon.compiler;

import com.redhat.ceylon.common.tool.FatalToolError;
import com.redhat.ceylon.compiler.java.launcher.Main;
import com.redhat.ceylon.compiler.java.launcher.Main.ExitState;

/**
 * <p>Exception type to indicate a bug in the compiler. 
 * The bug could be:</p>
 * <ul>
 * <li>an analysis error from javac due to ceylon codegen producing a bad tree or</li>
 * <li>ceylon codegen throwing an uncaught exception or using makeErroneous() or</li>
 * <li>a real bug in javac</li>
 * </ul> 
 */
public class CompilerBugException extends FatalToolError {
    
    private final ExitState exitState;

    public CompilerBugException(ExitState exitState) {
        super(getMessage(exitState), exitState.abortingException);
        this.exitState = exitState;
    }

    private static String getMessage(ExitState exitState) {
        String message;
        if (exitState.ceylonCodegenExceptionCount > 0) {
            // Codegen threw exception
            message = "Codegen Bug";
        } else if (exitState.ceylonCodegenErroneousCount > 0) {
            // Codegen used erroneous
            message = "Codegen Assertion";
        } else if (exitState.nonCeylonErrorCount > 0) {
            // Codegen generated crap which javac rejected
            message = "Codegen Error";
        } else {
            // javac screwed up
            message = "Bug";
        }
        return message;
    }

    public String getErrorMessage() {
        // In case of codegen throwing uncaught exception the strack is 
        // printed and compiler continues, so we need to refer used to 
        // stacktrace *above*
        // In other cases we want to refer to stacktrace below
        return CeylonCompileMessages.msgBug(exitState.javacExitCode, getCause(), exitState.ceylonCodegenExceptionCount > 0);
    }
}
