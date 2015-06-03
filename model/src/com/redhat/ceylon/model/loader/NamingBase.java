package com.redhat.ceylon.model.loader;

import com.redhat.ceylon.model.loader.model.FieldValue;
import com.redhat.ceylon.model.loader.model.JavaBeanValue;
import com.redhat.ceylon.model.loader.model.OutputElement;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

public class NamingBase {

    public static final String OLD_MODULE_DESCRIPTOR_CLASS_NAME = "module_";
    public static final String MODULE_DESCRIPTOR_CLASS_NAME = "$module_";
    public static final String PACKAGE_DESCRIPTOR_CLASS_NAME = "$package_";

    /**
     * A synthetic name, or part of a synthetic name 
     */
    interface Affix {
    }
    
    /**
     * An internally used identifier (not used as a prefix or suffix).
     * Should start and end with a {@code $} and contain no {@code $}
     */
    public enum Unfix implements Affix {
        ref,
        set_,
        get_,
        value,
        
        $name$,
        $annotationSequence$,
        $array$,
        $call$,
        $callvariadic$,
        $calltyped$,
        $element$,
        $evaluate$,
        $getArray$,
        $getFirst$,
        $getLength$,
        $getType$,
        $getIterables$,
        $index$,
        $initException$,
        $instance$,
        $invoke$,
        $lookup$,
        $refine$,
        $return$,
        $sb$,
        $spreadVarargs$,
        $TypeDescriptor$,
        
        $serialize$,
        $deserialize$,
        deconstructor,
        deconstructed
    }
    
    /**
     * Enumerates suffixes used in synthetic names.
     * 
     * Should start and end with a {@code $} and contain no {@code $}
     */
    public enum Suffix implements Affix {
        $delegation$,
        $aliased$,
        $annotation$,
        $annotations$,
        $arg$,
        $args$,
        $argthis$,
        $callable$,
        $canonical$,
        $element$,
        $exhausted$,
        $getter$,
        $impl, // special case, since it's used in type names
        $iterable$,
        $iteration$,
        $iterator$,
        $new$,
        $param$,
        $priv$,
        $qual$,
        $reified$,
        $sb$,
        $setter$,
        $specifier$,
        $this$,
        $variadic$, 
    }
    
    /**
     * Enumerates prefixes used in synthetic names.
     * 
     * Should start and end with a {@code $} and contain no {@code $}
     */
    public enum Prefix implements Affix {
        $next$,
        $arg$,
        $ceylontmp$,
        $default$,
        $init$,
        $iterator$,
        $reified$,
        $superarg$,
        $pattern$
    }

    public static String suffixName(Suffix suffix, String s) {
        return s + suffix.toString();
    }

    public static String getAliasInstantiatorMethodName(Class model) {
        String name = suffixName(Suffix.$aliased$, model.getName());
        if (!model.isShared()) {
            name = suffixName(Suffix.$priv$, name);
        }
        return name;
    }

    /**
     * Removes any leading $ from the given string.
     */
    public static String stripLeadingDollar(String str){
        return (str.charAt(0) == '$') ? str.substring(1) : str;
    }

    public static String capitalize(String str){
        return new StringBuilder().appendCodePoint(Character.toUpperCase(str.codePointAt(0))).append(str.substring(Character.isSurrogate(str.charAt(0)) ? 2 : 1)).toString();
    }

    public static String getDisambigAnnoCtorName(Interface iface, OutputElement target) {
        return getJavaBeanName(iface.getName())+"__"+target;
    }

    /**
     * Turns:
     * - UrlDecoder -> urlDecoder
     * - URLDecoder -> urlDecoder
     * - urlDecoder -> urlDecoder
     * - URL -> url
     */
    public static String getJavaBeanName(String name) {
        // See https://github.com/ceylon/ceylon-compiler/issues/340
        // make it lowercase until the first non-uppercase
        
        int[] newName = new int[name.codePointCount(0, name.length())];
        // fill the code point array; String has no getCodePointArray()
        for(int charIndex=0,codePointIndex=0;charIndex<name.length();){
            int c = name.codePointAt(charIndex);
            newName[codePointIndex++] = c;
            charIndex += Character.charCount(c);
        }
        
        for(int i=0;i<newName.length;i++){
            int codepoint = newName[i];
            if(Character.isLowerCase(codepoint)){
                // if we had more than one upper-case, we leave the last uppercase: getURLDecoder -> urlDecoder
                if(i > 1){
                    newName[i-1] = Character.toUpperCase(newName[i-1]);
                }
                break;
            }
            newName[i] = Character.toLowerCase(codepoint);
        }
        return new String(newName, 0, newName.length);
    }

