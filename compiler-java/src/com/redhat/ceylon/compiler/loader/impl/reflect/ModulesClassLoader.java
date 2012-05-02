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
package com.redhat.ceylon.compiler.loader.impl.reflect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class loader which looks into a list of jar files
 */
class ModulesClassLoader extends ClassLoader {
    
    /**
     * Jar file where we cache the TOC
     */
    static class CachedTOCJar {
        File jar;
        Set<String> contents = new HashSet<String>();
        
        CachedTOCJar(File jar){
            this.jar = jar;
            try {
                ZipFile zf = new ZipFile(jar);
                try{
                    Enumeration<? extends ZipEntry> entries = zf.entries();
                    while(entries.hasMoreElements()){
                        ZipEntry entry = entries.nextElement();
                        // only cache class files
                        if(!entry.isDirectory())
                            contents.add(entry.getName());
                    }
                }finally{
                    zf.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        boolean containsFile(String path){
            return contents.contains(path);
        }
        
        byte[] getContents(String path){
            try {
                ZipFile zf = new ZipFile(jar);
                try{
                    ZipEntry entry = zf.getEntry(path);
                    if(entry != null)
                        return loadFile(zf.getInputStream(entry), (int)entry.getSize());
                }finally{
                    zf.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException("Missing entry: "+path+" in jar file: "+jar.getPath());
        }

        private byte[] loadFile(InputStream inputStream, int size) throws IOException {
            byte[] buf = new byte[size];
            try{
                int read;
                int offset = 0;
                while(offset != size && (read = inputStream.read(buf, offset, size - offset)) >= 0){
                    offset += read;
                }
                return buf;
            }finally    {
                inputStream.close();
            }
        }
    }
    
    private List<CachedTOCJar> jars = new LinkedList<CachedTOCJar>();
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        for(CachedTOCJar jar : jars){
            if(jar.containsFile(path)){
                byte[] contents = jar.getContents(path);
                return defineClass(name, contents, 0, contents.length);
            }
        }
        return super.findClass(name);
    }

    public void addJar(File file) {
        jars.add(new CachedTOCJar(file));
    }

}