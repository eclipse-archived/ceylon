package ceylon.modules.bootstrap;

import org.eclipse.ceylon.common.tool.ToolError;

@SuppressWarnings("serial")
public class CeylonTestFailureError extends ToolError {

    public CeylonTestFailureError() {
        super("Tests failed", 100);
    }

}