    /**
     * Turns:
     * - urlDecoder -> URLDecoder
     * - url -> URL
     */
    public static String getReverseJavaBeanName(String name){
        // turns urlDecoder -> URLDecoder
        for(int i=0; i<name.length(); i+= Character.isSurrogate(name.charAt(i)) ? 2 : 1){
            if(Character.isUpperCase(name.codePointAt(i))){
                // make everything before the upper case into upper case
                return name.substring(0, i).toUpperCase() + name.substring(i);
            }
        }
        // we did not find a single upper case, just make it all upper case
        return name.toUpperCase();
    }

    public static String getGetterName(Declaration decl) {
        return getGetterName(decl, false);
    }

    public static String getGetterName(Declaration decl, boolean indirect) {
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        if (decl instanceof FieldValue){
            return ((FieldValue)decl).getRealName();
        }
        if (decl instanceof JavaBeanValue) {
            return ((JavaBeanValue)decl).getGetterName();
        }
        if (ModelUtil.withinClassOrInterface(decl) && !ModelUtil.isLocalToInitializer(decl) && !indirect) {
            return getErasedGetterName(decl);
        } else if (decl instanceof TypedDeclaration && JvmBackendUtil.isBoxedVariable((TypedDeclaration)decl)) {
            return name(Unfix.ref);
        } else {
            return name(Unfix.get_);
        }
    }

    public static String getSetterName(Declaration decl){
        // use the refined decl except when the current declaration is variable and the refined isn't
        Declaration refinedDecl = decl.getRefinedDeclaration();
        if (JvmBackendUtil.isValue(decl) && JvmBackendUtil.isValue(refinedDecl)) {
            Value v = (Value)decl;
            Value rv = (Value)refinedDecl;
            if (!v.isVariable() || rv.isVariable()) {
                decl = refinedDecl;
            }
        } else {
            decl = refinedDecl;
        }
        if (decl instanceof FieldValue){
            return ((FieldValue)decl).getRealName();
        }
        if (decl instanceof JavaBeanValue
                // only if the declaration actually has a setter name, if it's a non-variable
                // one it will not. This is also used for late setters...
                && ((JavaBeanValue)decl).getSetterName() != null) {
            return ((JavaBeanValue)decl).getSetterName();
        } else if (ModelUtil.withinClassOrInterface(decl) && !ModelUtil.isLocalToInitializer(decl)) {
            String setterName = getSetterName(decl.getName());
            if (decl.isMember() && !decl.isShared()) {
                setterName = suffixName(Suffix.$priv$, setterName);
            }
            return setterName;
        } else if (decl instanceof TypedDeclaration && JvmBackendUtil.isBoxedVariable((TypedDeclaration)decl)) {
            return name(Unfix.ref);
        }  else {
            return name(Unfix.set_);
        }
    }

    private static String getErasedGetterName(Declaration decl) {
        String property = decl.getName();
        // ERASURE
        if (!(decl instanceof Value) || ((Value)decl).isShared()) {
            if ("hash".equals(property)) {
                return "hashCode";
            } else if ("string".equals(property)) {
                return "toString";
            }
        }
        
        String getterName = getGetterName(property);
        if (decl.isMember() && !decl.isShared()) {
            getterName = suffixName(Suffix.$priv$, getterName);
        }
        return getterName;
    }

    /** 
     * @deprecated Use of this method outside this package is 
     * <strong>strongly</strong> discouraged. 
     * Its public modifier will be removed at a future date.
     */
    public static String getGetterName(String property) {
        return "get"+capitalize(stripLeadingDollar(property));
    }

    /** 
     * @deprecated Use of this method outside this package is 
     * <strong>strongly</strong> discouraged. 
     * Its public modifier will be removed at a future date.
     */
    public static String getSetterName(String property){
        return "set"+capitalize(stripLeadingDollar(property));
    }

    public static String name(Unfix unfix) {
        return unfix.toString();
    }
}
