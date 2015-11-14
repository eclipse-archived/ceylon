/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.codegen;

import static javax.tools.JavaFileObject.Kind.CLASS;
import static javax.tools.JavaFileObject.Kind.HTML;
import static javax.tools.JavaFileObject.Kind.OTHER;
import static javax.tools.JavaFileObject.Kind.SOURCE;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.ForwardingFileObject;
import javax.tools.JavaFileObject;

import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Position;

public class CeylonFileObject extends ForwardingFileObject<JavaFileObject> implements JavaFileObject {
    private final JavaFileObject f;
    private List<JCDiagnostic> errorList = List.nil();

    public CeylonFileObject(JavaFileObject f) {
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
        else if (n.endsWith(SOURCE.extension) || n.endsWith(".ceylon"))
            return SOURCE;
        else if (n.endsWith(HTML.extension))
            return HTML;
        else
            return OTHER;
    }

    /**
     * Checks if this file object is compatible with the specified simple name
     * and kind. A simple name is a single identifier (not qualified) as defined
     * in the <a href="http://java.sun.com/docs/books/jls/">Java Language
     * Specification</a> 3rd ed., section 6.2 "Names and Identifiers".
     * 
     * @param simpleName a simple name of a class
     * @param kind a kind
     * @return {@code true} if this file object is compatible; false otherwise
     */
    public boolean isNameCompatible(String simpleName, Kind kind) {
        if (kind == Kind.SOURCE) {
            String name = f.getName();
            String n = simpleName + ".ceylon";
            if (name.endsWith(n)) {
                return true;
            }

            int limit = simpleName.indexOf("$$overload");
            if (limit >= 0) {
                n = simpleName.substring(0, limit) + ".ceylon";
                if (name.endsWith(n)) {
                    return true;
                }
            }
        }

        return f.isNameCompatible(simpleName, kind);
    }

    /**
     * Provides a hint about the nesting level of the class represented by this
     * file object. This method may return {@link NestingKind#MEMBER} to mean
     * {@link NestingKind#LOCAL} or {@link NestingKind#ANONYMOUS}. If the
     * nesting level is not known or this file object does not represent a class
     * file this method returns {@code null}.
     * 
     * @return the nesting kind, or {@code null} if the nesting kind is not
     *         known
     */
    public NestingKind getNestingKind() {
        return f.getNestingKind();
    }

    /**
     * Provides a hint about the access level of the class represented by this
     * file object. If the access level is not known or if this file object does
     * not represent a class file this method returns {@code null}.
     * 
     * @return the access level
     */
    public Modifier getAccessLevel() {
        return f.getAccessLevel();
    }

    public String toString() {
        return f.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CeylonFileObject)
            return f.equals(((CeylonFileObject)obj).f);
        return f.equals(obj);
    }

    public boolean hasError() {
        return !errorList.isEmpty();
    }

    public void addError(JCDiagnostic error){
        errorList = errorList.prepend(error);
    }
    
    public boolean hasError(int pos){
        for(JCDiagnostic error : errorList){
            if(error.getStartPosition() <= pos && pos <= error.getEndPosition())
                return true;
            // we don't know where that error is
            if(error.getStartPosition() == Position.NOPOS)
                return true;
        }
        return false;
    }
}
