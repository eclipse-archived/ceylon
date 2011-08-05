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
    
    public Class getNothingDeclaration() {
        return (Class) ((TypeDeclaration) getLanguageModuleDeclaration("Nothing"));
    }

    public Interface getEmptyDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Empty"));
    }

    public Interface getSequenceDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Sequence"));
    }

    public Class getDefaultSequenceDeclaration() {
        return (Class) ((TypeDeclaration) getLanguageModuleDeclaration("ArraySequence"));
    }

    public Interface getIterableDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Iterable"));
    }
    
    public Interface getIteratorDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Iterator"));
    }
    
    public Interface getCastableDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Castable"));
    }
    
    public Class getEntryDeclaration() {
        return (Class) ((TypeDeclaration) getLanguageModuleDeclaration("Entry"));
    }
    
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
    
    public ProducedType unionType(ProducedType... types) {
        List<ProducedType> list = new ArrayList<ProducedType>();
        for (ProducedType t : types) {
            addToUnion(list, t);
        }
        UnionType ut = new UnionType();
        ut.setCaseTypes(list);
        return ut.getType();
    }
    
    public ProducedType getEmptyType(ProducedType pt) {
        if (pt==null) {
            return null;
        }
        else {
            return unionType(pt, getEmptyDeclaration().getType());
        }
    }
    
    public ProducedType getOptionalType(ProducedType pt) {
        if (pt==null) {
            return null;
        }
        else {
            return unionType(pt, getNothingDeclaration().getType());
        }
    }
    
    public ProducedType getSequenceType(ProducedType et) {
        return producedType(getSequenceDeclaration(), et);
    }
    
    public ProducedType getDefaultSequenceType(ProducedType et) {
        return producedType(getDefaultSequenceDeclaration(), et);
    }
    
    public ProducedType getIterableType(ProducedType et) {
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

    public ProducedType getIteratorType(ProducedType et) {
        return producedType(getIteratorDeclaration(), et);
    }

    public ProducedType getCastableType(ProducedType et) {
        return producedType(getCastableDeclaration(), et);
    }

    public ProducedType getEntryType(ProducedType kt, ProducedType vt) {
        return producedType(getEntryDeclaration(), kt, vt);
    }

    public ProducedType getRangeType(ProducedType rt) {
        return producedType(getRangeDeclaration(), rt);
    }

    public ProducedType getDefiniteType(ProducedType pt) {
        return pt.minus(getNothingDeclaration());
    }

    public ProducedType getNonemptyType(ProducedType pt) {
        return pt.minus(getNothingDeclaration()).minus(getEmptyDeclaration());
    }

    public ProducedType getNonemptySequenceType(ProducedType pt) {
        return pt.minus(getEmptyDeclaration()).getSupertype(getSequenceDeclaration());
    }
}
