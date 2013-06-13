package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer.AVERBATIM_STRING;
import static com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer.STRING_END;
import static com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer.STRING_MID;
import static com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer.STRING_START;
import static com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer.VERBATIM_STRING;
import static java.lang.Character.isWhitespace;
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
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringTemplate;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class LiteralVisitor extends Visitor {
	
    private int indent;
    
    @Override
    public void visit(StringLiteral that) {
        int type = that.getToken().getType();
        String text = that.getText();
        if (type!=STRING_MID && type!=STRING_END) {
            indent = getIndentPosition(that);
        }
        if (type==VERBATIM_STRING || type==AVERBATIM_STRING) {
            text = text.substring(3,text.length()-(text.endsWith("\"\"\"")?3:0));
        }
        else if (type==STRING_MID) {
            text = text.substring(2, text.length()-2);
        }
        else if (type==STRING_END) {
            text = text.substring(2, text.length()-(text.endsWith("\"")?1:0));
        }
        else if (type==STRING_START) {
            text = text.substring(1, text.length()-2);
        }
        else {
            text = text.substring(1, text.length()-(text.endsWith("\"")?1:0));
        }
        StringBuilder result = new StringBuilder();
        boolean allTrimmed = stripIndent(text, indent, result);
        if (!allTrimmed) {
            that.addError("multiline string content should align with start of string: string begins at character position " + indent);
        }
        if (type!=VERBATIM_STRING && type!=AVERBATIM_STRING) {
            interpolateEscapes(result, that);
        }
        that.setText(result.toString());
        if (type!=STRING_MID && type!=STRING_START) {
            indent = 0;
        }
    }

    @Override
    public void visit(StringTemplate that) {
        int oi = indent;
        indent = 0;
        super.visit(that);
        indent = oi;
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
		return token==null ? 
		        0 : token.getCharPositionInLine() +
		                getQuoteLength(token);
	}

    private int getQuoteLength(Token token) {
        int type = token.getType();
        return type==VERBATIM_STRING || 
                type==AVERBATIM_STRING ? 
                        3 : 1;
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
    
    private static boolean stripIndent(final String text, final int start, 
            final StringBuilder result) {
        boolean correctlyIndented = true;
        int num = 0;
        for (String line: text.split("\n|\r\n?")) {
            if (num++==0) {
                result.append(line);
            }
            else {
                for (int i=0; i<line.length()&&i<start; i++) {
                    if (!isWhitespace(line.charAt(i))) {
                        correctlyIndented = false;
                        break;
                    }
                }
                if (line.length()>=start) {
                    result.append(line.substring(start));
                }
            }
            result.append("\n");
        }
        result.setLength(result.length()-1);
        return correctlyIndented;
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
