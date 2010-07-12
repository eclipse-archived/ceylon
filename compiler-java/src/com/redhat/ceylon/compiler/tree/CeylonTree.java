package com.redhat.ceylon.compiler.tree;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

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
    Token token = ((CommonTree) src).getToken();

    Class<? extends CeylonTree> klass;
    if (token == null) {
      klass = CompilationUnit.class;
    }
    else {
      int type = token.getType();
      klass = classes.get(type);
      assert klass != null : type + ": " + CeylonParser.tokenNames[type];
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

    ListBuffer<CeylonTree> children = new ListBuffer<CeylonTree>();
    for (int i = 0; i < src.getChildCount(); i++) {
      CeylonTree child = consume(src.getChild(i));
      child.parent = dst;
      children.append(child);
    }
    dst.children = children.toList();

    return dst;
  }

  /**
   * Mapping of ANTLR tokens to CeylonTree subclasses.
   */
  private static Map<Integer, Class<? extends CeylonTree>> classes;

  static {
    classes = new HashMap<Integer, Class<? extends CeylonTree>>();

    // NB CompilationUnit.class is handled in classFor(Tree)
    classes.put(CeylonParser.IMPORT_LIST,          ImportList.class);
    classes.put(CeylonParser.IMPORT_DECL,          ImportDeclaration.class);
    classes.put(CeylonParser.IMPORT_PATH,          ImportPath.class);
    classes.put(CeylonParser.IMPORT_WILDCARD,      ImportWildcard.class);
    classes.put(CeylonParser.TYPE_DECL,            TypeDeclaration.class);
    classes.put(CeylonParser.CLASS_DECL,           ClassDeclaration.class);
    classes.put(CeylonParser.INTERFACE_DECL,       InterfaceDeclaration.class);
    classes.put(CeylonParser.ALIAS_DECL,           AliasDeclaration.class);
    classes.put(CeylonParser.TYPE_NAME,            TypeName.class);
    classes.put(CeylonParser.TYPE_PARAMETER_LIST,  TypeParameterList.class);
    classes.put(CeylonParser.TYPE_PARAMETER,       TypeParameter.class);
    classes.put(CeylonParser.MEMBER_DECL,          MemberDeclaration.class);
    classes.put(CeylonParser.MEMBER_NAME,          MemberName.class);
    classes.put(CeylonParser.MEMBER_TYPE,          MemberType.class);

    // XXX possibly rubbish node subclasses    
    
    classes.put(CeylonParser.LANG_ANNOTATION,      Annotation.class);
    classes.put(CeylonParser.USER_ANNOTATION,      Annotation.class);
    classes.put(CeylonParser.ANNOTATION_LIST,      AnnotationList.class);
    classes.put(CeylonParser.ANNOTATION_NAME,      AnnotationName.class);
    classes.put(CeylonParser.ABSTRACT,             Abstract.class);
    classes.put(CeylonParser.ARG_LIST,             ArgumentList.class);
    classes.put(CeylonParser.CALL_EXPR,            CallExpression.class);
    classes.put(CeylonParser.DOT,                  Dot.class);
    classes.put(CeylonParser.EXPR,                 Expression.class);
    classes.put(CeylonParser.LIDENTIFIER,          LIdentifier.class);
    classes.put(CeylonParser.PUBLIC,               Public.class);
    classes.put(CeylonParser.SATISFIES_LIST,       SatisfiesList.class);
    classes.put(CeylonParser.STMT_LIST,            StatementList.class);
    classes.put(CeylonParser.SIMPLESTRINGLITERAL,  SimpleStringLiteral.class);
    classes.put(CeylonParser.STRING_CST,           StringConstant.class);
    classes.put(CeylonParser.SUBTYPE,              SubType.class);
    classes.put(CeylonParser.TYPE,                 Type.class);
    classes.put(CeylonParser.TYPE_ARG_LIST,        TypeArgumentList.class);
    classes.put(CeylonParser.TYPE_CONSTRAINT,      TypeConstraint.class);
    classes.put(CeylonParser.TYPE_CONSTRAINT_LIST, TypeConstraintList.class);
    classes.put(CeylonParser.UIDENTIFIER,          UIdentifier.class);
  }

  /**
   * This node's parent and children.
   */
  public CeylonTree parent;
  public List<CeylonTree> children;

  /**
   * The ANTLR token from which this node was constructed.
   */
  public Token token;

  /**
   * Base class for visitors.
   */
  public static class Visitor {
    public void visit(CompilationUnit that)      { visitDefault(that); }
    public void visit(ImportList that)           { visitDefault(that); }
    public void visit(ImportDeclaration that)    { visitDefault(that); }
    public void visit(ImportPath that)           { visitDefault(that); }
    public void visit(ImportWildcard that)       { visitDefault(that); }
    public void visit(TypeDeclaration that)      { visitDefault(that); }
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
   * A type declaration.
   */
  public static class TypeDeclaration extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }
  
  /**
   * A class declaration.
   */
  public static class ClassDeclaration extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A interface declaration.
   */
  public static class InterfaceDeclaration extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * An alias declaration.
   */
  public static class AliasDeclaration extends CeylonTree {
    public void accept(Visitor v) { v.visit(this); }
  }

  /**
   * A type name.
   */
  public static class TypeName extends CeylonTree {
	  
    public String name;
	  	  
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
