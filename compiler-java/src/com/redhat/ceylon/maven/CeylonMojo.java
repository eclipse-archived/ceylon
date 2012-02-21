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
package com.redhat.ceylon.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Executes Ceylon applications in the current VM.
 * @goal ceylon
 */
public class CeylonMojo extends AbstractMojo {
    
    /**
     * The directory containing ceylon source code. 
     * Equivalent to the <code>ceylonc</code>'s <code>-src</code> option.
     * 
     * @parameter expression="${ceylon.src}" default-value="${project.build.sourceDirectory}"
     */
    private File src;
    
    /**
     * If <code>true</code>, disables the default module repositories and source directory.
     * Equivalent to the <code>ceylonc</code>'s <code>-d</code> option.
     * 
     * @parameter expression="${ceylon.disableDefaultRepos}" default="false"
     */
    private boolean disableDefaultRepos = false;
    
    /**
     * The module repositories containing dependencies.
     * Equivalent to the <code>ceylonc</code>'s <code>-rep</code> option.
     * 
     * @parameter expression="${ceylon.repositories}"
     */
    private String[] repositories;
    
    /**
     * One of:
     * <ul>
     *   <li>The name (and optionally the version) of the runnable module to 
     *       run</li>
     *   <li>The name of the top-level class to run
     *   <li>The name of the top-level function to run
     * </ul>
     * 
     * @parameter expression="${ceylon.run}"
     */
    private String run;
    
    public void execute() throws MojoExecutionException
    {
        CommandLine args = new CommandLine(this);
        
        args.addOption("-src", src.getPath());
        
        if (disableDefaultRepos) {
            args.addOption("-d");
        }
        
        if (repositories != null) {
            for (String rep : repositories) {
                args.addOption("-rep", rep);
            }
        }
        
        // TODO Find the runnable thing and execute it in the current VM
        // TODO The maven-exec-plugin uses the enclosing project's
        //      dependencies as the classpath, but a ceylon applications 
        //      dependencies are in ceylon modules, so that shouldn't be needed
        
    }
}
