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
import java.util.List;

import javax.tools.JavaFileObject.Kind;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.mirror.AnnotatedMirror;
import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.sun.tools.javac.code.Symbol.ClassSymbol;

/**
 * Utility functions that are used in various packages and/or components of the 
 * Ceylon toolset.
 * @see com.redhat.ceylon.compiler.java.codegen.CodegenUtil
 */
public class Util {

    public static String quote(String name) {
        return Naming.quote(name);
    }

    public static String quoteIfJavaKeyword(String name){
        return Naming.quoteIfJavaKeyword(name);
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

    public static String strip(String str){
        return Naming.stripLeadingDollar(str);
    }

    public static String strip(String name, boolean isCeylon, boolean isShared) {
        String stripped = strip(name);
        if(isCeylon && !isShared && name.endsWith("$priv"))
            return stripped.substring(0, stripped.length() - 5);
        return stripped;
    }

    public static String capitalize(String str){
        return Naming.capitalize(str);
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
    
    /**
     * Is the declaration a method declared to return {@code void} 
     * (as opposed to a {@code Anything})
     */
    public static boolean isUnboxedVoid(Declaration decl) {
        return (decl instanceof Method && ((Method)decl).isDeclaredVoid());
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
        String name;
        AnnotationMirror annot = mirror.getAnnotation(AbstractModelLoader.CEYLON_NAME_ANNOTATION);
        if (annot != null) {
            name = (String)annot.getValue();
        } else {
            name = mirror.getName();
            if (mirror instanceof ClassMirror
                    && Util.isInitialLowerCase(name)
                    && name.endsWith("_")
                    && mirror.getAnnotation(AbstractModelLoader.CEYLON_CEYLON_ANNOTATION) != null) {
                name = name.substring(0, name.length()-1);
            }
        }
        return name;
    }

    public static boolean isInitialLowerCase(String name) {
        return !name.isEmpty() && isLowerCase(name.charAt(0));
    }

    public static boolean isLowerCase(char c) {
        return Character.isLowerCase(c) || c == '_';
    }
}
