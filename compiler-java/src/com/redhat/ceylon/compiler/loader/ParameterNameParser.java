package com.redhat.ceylon.compiler.loader;

import static com.redhat.ceylon.compiler.loader.ParameterNameLexer.*;

import java.util.Iterator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;

/**
 * Parser for {@link com.redhat.ceylon.compiler.java.metadata.FunctionalParameter#value()}.
 * <pre>
 * input     ::= nameList ( nameList )*
 * nameList  ::= '(' ( name ( ',' name )* )? ')'
 * name      ::= identifier ( nameList )*
 * </pre>
 * @author tom
 */
class ParameterNameParser {
    
    private final ParameterNameLexer lexer = new ParameterNameLexer();
    private final AbstractModelLoader loader;
    
    ParameterNameParser(AbstractModelLoader loader) {
        this.loader = loader;
    }
    
    public void parse(String input, ProducedType type, Method method) {
        lexer.setup(input);
        method.addParameterList(parseNameList(type, method.getUnit()));
        while (lexer.lookingAt(LEFT_PAREN)) {// mpl
            type = loader.getSimpleCallableReturnType(type);
            method.addParameterList(parseNameList(type, method.getUnit()));
        }
        method.setType(loader.getSimpleCallableReturnType(type));
        if (!lexer.lookingAt(EOI)) {
            throw new ParameterNameParserException("Expected end of input" + System.lineSeparator() + input);
        }
    }
    private ParameterList parseNameList(ProducedType type, Unit unit) {
        ParameterList pl = new ParameterList();
        lexer.eat(LEFT_PAREN);
        if (!lexer.lookingAt(RIGHT_PAREN)) {
            Iterator<ProducedType> ct = loader.getSimpleCallableArgumentTypes(type).iterator();
            if (!ct.hasNext()) {
                throw new ParameterNameParserException("Too few parameter types");
            }
            pl.getParameters().add(parseName(ct.next(), unit));
            while (lexer.lookingAt(COMMA)) {
                lexer.eat();
                if (!ct.hasNext()) {
                    throw new ParameterNameParserException("Too few parameter types");
                }
                pl.getParameters().add(parseName(ct.next(), unit));
            }
            if (ct.hasNext()) {
                throw new ParameterNameParserException("Too many parameter types");
            }
        }
        lexer.eat(RIGHT_PAREN);
        return pl;
    }

    private Parameter parseName(ProducedType type, Unit unit) {
        String identifier = lexer.eatIdentifier();
        final MethodOrValue result;
        if (lexer.lookingAt(LEFT_PAREN)) {
            Method method = new Method();
            method.setType(loader.getSimpleCallableReturnType(type));
            while (lexer.lookingAt(LEFT_PAREN)) {
                method.addParameterList(parseNameList(type, unit));
                type = loader.getSimpleCallableReturnType(type);
            }
            result = method;
        } else {
            Value value = new Value();
            value.setType(type);
            result = value;
        }
        result.setName(identifier);
        result.setUnit(unit);
        Parameter p = new Parameter();
        p.setModel(result);
        p.setName(identifier);
        
        return p;
    }
    
}
