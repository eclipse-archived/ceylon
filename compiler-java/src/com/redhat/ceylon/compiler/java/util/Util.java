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

package com.redhat.ceylon.compiler.java.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

import javax.tools.JavaFileObject.Kind;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.impl.CachingRepositoryManager;
import com.redhat.ceylon.cmr.impl.FileContentStore;
import com.redhat.ceylon.cmr.impl.MavenRepositoryHelper;
import com.redhat.ceylon.cmr.impl.SimpleRepositoryManager;
import com.redhat.ceylon.cmr.spi.StructureBuilder;
import com.redhat.ceylon.cmr.webdav.WebDAVContentStore;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.mirror.AnnotatedMirror;
import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.parser.Token;

/**
 * Utility functions that are used in various packages and/or components of the 
 * Ceylon toolset.
 * @see com.redhat.ceylon.compiler.java.codegen.CodegenUtil
 */
public class Util {

    public static String quote(String name) {
        return "$"+name;
    }

    public static String quoteIfJavaKeyword(String name){
        if(isJavaKeyword(name))
            return quote(name);
        return name;
    }
    
    /**
     * Returns a copy of the given array of identifiers, 
     * {@link #quoteIfJavaKeyword(String) quoting} keyword identifiers as 
     * necessary 
     * @param name The parts of a qualified name
     * @return The parts of the qualified name, quoted if necessary
     */
    public static String[] quoteJavaKeywords(String[] name){
        String[] result = new String[name.length];
        for (int ii = 0; ii < name.length; ii++) {
            result[ii] = quoteIfJavaKeyword(name[ii]);
        }
        return result;
    }
    
    /**
     * Returns a copy of the given qualified name, but with any
     * keyword components in the name 
     * {@link #quoteIfJavaKeyword(String) quoted} if necessary 
     * @param qualifiedName
     * @return
     */
    public static String quoteJavaKeywords(String qualifiedName){
        return join(".", quoteJavaKeywords(qualifiedName.split("\\.")));
    }
    
