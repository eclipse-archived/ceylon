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
class IntegerBoxing(){
    T id<T>(T x){
        return x;
    }
    void m() {
        // decl
        variable Integer localAttrInteger := +1;
        variable Integer localAttrOptionalInteger := +1;
        variable Object localAttrObject := +1;
        
        // assign
        localAttrInteger := localAttrInteger;
        localAttrOptionalInteger := localAttrInteger;
        localAttrObject := localAttrInteger;
        
        localAttrInteger := id(+1);
        localAttrOptionalInteger := id(+1);
        localAttrObject := id(+1);
        
        Object localAttrConstantObject = localAttrInteger;
        Integer localAttrConstantInteger = localAttrInteger;
        
        // is
        if(is Integer localAttrConstantObject){
            localAttrInteger := localAttrConstantObject;
        }
        if(is Integer localAttrConstantInteger){
            localAttrInteger := localAttrConstantInteger;
        }
        if(is String localAttrConstantObject){
            String foo = localAttrConstantObject;
        }
        if(is String localAttrConstantInteger){
            String foo = localAttrConstantInteger;
        }
        
        localAttrInteger := localAttrInteger + +2; 
        localAttrObject := localAttrInteger + +2; 
    }
}