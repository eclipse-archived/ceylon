package com.redhat.ceylon.model.loader;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.union;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.model.loader.model.FunctionOrValueInterface;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;


/**
 * <pThe sub-grammar from the spec looks like this:</p>
 * <blockquote><pre>
 * Type: UnionType | EntryType
 * 
 * EntryType: UnionType "->" UnionType
 * 
 * UnionType: IntersectionType ("|" IntersectionType)*
 * 
 * IntersectionType: PrimaryType ("&" PrimaryType)*
 * 
 * PrimaryType: AtomicType | OptionalType | SequenceType | CallableType
 * 
 * AtomicType: QualifiedType | EmptyType | TupleType | IterableType
 * 
 * OptionalType: PrimaryType "?"
 * 
 * SequenceType: PrimaryType "[" "]"
 * 
 * CallableType: PrimaryType "(" (TypeList? | SpreadType) ")"
 * 
 * 
 * QualifiedType: BaseType ("." TypeNameWithArguments)*
 * 
 * BaseType: PackageQualifier? TypeNameWithArguments | GroupedType
 * 
 * TypeNameWithArguments: TypeName TypeArguments?
 * 
 * PackageQualifier: "package" "."
 * 
 * GroupedType: "<" Type ">"
 * 
 * 
 * TypeArguments: "<" ((TypeArgument ",")* TypeArgument)? ">"
 * 
 * TypeArgument: Variance Type
 * 
 * Variance: ("out" | "in")?
 * 
 * SpreadType: "*" UnionType
 * 
 * IterableType: "{" UnionType ("*"|"+") "}"
 * 
 * EmptyType: "[" "]"
 * 
 * TupleType: "[" TypeList "]" | PrimaryType "[" DecimalLiteral "]"
 * 
 * 
 * TypeList: (DefaultedType ",")* (DefaultedType | VariadicType)
 * 
 * DefaultedType: Type "="?
 * 
 * VariadicType: UnionType ("*" | "+")
 *
 * </pre></blockquote>
 * @see com.redhat.ceylon.model.typechecker.util.TypePrinter
 */
public class TypeParser {
    public class Part {
        public Part(){}
        public Part(String name) {
            this.name = name;
        }
        public Part(String name, List<Type> parameters) {
            this.name = name;
            this.parameters = parameters;
        }
        String name;
        List<Type> parameters;
        List<SiteVariance> variance;
        List<Type> getParameters(){
            return parameters != null ? parameters : Collections.<Type>emptyList();
        }
        List<SiteVariance> getVariance(){
            return variance != null ? variance : Collections.<SiteVariance>emptyList();
        }
    }

    private ModelLoader loader;
    private Unit unit;
    private TypeLexer lexer = new TypeLexer();
    private Scope scope;
    private Module moduleScope;

    public TypeParser(ModelLoader loader){
        this.loader = loader;
    }
    
    public Type decodeType(String type, Scope scope, Module moduleScope, Unit unit){
        // save the previous state (this method is reentrant)
        char[] oldType = lexer.type;
        int oldIndex = lexer.index;
        int oldMark = lexer.mark;
        Scope oldScope = this.scope;
        Module oldModuleScope = this.moduleScope;
        Unit oldUnit = this.unit;
        try{
            // setup the new state
            lexer.setup(type);
            this.scope = scope;
            this.moduleScope = moduleScope;
            this.unit = unit;
            // do the parsing
            Type ret = parseType();
            if(!lexer.lookingAt(TypeLexer.EOT))
                throw new TypeParserException("Junk lexemes remaining: "+lexer.eatTokenString());
            return ret;
        }finally{
            // restore the previous state
            lexer.type = oldType;
            lexer.index = oldIndex;
            lexer.mark = oldMark;
            this.scope = oldScope;
            this.moduleScope = oldModuleScope;
            this.unit = oldUnit;
        }
    }

