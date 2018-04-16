/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.analyzer;

import static java.lang.Character.isWhitespace;
import static java.lang.Character.toChars;
import static java.lang.Integer.parseInt;
import static org.eclipse.ceylon.compiler.typechecker.parser.CeylonLexer.ASTRING_LITERAL;
import static org.eclipse.ceylon.compiler.typechecker.parser.CeylonLexer.AVERBATIM_STRING;
import static org.eclipse.ceylon.compiler.typechecker.parser.CeylonLexer.STRING_END;
import static org.eclipse.ceylon.compiler.typechecker.parser.CeylonLexer.STRING_MID;
import static org.eclipse.ceylon.compiler.typechecker.parser.CeylonLexer.STRING_START;
import static org.eclipse.ceylon.compiler.typechecker.parser.CeylonLexer.VERBATIM_STRING;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.eclipse.ceylon.compiler.typechecker.tree.CustomTree;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Validate and interpret the syntax of string and numeric
 * literals.
 * 
 * @author Gavin King
 *
 */
public class LiteralVisitor extends Visitor {

    private static final String GENERATED_PREFIX = "$pattern$param$";
    
    private int indent;
    
    static final Pattern DOC_LINK_PATTERN = 
            Pattern.compile("\\[\\[(([^\"`|\\[\\]]*\\|)?((module )|(package )|(class )|(interface )|(function )|(value )|(alias ))?(((\\w|\\.)+)::)?(\\w*)(\\.(\\w*))*(\\(\\))?)\\]\\]");
    private static Pattern CHARACTER_ESCAPE_PATTERN = 
            Pattern.compile("\\\\(\\{#([^}]*)\\}|\\{([^#]([^}]*))\\}|(.?))");
    
    
    @Override
    public void visit(Tree.CompilationUnit that) {
        if (!that.getLiteralsProcessed()) {
            super.visit(that);
            that.setLiteralsProcessed(true);
        }
    }
    
    @Override
    public void visit(Tree.StringLiteral that) {
        if (that.getToken()==null) return;
        int type = that.getToken().getType();
        String text = that.getText();
        
        if (type==AVERBATIM_STRING || type==ASTRING_LITERAL) {
            Matcher m = DOC_LINK_PATTERN.matcher(text);
            while (m.find()) {
                String group = m.group(1);
                int start = that.getStartIndex()+m.start(1);
                int end = that.getStartIndex()+m.end(1);
                String[] linesUpTo = 
                        text.substring(0, m.start(1))
                            .split("\n");
                CommonToken token = 
                        new CommonToken(ASTRING_LITERAL, group);
                token.setStartIndex(start);
                token.setStopIndex(end-1);
                token.setTokenIndex(that.getToken().getTokenIndex());
                int line = 
                        that.getToken().getLine() +
                        linesUpTo.length-1;
                int charInLine = 
                        linesUpTo.length==0 ? 0 : 
                            linesUpTo[linesUpTo.length-1].length();
                if (linesUpTo.length==1) {
                    charInLine +=
                            that.getToken().getCharPositionInLine();
                }
                token.setLine(line);
                token.setCharPositionInLine(charInLine);
                that.addDocLink(new Tree.DocLink(token));
            }
        }
        
        if (type!=STRING_MID && 
            type!=STRING_END) {
            indent = getIndentPosition(that);
        }
        if (type==VERBATIM_STRING || 
            type==AVERBATIM_STRING) {
            text = text.substring(3,
                    text.length()-(text.endsWith("\"\"\"")?3:0));
        }
        else if (type==STRING_MID) {
            text = text.substring(
                    text.startsWith("``") ? 2 : 1,
                    text.length()-2);
        }
        else if (type==STRING_END) {
            text = text.substring(
                    text.startsWith("``") ? 2 : 1, 
                    text.length()-(text.endsWith("\"")?1:0));
        }
        else if (type==STRING_START) {
            text = text.substring(1, 
                    text.length()-2);
        }
        else {
            text = text.substring(1, 
                    text.length()-(text.endsWith("\"")?1:0));
        }
        StringBuilder result = new StringBuilder();
        boolean allTrimmed = 
                stripIndent(text, indent, result);
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
    }

