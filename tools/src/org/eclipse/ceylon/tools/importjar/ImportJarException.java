
package org.eclipse.ceylon.tools.importjar;

import org.eclipse.ceylon.common.tool.ToolError;

@SuppressWarnings("serial")
public class ImportJarException extends ToolError {

    public ImportJarException(String msgKey) {
        super(ImportJarMessages.msg(msgKey));
    }

    public ImportJarException(String msgKey, Exception cause) {
        super(ImportJarMessages.msg(msgKey), cause);
    }

    public ImportJarException(String msgKey, Object[] msgArgs, Exception cause) {
        super(ImportJarMessages.msg(msgKey, msgArgs), cause);
    }

}
