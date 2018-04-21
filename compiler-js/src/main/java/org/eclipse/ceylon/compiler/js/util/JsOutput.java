/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.typechecker.io.VirtualFile;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.Value;

import org.eclipse.ceylon.compiler.js.CompilerErrorException;
import org.eclipse.ceylon.compiler.js.JsCompiler;
import org.eclipse.ceylon.compiler.js.loader.MetamodelVisitor;
import org.eclipse.ceylon.compiler.js.loader.ModelEncoder;
import org.eclipse.ceylon.compiler.js.loader.NpmAware;

/** A container for things we need to keep per-module. */
public class JsOutput {
    private File outfile;
    private File modfile;
    private Writer writer;
    protected String clalias = "";
    protected final Module module;
    protected boolean modelDone;
    private final Set<File> s = new HashSet<File>();
    final Map<String,String> requires = new HashMap<String,String>();
    public final MetamodelVisitor mmg;
    private static final String encoding = "UTF-8";
    private JsWriter jsw;
    private final boolean compilingLanguageModule;
    
    public JsOutput(Module m, boolean compilingLanguageModule) throws IOException {
        this.module = m;
        this.compilingLanguageModule = compilingLanguageModule;
        mmg = m==null?null:new MetamodelVisitor(m);
    }
    public void setJsWriter(JsWriter value) {
        jsw = value;
    }
    public Writer getWriter() throws IOException {
        if (writer == null) {
            outfile = File.createTempFile("ceylon-jsout-", ".tmp");
            writer = new OutputStreamWriter(new FileOutputStream(outfile), encoding);
        }
        return writer;
    }
    public File close() throws IOException {
        if (writer != null) {
            writer.close();
        }
        return outfile;
    }
    public File getModelFile() {
        return modfile;
    }

    public void addSource(File src) {
        s.add(src);
    }
    public Set<File> getSources() { return s; }

