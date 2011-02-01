grammar Treegen;

options { output=template; }

@members {
    String className(String nodeName) { 
        return toJavaIdentifier(nodeName, true); 
    }
    
    String fieldName(String nodeName) { 
        return toJavaIdentifier(nodeName, false); 
    }
    
    String toJavaIdentifier(String nodeName, boolean boundary) {
        StringBuilder result = new StringBuilder();
        for (char c: nodeName.toCharArray()) {
            if (c=='_') {
                boundary=true;
            }
            else if (boundary) {
                result.append(c);
                boundary=false;
            }
            else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
    
    
}

nodeList : node+ EOF;

node : '^' '(' 
       { System.out.print("public class "); }
       n=NODE_NAME 
       { System.out.print(className($n.text)); }
       extendsNode? 
       { System.out.println(" {"); }
       { System.out.println("    public static final String ANTLR_NODE_NAME = \"" + $n.text + "\""); }
       { System.out.println("    public static final int ANTLR_NODE_TYPE = CeylonParser." + $n.text + ";"); }
       { System.out.println("    public static final CommonTree treeNode;"); }
       { System.out.println("    public " + className($n.text) + "(CommonTree treeNode) {" ); }
       { System.out.println("        this.treeNode = treeNode;" ); }
       { System.out.println("    }" ); }
       subnode*
       field*
       ')' 
       { System.out.println("}\n"); }
     ;

extendsNode : ':' n=NODE_NAME 
              { System.out.print(" extends " + className($n.text)); }
            ;

subnode : n=NODE_NAME 
          { System.out.println("    private " + className($n.text) + " " + fieldName($n.text) + ";"); }
          { System.out.println("    public " + className($n.text) + " get" + className($n.text) + "() { return " + fieldName($n.text) + "; }"); }
        | on=NODE_NAME OPTIONAL 
          { System.out.println("    private " + className($on.text) + " " + fieldName($on.text) + ";"); }
          { System.out.println("    public " + className($on.text) + " get" + className($on.text) + "() { return " + fieldName($on.text) + "; }"); }
        | mn=NODE_NAME MANY 
          { System.out.println("    private List<" + className($mn.text) + "> " + fieldName($mn.text) + 
                               " = new ArrayList<" + className($mn.text) + ">();"); }
          { System.out.println("    public  List<" + className($mn.text) + "> get" + className($mn.text) + "() { return " + fieldName($mn.text) + "; }"); }
        ;

field : t=TYPE_NAME f=FIELD_NAME 
          { System.out.println("    private " + $t.text + " " + $f.text+ ";"); }
          { System.out.println("    public " + $t.text + " get" + $f.text + "() { return " + $f.text + "; }"); }
        ';'
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