package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.NO_TYPE_ARGS;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToUnion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.appliedType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.canonicalIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isNameMatching;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isToplevelAnonymousClass;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isToplevelClassConstructor;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.union;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.unionType;
import static com.redhat.ceylon.model.typechecker.model.Module.LANGUAGE_MODULE_NAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import com.redhat.ceylon.model.typechecker.context.TypeCache;

public class Unit {

    private Package pkg;
    private List<Import> imports = new ArrayList<Import>();
    private List<Declaration> declarations = new ArrayList<Declaration>();
    private String filename;
    private List<ImportList> importLists = new ArrayList<ImportList>();
    private Set<Declaration> duplicateDeclarations = new HashSet<Declaration>();
    private final Set<String> dependentsOf = new HashSet<String>();
    private String fullPath;
    private String relativePath;
    
    public List<Import> getImports() {
        return imports;
    }

    public List<ImportList> getImportLists() {
        return importLists;
    }

    /**
     * @return the dependentsOf
     */
    public Set<String> getDependentsOf() {
        return dependentsOf;
    }
    
    public Set<Declaration> getDuplicateDeclarations() {
        return duplicateDeclarations;
    }

    public Package getPackage() {
        return pkg;
    }

    public void setPackage(Package p) {
        pkg = p;
    }

    public List<Declaration> getDeclarations() {
        synchronized (declarations) {
            return new ArrayList<Declaration>(declarations);
        }
    }
    
    public void addDeclaration(Declaration declaration) {
        synchronized (declarations) {
            declarations.add(declaration);
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public String toString() {
        return filename;
    }

    public Import getImport(String name) {
        for (Import i: getImports()) {
            if (!i.isAmbiguous() &&
                    i.getTypeDeclaration()==null &&
                    i.getAlias().equals(name)) {
                return i;
            }
        }
        return null;
    }
    
    public String getAliasedName(Declaration dec) {
        return getAliasedName(dec, dec.getName());
    }

    public String getAliasedName(Declaration dec, String defaultValue) {
        for (Import i: getImports()) {
            if (!i.isAmbiguous() &&
                    i.getDeclaration().equals(getAbstraction(dec))) {
                return i.getAlias();
            }
        }
        return defaultValue;
    }

    public static Declaration getAbstraction(Declaration dec){
        if (isOverloadedVersion(dec)) {
            return dec.getContainer()
                    .getDirectMember(dec.getName(), 
                            null, false);
        }
        else {
            return dec;
        }
    }
    
    /**
     * Search the imports of a compilation unit for the 
     * named toplevel declaration.
     */
    public Declaration getImportedDeclaration(String name, 
            List<Type> signature, boolean ellipsis) {
        for (Import i: getImports()) {
            if (!i.isAmbiguous() && 
                    i.getAlias().equals(name)) {
                //in case of an overloaded member, this will
                //be the "abstraction", so search for the 
                //correct overloaded version
                Declaration d = i.getDeclaration();
                if (isToplevelImport(i, d)) {
                    return d.getContainer()
                            .getMember(d.getName(), 
                                    signature, ellipsis);
                }
            }
        }
        return null;
    }
    
    /**
     * Does this import element import a new name directly 
     * into the toplevel namespace of the compilation unit.
     * 
     * @return true if the imported name or alias is added
     *         to the namespace of the compilation unit
     */
    static boolean isToplevelImport(Import i, Declaration d) {
        return d.isToplevel() || 
            d.isStaticallyImportable() ||
            isToplevelClassConstructor(i.getTypeDeclaration(), d) ||
            isToplevelAnonymousClass(i.getTypeDeclaration());
    }
    
    /**
     * Search the imports of a compilation unit for the 
     * named member declaration.
     */
    public Declaration getImportedDeclaration(TypeDeclaration td, 
            String name, List<Type> signature, 
            boolean ellipsis) {
        for (Import i: getImports()) {
            TypeDeclaration itd = i.getTypeDeclaration();
            if (itd!=null && itd.equals(td) && 
                    !i.isAmbiguous() &&
                    i.getAlias().equals(name)) {
                //in case of an overloaded member, this will
                //be the "abstraction", so search for the 
                //correct overloaded version
                Declaration d = i.getDeclaration();
                return d.getContainer()
                        .getMember(d.getName(), 
                                signature, ellipsis);
            }
        }
        return null;
    }
    
    public Map<String, DeclarationWithProximity> 
    getMatchingImportedDeclarations(String startingWith, 
            int proximity) {
        Map<String, DeclarationWithProximity> result = 
                new TreeMap<String, DeclarationWithProximity>();
        for (Import i: new ArrayList<Import>(getImports())) {
            if (i.getAlias()!=null && 
                    !i.isAmbiguous() &&
                    isNameMatching(startingWith, i)) {
                Declaration d = i.getDeclaration();
                if (isToplevelImport(i, d)) {
                    result.put(i.getAlias(), 
                            new DeclarationWithProximity(i, 
                                    proximity));
                }
            }
        }
        return result;
    }
    
    public Map<String, DeclarationWithProximity> 
    getMatchingImportedDeclarations(TypeDeclaration td, 
            String startingWith, int proximity) {
        Map<String, DeclarationWithProximity> result = 
                new TreeMap<String, DeclarationWithProximity>();
        for (Import i: new ArrayList<Import>(getImports())) {
            TypeDeclaration itd = i.getTypeDeclaration();
            if (i.getAlias()!=null && 
                    !i.isAmbiguous() &&
                    itd!=null && itd.equals(td) &&
                    isNameMatching(startingWith, i)) {
                result.put(i.getAlias(), 
                        new DeclarationWithProximity(i, 
                                proximity));
            }
        }
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Unit) {
            Unit that = (Unit) obj;
            return that==this ||
                    that.getPackage()
                        .equals(getPackage()) && 
                    Objects.equals(getFilename(), 
                            that.getFilename()) &&
                    Objects.equals(that.getFullPath(), 
                            getFullPath());
        }
        else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return getFullPath().hashCode();
    }
    
    private Module languageModule;
    private Package languagePackage;

    /**
     * Search for a declaration in the language module. 
     */
    public Declaration getLanguageModuleDeclaration(String name) {
        //all elements in ceylon.language are auto-imported
        //traverse all default module packages provided they 
        //have not been traversed yet
        Module languageModule = getLanguageModule();
        if (languageModule!=null && 
                languageModule.isAvailable()) {
            if ("Nothing".equals(name)) {
                return getNothingDeclaration();
            }
            if (languagePackage==null) {
                languagePackage = 
                        languageModule.getPackage(LANGUAGE_MODULE_NAME);
            }
            if (languagePackage != null) {
                Declaration d = 
                        languagePackage.getMember(name, 
                                null, false);
                if (d != null && d.isShared()) {
                    return d;
                }
            }
        }
        return null;
    }
    
    private Module getLanguageModule() {
        if (languageModule==null) {
            languageModule = 
                    getPackage().getModule()
                        .getLanguageModule();
        }
        return languageModule;
    }

    /**
     * Search for a declaration in {@code ceylon.language.meta.model} 
     */
    public Declaration getLanguageModuleModelDeclaration(String name) {
        Module languageModule = 
                getPackage().getModule()
                    .getLanguageModule();
        if (languageModule!=null && 
                languageModule.isAvailable()) {
            Package languageScope = 
                    languageModule.getPackage("ceylon.language.meta.model");
            if (languageScope!=null) {
                Declaration d = 
                        languageScope.getMember(name, 
                                null, false);
                if (d!=null && d.isShared()) {
                    return d;
                }
            }
        }
        return null;
    }
    
    /**
     * Search for a declaration in {@code ceylon.language.meta.declaration} 
     */
    public Declaration getLanguageModuleDeclarationDeclaration(String name) {
        Module languageModule = 
                getPackage().getModule()
                    .getLanguageModule();
        if (languageModule!=null && 
                languageModule.isAvailable()) {
            Package languageScope = 
                    languageModule.getPackage("ceylon.language.meta.declaration");
            if (languageScope!=null) {
                Declaration d = 
                        languageScope.getMember(name, 
                                null, false);
                if (d!=null && d.isShared()) {
                    return d;
                }
            }
        }
        return null;
    }
    
    /**
     * Search for a declaration in {@code ceylon.language.serialization} 
     */
    public Declaration getLanguageModuleSerializationDeclaration(String name) {
        Module languageModule = 
                getPackage().getModule()
                    .getLanguageModule();
        if (languageModule!=null && 
                languageModule.isAvailable()) {
            Package languageScope = 
                    languageModule.getPackage("ceylon.language.serialization");
            if (languageScope!=null) {
                Declaration d = 
                        languageScope.getMember(name, 
                                null, false);
                if (d != null && d.isShared()) {
                    return d;
                }
            }
        }
        return null;
    }
    
    public Interface getCorrespondenceDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Correspondence");
    }
    
