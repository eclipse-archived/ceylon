package com.redhat.ceylon.compiler.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarOutputStream;

import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.typechecker.model.Module;

public class JarOutputRepositoryManager {
    
    private Map<Module,JarOutputStream> openJars = new HashMap<Module, JarOutputStream>();
    
    public JavaFileObject getFileObject(File outputDir, Module module, String fileName) throws IOException{
        JarOutputStream jarFile = getJarOutputStream(outputDir, module);
        return new JarEntryFileObject(jarFile, fileName);
    }
    
    private JarOutputStream getJarOutputStream(File outputDir, Module module) throws IOException {
        JarOutputStream jarFile = openJars.get(module);
        if(jarFile == null){
            String jarName = getJarName(module);
            System.out.println("Jar name: "+jarName);
            jarFile = new JarOutputStream(new FileOutputStream(new File(outputDir, jarName)));
            openJars.put(module, jarFile);
        }
        return jarFile;
    }

    public void flush(){
        for(JarOutputStream jarFile : openJars.values()){
            flush(jarFile);
        }
        openJars.clear();
    }
    
    private void flush(JarOutputStream jarFile) {
        try {
            jarFile.flush();
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

}
