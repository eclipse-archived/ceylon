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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.model.typechecker.context.TypeCache;

public class Unit implements LanguageModuleProvider, ImportScope {

    private Package pkg;
    private List<Import> imports = new ArrayList<Import>();
    private List<Declaration> declarations = new ArrayList<Declaration>();
    private String filename;
    private List<ImportList> importLists = new ArrayList<ImportList>();
    private Set<Declaration> duplicateDeclarations = new HashSet<Declaration>();
    private final Set<String> dependentsOf = new HashSet<String>();
    private String fullPath;
    private String relativePath;
    private Backends supportedBackends = Backends.ANY;
    private boolean unresolvedReferences;
    
    @Override
    public List<Import> getImports() {
        synchronized (imports) {
            return new ArrayList<Import>(imports);
        }
    }

    @Override
    public void addImport(Import imp) {
        synchronized (imports) {
            imports.add(imp);
        }
    }

    @Override
    public void removeImport(Import imp) {
        synchronized (imports) {
            imports.remove(imp);
        }
    }

    public List<ImportList> getImportLists() {
        return importLists;
    }

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
    
    public List<Declaration> getMembers() {
        synchronized (declarations) {
            ArrayList<Declaration> list = 
                    new ArrayList<Declaration>
                        (declarations.size());
            for (Declaration d: declarations) {
                if (d.isToplevel()) {
                    list.add(d);
                }
            }
            return list;
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
                    i.getDeclaration()
                        .equals(getAbstraction(dec))) {
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
            d.isStatic() ||
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
            if (itd!=null && td.inherits(itd) && 
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
            int proximity, Cancellable canceller) {
        Map<String, DeclarationWithProximity> result = 
                new TreeMap<String, DeclarationWithProximity>();
        for (Import i: getImports()) {
            if (canceller != null
                    && canceller.isCancelled()) {
                return Collections.emptyMap();
            }
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
            String startingWith, int proximity, Cancellable canceller) {
        Map<String, DeclarationWithProximity> result = 
                new TreeMap<String, DeclarationWithProximity>();
        for (Import i: getImports()) {
            if (canceller != null
                    && canceller.isCancelled()) {
                return Collections.emptyMap();
            }
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
            if (languagePackage!=null) {
                Declaration d = 
                        languagePackage.getMember(name, 
                                null, false);
                if (d!=null && d.isShared()) {
                    return d;
                }
            }
        }
        return null;
    }
    
    private Module getLanguageModule() {
        if (languageModule==null) {
            languageModule = 
                    getPackage()
                        .getModule()
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
    
    @Override
    public Interface getCorrespondenceDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getCorrespondenceDeclaration();
        }
        return null;
    }
    
    public Interface getCorrespondenceMutatorDeclaration() {
        return (Interface) getLanguageModuleDeclaration("CorrespondenceMutator");
    }

    public Interface getIndexedCorrespondenceMutatorDeclaration() {
        return (Interface) getLanguageModuleDeclaration("IndexedCorrespondenceMutator");
    }

    public Interface getKeyedCorrespondenceMutatorDeclaration() {
        return (Interface) getLanguageModuleDeclaration("KeyedCorrespondenceMutator");
    }

    @Override
    public Class getAnythingDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getAnythingDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getNullDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getNullDeclaration();
        }
        return null;
    }
    
    public Value getNullValueDeclaration() {
        return (Value) getLanguageModuleDeclaration("null");
    }
    
    @Override
    public Interface getEmptyDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getEmptyDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getSequenceDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getSequenceDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getObjectDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getObjectDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getBasicDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getBasicDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getIdentifiableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getIdentifiableDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getThrowableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getThrowableDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getExceptionDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getExceptionDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getCategoryDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getCategoryDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getIterableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getIterableDeclaration();
        }
        return null;
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