    /**
     * <blockquote><pre>
     * Type: UnionType | EntryType
     * EntryType: UnionType "->" UnionType
     * </pre></blockquote>
     */
    /*
     * type: unionType | entryType EOT
     * entryType: unionType -> unionType
     */
    private Type parseType(){
        Type type = parseUnionType();
        if (lexer.lookingAt(TypeLexer.THIN_ARROW)) {
            lexer.eat(TypeLexer.THIN_ARROW);
            type = unit.getEntryType(type, parseUnionType()); 
        }
        return type;
    }

    /**
     * <blockquote><pre>
     * UnionType: IntersectionType ("|" IntersectionType)*
     * </pre></blockquote>
     */
    /*
     * unionType: intersectionType (| intersectionType)*
     */
    private Type parseUnionType() {
        Type firstType = parseIntersectionType();
        if(lexer.lookingAt(TypeLexer.OR)){
            List<Type> caseTypes = new LinkedList<Type>();
            caseTypes.add(firstType);
            while(lexer.lookingAt(TypeLexer.OR)){
                lexer.eat();
                caseTypes.add(parseIntersectionType());
            }
            return union(caseTypes, unit);
        }else{
            return firstType;
        }
    }

    /**
     * <blockquote><pre>
     * IntersectionType: PrimaryType ("&" PrimaryType)*
     * </pre></blockquote>
     */
    /*
     *  intersectionType: qualifiedType (& qualifiedType)*
     */
    private Type parseIntersectionType() {
        Type firstType = parsePrimaryType();
        if(lexer.lookingAt(TypeLexer.AND)){
            List<Type> satisfiedTypes = new LinkedList<Type>();
            satisfiedTypes.add(firstType);
            while(lexer.lookingAt(TypeLexer.AND)){
                lexer.eat();
                satisfiedTypes.add(parsePrimaryType());
            }
            return intersection(satisfiedTypes, unit);
        }else{
            return firstType;
        }
    }
    
    /**
     * <blockquote><pre>
     * PrimaryType: AtomicType | OptionalType | SequenceType | CallableType
     * </pre></blockquote>
     * Where:
     * <blockquote><pre>
     * QualifiedType: BaseType ("." TypeNameWithArguments)*
     * BaseType: PackageQualifier? TypeNameWithArguments | GroupedType
     * </pre></blockquote>
     */
    /* primaryType: compoundQualifiedType | simpleQualifiedType
     */
    private Type parsePrimaryType() {
        Type type = parseAtomicType();
        // PrimaryType
        type = parsePrimaryType(type);
        return type;
    }

    /**
     * Spec says:
     * <blockquote><pre>
     * AtomicType: QualifiedType | EmptyType | TupleType | IterableType)
     * </pre></blockquote>
     * We implement this, but we've merged {@code EmptyType} and {@code TupleType}
     * in {@link #parseEmptyOrTupleType()}
     */
    protected Type parseAtomicType() {
        Type type;
        if (lexer.lookingAt(TypeLexer.OPEN_SQ)) {
            type = parseEmptyOrTupleType();
        } else if (lexer.lookingAt(TypeLexer.OPEN_BR)) {
            type = parseIterableAbbreviatedType();
        } else {
            type = parseQualifiedType();
        }
        return type;
    }

    /**
     * Spec says:
     * <blockquote><pre>
     * PrimaryType: AtomicType | OptionalType | SequenceType | CallableType
     * </pre></blockquote>
     * We rely on our caller to have parsed the "primary type" and just handle
     * the postfix {@code ?}, {@code [...]} or {@code (...)}
     */
    protected Type parsePrimaryType(Type type) {
        while (lexer.lookingAt(TypeLexer.QN)
                || lexer.lookingAt(TypeLexer.OPEN_SQ)
                || lexer.lookingAt(TypeLexer.OPEN_PAR)) {
            if (lexer.lookingAt(TypeLexer.QN)) {
                type = parseOptionalType(type);
            } else if (lexer.lookingAt(TypeLexer.OPEN_SQ)) {
                type = parseSequenceType(type);
            } else if (lexer.lookingAt(TypeLexer.OPEN_PAR)) {
                type = parseCallableType(type);
            }
        }
        return type;
    }
    
