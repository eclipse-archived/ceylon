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
import java.util.LinkedList;
import java.util.List;

import javax.tools.StandardLocation;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.impl.FileContentStore;
import com.redhat.ceylon.cmr.impl.RepositoryBuilder;
import com.redhat.ceylon.cmr.impl.RootBuilder;
import com.redhat.ceylon.cmr.impl.SimpleRepository;
import com.redhat.ceylon.cmr.spi.StructureBuilder;
import com.redhat.ceylon.cmr.webdav.WebDAVContentStore;
import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.sun.tools.javac.parser.Token;
import com.sun.tools.javac.util.JavacFileManager;

public class Util {

    public static boolean isErasedAttribute(String name){
        // ERASURE
        return "hash".equals(name) || "string".equals(name);
    }

    public static String quoteMethodNameIfProperty(Method method, Unit typeFact) {
        String name = method.getName();
        // only quote if method is a member, we cannot get a conflict for local or toplevel methods
        // since local methods have a $getter suffix and toplevel attribute class names are not mangled
        if(!method.isClassOrInterfaceMember())
            return name;
        // get/is with at least one more letter, no parameter and non-void type
        if(((name.length() >= 4 && name.startsWith("get"))
             || name.length() >= 3 && name.startsWith("is"))
            && method.getParameterLists().get(0).getParameters().isEmpty()
            && !method.getType().isExactly(typeFact.getVoidDeclaration().getType()))
            return quote(name);
        // set with one parameter and void type
        if((name.length() >= 4 && name.startsWith("set"))
           && method.getParameterLists().get(0).getParameters().size() == 1
           && method.getType().isExactly(typeFact.getVoidDeclaration().getType()))
            return quote(name);
        return name;
    }

    private static String quote(String name) {
        return "$"+name;
    }

