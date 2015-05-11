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
package com.redhat.ceylon.model.loader.impl.reflect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.loader.ContentAwareArtifactResult;
import com.redhat.ceylon.model.typechecker.model.Module;

public class CachedTOCJars {

    /**
     * Jar file where we cache the TOC
     */
    static class CachedTOCJar {
        ArtifactResult artifact;
        // stores class file names with slashes
        Set<String> contents = new HashSet<String>();
        // stores package paths with slashes but not last one
        Set<String> packages = new HashSet<String>();
        // not not attempt to load contents from this jar, just its TOC
        boolean skipContents;
        
        CachedTOCJar(ArtifactResult artifact, boolean skipContents){
            this.artifact = artifact;
            this.skipContents = skipContents;
            if (artifact instanceof ContentAwareArtifactResult) {
                packages.addAll(((ContentAwareArtifactResult) artifact).getPackages());
                contents.addAll(((ContentAwareArtifactResult) artifact).getEntries());
            } else {
                if (artifact.artifact() != null) {
                    try {
                        ZipFile zf = new ZipFile(artifact.artifact());
                        try{
                            Enumeration<? extends ZipEntry> entries = zf.entries();
                            while(entries.hasMoreElements()){
                                ZipEntry entry = entries.nextElement();
                                // only cache class files
                                if(!entry.isDirectory()){
                                    packages.add(getPackageName(entry.getName()));
                                    contents.add(entry.getName());
                                }
                            }
                        }finally{
                            zf.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private String getPackageName(String name) {
            int lastSlash = name.lastIndexOf('/');
            if(lastSlash == -1)
                return "";
            return name.substring(0, lastSlash);
        }

        boolean containsFile(String path){
            return contents.contains(path);
        }

        boolean containsPackage(String path) {
            return packages.contains(path);
        }

        byte[] getContents(String path){
            if (artifact instanceof ContentAwareArtifactResult) {
                return ((ContentAwareArtifactResult) artifact).getContents(path);
            }
            File jar = artifact.artifact();
            if (jar != null) {
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
                throw new RuntimeException("Missing entry: "+path+" in jar file: "+ jar.getPath());
            }
            throw new RuntimeException("No file associated with artifact : " + artifact.toString());
        }

        URI getContentUri(String path){
            if (artifact instanceof ContentAwareArtifactResult) {
                return ((ContentAwareArtifactResult) artifact).getContentUri(path);
            }
            File jar = artifact.artifact();
            if (jar != null) {
                try {
                    ZipFile zf = new ZipFile(jar);
                    try{
                        ZipEntry entry = zf.getEntry(path);
                        if (entry != null) {
                            String uripath = FileUtil.absoluteFile(jar).toURI().getSchemeSpecificPart();
                            return new URI("classpath:" + uripath + "!" + entry.getName());
                        }
                    }finally{
                        zf.close();
                    }
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                throw new RuntimeException("Missing entry: "+path+" in jar file: "+ jar.getPath());
            }
            throw new RuntimeException("No file associated with artifact : " + artifact.toString());
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
        
        private List<String> getFileNames(String path){
            if (artifact instanceof ContentAwareArtifactResult) {
                return ((ContentAwareArtifactResult) artifact).getFileNames(path);
            }
            
            File jar = artifact.artifact();
            if (jar != null) {
                try {
                    // add a trailing / to only list members
                    path += "/";
                    ZipFile zf = new ZipFile(jar);
                    try{
                        Enumeration<? extends ZipEntry> entries = zf.entries();
                        List<String> ret = new ArrayList<String>();
                        while(entries.hasMoreElements()){
                            ZipEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // only cache class files
                            if(!entry.isDirectory() && name.startsWith(path)){
                                String part = name.substring(path.length());
                                if(part.indexOf('/') == -1)
                                    ret.add(name);
                            }
                        }
                        return ret;
                    }finally{
                        zf.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("No file associated with artifact : " + artifact.toString());
            }
        }

        @Override
        public String toString(){
            return "CachedTOCJar[jar="+artifact+"; contents="+contents+"; packages="+packages+"]";
        }
    }
    
    private Map<Module, CachedTOCJar> jars = new HashMap<Module, CachedTOCJar>();
    
    public void addJar(ArtifactResult artifact, Module module) {
        addJar(artifact, module, false);
    }
    
    public void addJar(ArtifactResult artifact, Module module, boolean skipContents) {
        // skip duplicates
        if(jars.containsKey(module))
            return;
        jars.put(module, new CachedTOCJar(artifact, skipContents));
    }

    public boolean packageExists(Module module, String name) {
        String path = name.replace('.', '/');
        CachedTOCJar jar = jars.get(module);
        return jar != null && jar.containsPackage(path);
    }

    public List<String> getPackageList(Module module, String name) {
        String path = name.replace('.', '/');
        CachedTOCJar jar = jars.get(module);
        return jar != null && jar.containsPackage(path) ?
                jar.getFileNames(path) : Collections.<String>emptyList();
    }

    public byte[] getContents(String path) {
        for(CachedTOCJar jar : jars.values()){
            if(!jar.skipContents && jar.containsFile(path)){
                return jar.getContents(path);
            }
        }
        return null;
    }

    public URI getContentUri(String path) {
        for(CachedTOCJar jar : jars.values()){
            if(!jar.skipContents && jar.containsFile(path)){
                return jar.getContentUri(path);
            }
        }
        return null;
    }

    public byte[] getContents(Module module, String path) {
        CachedTOCJar jar = jars.get(module);
        if(jar != null && !jar.skipContents && jar.containsFile(path)){
            return jar.getContents(path);
        }
        return null;
    }

    public URI getContentUri(Module module, String path) {
        CachedTOCJar jar = jars.get(module);
        if(jar != null && !jar.skipContents && jar.containsFile(path)){
            return jar.getContentUri(path);
        }
        return null;
    }

    @Override
    public String toString(){
        return "CachedTOCJars[jars="+jars+"]";
    }
}