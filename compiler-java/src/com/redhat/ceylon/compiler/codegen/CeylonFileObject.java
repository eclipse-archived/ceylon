package com.redhat.ceylon.compiler.codegen;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.ForwardingFileObject;
import javax.tools.JavaFileObject;

import static javax.tools.JavaFileObject.Kind.*;

public class CeylonFileObject
extends ForwardingFileObject<JavaFileObject>
implements JavaFileObject
{
    private final JavaFileObject f;
    public CeylonFileObject(JavaFileObject f)
    {
        super(f);
        this.f = f;
    }

    public CeylonFileObject(JavaFileObject f, String[] sourcePath) {
        super(f);
        this.f = f;
    }

    public JavaFileObject getFile() {
        return f;
    }

    /**
     * Gets the kind of this file object.
     *
     * @return the kind
     */
    public JavaFileObject.Kind getKind() {
        String n = f.getName();
        if (n.endsWith(CLASS.extension))
            return CLASS;
        else if (n.endsWith(SOURCE.extension)
                || n.endsWith(".ceylon"))
            return SOURCE;
        else if (n.endsWith(HTML.extension))
            return HTML;
        else
            return OTHER;
    }

    /**
      * Checks if this file object is compatible with the specified
      * simple name and kind.  A simple name is a single identifier
      * (not qualified) as defined in the <a
      * href="http://java.sun.com/docs/books/jls/">Java Language
      * Specification</a> 3rd ed., section 6.2 "Names and Identifiers".
      *
      * @param simpleName a simple name of a class
      * @param kind a kind
      * @return {@code true} if this file object is compatible; false
      * otherwise
      */
     public boolean isNameCompatible(String simpleName, Kind kind)
     {
         if (kind == Kind.SOURCE)
         {
             String name = f.getName();
             String n = simpleName + ".ceylon";
             if (name.endsWith(n)) {
                 return true;
             }
         }

         return f.isNameCompatible(simpleName, kind);
     }

     /**
      * Provides a hint about the nesting level of the class
      * represented by this file object.  This method may return
      * {@link NestingKind#MEMBER} to mean
      * {@link NestingKind#LOCAL} or {@link NestingKind#ANONYMOUS}.
      * If the nesting level is not known or this file object does not
      * represent a class file this method returns {@code null}.
      *
      * @return the nesting kind, or {@code null} if the nesting kind
      * is not known
      */
     public NestingKind getNestingKind() {
         return f.getNestingKind();
     }

     /**
      * Provides a hint about the access level of the class represented
      * by this file object.  If the access level is not known or if
      * this file object does not represent a class file this method
      * returns {@code null}.
      *
      * @return the access level
      */
     public Modifier getAccessLevel() {
         return f.getAccessLevel();
     }

     public String toString() {
         return f.toString();
     }
}
