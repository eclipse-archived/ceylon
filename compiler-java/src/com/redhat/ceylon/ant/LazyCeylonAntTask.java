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

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import com.redhat.ceylon.common.Constants;


/**
 * Baseclass for tasks which only do something if source files are newer than
 * corresponding module artifacts in the output repository.
 * @see LazyHelper
 * @author tom
 */
abstract class LazyCeylonAntTask extends RepoUsingCeylonAntTask implements Lazy {

    private Path src;
    private String encoding;
    private String out;
    private String user;
    private String pass;
    private Boolean noMtimeCheck = false;
    
    protected LazyCeylonAntTask(String toolName) {
        super(toolName);
    }
    
    /**
     * Set the source directories to find the source Java and Ceylon files.
     * @param src the source directories as a path
     */
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

    /**
     * Set the encoding for the the source files.
     * @param out Charset encoding name
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public List<File> getResource() {
        return Collections.emptyList();
    }

    /**
     * Sets the user name for the output module repository (HTTP only)
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Sets the password for the output module repository (HTTP only)
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
    
    /**
     * Sets whether a file modification time check should be performed
     * @param noMtimeCheck
     */
    public void setNoMtimeCheck(Boolean noMtimeCheck) {
        this.noMtimeCheck = noMtimeCheck;
    }
    
    public boolean getNoMtimeCheck() {
        return noMtimeCheck;
    }

    /**
     * Set the destination repository into which the Java source files should be
     * compiled.
     * @param out the destination repository
     */
    public void setOut(String out) {
        this.out = out;
    }

    public String getOut() {
        if (this.out == null) {
            return new File(getProject().getBaseDir(), Constants.DEFAULT_MODULE_DIR).getPath();
        }
        return this.out;
    }
    
    @Override
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if (out != null) {
            appendOptionArgument(cmd, "--out", out);
        }

        appendUserOption(cmd, user);
        appendPassOption(cmd, pass);
        
        if (encoding != null) {
            appendOptionArgument(cmd, "--encoding", encoding);
        } else  {
            log(getLocation().getFileName() + ":" +getLocation().getLineNumber() + ": No encoding specified, this might cause problems with portability to other platforms!", Project.MSG_WARN);
        }
        
        for (File src : getSrc()) {
            appendOptionArgument(cmd, "--source", src.getAbsolutePath());
        }        
    }
    
}
