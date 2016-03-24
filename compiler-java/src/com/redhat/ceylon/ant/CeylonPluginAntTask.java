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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import com.redhat.ceylon.common.Constants;

/**
 * Ant task wrapping the {@code ceylon plugin} tool
 * @author tako
 */
@ToolEquivalent("plugin")
public class CeylonPluginAntTask extends OutputRepoUsingCeylonAntTask {

    public CeylonPluginAntTask() {
        super("plugin");
    }

    @Override
    protected String getFailMessage() {
        return "plugin failed";
    }
    
    // This should contain the same values as CeylonPluginTool.Mode
    public static enum Mode {
        pack, list, install, uninstall;
    }
    
    private ModuleSet moduleset = new ModuleSet();
    private Mode mode;
    private Path src;
    private Path script;
    private boolean force;
    private boolean system;
    private boolean local;

    @AntDoc("A module to install the scripts of.")
    public void addConfiguredModule(Module module){
        this.moduleset.addConfiguredModule(module);
    }
    
    @AntDoc("A set of modules to install the scripts of.")
    public void addConfiguredModuleset(ModuleSet moduleset){
        this.moduleset.addConfiguredModuleSet(moduleset);
    }
    
    @AntDoc("A set of modules to install the scripts of.")
    public void addConfiguredSourceModules(SourceModules modules){
        this.moduleset.addConfiguredSourceModules(modules);
    }

    public Mode getMode() {
        return mode;
    }

    @AntDoc("The action to perform. One of `list`, `install`, `uninstall` or `pack`")
    @Required
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @OptionEquivalent("--source")
    public void setSrc(Path src) {
        if (this.src == null) {
            this.src = src;
        } else {
            this.src.append(src);
        }
    }

    public List<File> getSrc() {
        if (this.src == null) {
            return Collections.singletonList(getProject().resolveFile(Constants.DEFAULT_SOURCE_DIR));
        }
        String[] paths = this.src.list();
        ArrayList<File> result = new ArrayList<File>(paths.length);
        for (String path : paths) {
            result.add(getProject().resolveFile(path));
        }
        return result;
    }

    @OptionEquivalent
    public void setScript(Path script) {
        if (this.script == null) {
            this.script = script;
        } else {
            this.script.append(script);
        }
    }

    public List<File> getScript() {
        if (this.script == null) {
            return Collections.singletonList(getProject().resolveFile(Constants.DEFAULT_SCRIPT_DIR));
        }
        String[] paths = this.script.list();
        ArrayList<File> result = new ArrayList<File>(paths.length);
        for (String path : paths) {
            result.add(getProject().resolveFile(path));
        }
        return result;
    }

    public boolean getForce() {
        return force;
    }

    @OptionEquivalent
    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean getSystem() {
        return system;
    }

    @OptionEquivalent
    public void setSystem(boolean system) {
        this.system = system;
    }

    public boolean getLocal() {
        return local;
    }

    @OptionEquivalent
    public void setLocal(boolean local) {
        this.local = local;
    }

    @Override
    protected void checkParameters() throws BuildException {
        if (this.moduleset.getModules().isEmpty()) {
            throw new BuildException("You must specify a <module>");
        }
        if (mode == null) {
            throw new BuildException("You must specify a \"mode\"");
        }
    }
    
    @Override
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if (force) {
            appendOption(cmd, "--force");
        }
        
        if (system) {
            appendOption(cmd, "--system");
        } else if (local) {
            appendOption(cmd, "--local");
        }
        
        for (File src : getSrc()) {
            appendOptionArgument(cmd, "--source", src.getAbsolutePath());
        }        
        
        for (File script : getScript()) {
            appendOptionArgument(cmd, "--script", script.getAbsolutePath());
        }        
        
        cmd.createArgument().setValue(mode.toString());
        
        for (Module module : moduleset.getModules()) {
            cmd.createArgument().setValue(module.toSpec());
        }
    }

}
