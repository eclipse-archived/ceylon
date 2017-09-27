package org.eclipse.ceylon.model.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.ceylon.model.loader.ClassFileUtil;
import org.junit.Assert;
import org.junit.Test;

import org.eclipse.ceylon.langtools.classfile.Annotation;
import org.eclipse.ceylon.langtools.classfile.Attribute;
import org.eclipse.ceylon.langtools.classfile.ClassFile;
import org.eclipse.ceylon.langtools.classfile.ConstantPoolException;
import org.eclipse.ceylon.langtools.classfile.RuntimeVisibleAnnotations_attribute;

@interface TestAnnotation {
    String val();
}

@Retention(RetentionPolicy.RUNTIME)
@interface TestInterface {
    boolean bool();
    char c();
    byte b();
    short s();
    int i();
    long l();
    float f();
    double d();
    String string();
    String[] array();
    TestAnnotation annot();
}

@TestInterface(
        annot = @TestAnnotation(val = "annot"), 
        bool = true, 
        b = 1, 
        s = 2, 
        i = 3, 
        l = 4, 
        c = 'a', 
        f = 5.0f, 
        d = 6.0, 
        string = "str",
        array = {"a", "b"}
        )
public class ClassFileUtilTest {
    @Test
    public void test() throws IOException, ConstantPoolException{
        String location = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File f = new File(location);
        String classPath = ClassFileUtilTest.class.getName().replace('.', '/')+".class";
        ClassFile classFile;
        if(f.isDirectory()){
            // folder
            File klassFile = new File(f, classPath);
            classFile = ClassFile.read(klassFile);
        }else{
            // zip
            try(ZipFile zf = new ZipFile(f)){
                ZipEntry entry = zf.getEntry(classPath);
                try(InputStream is = zf.getInputStream(entry)){
                    classFile = ClassFile.read(is);
                }
            }
        }
         
        RuntimeVisibleAnnotations_attribute attribute = (RuntimeVisibleAnnotations_attribute) classFile.getAttribute(Attribute.RuntimeVisibleAnnotations);
        Annotation annotation = ClassFileUtil.findAnnotation(classFile, attribute, TestInterface.class);
        Assert.assertNotNull(annotation);
        Assert.assertEquals(true, ClassFileUtil.getAnnotationValue(classFile, annotation, "bool"));
        Assert.assertEquals((byte) 1, ClassFileUtil.getAnnotationValue(classFile, annotation, "b"));
        Assert.assertEquals((short) 2, ClassFileUtil.getAnnotationValue(classFile, annotation, "s"));
        Assert.assertEquals((int) 3, ClassFileUtil.getAnnotationValue(classFile, annotation, "i"));
        Assert.assertEquals((long) 4, ClassFileUtil.getAnnotationValue(classFile, annotation, "l"));
        Assert.assertEquals('a', ClassFileUtil.getAnnotationValue(classFile, annotation, "c"));
        Assert.assertEquals((float) 5.0, ClassFileUtil.getAnnotationValue(classFile, annotation, "f"));
        Assert.assertEquals((double) 6.0, ClassFileUtil.getAnnotationValue(classFile, annotation, "d"));
        Assert.assertEquals("str", ClassFileUtil.getAnnotationValue(classFile, annotation, "string"));
        Assert.assertArrayEquals(new Object[]{"a", "b"}, (Object[]) ClassFileUtil.getAnnotationValue(classFile, annotation, "array"));
        
        Annotation subAnnotation = (Annotation) ClassFileUtil.getAnnotationValue(classFile, annotation, "annot");
        Assert.assertNotNull(subAnnotation);
        Assert.assertEquals("annot", ClassFileUtil.getAnnotationValue(classFile, subAnnotation, "val"));
    }
}
