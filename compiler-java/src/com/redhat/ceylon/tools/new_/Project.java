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
package com.redhat.ceylon.tools.new_;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Tool;

public abstract class Project implements Tool {

    private File directory = new File(".");
    
    public abstract List<Variable> getVariables();
    
    public abstract List<Copy> getResources(Environment env);
    
    @Argument(argumentName="dir", multiplicity="?", order=1)
    public void setDirectory(File directory) {
        this.directory = directory;
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    File mkBaseDir() throws IOException {
        if (directory.exists() && !directory.isDirectory()) {
            throw new IOException(Messages.msg("path.exists.and.not.dir", directory));
        } else if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException(Messages.msg("could.not.mkdir", directory));
            }
        }
        return directory;
    }
    
    @Override
    public final void run() throws Exception {
        // Projects are never run as tools
        throw new RuntimeException();
    }
    
}

