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
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import com.redhat.ceylon.ceylondoc.Main;

/**
 * Compiles API document from Ceylon source code using the 
 * ceylond documentation compiler.
 * @goal ceylond
 */
public class CeylondMojo extends AbstractMojo {
    
    /**
     * The output module repository (which must be publishable). 
     * Equivalent to <code>ceylond</code>'s <code>-out</code> option. 
     *
     * @parameter expression="${ceylonc.out}" default-value="${project.build.directory}/ceylondoc"
     */
    private File out;
    
    /**
     * A source directory. 
     * Equivalent to <code>ceylond</code>'s <code>-src</code> option.
     * 
     * @parameter expression="${ceylonc.src}" default-value="${project.build.sourceDirectory}"
     */
    private File src;
    
    /**
     * If <code>true</code>, disables the default module repositories and 
     * source directory.
     * Equivalent to <code>ceylond</code>'s <code>-d</code> option.
     * 
     * @parameter expression="${ceylonc.disableDefaultRepos}" default="false"
     */
    private boolean disableDefaultRepos = false;
    
    /**
     * If <code>true</code>, enables documentation generation for 
     * non-<code>shared</code> declarations
     * Equivalent to <code>ceylond</code>'s <code>-non-shared</code> option.
     * 
     * @parameter expression="${ceylonc.nonShared}" default="false"
     */
    private boolean nonShared = false;
    
    /**
     * If <code>true</code>, includes the source code in the generated documentation
     * Equivalent to <code>ceylond</code>'s <code>-source-code</code> option.
     * 
     * @parameter expression="${ceylonc.sourceCode}" default="false"
     */
    private boolean sourceCode = false;
    
    /**
     * The module repositories containing dependencies.
     * Equivalent to <code>ceylond</code>'s <code>-rep</code> option.
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
        
        if (nonShared) {
            args.addOption("-non-shared");
        }
        
        if (sourceCode) {
            args.addOption("-sourceCode");
        }
        
        if (disableDefaultRepos) {
            args.addOption("-d");
        }
        
        if (repositories != null) {
            for (String rep : repositories) {
                args.addOption("-rep");
                args.addOption(rep);
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
        
        try {
            Main.main(args.toArray());
        } catch (IOException e) {
            throw new MojoExecutionException("Something went wrong", e);
        }
    }
}