    public Class getAnythingDeclaration() {
        return (Class) getLanguageModuleDeclaration("Anything");
    }
    
    public Class getNullDeclaration() {
        return (Class) getLanguageModuleDeclaration("Null");
    }
    
    public Value getNullValueDeclaration() {
        return (Value) getLanguageModuleDeclaration("null");
    }
    
    public Interface getEmptyDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Empty");
    }
    
    public Interface getSequenceDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Sequence");
    }
    
    public Class getObjectDeclaration() {
        return (Class) getLanguageModuleDeclaration("Object");
    }
    
    public Class getBasicDeclaration() {
        return (Class) getLanguageModuleDeclaration("Basic");
    }
    
    public Interface getIdentifiableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Identifiable");
    }
    
    public Class getThrowableDeclaration() {
        return (Class) getLanguageModuleDeclaration("Throwable");
    }
    
    public Class getErrorDeclaration() {
        return (Class) getLanguageModuleDeclaration("Error");
    }
    
    public Class getExceptionDeclaration() {
        return (Class) getLanguageModuleDeclaration("Exception");
    }
    
    public Interface getCategoryDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Category");
    }
    
    public Interface getIterableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Iterable");
    }

    public Interface getJavaIterableDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Interface) lang.getMember("Iterable", null, false);
        }
    }
    
    public Interface getJavaCollectionDeclaration() {
        Package util = getJavaUtilPackage();
        if (util==null) {
            return null;
        }
        else {
            return (Interface) util.getMember("Collection", null, false);
        }
    }
    
    public Interface getJavaListDeclaration() {
        Package lang = getJavaUtilPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Interface) lang.getMember("List", null, false);
        }
    }
    
    public Interface getJavaMapDeclaration() {
        Package lang = getJavaUtilPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Interface) lang.getMember("Map", null, false);
        }
    }

    public Interface getJavaAutoCloseableDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Interface) lang.getMember("AutoCloseable", null, false);
        }
    }
    
    protected Package getJavaLangPackage() {
        return getPackage().getModule().getPackage("java.lang");
    }
    
    protected Package getJavaUtilPackage() {
        return getPackage().getModule().getPackage("java.util");
    }
    
    public Interface getSequentialDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Sequential");
    }
    
    public Interface getListDeclaration() {
        return (Interface) getLanguageModuleDeclaration("List");
    }
    
    public Interface getCollectionDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Collection");
    }
    
    public Interface getIteratorDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Iterator");
    }
    
    public Interface getCallableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Callable");
    }
    
    public Interface getScalableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Scalable");
    }
    
    public Interface getSummableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Summable");
    }
     
    public Interface getNumericDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Numeric");
    }
    
    public Interface getIntegralDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Integral");
    }
    
    public Interface getInvertableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Invertible");
    }
    
    public Interface getExponentiableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Exponentiable");
    }
    
    public Interface getSetDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Set");
    }
    
    public TypeDeclaration getComparisonDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("Comparison");
    }
    
    public TypeDeclaration getBooleanDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("Boolean");
    }
    
    public Value getTrueValueDeclaration() {
        return (Value) getLanguageModuleDeclaration("true");
    }
    
    public Value getFalseValueDeclaration() {
        return (Value) getLanguageModuleDeclaration("false");
    }
    
    public TypeDeclaration getStringDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("String");
    }
    
    public TypeDeclaration getFloatDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("Float");
    }
    
    public TypeDeclaration getIntegerDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("Integer");
    }
    
    public TypeDeclaration getCharacterDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("Character");
    }
    
    public TypeDeclaration getByteDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("Byte");
    }
    
    public Interface getComparableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Comparable");
    }
    
    public Interface getUsableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Usable");
    }
    
    public Interface getDestroyableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Destroyable");
    }
    
    public Interface getObtainableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Obtainable");
    }
    
    public Interface getOrdinalDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Ordinal");
    }
        
    public Interface getEnumerableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Enumerable");
    }
        
    public Class getRangeDeclaration() {
        return (Class) getLanguageModuleDeclaration("Range");
    }
    
    public Class getSpanDeclaration() {
        return (Class) getLanguageModuleDeclaration("Span");
    }
    
    public Class getMeasureDeclaration() {
        return (Class) getLanguageModuleDeclaration("Measure");
    }
    
    public Class getTupleDeclaration() {
        return (Class) getLanguageModuleDeclaration("Tuple");
    }
    
    public TypeDeclaration getArrayDeclaration() {
        return (Class) getLanguageModuleDeclaration("Array");
    }
    
    public Interface getRangedDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Ranged");
    }
        
    public Class getEntryDeclaration() {
        return (Class) getLanguageModuleDeclaration("Entry");
    }
    
    Type getCallableType(Reference ref, Type rt) {
        Type result = rt;
        Declaration declaration = ref.getDeclaration();
        if (declaration instanceof Functional) {
            Functional fd = (Functional) declaration;
            List<ParameterList> pls = fd.getParameterLists();
            for (int i=pls.size()-1; i>=0; i--) {
                boolean hasSequenced = false;
                boolean atLeastOne = false;
                int firstDefaulted = -1;
                List<Parameter> ps = 
                        pls.get(i).getParameters();
                List<Type> args = 
                        new ArrayList<Type>
                            (ps.size());
                for (int j=0; j<ps.size(); j++) {
                    Parameter p = ps.get(j);
                    if (p.getModel()==null) {
                        args.add(getUnknownType());
                    }
                    else {
                        TypedReference np = 
                                ref.getTypedParameter(p);
                        Type npt = np.getType();
                        if (npt==null) {
                            args.add(getUnknownType());
                        }
                        else {
                            if (p.isDefaulted() && 
                                    firstDefaulted==-1) {
                                firstDefaulted = j;
                            }
                            if (np.getDeclaration() 
                                    instanceof Functional) {
                                args.add(getCallableType(np, npt));
                            }
                            else if (p.isSequenced()) {
                                args.add(getIteratedType(npt));
                                hasSequenced = true;
                                atLeastOne = p.isAtLeastOne();
                            }
                            else {
                                args.add(npt);
                            }
                        }
                    }
                }
                Type paramListType = 
                        getTupleType(args, 
                                hasSequenced, atLeastOne, 
                                firstDefaulted);
                result = appliedType(getCallableDeclaration(), 
                        result, paramListType);
            }
        }
        return result;
    }

    public Type getTupleType(
            List<Type> elemTypes, 
            Type variadicTailType, 
            int firstDefaulted) {
        boolean hasVariadicTail = variadicTailType!=null;
        Type result = hasVariadicTail ?
                variadicTailType : 
                getEmptyType();
        Type union = hasVariadicTail ?
                getSequentialElementType(variadicTailType) :
                getNothingType();
        return getTupleType(elemTypes, 
                false, false, 
                firstDefaulted, 
                result, union);
    }

    public Type getTupleType(
            List<Type> elemTypes, 
            boolean variadic, boolean atLeastOne, 
            int firstDefaulted) {
        return getTupleType(elemTypes, 
                variadic, atLeastOne, 
                firstDefaulted,
                getEmptyType(), 
                getNothingType());
    }

    private Type getTupleType(
            List<Type> elemTypes,
            boolean variadic, boolean atLeastOne, 
            int firstDefaulted,
            Type result, Type union) {
        int last = elemTypes.size()-1;
        for (int i=last; i>=0; i--) {
            Type elemType = elemTypes.get(i);
            union = unionType(union, elemType, this);
            if (variadic && i==last) {
                result = atLeastOne ? 
                        getSequenceType(elemType) : 
                        getSequentialType(elemType);
            }
            else {
                result = appliedType(getTupleDeclaration(), 
                        union, elemType, result);
                if (firstDefaulted>=0 && i>=firstDefaulted) {
                    result = unionType(result, 
                            getEmptyType(), this);
                }
            }
        }
        return result;
    }

    public Type getEmptyType(Type pt) {
        return pt==null ? null : 
            unionType(pt, getEmptyType(), this);
    }
    
    public Type getPossiblyEmptyType(Type pt) {
        return pt==null ? null :
            appliedType(getSequentialDeclaration(),
                    getSequentialElementType(pt));
    }
    
    public Type getOptionalType(Type pt) {
        return pt==null ? null :
            unionType(pt, getNullType(), this);
    }

    public Type getUnknownType() {
        return new UnknownType(this).getType();
    }
    
    public Type getNothingType() {
        return getType(getNothingDeclaration());
    }

    public Type getEmptyType() {
        return getType(getEmptyDeclaration());
    }
    
    public Type getAnythingType() {
        return getType(getAnythingDeclaration());
    }
    
    public Type getObjectType() {
        return getType(getObjectDeclaration());
    }
    
    public Type getIdentifiableType() {
        return getType(getIdentifiableDeclaration());
    }
    
    public Type getBasicType() {
        return getType(getBasicDeclaration());
    }

    public Type getNullType() {
        return getType(getNullDeclaration());
    }
    
    public Type getThrowableType() {
        return getType(getThrowableDeclaration());
    }
    
    public Type getExceptionType() {
        return getType(getExceptionDeclaration());
    }
    
    public Type getBooleanType() {
        return getType(getBooleanDeclaration());
    }
    
    public Type getStringType() {
        return getType(getStringDeclaration());
    }
    
    public Type getIntegerType() {
        return getType(getIntegerDeclaration());
    }
    
    public Type getFloatType() {
        return getType(getFloatDeclaration());
    }
    
    public Type getCharacterType() {
        return getType(getCharacterDeclaration());
    }
    
    public Type getByteType() {
        return getType(getByteDeclaration());
    }
    
    public Type getComparisonType() {
        return getType(getComparisonDeclaration());
    }
    
    public Type getDestroyableType() {
        return getType(getDestroyableDeclaration());
    }

    public Type getObtainableType() {
        return getType(getObtainableDeclaration());
    }
    
    public Type getJavaAutoCloseableType() {
        return getType(getJavaAutoCloseableDeclaration());
    }

    public Type getSequenceType(Type et) {
        return appliedType(getSequenceDeclaration(), et);
    }
    
    public Type getSequentialType(Type et) {
        return appliedType(getSequentialDeclaration(), et);
    }
    
    public Type getIterableType(Type et) {
        return appliedType(getIterableDeclaration(), et, 
                getNullType());
    }

    public Type getNonemptyIterableType(Type et) {
        return appliedType(getIterableDeclaration(), et, 
                getNothingType());
    }

    public Type getSetType(Type et) {
        return appliedType(getSetDeclaration(), et);
    }

    /**
     * Returns a Type corresponding to {@code Iterator<T>}
     * @param et The Type corresponding to {@code T}
     * @return The Type corresponding to {@code Iterator<T>}
     */
    public Type getIteratorType(Type et) {
        return appliedType(getIteratorDeclaration(), et);
    }

    /**
     * Returns a Type corresponding to {@code Span<T>}
     * @param rt The Type corresponding to {@code T}
     * @return The Type corresponding to {@code Span<T>}
     */
    public Type getSpanType(Type rt) {
        return appliedType(getRangeDeclaration(), rt);
    }

    /**
     * Returns a Type corresponding to {@code SizedRange<T>|[]}
     * @param rt The Type corresponding to {@code T}
     * @return The Type corresponding to {@code SizedRange<T>|[]}
     */
    public Type getMeasureType(Type rt) {
        return unionType(appliedType(getRangeDeclaration(), rt), 
                getEmptyType(), 
                this);
    }

    public Type getEntryType(Type kt, Type vt) {
        return appliedType(getEntryDeclaration(), kt, vt);
    }

    public Type getKeyType(Type type) {
        Type st = type.getSupertype(getEntryDeclaration());
        if (st!=null && 
                st.getTypeArguments().size()==2) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public Type getValueType(Type type) {
        Type st = type.getSupertype(getEntryDeclaration());
        if (st!=null && 
                st.getTypeArguments().size()==2) {
            return st.getTypeArgumentList().get(1);
        }
        else {
            return null;
        }
    }

    public Type getIteratedType(Type type) {
        Interface id = getIterableDeclaration();
        Type st = type.getSupertype(id);
        if (st!=null && 
                st.getTypeArguments().size()>0) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public Type getJavaIteratedType(Type type) {
        Interface id = getJavaIterableDeclaration();
        Type st = type.getSupertype(id);
        if (st!=null && 
                st.getTypeArguments().size()>0) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public Type getAbsentType(Type type) {
        Interface id = getIterableDeclaration();
        Type st = type.getSupertype(id);
        if (st!=null && 
                st.getTypeArguments().size()>1) {
            return st.getTypeArgumentList().get(1);
        }
        else {
            return null;
        }
    }
    
    public boolean isNonemptyIterableType(Type type) {
        Type ft = getAbsentType(type);
        return ft!=null && ft.isExactlyNothing();
    }

    public Type getSetElementType(Type type) {
        Type st = type.getSupertype(getSetDeclaration());
        if (st!=null && 
                st.getTypeArguments().size()==1) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public Type getSequentialElementType(Type type) {
        Interface sd = getSequentialDeclaration();
        Type st = type.getSupertype(sd);
        if (st!=null && 
                st.getTypeArguments().size()==1) {
            Type et = st.getTypeArgumentList().get(0);
            if (type.isTuple() && et.isUnion()) {
                //total hack to accommodate the fact that
                //tuple types are created with unsimplified
                //unions
                Unit unit = et.getDeclaration().getUnit();
                List<Type> types = new ArrayList<Type>();
                for (Type ct: et.getCaseTypes()) {
                    addToUnion(types, ct);
                }
                return union(types, unit).getType();
            }
            return et;
        }
        else {
            return null;
        }
    }

    public Type getDefiniteType(Type pt) {
        return intersectionType(getObjectType(), pt, this);
    }

    public Type getNonemptyType(Type pt) {
        Type st = 
                appliedType(getSequenceDeclaration(), 
                        getSequentialElementType(pt));
        return intersectionType(st, pt, this);
    }

    public Type getNonemptyDefiniteType(Type pt) {
        return getNonemptyType(getDefiniteType(pt));
    }
    
    public boolean isIterableType(Type pt) {
        return pt.getDeclaration()
                .inherits(getIterableDeclaration());
    }
    
    public boolean isJavaIterableType(Type pt) {
        return pt.getDeclaration()
                .inherits(getJavaIterableDeclaration());
    }
    
    public boolean isUsableType(Type pt) {
        return pt.getDeclaration()
                .inherits(getUsableDeclaration());
    }
    
    public boolean isEntryType(Type pt) {
        return pt.getDeclaration()
                .inherits(getEntryDeclaration());
    }
    
    public boolean isSequentialType(Type pt) {
        return pt.getDeclaration().isSequentialType();
    }
    
    public boolean isSequenceType(Type pt) {
        return pt.getDeclaration().isSequenceType();
    }
    
    public boolean isEmptyType(Type pt) {
        return pt.getDeclaration().isEmptyType();
    }
    
    public boolean isTupleType(Type pt) {
        return pt.getDeclaration().isTupleType();
    }
    
    public boolean isOptionalType(Type pt) {
        //must have non-empty intersection with Null
        //and non-empty intersection with Value
        return !intersectionType(getNullType(), 
                        pt, this)
                    .isNothing() &&
                !intersectionType(getObjectType(), 
                        pt, this)
                    .isNothing();
    }
    
    public boolean isPossiblyEmptyType(Type pt) {
        //must be a subtype of Sequential<Anything>
        return isSequentialType(getDefiniteType(pt)) &&
        //must have non-empty intersection with Empty
        //and non-empty intersection with Sequence<Nothing>
               !intersectionType(getEmptyType(), 
                           pt, this)
                        .isNothing() &&
               !intersectionType(getSequentialType(getNothingType()), 
                           pt, this)
                        .isNothing();
    }
    
    public boolean isCallableType(Type pt) {
        return pt!=null && 
                pt.getDeclaration()
                    .inherits(getCallableDeclaration());
    }
    
    public NothingType getNothingDeclaration() {
        return new NothingType(this);
    }
    
    public Type denotableType(Type type) {
        if (type!=null) {
            if (type.isUnion()) {
                List<Type> cts = type.getCaseTypes();
                List<Type> list = 
                        new ArrayList<Type>
                            (cts.size()+1);
                for (Type ct: cts) {
                    addToUnion(list, 
                            denotableType(ct));
                }
                return union(list, this);
            }
            if (type.isIntersection()) {
                List<Type> sts = type.getSatisfiedTypes();
                List<Type> list = 
                        new ArrayList<Type>
                            (sts.size()+1);
                for (Type st: sts) {
                    addToIntersection(list, 
                            denotableType(st), 
                            this);
                }
                return canonicalIntersection(list, this);
            }
            TypeDeclaration dec = type.getDeclaration();
            Type et = dec.getExtendedType();
            TypeDeclaration ed = 
                    et==null ? null : 
                        et.getDeclaration();
            if (dec.isOverloaded()) {
                type = type.getSupertype(ed);
            }
            if (dec instanceof Constructor) {
                return type.getSupertype(ed);
            }
            if (dec instanceof Class && dec.isAnonymous()) {
                List<Type> sts = dec.getSatisfiedTypes();
                List<Type> list = 
                        new ArrayList<Type>
                            (sts.size()+1);
                if (et!=null) {
                    TypeDeclaration etd = 
                            et.getDeclaration();
                    addToIntersection(list, 
                            type.getSupertype(etd), 
                            this);
                }
                for (Type st: sts) {
                    if (st!=null) {
                        TypeDeclaration std = 
                                st.getDeclaration();
                        addToIntersection(list, 
                                type.getSupertype(std), 
                                this);
                    }
                }
                return canonicalIntersection(list, this);
            }
            else {
                List<Type> typeArgList = 
                        type.getTypeArgumentList();
                if (typeArgList.isEmpty()) {
                    return type;
                }
                else {
                    dec = type.getDeclaration();
                    List<TypeParameter> typeParamList = 
                            dec.getTypeParameters();
                    List<Type> typeArguments = 
                            new ArrayList<Type>
                                (typeArgList.size());
                    for (int i=0; 
                            i<typeParamList.size() && 
                            i<typeArgList.size(); 
                            i++) {
                        Type at = typeArgList.get(i);
                        TypeParameter tp = 
                                typeParamList.get(i);
                        typeArguments.add(tp.isCovariant() ? 
                                denotableType(at) : at);
                    }
                    Type qt = type.getQualifyingType();
                    Type dt = dec.appliedType(qt, typeArguments);
                    dt.setUnderlyingType(type.getUnderlyingType());
                    dt.setVarianceOverrides(type.getVarianceOverrides());
                    dt.setTypeConstructor(type.isTypeConstructor());
                    dt.setTypeConstructorParameter(
                            type.getTypeConstructorParameter());
                    dt.setRaw(type.isRaw());
                    return dt;
                }
            }
        }
        else {
            return null;
        }
    }
    
    public Type nonemptyArgs(Type args) {
        return getEmptyType().isSubtypeOf(args) ? 
                getNonemptyType(args) : args;
    }
    
    public boolean isHomogeneousTuple(Type args) {
        if (args!=null) {
            Class td = getTupleDeclaration();
            Type tuple = args.getSupertype(td);
            if (tuple!=null) {
                List<Type> tal = tuple.getTypeArgumentList();
                Type elemType;
                if (tal.size()>=3) {
                    elemType = tal.get(0);
                }
                else {
                    return false;
                }
                Type emptyType = getEmptyType();
                while (true) {
                    tal = tuple.getTypeArgumentList();
                    if (tal.size()>=3) {
                        Type first = tal.get(1);
                        if (first==null) {
                            return false;
                        }
                        else if (!first.isExactly(elemType)) {
                            return false;
                        }
                        else {
                            Type rest = tal.get(2);
                            if (rest==null) {
                                return false;
                            }
                            else if (rest.isExactly(emptyType)) {
                                return true;
                            }
                            else {
                                tuple = rest.getSupertype(td);
                                if (tuple==null) {
                                    return false;
                                }
                            }
                        }
                    }
                    else {
                        return false;
                    }
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    
    /*public Type getHomogeneousTupleElementType(Type args) {
        if (args!=null) {
            Class td = getTupleDeclaration();
            Type tuple = args.getSupertype(td);
            if (tuple!=null) {
                return getSequentialElementType(tuple);
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }*/
    
    public int getHomogeneousTupleLength(Type args) {
        if (args!=null) {
            Class td = getTupleDeclaration();
            Type tuple = args.getSupertype(td);
            if (tuple!=null) {
                List<Type> tal = tuple.getTypeArgumentList();
                Type elemType;
                if (tal.size()>=1) {
                    elemType = tal.get(0);
                }
                else {
                    return -1;
                }
                int size = 0;
                Type emptyType = getEmptyType();
                while (true) {
                    size++;
                    tal = tuple.getTypeArgumentList();
                    if (tal.size()>=3) {
                        Type first = tal.get(1);
                        if (first==null) {
                            return -1;
                        }
                        else if (!first.isExactly(elemType)) {
                            return -1;
                        }
                        else {
                            Type rest = tal.get(2);
                            if (rest==null) {
                                return -1;
                            }
                            else if (rest.isExactly(emptyType)) {
                                return size;
                            }
                            else if (rest.isTuple()) {
                                tuple = rest;
                                //continue looping
                            }
                            else {
                                return -1;
                            }
                        }
                    }
                    else {
                        return -1;
                    }
                }
            }
            else {
                return -1;
            }
        }
        else {
            return -1;
        }
    }

    public List<Type> getTupleElementTypes(Type args) {
        if (args!=null) {
            /*List<Type> simpleResult = 
                    getSimpleTupleElementTypes(args, 0);
            if (simpleResult!=null) {
                return simpleResult;
            }*/
            if (isEmptyType(args)) {
                return NO_TYPE_ARGS;
            }
            Class td = getTupleDeclaration();
            Type tuple =
                    nonemptyArgs(args)
                        .getSupertype(td);
            if (tuple!=null) {
                List<Type> result = 
                        new LinkedList<Type>();
                while (true) {
                    List<Type> tal = 
                            tuple.getTypeArgumentList();
                    if (tal.size()>=3) {
                        Type first = tal.get(1);
                        if (first==null) {
                            first = getUnknownType();
                        }
                        result.add(first);
                        Type rest = tal.get(2);
                        if (rest==null) {
                            result.add(getUnknownType());
                            return result;
                        }
                        else if (isEmptyType(rest)) {
                            return result;
                        }
                        else {
                            tuple = nonemptyArgs(rest)
                                    .getSupertype(td);
                            if (tuple==null) {
                                if (isSequentialType(rest)) {
                                    //this is pretty weird: return the whole
                                    //tail type as the element of the list! 
                                    result.add(rest);
                                    return result;
                                }
                                else {
                                    result.add(getUnknownType());
                                    return result; 
                                }
                            }
                            //else continue the loop!
                        }
                    }
                    else {
                        result.add(getUnknownType());
                        return result;
                    }
                }
            }
            else if (isSequentialType(args)) {
                //this is pretty weird: return the whole
                //tail type as the element of the list! 
                return singleton(args);
            }
        }
        return singleton(getUnknownType());
    }
    
    private static List<Type> singleton(Type pt) {
        List<Type> result = 
                new ArrayList<Type>(1);
        result.add(pt);
        return result;
    }
    
    /*private List<Type> getSimpleTupleElementTypes(
            Type args, int count) {
        // can be a defaulted tuple of Empty|Tuple
        if (args.isUnion()) {
            List<Type> caseTypes = 
                    args.getCaseTypes();
            if (caseTypes == null || caseTypes.size() != 2) {
                return null;
            }
            Type caseA = caseTypes.get(0);
            Type caseB = caseTypes.get(1);
            if (!caseA.isClassOrInterface() || 
                !caseB.isClassOrInterface()) {
                    return null;
            }
            TypeDeclaration caseADecl = 
                    caseA.getDeclaration();
            TypeDeclaration caseBDecl = 
                    caseB.getDeclaration();
            String caseAName = 
                    caseADecl.getQualifiedNameString();
            String caseBName = 
                    caseBDecl.getQualifiedNameString();
            if (caseAName.equals("ceylon.language::Empty") && 
                caseBName.equals("ceylon.language::Tuple")) {
                return getSimpleTupleElementTypes(caseB, count);
            }
            if (caseBName.equals("ceylon.language::Empty") && 
                caseAName.equals("ceylon.language::Tuple")) {
                return getSimpleTupleElementTypes(caseA, count);
            }
            return null;
        }
        // can be Tuple, Empty, Sequence or Sequential
        if (!(args.isClassOrInterface())) {
            return null;
        }
        String name = 
                args.getDeclaration()
                    .getQualifiedNameString();
        if (name.equals("ceylon.language::Tuple")){
            List<Type> tal = 
                    args.getTypeArgumentList();
            Type first = tal.get(1);
            Type rest = tal.get(2);
            List<Type> ret = 
                    getSimpleTupleElementTypes(rest, count+1);
            if (ret == null)
                return null;
            ret.set(count, first);
            return ret;
        }
        if (name.equals("ceylon.language::Empty")){
            ArrayList<Type> ret = 
                    new ArrayList<Type>(count);
            for (int i=0;i<count;i++) {
                ret.add(null);
            }
            return ret;
        }
        if (name.equals("ceylon.language::Sequential")
                || name.equals("ceylon.language::Sequence")
                || name.equals("ceylon.language::Range")) {
            ArrayList<Type> ret = 
                    new ArrayList<Type>(count+1);
            for (int i=0;i<count;i++) {
                ret.add(null);
            }
            ret.add(args);
            return ret;
        }
        return null;
    }*/

    public boolean isTupleLengthUnbounded(Type args) {
        if (args!=null) {
            /*Boolean simpleTupleLengthUnbounded = 
                    isSimpleTupleLengthUnbounded(args);
            if (simpleTupleLengthUnbounded != null) {
                return simpleTupleLengthUnbounded.booleanValue();
            }*/
            if (args.isSubtypeOf(getEmptyType())) {
                return false;
            }
            //TODO: this doesn't account for the case where
            //      a tuple occurs in a union with []
            Class td = getTupleDeclaration();
            Type tst = 
                    nonemptyArgs(args)
                        .getSupertype(td);
            if (tst==null) {
                return true;
            }
            else {
                while (true) {
                    List<Type> tal = 
                            tst.getTypeArgumentList();
                    if (tal.size()>=3) {
                        Type rest = tal.get(2);
                        if (rest==null) {
                            return false;
                        }
                        else if (rest.isSubtypeOf(getEmptyType())) {
                            return false;
                        }
                        else {
                            tst = nonemptyArgs(rest)
                                    .getSupertype(td);
                            if (tst==null) {
                                return true;
                            }
                            //else continue the loop!
                        }
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    /*protected Boolean isSimpleTupleLengthUnbounded(Type args) {
        // can be a defaulted tuple of Empty|Tuple
        if (args.isUnion()) {
            List<Type> caseTypes = 
                    args.getCaseTypes();
            if (caseTypes == null || caseTypes.size() != 2) {
                return null;
            }
            Type caseA = caseTypes.get(0);
            Type caseB = caseTypes.get(1);
            if (!caseA.isClassOrInterface() || 
                !caseB.isClassOrInterface()) {
                    return null;
            }
            TypeDeclaration caseADecl = 
                    caseA.getDeclaration();
            TypeDeclaration caseBDecl = 
                    caseB.getDeclaration();
            String caseAName = 
                    caseADecl.getQualifiedNameString();
            String caseBName = 
                    caseBDecl.getQualifiedNameString();
            if (caseAName.equals("ceylon.language::Empty") && 
                caseBName.equals("ceylon.language::Tuple")) {
                return isSimpleTupleLengthUnbounded(caseB);
            }
            if (caseBName.equals("ceylon.language::Empty") && 
                caseAName.equals("ceylon.language::Tuple")) {
                return isSimpleTupleLengthUnbounded(caseA);
            }
            return null;
        }
        // can be Tuple, Empty, Sequence or Sequential
        if (!(args.isClassOrInterface())) {
            return null;
        }
        String name = 
                args.getDeclaration()
                    .getQualifiedNameString();
        if (name.equals("ceylon.language::Tuple")) {
            Type rest = 
                    args.getTypeArgumentList().get(2);
            return isSimpleTupleLengthUnbounded(rest);
        }
        if (name.equals("ceylon.language::Empty")) {
            return false;
        }
        if (name.equals("ceylon.language::Range")) {
            return true;
        }
        if (name.equals("ceylon.language::Sequential") || 
            name.equals("ceylon.language::Sequence")) {
            return true;
        }
        return null;
    }*/

    public boolean isTupleVariantAtLeastOne(Type args) {
        if (args!=null) {
            /*Boolean simpleTupleVariantAtLeastOne = 
                    isSimpleTupleVariantAtLeastOne(args);
            if (simpleTupleVariantAtLeastOne != null) {
                return simpleTupleVariantAtLeastOne.booleanValue();
            }*/
            if (getEmptyType().isSubtypeOf(args)) {
                return false;
            }
            Class td = getTupleDeclaration();
            Type tuple = 
                    nonemptyArgs(args)
                        .getSupertype(td);
            if (tuple == null) {
                return isSequenceType(args);
            }
            else {
                while (true) {
                    List<Type> tal = 
                            tuple.getTypeArgumentList();
                    if (tal.size()>=3) {
                        Type rest = tal.get(2);
                        if (rest==null) {
                            return false;
                        }
                        else if (getEmptyType()
                                .isSubtypeOf(rest)) {
                            return false;
                        }
                        else if (isSequenceType(rest) && 
                                !isTupleType(args)) {
                            return true;
                        }
                        else {
                            tuple = nonemptyArgs(rest)
                                    .getSupertype(td);
                            if (tuple==null) {
                                return isSequenceType(args);
                            }
                            //else continue the loop!
                        }
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    /*private Boolean isSimpleTupleVariantAtLeastOne(Type args) {
        // can be a defaulted tuple of Empty|Tuple
        if (args.isUnion()) {
            List<Type> caseTypes = 
                    args.getCaseTypes();
            if (caseTypes == null || caseTypes.size() != 2) {
                return null;
            }
            Type caseA = caseTypes.get(0);
            Type caseB = caseTypes.get(1);
            if (!caseA.isClassOrInterface() || 
                !caseB.isClassOrInterface()) {
                    return null;
            }
            TypeDeclaration caseADecl = 
                    caseA.getDeclaration();
            TypeDeclaration caseBDecl = 
                    caseB.getDeclaration();
            String caseAName = 
                    caseADecl.getQualifiedNameString();
            String caseBName = 
                    caseBDecl.getQualifiedNameString();
            if (caseAName.equals("ceylon.language::Empty") && 
                caseBName.equals("ceylon.language::Tuple")) {
                return isSimpleTupleVariantAtLeastOne(caseB);
            }
            if (caseBName.equals("ceylon.language::Empty") && 
                caseAName.equals("ceylon.language::Tuple")) {
                return isSimpleTupleVariantAtLeastOne(caseA);
            }
            return null;
        }
        // can be Tuple, Empty, Sequence or Sequential
        if (!(args.isClassOrInterface())) {
            return null;
        }
        String name = 
                args.getDeclaration()
                    .getQualifiedNameString();
        if (name.equals("ceylon.language::Tuple")) {
            Type rest = 
                    args.getTypeArgumentList().get(2);
            return isSimpleTupleVariantAtLeastOne(rest);
        }
        if (name.equals("ceylon.language::Empty")) {
            return false;
        }
        if (name.equals("ceylon.language::Range")) {
            return true;
        }
        if (name.equals("ceylon.language::Sequential")) {
            return false;
        }
        if (name.equals("ceylon.language::Sequence")) {
            return true;
        }
        return null;
    }*/

    public int getTupleMinimumLength(Type args) {
        if (args!=null) {
            /*int simpleMinimumLength = 
                    getSimpleTupleMinimumLength(args);
            if (simpleMinimumLength != -1) {
                return simpleMinimumLength;
            }*/
            if (getEmptyType().isSubtypeOf(args)) {
                return 0;
            }
            Class td = getTupleDeclaration();
            Type tuple = 
                    nonemptyArgs(args)
                        .getSupertype(td);
            if (tuple == null) {
                return isSequenceType(args) ? 
                        1 : 0;
            }
            else {
                int size = 0;
                while (true) {
                    List<Type> tal = 
                            tuple.getTypeArgumentList();
                    size++;
                    if (tal.size()>=3) {
                        Type rest = tal.get(2);
                        if (rest==null) {
                            return size;
                        }
                        else if (getEmptyType()
                                .isSubtypeOf(rest)) {
                            return size;
                        }
                        else {
                            tuple = nonemptyArgs(rest)
                                    .getSupertype(td);
                            if (tuple==null) {
                                return isSequenceType(args) ? 
                                        size+1 : size;
                            }
                            //else continue the loop!
                        }
                    }
                    else {
                        return size;
                    }
                }
            }
        }
        return 0;
    }
    
    /*private int getSimpleTupleMinimumLength(Type args) {
        // can be a defaulted tuple of Empty|Tuple
        if (args.isUnion()){
            List<Type> caseTypes = 
                    args.getCaseTypes();
            if (caseTypes == null || caseTypes.size() != 2) {
                return -1;
            }
            Type caseA = caseTypes.get(0);
            Type caseB = caseTypes.get(1);
            if (!caseA.isClassOrInterface() || 
                !caseB.isClassOrInterface()) {
                    return -1;
            }
            TypeDeclaration caseADecl = 
                    caseA.getDeclaration();
            TypeDeclaration caseBDecl = 
                    caseB.getDeclaration();
            String caseAName = 
                    caseADecl.getQualifiedNameString();
            String caseBName = 
                    caseBDecl.getQualifiedNameString();
            if (caseAName.equals("ceylon.language::Empty") && 
                caseBName.equals("ceylon.language::Tuple")) {
                return 0;
            }
            if (caseBName.equals("ceylon.language::Empty") && 
                caseAName.equals("ceylon.language::Tuple")) {
                return 0;
            }
            return -1;
        }
        // can be Tuple, Empty, Sequence or Sequential
        if (!(args.isClassOrInterface())) {
            return -1;
        }
        String name = 
                args.getDeclaration()
                    .getQualifiedNameString();
        if (name.equals("ceylon.language::Tuple")) {
            Type rest = 
                    args.getTypeArgumentList().get(2);
            int ret = getSimpleTupleMinimumLength(rest);
            return ret == -1 ? -1 : ret + 1;
        }
        if (name.equals("ceylon.language::Empty")) {
            return 0;
        }
        if (name.equals("ceylon.language::Range")) {
            return 1;
        }
        if (name.equals("ceylon.language::Sequential")) {
            return 0;
        }
        if (name.equals("ceylon.language::Sequence")) {
            return 1;
        }
        return -1;
    }*/

    public List<Type> getCallableArgumentTypes(Type t) {
        Type tuple = getCallableTuple(t);
        if (tuple == null) {
            return Collections.emptyList();
        }
        else {
            return getTupleElementTypes(tuple);
        }
    }
    
    public Type getCallableTuple(Type t) {
        if (t==null) return null;
        Interface cd = getCallableDeclaration();
        Type ct = t.getSupertype(cd);
        if (ct!=null) {
            List<Type> typeArgs = 
                    ct.getTypeArgumentList();
            if (typeArgs.size()>=2) {
                return typeArgs.get(1);
            }
        }
        return null;
    }
    
    public Type getCallableReturnType(Type t) {
        if (t==null) return null;
        if (t.isNothing()) return t;
        Interface cd = getCallableDeclaration();
        Type ct = t.getSupertype(cd);
        if (ct!=null) {
            List<Type> typeArgs = ct.getTypeArgumentList();
            if (typeArgs.size()>=1) {
                return typeArgs.get(0);
            }
        }
        return null;
    }
    
    public boolean isIterableParameterType(Type t) {
        return t.getDeclaration().isIterable();
    }
    
    public TypeDeclaration 
    getLanguageModuleModelTypeDeclaration(String name) {
        return (TypeDeclaration) 
                getLanguageModuleModelDeclaration(name);
    }
    public TypeDeclaration 
    getLanguageModuleDeclarationTypeDeclaration(String name) {
        return (TypeDeclaration) 
                getLanguageModuleDeclarationDeclaration(name);
    }
    
    private final Map<String,String> modifiers = 
            new HashMap<String,String>();
    private void put(String modifier) {
        modifiers.put(modifier, modifier);
    }
    {
        put("shared");
        put("default");
        put("formal");
        put("native");
        put("actual");
        put("abstract");
        put("final");
        put("sealed");
        put("variable");
        put("late");
        put("deprecated");
        put("annotation");
        put("optional");
        put("serializable");
    }
    public Map<String, String> getModifiers() {
        return modifiers;
    }
    
    public Type getValueMetatype(TypedReference reference) {
        TypedDeclaration declaration = 
                reference.getDeclaration();
        boolean variable = declaration.isVariable();
        Type getType;
        Type setType = getNothingType();
        Type qualifyingType = reference.getQualifyingType();
        if (declaration.getTypeDeclaration() 
                instanceof Constructor) {
            getType = denotableType(reference.getType());
            if (qualifyingType!=null) {
                qualifyingType = 
                        qualifyingType.getQualifyingType();
            }            
        }
        else {
            getType = reference.getType();
            if (variable) {
                setType = getType;
            }
        }
        if (qualifyingType!=null) {
            TypeDeclaration ad = 
                    getLanguageModuleModelTypeDeclaration(
                            "Attribute");
            return appliedType(ad, qualifyingType, 
                    getType, setType);
        }
        else {
            TypeDeclaration vd = 
                    getLanguageModuleModelTypeDeclaration(
                            "Value");
            return appliedType(vd, getType, setType);
        }
    }
    
    public Type getFunctionMetatype(TypedReference reference) {
        TypedDeclaration dec = reference.getDeclaration();
        Functional fun = (Functional) dec;
        if (fun.getParameterLists().isEmpty()) {
            return null;
        }
        ParameterList fpl = fun.getFirstParameterList();
        List<Parameter> params = fpl.getParameters();
        Type parameterTuple = 
                getParameterTypesAsTupleType(params, 
                        reference);
        Type fullType = reference.getFullType();
        Type returnType = getCallableReturnType(fullType);
        if (returnType == null) {
            return null;
        }
        else {
            Type qualifyingType = 
                    reference.getQualifyingType();
            if (qualifyingType!=null) {
                TypeDeclaration md = 
                        getLanguageModuleModelTypeDeclaration(
                                "Method");
                return appliedType(md, qualifyingType, 
                        returnType, parameterTuple);
            }
            else {
                TypeDeclaration fd = 
                        getLanguageModuleModelTypeDeclaration(
                                "Function");
                return appliedType(fd, returnType, 
                        parameterTuple);
            }
        }
    }
    
    public Type getConstructorMetatype(TypedReference reference) {
        TypeDeclaration d = 
                reference.getDeclaration()
                    .getTypeDeclaration();
        if (d==null) {
            return null;
        }
        return getConstructorMetatype(reference, d);
    }

    public Type getConstructorMetatype(Type type) {
        TypeDeclaration d = type.getDeclaration();
        if (d==null) {
            return null;
        }
        return getConstructorMetatype(type, d);
    }

    private Type getConstructorMetatype(
            Reference reference, 
            TypeDeclaration d) {
        Functional f = (Functional) d;
        ParameterList fpl = f.getFirstParameterList();
        if (fpl==null) {
            return null;
        }
        List<Parameter> params = fpl.getParameters();
        Type parameterTuple = getNothingType();
        Scope scope = d.getContainer();
        if (scope instanceof Class) {
            Class c = (Class) scope;
            if (c.isClassOrInterfaceMember() || c.isToplevel()) {
                parameterTuple = 
                        getParameterTypesAsTupleType(params, 
                                reference);
            }
            else {
                parameterTuple = getNothingType();
            }
        }
        Type fullType = reference.getFullType();
        Type returnType = 
                denotableType(getCallableReturnType(fullType));
        if (returnType == null) {
            return null;
        }
        else {
            Type qualifyingType = 
                    reference.getQualifyingType();
            if (qualifyingType!=null && 
                    qualifyingType.getDeclaration()
                        .isClassOrInterfaceMember()) {
                Type qqt = qualifyingType.getQualifyingType();
                TypeDeclaration mccd = 
                        getLanguageModuleModelTypeDeclaration(
                                "MemberClassCallableConstructor");
                return appliedType(mccd, qqt, returnType, 
                        parameterTuple);
            }
            else {
                TypeDeclaration cd = 
                        getLanguageModuleModelTypeDeclaration(
                                "CallableConstructor");
                return appliedType(cd, returnType, 
                        parameterTuple);
            }
        }
    }
    
    public Type getValueConstructorMetatype(
            Reference reference) {
        Type fullType = reference.getFullType();
        Type returnType = 
                fullType;
        if (returnType == null) {
            return null;
        }
        else {
            Type qualifyingType = 
                    reference.getQualifyingType();
            if (qualifyingType!=null && 
                    qualifyingType.getDeclaration()
                        .isClassOrInterfaceMember()) {
                Type qqt = qualifyingType.getQualifyingType();
                TypeDeclaration mccd = 
                        getLanguageModuleModelTypeDeclaration(
                                "MemberClassValueConstructor");
                return appliedType(mccd, qqt, returnType);
            }
            else {
                TypeDeclaration cd = 
                        getLanguageModuleModelTypeDeclaration(
                                "ValueConstructor");
                return appliedType(cd, returnType);
            }
        }
    }
    
    public Type getClassMetatype(Type type) {
        Class c = (Class) type.getDeclaration();
        ParameterList parameterList = c.getParameterList();
        Type parameterTuple;
        if ((c.isClassOrInterfaceMember() || c.isToplevel()) &&
                parameterList!=null) {
            List<Parameter> params = 
                    parameterList.getParameters();
            parameterTuple = 
                    getParameterTypesAsTupleType(params, type);
        }
        else {
            parameterTuple = getNothingType();
        }
        Type qualifyingType = type.getQualifyingType();
        if (qualifyingType!=null) {
            TypeDeclaration mcd = 
                    getLanguageModuleModelTypeDeclaration(
                            "MemberClass");
            return appliedType(mcd, qualifyingType, 
                    type, parameterTuple);
        }
        else {
            TypeDeclaration cd = 
                    getLanguageModuleModelTypeDeclaration(
                            "Class");
            return appliedType(cd, type, 
                    parameterTuple);
        }
    }
    
    public Type getInterfaceMetatype(Type type) {
        Type qualifyingType = type.getQualifyingType();
        if (qualifyingType!=null) {
            TypeDeclaration mid = 
                    getLanguageModuleModelTypeDeclaration(
                            "MemberInterface");
            return appliedType(mid, qualifyingType, type);
        }
        else {
            TypeDeclaration id = 
                    getLanguageModuleModelTypeDeclaration(
                            "Interface");
            return appliedType(id, type);
        }
    }

    public Type getTypeMetaType(Type type) {
        if (type.isUnion()) {
            TypeDeclaration utd = 
                    getLanguageModuleModelTypeDeclaration(
                            "UnionType");
            return appliedType(utd, type);
        }
        else if (type.isIntersection()) {
            TypeDeclaration itd = 
                    getLanguageModuleModelTypeDeclaration(
                            "IntersectionType");
            return appliedType(itd, type);
        }
        else {
            TypeDeclaration td = 
                    getLanguageModuleModelTypeDeclaration(
                            "Type");
            return appliedType(td, type);
        }
    }
    
    public Type getParameterTypesAsTupleType(
            List<Parameter> params, Reference reference) {
        List<Type> paramTypes = 
                new ArrayList<Type>
                    (params.size());
        int max = params.size()-1;
        int firstDefaulted = -1;
        boolean sequenced = false;
        boolean atLeastOne = false;
        for (int i=0; i<=max; i++) {
            Parameter p = params.get(i);
            Type fullType;
            if (p.getModel() == null) {
                fullType = getUnknownType();
            }
            else {
                if (reference==null) {
                    //this special case is here because
                    //TypeArgumentInference abuses this
                    //API by passing a qualifying type
                    //which does not actually own the
                    //given parameters directly
                    fullType =
                            p.getModel()
                                .getReference()
                                .getFullType();
                }
                else {
                    fullType = 
                            reference.getTypedParameter(p)
                                .getFullType();
                }
                if (firstDefaulted<0 && p.isDefaulted()) {
                    firstDefaulted = i;
                }
                if (i==max && p.isSequenced()) {
                    sequenced = true;
                    atLeastOne = p.isAtLeastOne();
                    if (fullType!=null) {
                        fullType = getIteratedType(fullType);
                    }
                }
            }
            paramTypes.add(fullType);
        }
        return getTupleType(paramTypes, 
                sequenced, atLeastOne, 
                firstDefaulted);
    }
    
    public Type getTailType(Type sequenceType, 
            int fixedLength) {
        int i=0;
        Type tail = sequenceType;
        while (i++<fixedLength && tail!=null) {
            if (isTupleType(tail)) {
                List<Type> list = 
                        tail.getTypeArgumentList();
                if (list.size()>=3) {
                    tail = list.get(2);
                }
                else {
                    tail = null;
                }
            }
            else {
                tail = null;
            }
        }
        return tail;
    }
    
    public Type getType(TypeDeclaration td) {
        return td==null ? getUnknownType() : td.getType();
    }
    
    public Type getPackageDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("Package"));
    }
    
    public Type getModuleDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("Module"));
    }
    
    public Type getImportDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("Import"));
    }
    
    public Type getClassDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("ClassDeclaration"));
    }
    
    public Type getClassDeclarationType(Class clazz) {
        return clazz.hasConstructors() || clazz.hasEnumerated() || clazz.isAnonymous() ?
                getType(getLanguageModuleDeclarationTypeDeclaration("ClassWithConstructorsDeclaration")) :
                getType(getLanguageModuleDeclarationTypeDeclaration("ClassWithInitializerDeclaration"));
    }
    
    public Type getConstructorDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("ConstructorDeclaration"));
    }
    
    public Type getCallableConstructorDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("CallableConstructorDeclaration"));
    }
    
    public Type getValueConstructorDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("ValueConstructorDeclaration"));
    }
    
    public Type getInterfaceDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("InterfaceDeclaration"));
    }
    
    public Type getAliasDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("AliasDeclaration"));
    }
    
    public Type getTypeParameterDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("TypeParameter"));
    }
    
    public Type getFunctionDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("FunctionDeclaration"));
    }
    
    public Type getValueDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("ValueDeclaration"));
    }
    
    public TypeDeclaration getAnnotationDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("Annotation");
    }
    
    public TypeDeclaration getConstrainedAnnotationDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("ConstrainedAnnotation");
    }
    
    public TypeDeclaration getSequencedAnnotationDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("SequencedAnnotation");
    }
    
    public TypeDeclaration getOptionalAnnotationDeclaration() {
        return (TypeDeclaration) getLanguageModuleDeclaration("OptionalAnnotation");
    }
    
    public TypeDeclaration getDeclarationDeclaration() {
        return getLanguageModuleDeclarationTypeDeclaration("Declaration");
    }
    
    public TypeCache getCache() {
        Module module = getPackage().getModule();
        return module != null ? module.getCache() : null;
    }

}