    /**
     * Joins the given parts using the given separator
     * @param sep The separator
     * @param parts The parts
     * @return The parts, joined with the separator
     */
    public static String join(String sep, String... parts) {
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part).append(sep);
        }
        return sb.subSequence(0, sb.length() - sep.length()).toString();
    }
    
    private static final HashSet<String> tokens;
    static {
        tokens = new HashSet<String>();
        for (Token t : Token.values()) {
            tokens.add(t.name);
        }
    }
    private static boolean isJavaKeyword(String name) {
        return tokens.contains(name);
    }

    public static String strip(String str){
        return (str.charAt(0) == '$') ? str.substring(1) : str;
    }

    public static String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getErasedMethodName(String name) {
        // ERASURE
        if ("hash".equals(name)) {
            return "hashCode";
        } else if ("string".equals(name)) {
            return "toString";
        } else if ("equals".equals(name)) {
            // This is a special case where we override the mangling of getMethodName()
            return "equals";
        } else if ("clone".equals(name)) {
            // This is a special case where we override the mangling of getMethodName()
            // FIXME we should only do this when implementing Cloneable!
            return "clone";
        } else {
            return getMethodName(name);
        }
    }

    public static String getMethodName(String name) {
        // ERASURE
        if ("hashCode".equals(name)) {
            return "$hashCode";
        } else if ("toString".equals(name)) {
            return "$toString";
        } else if ("equals".equals(name)) {
            return "$equals";
        } else if ("wait".equals(name)) {
            return "$wait";
        } else if ("notify".equals(name)) {
            return "$notify";
        } else if ("notifyAll".equals(name)) {
            return "$notifyAll";
        } else if ("getClass".equals(name)) {
            return "$getClass";
        } else if ("finalize".equals(name)) {
            return "$finalize";
        } else if ("clone".equals(name)) {
            return "$clone";
        } else {
            return quoteIfJavaKeyword(name);
        }
    }

    public static String getErasedGetterName(String property) {
        // ERASURE
        if ("hash".equals(property)) {
            return "hashCode";
        } else if ("string".equals(property)) {
            return "toString";
        }
        
        return getGetterName(property);
    }

    public static String getGetterName(String property) {
        return "get"+capitalize(strip(property));
    }

    public static String getSetterName(String property){
        return "set"+capitalize(strip(property));
    }

    // Used by the IDE
    public static String getModuleArchiveName(Module module) {
        return getArchiveName(module, "car");
    }

    // Used by the IDE
    public static String getSourceArchiveName(Module module) {
        return getArchiveName(module, "src");
    }

    // Used by the IDE
    public static String getArchiveName(Module module, String extension) {
        String moduleName = module.getNameAsString();
        if(module.isDefault())
            moduleName = "default";
        else{
            moduleName += "-"+module.getVersion();
        }
        return moduleName+"."+extension;
    }

    public static File getModulePath(File outputDir, Module module) {
        // See 7.2.4. Module repositories
        String moduleName = module.getNameAsString();
        String modulePath;
        
        if(module.isDefault())
            modulePath = "default";
        else{
            modulePath = moduleName.replace('.', File.separatorChar);
            modulePath += File.separatorChar + module.getVersion();
        }
        
        return new File(outputDir, modulePath);
    }

    public static String getHomeRepository() {
        return System.getProperty("user.home")+File.separator
                +".ceylon"+File.separator+"repo";
    }

    // Used by the IDE
    public static String getName(List<String> parts){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            sb.append(parts.get(i));
            if (i < parts.size() - 1) {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    public static RepositoryManager makeRepositoryManager(List<String> userRepos, String outRepo, Logger log) {
        final RepositoryManagerBuilder builder = new RepositoryManagerBuilder(log);

        // any user defined repos first
        if(userRepos.isEmpty()){
            builder.addModules();
        }else{
            // go in reverse order because we prepend
            for (int i=userRepos.size()-1;i>=0;i--) {
                String repo = userRepos.get(i);
                boolean maven = false;
                if(repo.startsWith("mvn:")){
                    maven = true;
                    repo = repo.substring(4);
                }
                try {
                    // we need to prepend to bypass the caching repo
                    if(!maven){
                        Repository root = builder.repositoryBuilder().buildRepository(repo);
                        builder.prependRepository(root);
                    }else{
                        Repository mvnRepo = MavenRepositoryHelper.getMavenRepository(repo, log);
                        builder.prependRepository(mvnRepo);
                    }
                } catch (Exception e) {
                    log.warning("Failed to add repository: " + repo + ": "+e.getMessage());
                }
            }
        }

        if(outRepo != null){
            try{
                Repository root = builder.repositoryBuilder().buildRepository(outRepo);
                builder.prependRepository(root);
            }catch(Exception e){
                log.debug("Failed to add output repository as input repository (doesn't matter): " + outRepo + ": "+e.getMessage());
            }
        }
        
        // Caching repo
        builder.addCeylonHome();

        // add remote module repo
        builder.addModulesCeylonLangOrg();

        return builder.buildRepository();
    }

    public static RepositoryManager makeOutputRepositoryManager(String outRepo, Logger log, String user, String password) {
        if(outRepo == null){
            outRepo = "modules";
        }

        if(!isHTTP(outRepo, log)){
            File repoFolder = new File(outRepo);
            if(repoFolder.exists()){
                if(!repoFolder.isDirectory())
                    log.error("Output repository is not a directory: "+outRepo);
                else if(!repoFolder.canWrite())
                    log.error("Output repository is not writable: "+outRepo);
            }else if(!repoFolder.mkdirs())
                log.error("Failed to create output repository: "+outRepo);
            StructureBuilder structureBuilder = new FileContentStore(repoFolder);
            return new SimpleRepositoryManager(structureBuilder, log);
        }else{
            File cachingDir = makeTempDir("ceylonc");

            // HTTP
            WebDAVContentStore davContentStore = new WebDAVContentStore(outRepo, log);
            davContentStore.setUsername(user);
            davContentStore.setPassword(password);

            return new CachingRepositoryManager(davContentStore, cachingDir, log);
        }
    }

    private static boolean isHTTP(String repo, Logger log) {
        try {
            URL url = new URL(repo);
            String protocol = url.getProtocol();
            return "http".equals(protocol) || "https".equals(protocol);
        } catch (MalformedURLException e) {
            log.debug("Invalid repo URL: "+repo+" (assuming file)");
            return false;
        }
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while((read = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
    }


    public static boolean isSubPackage(String moduleName, String pkgName) {
        return pkgName.equals(moduleName)
                || pkgName.startsWith(moduleName+".");
    }
    
    public static boolean isUnboxedVoid(Declaration decl) {
        return (decl instanceof Method)
                && ((Method)decl).isDeclaredVoid();
    }

    public static File makeTempDir(String prefix){
        try {
            File dir = File.createTempFile(prefix, "");
            if(!dir.delete()
                    || !dir.mkdirs())
                throw new RuntimeException("Failed to create tmp dir: "+dir);
            return dir;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void delete(File f){
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new RuntimeException("Failed to delete file: " + f.getPath());
    }

    public static boolean isJavaSource(ClassSymbol classSymbol) {
        if(classSymbol.classfile != null)
            return classSymbol.classfile.getKind() == Kind.SOURCE && classSymbol.classfile.getName().endsWith(".java");
        if(classSymbol.sourcefile != null)
            return classSymbol.sourcefile.getKind() == Kind.SOURCE && classSymbol.sourcefile.getName().endsWith(".java");
        // we don't know but it's probably not
        return false;
    }

    public static boolean isLoadedFromSource(ClassSymbol classSymbol) {
        if(classSymbol.classfile != null)
            return classSymbol.classfile.getKind() != Kind.CLASS;
        if(classSymbol.sourcefile != null)
            return classSymbol.sourcefile.getKind() != Kind.CLASS;
        // we don't know but it's probably not
        return false;
    }
    
    public static String getMirrorName(AnnotatedMirror mirror) {
        String name = strip(mirror.getName()); // FIXME The strip() really should't be necessary
        AnnotationMirror annot = mirror.getAnnotation(AbstractModelLoader.CEYLON_NAME_ANNOTATION);
        if (annot != null) {
            name = (String)annot.getValue();
        }
        return name;
    }
}
