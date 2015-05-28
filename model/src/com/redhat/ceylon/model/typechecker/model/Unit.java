package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.Module.LANGUAGE_MODULE_NAME;
import static com.redhat.ceylon.model.typechecker.model.Util.NO_TYPE_ARGS;
import static com.redhat.ceylon.model.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.model.typechecker.model.Util.addToUnion;
import static com.redhat.ceylon.model.typechecker.model.Util.intersectionType;
import static com.redhat.ceylon.model.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.model.typechecker.model.Util.isOverloadedVersion;
import static com.redhat.ceylon.model.typechecker.model.Util.isToplevelAnonymousClass;
import static com.redhat.ceylon.model.typechecker.model.Util.isToplevelClassConstructor;
import static com.redhat.ceylon.model.typechecker.model.Util.producedType;
import static com.redhat.ceylon.model.typechecker.model.Util.unionType;

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

import com.redhat.ceylon.model.typechecker.context.ProducedTypeCache;

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
        for (Import i: getImports()) {
            if (!i.isAmbiguous() &&
            		i.getDeclaration().equals(getAbstraction(dec))) {
                return i.getAlias();
            }
        }
		return dec.getName();
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
     * Search the imports of a compilation unit
     * for the named toplevel declaration.
     */
    public Declaration getImportedDeclaration(String name, 
            List<ProducedType> signature, boolean ellipsis) {
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

    static boolean isToplevelImport(Import i, Declaration d) {
        return d.isToplevel() || 
            d.isStaticallyImportable() ||
            isToplevelClassConstructor(i.getTypeDeclaration(), d) ||
            isToplevelAnonymousClass(i.getTypeDeclaration());
    }
    
    /**
     * Search the imports of a compilation unit
     * for the named member declaration.
     */
    public Declaration getImportedDeclaration(TypeDeclaration td, 
            String name, List<ProducedType> signature, 
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
    getMatchingImportedDeclarations(String startingWith, int proximity) {
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
    
    ProducedType getCallableType(ProducedReference ref, ProducedType rt) {
    	ProducedType result = rt;
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
                List<ProducedType> args = 
                        new ArrayList<ProducedType>
                            (ps.size());
                for (int j=0; j<ps.size(); j++) {
                    Parameter p = ps.get(j);
                    if (p.getModel()==null) {
                        args.add(getUnknownType());
                    }
                    else {
                        ProducedTypedReference np = 
                                ref.getTypedParameter(p);
                        ProducedType npt = np.getType();
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
    	    	ProducedType paramListType = 
    	    	        getTupleType(args, 
    	    	                hasSequenced, atLeastOne, 
    	    	                firstDefaulted);
                result = producedType(getCallableDeclaration(), 
                        result, paramListType);
    	    }
    	}
    	return result;
    }

    public ProducedType getTupleType(
            List<ProducedType> elemTypes, 
            ProducedType variadicTailType, 
            int firstDefaulted) {
        boolean hasVariadicTail = variadicTailType!=null;
        ProducedType result = hasVariadicTail ?
                variadicTailType : 
                getEmptyType();
        ProducedType union = hasVariadicTail ?
                getSequentialElementType(variadicTailType) :
                getNothingType();
        return getTupleType(elemTypes, 
                false, false, 
                firstDefaulted, 
                result, union);
    }

    public ProducedType getTupleType(
            List<ProducedType> elemTypes, 
    		boolean variadic, boolean atLeastOne, 
    		int firstDefaulted) {
    	return getTupleType(elemTypes, 
    	        variadic, atLeastOne, 
    	        firstDefaulted,
                getEmptyType(), 
                getNothingType());
    }

    private ProducedType getTupleType(
            List<ProducedType> elemTypes,
            boolean variadic, boolean atLeastOne, 
            int firstDefaulted,
            ProducedType result, ProducedType union) {
        int last = elemTypes.size()-1;
    	for (int i=last; i>=0; i--) {
    		ProducedType elemType = elemTypes.get(i);
    		union = unionType(union, elemType, this);
    		if (variadic && i==last) {
    			result = atLeastOne ? 
    					getSequenceType(elemType) : 
    					getSequentialType(elemType);
    		}
    		else {
    			result = producedType(getTupleDeclaration(), 
    					union, elemType, result);
    			if (firstDefaulted>=0 && i>=firstDefaulted) {
    				result = unionType(result, 
    				        getEmptyType(), this);
    			}
    		}
    	}
    	return result;
    }

    public ProducedType getEmptyType(ProducedType pt) {
        return pt==null ? null : 
            unionType(pt, getEmptyType(), this);
    }
    
    public ProducedType getPossiblyEmptyType(ProducedType pt) {
        return pt==null ? null :
            producedType(getSequentialDeclaration(),
                    getSequentialElementType(pt));
    }
    
    public ProducedType getOptionalType(ProducedType pt) {
        return pt==null ? null :
            unionType(pt, getNullType(), this);
    }

    public ProducedType getUnknownType() {
        return new UnknownType(this).getType();
    }
    
    public ProducedType getNothingType() {
        return getType(getNothingDeclaration());
    }

    public ProducedType getEmptyType() {
        return getType(getEmptyDeclaration());
    }
    
    public ProducedType getAnythingType() {
        return getType(getAnythingDeclaration());
    }
    
    public ProducedType getObjectType() {
        return getType(getObjectDeclaration());
    }
    
    public ProducedType getIdentifiableType() {
        return getType(getIdentifiableDeclaration());
    }
    
    public ProducedType getNullType() {
        return getType(getNullDeclaration());
    }
    
    public ProducedType getThrowableType() {
        return getType(getThrowableDeclaration());
    }
    
    public ProducedType getExceptionType() {
        return getType(getExceptionDeclaration());
    }
    
    public ProducedType getBooleanType() {
        return getType(getBooleanDeclaration());
    }
    
    public ProducedType getIntegerType() {
        return getType(getIntegerDeclaration());
    }
    
    public ProducedType getComparisonType() {
        return getType(getComparisonDeclaration());
    }
    
    public ProducedType getDestroyableType() {
        return getType(getDestroyableDeclaration());
    }

    public ProducedType getObtainableType() {
        return getType(getObtainableDeclaration());
    }
    
    public ProducedType getSequenceType(ProducedType et) {
        return producedType(getSequenceDeclaration(), et);
    }
    
    public ProducedType getSequentialType(ProducedType et) {
        return producedType(getSequentialDeclaration(), et);
    }
    
    public ProducedType getIterableType(ProducedType et) {
        return producedType(getIterableDeclaration(), et, 
                getNullType());
    }

    public ProducedType getNonemptyIterableType(ProducedType et) {
        return producedType(getIterableDeclaration(), et, 
                getNothingType());
    }

    public ProducedType getSetType(ProducedType et) {
        return producedType(getSetDeclaration(), et);
    }

    /**
     * Returns a ProducedType corresponding to {@code Iterator<T>}
     * @param et The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Iterator<T>}
     */
    public ProducedType getIteratorType(ProducedType et) {
        return producedType(getIteratorDeclaration(), et);
    }

    /**
     * Returns a ProducedType corresponding to {@code Span<T>}
     * @param rt The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Span<T>}
     */
    public ProducedType getSpanType(ProducedType rt) {
        return producedType(getRangeDeclaration(), rt);
    }

    /**
     * Returns a ProducedType corresponding to {@code SizedRange<T>|[]}
     * @param rt The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code SizedRange<T>|[]}
     */
    public ProducedType getMeasureType(ProducedType rt) {
        return unionType(producedType(getRangeDeclaration(), rt), 
        		getEmptyType(), 
        		this);
    }

    public ProducedType getEntryType(ProducedType kt, ProducedType vt) {
        return producedType(getEntryDeclaration(), kt, vt);
    }

    public ProducedType getKeyType(ProducedType type) {
        ProducedType st = 
                type.getSupertype(getEntryDeclaration());
        if (st!=null && 
                st.getTypeArguments().size()==2) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public ProducedType getValueType(ProducedType type) {
        ProducedType st = 
                type.getSupertype(getEntryDeclaration());
        if (st!=null && 
                st.getTypeArguments().size()==2) {
            return st.getTypeArgumentList().get(1);
        }
        else {
            return null;
        }
    }

    public ProducedType getIteratedType(ProducedType type) {
        ProducedType st = 
                type.getSupertype(getIterableDeclaration());
        if (st!=null && 
                st.getTypeArguments().size()>0) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public ProducedType getFirstType(ProducedType type) {
        ProducedType st = 
                type.getSupertype(getIterableDeclaration());
        if (st!=null && 
                st.getTypeArguments().size()>1) {
            return st.getTypeArgumentList().get(1);
        }
        else {
            return null;
        }
    }
    
    public boolean isNonemptyIterableType(ProducedType type) {
    	ProducedType ft = getFirstType(type);
    	return ft!=null && ft.isNothing();
    }

    public ProducedType getSetElementType(ProducedType type) {
        ProducedType st = 
                type.getSupertype(getSetDeclaration());
        if (st!=null && 
                st.getTypeArguments().size()==1) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public ProducedType getSequentialElementType(ProducedType type) {
        ProducedType st = 
                type.getSupertype(getSequentialDeclaration());
        if (st!=null && 
                st.getTypeArguments().size()==1) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public ProducedType getDefiniteType(ProducedType pt) {
        return intersectionType(getObjectType(), pt, this);
        /*if (pt.getDeclaration().equals(getAnythingDeclaration())) {
            return getObjectDeclaration().getType();
        }
        else {
            return pt.minus(getNullDeclaration());
        }*/
    }

    public ProducedType getNonemptyType(ProducedType pt) {
        return intersectionType(producedType(getSequenceDeclaration(), 
                getSequentialElementType(pt)), pt, 
                pt.getDeclaration().getUnit());
        /*if (pt.getDeclaration().equals(getAnythingDeclaration())) {
            return getObjectDeclaration().getType();
        }
        else {
            return pt.minus(getNullDeclaration());
        }*/
    }

    public ProducedType getNonemptyDefiniteType(ProducedType pt) {
        return getNonemptyType(getDefiniteType(pt));
    }
    
    public boolean isEntryType(ProducedType pt) {
        return pt.getDeclaration()
                .inherits(getEntryDeclaration());
    }
    
    public boolean isIterableType(ProducedType pt) {
        return pt.getDeclaration()
                .inherits(getIterableDeclaration());
    }
    
    public boolean isUsableType(ProducedType pt) {
        return pt.getDeclaration()
                .inherits(getUsableDeclaration());
    }
    
    public boolean isSequentialType(ProducedType pt) {
        return pt.getDeclaration()
                .inherits(getSequentialDeclaration());
    }
    
    public boolean isSequenceType(ProducedType pt) {
        return pt.getDeclaration()
                .inherits(getSequenceDeclaration());
    }
    
    public boolean isEmptyType(ProducedType pt) {
        return pt.getDeclaration()
                .inherits(getEmptyDeclaration());
    }
    
    public boolean isTupleType(ProducedType pt) {
        return pt.getDeclaration()
                .inherits(getTupleDeclaration());
    }
    
    public boolean isOptionalType(ProducedType pt) {
        //must have non-empty intersection with Null
        //and non-empty intersection with Value
        return !intersectionType(getNullType(), 
                        pt, this)
                    .isNothing() &&
                !intersectionType(getType(getObjectDeclaration()), 
                        pt, this)
                    .isNothing();
    }
    
    public boolean isPossiblyEmptyType(ProducedType pt) {
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
    
    public boolean isCallableType(ProducedType pt) {
    	return pt!=null && 
    	        pt.getDeclaration()
    	            .inherits(getCallableDeclaration());
    }
    
    public NothingType getNothingDeclaration() {
        return new NothingType(this);
    }
    
    public ProducedType denotableType(ProducedType type) {
    	if (type!=null) {
    		if (type.isUnion()) {
    		    List<ProducedType> cts = 
                        type.getCaseTypes();
                List<ProducedType> list = 
                        new ArrayList<ProducedType>
                            (cts.size()+1);
                for (ProducedType ct: cts) {
                    addToUnion(list, 
                            denotableType(ct));
                }
                UnionType ut = new UnionType(this);
                ut.setCaseTypes(list);
                return ut.getType();
    		}
            if (type.isIntersection()) {
                List<ProducedType> sts = 
                        type.getSatisfiedTypes();
                List<ProducedType> list = 
                        new ArrayList<ProducedType>
                            (sts.size()+1);
                for (ProducedType st: sts) {
                    addToIntersection(list, 
                            denotableType(st), 
                            this);
                }
                IntersectionType it = 
                        new IntersectionType(this);
                it.setSatisfiedTypes(list);
                return it.canonicalize().getType();
            }
            TypeDeclaration dec = type.getDeclaration();
            ProducedType et = dec.getExtendedType();
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
    			List<ProducedType> sts = 
    			        dec.getSatisfiedTypes();
    			List<ProducedType> list = 
    			        new ArrayList<ProducedType>
    			            (sts.size()+1);
    			if (et!=null) {
    			    TypeDeclaration etd = 
    			            et.getDeclaration();
    			    addToIntersection(list, 
    			            type.getSupertype(etd), 
    			            this);
    			}
    			for (ProducedType st: sts) {
    			    if (st!=null) {
                        TypeDeclaration std = 
                                st.getDeclaration();
    			        addToIntersection(list, 
    			                type.getSupertype(std), 
    			                this);
    			    }
    			}
    			IntersectionType it = 
    			        new IntersectionType(this);
    			it.setSatisfiedTypes(list);
    			return it.canonicalize().getType();
    		}
    		else {
                List<ProducedType> typeArgList = 
                        type.getTypeArgumentList();
                if (typeArgList.isEmpty()) {
                    return type;
                }
                else {
                    dec = type.getDeclaration();
                    List<TypeParameter> typeParamList = 
                            dec.getTypeParameters();
                    List<ProducedType> typeArguments = 
                            new ArrayList<ProducedType>
                                (typeArgList.size());
                    for (int i=0; 
                            i<typeParamList.size() && 
                            i<typeArgList.size(); 
                            i++) {
                        ProducedType at = 
                                typeArgList.get(i);
                        TypeParameter tp = 
                                typeParamList.get(i);
                        typeArguments.add(tp.isCovariant() ? 
                                denotableType(at) : at);
                    }
                    ProducedType qt = type.getQualifyingType();
                    ProducedType dt = 
                            dec.getProducedType(qt, 
                                    typeArguments);
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
    
    public ProducedType nonemptyArgs(ProducedType args) {
        return getEmptyType().isSubtypeOf(args) ? 
                getNonemptyType(args) : args;
    }
    
    public boolean isHomogeneousTuple(ProducedType args) {
        if (args!=null) {
            Class td = getTupleDeclaration();
            ProducedType tuple = args.getSupertype(td);
            if (tuple!=null) {
                List<ProducedType> tal = 
                        tuple.getTypeArgumentList();
                ProducedType elemType;
                if (tal.size()>=3) {
                    elemType = tal.get(0);
                }
                else {
                    return false;
                }
                ProducedType emptyType = getEmptyType();
                while (true) {
                    tal = tuple.getTypeArgumentList();
                    if (tal.size()>=3) {
                        ProducedType first = tal.get(1);
                        if (first==null) {
                            return false;
                        }
                        else if (!first.isExactly(elemType)) {
                            return false;
                        }
                        else {
                            ProducedType rest = tal.get(2);
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

    public int getHomogeneousTupleLength(ProducedType args) {
        if (args!=null) {
            Class td = getTupleDeclaration();
            ProducedType tuple = args.getSupertype(td);
            if (tuple!=null) {
                List<ProducedType> tal = 
                        tuple.getTypeArgumentList();
                ProducedType elemType;
                if (tal.size()>=1) {
                    elemType = tal.get(0);
                }
                else {
                    return -1;
                }
                int size = 0;
                ProducedType emptyType = getEmptyType();
                while (true) {
                    size++;
                    tal = tuple.getTypeArgumentList();
                    if (tal.size()>=3) {
                        ProducedType first = tal.get(1);
                        if (first==null) {
                            return -1;
                        }
                        else if (!first.isExactly(elemType)) {
                            return -1;
                        }
                        else {
                            ProducedType rest = tal.get(2);
                            if (rest==null) {
                                return -1;
                            }
                            else if (rest.isExactly(emptyType)) {
                                return size;
                            }
                            else {
                                tuple = rest.getSupertype(td);
                                if (tuple==null) {
                                    return -1;
                                }
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

    public List<ProducedType> getTupleElementTypes(ProducedType args) {
        if (args!=null) {
            /*List<ProducedType> simpleResult = 
                    getSimpleTupleElementTypes(args, 0);
            if (simpleResult!=null) {
                return simpleResult;
            }*/
            if (isEmptyType(args)) {
                return NO_TYPE_ARGS;
            }
            Class td = getTupleDeclaration();
            ProducedType tuple =
                    nonemptyArgs(args)
                        .getSupertype(td);
            if (tuple!=null) {
                List<ProducedType> result = 
                        new LinkedList<ProducedType>();
                while (true) {
                    List<ProducedType> tal = 
                            tuple.getTypeArgumentList();
                    if (tal.size()>=3) {
                        ProducedType first = tal.get(1);
                        if (first==null) {
                            first = getUnknownType();
                        }
                        result.add(first);
                        ProducedType rest = tal.get(2);
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
    
    private static List<ProducedType> singleton(ProducedType pt) {
        List<ProducedType> result = 
                new ArrayList<ProducedType>(1);
        result.add(pt);
        return result;
    }
    
    /*private List<ProducedType> getSimpleTupleElementTypes(
            ProducedType args, int count) {
        // can be a defaulted tuple of Empty|Tuple
        if (args.isUnion()) {
            List<ProducedType> caseTypes = 
                    args.getCaseTypes();
            if (caseTypes == null || caseTypes.size() != 2) {
                return null;
            }
            ProducedType caseA = caseTypes.get(0);
            ProducedType caseB = caseTypes.get(1);
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
            List<ProducedType> tal = 
                    args.getTypeArgumentList();
            ProducedType first = tal.get(1);
            ProducedType rest = tal.get(2);
            List<ProducedType> ret = 
                    getSimpleTupleElementTypes(rest, count+1);
            if (ret == null)
                return null;
            ret.set(count, first);
            return ret;
        }
        if (name.equals("ceylon.language::Empty")){
            ArrayList<ProducedType> ret = 
                    new ArrayList<ProducedType>(count);
            for (int i=0;i<count;i++) {
                ret.add(null);
            }
            return ret;
        }
        if (name.equals("ceylon.language::Sequential")
                || name.equals("ceylon.language::Sequence")
                || name.equals("ceylon.language::Range")) {
            ArrayList<ProducedType> ret = 
                    new ArrayList<ProducedType>(count+1);
            for (int i=0;i<count;i++) {
                ret.add(null);
            }
            ret.add(args);
            return ret;
        }
        return null;
    }*/

    public boolean isTupleLengthUnbounded(ProducedType args) {
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
            ProducedType tst = 
                    nonemptyArgs(args)
                        .getSupertype(td);
            if (tst==null) {
                return true;
            }
            else {
                while (true) {
                    List<ProducedType> tal = 
                            tst.getTypeArgumentList();
                    if (tal.size()>=3) {
                        ProducedType rest = tal.get(2);
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
    
    /*protected Boolean isSimpleTupleLengthUnbounded(ProducedType args) {
        // can be a defaulted tuple of Empty|Tuple
        if (args.isUnion()) {
            List<ProducedType> caseTypes = 
                    args.getCaseTypes();
            if (caseTypes == null || caseTypes.size() != 2) {
                return null;
            }
            ProducedType caseA = caseTypes.get(0);
            ProducedType caseB = caseTypes.get(1);
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
            ProducedType rest = 
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

    public boolean isTupleVariantAtLeastOne(ProducedType args) {
        if (args!=null) {
            /*Boolean simpleTupleVariantAtLeastOne = 
                    isSimpleTupleVariantAtLeastOne(args);
            if (simpleTupleVariantAtLeastOne != null) {
                return simpleTupleVariantAtLeastOne.booleanValue();
            }*/
            if (getEmptyType().isSubtypeOf(args)) {
                return false;
            }
            ProducedType snt = 
                    getSequenceType(getNothingType());
            if (snt.isSubtypeOf(args)) {
                return true;
            }
            Class td = getTupleDeclaration();
            ProducedType tuple = 
                    nonemptyArgs(args)
                        .getSupertype(td);
            if (tuple == null) {
                return false;
            }
            else {
                while (true) {
                    List<ProducedType> tal = 
                            tuple.getTypeArgumentList();
                    if (tal.size()>=3) {
                        ProducedType rest = tal.get(2);
                        if (rest==null) {
                            return false;
                        }
                        else if (getEmptyType().isSubtypeOf(rest)) {
                            return false;
                        }
                        else if (snt.isSubtypeOf(rest)) {
                            return true;
                        }
                        else {
                            tuple = nonemptyArgs(rest)
                                    .getSupertype(td);
                            if (tuple==null) {
                                return false;
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
    
    /*private Boolean isSimpleTupleVariantAtLeastOne(ProducedType args) {
        // can be a defaulted tuple of Empty|Tuple
        if (args.isUnion()) {
            List<ProducedType> caseTypes = 
                    args.getCaseTypes();
            if (caseTypes == null || caseTypes.size() != 2) {
                return null;
            }
            ProducedType caseA = caseTypes.get(0);
            ProducedType caseB = caseTypes.get(1);
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
            ProducedType rest = 
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

    public int getTupleMinimumLength(ProducedType args) {
        if (args!=null) {
            /*int simpleMinimumLength = 
                    getSimpleTupleMinimumLength(args);
            if (simpleMinimumLength != -1) {
                return simpleMinimumLength;
            }*/
            if (getEmptyType().isSubtypeOf(args)) {
                return 0;
            }
            ProducedType snt = 
                    getSequenceType(getNothingType());
            if (snt.isSubtypeOf(args)) {
                return 1;
            }
            Class td = getTupleDeclaration();
            ProducedType tuple = 
                    nonemptyArgs(args)
                        .getSupertype(td);
            if (tuple == null) {
                return 0;
            }
            else {
                int size = 0;
                while (true) {
                    List<ProducedType> tal = 
                            tuple.getTypeArgumentList();
                    size++;
                    if (tal.size()>=3) {
                        ProducedType rest = tal.get(2);
                        if (rest==null) {
                            return size;
                        }
                        else if (getEmptyType().isSubtypeOf(rest)) {
                            return size;
                        }
                        else if (snt.isSubtypeOf(rest)) {
                            return size+1;
                        }
                        else {
                            tuple = nonemptyArgs(rest)
                                    .getSupertype(td);
                            if (tuple==null) {
                                return size;
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
    
    /*private int getSimpleTupleMinimumLength(ProducedType args) {
        // can be a defaulted tuple of Empty|Tuple
        if (args.isUnion()){
            List<ProducedType> caseTypes = 
                    args.getCaseTypes();
            if (caseTypes == null || caseTypes.size() != 2) {
                return -1;
            }
            ProducedType caseA = caseTypes.get(0);
            ProducedType caseB = caseTypes.get(1);
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
            ProducedType rest = 
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

    public List<ProducedType> getCallableArgumentTypes(ProducedType t) {
        ProducedType tuple = getCallableTuple(t);
        if (tuple == null) {
            return Collections.emptyList();
        }
        else {
            return getTupleElementTypes(tuple);
        }
    }
    
    public ProducedType getCallableTuple(ProducedType t) {
        if (t==null) return null;
        ProducedType ct = 
                t.getSupertype(getCallableDeclaration());
        if (ct!=null) {
            List<ProducedType> typeArgs = 
                    ct.getTypeArgumentList();
            if (typeArgs.size()>=2) {
                return typeArgs.get(1);
            }
        }
        return null;
    }
    
    public ProducedType getCallableReturnType(ProducedType t) {
        if (t==null) return null;
        if (t.isNothing()) return t;
        ProducedType ct = 
                t.getSupertype(getCallableDeclaration());
        if (ct!=null) {
            List<ProducedType> typeArgs = 
                    ct.getTypeArgumentList();
            if (typeArgs.size()>=1) {
                return typeArgs.get(0);
            }
        }
        return null;
    }
    
    public boolean isIterableParameterType(ProducedType t) {
    	TypeDeclaration dec = t.getDeclaration();
        return dec instanceof Interface &&
    			dec.equals(getIterableDeclaration());
    }
    
    public TypeDeclaration getLanguageModuleModelTypeDeclaration
            (String name) {
        return (TypeDeclaration) 
                getLanguageModuleModelDeclaration(name);
    }
    public TypeDeclaration getLanguageModuleDeclarationTypeDeclaration
            (String name) {
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
    
    public ProducedType getValueMetatype(ProducedTypedReference pr) {
        boolean variable = pr.getDeclaration().isVariable();
        ProducedType getType = pr.getType();
        ProducedType setType = variable ? 
                pr.getType() : getNothingType();
        ProducedType qualifyingType = 
                pr.getQualifyingType();
        if (qualifyingType!=null) {
            TypeDeclaration ad = 
                    getLanguageModuleModelTypeDeclaration(
                            "Attribute");
            return producedType(ad, qualifyingType, 
                    getType, setType);
        }
        else {
            TypeDeclaration vd = 
                    getLanguageModuleModelTypeDeclaration(
                            "Value");
            return producedType(vd, getType, setType);
        }
    }
    
    public ProducedType getFunctionMetatype(ProducedTypedReference pr) {
        TypedDeclaration d = pr.getDeclaration();
        Functional f = (Functional) d;
        if (f.getParameterLists().isEmpty()) {
            return null;
        }
        ParameterList fpl = f.getParameterLists().get(0);
        List<Parameter> params = fpl.getParameters();
        ProducedType parameterTuple = 
                getParameterTypesAsTupleType(params, pr);
        ProducedType returnType = 
                getCallableReturnType(pr.getFullType());
        if (returnType == null) {
            return null;
        }
        else {
            ProducedType qualifyingType = 
                    pr.getQualifyingType();
            if (qualifyingType!=null) {
                TypeDeclaration md = 
                        getLanguageModuleModelTypeDeclaration(
                                "Method");
                return producedType(md, qualifyingType, 
                        returnType, parameterTuple);
            }
            else {
                TypeDeclaration fd = 
                        getLanguageModuleModelTypeDeclaration(
                                "Function");
                return producedType(fd, returnType, 
                        parameterTuple);
            }
        }
    }
    
    public ProducedType getConstructorMetatype(ProducedType pr) {
        TypeDeclaration d = pr.getDeclaration();
        Functional f = (Functional) d;
        if (f.getParameterLists().isEmpty()) {
            return null;
        }
        ParameterList fpl = f.getParameterLists().get(0);
        List<Parameter> params = fpl.getParameters();
        ProducedType parameterTuple = 
                getParameterTypesAsTupleType(params, pr);
        ProducedType returnType = 
                getCallableReturnType(pr.getFullType());
        if (returnType == null) {
            return null;
        }
        else {
            ProducedType qt = pr.getQualifyingType();
            if (qt!=null && 
                    !qt.getDeclaration().isToplevel()) {
                ProducedType qqt = qt.getQualifyingType();
                TypeDeclaration mccd = 
                        getLanguageModuleModelTypeDeclaration(
                                "MemberClassConstructor");
                return producedType(mccd, qqt, returnType, 
                        parameterTuple);
            }
            else {
                TypeDeclaration cd = 
                        getLanguageModuleModelTypeDeclaration(
                                "Constructor");
                return producedType(cd, returnType, 
                        parameterTuple);
            }
        }
    }
    
    public ProducedType getClassMetatype(ProducedType literalType) {
        Class c = (Class) literalType.getDeclaration();
        ParameterList parameterList = c.getParameterList();
        ProducedType parameterTuple;
        if ((c.isClassOrInterfaceMember() || c.isToplevel()) &&
                parameterList!=null) {
        	parameterTuple = 
        	        getParameterTypesAsTupleType(
        	                parameterList.getParameters(), 
        	                literalType);
        }
        else {
        	parameterTuple = getNothingType();
        }
        ProducedType qualifyingType = 
                literalType.getQualifyingType();
        if (qualifyingType!=null) {
            TypeDeclaration mcd = 
                    getLanguageModuleModelTypeDeclaration(
                            "MemberClass");
            return producedType(mcd, qualifyingType, 
                    literalType, parameterTuple);
        }
        else {
            TypeDeclaration cd = 
                    getLanguageModuleModelTypeDeclaration(
                            "Class");
            return producedType(cd, literalType, 
                    parameterTuple);
        }
    }
    
    public ProducedType getInterfaceMetatype(ProducedType literalType) {
        ProducedType qualifyingType = 
                literalType.getQualifyingType();
        if (qualifyingType!=null) {
            TypeDeclaration mid = 
                    getLanguageModuleModelTypeDeclaration(
                            "MemberInterface");
            return producedType(mid, qualifyingType, 
                    literalType);
        }
        else {
            TypeDeclaration id = 
                    getLanguageModuleModelTypeDeclaration(
                            "Interface");
            return producedType(id, literalType);
        }
    }

    public ProducedType getTypeMetaType(ProducedType literalType) {
        if (literalType.isUnion()) {
            TypeDeclaration utd = 
                    getLanguageModuleModelTypeDeclaration(
                            "UnionType");
            return producedType(utd, literalType);
        }
        else if (literalType.isIntersection()) {
            TypeDeclaration itd = 
                    getLanguageModuleModelTypeDeclaration(
                            "IntersectionType");
            return producedType(itd, literalType);
        }
        else {
            TypeDeclaration td = 
                    getLanguageModuleModelTypeDeclaration(
                            "Type");
            return producedType(td, literalType);
        }
    }
    
    public ProducedType getParameterTypesAsTupleType(List<Parameter> params, 
            ProducedReference pr) {
        List<ProducedType> paramTypes = 
                new ArrayList<ProducedType>
                    (params.size());
        int max = params.size()-1;
        int firstDefaulted = -1;
        boolean sequenced = false;
        boolean atLeastOne = false;
        for (int i=0; i<=max; i++) {
            Parameter p = params.get(i);
            ProducedType ft;
            if (p.getModel() == null) {
                ft = getUnknownType();
            }
            else {
                ft = pr.getTypedParameter(p).getFullType();
                if (firstDefaulted<0 && p.isDefaulted()) {
                    firstDefaulted = i;
                }
                if (i==max && p.isSequenced()) {
                    sequenced = true;
                    atLeastOne = p.isAtLeastOne();
                    if (ft!=null) {
                        ft = getIteratedType(ft);
                    }
                }
            }
            paramTypes.add(ft);
        }
        return getTupleType(paramTypes, 
                sequenced, atLeastOne, 
                firstDefaulted);
    }
    
    public ProducedType getType(TypeDeclaration td) {
        return td==null ?
                getUnknownType() :
                td.getType();
    }
    
    public ProducedType getPackageDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("Package"));
    }
    
    public ProducedType getModuleDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("Module"));
    }
    
    public ProducedType getImportDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("Import"));
    }
    
    public ProducedType getClassDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("ClassDeclaration"));
    }
    
    public ProducedType getClassDeclarationType(Class clazz) {
        return clazz.hasConstructors() ?
                getType(getLanguageModuleDeclarationTypeDeclaration("ClassWithConstructorsDeclaration")) :
                getType(getLanguageModuleDeclarationTypeDeclaration("ClassWithInitializerDeclaration"));
    }
    
    public ProducedType getConstructorDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("ConstructorDeclaration"));
    }
    
    public ProducedType getInterfaceDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("InterfaceDeclaration"));
    }
    
    public ProducedType getAliasDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("AliasDeclaration"));
    }
    
    public ProducedType getTypeParameterDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("TypeParameter"));
    }
    
    public ProducedType getFunctionDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("FunctionDeclaration"));
    }
    
    public ProducedType getValueDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("ValueDeclaration"));
    }
    
    public ProducedType getValueDeclarationType(TypedDeclaration value) {
        return !(value instanceof Value) || ((Value) value).isTransient() ? 
                getValueDeclarationType() :
                getType(getLanguageModuleDeclarationTypeDeclaration("ReferenceDeclaration"));
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
    
    public ProducedTypeCache getCache() {
        Module module = getPackage().getModule();
        return module != null ? module.getCache() : null;
    }

}
