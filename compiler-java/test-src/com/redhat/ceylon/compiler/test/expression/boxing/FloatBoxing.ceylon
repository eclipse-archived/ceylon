/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
@nomodel
class FloatBoxing(){
    T id<T>(T x){
        return x;
    }
    void m() {
        // decl
        variable Float localAttrFloat := +1.0;
        variable Float? localAttrOptionalFloat := +1.0;
        variable Object localAttrObject := +1.0;
        
        // assign
        localAttrFloat := localAttrFloat;
        localAttrOptionalFloat := localAttrFloat;
        localAttrObject := localAttrFloat;
        
        localAttrFloat := id(+1.0);
        localAttrOptionalFloat := id(+1.0);
        localAttrObject := id(+1.0);
        
        Object localAttrConstantObject = localAttrFloat;
        Float localAttrConstantFloat = localAttrFloat;
        
        // is
        if(is Float localAttrConstantObject){
            localAttrFloat := localAttrConstantObject;
        }
        if(is Float localAttrConstantFloat){
            localAttrFloat := localAttrConstantFloat;
        }
        if(is String localAttrConstantObject){
            String foo = localAttrConstantObject;
        }
        if(is String localAttrConstantFloat){
            String foo = localAttrConstantFloat;
        }
        
        localAttrFloat := localAttrFloat + +2.0; 
        localAttrObject := localAttrFloat + +2.0; 
    }
}