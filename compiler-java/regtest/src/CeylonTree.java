import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public abstract class CeylonTree {
  
  /**
   * Mapping of ANTLR tokens to CeylonTree subclasses.
   */
  private static Map<Integer, Class<? extends CeylonTree>> classes;

  static {
    classes = new HashMap<Integer, Class<? extends CeylonTree>>();

    classes.put(ceylonParser.ANNOTATION, Annotation.class);
    classes.put(ceylonParser.ANNOTATION_LIST, AnnotationList.class);
    classes.put(ceylonParser.ANNOTATION_NAME, AnnotationName.class);
    classes.put(ceylonParser.ARG_LIST, ArgumentList.class);
    classes.put(ceylonParser.CALL_EXPR, CallExpression.class);
    classes.put(ceylonParser.CLASS_DECL, ClassDeclaration.class);
    classes.put(ceylonParser.DOT, Dot.class);
    classes.put(ceylonParser.EXPR, Expression.class);
    classes.put(ceylonParser.LIDENTIFIER, LIdentifier.class);
    classes.put(ceylonParser.MEMBER_NAME, MemberName.class);
    classes.put(ceylonParser.STMT_LIST, StatementList.class);
    classes.put(ceylonParser.SIMPLESTRINGLITERAL, SimpleStringLiteral.class);
    classes.put(ceylonParser.STRING_CST, StringConstant.class);
    classes.put(ceylonParser.TYPE_DECL, TypeDeclaration.class);
    classes.put(ceylonParser.TYPE_NAME, TypeName.class);
    classes.put(ceylonParser.UIDENTIFIER, UIdentifier.class);
  }

  /**
   * Create a CeylonTree from an ANTLR tree.
   */
  public static CeylonTree build(Tree src) {
    Token token = ((CommonTree) src).getToken();
    if (token != null) {
      // ANTLR doesn't create a null top-level node when it
      // would only have one child.  We want one always, to
      // map to the compilation unit, so we create one where
      // necessary.
      Tree tmp = new CommonTree((Token) null);
      tmp.addChild(src);
      src = tmp;
    }
    return consume(src);
  }

  private static CeylonTree consume(Tree src) {
    // Create the node
    Token token = ((CommonTree) src).getToken();

    Class<? extends CeylonTree> klass;
    if (token == null) {
      klass = CompilationUnit.class;
    }
    else {
      int type = token.getType();
      klass = classes.get(type);
      assert klass != null : type + ": " + ceylonParser.tokenNames[type];
    }

    CeylonTree dst;
    try {
      dst = klass.newInstance();
    }
    catch (InstantiationException e) {
      throw new RuntimeException(e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    dst.token = token;
    
    // Recurse into the children
    for (int i = 0; i < src.getChildCount(); i++)
      dst.addChild(consume(src.getChild(i)));

    return dst;
  }

  private CeylonTree parent;
  private List<CeylonTree> children;

  protected void addChild(CeylonTree child) {
    if (children == null)
      children = new LinkedList<CeylonTree>();
    children.add(child);
    assert child.parent == null;
    child.parent = this;
  }

  public int getChildCount() {
    if (children == null)
      return 0;
    return children.size();
  }

  public CeylonTree getChild(int index) {
    return children.get(index);
  }

  private Token token;

  public int getTokenType() {
    return token.getType();
  }
  public String getTokenTypeName() {
    return ceylonParser.tokenNames[getTokenType()];
  }
  public String getTokenText() {
    return token.getText();
  }

  /**
   * Visit this tree with a given visitor.
   */
  public abstract void accept(CeylonTreeVisitor v);

  /**
   * Convert a tree to a pretty-printed string
   */
  public String toString() {
    StringWriter s = new StringWriter();
    this.accept(new CeylonTreePrinter(s));
    return s.toString();
  }

  /**
   * An annotation.
   */
  public static class Annotation extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A list of annotations.
   */
  public static class AnnotationList extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * An annotation name.
   */
  public static class AnnotationName extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A list of arguments.
   */
  public static class ArgumentList extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A call expression.
   */
  public static class CallExpression extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A class declaration.
   */
  public static class ClassDeclaration extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A compilation unit.
   */
  public static class CompilationUnit extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A dot.
   */
  public static class Dot extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * An expression.
   */
  public static class Expression extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * An lowercase identifier.
   */
  public static class LIdentifier extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A member name.
   */
  public static class MemberName extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A list of statements.
   */
  public static class StatementList extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A simple string literal.
   */
  public static class SimpleStringLiteral extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A string constant.
   */
  public static class StringConstant extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A type declaration.
   */
  public static class TypeDeclaration extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * A type name.
   */
  public static class TypeName extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }

  /**
   * An uppercase identifier.
   */
  public static class UIdentifier extends CeylonTree {
    public void accept(CeylonTreeVisitor v) { v.visit(this); }
  }
}
