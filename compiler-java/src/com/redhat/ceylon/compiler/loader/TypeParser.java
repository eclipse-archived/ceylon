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
package com.redhat.ceylon.compiler.loader;

import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

public class TypeParser {
    public class Part {
        String name;
        List<ProducedType> parameters = new LinkedList<ProducedType>();
    }

    private ModelLoader loader;
    private Unit unit;
    private TypeLexer lexer = new TypeLexer();
    private Scope scope;

    public TypeParser(ModelLoader loader, Unit unit){
        this.loader = loader;
        this.unit = unit;
    }
    
    /*
     * type: unionType EOT
     */
    public ProducedType decodeType(String type, Scope scope){
        // save the previous state (this method is reentrant)
        char[] oldType = lexer.type;
        int oldIndex = lexer.index;
        int oldMark = lexer.mark;
        Scope oldScope = this.scope;
        try{
            // setup the new state
            lexer.setup(type);
            this.scope = scope;
            // do the parsing
            ProducedType ret = parseType();
            if(!lexer.lookingAt(TypeLexer.EOT))
                throw new TypeParserException("Junk lexemes remaining: "+lexer.eatTokenString());
            return ret;
        }finally{
            // restore the previous state
            lexer.type = oldType;
            lexer.index = oldIndex;
            lexer.mark = oldMark;
            this.scope = oldScope;
        }
    }

    /*
     * type: unionType EOT
     */
    private ProducedType parseType(){
        return parseUnionType();
    }

    /*
     * unionType: intersectionType (| intersectionType)*
     */
    private ProducedType parseUnionType() {
        UnionType type = new UnionType(unit);
        List<ProducedType> caseTypes = new LinkedList<ProducedType>();
        type.setCaseTypes(caseTypes);
        caseTypes.add(parseIntersectionType());
        while(lexer.lookingAt(TypeLexer.OR)){
            lexer.eat();
            caseTypes.add(parseIntersectionType());
        }
        return type.getType();
    }

    /*
     * intersectionType: qualifiedType (& qualifiedType)*
     */
    private ProducedType parseIntersectionType() {
        IntersectionType type = new IntersectionType(unit);
        List<ProducedType> satisfiedTypes = new LinkedList<ProducedType>();
        type.setSatisfiedTypes(satisfiedTypes);
        satisfiedTypes.add(parseQualifiedType());
        while(lexer.lookingAt(TypeLexer.AND)){
            lexer.eat();
            satisfiedTypes.add(parseQualifiedType());
        }
        return type.getType();
    }

    /*
     * qualifiedType: [packageName (. packageName)* ::] typeNameWithArguments (. typeNameWithArguments)*
     */
    private ProducedType parseQualifiedType() {
        String pkg;
        
        if (hasPackage()) {
            // handle the package name
            StringBuilder pkgstr = new StringBuilder(lexer.eatWord());
            while(lexer.lookingAt(TypeLexer.DOT)){
                lexer.eat();
                pkgstr = pkgstr.append('.').append(lexer.eatWord());
            }
            lexer.eat(TypeLexer.DBLCOLON);
            pkg = pkgstr.toString();
        } else {
            // type is in default package
            pkg = "";
        }
        
        // then the type itself
        Part part = parseTypeNameWithArguments();
        String fullName = (pkg.isEmpty()) ? part.name : pkg + "." + part.name;
        ProducedType qualifyingType = loadType(pkg, fullName, part, null);
        while(lexer.lookingAt(TypeLexer.DOT)){
            lexer.eat();
            part = parseTypeNameWithArguments();
            fullName = fullName + '.' + part.name;
            qualifyingType = loadType(pkg, fullName, part, qualifyingType);
        }
        if(qualifyingType == null){
            throw new ModelResolutionException("Could not find type '"+fullName+"'");
        }
        return qualifyingType;
    }

    private boolean hasPackage() {
        boolean result;
        lexer.mark();
        while(lexer.lookingAt(TypeLexer.WORD) || lexer.lookingAt(TypeLexer.DOT)){
            lexer.eat();
        }
        result = lexer.lookingAt(TypeLexer.DBLCOLON);
        lexer.reset();
        return result;
    }
    
    private ProducedType loadType(String pkg, String fullName, Part part, ProducedType qualifyingType) {
        // try to find a qualified type
        try{
            ProducedType newType;
            if(qualifyingType == null)
                newType = loader.getType(pkg, fullName, scope);
            else{
                // look it up via its qualifying type
                TypeDeclaration qualifyingDeclaration = qualifyingType.getDeclaration();
                Declaration member = getDirectMember(qualifyingDeclaration, part.name);
                if(!(member instanceof TypeDeclaration))
                    throw new ModelResolutionException("Failed to resolve inner type "+part.name+" in "+qualifyingDeclaration.getQualifiedNameString());
                newType = ((TypeDeclaration)member).getType();
            }
            return newType == null ? null : newType.getDeclaration().getProducedType(qualifyingType, part.parameters);
        }catch(ModelResolutionException x){
            // allow this only if we don't have any qualifying type or parameters:
            // - if we have no qualifying type we may be adding package name parts
            // - if we have a qualifying type then the inner type must exist
            // - if we have type parameters we must have a type
            if(qualifyingType != null
                    || !part.parameters.isEmpty())
                throw x;
            return null;
        }
    }

    /**
     * Looks for a direct member of type ClassOrInterface. We're not using Class.getDirectMember()
     * because it skips object types and we want them.
     */
    private Declaration getDirectMember(TypeDeclaration container, String name) {
        for (Declaration member : container.getMembers()) {
            if (member instanceof ClassOrInterface
                    && member.getName() != null
                    && member.getName().equals(name)) {
                return member;
            }
        }
        return null;
    }

    /*
     * typeNameWithArguments: WORD (< type (, type)* >)?
     */
    private Part parseTypeNameWithArguments() {
        Part type = new Part();
        type.name = lexer.eatWord();
        if(lexer.lookingAt(TypeLexer.LT)){
            lexer.eat();
            type.parameters.add(parseType());
            while(lexer.lookingAt(TypeLexer.COMMA)){
                lexer.eat();
                type.parameters.add(parseType());
            }
            lexer.eat(TypeLexer.GT);
        }
        return type;
    }
}
