package com.redhat.ceylon.compiler.java.runtime.ide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ArtifactResultType;
import com.redhat.ceylon.cmr.api.ImportType;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.api.VisibilityType;
import com.redhat.ceylon.cmr.impl.NodeUtils;
import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.typechecker.model.Module;

/**
 * Temporary launcher for the IDE that sets up the runtime module system, to be removed when the IDE
 * supports the jboss modules runtime.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class Launcher {

    public static void main(String[] args) {
        if(args.length < 2){
            System.err.println("Expecting at least two arguments: module descriptor file and main class name");
            System.exit(1);
        }
        File descriptorFile = new File(args[0]);
        if(!descriptorFile.exists() || !descriptorFile.canRead() || !descriptorFile.isFile()){
            System.err.println("Module descriptor file cannot be read: "+descriptorFile);
            System.exit(1);
        }
        try{
            // read the descriptor
//            System.err.println("Reading descriptor from "+descriptorFile.getPath());
            try{
                BufferedReader reader = new BufferedReader(new FileReader(descriptorFile));
                try{
                    readModuleDescriptor(reader);
                }finally{
                    reader.close();
                }
            }catch(IOException x){
                x.printStackTrace();
                error("IO error: "+x.getMessage());
            }
            // now forward to main class
            invokeMain(args);
        }catch(LauncherException x){
            System.err.println(x.getMessage());
            System.exit(1);
        }finally{
//            System.err.println("Deleting descriptor "+descriptorFile.getPath());
            descriptorFile.delete();
        }
    }

    private static void invokeMain(String[] args) throws LauncherException {
        String main = args[1];
        if(main.isEmpty())
            error("Main class cannot be empty");
        try {
            Class<?> mainClass = Class.forName(main);
            Method m = mainClass.getMethod("main", String[].class);
            m.setAccessible(true);
            String[] newArgs = new String[args.length-2];
            System.arraycopy(args, 2, newArgs, 0, newArgs.length);
            m.invoke(null, (Object)newArgs);
        } catch (ClassNotFoundException e) {
            error("Main class not found: "+main);
        } catch (NoSuchMethodException e) {
            error("Main method not found in: "+main);
        } catch (SecurityException e) {
            error("Security exception when trying to obtain main for: "+main);
        } catch (IllegalAccessException e) {
            error("Illegal access exception when invoking main for: "+main);
        } catch (IllegalArgumentException e) {
            error("Illegal argument exception when invoking main for: "+main);
        } catch (InvocationTargetException e) {
            // let normal exceptions trickle through
            throw new RuntimeException(e.getCause());
        }
    }

    private static void readModuleDescriptor(BufferedReader reader) throws IOException, LauncherException {
        String line;
        while((line = reader.readLine()) != null){
            String module = line;
            if(module.isEmpty())
                error("Empty module line");
            String version = null;
            if(!module.equals(Module.DEFAULT_MODULE_NAME)){
                version = reader.readLine();
                if(version == null || version.isEmpty())
                    error("Empty version line for "+module);
            }
            String file = reader.readLine();
            if(file == null || file.isEmpty())
                error("Empty file line for "+module);
            File f = new File(file);
            if(!f.exists() || !f.isFile() || !f.canRead())
                error("Cannot read "+file+" for "+module);
//            System.err.println("Loading module "+module+"/"+version+": "+file);
            Util.loadModule(module, version, makeModuleArtifact(f), Launcher.class.getClassLoader());
        }
    }

    private static ArtifactResult makeModuleArtifact(final File f) {
        return new ArtifactResult(){

            @Override
            public String name() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String version() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ImportType importType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ArtifactResultType type() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public VisibilityType visibilityType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public File artifact() throws RepositoryException {
                return f;
            }

            @Override
            public List<ArtifactResult> dependencies() throws RepositoryException {
                return Collections.emptyList();
            }

            @Override
            public String repositoryDisplayString() {
                return NodeUtils.UNKNOWN_REPOSITORY;
            }};
    }

    private static void error(String string) throws LauncherException {
        throw new LauncherException(string);
    }

    @SuppressWarnings("serial")
    private static class LauncherException extends Exception {

        public LauncherException(String string) {
            super(string);
        }

    }
}
