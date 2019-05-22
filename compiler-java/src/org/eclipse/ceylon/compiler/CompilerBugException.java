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
package org.eclipse.ceylon.compiler;

import org.eclipse.ceylon.common.tool.FatalToolError;
import org.eclipse.ceylon.compiler.java.launcher.Main.ExitState;

/**
 * <p>Exception type to indicate a bug in the compiler. 
 * The bug could be:</p>
 * <ul>
 * <li>an analysis error from javac due to ceylon codegen producing a bad tree or</li>
 * <li>ceylon codegen throwing an uncaught exception or using makeErroneous() or</li>
 * <li>a real bug in javac</li>
 * </ul> 
 */
@SuppressWarnings("serial")
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
            // Codegen generated stuff which javac rejected
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
        return CeylonCompileMessages.msgBug(exitState.javacExitCode.exitCode, getCause(), exitState.ceylonCodegenExceptionCount > 0);
    }
}
