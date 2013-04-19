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
@noanno
class IntegerBoxing(){
    variable Integer attrInteger = 1;
    
    T id<T>(T x){
        return x;
    }
    void m() {
        // decl
        variable Integer localAttrInteger = 1;
        variable Integer localAttrOptionalInteger = 1;
        variable Object localAttrObject = 1;
        
        // assign
        localAttrInteger = localAttrInteger;
        localAttrOptionalInteger = localAttrInteger;
        localAttrObject = localAttrInteger;
        
        localAttrInteger = id(1);
        localAttrOptionalInteger = id(1);
        localAttrObject = id(1);
        
        Object localAttrConstantObject = localAttrInteger;
        Integer localAttrConstantInteger = localAttrInteger;
        
        // is
        if(is Integer localAttrConstantObject){
            localAttrInteger = localAttrConstantObject;
        }
        
        localAttrInteger = localAttrInteger + 2; 
        localAttrObject = localAttrInteger + 2; 
        attrInteger = attrInteger + 2; 
        
        test(localAttrInteger + 2);
        test(attrInteger + 2);
        test(this.attrInteger + 2);
        
        localAttrInteger = localAttrInteger.plus(2);
        attrInteger = attrInteger.plus(2);
        this.attrInteger = this.attrInteger.plus(2);
        
        test(localAttrInteger.plus(2));
        test(attrInteger.plus(2));
        test(this.attrInteger.plus(2));
        
        localAttrInteger = localAttrInteger++;
        attrInteger = attrInteger++;
        this.attrInteger = this.attrInteger++;
        
        test(localAttrInteger++);
        test(attrInteger++);
        test(this.attrInteger++);
        
        localAttrInteger = localAttrInteger.successor;
        attrInteger = attrInteger.successor;
        this.attrInteger = this.attrInteger.successor;
        
        test(localAttrInteger.successor);
        test(attrInteger.successor);
        test(this.attrInteger.successor);
        
        localAttrInteger = localAttrInteger.positiveValue;
        attrInteger = attrInteger.positiveValue;
        this.attrInteger = this.attrInteger.positiveValue;
        
        test(localAttrInteger.positiveValue);
        test(attrInteger.positiveValue);
        test(this.attrInteger.positiveValue);
        
        localAttrInteger = -localAttrInteger.positiveValue;
        attrInteger = -attrInteger.positiveValue;
        this.attrInteger = -this.attrInteger.positiveValue;
        
        test(-localAttrInteger.positiveValue);
        test(-attrInteger.positiveValue);
        test(-this.attrInteger.positiveValue);
    }
    
    void test(Integer i) {
    }
}