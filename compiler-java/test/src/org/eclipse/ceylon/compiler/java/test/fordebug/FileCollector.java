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
package org.eclipse.ceylon.compiler.java.test.fordebug;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class FileCollector {

    private List<String> sources = new ArrayList<String>();
    
    public static final FileFilter JAVA_SOURCE_FILES = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".java");
        }
    };

    public static final FileFilter JAR_FILES = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".jar");
        }
    };
    
    public static final FileFilter JAR_OR_CAR_FILES = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".jar")
                    || pathname.getName().endsWith(".car");
        }
    };
    
    public FileCollector() {
        
    }
    
    public FileCollector addFiles(String file, FileFilter filter) {
        return addFiles(new File(file), filter);
    }
    
    public FileCollector addFiles(File file, FileFilter filter) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                addFiles(child, filter);
            }
        } else if (filter.accept(file)) {
            sources.add(file.getPath());
        }
        return this;
    }
    
    public List<String> getFiles() {
        return sources;
    }
    
}