    /**
     * Spec says:
     * <blockquote><pre>
     * CallableType: PrimaryType "(" (TypeList? | SpreadType) ")"
     * SpreadType: "*" UnionType
     * </pre></blockquote>
     * We rely on our caller to have parsed the primary type, and just handle 
     * the parenthesized argument list type.
     */
    private Type parseCallableType(Type primaryType) {
        lexer.eat(TypeLexer.OPEN_PAR);
        Type arguments;
        if (lexer.lookingAt(TypeLexer.STAR)) {
            lexer.eat(TypeLexer.STAR);
            arguments = parseUnionType();
        } else {
            if (!lexer.lookingAt(TypeLexer.CLOSE_PAR)) {
                TypeList typeList = parseTypeList();
                arguments = typeList.asTuple();
            } else {
                arguments = unit.getEmptyType();
            }
        }
        lexer.eat(TypeLexer.CLOSE_PAR);
        return unit.getCallableDeclaration().appliedType(null, Arrays.asList(primaryType, arguments));
    }

    /**
     * Spec says:
     * <blockquote><pre>
     *     SequenceType: PrimaryType "[" "]"
     * </pre></blockquote>
     * This method also handles the right hand variant of:
     * <blockquote><pre>
     *     TupleType: "[" TypeList "]" | PrimaryType "[" DecimalLiteral "]"
     * </blockquote></pre>
     * because it's more easily done here.
     */
    private Type parseSequenceType(Type elementType) {
        lexer.eat(TypeLexer.OPEN_SQ);
        Type result;
        if (lexer.lookingAt(TypeLexer.WORD)) {
            int length = lexer.eatDigits();
            result = unit.getEmptyType();
            while (length > 0) {
                result = unit.getTupleDeclaration().appliedType(null, Arrays.asList(elementType, elementType, result));
                length--;
            }
        } else {
            result = unit.getSequentialType(elementType);
        }
        lexer.eat(TypeLexer.CLOSE_SQ);
        return result;
    }

    /**
     * Spec says:
     * <blockquote><pre>
     * OptionalType: PrimaryType "?"
     * </pre></blockquote>
     * We rely on our caller to have parsed the primary type
     */
    private Type parseOptionalType(Type type) {
        lexer.eat(TypeLexer.QN);
        return unit.getOptionalType(type);
    }

    /**
     * Spec says:
     * <blockquote><pre>
     * IterableType: "{" UnionType ("*"|"+") "}"
     * </blockquote></pre>
     */
    private Type parseIterableAbbreviatedType() {
        lexer.eat(TypeLexer.OPEN_BR);
        Type iterated = parseUnionType();
        Type result = null;
        if (lexer.lookingAt(TypeLexer.PLUS)) {
            lexer.eat(TypeLexer.PLUS);
            result = unit.getNonemptyIterableType(iterated);
        } else if (lexer.lookingAt(TypeLexer.STAR)) {
            lexer.eat(TypeLexer.STAR);
            result = unit.getIterableType(iterated);
        } else {
            throw new TypeParserException("Expected multiplicity in abbreviated Iterable type: "+lexer.index);
        }
        lexer.eat(TypeLexer.CLOSE_BR);
        return result;
    }

    /**
     * Spec says:
     * <blockquote><pre>
     * EmptyType: "[" "]"
     * TupleType: "[" TypeList "]" | PrimaryType "[" DecimalLiteral "]"
     * </blockquote></pre>
     * This method doesn't handle the {@code X[123]} alternative of 
     * TupleType, that's done in {@link #parseSequenceType(Type)} instead.
     */
    private Type parseEmptyOrTupleType() {
        lexer.eat(TypeLexer.OPEN_SQ);
        if (lexer.lookingAt(TypeLexer.CLOSE_SQ)) {
            return parseEmptyType();
        }
        TypeList typeList = parseTypeList();
        final Type result = typeList.asTuple();
        lexer.eat(TypeLexer.CLOSE_SQ);
        return result;
    }
    
