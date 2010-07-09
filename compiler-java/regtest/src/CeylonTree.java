import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public abstract class CeylonTree {
  
  /**
   * Create a Ceylon compilation unit from an ANTLR tree.
   */
  public static CompilationUnit build(Tree src) {
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
    return (CompilationUnit) consume(src);
  }

  /**
   * Create a Ceylon tree from an ANTLR tree.
   */
  private static CeylonTree consume(Tree src) {
    Class<? extends CeylonTree> klass = classFor(src);

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

    dst.children = dst.processChildren(src);

    return dst;
  }

  /**
   * Decide which class to use to represent this node
   */
  private static Class<? extends CeylonTree> classFor(Tree src) {
    Token token = ((CommonTree) src).getToken();
    if (token == null)
      return CompilationUnit.class;

    int type = token.getType();
    if (type == ceylonParser.TYPE_DECL) {
      // TYPE_DECL nodes are used to ensure that a type's
      // annotations are grouped together with the type
      // itself.  We rewrite them to bring the type (eg
      // CLASS_DECL) to the top, and put the annotations
      // as the child of the type.
      Tree firstChild = src.getChild(0);
      int childType = ((CommonTree) firstChild).getToken().getType();
      assert childType == ceylonParser.TYPE_DECL
          || childType == ceylonParser.CLASS_DECL
          || childType == ceylonParser.INTERFACE_DECL
          || childType == ceylonParser.ALIAS_DECL
           : ceylonParser.tokenNames[childType];
      return classFor(firstChild);
    }
   
    Class<? extends CeylonTree> klass = classes.get(type);
    assert klass != null : type + ": " + ceylonParser.tokenNames[type];
    return klass;
  }

  /**
   * Mapping of ANTLR tokens to CeylonTree subclasses.
   */
  private static Map<Integer, Class<? extends CeylonTree>> classes;

  static {
    classes = new HashMap<Integer, Class<? extends CeylonTree>>();

    // NB CompilationUnit.class is handled in classFor(Tree)
    classes.put(ceylonParser.IMPORT_LIST,          ImportList.class);
    classes.put(ceylonParser.IMPORT_DECL,          ImportDeclaration.class);
    classes.put(ceylonParser.IMPORT_PATH,          ImportPath.class);
    classes.put(ceylonParser.IMPORT_WILDCARD,      ImportWildcard.class);
    classes.put(ceylonParser.CLASS_DECL,           ClassDeclaration.class);
    classes.put(ceylonParser.INTERFACE_DECL,       InterfaceDeclaration.class);
    classes.put(ceylonParser.ALIAS_DECL,           AliasDeclaration.class);
    classes.put(ceylonParser.TYPE_NAME,            TypeName.class);
    classes.put(ceylonParser.TYPE_PARAMETER_LIST,  TypeParameterList.class);
    classes.put(ceylonParser.TYPE_PARAMETER,       TypeParameter.class);
    classes.put(ceylonParser.MEMBER_DECL,          MemberDeclaration.class);
    classes.put(ceylonParser.MEMBER_NAME,          MemberName.class);
    classes.put(ceylonParser.MEMBER_TYPE,          MemberType.class);

    // XXX possibly rubbish node subclasses    
    
    classes.put(ceylonParser.LANG_ANNOTATION,      Annotation.class);
    classes.put(ceylonParser.USER_ANNOTATION,      Annotation.class);
    classes.put(ceylonParser.ANNOTATION_LIST,      AnnotationList.class);
    classes.put(ceylonParser.ANNOTATION_NAME,      AnnotationName.class);
    classes.put(ceylonParser.ABSTRACT,             Abstract.class);
    classes.put(ceylonParser.ARG_LIST,             ArgumentList.class);
    classes.put(ceylonParser.CALL_EXPR,            CallExpression.class);
    classes.put(ceylonParser.DOT,                  Dot.class);
    classes.put(ceylonParser.EXPR,                 Expression.class);
    classes.put(ceylonParser.LIDENTIFIER,          LIdentifier.class);
    classes.put(ceylonParser.PUBLIC,               Public.class);
    classes.put(ceylonParser.SATISFIES_LIST,       SatisfiesList.class);
    classes.put(ceylonParser.STMT_LIST,            StatementList.class);
    classes.put(ceylonParser.SIMPLESTRINGLITERAL,  SimpleStringLiteral.class);
    classes.put(ceylonParser.STRING_CST,           StringConstant.class);
    classes.put(ceylonParser.SUBTYPE,              SubType.class);
    classes.put(ceylonParser.TYPE,                 Type.class);
    classes.put(ceylonParser.TYPE_ARG_LIST,        TypeArgumentList.class);
    classes.put(ceylonParser.TYPE_CONSTRAINT,      TypeConstraint.class);
    classes.put(ceylonParser.TYPE_CONSTRAINT_LIST, TypeConstraintList.class);
    classes.put(ceylonParser.UIDENTIFIER,          UIdentifier.class);
  }

  /**
   * This node's children.
   */
  public List<CeylonTree> children;

  /**
   * Initialize this node's children.  The default behaviour is to
   * recursively consume the tree, though this may be overridden or
   * extended by subclasses.
   */
  protected List<CeylonTree> processChildren(Tree src) {
    ListBuffer<CeylonTree> children = new ListBuffer<CeylonTree>();
    for (int i = 0; i < src.getChildCount(); i++)
      children.append(consume(src.getChild(i)));
    return children.toList();
  }

  /**
   * Base class for visitors.
   */
  public static class Visitor {
    public void visit(CompilationUnit that)      { visitDefault(that); }
    public void visit(ImportList that)           { visitDefault(that); }
    public void visit(ImportDeclaration that)    { visitDefault(that); }
    public void visit(ImportPath that)           { visitDefault(that); }
    public void visit(ImportWildcard that)       { visitDefault(that); }
    public void visit(ClassDeclaration that)     { visitDefault(that); }
    public void visit(InterfaceDeclaration that) { visitDefault(that); }
    public void visit(AliasDeclaration that)     { visitDefault(that); }
    public void visit(TypeName that)             { visitDefault(that); }
    public void visit(TypeParameterList that)    { visitDefault(that); }
    public void visit(TypeParameter that)        { visitDefault(that); }
    public void visit(MemberDeclaration that)    { visitDefault(that); }
    public void visit(MemberName that)           { visitDefault(that); }
    public void visit(MemberType that)           { visitDefault(that); }

    // XXX possibly rubbish node subclasses

    public void visit(Abstract that)             { visitDefault(that); }
    public void visit(Annotation that)           { visitDefault(that); }
    public void visit(AnnotationList that)       { visitDefault(that); }
    public void visit(AnnotationName that)       { visitDefault(that); }
    public void visit(ArgumentList that)         { visitDefault(that); }
    public void visit(CallExpression that)       { visitDefault(that); }
    public void visit(Dot that)                  { visitDefault(that); }
    public void visit(Expression that)           { visitDefault(that); }
    public void visit(LIdentifier that)          { visitDefault(that); }
    public void visit(Public that)               { visitDefault(that); }
    public void visit(SatisfiesList that)        { visitDefault(that); }
    public void visit(StatementList that)        { visitDefault(that); }
    public void visit(SimpleStringLiteral that)  { visitDefault(that); }
    public void visit(StringConstant that)       { visitDefault(that); }
    public void visit(SubType that)              { visitDefault(that); }
    public void visit(Type that)                 { visitDefault(that); }
    public void visit(TypeArgumentList that)     { visitDefault(that); }
    public void visit(TypeConstraint that)       { visitDefault(that); }
    public void visit(TypeConstraintList that)   { visitDefault(that); }
    public void visit(UIdentifier that)          { visitDefault(that); }

    public void visitDefault(CeylonTree tree) {
      throw new RuntimeException();
    }
  }

  /**
   * Visit this tree with a given visitor.
   */
  public abstract void accept(Visitor v);

  /**
   * Convert a tree to a pretty-printed string
   */
  public String toString() {
    StringWriter s = new StringWriter();
    this.accept(new CeylonTreePrinter(s));
    return s.toString();
  }


  // Node subclasses

  /**
   * A compilation unit represents one source file.
   *
   * CompilationUnit = ImportList? TypeDeclaration+
   */
  public static class CompilationUnit extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }

    public ImportList getImportList() {
      if (children.nonEmpty()) {
        CeylonTree firstChild = children.head;
        if (firstChild instanceof ImportList)
          return (ImportList) firstChild;
      }
      return null;
    }

    public List<ImportDeclaration> getImports() {
      ImportList importList = getImportList();
      if (importList != null)
        return importList.getImports();
      return List.<ImportDeclaration> nil();
    }

    public List<TypeDeclaration> getTypeDecls() {
      List<CeylonTree> result = children;
      if (result.head instanceof ImportList)
        result = result.tail;
      return List.convert(TypeDeclaration.class, result);
    }
  }

  /**
   * A list of import declarations.
   *
   * ImportList = ImportDeclaration*
   */
  public static class ImportList extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }

    public List<ImportDeclaration> getImports() {
      return List.convert(ImportDeclaration.class, children);
    }
  }

  /**
   * An import declaration.
   */
  public static class ImportDeclaration extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * An import path.
   */
  public static class ImportPath extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A wildcard at the end of an import path.
   */
  public static class ImportWildcard extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A type declaration.  This abstract class is the parent
   * class for items which may be wrapped in TYPE_DECL nodes
   * in the parse tree, and contains the functionality to
   * unwrap them.
   */
  public static abstract class TypeDeclaration extends CeylonTree {
    protected List<CeylonTree> processChildren(Tree src) {
      // If the node is not a TYPE_DECL then we have nothing to do
      if (((CommonTree) src).getToken().getType() != ceylonParser.TYPE_DECL)
        return super.processChildren(src);

      // Firstly, remove TYPE_DECL nodes that have exactly
      // one child that is also a TYPE_DECL node.
      if (src.getChildCount() == 1) {
        Tree child = src.getChild(0);
        if (((CommonTree) child).getToken().getType() == ceylonParser.TYPE_DECL)
          return processChildren(child);
      }

      // Secondly, rewrite:
      //   TYPE_DECL
      //     CLASS_DECL | INTERFACE_DECL | ALIAS_DECL | MEMBER_DECL
      //       children*
      //     ANNOTATION_LIST?
      // as:
      //     CLASS_DECL | INTERFACE_DECL | ALIAS_DECL | MEMBER_DECL
      //       children*
      //       ANNOTATION_LIST?
      assert src.getChildCount() == 1 || src.getChildCount() == 2;
      ListBuffer<CeylonTree> children = new ListBuffer<CeylonTree>();
      children.appendList(super.processChildren(src.getChild(0)));
      for (int i = 1; i < src.getChildCount(); i++)
        children.append(consume(src.getChild(i)));
      return children.toList();
    }
  }
  
  /**
   * A class declaration.
   */
  public static class ClassDeclaration extends TypeDeclaration {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A interface declaration.
   */
  public static class InterfaceDeclaration extends TypeDeclaration {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * An alias declaration.
   */
  public static class AliasDeclaration extends TypeDeclaration {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A type name.
   */
  public static class TypeName extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A list of type parameters.
   */
  public static class TypeParameterList extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A type parameter.
   */
  public static class TypeParameter extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A member declaration.
   */
  public static class MemberDeclaration extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A member name.
   */
  public static class MemberName extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A member type.
   */
  public static class MemberType extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  
  // XXX possibly rubbish node subclasses

  /**
   * The word "abstract"
   */
  public static class Abstract extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }
  /**
   * An annotation.
   */
  public static class Annotation extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A list of annotations.
   */
  public static class AnnotationList extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * An annotation name.
   */
  public static class AnnotationName extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A list of arguments.
   */
  public static class ArgumentList extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A call expression.
   */
  public static class CallExpression extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A dot.
   */
  public static class Dot extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * An expression.
   */
  public static class Expression extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * An lowercase identifier.
   */
  public static class LIdentifier extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * The word "public".
   */
  public static class Public extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A list of satisfies.
   */
  public static class SatisfiesList extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A list of statements.
   */
  public static class StatementList extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A simple string literal.
   */
  public static class SimpleStringLiteral extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A string constant.
   */
  public static class StringConstant extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * The word "subtype"
   */
  public static class SubType extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A type.
   */
  public static class Type extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A list of type arguments.
   */
  public static class TypeArgumentList extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A type constraint.
   */
  public static class TypeConstraint extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A list of type constraints.
   */
  public static class TypeConstraintList extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * An uppercase identifier.
   */
  public static class UIdentifier extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }
}
