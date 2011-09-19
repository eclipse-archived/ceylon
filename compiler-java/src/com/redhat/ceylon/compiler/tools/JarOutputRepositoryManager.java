package com.redhat.ceylon.compiler.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.typechecker.model.Module;

public class JarOutputRepositoryManager {
    
    private Map<Module,ProgressiveJar> openJars = new HashMap<Module, ProgressiveJar>();
    
    public JavaFileObject getFileObject(File outputDir, Module module, String fileName) throws IOException{
        ProgressiveJar progressiveJar = getProgressiveJar(outputDir, module);
        return progressiveJar.getJavaFileObject(fileName);
    }
    
    private ProgressiveJar getProgressiveJar(File outputDir, Module module) throws IOException {
        ProgressiveJar jarFile = openJars.get(module);
        if(jarFile == null){
            String jarName = getJarName(module);
            System.out.println("Jar name: "+jarName);
            jarFile = new ProgressiveJar(new File(outputDir, jarName));
            openJars.put(module, jarFile);
        }
        return jarFile;
    }

    public void flush(){
        for(ProgressiveJar jarFile : openJars.values()){
            flush(jarFile);
        }
        openJars.clear();
    }
    
    private void flush(ProgressiveJar jarFile) {
        try {
            jarFile.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getJarName(Module module) {
        String moduleName = module.getNameAsString();
        // FIXME: do better than this
        if(moduleName.equals("<default module>"))
            moduleName = "default_module";
        String version = module.getVersion();
        if(version == null)
            version = "unversioned";
        return moduleName+"-"+version+".jar";
    }

    static class ProgressiveJar {
        final private File originalJarFile;
        final private File outputJarFile;
        final private JarOutputStream jarOutputStream;
        final private Set<String> writtenClasses = new HashSet<String>();
        
        public ProgressiveJar(File targetJarFile) throws IOException{
            if(targetJarFile.exists()){
                outputJarFile = File.createTempFile(targetJarFile.getName(), ".tmp", targetJarFile.getParentFile());
                originalJarFile = targetJarFile;
            }else{
                outputJarFile = targetJarFile; 
                originalJarFile = null;
            }
            jarOutputStream = new JarOutputStream(new FileOutputStream(outputJarFile));
        }

        public void close() throws IOException {
            // now copy all previous jar entries
            if(originalJarFile != null){
                JarFile jarFile = new JarFile(originalJarFile);
                Enumeration<JarEntry> entries = jarFile.entries();
                while(entries.hasMoreElements()){
                    JarEntry entry = entries.nextElement();
                    // skip the old entry if we overwrote it
                    if(writtenClasses.contains(entry.getName()))
                        continue;
                    jarOutputStream.putNextEntry(entry);
                    InputStream inputStream = jarFile.getInputStream(entry);
                    copy(inputStream, jarOutputStream);
                    inputStream.close();
                    jarOutputStream.closeEntry();
                }
            }
            jarOutputStream.flush();
            jarOutputStream.close();
            if(originalJarFile != null){
                // now rename to the original name
                outputJarFile.renameTo(originalJarFile);
            }
        }

        private void copy(InputStream inputStream, JarOutputStream outputStream) throws IOException {
            byte[] buffer = new byte[4096];
            int read;
            while((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, read);
            }
        }

        public JavaFileObject getJavaFileObject(String fileName) {
            // record the class file we produce so that we don't save it from the original jar
            writtenClasses.add(fileName);
            return new JarEntryFileObject(outputJarFile.getPath(), jarOutputStream, fileName);
        }
    }
}