    /**
     * Spec says:
     * <blockquote><pre>
     * EmptyType: "[" "]"
     * </blockquote></pre>
     */
    protected Type parseEmptyType() {
        lexer.eat(TypeLexer.CLOSE_SQ);
        return unit.getEmptyType();
    }
    
    /**
     * Helper for parsing tuple types.
     */
    class TypeList {
        
        public TypeList(List<Type> types, boolean variadic, boolean atLeastOne) {
            super();
            this.types = types;
            this.variadic = variadic;
            this.atLeastOne = atLeastOne;
        }
        public Type getFirst() {
            return types.get(0);
        }
        List<Type> types;
        boolean variadic;
        boolean atLeastOne;
        Type getLast() {
            return types.get(types.size()-1);
        }
        
        Type asTuple() {
            final Type result;
            if (types.size() == 0) {
                result = parseEmptyType();
            } else {
                final Type sequentialType;
                if (variadic) {
                    Part part = new Part("Sequence", Collections.singletonList(getLast()));
                    sequentialType = loadType("ceylon.language", 
                            atLeastOne ? "ceylon.language.Sequence" : "ceylon.language.Sequential", 
                                    part, null);
                } else {
                    sequentialType = unit.getEmptyType();
                }
                
                if (variadic && types.size() == 1) {
                    result = sequentialType;
                } else {
                    Part part = new Part();
                    Type union = getLast();
                    Type tupleType = sequentialType;
                    for (int ii  = types.size()-(variadic? 2 : 1); ii >= 0; ii--) {
                        Type t = types.get(ii);
                        union = ModelUtil.unionType(union, t, unit);
                        part.parameters = Arrays.asList(union, t, tupleType);
                        part.name = "Tuple";
                        tupleType = loadType("ceylon.language", "ceylon.language.Tuple", part, null);
                    }
                    result = tupleType;
                }
            }
            return result;
        }
    }
    
    /**
     * Spec says:
     * <blockquote><pre>
     * TypeList: (DefaultedType ",")* (DefaultedType | VariadicType)
     * DefaultedType: Type "="?
     * VariadicType: UnionType ("*" | "+")
     * </blockquote></pre>
     * We don't care about defaulted types though.
     */
    private TypeList parseTypeList() {
        ArrayList<Type> types= new ArrayList<>();
        types.add(parseType());
        while(lexer.lookingAt(TypeLexer.COMMA)){
            lexer.eat(TypeLexer.COMMA);
            // XXX When the last type is matching VariadicType
            // we should be using parseUnionType, not parseType().
            types.add(parseType());
        }
        boolean variadic;
        boolean atLeastOne;
        if (lexer.lookingAt(TypeLexer.STAR)){
            lexer.eat(TypeLexer.STAR);
            variadic = true;
            atLeastOne = false;
        } else if (lexer.lookingAt(TypeLexer.PLUS)){
            lexer.eat(TypeLexer.PLUS);
            variadic = true;
            atLeastOne = true;
        } else {
            variadic = false;
            atLeastOne = false;
        }
        return new TypeList(types, variadic, atLeastOne);
    }

    /**
     * Spec says: 
     * <blockquote><pre>
     * GroupedType: "<" Type ">"
     * </pre></blockquote>
     */
    private Type parseGroupedType() {
        lexer.eat(TypeLexer.LT);
        Type unionType = parseType();
        lexer.eat(TypeLexer.GT);
        return unionType;
    }
    
