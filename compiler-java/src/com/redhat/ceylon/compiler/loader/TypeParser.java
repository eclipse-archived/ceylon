package com.redhat.ceylon.compiler.loader;

import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

public class TypeParser {
    public class Part {
        String name;
        List<ProducedType> parameters = new LinkedList<ProducedType>();
    }

    private ModelLoader loader;
    private Unit unit;
    private TypeLexer lexer = new TypeLexer();
    private Scope scope;

    public TypeParser(ModelLoader loader, Unit unit){
        this.loader = loader;
        this.unit = unit;
    }
    
    /*
     * type: unionType EOT
     */
    public ProducedType decodeType(String type, Scope scope){
        // save the previous state (this method is reentrant)
        char[] oldType = lexer.type;
        int oldIndex = lexer.index;
        Scope oldScope = this.scope;
        try{
            // setup the new state
            lexer.setup(type);
            this.scope = scope;
            // do the parsing
            ProducedType ret = parseType();
            if(!lexer.lookingAt(TypeLexer.EOT))
                throw new RuntimeException("Junk lexemes remaining: "+lexer.eatTokenString());
            return ret;
        }finally{
            // restore the previous state
            lexer.type = oldType;
            lexer.index = oldIndex;
            this.scope = oldScope;
        }
    }

    /*
     * type: unionType EOT
     */
    private ProducedType parseType(){
        return parseUnionType();
    }

    /*
     * unionType: intersectionType (& intersectionType)*
     */
    private ProducedType parseUnionType() {
        UnionType type = new UnionType(unit);
        List<ProducedType> caseTypes = new LinkedList<ProducedType>();
        type.setCaseTypes(caseTypes);
        caseTypes.add(parseIntersectionType());
        while(lexer.lookingAt(TypeLexer.OR)){
            lexer.eat();
            caseTypes.add(parseIntersectionType());
        }
        return type.getType();
    }

    /*
     * intersectionType: qualifiedType (| qualifiedType)*
     */
    private ProducedType parseIntersectionType() {
        IntersectionType type = new IntersectionType(unit);
        List<ProducedType> satisfiedTypes = new LinkedList<ProducedType>();
        type.setSatisfiedTypes(satisfiedTypes);
        satisfiedTypes.add(parseQualifiedType());
        while(lexer.lookingAt(TypeLexer.AND)){
            lexer.eat();
            satisfiedTypes.add(parseQualifiedType());
        }
        return type.getType();
    }

    /*
     * qualifiedType: typeNameWithArguments (. typeNameWithArguments)*
     */
    private ProducedType parseQualifiedType() {
        Part part = parseTypeNameWithArguments();
        String fullName = part.name;
        ProducedType qualifyingType = loadType(fullName, part, null);
        while(lexer.lookingAt(TypeLexer.DOT)){
            lexer.eat();
            part = parseTypeNameWithArguments();
            fullName = fullName + '.' + part.name;
            qualifyingType = loadType(fullName, part, qualifyingType);
        }
        if(qualifyingType == null){
            throw new ModelResolutionException("Could not find type "+fullName);
        }
        return qualifyingType;
    }

    private ProducedType loadType(String fullName, Part part, ProducedType qualifyingType) {
        // try to find a qualifying type
        try{
            ProducedType newType = loader.getType(fullName, scope);
            return newType.getDeclaration().getProducedType(qualifyingType, part.parameters);
        }catch(ModelResolutionException x){
            // allow this only if we don't have any qualifying type or parameters:
            // - if we have no qualifying type we may be adding package name parts
            // - if we have a qualifying type then the inner type must exist
            // - if we have type parameters we must have a type
            if(qualifyingType != null
                    || !part.parameters.isEmpty())
                throw x;
            return null;
        }
    }

    /*
     * typeNameWithArguments: WORD (< type (, type)* >)?
     */
    private Part parseTypeNameWithArguments() {
        Part type = new Part();
        type.name = Util.quoteIfJavaKeyword(lexer.eatWord());
        if(lexer.lookingAt(TypeLexer.LT)){
            lexer.eat();
            type.parameters.add(parseType());
            while(lexer.lookingAt(TypeLexer.COMMA)){
                lexer.eat();
                type.parameters.add(parseType());
            }
            lexer.eat(TypeLexer.GT);
        }
        return type;
    }
}
