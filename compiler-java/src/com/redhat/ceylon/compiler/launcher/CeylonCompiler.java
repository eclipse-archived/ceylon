package com.redhat.ceylon.compiler.launcher;

import java.io.*;
import java.util.*;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import com.redhat.ceylon.compiler.tree.*;
import com.redhat.ceylon.compiler.tree.CeylonTree.Visitor;
import com.redhat.ceylon.compiler.codegen.Gen;
import com.redhat.ceylon.compiler.parser.*;

public class CeylonCompiler
{
    public class EmptyWalker extends Visitor {
        public void visitDefault(CeylonTree tree) {
            tree.visitChildren(this);
        }
    }

final PrintStream out;
  final FileInputStream is;

  CeylonCompiler(FileInputStream is, PrintStream out)
  {
    this.out = out;
    this.is = is;
  }

  void run(String comment,
           boolean consumeTree,
           boolean generateCode,
           String filename)
    throws Exception
  {
    ANTLRInputStream input
      = new ANTLRInputStream(is);

    CeylonLexer lexer = new CeylonLexer(input);

    CommonTokenStream tokens = new CommonTokenStream(lexer);

    CeylonParser parser = new CeylonParser(tokens);
    CeylonParser.compilationUnit_return r = parser.compilationUnit();

    CommonTree t = (CommonTree)r.getTree();

    if (comment != null)
        out.println("# "+ comment);

    if (! consumeTree) {
        new TreeVisitor().visit(t, new PrintTree());
        out.println();
    }

    if (parser.getNumberOfSyntaxErrors() == 0 &&
            lexer.getNumberOfSyntaxErrors() == 0 &&
            consumeTree) {
        CeylonTree.CompilationUnit cu = CeylonTree.build(t, filename);

        if (false) {
            PrintWriter w = new PrintWriter(out);
            cu.accept(new CeylonTreePrinter(w));
            w.flush();
            out.println();
        }

        cu.accept(new Grok());

        if (false) {
            out.print(cu);
            out.println();
        }

        if (generateCode) {
            out.println();
            new Gen().run(cu);
        }
    }
  }

  class PrintTree implements TreeVisitorAction
  {
    void indent()
    {
      out.println();
      for (int i = 0; i < depth; i++)
    out.print("  ");
    }

    public Object pre (Object o)
    {
      Tree t = (Tree)o;
      String s = null;
      Token tok = ((CommonTree)o).getToken();
      if (tok != null)
        s = tok.getText();
      if (t.getChildCount()==0 && s != null)
        {
          out.print(" " + s);
        }
      else
        {
          indent();
          out.print("(" + t);
        }
      depth++;

      return o;
    }

    public Object post (Object o)
    {
      Tree t = (Tree)o;
      String s = null;
      Token tok = ((CommonTree)o).getToken();
      if (tok != null)
        s = tok.getText();
      if (t.getChildCount()!=0 || s == null)
    out.print(")");
      depth--;

      return o;
    }

    int depth;
  }

  public static void main(String[] argv)
    throws Exception
  {
    Vector<String> args = new Vector<String> (Arrays.asList(argv));

    String outputdir = "";
    boolean consumeTree = false;
    boolean generateCode = false;

    for (int i = 0; i < args.size(); i++) {
      if (args.get(i).equals("-d"))
        {
          args.remove(i);
          outputdir = args.get(i);
          args.remove(i);
          new File(outputdir).mkdir();
          i--;
        }
      else if (args.get(i).equals("-g"))
        {
          consumeTree = true;
          args.remove(i);
          i--;
        }
      else if (args.get(i).equals("-t"))
        {
          consumeTree = true;
          generateCode = true;
          args.remove(i);
          i--;
        }
    }

    for (String filename: args) {

      CeylonCompiler theCeylonCompiler;
      String infile = filename;

      if (outputdir != "")
        {
          int slash = filename.lastIndexOf(File.separatorChar);
          if (slash >= 0)
            infile = filename.substring(slash + 1);
          theCeylonCompiler
            = new CeylonCompiler (new FileInputStream(filename),
                                  new PrintStream(new FileOutputStream
                                                  (outputdir
                                                   + File.separatorChar
                                                   + infile + ".out")));
        }
      else
        {
          theCeylonCompiler
            = new CeylonCompiler (new FileInputStream(filename), System.out);
        }

      System.err.println(infile);
      theCeylonCompiler.run(infile, consumeTree, generateCode, filename);

    }
  }
}