    /**
     * Spec says: 
     * <blockquote><pre>
     * QualifiedType: BaseType ("." TypeNameWithArguments)*
     * BaseType: PackageQualifier? TypeNameWithArguments | GroupedType
     * TypeNameWithArguments: TypeName TypeArguments?
     * PackageQualifier: "package" "."
     * GroupedType: "<" Type ">"
     * </blockquote></pre>
     * We implement this quite differently, because we handle 
     * {@code package.qualification::}, which the spec doesn't 
     * have to cover at all.
     */
    private Type parseQualifiedType() {
        Type baseType;
        if (lexer.lookingAt(TypeLexer.LT)){
            baseType = parseGroupedType();
        } else {
            BaseType bt = parseBaseType();
            
            String fullName = bt.fullName;
            Type qualifyingType = bt.qualifyingType;
            while(lexer.lookingAt(TypeLexer.DOT)){
                lexer.eat();
                Part part = parseTypeNameWithArguments();
                fullName = fullName + '.' + part.name;
                qualifyingType = loadType(bt.pkg, fullName, part, qualifyingType);
            }
            if(qualifyingType == null){
                throw new ModelResolutionException("Could not find type '"+fullName+"'");
            }
            if(qualifyingType instanceof Type == false){
                throw new ModelResolutionException("Type is a declaration (should be a Type): '"+fullName+"'");
            }
            baseType = (Type) qualifyingType;
        }
        
        Part part;
        String fullName = "";
        Type qualifyingType = baseType;
        while(lexer.lookingAt(TypeLexer.DOT)){
            lexer.eat();
            part = parseTypeNameWithArguments();
            fullName = fullName + '.' + part.name;
            qualifyingType = loadType("", fullName, part, qualifyingType);
        }
        if(qualifyingType == null){
            throw new ModelResolutionException("Could not find type '"+fullName+"'");
        }
        if(qualifyingType instanceof Type == false){
            throw new ModelResolutionException("Type is a declaration (should be a Type): '"+fullName+"'");
        }
        return (Type) qualifyingType;
    }
    
    /**
     * Helper for parsing base types.
     */
    static class BaseType {
        String pkg;
        String fullName;
        Type qualifyingType;
        public BaseType(String pkg, Type qualifyingType, String fullName) {
            super();
            this.pkg = pkg;
            this.qualifyingType = qualifyingType;
            this.fullName = fullName;
        }
    }
    
    private BaseType parseBaseType() {
        
        String pkg;
        
        if (hasPackage()) {
            // handle the package name
            StringBuilder pkgstr = new StringBuilder(lexer.eatWord());
            while(lexer.lookingAt(TypeLexer.DOT)){
                lexer.eat();
                pkgstr = pkgstr.append('.').append(lexer.eatWord());
            }
            lexer.eat(TypeLexer.DBLCOLON);
            pkg = pkgstr.toString();
        } else {
            // type is in default package
            pkg = "";
        }
        
        // then the type itself
        Part part = parseTypeNameWithArguments();
        String fullName = (pkg.isEmpty()) ? part.name : pkg + "." + part.name;
        Type qualifyingType = loadType(pkg, fullName, part, null);
        if(qualifyingType == null){
            throw new ModelResolutionException("Could not find type '"+fullName+"'");
        }
        return new BaseType(pkg, qualifyingType, fullName);
    }

    private boolean hasPackage() {
        boolean result;
        lexer.mark();
        while(lexer.lookingAt(TypeLexer.WORD) || lexer.lookingAt(TypeLexer.DOT)){
            lexer.eat();
        }
        result = lexer.lookingAt(TypeLexer.DBLCOLON);
        lexer.reset();
        return result;
    }
    
