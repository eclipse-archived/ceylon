grammar Buildergen;

@parser::header { 
    package com.redhat.ceylon.compiler.typechecker.treegen; 
    import static com.redhat.ceylon.compiler.typechecker.treegen.Util.*; 
}
@lexer::header { 
    package com.redhat.ceylon.compiler.typechecker.treegen; 
}

nodeList : 
    {
    println("package com.redhat.ceylon.compiler.typechecker.tree;\n");
    println("import static com.redhat.ceylon.compiler.typechecker.tree.Tree.*;\n");
    println("import static com.redhat.ceylon.compiler.typechecker.parser.CeylonParser.*;\n");
    println("import org.antlr.runtime.tree.CommonTree;\n");
    println("import java.util.*;\n");
    println("public class Builder {\n");
    }
           (DESCRIPTION? node)+ 
           EOF
    { println("}"); }
           ;

node : '^' '('
       ( 'abstract' n=NODE_NAME
         { println("    public void build" + className($n.text) + "(CommonTree treeNode, " + className($n.text) + " node) {"); }
       | n=NODE_NAME 
         { println("    public " + className($n.text) + " build" + className($n.text) +"(CommonTree treeNode) {"); }
         { println("        " + className($n.text) + " node = new " + className($n.text) + "(treeNode);"); }
         { println("        build" + className($n.text) + "(treeNode, node);"); }
         { println("        return node;"); }
         { println("    }\n"); }
         { println("    public void build" + className($n.text) + "(CommonTree treeNode, " + className($n.text) + " node) {"); }
       )
       extendsNode?
       (
       { println("        @SuppressWarnings(\"unchecked\")"); }
       { println("        List<CommonTree> children = (List<CommonTree>) treeNode.getChildren();"); }
       { println("        if (children!=null) for (CommonTree childTreeNode: children) {"); }
       (DESCRIPTION? subnode)+
       { println("        }"); }
       )?
       (DESCRIPTION? field)*
       ')' 
       { println("    }\n"); }
     ;

extendsNode : ':' n=NODE_NAME 
              { println("        build" + className($n.text) + "(treeNode, node);"); }
            ;

subnode : n=NODE_NAME '?'? f=FIELD_NAME
          { println("            if (childTreeNode.getType()==" + $n.text + " && node.get" + initialUpper($f.text) + "()==null) {"); }
          { println("                node.set" + initialUpper($f.text) + "(build" + className($n.text) + "(childTreeNode));"); }
          { println("                continue;"); }
          { println("            }"); }
        | n=NODE_NAME '?'? g=FIELD_NAME
          { println("            if (node.get" + initialUpper($g.text) + "()==null) {"); }
          '(' (
          s=NODE_NAME 
          { println("                if (childTreeNode.getType()==" + $s.text + ") {"); }
          { println("                    node.set" + initialUpper($g.text) + "(build" + className($s.text) + "(childTreeNode));"); }
          { println("                    continue;"); }
          { println("                }"); }
          )+ ')'
          { println("            }"); }
         | n=NODE_NAME '?'?
          { println("            if (childTreeNode.getType()==" + $n.text + " && node.get" + className($n.text) + "()==null) {"); }
          { println("                node.set" + className($n.text) + "(build" + className($n.text) + "(childTreeNode));"); }
          { println("                continue;"); }
          { println("            }"); }
        | n=NODE_NAME '?'? 
          { println("            if (node.get" + className($n.text) + "()==null) {"); }
          '(' (
          s=NODE_NAME 
          { println("                if (childTreeNode.getType()==" + $s.text + ") {"); }
          { println("                    node.set" + className($n.text) + "(build" + className($s.text) + "(childTreeNode));"); }
          { println("                    continue;"); }
          { println("                }"); }
          )+ ')'
          { println("            }"); }
        | mn=NODE_NAME '*'
          { println("            if (childTreeNode.getType()==" + $mn.text + ") {"); }
          { println("                node.add" + className($mn.text) + "(build" + className($mn.text) + "(childTreeNode));"); }
          { println("                continue;"); }
          { println("            }"); }
        | mn=NODE_NAME '*' f=FIELD_NAME
          { println("            if (childTreeNode.getType()==" + $mn.text + ") {"); }
          { println("                node.add" + initialUpper($f.text) + "(build" + className($mn.text) + "(childTreeNode));"); }
          { println("                continue;"); }
          { println("            }"); }
        | mn=NODE_NAME '*'
          '(' (
          s=NODE_NAME
          { println("            if (childTreeNode.getType()==" + $s.text + ") {"); }
          { println("                node.add" + className($mn.text) + "(build" + className($s.text) + "(childTreeNode));"); }
          { println("                continue;"); }
          { println("            }"); }
          )+ ')' 
        ;

field : 'abstract'? t=TYPE_NAME f=FIELD_NAME ';'
      ;

NODE_NAME : ('A'..'Z'|'_')+;

FIELD_NAME : ('a'..'z') ('a'..'z'|'A'..'Z')*;
TYPE_NAME : ('A'..'Z') ('a'..'z'|'A'..'Z')*;

WS : (' ' | '\n' | '\t' | '\r' | '\u000C') { skip(); };

CARAT : '^';

LPAREN : '(';
RPAREN : ')';

MANY : '*'|'+';
OPTIONAL : '?';

EXTENDS : ':';

SEMI : ';';

DESCRIPTION : '\"' (~'\"')* '\"';
