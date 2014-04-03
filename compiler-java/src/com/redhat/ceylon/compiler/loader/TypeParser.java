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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.loader.model.FunctionOrValueInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

public class TypeParser {
    public class Part {
        String name;
        List<ProducedType> parameters;
        List<ProducedType> getParameters(){
            return parameters != null ? parameters : Collections.<ProducedType>emptyList();
        }
    }

    private ModelLoader loader;
    private Unit unit;
    private TypeLexer lexer = new TypeLexer();
    private Scope scope;
    private Module moduleScope;

    public TypeParser(ModelLoader loader){
        this.loader = loader;
    }
    
    /*
     * type: unionType EOT
     */
    public ProducedType decodeType(String type, Scope scope, Module moduleScope, Unit unit){
        // save the previous state (this method is reentrant)
        char[] oldType = lexer.type;
        int oldIndex = lexer.index;
        int oldMark = lexer.mark;
        Scope oldScope = this.scope;
        Module oldModuleScope = this.moduleScope;
        Unit oldUnit = this.unit;
        try{
            // setup the new state
            lexer.setup(type);
            this.scope = scope;
            this.moduleScope = moduleScope;
            this.unit = unit;
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
            this.moduleScope = oldModuleScope;
            this.unit = oldUnit;
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
        ProducedType firstType = parseIntersectionType();
        if(lexer.lookingAt(TypeLexer.OR)){
            UnionType type = new UnionType(unit);
            List<ProducedType> caseTypes = new LinkedList<ProducedType>();
            type.setCaseTypes(caseTypes);
            caseTypes.add(firstType);
            while(lexer.lookingAt(TypeLexer.OR)){
                lexer.eat();
                caseTypes.add(parseIntersectionType());
            }
            return type.getType();
        }else{
            return firstType;
        }
    }

    /*
     * intersectionType: qualifiedType (& qualifiedType)*
     */
    private ProducedType parseIntersectionType() {
        ProducedType firstType = parseQualifiedType();
        if(lexer.lookingAt(TypeLexer.AND)){
            IntersectionType type = new IntersectionType(unit);
            List<ProducedType> satisfiedTypes = new LinkedList<ProducedType>();
            type.setSatisfiedTypes(satisfiedTypes);
            satisfiedTypes.add(firstType);
            while(lexer.lookingAt(TypeLexer.AND)){
                lexer.eat();
                satisfiedTypes.add(parseQualifiedType());
            }
            return type.getType();
        }else{
            return firstType;
        }
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
        if(qualifyingType instanceof ProducedType == false){
            throw new ModelResolutionException("Type is a declaration (should be a ProducedType): '"+fullName+"'");
        }
        return (ProducedType) qualifyingType;
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
            Declaration newDeclaration;
            if(qualifyingType == null){
                // FIXME: this only works for packages not contained in multiple modules
                Package foundPackage = moduleScope.getPackage(pkg);
                if(foundPackage != null)
                    newDeclaration = loader.getDeclaration(foundPackage.getModule(), pkg, fullName, scope);
                else if(scope != null){
                    // if we did not find any package and the scope is null, chances are we're after a type variable
                    // or a relative type, so use the module scope
                    newDeclaration = loader.getDeclaration(moduleScope, pkg, fullName, scope);
                }else
                    newDeclaration = null;
            }else{
                // look it up via its qualifying type or decl
                Declaration qualifyingDeclaration = qualifyingType.getDeclaration();
                if(qualifyingDeclaration instanceof FunctionOrValueInterface)
                    qualifyingDeclaration = ((FunctionOrValueInterface)qualifyingDeclaration).getUnderlyingDeclaration();
                newDeclaration = AbstractModelLoader.getDirectMember((Scope) qualifyingDeclaration, part.name);
                if(newDeclaration == null)
                    throw new ModelResolutionException("Failed to resolve inner type or declaration "+part.name+" in "+qualifyingDeclaration.getQualifiedNameString());
            }
            if(newDeclaration == null)
                return null;
            TypeDeclaration newTypeDeclaration;
            if(newDeclaration instanceof TypeDeclaration)
                newTypeDeclaration = (TypeDeclaration) newDeclaration;
            else
                newTypeDeclaration = new FunctionOrValueInterface((TypedDeclaration) newDeclaration);
            return newTypeDeclaration.getProducedType(qualifyingType, part.getParameters());
        }catch(ModelResolutionException x){
            // allow this only if we don't have any qualifying type or parameters:
            // - if we have no qualifying type we may be adding package name parts
            // - if we have a qualifying type then the inner type must exist
            // - if we have type parameters we must have a type
            if(qualifyingType != null
                    || (part.parameters != null && !part.parameters.isEmpty()))
                throw x;
            return null;
        }
    }

    /*
     * typeNameWithArguments: WORD (< type (, type)* >)?
     */
    private Part parseTypeNameWithArguments() {
        Part type = new Part();
        type.name = lexer.eatWord();
        if(lexer.lookingAt(TypeLexer.LT)){
            lexer.eat();
            type.parameters = new LinkedList<ProducedType>();
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
