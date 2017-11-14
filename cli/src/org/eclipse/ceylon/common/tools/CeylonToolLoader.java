
package org.eclipse.ceylon.common.tools;

import org.eclipse.ceylon.common.tool.ServiceToolLoader;
import org.eclipse.ceylon.common.tool.Tool;

public class CeylonToolLoader extends ServiceToolLoader {

    public CeylonToolLoader() {
        super(Tool.class);
    }

    public CeylonToolLoader(ClassLoader loader) {
        super(loader, Tool.class);
    }

    @Override
    protected String getToolClassName(String toolName) {
        if (toolName == null || toolName.isEmpty()) {
            return CeylonTool.class.getName();
        } else {
            return super.getToolClassName(toolName);
        }
    }

    @Override
    public String getToolName(String className) {
        return classNameToToolName(className);
    }
}