    @Override
    public void visit(Tree.StringTemplate that) {
        int oi = indent;
        indent = 0;
        super.visit(that);
        indent = oi;
    }
    
    @Override
    public void visit(Tree.QuotedLiteral that) {
        StringBuilder result = new StringBuilder();
        stripIndent(that.getText(), 
                getIndentPosition(that), 
                result);
        //interpolateEscapes(result, that);
        that.setText(result.toString());
    }
    
    private int getIndentPosition(Tree.Literal that) {
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
    public void visit(Tree.CharLiteral that) {
        StringBuilder result = 
                new StringBuilder(that.getText());
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
        
    private static boolean stripIndent(final String text, 
            final int indentation, final StringBuilder result) {
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
                    }
                    else {
                        result.append(line.substring(indentation));
                        break;
                    }
                }
            }
            result.append("\n");
        }
        if (result.length()>0) {
            result.setLength(result.length()-1);
        }
        return correctlyIndented;
    }
    
    private static void interpolateEscapes(StringBuilder result, 
            Node node) {
        Matcher matcher;
        int start=0;
        while ((matcher = 
                CHARACTER_ESCAPE_PATTERN.matcher(result))
                                        .find(start)) {
            String hex = matcher.group(2);
            String name = matcher.group(3);
            int from = matcher.start();
            int to = matcher.end();
            int next = to;
            if (name!=null) {
                boolean found=false;
                for (int codePoint=0; 
                        codePoint<=0xE01EF; 
                        codePoint++) {
                    String cn = Character.getName(codePoint);
                    if (cn!=null && cn.equals(name)) {
                        String unicodeChar = 
                                new String(toChars(codePoint));
                        result.replace(from, to, unicodeChar);
                        next = from+unicodeChar.length();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    String emoji = getEmoji(node, name);
                    if (emoji!=null) {
                        result.replace(from, to, emoji);
                        next = from+emoji.length();
                    }
                }
            }
            else if (hex!=null) {
                if (hex.length()!=2 && 
                    hex.length()!=4 &&
                    hex.length()!=6 && 
                    hex.length()!=8) { //tolerate 8 digits for backward compatibility only!
                    node.addError("illegal unicode escape sequence: must consist of 2, 4, or 6 digits");
                }
                else {
                    String unicodeChar = 
                            getUnicodeCharacter(node, hex);
                    if (unicodeChar!=null) {
                        result.replace(from, to, unicodeChar);
                        next = from+unicodeChar.length();
                    }
                }
            }
            else {
                String legacyEscape = matcher.group(5);
                if (legacyEscape.isEmpty()) {
                    result.delete(from, to+1);
                    next = from;
                }
                else {
                    String legacyEscapeChar = 
                            getLegacyEscape(node, legacyEscape);
                    if (legacyEscapeChar!=null) {
                        result.replace(from, to, legacyEscapeChar);
                        next = from+legacyEscapeChar.length();
                    }
                }
            }
            start = next;
        }
    }

    private static String getLegacyEscape(Node node, String legacyEscape) {
        char escape = legacyEscape.charAt(0);
        char ch;
        switch (escape) {
            case 'b': ch = '\b'; break;
            case 't': ch = '\t'; break;
            case 'n': ch = '\n'; break;
            case 'f': ch = '\f'; break;
            case 'r': ch = '\r'; break;
            case 'e': ch = 0x1b; break;
            case '0': ch = 0; break;
            case '$': ch = '$'; break;
            case '"':
            case '\'':
            case '`':
            case '\\':
                ch = escape; break;
            default:
                node.addError("illegal escape sequence: '\\" + escape + 
                        "' is not a recognized escape sequence");
                return null;
        }
        return Character.toString(ch);
    }

    private static String getUnicodeCharacter(Node node, String hex) {
        int codePoint;
        try {
            codePoint = parseInt(hex, 16);
        }
        catch (NumberFormatException nfe) {
            node.addError("illegal unicode escape sequence: '" + 
                    hex + "' is not a hexadecimal number");
            return null;
        }
        char[] chars;
        try {
            chars = toChars(codePoint);
        }
        catch (IllegalArgumentException iae) {
            node.addError("illegal unicode escape sequence: '" + 
                    hex + "' is not a valid Unicode code point");
            return null;
        }
        return new String(chars);
    }

    private static String getEmoji(Node node, String name) {
        int emoji = -1; 
        switch (name) {
        case ":)":
        case ":-)":
        case "=)":
            emoji = 0x1f603; break;
        case "O:)":
        case "O:-)":
        case "O=)":
            emoji = 0x1f607; break;
        case "}:)":
        case "}:-)":
        case "}=)":
            emoji = 0x1f608; break;
        case ":-(":
        case ":(":
        case "=(":
            emoji = 0x1f61e; break;
        case ":-|":
        case ":|":
        case "=|":
            emoji = 0x1f610; break;
        case ";-)":
        case ";)":
            emoji = 0x1f609; break;
        case "B-)":
        case "B)":
            emoji = 0x1f60e; break;
        case ":-D":
        case ":D":
            emoji = 0x1f600; break;
        case "=D":
            emoji = 0x1f604; break;
        case "-_-":
            emoji = 0x1f611; break;
        case "o_o":
            emoji = 0x1f613; break;
        case "u_u":
            emoji = 0x1f614; break;
        case ">_<":
            emoji = 0x1f623; break;
        case "^_^":
            emoji = 0x1f601; break;
        case "^_^;;":
            emoji = 0x1f605; break;
        case "<3":
            emoji = 0x1f49c; break;
        case "<\\3":
        case "</3":
            emoji = 0x1f494; break;
        case "~@~":
            emoji = 0x1f4a9; break;
        case "(]:{":
            emoji = 0x1f473; break;
        case "-<@%":
            emoji = 0x1f41d; break;
        case ":(|)":
            emoji = 0x1f435; break;
        case ":(:)":
            emoji = 0x1f437; break;
        case ":*":
        case ":-*":
            emoji = 0x1f617; break;
        case ";*":
        case ";-*":
            emoji = 0x1f618; break;
        case ":\\":
        case ":-\\":
        case "=\\":
        case ":/":
        case ":-/":
        case "=/":
            emoji = 0x1f615; break;
        case ":S":
        case ":-S":
        case ":s":
        case ":-s":
            emoji = 0x1f616; break;
        case ":P":
        case ":-P":
        case "=P":
        case ":p":
        case ":-p":
        case "=p":
            emoji = 0x1f61b; break;
        case ";P":
        case ";-P":
        case ";p":
        case ";-p":
            emoji = 0x1f61c; break;
        case ">.<":
        case ">:(":
        case ">:-(":
        case ">=(":
            emoji = 0x1f621; break;
        case "T_T":
        case ":'(":
        case ";_;":
        case "='(":
            emoji = 0x1f622; break;
        case "D:":
            emoji = 0x1f626; break;
        case "o.o":
        case ":o":
        case ":-o":
        case "=o":
            emoji = 0x1f62e; break;
        case "O.O":
        case ":O":
        case ":-O":
        case "=O":
            emoji = 0x1f632; break;
        case "x_x":
        case "X-O":
        case "x-o":
        case "X(":
        case "X-(":
            emoji = 0x1f635; break;
        case ":X)":
        case ":3":
        case "(=^..^=)":
        case "(=^.^=)":
        case "=^_^=":
            emoji = 0x1f638; break;
        default:
            node.addError("illegal unicode escape sequence: " + 
                    name + " is not a Unicode character name");
        }
        if (emoji<0) {
            return null;
        }
        else {
            return new String(Character.toChars(emoji));
        }
    }
    
    @Override
    public void visit(Tree.Identifier that) {
        super.visit(that);
        if (!that.isMissingToken()) {
            String text = that.getText();
            if (text.startsWith("\\")) {
                text = text.substring(2);
                that.setText(text);
            }
            if (text.startsWith(GENERATED_PREFIX)) {
                return;
            }
            int index = 0;
            while (index<text.length()) {
                int cp = text.codePointAt(index);
                index += Character.charCount(cp);
                int type = Character.getType(cp);
                boolean num = 
                        type==Character.LETTER_NUMBER ||
                        type==Character.DECIMAL_DIGIT_NUMBER ||
                        type==Character.OTHER_NUMBER;
                boolean letter = 
                        type==Character.LOWERCASE_LETTER ||
                        type==Character.UPPERCASE_LETTER ||
                        type==Character.TITLECASE_LETTER ||
                        type==Character.OTHER_LETTER||
                        type==Character.MODIFIER_LETTER;
                boolean us = cp=='_';
                if (index==0 && num) {
                    that.addError("identifier may not begin with a digit");
                    break;
                }
                else if (!num && !letter && !us) {
                    that.addError("identifier must be composed of letters, digits, and underscores");
                    break;
                }
            }
        }
    }
    
    Tree.Type asType(Tree.Pattern pattern) {
        if (pattern instanceof Tree.VariablePattern) {
            Tree.VariablePattern vp = 
                    (Tree.VariablePattern) pattern;
            Tree.Variable variable = vp.getVariable();
            Tree.Type type = variable.getType();
            if (!(type instanceof Tree.StaticType || 
                  type instanceof Tree.SequencedType)) {
                return null;
            }
            return type;
        }
        else if (pattern instanceof Tree.KeyValuePattern) {
            Tree.KeyValuePattern ep = 
                    (Tree.KeyValuePattern) pattern;
            Tree.Type keyType = asType(ep.getKey());
            Tree.Type valueType = asType(ep.getValue());
            if (!(keyType instanceof Tree.StaticType) ||
                !(valueType instanceof Tree.StaticType)) {
                return null;
            }
            Tree.EntryType et = new Tree.EntryType(null);
            et.setKeyType((Tree.StaticType) keyType);
            et.setValueType((Tree.StaticType) valueType);
            return et;
        }
        else if (pattern instanceof Tree.TuplePattern) {
            Tree.TuplePattern tp = 
                    (Tree.TuplePattern) pattern;
            Tree.TupleType tt = new Tree.TupleType(null);
            for (Tree.Pattern p: tp.getPatterns()) {
                Tree.Type type = asType(p);
                if (!(type instanceof Tree.StaticType || 
                      type instanceof Tree.SequencedType)) {
                    return null;
                }
                tt.getElementTypes().add(type);
            }
            return tt;
        }
        return null;
    }
    
    private Tree.Identifier switchId; 
    
    @Override
    public void visit(Tree.SwitchExpression that) {
        Tree.Identifier oid = switchId;
        createSwitchVariable(that.getSwitchClause(), 
                that.getSwitchCaseList());
        super.visit(that);
        switchId = oid;
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        Tree.Identifier oid = switchId;
        createSwitchVariable(that.getSwitchClause(), 
                that.getSwitchCaseList());
        super.visit(that);
        switchId = oid;
    }

    private void createSwitchVariable(
            Tree.SwitchClause switchClause, 
            Tree.SwitchCaseList switchCaseList) {
        switchId = null;
        if (switchClause!=null && switchCaseList!=null) {
            Tree.Switched switched = 
                    switchClause.getSwitched();
            if (switched!=null) {
                Tree.Variable variable = 
                        switched.getVariable();
                Tree.Expression expression = 
                        switched.getExpression();
                if (variable != null) {
                    switchId = variable.getIdentifier();
                }
                else if (expression != null) {
                    Tree.Term term = expression.getTerm();
                    if (term instanceof Tree.BaseMemberExpression) {
                        Tree.BaseMemberExpression bme = 
                                (Tree.BaseMemberExpression) 
                                    term;
                        switchId = bme.getIdentifier();
                    }
                    else {
                        for (Tree.CaseClause cc: 
                                switchCaseList.getCaseClauses()) {
                            Tree.CaseItem item = cc.getCaseItem();
                            if (item instanceof Tree.IsCase) {
                                return;
                            }
                            if (item instanceof Tree.PatternCase) {
                                Tree.Identifier id = 
                                        new Tree.Identifier(null);
                                id.setText("_");
                                switchId = id;
                                switched.setVariable(
                                        createVariable(id, expression));
                                switched.setExpression(null);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.CaseClause that) {
        Tree.CaseItem item = that.getCaseItem();
        if (item instanceof Tree.PatternCase) {
            Tree.PatternCase pc = (Tree.PatternCase) item;
            Tree.Pattern pattern = pc.getPattern();
            Tree.Type type = asType(pattern);
            if (type==null) {
                //TODO: better to construct a partial type!
                type = new Tree.ValueModifier(null);
                pattern.addError("missing type in pattern case: (pattern case must specify explicit types for every variable)");
            }
            Tree.Identifier id = new Tree.Identifier(null);
            id.setText(switchId==null ? "_" : switchId.getText());
            that.setCaseItem(createIsCase(type, id, pc));
            Tree.Destructure destructure = 
                    destructure(pattern, id, true);
            Tree.Expression e = that.getExpression();
            Tree.Block b = that.getBlock();
            if (e!=null) {
                Tree.LetClause letClause = 
                        new Tree.LetClause(null);
                letClause.getVariables()
                    .add(destructure);
                letClause.setExpression(e);
                that.setExpression(createLetExpression(letClause));
            }
            if (b!=null) {
                b.getStatements().add(0, destructure);
            }
        }
        super.visit(that);
    }

    private Tree.IsCase createIsCase(Tree.Type type, 
            Tree.Identifier id, Tree.PatternCase item) {
        Tree.IsCase ic = new CustomTree.IsCase(item.getToken());
        ic.setEndToken(item.getEndToken());
        ic.setType(type);
        ic.setVariable(createVariable(id));
        return ic;
    }

    private Tree.Variable createVariable(Tree.Identifier id, 
            Tree.Expression e) {
        Tree.SpecifierExpression se = 
                new Tree.SpecifierExpression(null);
        se.setExpression(e);
        Tree.Variable var = new Tree.Variable(null);
        var.setType(new Tree.SyntheticVariable(null));
        var.setIdentifier(id);
        var.setSpecifierExpression(se);
        return var;
    }
    
    private Tree.Variable createVariable(Tree.Identifier id) {
        Tree.Variable var = new Tree.Variable(null);
        var.setType(new Tree.SyntheticVariable(null));
        var.setIdentifier(id);
        var.setSpecifierExpression(createReference(id));
        return var;
    }
    
    @Override
    public void visit(Tree.FunctionArgument that) {
        //desugar pattern parameters in anon functions
        List<Tree.ParameterList> parameterLists = 
                that.getParameterLists();
        final Tree.Block funBody = that.getBlock();
        final Tree.ParExpression funExpression =
                new Tree.ParExpression(null);
        funExpression.setTerm(that.getExpression());
        final Tree.LetClause letClause = 
                new Tree.LetClause(null);
        int k = 0;
        for (int i=0; i<parameterLists.size(); i++) {
            Tree.ParameterList list = parameterLists.get(i);
            List<Tree.Parameter> parameters = 
                    list.getParameters();
            for (int j=0; j<parameters.size(); j++) {
                Tree.Parameter p = parameters.get(j);
                if (p instanceof Tree.PatternParameter) {
                    Tree.PatternParameter pp = 
                            (Tree.PatternParameter) p;
                    Tree.Pattern pattern = pp.getPattern();
                    Tree.Identifier id = 
                            new Tree.Identifier(null);
                    id.setText(GENERATED_PREFIX + k);
                    Tree.Parameter param = 
                            createParameter(id, 
                                    asType(pattern));
                    parameters.set(j, param);
                    Tree.Destructure destructure = 
                            destructure(pattern, id, false);
                    if (funBody==null) {
                        letClause.getVariables()
                            .add(destructure);
                    }
                    else {
                        funBody.getStatements()
                            .add(k, destructure);
                    }
                    k++;
                }
            }
        }
        if (k>0 && funBody==null) {
            letClause.setExpression(funExpression);
            that.setExpression(createLetExpression(letClause));
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.PatternParameter that) {
        super.visit(that);
        that.addError("parameter may not be a pattern (parameter destructuring is allowed for anonymous functions)");
    }

    private Tree.Expression createLetExpression(Tree.LetClause letClause) {
        Tree.LetExpression let = 
                new Tree.LetExpression(null);
        let.setLetClause(letClause);
        Tree.Expression expression = 
                new Tree.Expression(null);
        expression.setTerm(let);
        return expression;
    }

    private Tree.Parameter createParameter(Tree.Identifier id, Tree.Type type) {
        if (type!=null) {
            //we have enough explicit type
            //information in the pattern to set
            //up a static type for the parameter
            Tree.AttributeDeclaration model = 
                    new Tree.AttributeDeclaration(null);
            model.setIdentifier(id);
            model.setType(type);
            Tree.AnnotationList al =
                    new Tree.AnnotationList(null);
            model.setAnnotationList(al);
            Tree.ValueParameterDeclaration vpd =
                    new Tree.ValueParameterDeclaration(null);
            vpd.setTypedDeclaration(model);
            return vpd;
        }
        else {
            //its type is going to be inferred
            Tree.InitializerParameter ip =
                    new Tree.InitializerParameter(null);
            ip.setIdentifier(id);
            return ip;
        }
    }

    private Tree.Destructure destructure(Tree.Pattern pattern, 
            Tree.Identifier id, boolean patternCase) {
        Tree.Destructure destructure = 
                new Tree.Destructure(null);
        destructure.setSpecifierExpression(createReference(id));
        destructure.setPattern(pattern);
        destructure.setPatternCase(patternCase);
        return destructure;
    }

    private Tree.SpecifierExpression createReference(Tree.Identifier id) {
        Tree.BaseMemberExpression bme = 
                new Tree.BaseMemberExpression(null);
        bme.setIdentifier(id);
        Tree.InferredTypeArguments typeArgs = 
                new Tree.InferredTypeArguments(null);
        bme.setTypeArguments(typeArgs);
        Tree.Expression destExpression = 
                new Tree.Expression(null);
        destExpression.setTerm(bme);
        Tree.SpecifierExpression spec = 
                new Tree.SpecifierExpression(null);
        spec.setExpression(destExpression);
        return spec;
    }
    
    //TODO: remove this transformation when the backends are fixed
    
    @Override
    public void visit(Tree.Body that) {
        super.visit(that);
        List<Tree.Statement> statements = that.getStatements();
        for (int i=0; i<statements.size(); i++) {
            Tree.Statement bs = statements.get(i);
            if (bs instanceof Tree.LetStatement) {
                Tree.LetStatement ls =
                        (Tree.LetStatement) bs;
                List<Tree.Statement> variables = ls.getVariables();
                for (int j=0; j<variables.size(); j++) {
                    Tree.Statement s = variables.get(j);
                    if (s instanceof Tree.Variable) {
                        while (j<variables.size()) {
                            Tree.Statement rs = 
                                    eliminateVariable(variables.remove(j));
                            statements.add(++i, rs);
                        }
                        break;
                    }
                }
            }
        }
    }
    
    private Tree.Statement eliminateVariable(Tree.Statement s) {
        if (s instanceof Tree.Variable) {
            Tree.Variable v = (Tree.Variable) s;
            Tree.AttributeDeclaration ad = 
                    new Tree.AttributeDeclaration(
                            v.getMainToken());
            Tree.AnnotationList al = 
                    v.getAnnotationList();
            if (al==null) al = 
                    new Tree.AnnotationList(null);
            ad.setAnnotationList(al);
            ad.setType(v.getType());
            ad.setIdentifier(v.getIdentifier());
            ad.setSpecifierOrInitializerExpression(
                    v.getSpecifierExpression());
            return ad;
        }
        else {
            Tree.LetStatement ls = 
                    new Tree.LetStatement(null);
            ls.addVariable(s);
            return ls;
        }
    }
    
}
