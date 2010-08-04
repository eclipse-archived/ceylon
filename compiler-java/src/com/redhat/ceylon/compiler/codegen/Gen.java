package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.PUBLIC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.lang.model.element.TypeElement;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.redhat.ceylon.compiler.tree.*;
import com.sun.source.tree.*;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.Options;

import com.redhat.ceylon.compiler.tree.CeylonTree;
import com.redhat.ceylon.compiler.tree.CeylonTree.*;

public class Gen {
    Context context;
    TreeMaker make;
    Names names;
    ClassReader reader;
    Resolve resolve;
    JavaCompiler compiler;
    DiagnosticCollector<JavaFileObject> diagnostics;
    JavacFileManager fileManager;
    JavacTaskImpl task;
    Options options;
    
    JCCompilationUnit jcCompilationUnit;

    public Gen() throws Exception {
        compiler = ToolProvider.getSystemJavaCompiler();
        diagnostics
        = new DiagnosticCollector<JavaFileObject>();
        fileManager
        = (JavacFileManager)compiler.getStandardFileManager(diagnostics, null, null);
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
                Arrays.asList(new File("/tmp")));

        fileManager.setLocation(StandardLocation.CLASS_PATH,
                Arrays.asList(new File("/tmp")));
        Iterable<? extends JavaFileObject> compilationUnits
        = fileManager.getJavaFileObjectsFromStrings(new ArrayList<String>());

        JavaCompiler.CompilationTask aTask
        = compiler.getTask(null, fileManager,
                diagnostics,
                Arrays.asList("-g", /* "-verbose", */
                        "-source", "7", "-XDallowFunctionTypes"),
                        null, compilationUnits);
        setup((JavacTaskImpl)aTask);
    }

    void setup (JavacTaskImpl task) {
        this.task = task;

        context = task.getContext();
        options = Options.instance(context);
        // It's a bit weird to see "invokedynamic" set here,
        // but it has to be done before Resolve.instance().
        options.put("invokedynamic", "invokedynamic");
        make = TreeMaker.instance(context);
        names = Names.instance(context);
        reader = ClassReader.instance(context);
        resolve = Resolve.instance(context);
    }

    public void run(CeylonTree.CompilationUnit t, String filename) 
        throws IOException
    {        
        JCCompilationUnit tree = convert(t);
        CeylonFileObject f = 
            new CeylonFileObject(fileManager.getFileForInput(filename));
        
        tree.sourcefile = f;

        Iterable<? extends TypeElement> result =
            task.enter(List.of(tree));
        Iterable<? extends JavaFileObject> files = task.generate(result);

        System.out.println(diagnostics.getDiagnostics());
    }
    
    public JCCompilationUnit convert(CeylonTree.CompilationUnit t) {
        List<JCTree> classes = List.<JCTree>nil();
        for (ClassDeclaration decl: t.classDecls) {
            classes = classes.append(convert(decl));
        }

        JCCompilationUnit topLev =
            make.TopLevel(List.<JCTree.JCAnnotation>nil(),
                    /* package id*/ null, classes);

        System.out.println(topLev);
        return topLev;
    }

    public JCClassDecl convert(CeylonTree.ClassDeclaration cdecl) {
        JCClassDecl classDef = 
            make.ClassDef(make.Modifiers(PUBLIC, List.<JCTree.JCAnnotation>nil()),
                    names.fromString(cdecl.nameAsString()),
                    List.<JCTypeParameter>nil(), null,
                    List.<JCExpression>nil(),
                    List.<JCTree>nil());

        System.out.println(classDef);

        return classDef;
    }

    public JCBlock convert(CeylonTree.Block block) {
        return make.Block(0, List.<JCStatement>nil());
    }

}
