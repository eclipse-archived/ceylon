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

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

public class TypeParser {
    private ModelLoader loader;
    private Unit unit;
    
    public TypeParser(ModelLoader loader, Unit unit) {
        this.loader = loader;
        this.unit = unit;
    }
    
    /** 
     * Format: spec: type(<spec(,spec)*>)?(\|type(<spec(,spec)*>)?)
     */
    public ProducedType decodeType(String value, Scope scope) {
        // stack of lists of union or param types 
        Deque<LinkedList<ProducedType>> types = new LinkedList<LinkedList<ProducedType>>();
        types.push(new LinkedList<ProducedType>());
        char[] chars = value.toCharArray();
        int nameStart = 0;
        boolean expectingUnion = true;
        for (int i = 0; i < chars.length; i++) {
            String typeName = null;
            switch(chars[i]){
            case ',':
            case '|':
            case '>':
            case '<':
                if(i > nameStart){
                    // we're adding one simple type
                    typeName = new String(chars, nameStart, i-nameStart).trim();
                    unify(types.peek(), loader.getType(typeName, scope), expectingUnion);
                }// else it has already been added on the way down
                nameStart = i+1;
            }
            switch(chars[i]){
            case '<':
                // we're starting a new parameter list
                types.push(new LinkedList<ProducedType>());
                break;
            case ',':
                expectingUnion = false;
                break;
            case '|':
                expectingUnion = true;
                break;
            case '>':
                // we have every param for the current type, now create it
                LinkedList<ProducedType> params = types.pop();
                ProducedType type = types.peek().getLast();
                // if it's a union type we're parameterising its last type
                if(type.getDeclaration() instanceof UnionType){
                    List<ProducedType> caseTypes = type.getDeclaration().getCaseTypes();
                    type = caseTypes.get(caseTypes.size()-1);
                    ProducedType parameterisedType = type.getDeclaration().getProducedType(null, params);
                    // replace it
                    caseTypes.set(caseTypes.size()-1, parameterisedType);
                }else{
                    ProducedType parameterisedType = type.getDeclaration().getProducedType(null, params);
                    // and replace it
                    types.peek().set(types.peek().size()-1, parameterisedType);
                }
                expectingUnion = false;
                break;
            }
        }
        // at any last simple type
        if(nameStart < chars.length){
            // we're adding one simple type
            String typeName = new String(chars, nameStart, chars.length - nameStart).trim();
            unify(types.peek(), loader.getType(typeName, scope), expectingUnion);
        }// else it has already been added on the way down 

        // we should have it done
        return types.pop().pop();
    }

    private void unify(LinkedList<ProducedType> list, ProducedType type, boolean unify) {
        if(!unify){
            list.add(type);
            return;
        }
        if(list.isEmpty()){
            // add a new union type
            UnionType unionType = new UnionType(unit);
            LinkedList<ProducedType> unionTypes = new LinkedList<ProducedType>();
            unionTypes.add(type);
            unionType.setCaseTypes(unionTypes);
            list.add(unionType.getType());
        }else{
            ProducedType last = list.getLast();
            if(last.getDeclaration() instanceof UnionType){
                // add to an existing union type
                last.getDeclaration().getCaseTypes().add(type);
            }else{
                // turn a non-union type into one
                UnionType unionType = new UnionType(unit);
                LinkedList<ProducedType> unionTypes = new LinkedList<ProducedType>();
                unionTypes.add(last);
                unionTypes.add(type);
                unionType.setCaseTypes(unionTypes);
                list.set(list.size()-1, unionType.getType());
            }
        }
    }
}
