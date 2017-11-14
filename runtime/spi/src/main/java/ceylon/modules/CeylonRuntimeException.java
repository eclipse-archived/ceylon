
package ceylon.modules;

import org.eclipse.ceylon.common.tool.ToolError;

/**
 * @author Stephane Epardaud
 */
@SuppressWarnings("serial")
public class CeylonRuntimeException extends ToolError {
    public CeylonRuntimeException(String string) {
        super(string);
    }
}
