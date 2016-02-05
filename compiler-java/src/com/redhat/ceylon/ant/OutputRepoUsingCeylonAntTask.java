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

import org.apache.tools.ant.types.Commandline;

import com.redhat.ceylon.common.Constants;


/**
 * Baseclass for tasks that besides using repositories to read also have an output repository
 * @author tom, tako
 */
abstract class OutputRepoUsingCeylonAntTask extends RepoUsingCeylonAntTask {

    protected String out;
    protected String user;
    protected String pass;
    
    protected OutputRepoUsingCeylonAntTask(String toolName) {
        super(toolName);
    }
    
    /**
     * Sets the user name for the output module repository (HTTP only)
     */
    @OptionEquivalent("--user")
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Sets the password for the output module repository (HTTP only)
     */
    @OptionEquivalent("--pass")
    public void setPass(String pass) {
        this.pass = pass;
    }
    
    /**
     * Set the destination repository into which the Java source files should be
     * compiled.
     * @param out the destination repository
     */
    @OptionEquivalent("--out")
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
    }
    
    /**
     * Adds a {@code --user=user} to the given command line iff the given user 
     * is not null.
     * @param cmd The command line
     * @param user The user
     */
    protected static void appendUserOption(Commandline cmd, String user) {
        appendOptionArgument(cmd, "--user", user);
    }
    
    /**
     * Adds a {@code --pass=pass} to the given command line iff the given 
     * password is not null.
     * @param cmd The command line
     * @param pass The password
     */
    protected static void appendPassOption(Commandline cmd, String pass) {
        appendOptionArgument(cmd, "--pass", pass);
    }
    
}
