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
import com.redhat.ceylon.model.cmr.PathFilter;
import com.redhat.ceylon.model.loader.ContentAwareArtifactResult;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.typechecker.model.Module;

public class CachedTOCJars {

    /**
     * Jar file where we cache the TOC
     */
    static class CachedTOCJar {
        ArtifactResult artifact;
        // contents, folders and packages are lazy-loaded
        private boolean loaded = false;
        // stores class file names with slashes
        Set<String> contents = new HashSet<String>();
        // stores folder names with slashes
        Set<String> folders = new HashSet<String>();
        // stores package paths with slashes but not last one
        Set<String> packages = new HashSet<String>();
        // not not attempt to load contents from this jar, just its TOC
        boolean skipContents;
        
        CachedTOCJar(ArtifactResult artifact, boolean skipContents){
            this.artifact = artifact;
            this.skipContents = skipContents;
        }

        private void load(){
            if(loaded)
                return;
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
                                String name = entry.getName();
                                if(accept(name)){
                                    if(entry.isDirectory()){
                                        folders.add(name);
                                    }else{
                                        if(JvmBackendUtil.definesPackage(name))
                                            packages.add(getPackageName(name));
                                        contents.add(name);
                                    }
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
            loaded = true;
        }
        
        private boolean accept(String path) {
            PathFilter filter = artifact.filter();
            return filter == null || filter.accept(path);
        }

        private String getPackageName(String path) {
            int sep = path.lastIndexOf('/');
            if(sep != -1)
                path = path.substring(0, sep);
            else
                path = "";// default package
            return path;
        }

        boolean containsFile(String path){
            load();
            return contents.contains(path);
        }

        boolean containsPackage(String path) {
            load();
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
                load();
                try{
                    if(contents.contains(path) || folders.contains(path)){
                        String uripath = FileUtil.absoluteFile(jar).toURI().getSchemeSpecificPart();
                        return new URI("classpath", uripath + "!" + path, null);
                    }
                } catch (URISyntaxException e) {
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
                load();
                // add a trailing / to only list members
                boolean emptyPackage = path.isEmpty();
                // only used for non-empty packages
                path += "/";
                List<String> ret = new ArrayList<String>();
                for(String name : contents){
                    String part = null;
                    if(!emptyPackage && name.startsWith(path)){
                        // keep only the part after the package name
                        part = name.substring(path.length());
                    }else if(emptyPackage){
                        // keep it all, we'll filter later those in subfolders
                        part = name;
                    }
                    // only keep those not in subfolders
                    if(part != null && part.indexOf('/') == -1)
                        ret.add(name);
                }
                return ret;
            } else {
                throw new RuntimeException("No file associated with artifact : " + artifact.toString());
            }
        }

        public Set<String> getPackagePaths() {
            load();
            return packages;
        }

        @Override
        public String toString(){
            return "CachedTOCJar[jar="+artifact+"]";
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

    public Set<String> getPackagePaths(Module module) {
        CachedTOCJar jar = jars.get(module);
        if(jar != null){
            return jar.getPackagePaths();
        }
        return null;
   }

    @Override
    public String toString(){
        return "CachedTOCJars[jars="+jars+"]";
    }
}