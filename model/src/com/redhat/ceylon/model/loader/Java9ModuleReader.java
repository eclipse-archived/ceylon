package com.redhat.ceylon.model.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.redhat.ceylon.langtools.classfile.Attribute;
import com.redhat.ceylon.langtools.classfile.ClassFile;
import com.redhat.ceylon.langtools.classfile.ConstantPool.InvalidIndex;
import com.redhat.ceylon.langtools.classfile.ConstantPool.UnexpectedEntry;
import com.redhat.ceylon.langtools.classfile.ConstantPoolException;
import com.redhat.ceylon.langtools.classfile.Module_attribute;
import com.redhat.ceylon.langtools.classfile.Module_attribute.ExportsEntry;

public class Java9ModuleReader {

    public static List<String> getExportedPackages(File jar){
        ClassFile classFile = getClassFile(jar);
        if(classFile == null)
            return null;
        Module_attribute moduleAttribute = (Module_attribute) classFile.getAttribute(Attribute.Module);
        try {
            if(moduleAttribute != null){
                List<String> ret = new ArrayList<String>(moduleAttribute.exports_count);
                for(ExportsEntry export : moduleAttribute.exports){
                    if(export.exports_to_count == 0){
                        String ex;
                        ex = classFile.constant_pool.getUTF8Value(export.exports_index).replace('/', '.');
                        ret.add(ex);
                    }
                }
                return ret;
            }
            return null;
        } catch (InvalidIndex | UnexpectedEntry x) {
            throw new RuntimeException(x);
        }
    }

    private static ClassFile getClassFile(File jar){
        try{
            try(ZipFile zipFile = new ZipFile(jar)){
                ZipEntry entry = zipFile.getEntry("module-info.class");
                if(entry != null){
                    try(InputStream in = zipFile.getInputStream(entry)){
                        return ClassFile.read(in);
                    }
                }
            }
            return null;
        }catch(IOException | ConstantPoolException x){
            throw new RuntimeException(x);
        }
    }
}