    /** Writes the model file to a temporary file. */
    protected void writeModelFile() throws IOException {
        modfile = File.createTempFile("ceylon-jsmod-", ".tmp");
        try (OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(modfile), encoding)) {
            JsCompiler.beginWrapper(fw);
            fw.write("x$.$M$=");
            ModelEncoder.encodeModel(mmg.getModel(), fw);
            fw.write(";\n");
            JsCompiler.endWrapper(fw);
        } finally {
        }
    }

    /** Write the function to retrieve or define the model JSON map. */
    protected void writeModelRetriever() throws IOException {
        out("require('", JsCompiler.scriptPath(module), "-model').$M$");
    }

    public void encodeModel(final JsIdentifierNames names) throws IOException {
        if (!modelDone) {
            modelDone = true;
            writeModelFile();
            out("\nvar _CTM$;function $M$(){if (_CTM$===undefined)_CTM$=");
            writeModelRetriever();
            out(";return _CTM$;}\n");
            out("x$.$M$=$M$;\n");
            if (!compilingLanguageModule) {
                Module clm = module.getLanguageModule();
                clalias = names.moduleAlias(clm) + ".";
                require(clm, names);
                out(clalias, "$addmod$(x$,'", module.getNameAsString(), "/", module.getVersion(), "');\n");
            }
        }
    }

    public void outputFile(File f) {
        try (FileReader in = new FileReader(f)) {
            outputFile(in);
        } catch(IOException ex) {
            throw new CompilerErrorException("Reading from " + f);
        }
    }

    public void outputFile(VirtualFile f) {
        try {
            outputFile(new InputStreamReader(f.getInputStream()));
        } catch(IOException ex) {
            throw new CompilerErrorException("Reading from " + f);
        }
    }

    public void outputFile(Reader in) throws IOException {
        try (BufferedReader r = new BufferedReader(in)) {
            String line = null;
            while ((line = r.readLine()) != null) {
                final String c = line.trim();
                if (!c.isEmpty()) {
                    out(c, "\n");
                }
            }
        }
    }

    public String getLanguageModuleAlias() {
        return clalias;
    }

    public void requireFromNpm(final Module mod, final JsIdentifierNames names) {
        final String modAlias = names.moduleAlias(mod);
        final String path = ((NpmAware)mod).getNpmPath();
        if (requires.put(path, modAlias) == null) {
            //For NPM modules on Node.js we use our own special "require" which will
            //wrap single functions in a proper exports object. If the module name
            //has dashes, dots, or underscores, we transform that into camel casing.
            String modName = mod.getNameAsString();
            int colonIndex = modName.indexOf(':');
            if (colonIndex>0) {
                modName = modName.substring(colonIndex+1);
            }
            String singleFunctionName = 
                    toCamelCase(modName.replace('_', '.')
                                       .replace('-', '.'));
            out("var ", modAlias, "=", "(", getLanguageModuleAlias(), "run$isNode())?",
                    getLanguageModuleAlias(), "npm$req('", singleFunctionName, "','",
                    path, "',require):require('", JsCompiler.scriptPath(mod), "');\n");
            if (modAlias != null && !modAlias.isEmpty()) {
                out(clalias, "$addmod$(", modAlias,",'", modName, "/", mod.getVersion(), "');\n");
            }
        }
    }
    
    private static String toCamelCase(String string) {
        int dotIdx = string.indexOf('.');
        while (dotIdx > 0) {
            string = string.substring(0, dotIdx) +
                    Character.toUpperCase(string.charAt(dotIdx+1)) +
                    string.substring(dotIdx+2);
            dotIdx = string.indexOf('.', dotIdx);
        }
        return string;
    }
    
    public void require(final Module mod, final JsIdentifierNames names) {
        final String path = JsCompiler.scriptPath(mod);
        final String modAlias = names.moduleAlias(mod);
        if (requires.put(path, modAlias) == null) {
            out("var ", modAlias, "=require('", path, "');\n");
            if (modAlias != null && !modAlias.isEmpty()) {
                out(clalias, "$addmod$(", modAlias,",'", mod.getNameAsString(), "/", mod.getVersion(), "');\n");
            }
        }
    }

    /** Print generated code to the Writer.
     * @param code The main code
     * @param codez Optional additional strings to print after the main code. */
    public void out(String code, String... codez) {
        if (jsw != null) {
            jsw.write(code, codez);
            return;
        }
        try {
            getWriter().write(code);
            for (String s : codez) {
                getWriter().write(s);
            }
        }
        catch (IOException ioe) {
            throw new RuntimeException("Generating JS code", ioe);
        }
    }

    public void publishUnsharedDeclarations(JsIdentifierNames names) {
        //#489 lists with unshared toplevel members of each package
        for (org.eclipse.ceylon.model.typechecker.model.Package pkg : module.getPackages()) {
            ArrayList<Declaration> unsharedDecls = new ArrayList<>(pkg.getMembers().size());
            for (Declaration d : pkg.getMembers()) {
                if (!d.isShared()
                        && !(d.isAnonymous() && d.getName() != null && d.getName().startsWith("anonymous#"))
                        && (!d.isNative() || d.getNativeBackends().supports(Backend.JavaScript))) {
                    unsharedDecls.add(d);
                }
            }
            if (!unsharedDecls.isEmpty()) {
                out("x$.$pkgunsh$", pkg.getNameAsString().replace('.', '$'), "={");
                boolean first=true;
                for (Declaration d : unsharedDecls) {
                    if (d.getName() == null) continue;
                    //TODO only use quotes when absolutely necessary
                    if (d.isAnonymous()) {
                        //Don't generate anything for anonymous types
                    } else if (d instanceof Setter) {
                        //ignore
                        if (((Setter) d).getGetter() == null) {
                            if (first)first=false;else out(",");
                            out("'", d.getName(), "':", names.setter(d));
                        }
                    } else if (d instanceof Value) {
                        if (first)first=false;else out(",");
                        out("'", d.getName(), "':", names.getter(d, true));
                    } else {
                        if (first)first=false;else out(",");
                        out("'", d.getName(), "':", names.name(d));
                    }
                }
                out("};\n");
            }
        }
    }

    public void openWrapper() throws IOException {
        JsCompiler.beginWrapper(getWriter());
        JsCompiler.requireWrapper(getWriter(), module);
    }
    public void closeWrapper() throws IOException {
        JsCompiler.endWrapper(writer);
    }
}