    public static String quoteMethodName(String name){
        // ERASURE
        if ("hash".equals(name)) {
            return "hashCode";
        } else if ("string".equals(name)) {
            return "toString";
        } else if ("hashCode".equals(name)) {
            return "$hashCode";
        } else if ("toString".equals(name)) {
            return "$toString";
        } else {
            return quoteIfJavaKeyword(name);
        }
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
    
    private static boolean isJavaKeyword(String name) {
        try{
            Token token = Token.valueOf(name.toUpperCase());
            return token != null && token.name != null && token.name.equals(name);
        }catch(IllegalArgumentException x){
            return false;
        }
    }

    public static String strip(String str){
        return (str.charAt(0) == '$') ? str.substring(1) : str;
    }

    public static String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getGetterName(String property) {
        // ERASURE
        if ("hash".equals(property)) {
            // FIXME This is NOT the way to handle this, we should check that we're
            // actually trying to override the hash attribute defined in Equality
            return "hashCode";
        } else if ("string".equals(property)) {
            return "toString";
        }
        
        return "get"+capitalize(strip(property));
    }

    public static String getGetterName(Declaration decl) {
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        if(decl instanceof JavaBeanValue){
            return ((JavaBeanValue)decl).getGetterName();
        }
        return getGetterName(decl.getName());
    }

    public static String getSetterName(String property){
        return "set"+capitalize(strip(property));
    }

    public static String getSetterName(Declaration decl){
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        if(decl instanceof JavaBeanValue){
            return ((JavaBeanValue)decl).getSetterName();
        }
        return getSetterName(decl.getName());
    }

    public static String getCompanionClassName(String name){
        return name + "$impl";
    }
    
	public static String getQualifiedPrefixedName(Declaration decl){
	    String name = decl.getQualifiedNameString();
	    String prefix;
	    if(decl instanceof ClassOrInterface)
	        prefix = "C";
	    else if(decl instanceof Value)
	        prefix = "V";
        else if(decl instanceof Getter)
            prefix = "G";
        else if(decl instanceof Setter)
            prefix = "S";
        else if(decl instanceof Method)
            prefix = "M";
        else
            throw new RuntimeException("Don't know how to prefix decl: "+decl);
	    return prefix + name;
	}

    public static String getSimpleName(String name) {
        name = name.replace('$', '.');
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public static Declaration getTopmostRefinedDeclaration(Declaration decl){
        if(decl instanceof Parameter && decl.getContainer() instanceof Functional){
            // Parameters in a refined class, interface or method are not considered refinements themselves
            // so we have to look up the corresponding parameter in the container's refined declaration
            Functional func = (Functional)decl.getContainer();
            Parameter param = (Parameter)decl;
            Functional refinedFunc = (Functional) getTopmostRefinedDeclaration((Declaration)decl.getContainer());
            // shortcut if the functional doesn't override anything
            if(refinedFunc == decl.getContainer())
                return decl;
            if(func.getParameterLists().size() != 1 || refinedFunc.getParameterLists().size() != 1)
                throw new RuntimeException("Multiple parameter lists not supported");
            // find the index of the parameter
            int index = func.getParameterLists().get(0).getParameters().indexOf(param);
            return refinedFunc.getParameterLists().get(0).getParameters().get(index);
        }
        Declaration refinedDecl = decl.getRefinedDeclaration();
        if(refinedDecl != null && refinedDecl != decl)
            return getTopmostRefinedDeclaration(refinedDecl);
        return decl;
    }

    public static boolean isUnBoxed(Term node){
        return node.getUnboxed();
    }

    public static boolean isUnBoxed(TypedDeclaration decl){
        // null is considered boxed
        return decl.getUnboxed() == Boolean.TRUE;
    }

    public static void markUnBoxed(Term node) {
        node.setUnboxed(true);
    }

    public static BoxingStrategy getBoxingStrategy(Term node) {
        return isUnBoxed(node) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
    }

    public static BoxingStrategy getBoxingStrategy(TypedDeclaration decl) {
        return isUnBoxed(decl) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
    }

    public static boolean hasCompilerAnnotation(Tree.Declaration decl, String name){
        for(CompilerAnnotation annotation : decl.getCompilerAnnotations()){
            if(annotation.getIdentifier().getText().equals(name))
                return true;
        }
        return false;
    }

    public static String getModuleArchiveName(Module module) {
        return getArchiveName(module, "car");
    }
    
    public static String getSourceArchiveName(Module module) {
        return getArchiveName(module, "src");
    }

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

    public static String getSourceFilePath(JavacFileManager fileManager, String file){
        Iterable<? extends File> prefixes = fileManager.getLocation(StandardLocation.SOURCE_PATH);

        // find the matching source prefix
        int srcDirLength = 0;
        for (File prefixFile : prefixes) {
            String prefix = prefixFile.getPath();
            if (file.startsWith(prefix) && prefix.length() > srcDirLength) {
                srcDirLength = prefix.length();
            }
        }
        
        String path = file.substring(srcDirLength);
        if(path.startsWith(File.separator))
            path = path.substring(1);
        return path;
    }

    public static String getDefaultedParamMethodName(Declaration decl, Parameter param) {
        if (decl instanceof Method) {
            return decl.getName() + "$" + param.getName();
        } else if (decl instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) {
            return "$init$" + param.getName();
        } else {
            // Should never happen (for now at least)
            return null;
        }
    }
    
    public static List<String> addDefaultRepositories(List<String> userRepos){
        List<String> defaultRepositories = new LinkedList<String>();
        // DIST first
        String ceylonHome = System.getProperty("ceylon.home");
        // if it's not set, let's not use it
        if(ceylonHome != null && !ceylonHome.isEmpty()){
            defaultRepositories.add(ceylonHome+File.separator+"repo");
        }
        // then USER repos with default
        if(userRepos.isEmpty())
            defaultRepositories.add("modules");
        else
            defaultRepositories.addAll(userRepos);
        // then HOME repo
        defaultRepositories.add(getHomeRepository());
        return defaultRepositories;
    }

    public static String getHomeRepository() {
        return System.getProperty("user.home")+File.separator
                +".ceylon"+File.separator+"repo";
    }

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

    public static boolean isDirectAccessVariable(Term term) {
        if(!(term instanceof BaseMemberExpression))
            return false;
        Declaration decl = ((BaseMemberExpression)term).getDeclaration();
        if(decl == null) // typechecker error
            return false;
        // make sure we don't try to optimise things which can't be optimised
        return decl instanceof Value
                && !decl.isToplevel()
                && !decl.isClassOrInterfaceMember()
                && !decl.isCaptured()
                && !decl.isShared();
    }

    public static Repository makeRepository(List<String> userRepos, Logger log) {
        final RepositoryBuilder builder = new RepositoryBuilder(log);

        // any user defined repos first
        if(userRepos.isEmpty()){
            builder.addModules();
        }else{
            // go in reverse order because we prepend
            for (int i=userRepos.size()-1;i>=0;i--) {
                String repo = userRepos.get(i);
                try {
                    final RootBuilder rb = new RootBuilder(repo, log);
                    // we need to prepend to bypass the caching repo
                    builder.prependExternalRoot(rb.buildRoot());
                } catch (Exception e) {
                    log.warning("Failed to add repository: " + repo + ": "+e.getMessage());
                }
            }
        }

        // Caching repo
        builder.addCeylonHome();

        // add remote module repo
        builder.addModulesCeylonLangOrg();

        return builder.buildRepository();
    }

    public static Repository makeOutputRepository(String outRepo, Logger log) {
        if(outRepo == null){
            outRepo = "modules";
        }

        StructureBuilder structureBuilder;
        if(!isHTTP(outRepo, log)){
            File repoFolder = new File(outRepo);
            if(repoFolder.exists()){
                if(!repoFolder.isDirectory())
                    log.error("Output repository is not a directory: "+outRepo);
                else if(!repoFolder.canWrite())
                    log.error("Output repository is not writable: "+outRepo);
            }else if(!repoFolder.mkdirs())
                log.error("Failed to create output repository: "+outRepo);
            structureBuilder = new FileContentStore(repoFolder);
        }else{
            // HTTP
            structureBuilder = new WebDAVContentStore(outRepo, log);
        }
        return new SimpleRepository(structureBuilder, log);
    }

    private static boolean isHTTP(String repo, Logger log) {
        try {
            URL url = new URL(repo);
            String protocol = url.getProtocol();
            return "http".equals(protocol) || "https".equals(protocol);
        } catch (MalformedURLException e) {
            log.debug("[Invalid repo URL: "+repo+" (assuming file)]");
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

    public static Package getPackage(Object decl) {
        if(decl == null)
            return null;
        if(decl instanceof Package)
            return (Package) decl;
        return getPackage(((Declaration)decl).getContainer());
    }
}