    private Type loadType(String pkg, String fullName, Part part, Type qualifyingType) {
        // try to find a qualified type
        try{
            Declaration newDeclaration;
            if(qualifyingType == null){
                // FIXME: this only works for packages not contained in multiple modules
                Package foundPackage = moduleScope.getPackage(pkg);
                if(foundPackage != null)
                    newDeclaration = loader.getDeclaration(foundPackage.getModule(), pkg, fullName, scope);
                else if(scope != null){
                    // if we did not find any package and the scope is null, chances are we're after a type variable
                    // or a relative type, so use the module scope
                    newDeclaration = loader.getDeclaration(moduleScope, pkg, fullName, scope);
                }else
                    newDeclaration = null;
            }else{
                // look it up via its qualifying type or decl
                Declaration qualifyingDeclaration = qualifyingType.getDeclaration();
                if (qualifyingType.isUnion() || qualifyingType.isIntersection()) {
                    newDeclaration = qualifyingDeclaration.getMember(part.name, null, false);
                } else {
                    if(qualifyingDeclaration instanceof FunctionOrValueInterface)
                        qualifyingDeclaration = ((FunctionOrValueInterface)qualifyingDeclaration).getUnderlyingDeclaration();
                    newDeclaration = AbstractModelLoader.getDirectMember((Scope) qualifyingDeclaration, part.name);
                }
                if(newDeclaration == null)
                    throw new ModelResolutionException("Failed to resolve inner type or declaration "+part.name+" in "+qualifyingDeclaration.getQualifiedNameString());
            }
            if(newDeclaration == null)
                return null;
            TypeDeclaration newTypeDeclaration;
            if(newDeclaration instanceof TypeDeclaration)
                newTypeDeclaration = (TypeDeclaration) newDeclaration;
            else
                newTypeDeclaration = new FunctionOrValueInterface((TypedDeclaration) newDeclaration);
            Type ret = newTypeDeclaration.appliedType(qualifyingType, part.getParameters());
            // set the use-site variance if required, now that we know the TypeParameter declarations
            if(!part.getVariance().isEmpty()){
                List<TypeParameter> tps = newTypeDeclaration.getTypeParameters();
                List<SiteVariance> variance = part.getVariance();
                for(int i=0, l1=tps.size(), l2=variance.size() ; i<l1 && i<l2 ; i++){
                    SiteVariance siteVariance = variance.get(i);
                    if(siteVariance != null){
                        ret.setVariance(tps.get(i), siteVariance);
                    }
                }
            }
            return ret;
        }catch(ModelResolutionException x){
            // allow this only if we don't have any qualifying type or parameters:
            // - if we have no qualifying type we may be adding package name parts
            // - if we have a qualifying type then the inner type must exist
            // - if we have type parameters we must have a type
            if(qualifyingType != null
                    || (part.parameters != null && !part.parameters.isEmpty()))
                throw x;
            return null;
        }
    }

    /**
     * Spec says:
     * <blockquote><pre>
     * TypeNameWithArguments: TypeName TypeArguments?
     * TypeArguments: "<" ((TypeArgument ",")* TypeArgument)? ">"
     * TypeArgument: Variance Type
     * Variance: ("out" | "in")?
     * </pre></blockquote>
     */
    private Part parseTypeNameWithArguments() {
        Part type = new Part();
        type.name = lexer.eatWord();
        if(lexer.lookingAt(TypeLexer.LT)){
            lexer.eat();
            parseTypeArgumentVariance(type);
            type.parameters = new LinkedList<Type>();
            type.parameters.add(parseType());
            while(lexer.lookingAt(TypeLexer.COMMA)){
                lexer.eat();
                parseTypeArgumentVariance(type);
                type.parameters.add(parseType());
            }
            lexer.eat(TypeLexer.GT);
        }
        return type;
    }

    /**
     * Spec says:
     * <blockquote><pre>
     * Variance: ("out" | "in")?
     * </blockquote></pre>
     * Which is what we do, but by updating the given part.
     */
    private void parseTypeArgumentVariance(Part type) {
        SiteVariance variance = null;
        if(lexer.lookingAt(TypeLexer.OUT)){
            variance = SiteVariance.OUT;
            lexer.eat();
        }else if(lexer.lookingAt(TypeLexer.IN)){
            variance = SiteVariance.IN;
            lexer.eat();
        }
        // lazy allocation
        if(variance != null && type.variance == null){
            type.variance = new LinkedList<SiteVariance>();
            for(int i=0,l=type.getParameters().size();i<l;i++){
                // patch it up for the previous type params which did not have variance
                type.variance.add(null);
            }
        }
        // only add the variance if we have to
        if(type.variance != null){
            // we add it even if it's null, as long as we're recording variance
            type.variance.add(variance);
        }
    }
}
