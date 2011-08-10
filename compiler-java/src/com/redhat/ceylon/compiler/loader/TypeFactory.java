package com.redhat.ceylon.compiler.loader;

import static com.redhat.ceylon.compiler.typechecker.model.Util.addToUnion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class TypeFactory {
    private Context context;

    public static TypeFactory instance(com.sun.tools.javac.util.Context context) {
        TypeFactory instance = context.get(TypeFactory.class);
        if (instance == null) {
            instance = new TypeFactory(LanguageCompiler.getCeylonContextInstance(context));
            context.put(TypeFactory.class, instance);
        }
        return instance;
    }
    
    public TypeFactory(Context context) {
        this.context = context;
    }
    
    public Context getContext() {
        return context;
    }
    
    /**
     * Search for a declaration in the language module. 
     */
    private Declaration getLanguageModuleDeclaration(String name) {
        //all elements in ceylon.language are auto-imported
        //traverse all default module packages provided they have not been traversed yet
        Module languageModule = getContext().getModules().getLanguageModule();
        if ( languageModule != null && languageModule.isAvailable() ) {
            if ("Bottom".equals(name)) {
                return new BottomType();
            }
            for (Package languageScope : languageModule.getPackages() ) {
                Declaration d = languageScope.getMember(name);
                if (d != null) {
                    return d;
                }
            }
        }
        return null;
    }
    
    /**
     * Gets the declaration of {@code Nothing}
     * @return The declaration
     */
    public Class getNothingDeclaration() {
        return (Class) ((TypeDeclaration) getLanguageModuleDeclaration("Nothing"));
    }

    /**
     * Gets the declaration of {@code Empty}
     * @return The declaration
     */
    public Interface getEmptyDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Empty"));
    }

    /**
     * Gets the declaration of {@code Sequence}
     * @return The declaration
     */
    public Interface getSequenceDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Sequence"));
    }

    /**
     * Gets the declaration of {@code ArraySequence}
     * @return The declaration
     */
    public Class getDefaultSequenceDeclaration() {
        return (Class) ((TypeDeclaration) getLanguageModuleDeclaration("ArraySequence"));
    }

    /**
     * Gets the declaration of {@code Iterable}
     * @return The declaration
     */
    public Interface getIterableDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Iterable"));
    }
    
    /**
     * Gets the declaration of {@code Iterator}
     * @return The declaration
     */
    public Interface getIteratorDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Iterator"));
    }
    
    /**
     * Gets the declaration of {@code Castable}
     * @return The declaration
     */
    public Interface getCastableDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Castable"));
    }
    
    /**
     * Gets the declaration of {@code Entry}
     * @return The declaration
     */
    public Class getEntryDeclaration() {
        return (Class) ((TypeDeclaration) getLanguageModuleDeclaration("Entry"));
    }
    
    /**
     * Gets the declaration of {@code Range}
     * @return The declaration
     */
    public Class getRangeDeclaration() {
        return (Class) ((TypeDeclaration) getLanguageModuleDeclaration("Range"));
    }
    
    public static ProducedType producedType(TypeDeclaration declaration, ProducedType typeArgument) {
        return declaration.getProducedType(null, Collections.singletonList(typeArgument));
    }

    public static ProducedType producedType(TypeDeclaration declaration, ProducedType... typeArguments) {
        return declaration.getProducedType(null, Arrays.asList(typeArguments));
    }

    public static List<ProducedType> getTypeArguments(Tree.TypeArguments tal) {
        List<ProducedType> typeArguments = new ArrayList<ProducedType>();
        if (tal instanceof Tree.TypeArgumentList) {
            for (Tree.Type ta: ( (Tree.TypeArgumentList) tal ).getTypes()) {
                ProducedType t = ta.getTypeModel();
                if (t==null) {
                    ta.addError("could not resolve type argument");
                    typeArguments.add(null);
                }
                else {
                    typeArguments.add(t);
                }
            }
        }
        return typeArguments;
    }
    
    /**
     * Returns a ProducedType corresponding to the union of the given types
     * @param types The types to union together
     * @return The type
     */
    public ProducedType unionType(ProducedType... types) {
        List<ProducedType> list = new ArrayList<ProducedType>();
        for (ProducedType t : types) {
            addToUnion(list, t);
        }
        UnionType ut = new UnionType();
        ut.setCaseTypes(list);
        return ut.getType();
    }
    
    /**
     * Determines whether the given ProducedType is a union
     * @param pt 
     * @return whether the type is a union type
     */
    public boolean isUnion(ProducedType pt) {
        TypeDeclaration tdecl = pt.getDeclaration();
        return (tdecl instanceof UnionType && tdecl.getCaseTypes().size() > 1);
    }
    
    /**
     * Determines whether the given ProducedType is {@code Empty|T}
     * @param pt The ProducedType to check
     * @return whether the type is a union type with empty
     */
    public boolean isUnionWithEmpty(ProducedType pt) {
        TypeDeclaration tdecl = pt.getDeclaration();
        if (tdecl instanceof UnionType && tdecl.getCaseTypes().size() == 2) {
            return !(pt.minus(getEmptyDeclaration()).getDeclaration() instanceof UnionType);
        }
        return false;
    }
    
    /**
     * Determines whether the given ProducedType is optional ({@code Nothing|T})
     * @param pt The ProducedType to check
     * @return whether the type is optional
     */
    public boolean isOptional(ProducedType pt) {
        TypeDeclaration tdecl = pt.getDeclaration();
        return (tdecl instanceof UnionType && tdecl.getCaseTypes().size() > 1 && getNothingDeclaration().getType().isSubtypeOf(pt));
    }
    
    /**
     * Returns a ProducedType corresponding to {@code Empty|T}
     * @param pt The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Empty|T}
     */
    public ProducedType makeEmptyType(ProducedType pt) {
        if (pt==null) {
            return null;
        }
        else {
            return unionType(pt, getEmptyDeclaration().getType());
        }
    }
    
    /**
     * Returns a ProducedType corresponding to {@code Nothing|T}
     * @param pt The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Nothing|T}
     */
    public ProducedType makeOptionalType(ProducedType pt) {
        if (pt==null) {
            return null;
        }
        else {
            return unionType(pt, getNothingDeclaration().getType());
        }
    }
    
    /**
     * Returns a ProducedType corresponding to {@code Sequence<T>}
     * @param et The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Sequence<T>}
     */
    public ProducedType makeSequenceType(ProducedType et) {
        return producedType(getSequenceDeclaration(), et);
    }
    
    /**
     * Returns a ProducedType corresponding to {@code ArraySequence<T>}
     * @param et The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code ArraySequence<T>}
     */
    public ProducedType makeDefaultSequenceType(ProducedType et) {
        return producedType(getDefaultSequenceDeclaration(), et);
    }
    
    /**
     * Returns a ProducedType corresponding to {@code Iterable<T>}
     * @param et The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Iterable<T>}
     */
    public ProducedType makeIterableType(ProducedType et) {
        return producedType(getIterableDeclaration(), et);
    }
    
    public ProducedType getIteratedType(ProducedType type) {
        ProducedType st = type.getSupertype(getIterableDeclaration());
        if (st!=null && st.getTypeArguments().size()==1) {
                return st.getTypeArgumentList().get(0);
        } else {
                return null;
        }
    }

    /**
     * Returns a ProducedType corresponding to {@code Iterator<T>}
     * @param et The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Iterator<T>}
     */
    public ProducedType makeIteratorType(ProducedType et) {
        return producedType(getIteratorDeclaration(), et);
    }

    /**
     * Returns a ProducedType corresponding to {@code Castable<T>}
     * @param et The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Castable<T>}
     */
    public ProducedType makeCastableType(ProducedType et) {
        return producedType(getCastableDeclaration(), et);
    }

    /**
     * Returns a ProducedType corresponding to {@code Entry<K,V>}
     * @param kt The ProducedType corresponding to {@code K}
     * @param vt The ProducedType corresponding to {@code V}
     * @return The ProducedType corresponding to {@code Entry<K,V>}
     */
    public ProducedType makeEntryType(ProducedType kt, ProducedType vt) {
        return producedType(getEntryDeclaration(), kt, vt);
    }

    /**
     * Returns a ProducedType corresponding to {@code Range<T>}
     * @param rt The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Range<T>}
     */
    public ProducedType makeRangeType(ProducedType rt) {
        return producedType(getRangeDeclaration(), rt);
    }

    /**
     * Removes Nothing from a type such as Nothing|T
     * @param pt The ProducedType corresponding to {@code Nothing|T}
     * @return The ProducedType corresponding to {@code T}
     */
    public ProducedType getDefiniteType(ProducedType pt) {
        return pt.minus(getNothingDeclaration());
    }

    /**
     * Removes Nothing and Empty from a type such as Nothing|Empty|T
     * @param pt The ProducedType corresponding to {@code Nothing|Empty|T}
     * @return The ProducedType corresponding to {@code T}
     */
    public ProducedType getNonemptyType(ProducedType pt) {
        return pt.minus(getNothingDeclaration()).minus(getEmptyDeclaration());
    }

    public ProducedType getNonemptySequenceType(ProducedType pt) {
        return pt.minus(getEmptyDeclaration()).getSupertype(getSequenceDeclaration());
    }

    public ProducedType getNonemptyIterableType(ProducedType pt) {
        return pt.minus(getEmptyDeclaration()).getSupertype(getIterableDeclaration());
    }
}
