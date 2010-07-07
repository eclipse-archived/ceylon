public class CeylonTreeVisitor {
  public void visit(CeylonTree.Annotation that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.AnnotationList that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.AnnotationName that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.ArgumentList that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.CallExpression that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.ClassDeclaration that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.CompilationUnit that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.Dot that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.Expression that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.LIdentifier that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.MemberName that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.StatementList that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.SimpleStringLiteral that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.StringConstant that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.TypeName that) {
    visitDefault(that);
  }

  public void visit(CeylonTree.UIdentifier that) {
    visitDefault(that);
  }

  public void visitDefault(CeylonTree tree) {
    throw new RuntimeException();
  }
}