    public Interface getJavaCharSequenceDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Interface) lang.getMember("CharSequence", null, false);
        }
    }


    public Class getJavaStringDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("String", null, false);
        }
    }

    public Class getJavaClassDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("Class", null, false);
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
    
    public Class getJavaObjectArrayDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("ObjectArray", null, false);
        }
    }
    
    public Class getJavaBooleanArrayDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("BooleanArray", null, false);
        }
    }
    
    public Class getJavaByteArrayDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("ByteArray", null, false);
        }
    }
    
    public Class getJavaShortArrayDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("ShortArray", null, false);
        }
    }
    
    public Class getJavaIntArrayDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("IntArray", null, false);
        }
    }
    
    public Class getJavaLongArrayDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("LongArray", null, false);
        }
    }
    
    public Class getJavaFloatArrayDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("FloatArray", null, false);
        }
    }
    
    public Class getJavaDoubleArrayDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("DoubleArray", null, false);
        }
    }
    
    public Class getJavaCharArrayDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("CharArray", null, false);
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

    public Class getJavaEnumDeclaration() {
        Package lang = getJavaLangPackage();
        if (lang==null) {
            return null;
        }
        else {
            return (Class) lang.getMember("Enum", null, false);
        }
    }

    public Interface getJavaSerializableDeclaration() {
        Package io = getJavaIoPackage();
        if (io==null) {
            return null;
        }
        else {
            return (Interface) io.getMember("Serializable", null, false);
        }
    }

    protected Package getJavaLangPackage() {
        return getPackage().getModule().getPackage("java.lang");
    }
    
    protected Package getJavaUtilPackage() {
        return getPackage().getModule().getPackage("java.util");
    }
    
    protected Package getJavaIoPackage() {
        return getPackage().getModule().getPackage("java.io");
    }
    
    @Override
    public Interface getSequentialDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getSequentialDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getListDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getListDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getCollectionDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getCollectionDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getIteratorDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getIteratorDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getCallableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getCallableDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getScalableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getScalableDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getSummableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getSummableDeclaration();
        }
        return null;
    }
     
    @Override
    public Interface getNumericDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getNumericDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getIntegralDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getIntegralDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getInvertableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getInvertableDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getExponentiableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getExponentiableDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getSetDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getSetDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getMapDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getMapDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getComparisonDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getComparisonDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getBooleanDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getBooleanDeclaration();
        }
        return null;
    }
    
    public Value getTrueValueDeclaration() {
        return (Value) getLanguageModuleDeclaration("true");
    }
    
    public Value getFalseValueDeclaration() {
        return (Value) getLanguageModuleDeclaration("false");
    }
    
    @Override
    public Class getStringDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getStringDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getFloatDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getFloatDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getIntegerDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getIntegerDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getCharacterDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getCharacterDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getByteDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getByteDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getComparableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getComparableDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getUsableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getUsableDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getDestroyableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getDestroyableDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getObtainableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getObtainableDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getOrdinalDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getOrdinalDeclaration();
        }
        return null;
    }
        
    @Override
    public Interface getEnumerableDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getEnumerableDeclaration();
        }
        return null;
    }
        
    @Override
    public Class getRangeDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getRangeDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getSpanDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getSpanDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getMeasureDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getMeasureDeclaration();
        }
        return null;
    }
    
    @Override
    public Class getTupleDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getTupleDeclaration();
        }
        return null;
    }
    
    @Override
    public TypeDeclaration getArrayDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getArrayDeclaration();
        }
        return null;
    }
    
    @Override
    public Interface getRangedDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getRangedDeclaration();
        }
        return null;
    }
        
    @Override
    public Class getEntryDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getEntryDeclaration();
        }
        return null;
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
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getUnknownType();
        }
        return null;
    }
    
    @Override
    public Type getNothingType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getNothingType();
        }
        return null;
    }

    @Override
    public Type getEmptyType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getEmptyType();
        }
        return null;
    }
    
    @Override
    public Type getAnythingType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getAnythingType();
        }
        return null;
    }
    
    @Override
    public Type getObjectType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getObjectType();
        }
        return null;
    }
    
    @Override
    public Type getIdentifiableType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getIdentifiableType();
        }
        return null;
    }
    
    @Override
    public Type getBasicType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getBasicType();
        }
        return null;
    }

    @Override
    public Type getNullType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getNullType();
        }
        return null;
    }
    
    public Type getNullValueType() {
        return getNullValueDeclaration().getType();
    }
    
    @Override
    public Type getThrowableType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getThrowableType();
        }
        return null;
    }
    
    @Override
    public Type getExceptionType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getExceptionType();
        }
        return null;
    }
    
    @Override
    public Type getBooleanType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getBooleanType();
        }
        return null;
    }
    
    @Override
    public Type getStringType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getStringType();
        }
        return null;
    }
    
    @Override
    public Type getIntegerType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getIntegerType();
        }
        return null;
    }
    
    @Override
    public Type getFloatType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getFloatType();
        }
        return null;
    }
    
    @Override
    public Type getCharacterType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getCharacterType();
        }
        return null;
    }
    
    @Override
    public Type getByteType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getByteType();
        }
        return null;
    }
    
    @Override
    public Type getComparisonType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getComparisonType();
        }
        return null;
    }
    
    @Override
    public Type getDestroyableType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getDestroyableType();
        }
        return null;
    }

    @Override
    public Type getObtainableType() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getObtainableType();
        }
        return null;
    }
    
    public Type getJavaAutoCloseableType() {
        return getType(getJavaAutoCloseableDeclaration());
    }

    public Type getJavaEnumType(Type et) {
        return appliedType(getJavaEnumDeclaration(), et);
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
    
    public Type getJavaArrayElementType(Type type) {
        Type t = type.resolveAliases();
        TypeDeclaration dec = t.getDeclaration();
        if (dec instanceof Constructor) {
            dec = dec.getExtendedType()
                    .getDeclaration();
        }
        if (!(dec instanceof Class)) {
            return null;
        }
        else if (dec.equals(getJavaObjectArrayDeclaration())) {
            Type supertype = t.getSupertype(dec);
            if (!supertype.getTypeArguments().isEmpty()) {
                return supertype.getTypeArgumentList().get(0);
            }
            else {
                return null;
            }
        }
        else if (dec.equals(getJavaIntArrayDeclaration())) {
            return getIntegerType();
        }
        else if (dec.equals(getJavaLongArrayDeclaration())) {
            return getIntegerType();
        }
        else if (dec.equals(getJavaShortArrayDeclaration())) {
            return getIntegerType();
        }
        else if (dec.equals(getJavaByteArrayDeclaration())) {
            return getByteType();
        }
        else if (dec.equals(getJavaCharArrayDeclaration())) {
            return getCharacterType();
        }
        else if (dec.equals(getJavaBooleanArrayDeclaration())) {
            return getBooleanType();
        }
        else if (dec.equals(getJavaFloatArrayDeclaration())) {
            return getFloatType();
        }
        else if (dec.equals(getJavaDoubleArrayDeclaration())) {
            return getFloatType();
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
        if (type==null) {
            return false;
        }
        else {
            Type ft = getAbsentType(type);
            return ft!=null && ft.isExactlyNothing();
        }
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
    
    public Type getElementType(Type expressionType) {
        Type it = getIteratedType(expressionType);
        if (it==null) {
            it = getJavaIteratedType(expressionType);
        }
        if (it==null) {
            it = getJavaArrayElementType(expressionType);
        }
        return it;
    }

    public boolean isContainerType(Type t) {
        return isIterableType(t)
            || isJavaIterableType(t)
            || isJavaObjectArrayType(t)
            || isJavaPrimitiveArrayType(t);
    }
    
    public boolean isIterableType(Type pt) {
        return pt.getDeclaration()
                .inherits(getIterableDeclaration());
    }
    
    public boolean isJavaIterableType(Type pt) {
        return pt.getDeclaration()
                .inherits(getJavaIterableDeclaration());
    }
    
    public boolean isJavaObjectArrayType(Type pt) {
        TypeDeclaration dec = 
                pt.resolveAliases()
                    .getDeclaration();
        if (dec instanceof Constructor) {
            dec = dec.getExtendedType()
                    .getDeclaration();
        }
        return dec instanceof Class &&
                dec.equals(getJavaObjectArrayDeclaration());
    }
    
    public boolean isJavaPrimitiveArrayType(Type pt) {
        TypeDeclaration dec = 
                pt.resolveAliases()
                    .getDeclaration();
        if (dec instanceof Constructor) {
            dec = dec.getExtendedType()
                    .getDeclaration();
        }
        return dec instanceof Class &&
                (dec.equals(getJavaIntArrayDeclaration()) ||
                dec.equals(getJavaShortArrayDeclaration()) ||
                dec.equals(getJavaLongArrayDeclaration()) ||
                dec.equals(getJavaByteArrayDeclaration()) ||
                dec.equals(getJavaCharArrayDeclaration()) ||
                dec.equals(getJavaBooleanArrayDeclaration()) ||
                dec.equals(getJavaFloatArrayDeclaration()) ||
                dec.equals(getJavaDoubleArrayDeclaration()));
    }
    
    public boolean isJavaArrayType(Type pt) {
        return isJavaObjectArrayType(pt) 
            || isJavaPrimitiveArrayType(pt);
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
                .isNothing() 
            && !intersectionType(getObjectType(), 
                    pt, this)
                .isNothing();
    }
    
    public boolean isPossiblyEmptyType(Type pt) {
        Type defType = getDefiniteType(pt);
        //must be a subtype of Sequential<Anything>
        return isSequentialType(defType)
        //must have non-empty intersection with Empty
        //and non-empty intersection with Sequence<Nothing>
            && !intersectionType(getEmptyType(), 
                    defType, this)
                .isNothing()
            && !intersectionType(getSequenceTopType(), 
                    defType, this)
                .isNothing();
    }

    public Type getSequenceTopType() {
        return getSequenceType(getAnythingType());
    }
    
    public boolean isCallableType(Type pt) {
        return pt!=null && 
                pt.getDeclaration()
                    .inherits(getCallableDeclaration());
    }
    
    @Override
    public NothingType getNothingDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache().getNothingDeclaration();
        }
        return null;
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
                    dt.setVarianceOverrides(type.getVarianceOverrides());
                    dt.setTypeConstructor(type.isTypeConstructor());
                    dt.setTypeConstructorParameter(
                            type.getTypeConstructorParameter());
                    if (dt.isCached()) {
                        dt = dt.clone();
                    }
                    dt.setUnderlyingType(type.getUnderlyingType());
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
    
    public boolean isTupleLengthUnbounded(Type args) {
        if (args!=null) {
            Type emptyType = getEmptyType();
            if (args.isSubtypeOf(emptyType)) {
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
                        else if (rest.isSubtypeOf(emptyType)) {
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
    
    public boolean isTupleVariantAtLeastOne(Type args) {
        if (args!=null) {
            Type emptyType = getEmptyType();
            if (emptyType.isSubtypeOf(args)) {
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
                        else if (emptyType.isSubtypeOf(rest)) {
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
    
    public int getTupleMinimumLength(Type args) {
        if (args!=null) {
            Type emptyType = getEmptyType();
            if (emptyType.isSubtypeOf(args)) {
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
                        else if (emptyType.isSubtypeOf(rest)) {
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
    
    public int getTupleMaximumLength(Type args) {
        if (args!=null) {
            Type emptyType = getEmptyType();
            if (args.isSubtypeOf(emptyType)) {
                return 0;
            }
            Class td = getTupleDeclaration();
            Type tuple = 
                    nonemptyArgs(args)
                        .getSupertype(td);
            if (tuple == null) {
                return Integer.MAX_VALUE;
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
                        else if (rest.isSubtypeOf(emptyType)) {
                            return size;
                        }
                        else {
                            tuple = nonemptyArgs(rest)
                                    .getSupertype(td);
                            if (tuple==null) {
                                return Integer.MAX_VALUE;
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
        return Integer.MAX_VALUE;
    }
    
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
        put("small");
        put("service");
        put("static");
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
        Type qualifyingType = 
                declaration.isStatic() ?
                    getNullType() :
                    reference.getQualifyingType();
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
        TypedDeclaration declaration = 
                reference.getDeclaration();
        Functional fun = (Functional) declaration;
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
                    declaration.isStatic() ?
                        getNullType() :
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
        Class declaration = 
                (Class) type.getDeclaration();
        ParameterList parameterList = 
                declaration.getParameterList();
        Type parameterTuple;
        Constructor dc = 
                declaration.getDefaultConstructor();
        if ((declaration.isClassOrInterfaceMember() 
                || declaration.isToplevel()) 
            && parameterList!=null
            && !declaration.isAbstraction()
            && (!declaration.isSealed() 
                    || inSameModule(declaration))
            && !declaration.isAbstract()
            && (dc==null || dc.isShared())) {
            List<Parameter> params = 
                    parameterList.getParameters();
            parameterTuple = 
                    getParameterTypesAsTupleType(params, type);
        }
        else {
            parameterTuple = getNothingType();
        }
        Type qualifyingType = 
                declaration.isStatic() ?
                    getNullType() :
                    type.getQualifyingType();
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

    public boolean inSameModule(Declaration declaration) {
        Module module = 
                getPackage().getModule();
        Module otherModule = 
                declaration.getUnit()
                    .getPackage().getModule();
        return module.equals(otherModule);
    }

    public Type getInterfaceMetatype(Type type) {
        Interface declaration = 
                (Interface) type.getDeclaration();
        Type qualifyingType = 
                declaration.isStatic() ?
                    getNullType() :
                    type.getQualifyingType();
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

    public Type getClassOrInterfaceDeclarationType() {
        return getType(getLanguageModuleDeclarationTypeDeclaration("ClassOrInterfaceDeclaration"));
    }

    public TypeDeclaration getClassOrInterfaceModelDeclaration(){
        return getLanguageModuleModelTypeDeclaration("ClassOrInterface");
    }

    public Type getClassOrInterfaceModelType(Type classType){
        TypeDeclaration decl = getClassOrInterfaceModelDeclaration();
        if(decl == null)
            return null;
        return decl.appliedType(null, Arrays.asList(classType));
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
    
    @Override
    public TypeDeclaration getAnnotationDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getAnnotationDeclaration();
        }
        return null;
    }
    
    @Override
    public TypeDeclaration getConstrainedAnnotationDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getConstrainedAnnotationDeclaration();
        }
        return null;
    }
    
    @Override
    public TypeDeclaration getSequencedAnnotationDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getSequencedAnnotationDeclaration();
        }
        return null;
    }
    
    @Override
    public TypeDeclaration getOptionalAnnotationDeclaration() {
        Module theLanguageModule = getLanguageModule();
        if (theLanguageModule != null) {
            return theLanguageModule.getLanguageModuleCache()
                    .getOptionalAnnotationDeclaration();
        }
        return null;
    }
    
    public TypeDeclaration getDeclarationDeclaration() {
        return getLanguageModuleDeclarationTypeDeclaration("Declaration");
    }
    
    public TypeCache getCache() {
        Module module = getPackage().getModule();
        return module != null ? module.getCache() : null;
    }

    public Backends getSupportedBackends() {
        return supportedBackends;
    }
    
    public void setSupportedBackends(Backends backends) {
        supportedBackends = backends;
    }

    public void setUnresolvedReferences() {
        this.unresolvedReferences = true;
    }
    
    public boolean getUnresolvedReferences() {
        return unresolvedReferences;
    }
    
    public boolean isJdkPackage(String name) {
        return false;
    }
    
    /**
     * Returns true if any part of the given Callable is unknown, like Callable&lt;Ret,Args>
     */
    public boolean isUnknownArgumentsCallable(Type callableType) {
        if (callableType==null) return true;
        if (getNothingType().isExactly(callableType)) {
            return false;
        }
        Type args = getCallableTuple(callableType);
        return isUnknownTuple(args);
    }
    
    private boolean isUnknownTuple(Type args) {
        if (args==null) return true;
        if (args.isTypeParameter()) {
            return true;
        } else if (args.isUnion()){
            /* Callable<R,A>&Callable<R,B> is the same as Callable<R,A|B> so 
             * for a union if either A or B is known then the union is known
             */
            java.util.List<Type> caseTypes = args.getCaseTypes();
            if(caseTypes == null || caseTypes.size() < 2)
                return true;
            for (int ii = 0; ii < caseTypes.size(); ii++) {
                if (!isUnknownTuple(caseTypes.get(ii))) {
                    return false;
                }
            }// all unknown
            return true;
        } else if (args.isIntersection()) {
            /* Callable<R,A>|Callable<R,B> is the same as Callable<R,A&B> so 
             * for an intersection if both A and B are known then the intersection is known
             */
            java.util.List<Type> caseTypes = args.getSatisfiedTypes();
            if(caseTypes == null || caseTypes.size() < 2)
                return true;
            for (int ii = 0; ii < caseTypes.size(); ii++) {
                if (isUnknownTuple(caseTypes.get(ii))) {
                    return true;
                }
            }
            return false;
        } else if (args.isNothing()) {
            return true;
        } else if(args.isClassOrInterface()) {
            TypeDeclaration declaration = args.getDeclaration();
            if(declaration.isTuple()){
                Type rest = args.getTypeArgumentList().get(2);
                return isUnknownTuple(rest);
            }
            if(declaration.isEmpty()){
                return false;
            }
            if(declaration.isSequential() || declaration.isSequence()){
                return false;
            }
        } else if (args.isTypeAlias()) {
            return isUnknownTuple(args.resolveAliases());
        }
        return true;
        
    }
}
