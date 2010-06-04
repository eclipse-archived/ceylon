import java.io.*;
import java.util.*;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

public class CeylonCompiler
{
  int depth;

  PrintStream out;
  FileInputStream is;
  String comment;

  CeylonCompiler(FileInputStream is, PrintStream out)
  {
    this.out = out;
    this.is = is;
  }

  void run(String comment)
    throws Exception
  {
    ANTLRInputStream input
      = new ANTLRInputStream(is);

    ceylonLexer lexer = new ceylonLexer(input);

    CommonTokenStream tokens = new CommonTokenStream(lexer);

    ceylonParser parser = new ceylonParser(tokens);
    ceylonParser.compilationUnit_return r = parser.compilationUnit();

    CommonTree t = (CommonTree)r.getTree();

    if (comment != null)
      out.println("# "+ comment);

    printTree(t);
    out.println();
  }

  void indent()
  {
    out.println();
    for (int i = 0; i < depth; i++)
      out.print("  ");
  }

  void printTree(Tree t)
  {
    String s = null;
    Token tok = ((CommonTree)t).getToken();
    if (tok != null)
      s = tok.getText();
    if ( t.getChildCount()==0 && s != null)
      {
	out.print(s);
      }
    else
      {
	indent();
	out.print("(" + t + " ");

	for (int i = 0; i < t.getChildCount(); i++) {
	  Tree tt = t.getChild(i);
	  if ( i>0 ) {
	    out.print(' ');
	  }
	  depth++;
	  printTree(tt);
	  depth--;
	}

	out.print(")");
      }
  }

  public static void main(String[] argv)
    throws Exception
  {
    Vector<String> args = new Vector<String> (Arrays.asList(argv));

    String outputdir = "";

    for (int i = 0; i < args.size(); i++) {
      if (args.get(i).equals("-d"))
	{
	  args.remove(i);
	  outputdir = args.get(i);
	  args.remove(i);
	  new File(outputdir).mkdir();
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
      theCeylonCompiler.run(infile);
    }
  }
}
