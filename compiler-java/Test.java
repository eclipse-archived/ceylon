import java.io.*;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

public class Test
{
  public static void main(String[] args)
    throws Exception
  {
    ANTLRInputStream input 
      = new ANTLRInputStream(new FileInputStream("/home/aph/ceylon/try1"));

    ceylonLexer lexer = new ceylonLexer(input);

    CommonTokenStream tokens = new CommonTokenStream(lexer);

    ceylonParser parser = new ceylonParser(tokens);
    ceylonParser.compilationUnit_return r = parser.compilationUnit();

    CommonTree t = (CommonTree)r.getTree();
    System.out.println(t.toStringTree());

    // lexer.mSIMPLESTRINGLITERAL();
    // parser.stringLiteral();
    System.out.println(t);
  }
}
