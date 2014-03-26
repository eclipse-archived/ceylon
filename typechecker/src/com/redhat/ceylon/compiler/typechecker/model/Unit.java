package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Module.LANGUAGE_MODULE_NAME;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.intersectionType;
import static com.redhat.ceylon.compiler.typechecker.model.Util.producedType;
import static com.redhat.ceylon.compiler.typechecker.model.Util.unionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.redhat.ceylon.compiler.typechecker.context.ProducedTypeCache;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;

public class Unit {

	private Package pkg;
	private List<Import> imports = new ArrayList<Import>();
	private List<Declaration> declarations = new ArrayList<Declaration>();
	private String filename;
	private List<ImportList> importLists = new ArrayList<ImportList>();
	private Set<Identifier> unresolvedReferences = new HashSet<Identifier>();
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
    
    public Set<Identifier> getUnresolvedReferences() {
        return unresolvedReferences;
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
        return "Unit[" + filename + "]";
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
            		i.getDeclaration().equals(dec)) {
                return i.getAlias();
            }
        }
		return dec.getName();
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
                return d.getContainer().getMember(d.getName(), signature, ellipsis);
            }
        }
        return null;
    }
    
    /**
     * Search the imports of a compilation unit
     * for the named member declaration.
     */
    public Declaration getImportedDeclaration(TypeDeclaration td, String name, 
            List<ProducedType> signature, boolean ellipsis) {
        for (Import i: getImports()) {
            TypeDeclaration itd = i.getTypeDeclaration();
			if (itd!=null && itd.equals(td) && 
					!i.isAmbiguous() &&
            		i.getAlias().equals(name)) {
                //in case of an overloaded member, this will
                //be the "abstraction", so search for the 
                //correct overloaded version
                Declaration d = i.getDeclaration();
                return d.getContainer().getMember(d.getName(), signature, ellipsis);
            }
        }
        return null;
    }
    
    public Map<String, DeclarationWithProximity> getMatchingImportedDeclarations(String startingWith, int proximity) {
    	Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
        for (Import i: new ArrayList<Import>(getImports())) {
            if (i.getAlias()!=null && !i.isAmbiguous() &&
                    i.getAlias().toLowerCase().startsWith(startingWith.toLowerCase())) {
                result.put(i.getAlias(), new DeclarationWithProximity(i, proximity));
            }
        }
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Unit) {
            Unit that = (Unit) obj;
            return that.getPackage().equals(getPackage())
                    && that.getFullPath().equals(getFullPath());
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
        //traverse all default module packages provided they have not been traversed yet
        Module languageModule = getLanguageModule();
        if (languageModule!=null && languageModule.isAvailable()) {
            if ("Nothing".equals(name)) {
                return getNothingDeclaration();
            }
            if (languagePackage==null) {
                languagePackage = languageModule.getPackage(LANGUAGE_MODULE_NAME);
            }
            if (languagePackage != null) {
                Declaration d = languagePackage.getMember(name, null, false);
                if (d != null && d.isShared()) {
                    return d;
                }
            }
        }
        return null;
    }
    
    private Module getLanguageModule() {
        if (languageModule==null) {
            languageModule = getPackage().getModule().getLanguageModule();
        }
        return languageModule;
    }

    /**
     * Search for a declaration in {@code ceylon.language.model} 
     */
    public Declaration getLanguageModuleModelDeclaration(String name) {
        Module languageModule = getPackage().getModule().getLanguageModule();
        if ( languageModule != null && languageModule.isAvailable() ) {
            Package languageScope = languageModule.getPackage("ceylon.language.meta.model");
            if (languageScope != null) {
                Declaration d = languageScope.getMember(name, null, false);
                if (d != null && d.isShared()) {
                    return d;
                }
            }
        }
        return null;
    }
    
    /**
     * Search for a declaration in {@code ceylon.language.model.declaration} 
     */
    public Declaration getLanguageModuleDeclarationDeclaration(String name) {
        Module languageModule = getPackage().getModule().getLanguageModule();
        if ( languageModule != null && languageModule.isAvailable() ) {
            Package languageScope = languageModule.getPackage("ceylon.language.meta.declaration");
            if (languageScope != null) {
                Declaration d = languageScope.getMember(name, null, false);
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
        return (Interface) getLanguageModuleDeclaration("Invertable");
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
    
    public Interface getComparableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Comparable");
    }
    
    public Interface getCloseableDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Closeable");
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
        
    public Class getRangeDeclaration() {
        return (Class) getLanguageModuleDeclaration("Range");
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
    	/*if (isTypeUnknown(ref.getType())) {
    		//special case for forward reference to member
    		//with inferred type TODO: something better
    		return new UnknownType(this).getType();
    	}*/
    	ProducedType result = rt;
    	if (ref.getDeclaration() instanceof Functional) {
    	    List<ParameterList> pls = ((Functional) ref.getDeclaration()).getParameterLists();
            for (int i=pls.size()-1; i>=0; i--) {
        	    boolean hasSequenced = false;
        	    boolean atLeastOne = false;
        	    int firstDefaulted = -1;
    	    	List<Parameter> ps = pls.get(i).getParameters();
                List<ProducedType> args = new ArrayList<ProducedType>(ps.size());
				for (int j=0; j<ps.size(); j++) {
					Parameter p = ps.get(j);
    	    		ProducedTypedReference np = ref.getTypedParameter(p);
    	    		ProducedType npt = np.getType();
    	    		if (npt==null) {
    	    			args.add(new UnknownType(this).getType());
    	    		}
    	    		else {
    	    			if (p.isDefaulted() && 
    	    					firstDefaulted==-1) {
    	    				firstDefaulted = j;
    	    			}
    	    			if (np.getDeclaration() instanceof Functional) {
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
    	    	result = producedType(getCallableDeclaration(), result,
    	    			getTupleType(args, hasSequenced, atLeastOne, firstDefaulted));
    	    }
    	}
    	return result;
    }
    
    public ProducedType getTupleType(List<ProducedType> elemTypes, 
    		boolean variadic, boolean atLeastOne, int firstDefaulted) {
    	ProducedType result = getType(getEmptyDeclaration());
    	ProducedType union = getType(getNothingDeclaration());
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
    				        getType(getEmptyDeclaration()), this);
    			}
    		}
    	}
    	return result;
    }
    
    public ProducedType getEmptyType(ProducedType pt) {
        return pt==null ? null :
            unionType(pt, getType(getEmptyDeclaration()), this);
        /*else if (isEmptyType(pt)) {
            //Null|Null|T == Null|T
            return pt;
        }
        else if (pt.getDeclaration() instanceof NothingType) {
            //Null|0 == Null
            return getEmptyDeclaration().getType();
        }
        else {
            UnionType ut = new UnionType();
            List<ProducedType> types = new ArrayList<ProducedType>();
            addToUnion(types,getEmptyDeclaration().getType());
            addToUnion(types,pt);
            ut.setCaseTypes(types);
            return ut.getType();
        }*/
    }
    
    public ProducedType getPossiblyNoneType(ProducedType pt) {
        return pt==null ? null :
            unionType(pt, producedType(getSequentialDeclaration(),
                    getType(getAnythingDeclaration())), this);
    }
    
    public ProducedType getOptionalType(ProducedType pt) {
        return pt==null ? null :
            unionType(pt, getType(getNullDeclaration()), this);
        /*else if (isOptionalType(pt)) {
            //Null|Null|T == Null|T
            return pt;
        }
        else if (pt.getDeclaration() instanceof NothingType) {
            //Null|0 == Null
            return getNullDeclaration().getType();
        }
        else {
            UnionType ut = new UnionType();
            List<ProducedType> types = new ArrayList<ProducedType>();
            addToUnion(types,getNullDeclaration().getType());
            addToUnion(types,pt);
            ut.setCaseTypes(types);
            return ut.getType();
        }*/
    }
    
    public ProducedType getSequenceType(ProducedType et) {
        return producedType(getSequenceDeclaration(), et);
    }
    
    public ProducedType getSequentialType(ProducedType et) {
        return producedType(getSequentialDeclaration(), et);
    }
    
    public ProducedType getIterableType(ProducedType et) {
        return producedType(getIterableDeclaration(), et, 
                getType(getNullDeclaration()));
    }

    public ProducedType getNonemptyIterableType(ProducedType et) {
        return producedType(getIterableDeclaration(), et, 
        		getNothingDeclaration().getType());
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
        return Util.producedType(getIteratorDeclaration(), et);
    }

    /**
     * Returns a ProducedType corresponding to {@code Range<T>}
     * @param rt The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Range<T>}
     */
    public ProducedType getRangeType(ProducedType rt) {
        return Util.producedType(getRangeDeclaration(), rt);
    }

    public ProducedType getEntryType(ProducedType kt, ProducedType vt) {
        return producedType(getEntryDeclaration(), kt, vt);
    }

    public ProducedType getKeyType(ProducedType type) {
        ProducedType st = type.getSupertype(getEntryDeclaration());
        if (st!=null && st.getTypeArguments().size()==2) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public ProducedType getValueType(ProducedType type) {
        ProducedType st = type.getSupertype(getEntryDeclaration());
        if (st!=null && st.getTypeArguments().size()==2) {
            return st.getTypeArgumentList().get(1);
        }
        else {
            return null;
        }
    }

    public ProducedType getIteratedType(ProducedType type) {
        ProducedType st = type.getSupertype(getIterableDeclaration());
        if (st!=null && st.getTypeArguments().size()>0) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public ProducedType getFirstType(ProducedType type) {
        ProducedType st = type.getSupertype(getIterableDeclaration());
        if (st!=null && st.getTypeArguments().size()>1) {
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
        ProducedType st = type.getSupertype(getSetDeclaration());
        if (st!=null && st.getTypeArguments().size()==1) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public ProducedType getSequentialElementType(ProducedType type) {
        ProducedType st = type.getSupertype(getSequentialDeclaration());
        if (st!=null && st.getTypeArguments().size()==1) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }

    public ProducedType getDefiniteType(ProducedType pt) {
        return intersectionType(getType(getObjectDeclaration()), 
                pt, pt.getDeclaration().getUnit());
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
        return pt.getSupertype(getEntryDeclaration())!=null;
    }
    
    public boolean isIterableType(ProducedType pt) {
        return pt.getSupertype(getIterableDeclaration())!=null;
    }
    
    public boolean isSequentialType(ProducedType pt) {
        return pt.getSupertype(getSequentialDeclaration())!=null;
    }
    
    public boolean isSequenceType(ProducedType pt) {
        return pt.getSupertype(getSequenceDeclaration())!=null;
    }
    
    public boolean isEmptyType(ProducedType pt) {
        return pt.getSupertype(getEmptyDeclaration())!=null;
    }
    
    public boolean isOptionalType(ProducedType pt) {
        //must have non-empty intersection with Null
        //and non-empty intersection with Value
        return !intersectionType(getType(getNullDeclaration()), pt, this)
                    .isNothing() &&
                !intersectionType(getType(getObjectDeclaration()), pt, this)
                    .isNothing();
    }
    
    public boolean isPossiblyEmptyType(ProducedType pt) {
        //must be a subtype of Sequential<Anything>
        return isSequentialType(getDefiniteType(pt)) &&
        //must have non-empty intersection with Empty
        //and non-empty intersection with Sequence<Nothing>
               !intersectionType(getType(getEmptyDeclaration()), pt, this)
                        .isNothing() &&
               !intersectionType(getSequenceType(getNothingDeclaration().getType()), pt, this)
                        .isNothing();
    }
    
    public boolean isCallableType(ProducedType pt) {
    	return pt!=null && pt.getSupertype(getCallableDeclaration())!=null;
    }
    
    public NothingType getNothingDeclaration() {
        return new NothingType(this);
    }
    
    public ProducedType denotableType(ProducedType pt) {
    	if (pt!=null) {
    		TypeDeclaration d = pt.getDeclaration();
    		if (d instanceof Functional) {
    			if (((Functional) d).isOverloaded()) {
    				pt = pt.getSupertype(d.getExtendedTypeDeclaration());
    			}
    		}
    		if (d!=null && d.isAnonymous() ) {
    			ClassOrInterface etd = d.getExtendedTypeDeclaration();
    			List<TypeDeclaration> stds = d.getSatisfiedTypeDeclarations();
    			List<ProducedType> list = new ArrayList<ProducedType>(stds.size()+1);
    			addToIntersection(list, pt.getSupertype(etd), this);
    			for (TypeDeclaration td: stds) {
    				addToIntersection(list, pt.getSupertype(td), this);
    			}
    			IntersectionType it = new IntersectionType(this);
    			it.setSatisfiedTypes(list);
    			return it.getType();
    		}
    		else {
    			return pt;
    		}
    	}
    	else {
    		return null;
    	}
    }
    
    public ProducedType nonemptyArgs(ProducedType args) {
        return isPossiblyEmptyType(args) ? 
                getNonemptyType(args) : args;
    }
    
    public List<ProducedType> getTupleElementTypes(ProducedType args) {
        if (args!=null) {
            List<ProducedType> simpleResult = getSimpleTupleElementTypes(args, 0);
            if(simpleResult != null){
                return simpleResult;
            }
            ProducedType tst = nonemptyArgs(args).getSupertype(getTupleDeclaration());
            if (tst!=null) {
                List<ProducedType> tal = tst.getTypeArgumentList();
                if (tal.size()>=3) {
                    List<ProducedType> result = getTupleElementTypes(tal.get(2));
                    result.add(0, tal.get(1));
                    return result;
                }
            }
            else if (isEmptyType(args)) {
                return new LinkedList<ProducedType>();
            }
            else if (isSequentialType(args)) {
            	if (!(args.getDeclaration() instanceof TypeParameter)) {
            		LinkedList<ProducedType> sequenced = new LinkedList<ProducedType>();
            		sequenced.add(args);
            		return sequenced;
            	}
            }
        }
        LinkedList<ProducedType> unknown = new LinkedList<ProducedType>();
        unknown.add(new UnknownType(this).getType());
        return unknown;
    }
    
    private List<ProducedType> getSimpleTupleElementTypes(ProducedType args, int count) {
        // can be a defaulted tuple of Empty|Tuple
        TypeDeclaration declaration = args.getDeclaration();
        if(declaration instanceof UnionType){
            List<ProducedType> caseTypes = declaration.getCaseTypes();
            if(caseTypes == null || caseTypes.size() != 2)
                return null;
            ProducedType caseA = caseTypes.get(0);
            TypeDeclaration caseADecl = caseA.getDeclaration();
            ProducedType caseB = caseTypes.get(1);
            TypeDeclaration caseBDecl = caseB.getDeclaration();
            if(caseADecl instanceof ClassOrInterface == false
                    || caseBDecl instanceof ClassOrInterface == false)
                return null;
            if(caseADecl.getQualifiedNameString().equals("ceylon.language::Empty")
                    && caseBDecl.getQualifiedNameString().equals("ceylon.language::Tuple"))
                return getSimpleTupleElementTypes(caseB, count);
            if(caseBDecl.getQualifiedNameString().equals("ceylon.language::Empty")
                    && caseADecl.getQualifiedNameString().equals("ceylon.language::Tuple"))
                return getSimpleTupleElementTypes(caseA, count);
            return null;
        }
        // can be Tuple, Empty, Sequence or Sequential
        if(declaration instanceof ClassOrInterface == false)
            return null;
        String name = declaration.getQualifiedNameString();
        if(name.equals("ceylon.language::Tuple")){
            ProducedType first = args.getTypeArgumentList().get(1);
            ProducedType rest = args.getTypeArgumentList().get(2);
            List<ProducedType> ret = getSimpleTupleElementTypes(rest, count+1);
            if(ret == null)
                return null;
            ret.set(count, first);
            return ret;
        }
        if(name.equals("ceylon.language::Empty")){
            ArrayList<ProducedType> ret = new ArrayList<ProducedType>(count);
            for(int i=0;i<count;i++)
                ret.add(null);
            return ret;
        }
        if(name.equals("ceylon.language::Sequential")
                || name.equals("ceylon.language::Sequence")){
            ArrayList<ProducedType> ret = new ArrayList<ProducedType>(count+1);
            for(int i=0;i<count;i++)
                ret.add(null);
            ret.add(args);
            return ret;
        }
        return null;
    }

    public boolean isTupleLengthUnbounded(ProducedType args) {
        if (args!=null) {
            Boolean simpleTupleLengthUnbounded = isSimpleTupleLengthUnbounded(args);
            if(simpleTupleLengthUnbounded != null)
                return simpleTupleLengthUnbounded.booleanValue();
            ProducedType tst = nonemptyArgs(args).getSupertype(getTupleDeclaration());
            if (tst!=null) {
                List<ProducedType> tal = tst.getTypeArgumentList();
                if (tal.size()>=3) {
                    return isTupleLengthUnbounded(tal.get(2));
                }
            }
            else if (isEmptyType(args)) {
                return false;
            }
            else if (isSequentialType(args)) {
                return !(args.getDeclaration() instanceof TypeParameter);
            }
        }
        return false;
    }
    
    private Boolean isSimpleTupleLengthUnbounded(ProducedType args) {
        // can be a defaulted tuple of Empty|Tuple
        TypeDeclaration declaration = args.getDeclaration();
        if(declaration instanceof UnionType){
            List<ProducedType> caseTypes = declaration.getCaseTypes();
            if(caseTypes == null || caseTypes.size() != 2)
                return null;
            ProducedType caseA = caseTypes.get(0);
            TypeDeclaration caseADecl = caseA.getDeclaration();
            ProducedType caseB = caseTypes.get(1);
            TypeDeclaration caseBDecl = caseB.getDeclaration();
            if(caseADecl instanceof ClassOrInterface == false
                    || caseBDecl instanceof ClassOrInterface == false)
                return null;
            if(caseADecl.getQualifiedNameString().equals("ceylon.language::Empty")
                    && caseBDecl.getQualifiedNameString().equals("ceylon.language::Tuple"))
                return isSimpleTupleLengthUnbounded(caseB);
            if(caseBDecl.getQualifiedNameString().equals("ceylon.language::Empty")
                    && caseADecl.getQualifiedNameString().equals("ceylon.language::Tuple"))
                return isSimpleTupleLengthUnbounded(caseA);
            return null;
        }
        // can be Tuple, Empty, Sequence or Sequential
        if(declaration instanceof ClassOrInterface == false)
            return null;
        String name = declaration.getQualifiedNameString();
        if(name.equals("ceylon.language::Tuple")){
            ProducedType rest = args.getTypeArgumentList().get(2);
            return isSimpleTupleLengthUnbounded(rest);
        }
        if(name.equals("ceylon.language::Empty")){
            return false;
        }
        if(name.equals("ceylon.language::Sequential")
           || name.equals("ceylon.language::Sequence")){
            return true;
        }
        return null;
    }

    public boolean isTupleVariantAtLeastOne(ProducedType args) {
        if (args!=null) {
            Boolean simpleTupleVariantAtLeastOne = isSimpleTupleVariantAtLeastOne(args);
            if(simpleTupleVariantAtLeastOne != null)
                return simpleTupleVariantAtLeastOne.booleanValue();
            ProducedType tst = nonemptyArgs(args).getSupertype(getTupleDeclaration());
            if (tst!=null) {
                List<ProducedType> tal = tst.getTypeArgumentList();
                if (tal.size()>=3) {
                    return isTupleVariantAtLeastOne(tal.get(2));
                }
            }
            else if (isEmptyType(args)) {
                return false;
            }
            else if (isSequenceType(args)) {
                return true;
            }
            else if (isSequentialType(args)) {
                return false;
            }
        }
        return false;
    }
    
    private Boolean isSimpleTupleVariantAtLeastOne(ProducedType args) {
        // can be a defaulted tuple of Empty|Tuple
        TypeDeclaration declaration = args.getDeclaration();
        if(declaration instanceof UnionType){
            List<ProducedType> caseTypes = declaration.getCaseTypes();
            if(caseTypes == null || caseTypes.size() != 2)
                return null;
            ProducedType caseA = caseTypes.get(0);
            TypeDeclaration caseADecl = caseA.getDeclaration();
            ProducedType caseB = caseTypes.get(1);
            TypeDeclaration caseBDecl = caseB.getDeclaration();
            if(caseADecl instanceof ClassOrInterface == false
                    || caseBDecl instanceof ClassOrInterface == false)
                return null;
            if(caseADecl.getQualifiedNameString().equals("ceylon.language::Empty")
                    && caseBDecl.getQualifiedNameString().equals("ceylon.language::Tuple"))
                return isSimpleTupleVariantAtLeastOne(caseB);
            if(caseBDecl.getQualifiedNameString().equals("ceylon.language::Empty")
                    && caseADecl.getQualifiedNameString().equals("ceylon.language::Tuple"))
                return isSimpleTupleVariantAtLeastOne(caseA);
            return null;
        }
        // can be Tuple, Empty, Sequence or Sequential
        if(declaration instanceof ClassOrInterface == false)
            return null;
        String name = declaration.getQualifiedNameString();
        if(name.equals("ceylon.language::Tuple")){
            ProducedType rest = args.getTypeArgumentList().get(2);
            return isSimpleTupleVariantAtLeastOne(rest);
        }
        if(name.equals("ceylon.language::Empty")){
            return false;
        }
        if(name.equals("ceylon.language::Sequential")){
            return false;
        }
        if(name.equals("ceylon.language::Sequence")){
            return true;
        }
        return null;
    }

    public int getTupleMinimumLength(ProducedType args) {
        if (args!=null) {
            int simpleMinimumLength = getSimpleTupleMinimumLength(args);
            if(simpleMinimumLength != -1)
                return simpleMinimumLength;
            if (isPossiblyEmptyType(args)) {
                return 0;
            }
            ProducedType tst = nonemptyArgs(args).getSupertype(getTupleDeclaration());
            if (tst!=null) {
                List<ProducedType> tal = tst.getTypeArgumentList();
                if (tal.size()>=3) {
                    return getTupleMinimumLength(tal.get(2))+1;
                }
            }
            else if (isEmptyType(args)) {
                return 0;
            }
            else if (isSequenceType(args)) {
                return 1;
            }
            else if (isSequentialType(args)) {
                return 0;
            }
        }
        return 0;
    }
    
    private int getSimpleTupleMinimumLength(ProducedType args) {
        // can be a defaulted tuple of Empty|Tuple
        TypeDeclaration declaration = args.getDeclaration();
        if(declaration instanceof UnionType){
            List<ProducedType> caseTypes = declaration.getCaseTypes();
            if(caseTypes == null || caseTypes.size() != 2)
                return -1;
            ProducedType caseA = caseTypes.get(0);
            TypeDeclaration caseADecl = caseA.getDeclaration();
            ProducedType caseB = caseTypes.get(1);
            TypeDeclaration caseBDecl = caseB.getDeclaration();
            if(caseADecl instanceof ClassOrInterface == false
                    || caseBDecl instanceof ClassOrInterface == false)
                return -1;
            if(caseADecl.getQualifiedNameString().equals("ceylon.language::Empty")
                    && caseBDecl.getQualifiedNameString().equals("ceylon.language::Tuple"))
                return 0;
            if(caseBDecl.getQualifiedNameString().equals("ceylon.language::Empty")
                    && caseADecl.getQualifiedNameString().equals("ceylon.language::Tuple"))
                return 0;
            return -1;
        }
        // can be Tuple, Empty, Sequence or Sequential
        if(declaration instanceof ClassOrInterface == false)
            return -1;
        String name = declaration.getQualifiedNameString();
        if(name.equals("ceylon.language::Tuple")){
            ProducedType rest = args.getTypeArgumentList().get(2);
            int ret = getSimpleTupleMinimumLength(rest);
            if(ret == -1)
                return -1;
            return ret + 1;
        }
        if(name.equals("ceylon.language::Empty")){
            return 0;
        }
        if(name.equals("ceylon.language::Sequential")){
            return 0;
        }
        if(name.equals("ceylon.language::Sequence")){
            return 1;
        }
        return -1;
    }

    public List<ProducedType> getCallableArgumentTypes(ProducedType t){
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
        ProducedType ct = t.getSupertype(getCallableDeclaration());
        if (ct!=null) {
            List<ProducedType> typeArgs = ct.getTypeArgumentList();
            if (typeArgs.size()>=2) {
                return typeArgs.get(1);
            }
        }
        return null;
    }
    
    public ProducedType getCallableReturnType(ProducedType t){
        if (t==null) return null;
        ProducedType ct = t.getSupertype(getCallableDeclaration());
        if (ct!=null) {
            List<ProducedType> typeArgs = ct.getTypeArgumentList();
            if (typeArgs.size()>=1) {
                return typeArgs.get(0);
            }
        }
        return null;
    }
    
    public boolean isIterableParameterType(ProducedType t) {
    	return t.getDeclaration() instanceof Interface &&
    			t.getDeclaration().equals(getIterableDeclaration());
    }
    
    public TypeDeclaration getLanguageModuleModelTypeDeclaration(String name) {
        return (TypeDeclaration) getLanguageModuleModelDeclaration(name);
    }
    public TypeDeclaration getLanguageModuleDeclarationTypeDeclaration(String name) {
        return (TypeDeclaration) getLanguageModuleDeclarationDeclaration(name);
    }
    
    private final Map<String,String> modifiers = new HashMap<String,String>();
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
        put("variable");
        put("late");
        put("deprecated");
        put("annotation");
        put("optional");
    }
    public Map<String, String> getModifiers() {
        return modifiers;
    }
    
    public ProducedType getValueMetatype(ProducedTypedReference pr) {
        boolean variable = pr.getDeclaration().isVariable();
        ProducedType getType = denotableType(pr.getType());
        ProducedType setType = variable ? denotableType(pr.getType()) : new NothingType(this).getType();
        if (pr.getQualifyingType() != null) {
            return producedType(getLanguageModuleModelTypeDeclaration("Attribute"),
                    pr.getQualifyingType(), getType, setType);
        }
        else {
            return producedType(getLanguageModuleModelTypeDeclaration("Value"),
                    getType, setType);
        }
    }
    
    public ProducedType getFunctionMetatype(ProducedTypedReference pr) {
        Functional f = (Functional) pr.getDeclaration();
        ParameterList fpl = f.getParameterLists().get(0);
        ProducedType parameterTuple = getParameterTypesAsTupleType(fpl.getParameters(), pr);
        ProducedType returnType = getCallableReturnType(pr.getFullType());
        if (returnType == null) {
            return null;
        }
        else {
            if (pr.getQualifyingType() != null) {
                return producedType(getLanguageModuleModelTypeDeclaration("Method"),
                        pr.getQualifyingType(), returnType, parameterTuple);
            }
            else {
                return producedType(getLanguageModuleModelTypeDeclaration("Function"),
                        returnType, parameterTuple);
            }
        }
    }
    
    public ProducedType getClassMetatype(ProducedType literalType) {
        Class c = (Class) literalType.getDeclaration();
        ParameterList parameterList = c.getParameterList();
        ProducedType parameterTuple;
        if (c.isClassOrInterfaceMember()||c.isToplevel()) {
        	parameterTuple = getParameterTypesAsTupleType(parameterList != null ? 
        			    parameterList.getParameters() : Collections.<Parameter>emptyList(), 
        		    literalType);
        }
        else {
        	parameterTuple = new NothingType(this).getType();
        }
        if (literalType.getDeclaration().isMember()) {
            return producedType(getLanguageModuleModelTypeDeclaration("MemberClass"),
                    literalType.getQualifyingType(), literalType, parameterTuple);
        }
        else {
            return producedType(getLanguageModuleModelTypeDeclaration("Class"),
                    literalType, parameterTuple);
        }
    }
    
    public ProducedType getInterfaceMetatype(ProducedType literalType) {
        if (literalType.getDeclaration().isMember()) {
            return producedType(getLanguageModuleModelTypeDeclaration("MemberInterface"),
                    literalType.getQualifyingType(), literalType);
        }
        else {
            return producedType(getLanguageModuleModelTypeDeclaration("Interface"), literalType);
        }
    }

    public ProducedType getTypeMetaType(ProducedType literalType) {
        TypeDeclaration declaration = literalType.getDeclaration();
        if (declaration instanceof UnionType) {
            return producedType(getLanguageModuleModelTypeDeclaration("UnionType"), literalType);
        }
        else if (declaration instanceof IntersectionType) {
            return producedType(getLanguageModuleModelTypeDeclaration("IntersectionType"), literalType);
        }
        else {
            return producedType(getLanguageModuleModelTypeDeclaration("Type"), literalType);
        }
    }
    
    public ProducedType getParameterTypesAsTupleType(List<Parameter> params, 
            ProducedReference pr) {
        List<ProducedType> paramTypes = new ArrayList<ProducedType>(params.size());
        int max = params.size()-1;
        int firstDefaulted = -1;
        boolean sequenced = false;
        boolean atLeastOne = false;
        for (int i=0; i<=max; i++) {
            Parameter p = params.get(i);
            ProducedType ft = pr.getTypedParameter(p).getFullType();
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
            paramTypes.add(ft);
        }
        return getTupleType(paramTypes, sequenced, atLeastOne, 
                firstDefaulted);
    }
    
    public ProducedType getType(TypeDeclaration td) {
        return td==null ? new UnknownType(this).getType() : td.getType();
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
    
    public ProducedType getValueDeclarationType(TypedDeclaration value) {
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

    public ProducedTypeCache getCache() {
        Module module = getPackage().getModule();
        return module != null ? module.getCache() : null;
    }
    
}
