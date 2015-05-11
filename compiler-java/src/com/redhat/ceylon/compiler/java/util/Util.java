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

import com.redhat.ceylon.common.JVMModuleUtil;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.loader.mirror.AnnotatedMirror;
import com.redhat.ceylon.model.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Method;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.sun.tools.javac.code.Symbol.ClassSymbol;

/**
 * Utility functions that are used in various packages and/or components of the 
 * Ceylon toolset.
 * @see com.redhat.ceylon.compiler.java.codegen.CodegenUtil
 */
public class Util {

    public static String quote(String name) {
        return JVMModuleUtil.quote(name);
    }

    public static String quoteIfJavaKeyword(String name){
        return JVMModuleUtil.quoteIfJavaKeyword(name);
    }
    
    /**
     * Returns a copy of the given array of identifiers, 
     * {@link #quoteIfJavaKeyword(String) quoting} keyword identifiers as 
     * necessary 
     * @param name The parts of a qualified name
     * @return The parts of the qualified name, quoted if necessary
     */
    public static String[] quoteJavaKeywords(String[] name){
        return JVMModuleUtil.quoteJavaKeywords(name);
    }
    
    /**
     * Returns a copy of the given qualified name, but with any
     * keyword components in the name 
     * {@link #quoteIfJavaKeyword(String) quoted} if necessary 
     * @param qualifiedName
     * @return
     */
    public static String quoteJavaKeywords(String qualifiedName){
        return JVMModuleUtil.quoteJavaKeywords(qualifiedName);
    }

    public static String strip(String str){
        return Naming.stripLeadingDollar(str);
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
        return JvmBackendUtil.getName(parts);
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while((read = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
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
}
