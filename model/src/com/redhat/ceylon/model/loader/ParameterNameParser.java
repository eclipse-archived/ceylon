package com.redhat.ceylon.model.loader;

import static com.redhat.ceylon.model.loader.ParameterNameLexer.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Parser for {@link com.redhat.ceylon.compiler.java.metadata.FunctionalParameter#value()}.
 * <pre>
 * input     ::= ( '!' )? nameList ( nameList )*
 * nameList  ::= '(' ( name ( ',' name )* )? ')'
 * name      ::= identifier ( '+' | '*' )? ( '!' )? ( nameList )*
 * </pre>
 * <ul>
 *   <li>A {@code !} means that the {@code Function} model is declared {@code void}</li>
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
    
    public void parse(String input, Type type, Function method) {
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
    public void parseMpl(String input, Type type, Function method) {
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
    private ParameterList parseNameList(Type type, Function method) {
        ParameterList pl = new ParameterList();
        List<Parameter> parameters = pl.getParameters();
        //startParameterList();
        lexer.eat(LEFT_PAREN);
        if (!lexer.lookingAt(RIGHT_PAREN)) {
            Iterator<Type> ct = loader.getSimpleCallableArgumentTypes(type).iterator();
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

    private Parameter parseName(Type type, Function container) {
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
        
        final FunctionOrValue result;
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
        result.setScope(container);
        Parameter p = new Parameter();
        p.setName(identifier);
        p.setSequenced(sequenced);
        p.setAtLeastOne(atLeastOne);
        p.setDeclaredAnything(declaredVoid);
        p.setModel(result);
        result.setInitializerParameter(p);
        container.addMember(result);
        return p;
    }

    private Value parseValue(Type type) {
        Value value = new Value();
        value.setType(type);
        return value;
    }

    private Function parseMethod(Type type, boolean declaredVoid) {
        Function method = new Function();
        method.setDeclaredVoid(declaredVoid);
        method.setType(loader.getSimpleCallableReturnType(type));
        while (lexer.lookingAt(LEFT_PAREN)) {
            method.addParameterList(parseNameList(type, method));
            type = loader.getSimpleCallableReturnType(type);
        }
        return method;
    }
    
}
