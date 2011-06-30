package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public abstract class AbstractVisitor extends Visitor {

    protected abstract Context getContext();
    
    /**
     * Search for the declaration referred to by a qualified 
     * name in containing scopes and imports, finally looking 
     * in the language module.
     * 
     *  TODO: I would just love to make this method go away
     *        by somehow treating declarations in the language 
     *        module like any other imported declaration!
     *        
     */
    private Declaration getBaseDeclaration(Scope scope, Unit unit, String name) {
        Declaration d = scope.getMemberOrParameter(unit, name);
        if (d!=null) {
            return d;
        }
        else {
            return getLanguageModuleDeclaration(name);
        }
    }
    
    protected TypedDeclaration getBaseDeclaration(Tree.BaseMemberExpression m) {
        return (TypedDeclaration) getBaseDeclaration(m.getScope(), m.getUnit(), m.getIdentifier().getText());
    }
    
    protected TypeDeclaration getBaseDeclaration(Tree.BaseType that) {
        return (TypeDeclaration) getBaseDeclaration(that.getScope(), that.getUnit(), that.getIdentifier().getText());
    }
    
    protected TypeDeclaration getBaseDeclaration(Tree.BaseTypeExpression that) {
        return (TypeDeclaration) getBaseDeclaration(that.getScope(), that.getUnit(), that.getIdentifier().getText());
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
    
    private TypeDeclaration getLanguageDeclaration(String type) {
        return (TypeDeclaration) getLanguageModuleDeclaration(type);
    }

    protected Interface getCorrespondenceDeclaration() {
        return (Interface) getLanguageDeclaration("Correspondence");
    }

    protected Class getVoidDeclaration() {
        return (Class) getLanguageDeclaration("Void");
    }
    
    protected Class getNothingDeclaration() {
        return (Class) getLanguageDeclaration("Nothing");
    }

    protected Interface getEmptyDeclaration() {
        return (Interface) getLanguageDeclaration("Empty");
    }

    protected Interface getSequenceDeclaration() {
        return (Interface) getLanguageDeclaration("Sequence");
    }

    protected Class getObjectDeclaration() {
        return (Class) getLanguageDeclaration("Object");
    }
    
    protected Class getIdentifiableObjectDeclaration() {
        return (Class) getLanguageDeclaration("IdentifiableObject");
    }
    
    protected Class getExceptionDeclaration() {
        return (Class) getLanguageDeclaration("Exception");
    }

    protected Interface getCategoryDeclaration() {
        return (Interface) getLanguageDeclaration("Category");
    }
    
    protected Interface getIterableDeclaration() {
        return (Interface) getLanguageDeclaration("Iterable");
    }
    
    protected Interface getCastableDeclaration() {
        return (Interface) getLanguageDeclaration("Castable");
    }
    
    protected Interface getSummableDeclaration() {
        return (Interface) getLanguageDeclaration("Summable");
    }
        
    protected Interface getNumericDeclaration() {
        return (Interface) getLanguageDeclaration("Numeric");
    }
        
    protected Interface getIntegralDeclaration() {
        return (Interface) getLanguageDeclaration("Integral");
    }
        
    protected Interface getInvertableDeclaration() {
        return (Interface) getLanguageDeclaration("Invertable");
    }
        
    protected Interface getSlotsDeclaration() {
        return (Interface) getLanguageDeclaration("Slots");
    }
        
    protected TypeDeclaration getComparisonDeclaration() {
        return getLanguageDeclaration("Comparison");
    }
        
    protected TypeDeclaration getBooleanDeclaration() {
        return getLanguageDeclaration("Boolean");
    }
        
    protected TypeDeclaration getStringDeclaration() {
        return getLanguageDeclaration("String");
    }
        
    protected TypeDeclaration getFloatDeclaration() {
        return getLanguageDeclaration("Float");
    }
        
    protected TypeDeclaration getNaturalDeclaration() {
        return getLanguageDeclaration("Natural");
    }
        
    protected TypeDeclaration getCharacterDeclaration() {
        return getLanguageDeclaration("Character");
    }
        
    protected TypeDeclaration getQuotedDeclaration() {
        return getLanguageDeclaration("Quoted");
    }
        
    protected Interface getEqualityDeclaration() {
        return (Interface) getLanguageDeclaration("Equality");
    }
        
    protected Interface getComparableDeclaration() {
        return (Interface) getLanguageDeclaration("Comparable");
    }
        
    protected Interface getOrdinalDeclaration() {
        return (Interface) getLanguageDeclaration("Ordinal");
    }
        
    protected Class getRangeDeclaration() {
        return (Class) getLanguageDeclaration("Range");
    }
        
    protected Class getEntryDeclaration() {
        return (Class) getLanguageDeclaration("Entry");
    }
    
    protected static List<ProducedType> getTypeArguments(Tree.TypeArgumentList tal) {
        List<ProducedType> typeArguments = new ArrayList<ProducedType>();
        if (tal!=null) {
            for (Tree.Type ta: tal.getTypes()) {
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
    
}
