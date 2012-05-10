package com.redhat.ceylon.compiler.loader.typeparser;

import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.ModelLoader;
import com.redhat.ceylon.compiler.loader.ModelResolutionException;
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

    public TypeParser(ModelLoader loader, Unit unit){
        this.loader = loader;
        this.unit = unit;
    }
    
    /*
     * type: unionType EOT
     */
    public ProducedType decodeType(String type, Scope scope){
        TypeLexer lexer = new TypeLexer(type);
        ProducedType ret = parseType(lexer, scope);
        if(!lexer.lookingAt(TypeLexer.EOT))
            throw new RuntimeException("Junk lexemes remaining: "+lexer.getTokenString());
        return ret;
    }

    /*
     * type: unionType EOT
     */
    private ProducedType parseType(TypeLexer lexer, Scope scope){
        return parseUnionType(lexer, scope);
    }

    /*
     * unionType: intersectionType (& intersectionType)*
     */
    private ProducedType parseUnionType(TypeLexer lexer, Scope scope) {
        UnionType type = new UnionType(unit);
        List<ProducedType> caseTypes = new LinkedList<ProducedType>();
        type.setCaseTypes(caseTypes);
        caseTypes.add(parseIntersectionType(lexer, scope));
        while(lexer.lookingAt(TypeLexer.OR)){
            lexer.eat();
            caseTypes.add(parseIntersectionType(lexer, scope));
        }
        return type.getType();
    }

    /*
     * intersectionType: qualifiedType (| qualifiedType)*
     */
    private ProducedType parseIntersectionType(TypeLexer lexer, Scope scope) {
        IntersectionType type = new IntersectionType(unit);
        List<ProducedType> satisfiedTypes = new LinkedList<ProducedType>();
        type.setSatisfiedTypes(satisfiedTypes);
        satisfiedTypes.add(parseQualifiedType(lexer, scope));
        while(lexer.lookingAt(TypeLexer.AND)){
            lexer.eat();
            satisfiedTypes.add(parseQualifiedType(lexer, scope));
        }
        return type.getType();
    }

    /*
     * qualifiedType: typeNameWithArguments (. typeNameWithArguments)*
     */
    private ProducedType parseQualifiedType(TypeLexer lexer, Scope scope) {
        Part part = parseTypeNameWithArguments(lexer, scope);
        String fullName = part.name;
        ProducedType qualifyingType = loadType(fullName, part, null, scope);
        while(lexer.lookingAt(TypeLexer.DOT)){
            lexer.eat();
            part = parseTypeNameWithArguments(lexer, scope);
            fullName = fullName + '.' + part.name;
            qualifyingType = loadType(fullName, part, qualifyingType, scope);
        }
        if(qualifyingType == null){
            throw new ModelResolutionException("Could not find type "+fullName);
        }
        return qualifyingType;
    }

    private ProducedType loadType(String fullName, Part part, ProducedType qualifyingType, Scope scope) {
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
    private Part parseTypeNameWithArguments(TypeLexer lexer, Scope scope) {
        Part type = new Part();
        type.name = Util.quoteIfJavaKeyword(lexer.getWord());
        if(lexer.lookingAt(TypeLexer.LT)){
            lexer.eat();
            type.parameters.add(parseType(lexer, scope));
            while(lexer.lookingAt(TypeLexer.COMMA)){
                lexer.eat();
                type.parameters.add(parseType(lexer, scope));
            }
            lexer.eat(TypeLexer.GT);
        }
        return type;
    }
}
