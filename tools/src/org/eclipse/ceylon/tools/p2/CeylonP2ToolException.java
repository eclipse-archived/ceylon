

package org.eclipse.ceylon.tools.p2;

import org.eclipse.ceylon.common.tool.ToolError;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@SuppressWarnings("serial")
public class CeylonP2ToolException extends ToolError {

    public CeylonP2ToolException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CeylonP2ToolException(Throwable cause) {
        super(cause);
    }

    public CeylonP2ToolException(String message) {
        super(message);
    }

}
