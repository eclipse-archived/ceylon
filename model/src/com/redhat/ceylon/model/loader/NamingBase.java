package com.redhat.ceylon.model.loader;

import static com.redhat.ceylon.common.JVMModuleUtil.quoteIfJavaKeyword;
import static com.redhat.ceylon.model.loader.JvmBackendUtil.isBoxedVariable;
import static com.redhat.ceylon.model.loader.JvmBackendUtil.isValue;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getConstructedClass;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isLocalToInitializer;

import com.redhat.ceylon.model.loader.model.FieldValue;
import com.redhat.ceylon.model.loader.model.JavaBeanValue;
import com.redhat.ceylon.model.loader.model.OutputElement;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Type;
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
        $get$,
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
        $apply$, apply,
        
        $serialize$,
        deconstructor,
        $references$,
        $set$,
        reference,
        instance, $isMember$,
        $object$
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
        $pattern$,
        $instance$, $array$
    }

    public static String prefixName(Prefix prefix, String s) {
        return prefix.toString() + s;
    }

    public static String prefixName(Prefix prefix, String... rest) {
        if (rest.length == 0) {
            throw new RuntimeException();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        for (String s : rest) {
            sb.append(s).append('$');
        }
        
        sb.setLength(sb.length()-1);// remove last $
        return sb.toString();
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
        return (!str.isEmpty() && str.charAt(0) == '$') ? str.substring(1) : str;
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

        if(is_CONSTANT_CASE(newName))
            return constant_case_toCamelCase(newName);

        for(int i=0;i<newName.length;i++){
            int codepoint = newName[i];
            if(Character.isLowerCase(codepoint) || codepoint == '_'){
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

    private static String constant_case_toCamelCase(int[] newName) {
        int j = 0;
        boolean capitaliseNext = false;
        for(int i=0;i<newName.length;i++){
            int codepoint = newName[i];
            if(codepoint == '_'){
                // skip underscore
                capitaliseNext = true;
            }else if(capitaliseNext){
                newName[j++] = codepoint;
                capitaliseNext = false;
            }else{
                newName[j++] = Character.toLowerCase(codepoint);
            }
        }
        return new String(newName, 0, j);
    }

    private static boolean is_CONSTANT_CASE(int[] newName) {
        // reject empty, "U" and "_"
        if(newName.length <= 1)
            return false;
        boolean hasOneUnderscore = false;
        for(int i=0;i<newName.length;i++){
            int codepoint = newName[i];
            if(Character.isLowerCase(codepoint) && codepoint != '_')
                return false;
            if(codepoint == '_')
                hasOneUnderscore = true;
        }
        return hasOneUnderscore;
    }

    /**
     * Turns:
     * - urlDecoder -> URLDecoder
     * - url -> URL
     * Warning: also turns:
     * - uRLDecoder -> URLDecoder (which is then decoded to urlDecoder instead of the original, so we need to guard)
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
        if (decl instanceof FieldValue) {
            return ((FieldValue)decl).getRealName();
        }
        if (decl instanceof JavaBeanValue && !indirect) {
            return ((JavaBeanValue)decl).getGetterName();
        }
        
        boolean enumeratedConstructor = false;
        if (decl instanceof Value) {
            Type type = ((Value)decl).getType();
            enumeratedConstructor = type != null
                && type.getDeclaration() instanceof com.redhat.ceylon.model.typechecker.model.Constructor;
        }
        if (decl.isClassOrInterfaceMember() 
                && (!isLocalToInitializer(decl) || enumeratedConstructor || decl.isStatic()) 
                && !indirect) {
            if (enumeratedConstructor) {
                Class constructedClass = getConstructedClass(decl);
                // See CeylonVisitor.transformSingletonConstructor for that logic
                if(constructedClass.isToplevel() || constructedClass.isClassMember())
                    return getGetterName(((Class)decl.getContainer()).getName() + "$" + decl.getName());
                return name(Unfix.ref);
            }
            return getErasedGetterName(decl);
        } else if (decl instanceof TypedDeclaration 
                && isBoxedVariable((TypedDeclaration)decl)) {
            return name(Unfix.ref);
        } else {
            return name(Unfix.get_);
        }
    }

    public static String getSetterName(Declaration decl) {
        // use the refined decl except when the current declaration is variable and the refined isn't
        Declaration refinedDecl = decl.getRefinedDeclaration();
        if (isValue(decl) && isValue(refinedDecl)) {
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
        } else if (decl.isClassOrInterfaceMember() && !isLocalToInitializer(decl)
                || decl.isStatic()) {
            String setterName = getSetterName(decl.getName());
            if (decl.isMember() && !decl.isShared()) {
                setterName = suffixName(Suffix.$priv$, setterName);
            }
            return setterName;
        } else if (decl instanceof TypedDeclaration 
                && isBoxedVariable((TypedDeclaration)decl)) {
            return name(Unfix.ref);
        }  else {
            return name(Unfix.set_);
        }
    }

    private static String getErasedGetterName(Declaration decl) {
        String property = decl.getName();
        // ERASURE
        if (!(decl instanceof Value) 
                || ((Value)decl).isShared()) {
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
        String result = "get"+capitalize(stripLeadingDollar(property));
        if ("getClass".equals(result)) {
            result = "getClass$";
        }
        return result;
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
    
    /** 
     * Return the name of the Ceylon attribute according to conventions 
     * applied to the given getter/setter method name.
     * Note: The value of a {@code @Name} annotation take precedence over
     * the convention-derived name returned by this method. 
     */
    public static String getJavaAttributeName(String getterName) {
        if (getterName.startsWith("get") || getterName.startsWith("set")) {
            return getJavaBeanName(getterName.substring(3));
        } else if (getterName.startsWith("is")) {
            // Starts with "is"
            return getJavaBeanName(getterName.substring(2));
        } else if (getterName.equals("hashCode")) {
            // Starts with "is"
            return "hash";
        } else if (getterName.equals("toString")) {
            // Starts with "is"
            return "string";
        } else {
            throw new RuntimeException("Illegal java getter/setter name " + getterName);
        }
    }

    public static String getValueConstructorFieldNameAsString(Value singletonModel) {
        Class clz = (Class)singletonModel.getContainer();
        if (clz.isToplevel()) {
            return quoteIfJavaKeyword(singletonModel.getName());
        }
        else {
            return prefixName(Prefix.$instance$, clz.getName(), singletonModel.getName());
        }
    }
}
