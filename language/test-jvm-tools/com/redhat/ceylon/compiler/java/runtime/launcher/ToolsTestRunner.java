package com.redhat.ceylon.compiler.java.runtime.launcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class ToolsTestRunner {
    private static final String BuildToolsBuildDir = "build/toolsTest";
    private static final String BuildToolsClassesDir = BuildToolsBuildDir + "/classes";
    private static String SystemRepo = BuildToolsBuildDir + "/repo";
    private static String FlatRepoLib = BuildToolsBuildDir + "/lib";

    private static void addClassPath(File classpath, final List<URL> urls) throws IOException {
        Files.walkFileTree(classpath.toPath(), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String name = file.getFileName().toString();
                if(name.endsWith(".jar") || name.endsWith(".car")){
                    urls.add(file.toUri().toURL());
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        File systemClasspath = new File(SystemRepo);
        File libClasspath = new File(FlatRepoLib);
        
        List<URL> urls = new LinkedList<URL>();
        addClassPath(systemClasspath, urls);
        addClassPath(libClasspath, urls); 
        urls.add(new File("lib/junit-4.9b2.jar").toURL());
        urls.add(new File(BuildToolsClassesDir).toURL());
        URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
        try{
            Class<?> testClass = classLoader.loadClass("com.redhat.ceylon.compiler.java.runtime.ToolsTest");
            Method method = testClass.getDeclaredMethod(args[0]+"_");
            Constructor<?> constructor = testClass.getConstructor();
            constructor.setAccessible(true);
            method.setAccessible(true);
            method.invoke(constructor.newInstance());
        }finally{
            classLoader.close();
        }
    }
}
