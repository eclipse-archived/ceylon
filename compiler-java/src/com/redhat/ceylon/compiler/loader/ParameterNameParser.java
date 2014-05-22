package com.redhat.ceylon.compiler.loader;

import static com.redhat.ceylon.compiler.loader.ParameterNameLexer.*;

import java.util.ArrayList;
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
 * input     ::= ( '!' )? nameList ( nameList )*
 * nameList  ::= '(' ( name ( ',' name )* )? ')'
 * name      ::= identifier ( '+' | '*' )? ( '!' )? ( nameList )*
 * </pre>
 * <ul>
 *   <li>A {@code !} means that the {@code Method} model is declared {@code void}</li>
 *   <li>A {@code +} means that the {@code Parameter} model is possibly-empty variadic</li>
 *   <li>A {@code *} means that the {@code Parameter} model is nonempty variadic</li>
 * </ul> 
 * @author tom
 */
class ParameterNameParser {
    
    private final ParameterNameLexer lexer = new ParameterNameLexer();
    private final AbstractModelLoader loader;
    private Unit unit;
    
    
    
    ParameterNameParser(AbstractModelLoader loader) {
        this.loader = loader;
    }
    
    public void parse(String input, ProducedType type, Method method) {
        lexer.setup(input);
        this.unit = method.getUnit();
        boolean declaredVoid = false;
        ArrayList<ParameterList> lists = new ArrayList<>();
        if (lexer.lookingAt(BANG)) {
            lexer.eat();
            declaredVoid = true;
        }
        lists.add(parseNameList(type, method));
        while (lexer.lookingAt(LEFT_PAREN)) {// mpl
            type = loader.getSimpleCallableReturnType(type);
            lists.add(parseNameList(type, method));
        }
        for (ParameterList parameterList : lists) {
            method.addParameterList(parameterList);
        }
        method.setDeclaredVoid(declaredVoid);
        method.setType(loader.getSimpleCallableReturnType(type));
        if (!lexer.lookingAt(EOI)) {
            throw new ParameterNameParserException("Expected end of input" + System.lineSeparator() + input);
        }
    }
    public void parseMpl(String input, ProducedType type, Method method) {
        lexer.setup(input);
        this.unit = method.getUnit();
        ArrayList<ParameterList> lists = new ArrayList<>();
        lists.add(parseNameList(type, method));
        while (lexer.lookingAt(LEFT_PAREN)) {// mpl
            type = loader.getSimpleCallableReturnType(type);
            lists.add(parseNameList(type, method));
        }
        for (ParameterList parameterList : lists) {
            method.addParameterList(parameterList);
        }
        method.setType(loader.getSimpleCallableReturnType(type));
        if (!lexer.lookingAt(EOI)) {
            throw new ParameterNameParserException("Expected end of input" + System.lineSeparator() + input);
        }
    }
    private ParameterList parseNameList(ProducedType type, Method method) {
        ParameterList pl = new ParameterList();
        List<Parameter> parameters = pl.getParameters();
        //startParameterList();
        lexer.eat(LEFT_PAREN);
        if (!lexer.lookingAt(RIGHT_PAREN)) {
            Iterator<ProducedType> ct = loader.getSimpleCallableArgumentTypes(type).iterator();
            if (!ct.hasNext()) {
                throw new ParameterNameParserException("Too few parameter types");
            }
            parameters.add(parseName(ct.next(), method));
            // addParameter()
            while (lexer.lookingAt(COMMA)) {
                lexer.eat();
                if (!ct.hasNext()) {
                    throw new ParameterNameParserException("Too few parameter types");
                }
                parameters.add(parseName(ct.next(), method));
            }
            if (ct.hasNext()) {
                throw new ParameterNameParserException("Too many parameter types");
            }
        }
        lexer.eat(RIGHT_PAREN);
        //endParameterList();
        return pl;
    }

    private Parameter parseName(ProducedType type, Method container) {
        String identifier = lexer.eatIdentifier();
        boolean declaredVoid = false;
        boolean sequenced = false;
        boolean atLeastOne = false;
        if (lexer.lookingAt(STAR)) {
            lexer.eat();
            sequenced = true;
        } else if (lexer.lookingAt(PLUS)) {
            lexer.eat();
            sequenced = true;
            atLeastOne = true;
        }
        if (lexer.lookingAt(BANG)) {
            lexer.eat();
            declaredVoid = true;
        }
        
        final MethodOrValue result;
        if (lexer.lookingAt(LEFT_PAREN)) {
            // functionParameter()
            result = parseMethod(type, declaredVoid);
        } else {
            if (declaredVoid) {
                throw new ParameterNameParserException("void Value");
            }
            // valueParameter();
            result = parseValue(type);
        }
        result.setName(identifier);
        result.setUnit(unit);
        result.setContainer(container);
        Parameter p = new Parameter();
        p.setName(identifier);
        p.setSequenced(sequenced);
        p.setAtLeastOne(atLeastOne);
        p.setDeclaredAnything(declaredVoid);
        p.setModel(result);
        return p;
    }

    private Value parseValue(ProducedType type) {
        Value value = new Value();
        value.setType(type);
        return value;
    }

    private Method parseMethod(ProducedType type, boolean declaredVoid) {
        Method method = new Method();
        method.setDeclaredVoid(declaredVoid);
        method.setType(loader.getSimpleCallableReturnType(type));
        while (lexer.lookingAt(LEFT_PAREN)) {
            method.addParameterList(parseNameList(type, method));
            type = loader.getSimpleCallableReturnType(type);
        }
        return method;
    }
    
}
