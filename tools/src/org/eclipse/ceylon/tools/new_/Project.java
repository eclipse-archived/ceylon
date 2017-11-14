
package org.eclipse.ceylon.tools.new_;

import java.io.File;

import org.eclipse.ceylon.common.tool.Argument;

public abstract class Project extends NewSubTool {

    @Argument(argumentName="dir", multiplicity="?", order=1)
    public void setDirectory(File directory) {
        super.setDirectory(directory);
    }
    
}
