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

package com.redhat.ceylon.ant;

import org.apache.tools.ant.types.Commandline;

/**
 * Ant task wrapping the {@code ceylon import-jar} tool
 * @author tom
 */
public class CeylonImportJarAntTask extends OutputRepoUsingCeylonAntTask {

    public CeylonImportJarAntTask() {
        super("import-jar");
    }

    @Override
    protected String getFailMessage() {
        return "import-jar failed";
    }
    
    private String module;
    private String jar;
    private String descriptor;
    private boolean force;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getJar() {
        return jar;
    }

    public void setJar(String jar) {
        this.jar = jar;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public boolean getForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    @Override
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if (force) {
            appendOption(cmd, "--force");
        }
        
        if (descriptor != null) {
            appendOptionArgument(cmd, "--descriptor", getDescriptor());
        }
        
        if (module != null) {
            cmd.createArgument().setValue(getModule());
        }
        
        if (jar != null) {
            cmd.createArgument().setValue(getJar());
        }
    }

}
