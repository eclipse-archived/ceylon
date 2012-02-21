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

import com.redhat.ceylon.compiler.java.Main;

/**
 * Compiles Ceylon and Java source code using the ceylonc compiler
 * @goal ceylonc
 */
public class CeyloncMojo extends AbstractMojo {
    
    /**
     * The directory in which to create the output <code>.car</code> file. 
     * Equivalent to the <code>ceylonc</code>'s <code>-out</code> option. 
     *
     * @parameter expression="${ceylonc.out}" default-value="${project.build.directory}"
     */
    private File out;
    
    /**
     * The directory containing ceylon source code. 
     * Equivalent to the <code>ceylonc</code>'s <code>-src</code> option.
     * 
     * @parameter expression="${ceylonc.src}" default-value="${project.build.sourceDirectory}"
     */
    private File src;
    
    /**
     * If <code>true</code>, disables the default module repositories and source directory.
     * Equivalent to the <code>ceylonc</code>'s <code>-d</code> option.
     * 
     * @parameter expression="${ceylonc.disableDefaultRepos}" default="false"
     */
    private boolean disableDefaultRepos = false;
    
    /**
     * The module repositories containing dependencies.
     * Equivalent to the <code>ceylonc</code>'s <code>-rep</code> option.
     * 
     * @parameter expression="${ceylonc.repositories}"
     */
    private String[] repositories;
    
    /**
     * The modules to compile (without versions).
     * 
     * @parameter expression="${ceylonc.modules}"
     */
    private String[] modules;
    
    public void execute() throws MojoExecutionException
    {
        CommandLine args = new CommandLine(this);
        args.addOption("-out", out.getPath());
        
        args.addOption("-src", src.getPath());
        
        if (disableDefaultRepos) {
            args.addOption("-d");
        }
        
        if (repositories != null) {
            for (String rep : repositories) {
                args.addOption("-rep", rep);
            }
        }
        
        if (modules != null 
                && modules.length != 0) {
            for (String module : modules) {
                args.addOption(module);
            }
        } else {
            getLog().error("No modules to compile. Specify these using 'ceylonc.modules'");
        }        
        
        int sc = Main.compile(args.toArray());
        if (sc != 0) {
            throw new MojoExecutionException("There were compiler errors");
        }
    }
}
