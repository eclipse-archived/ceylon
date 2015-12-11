package com.redhat.ceylon.model.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.redhat.ceylon.langtools.classfile.Attribute;
import com.redhat.ceylon.langtools.classfile.ClassFile;
import com.redhat.ceylon.langtools.classfile.ConstantPoolException;
import com.redhat.ceylon.langtools.classfile.MainClass_attribute;
import com.redhat.ceylon.langtools.classfile.Module_attribute;
import com.redhat.ceylon.langtools.classfile.Module_attribute.ExportsEntry;
import com.redhat.ceylon.langtools.classfile.Version_attribute;

public class Java9ModuleReader {

    public static class Java9Module{
        public final String name, version, mainClass;
        public final Set<String> exports = new HashSet<>();
        
        public Java9Module(Module_attribute moduleAttribute, ClassFile classFile){
            try {
                String nameAttr = classFile.getName();
                if(nameAttr.endsWith("/module-info"))
                    nameAttr = nameAttr.substring(0, nameAttr.length()-12);
                name = nameAttr.replace('/', '.');
                for(ExportsEntry export : moduleAttribute.exports){
                    if(export.exports_to_count == 0){
                        String ex;
                        ex = classFile.constant_pool.getUTF8Value(export.exports_index).replace('/', '.');
                        exports.add(ex);
                    }
                }
                Version_attribute versionAttribute = (Version_attribute) classFile.getAttribute(Attribute.Version);
                if(versionAttribute != null){
                    version = classFile.constant_pool.getUTF8Value(versionAttribute.version_index);
                }else{
                    // or throw?
                    version = null;
                }
                MainClass_attribute mainAttribute = (MainClass_attribute) classFile.getAttribute(Attribute.MainClass);
                if(mainAttribute != null){
                    mainClass = mainAttribute.getMainClassName(classFile.constant_pool).replace('/', '.');
                }else{
                    mainClass = null;
                }
            } catch (ConstantPoolException x) {
                throw new RuntimeException(x);
            }
            
        }
    }
    
    public static List<String> getExportedPackages(File jar){
        Java9Module module = getJava9Module(jar);
        if(module != null){
            List<String> ret = new ArrayList<String>(module.exports.size());
            ret.addAll(module.exports);
            return ret;
        }
        return null;
    }

    public static Java9Module getJava9Module(ZipFile zipFile, ZipEntry entry){
        ClassFile classFile = getClassFile(zipFile, entry);
        if(classFile == null)
            return null;
        Module_attribute moduleAttribute = (Module_attribute) classFile.getAttribute(Attribute.Module);
        if(moduleAttribute != null){
            return new Java9Module(moduleAttribute, classFile);
        }
        return null;
    }

    public static Java9Module getJava9Module(File jar){
        ClassFile classFile = getClassFile(jar);
        if(classFile == null)
            return null;
        Module_attribute moduleAttribute = (Module_attribute) classFile.getAttribute(Attribute.Module);
        if(moduleAttribute != null){
            return new Java9Module(moduleAttribute, classFile);
        }
        return null;
    }

    private static ClassFile getClassFile(File jar){
        try{
            try(ZipFile zipFile = new ZipFile(jar)){
                ZipEntry entry = zipFile.getEntry("module-info.class");
                if(entry != null){
                    return getClassFile(zipFile, entry);
                }
            }
            return null;
        }catch(IOException x){
            throw new RuntimeException(x);
        }
    }

    private static ClassFile getClassFile(ZipFile zipFile, ZipEntry entry){
        try{
            try(InputStream in = zipFile.getInputStream(entry)){
                return ClassFile.read(in);
            }
        }catch(IOException | ConstantPoolException x){
            throw new RuntimeException(x);
        }
    }
}
