package com.redhat.ceylon.compiler.typechecker.analyzer;

import static java.lang.Character.toChars;
import static java.lang.Integer.parseInt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.Token;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CharLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FloatLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Literal;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NaturalLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QuotedLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class LiteralVisitor extends Visitor {
	
    @Override
    public void visit(StringLiteral that) {
        StringBuilder result = new StringBuilder();
        stripIndent(that.getText(), getIndentPosition(that), result);
        interpolateEscapes(result, that);
        that.setText(result.toString());
    }

    @Override
    public void visit(QuotedLiteral that) {
        StringBuilder result = new StringBuilder();
        stripIndent(that.getText(), getIndentPosition(that), result);
        //interpolateEscapes(result, that);
        that.setText(result.toString());
    }
    
	private int getIndentPosition(Literal that) {
		Token token = that.getToken();
		return token==null ? 0 : token.getCharPositionInLine()+1;
	}
    
    @Override
    public void visit(CharLiteral that) {
        StringBuilder result = new StringBuilder(that.getText());
        interpolateEscapes(result, that);
        that.setText(result.toString());
    }
    
    @Override
    public void visit(FloatLiteral that) {
    	that.setText(that.getText()
    			.replace("_", "")
                .replace("k", "e+3")
                .replace("M", "e+6")
                .replace("G", "e+9")
                .replace("T", "e+12")
                .replace("P", "e+15")
                .replace("m", "e-3")
                .replace("u", "e-6")
                .replace("n", "e-9")
                .replace("p", "e-12")
                .replace("f", "e-15"));
    }
    
    @Override
    public void visit(NaturalLiteral that) {
    	that.setText(that.getText()
    			.replace("_", "")
                .replace("k", "000")
                .replace("M", "000000")
                .replace("G", "000000000")
                .replace("T", "000000000000")
                .replace("P", "000000000000000"));
    }
    
    private static void stripIndent(final String text, final int start, 
            final StringBuilder result) {
        int num = 0;
        for (String line: text.split("\n|\r\n?")) {
            if (num++==0 || line.length()<start) {
                result.append(line);
            }
            else {
                boolean trimIndent = true;
                for (int i=0; i<start; i++) {
                    if (line.charAt(i)!=' ') {
                        trimIndent = false;
                        break;
                    }
                }
                if (trimIndent) {
                    result.append(line.substring(start));
                }
                else {
                    result.append(line);
                }
            }
            result.append("\n");
        }
        result.setLength(result.length()-1);
    }
    
    private static Pattern re = Pattern.compile("\\\\(\\{#([^}]*)\\}|(.))");
    
    private static void interpolateEscapes(final StringBuilder result, Node node) {
        Matcher m;
        int start=0;
        while ((m = re.matcher(result)).find(start)) {
            String hex = m.group(2);
            if (hex!=null) {
                if (hex.length()!=2 && hex.length()!=4 && hex.length()!=8) {
                    node.addError("illegal unicode escape sequence: must consist of 2, 4 or 8 digits");
                }
                else {
                    int codePoint=0;
                    try {
                        codePoint = parseInt(hex, 16);
                    }
                    catch (NumberFormatException nfe) {
                        node.addError("illegal unicode escape sequence: '" + 
                                hex + "' is not a hexadecimal number");
                    }
                    result.replace(m.start(), m.end(), new String(toChars(codePoint)));
                }
            }
            else {
                char escape = m.group(3).charAt(0);
                char ch;
                switch (escape) {
                    case 'b': ch = '\b'; break;
                    case 't': ch = '\t'; break;
                    case 'n': ch = '\n'; break;
                    case 'f': ch = '\f'; break;
                    case 'r': ch = '\r'; break;
                    case '"':
                    case '\'':
                    case '`':
                    case '\\':
                    	ch = escape; break;
                    default:
                    	node.addError("illegal escape sequence: \\" + escape);
                    	ch='?';
                }
                result.replace(m.start(), m.end(), Character.toString(ch));
            }
            start = m.start()+1;
        }
    }
    
}
