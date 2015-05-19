package com.redhat.ceylon.compiler.typechecker.parser;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.RecognitionException;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.tree.Message;

public abstract class RecognitionError implements Message {
	
	RecognitionException recognitionException;
	String[] tokenNames;
	
	public RecognitionError(RecognitionException re, String[] tn) {
		recognitionException = re;
		if (tn!=null) {
			tokenNames = new String[tn.length];
			for (int i=0; i<tn.length; i++) {
				tokenNames[i] = tokens.get(tn[i]);
			}
		}
	}
	
	public String[] getTokenNames() {
		return tokenNames;
	}
	
	public RecognitionException getRecognitionException() {
		return recognitionException;
	}
	
    @Override
    public Backend getBackend() {
        return null;
    }
    
	@Override
	public int getLine() {
		return recognitionException.line;
	}
	
	public int getCharacterInLine() {
		return recognitionException.charPositionInLine;
	}
	
	private static Map<String,String> tokens = new HashMap<String, String>();
	static {
		tokens.put("ABSTRACTED_TYPE", "'abstracts' keyword");
		tokens.put("ADAPTED_TYPES", "'adapts' keyword");
		tokens.put("ADD_ASSIGN_OP", "'+=' operator");
		tokens.put("AND_ASSIGN_OP", "'&&=' operator");
		tokens.put("AND_OP", "'&&' operator");
		tokens.put("ARRAY", "'[]'");
		tokens.put("ASSIGN", "'assign' keyword");
		tokens.put("ASSIGN_OP", "':=' operator");
		tokens.put("BREAK", "'break' keyword");
		tokens.put("CASE_CLAUSE", "'case' keyword");
		tokens.put("CASE_TYPES", "'of' keyword");
		tokens.put("CATCH_CLAUSE", "'catch' keyword");
		tokens.put("CHAR_LITERAL", "character literal");
		tokens.put("CLASS_DEFINITION", "'class' keyword");
		tokens.put("COMMA", "','");
		tokens.put("COMPARE_OP", "'<=>' operator");
		tokens.put("COMPLEMENT_ASSIGN_OP", "'~=' operator");
		tokens.put("COMPLEMENT_OP", "'~' operator");
		tokens.put("CONTINUE", "'continue' keyword");
		tokens.put("DECREMENT_OP", "'--' keyword");
		tokens.put("DEFAULT_OP", "'?'");
		tokens.put("DIFFERENCE_OP", "'-' operator");
		tokens.put("DIVIDE_ASSIGN_OP", "'/=' operator");
		tokens.put("ELLIPSIS", "'...'");
		tokens.put("ELSE_CLAUSE", "'else' keyword");
		tokens.put("ENTRY_OP", "'->' operator");
		tokens.put("EQUAL_OP", "'==' operator");
		tokens.put("EXISTS", "'exists' operator");
		tokens.put("EXTENDS", "'extends' keyword");
		tokens.put("FINALLY_CLAUSE", "'finally' keyword");
		tokens.put("FLOAT_LITERAL", "floating point literal");
		tokens.put("FOR_CLAUSE", "'for' keyword");
		tokens.put("FUNCTION_MODIFIER", "'function' keyword");
		tokens.put("IDENTICAL_OP", "'===' operator");
		tokens.put("IF_CLAUSE", "'if' keyword");
		tokens.put("IMPORT", "'import' keyword");
		tokens.put("INCREMENT_OP", "'++' operator");
		tokens.put("INDEX_OP", "opening bracket '['");
		tokens.put("INTERFACE_DEFINITION", "'interface' keyword");
		tokens.put("INTERSECTION_OP", "'&' operator");
		tokens.put("INTERSECT_ASSIGN_OP", "'&=' operator");
		tokens.put("IN_OP", "'in' operator");
		tokens.put("IS_OP", "'is' operator");
		tokens.put("LARGER_OP", "'>' operator");
		tokens.put("LARGE_AS_OP", "'>=' operator");
		tokens.put("LBRACE", "opening brace '{'");
		tokens.put("LBRACKET", "opening bracket '['");
		tokens.put("LIDENTIFIER", "initial-lowercase identifier");
		tokens.put("LINE_COMMENT", "comment");
		tokens.put("LPAREN", "opening parenthesis '('");
		tokens.put("MEMBER_OP", "'.' operator");
		tokens.put("MODULE", "'module' keyword");
		tokens.put("MULTIPLY_ASSIGN_OP", "'*=' operator");
		tokens.put("MULTI_COMMENT", "multiline comment");
		tokens.put("NATURAL_LITERAL", "number literal");
		tokens.put("NONEMPTY", "'nonempty' operator");
		tokens.put("NOT_EQUAL_OP", "'!=' operator");
		tokens.put("NOT_OP", "'!' operator");
		tokens.put("OBJECT_DEFINITION", "'object' keyword");
		tokens.put("OR_ASSIGN_OP", "'||=' operator");
		tokens.put("OR_OP", "'||' operator");
		tokens.put("OUT", "'out' keyword");
		tokens.put("OUTER", "'outer' keyword");
		tokens.put("PACKAGE", "'package' keyword");
		tokens.put("POWER_OP", "'**' operator");
		tokens.put("PRODUCT_OP", "'*' operator");
		tokens.put("QMARK", "'?'");
		tokens.put("QUOTED_LITERAL", "single-quoted string literal");
		tokens.put("QUOTIENT_OP", "'/' operator");
		tokens.put("RANGE_OP", "'..' operator");
		tokens.put("RBRACE", "closing brace '}'");
		tokens.put("RBRACKET", "closing bracket ']'");
		tokens.put("REMAINDER_ASSIGN_OP", "'%=' operator");
		tokens.put("REMAINDER_OP", "'%' operator");
		tokens.put("RETURN", "'return' keyword");
		tokens.put("RPAREN", "closing parenthesis ')'");
		tokens.put("SAFE_INDEX_OP", "'?['");
		tokens.put("SAFE_MEMBER_OP", "'?.' operator");
		tokens.put("SATISFIES", "'satisfies' keyword");
		tokens.put("SEGMENT_OP", "':' operator");
		tokens.put("SEMICOLON", "statement-ending ';'");
		tokens.put("SMALLER_OP", "'<' operator");
		tokens.put("SMALL_AS_OP", "'<=' operator");
		tokens.put("SPECIFY", "'=' specifier");
		tokens.put("SPREAD_OP", "'[].' operator");
		tokens.put("STRING_LITERAL", "double-quoted string literal");
		tokens.put("SUBTRACT_ASSIGN_OP", "'-=' operator");
		tokens.put("SUM_OP", "'+' operator");
		tokens.put("SUPER", "'super' keyword");
		tokens.put("SWITCH_CLAUSE", "'switch' keyword");
		tokens.put("THEN_CLAUSE", "'then' operator");
		tokens.put("THIS", "'this' keyword");
		tokens.put("THROW", "'throw' keyword");
		tokens.put("TRY_CLAUSE", "'try' keyword");
		tokens.put("TYPE_CONSTRAINT", "'given' keyword");
		tokens.put("UIDENTIFIER", "initial-uppercase identifier");
		tokens.put("UNION_ASSIGN_OP", "'|=' operator");
		tokens.put("UNION_OP", "'|' operator");
		tokens.put("VALUE_MODIFIER", "'value' keyword");
		tokens.put("VOID_MODIFIER", "'void' keyword");
		tokens.put("WHILE_CLAUSE", "'while' keyword");
		tokens.put("WS", "whitespace");
		tokens.put("XOR_ASSIGN_OP", "'^=' operator");
		tokens.put("XOR_OP", "'^' operator");
	}
	
}
