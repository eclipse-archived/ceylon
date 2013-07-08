package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.intersectionType;
import static com.redhat.ceylon.compiler.typechecker.model.Util.producedType;
import static com.redhat.ceylon.compiler.typechecker.model.Util.unionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
                    && that.getFilename().equals(getFilename());
        }
        else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return getFilename().hashCode();
    }

    /**
     * Search for a declaration in the language module. 
     */
    public Declaration getLanguageModuleDeclaration(String name) {
        //all elements in ceylon.language are auto-imported
        //traverse all default module packages provided they have not been traversed yet
        Module languageModule = getPackage().getModule().getLanguageModule();
        if ( languageModule != null && languageModule.isAvailable() ) {
            if ("Nothing".equals(name)) {
                return getNothingDeclaration();
            }
            Package languageScope = languageModule.getPackage(Module.LANGUAGE_MODULE_NAME);
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
     * Search for a declaration in {@code ceylon.language.metamodel} 
     */
    public Declaration getLanguageModuleMetamodelDeclaration(String name) {
        //all elements in ceylon.language are auto-imported
        //traverse all default module packages provided they have not been traversed yet
        Module languageModule = getPackage().getModule().getLanguageModule();
        if ( languageModule != null && languageModule.isAvailable() ) {
            Package languageScope = languageModule.getPackage("ceylon.language.metamodel");
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
     * Search for a declaration in {@code ceylon.language.metamodel.declaration} 
     */
    public Declaration getLanguageModuleMetamodelDeclarationDeclaration(String name) {
        //all elements in ceylon.language are auto-imported
        //traverse all default module packages provided they have not been traversed yet
        Module languageModule = getPackage().getModule().getLanguageModule();
        if ( languageModule != null && languageModule.isAvailable() ) {
            Package languageScope = languageModule.getPackage("ceylon.language.metamodel.declaration");
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
        	    int firstDefaulted = -1;
    	        List<ProducedType> args = new ArrayList<ProducedType>();
    	    	List<Parameter> ps = pls.get(i).getParameters();
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
    	    			}
    	    			else {
    	    				args.add(npt);
    	    			}
    	    		}
    	    	}
    	    	result = producedType(getCallableDeclaration(), result,
    	    			getTupleType(args, hasSequenced, false, firstDefaulted));
    	    }
    	}
    	return result;
    }
    
    public ProducedType getTupleType(List<ProducedType> elemTypes, 
    		boolean variadic, boolean atLeastOne, int firstDefaulted) {
    	ProducedType result = getEmptyDeclaration().getType();
    	ProducedType union = getNothingDeclaration().getType();
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
    						getEmptyDeclaration().getType(), this);
    			}
    		}
    	}
    	return result;
    }
    
    public ProducedType getEmptyType(ProducedType pt) {
        return pt==null ? null :
            unionType(pt, getEmptyDeclaration().getType(), this);
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
                    getAnythingDeclaration().getType()), this);
    }
    
    public ProducedType getOptionalType(ProducedType pt) {
        return pt==null ? null :
            unionType(pt, getNullDeclaration().getType(), this);
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
        		getNullDeclaration().getType());
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
        return intersectionType(getObjectDeclaration().getType(), 
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
        return !intersectionType(getNullDeclaration().getType(), pt, this)
                        .isNothing() &&
               !intersectionType(getObjectDeclaration().getType(), pt, this)
                        .isNothing();
    }
    
    public boolean isPossiblyEmptyType(ProducedType pt) {
        //must be a subtype of Sequential<Anything>
        return isSequentialType(getDefiniteType(pt)) &&
        //must have non-empty intersection with Empty
        //and non-empty intersection with Sequence<Nothing>
               !intersectionType(getEmptyDeclaration().getType(), pt, this)
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
        if ( pt!=null && pt.getDeclaration()!=null &&
                pt.getDeclaration().isAnonymous() ) {
            List<ProducedType> list = new ArrayList<ProducedType>();
            addToIntersection(list, pt.getSupertype(pt.getDeclaration().getExtendedTypeDeclaration()), this);
            for (TypeDeclaration td: pt.getDeclaration().getSatisfiedTypeDeclarations()) {
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

    public ProducedType nonemptyArgs(ProducedType args) {
        return isPossiblyEmptyType(args) ? 
                getNonemptyType(args) : args;
    }

    public List<ProducedType> getTupleElementTypes(ProducedType args) {
        if (args!=null) {
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
    
    public boolean isTupleLengthUnbounded(ProducedType args) {
        if (args!=null) {
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
    
    public boolean isTupleVariantAtLeastOne(ProducedType args) {
        if (args!=null) {
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
    
    public int getTupleMinimumLength(ProducedType args) {
        if (args!=null) {
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
            else if (isSequentialType(args)) {
                return 0;
            }
        }
        return 0;
    }

    public List<ProducedType> getCallableArgumentTypes(ProducedType t){
        ProducedType tuple = getCallableTuple(t);
        if(tuple != null)
            return getTupleElementTypes(tuple);
        return Collections.emptyList();
    }
    
    public ProducedType getCallableTuple(ProducedType t) {
        ProducedType ct = t.getSupertype(getCallableDeclaration());
        if (ct!=null) {
            List<ProducedType> typeArgs = ct.getTypeArgumentList();
            if (typeArgs.size()>=2) {
                return typeArgs.get(1);
            }
        }
        return null;
    }
}
