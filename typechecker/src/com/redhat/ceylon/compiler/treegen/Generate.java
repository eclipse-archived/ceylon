package com.redhat.ceylon.compiler.treegen;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;

public class Generate {

	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		tree(file);
		builder(file);
		walker(file);
		visitor(file);
	}
	
	private static void tree(File file) throws Exception {
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        TreegenLexer lexer = new TreegenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TreegenParser parser = new TreegenParser(tokens);
        File out = new File("gensrc/com/redhat/ceylon/compiler/tree/Tree.java");
        out.createNewFile();
        parser.out=new PrintStream(out);
        parser.nodeList();
	}

	private static void builder(File file) throws Exception {
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        BuildergenLexer lexer = new BuildergenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BuildergenParser parser = new BuildergenParser(tokens);
        File out = new File("gensrc/com/redhat/ceylon/compiler/tree/TreeBuilder.java");
        out.createNewFile();
        parser.out=new PrintStream(out);
        parser.nodeList();
	}
	
	private static void walker(File file) throws Exception {
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        WalkergenLexer lexer = new WalkergenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        WalkergenParser parser = new WalkergenParser(tokens);
        File out = new File("gensrc/com/redhat/ceylon/compiler/tree/Walker.java");
        out.createNewFile();
        parser.out=new PrintStream(out);
        parser.nodeList();
	}
	
	private static void visitor(File file) throws Exception {
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        VisitorgenLexer lexer = new VisitorgenLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        VisitorgenParser parser = new VisitorgenParser(tokens);
        File out = new File("gensrc/com/redhat/ceylon/compiler/tree/Visitor.java");
        out.createNewFile();
        parser.out=new PrintStream(out);
        parser.nodeList();
	}
	
}
