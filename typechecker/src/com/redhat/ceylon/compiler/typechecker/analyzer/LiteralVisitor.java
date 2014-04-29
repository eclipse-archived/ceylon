package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer.ASTRING_LITERAL;
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

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CharLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Literal;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QuotedLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringTemplate;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class LiteralVisitor extends Visitor {

    private int indent;
    static final Pattern DOC_LINK_PATTERN = Pattern.compile("\\[\\[(([^\"`|\\[\\]]*\\|)?((module )|(package )|(class )|(interface )|(function )|(value )|(alias ))?(((\\w|\\.)+)::)?(\\w*)(\\.(\\w*))*)\\]\\]");
    private static Pattern CHARACTER_ESCAPE_PATTERN = Pattern.compile("\\\\(\\{#([^}]*)\\}|\\{([^}^#]*)\\}|(.))");
    
    
    @Override
    public void visit(CompilationUnit that) {
        if (!that.getLiteralsProcessed()) {
            super.visit(that);
            that.setLiteralsProcessed(true);
        }
    }
    
    @Override
    public void visit(StringLiteral that) {
        int type = that.getToken().getType();
        String text = that.getText();
        if (type!=STRING_MID && 
            type!=STRING_END) {
            indent = getIndentPosition(that);
        }
        if (type==VERBATIM_STRING || 
            type==AVERBATIM_STRING) {
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
            that.addError("multiline string content should align with start of string: string begins at character position " + indent, 6000);
        }
        if (type!=VERBATIM_STRING && 
            type!=AVERBATIM_STRING) {
            interpolateEscapes(result, that);
        }
        that.setText(result.toString());
        if (type!=STRING_MID && 
            type!=STRING_START) {
            indent = 0;
        }
        
        if (type==AVERBATIM_STRING || type==ASTRING_LITERAL) {
            Matcher m = DOC_LINK_PATTERN.matcher(that.getText());
            while (m.find()) {
                String group = m.group(1);
                int start = that.getStartIndex()+m.start(1);
                int end = that.getStartIndex()+m.end(1);
                CommonToken token = new CommonToken(ASTRING_LITERAL, group);
                token.setStartIndex(start);
                token.setStopIndex(end-1);
                token.setTokenIndex(that.getToken().getTokenIndex());
                that.addDocLink(new Tree.DocLink(token));
            }
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
    
    static final String digits = "\\d+";
    static final String groups = "\\d{1,3}(_\\d{3})+";
    static final String fractionalGroups = "(\\d{3}_)+\\d{1,3}";
    static final String magnitude = "k|M|G|T|P";
    static final String fractionalMagnitude = "m|u|n|p|f";
    static final String exponent = "(e|E)(\\+|-)?" + digits;

    static final String hexDigits = "(\\d|[a-f]|[A-F])+";
    static final String hexGroups = "(\\d|[a-f]|[A-F]){1,4}(_(\\d|[a-f]|[A-F]){4})+|(\\d|[a-f]|[A-F]){1,2}(_(\\d|[a-f]|[A-F]){2})+";
    
    static final String binDigits = "(0|1)+";
    static final String binGroups = "(0|1){1,4}(_(0|1){4})+";
    
    @Override
    public void visit(Tree.NaturalLiteral that) {
        super.visit(that);
        String text = that.getToken().getText();
        if (!text.matches("^(" + digits + "|" + groups + ")(" + magnitude + ")?$") &&
            !text.matches("#(" + hexDigits + "|" + hexGroups + ")") &&
            !text.matches("\\$(" + binDigits + "|" + binGroups + ")")) {
            that.addError("illegal integer literal format");
        }        
        that.setText(that.getText()
                .replace("_", "")
                .replace("k", "000")
                .replace("M", "000000")
                .replace("G", "000000000")
                .replace("T", "000000000000")
                .replace("P", "000000000000000"));
    }
    
    @Override
    public void visit(Tree.FloatLiteral that) {
        super.visit(that);
        String text = that.getToken().getText();
        if (!text.matches("^(" + digits + "|" + groups + ")(\\.(" + 
                digits + "|" + fractionalGroups  + ")(" + 
                magnitude + "|" + fractionalMagnitude + "|" + exponent + ")?|" +
                fractionalMagnitude + ")$")) {
            that.addError("illegal floating literal format");
        }
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
        
    private static boolean stripIndent(final String text, final int indentation, 
            final StringBuilder result) {
        boolean correctlyIndented = true;
        int num = 0;
        for (String line: text.split("\n|\r\n?")) {
            if (num++==0) {
                result.append(line);
            }
            else {
                for (int i = 0; i < line.length(); i++) {
                    if (i < indentation) {
                        if (!isWhitespace(line.charAt(i))) {
                            correctlyIndented = false;
                            result.append(line.substring(i));
                            break;
                        }
                    } else {
                        result.append(line.substring(indentation));
                        break;
                    }
                }
            }
            result.append("\n");
        }
        result.setLength(result.length()-1);
        return correctlyIndented;
    }
    
    private static void interpolateEscapes(final StringBuilder result, Node node) {
        Matcher m;
        int start=0;
        while ((m = CHARACTER_ESCAPE_PATTERN.matcher(result)).find(start)) {
            String hex = m.group(2);
            String name = m.group(3);
            if (name!=null) {
                boolean found=false;
                for (int codePoint=0; codePoint<=0xE01EF; codePoint++) {
                    String cn = Character.getName(codePoint);
                    if (cn!=null && cn.equals(name)) {
                        char[] chars = toChars(codePoint);
                        result.replace(m.start(), m.end(), new String(chars));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    if (name.equals(":-)")) {
                        result.replace(m.start(), m.end(), "\u263A");
                    }
                    else if (name.equals(":-(")) {
                        result.replace(m.start(), m.end(), "\u2639");
                    }
                    else if (name.equals("<3")) {
                        result.replace(m.start(), m.end(), "\u2665");
                    }
                    else {
                        node.addError("illegal unicode escape sequence: " + 
                                name + " is not a Unicode character");
                    }
                }
            }
            else if (hex!=null) {
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
                    char[] chars;
                    try {
                        chars = toChars(codePoint);
                    }
                    catch (IllegalArgumentException iae) {
                        node.addError("illegal unicode escape sequence: '" + 
                                hex + "' is not a valid Unicode code point");
                        continue;
                    }
                    result.replace(m.start(), m.end(), new String(chars));
                }
            }
            else {
                char escape = m.group(4).charAt(0);
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
