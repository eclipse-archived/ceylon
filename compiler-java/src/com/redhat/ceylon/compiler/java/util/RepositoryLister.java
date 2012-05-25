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
package com.redhat.ceylon.compiler.java.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class RepositoryLister {
    private List<String> extensions;
    
    public RepositoryLister(List<String> extensions) {
        this.extensions = extensions;
    }
    
    public RepositoryLister() {
        this.extensions = Arrays.asList(".jar", ".car");
    }
    
    public static abstract class Actions {
        public abstract void doWithFile(File path);
        public void enterDirectory(File path) {}
        public void exitDirectory(File path) {}
    };
    
    public void list(File path, RepositoryLister.Actions actions) {
        if (path.isDirectory()) {
            actions.enterDirectory(path);
            for (File f : path.listFiles()) {
                list(f, actions);
            }
            actions.exitDirectory(path);
        }
        else if (path.isFile()) {
            String fileName = path.getName();
            for (String extension : extensions) {
                if (extension.equals(".*") || fileName.endsWith(extension) ) {
                    actions.doWithFile(path);
                    return;
                }
            }
        }
    }
}