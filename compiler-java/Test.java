import java.io.*;
import org.antlr.runtime.*;

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
    ceylonParser.program_return_r = parser.program();

    CommonTree t = (CommonTree)r.getTree();
    System.out.println(t.toStringtree);

    // lexer.mSIMPLESTRINGLITERAL();
    // parser.stringLiteral();
  }
}
