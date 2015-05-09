package com.redhat.ceylon.compiler.typechecker.treegen;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;

public class Generate {

    private static final String GENERATED_PACKAGE_DIR = "gensrc/com/redhat/ceylon/compiler/typechecker/tree/";

    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        tree(file);
        //builder(file);
        walker(file);
        visitor(file);
        visitorAdaptor(file);
        validator(file);
    }
    
    private static void tree(File file) throws Exception {
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        TreegenLexer lexer = new TreegenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TreegenParser parser = new TreegenParser(tokens);
        File out = new File( GENERATED_PACKAGE_DIR + "Tree.java" );
        out.createNewFile();
        Util.out=new PrintStream(out);
        parser.nodeList();
    }

    /*private static void builder(File file) throws Exception {
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        BuildergenLexer lexer = new BuildergenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BuildergenParser parser = new BuildergenParser(tokens);
        File out = new File( GENERATED_PACKAGE_DIR + "Builder.java" );
        out.createNewFile();
        Util.out=new PrintStream(out);
        parser.nodeList();
    }*/
    
    private static void walker(File file) throws Exception {
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        WalkergenLexer lexer = new WalkergenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        WalkergenParser parser = new WalkergenParser(tokens);
        File out = new File( GENERATED_PACKAGE_DIR + "Walker.java" );
        out.createNewFile();
        Util.out=new PrintStream(out);
        parser.nodeList();
    }
    
    private static void visitor(File file) throws Exception {
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        VisitorgenLexer lexer = new VisitorgenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        VisitorgenParser parser = new VisitorgenParser(tokens);
        File out = new File( GENERATED_PACKAGE_DIR + "Visitor.java" );
        out.createNewFile();
        Util.out=new PrintStream(out);
        parser.nodeList();
    }
    
    private static void visitorAdaptor(File file) throws Exception {
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        VisitorAdaptorgenLexer lexer = new VisitorAdaptorgenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        VisitorAdaptorgenParser parser = new VisitorAdaptorgenParser(tokens);
        File out = new File( GENERATED_PACKAGE_DIR + "VisitorAdaptor.java" );
        out.createNewFile();
        Util.out=new PrintStream(out);
        parser.nodeList();
    }
    
    private static void validator(File file) throws Exception {
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        ValidatorgenLexer lexer = new ValidatorgenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ValidatorgenParser parser = new ValidatorgenParser(tokens);
        File out = new File( GENERATED_PACKAGE_DIR + "Validator.java" );
        out.createNewFile();
        Util.out=new PrintStream(out);
        parser.nodeList();
    }
    
}
