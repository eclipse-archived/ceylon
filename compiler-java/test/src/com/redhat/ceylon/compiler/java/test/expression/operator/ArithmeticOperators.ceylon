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

// we need those to test for boxed numbers
@nomodel
abstract class MyInteger()
        extends Object()
        satisfies Integral<MyInteger> &
                  Exponentiable<MyInteger, MyInteger>{}

@nomodel
variable Integer toplevelN1 = 0;
@nomodel
variable Integer toplevelN2 = 0;
@nomodel
variable Integer toplevelI1 = +0;

@nomodel 
Integer toplevelGetterN1 {
    return toplevelN1; 
}assign toplevelGetterN1 {
    toplevelN1 = toplevelGetterN1; 
}
@nomodel 
Integer toplevelGetterN2 {
    return toplevelN2; 
}assign toplevelGetterN2 {
    toplevelN2 = toplevelGetterN2; 
}
@nomodel 
Integer toplevelGetterI1 {
    return toplevelI1; 
}assign toplevelGetterI1 {
    toplevelI1 = toplevelGetterI1; 
}

@nomodel
class ArithmeticOperators(MyInteger initN, MyInteger initI) {
    shared variable Integer n1 = 0;
    shared variable Integer n2 = 0;
    shared variable Integer i1 = +0;
    
    shared Integer getterN1 {
        return n1; 
    }assign getterN1 {
        n1 = getterN1; 
    }
    shared Integer getterN2 {
        return n2; 
    }assign getterN2 {
        n2 = getterN2; 
    }
    shared Integer getterI1 {
        return i1; 
    }assign getterI1 {
        i1 = getterI1; 
    }

    shared variable MyInteger boxedN1 = initN;
    shared variable MyInteger boxedN2 = initN;
    shared variable MyInteger boxedI1 = initI;

    //
    // Class Attributes

    void unboxedAttributes() {
        n1++;
        ++n1;
        n1--;
        --n1;
        
        i1 = +n1;
        i1 = -n1;
        
        n1 = n1 + n2;
        i1 = n1 - n2;
        n1 = n1 * n2;
        n1 = n1 / n2;
        n1 = n1 % n2;
        n1 = n1 ** n2;
        
        n1 += n2;
        n1 *= n1;
        n1 /= n2;
        n1 %= n2;
    }

    void unboxedGetterAttributes() {
        getterN1++;
        ++getterN1;
        getterN1--;
        --getterN1;
        
        getterI1 = +getterN1;
        getterI1 = -getterN1;
        
        getterN1 = getterN1 + getterN2;
        getterI1 = getterN1 - getterN2;
        getterN1 = getterN1 * getterN2;
        getterN1 = getterN1 / getterN2;
        getterN1 = getterN1 % getterN2;
        getterN1 = getterN1 ** getterN2;
        
        getterN1 += getterN2;
        getterN1 *= getterN1;
        getterN1 /= getterN2;
        getterN1 %= getterN2;
    }

    //
    // Qualified Class Attributes

    void unboxedQualifiedAttributes() {
        this.n1++;
        ++this.n1;
        this.n1--;
        --this.n1;
        
        this.i1 = +this.n1;
        this.i1 = -this.n1;
        
        this.n1 = this.n1 + this.n2;
        this.i1 = this.n1 - this.n2;
        this.n1 = this.n1 * this.n2;
        this.n1 = this.n1 / this.n2;
        this.n1 = this.n1 % this.n2;
        this.n1 = this.n1 ** this.n2;
        
        this.n1 += this.n2;
        this.n1 *= this.n1;
        this.n1 /= this.n2;
        this.n1 %= this.n2;
    }

    shared default void unboxedQualifiedGetterAttributes() {
        this.getterN1++;
        ++this.getterN1;
        this.getterN1--;
        --this.getterN1;
        
        this.getterI1 = +this.getterN1;
        this.getterI1 = -this.getterN1;
        
        this.getterN1 = this.getterN1 + this.getterN2;
        this.getterI1 = this.getterN1 - this.getterN2;
        this.getterN1 = this.getterN1 * this.getterN2;
        this.getterN1 = this.getterN1 / this.getterN2;
        this.getterN1 = this.getterN1 % this.getterN2;
        this.getterN1 = this.getterN1 ** this.getterN2;
        
        this.getterN1 += this.getterN2;
        this.getterN1 *= this.getterN1;
        this.getterN1 /= this.getterN2;
        this.getterN1 %= this.getterN2;
    }

    shared default void boxedQualifiedAttributes() {
        this.boxedN1++;
        ++this.boxedN1;
        this.boxedN1--;
        --this.boxedN1;
        
        this.boxedI1 = +this.boxedN1;
        this.boxedI1 = -this.boxedN1;
        
        this.boxedN1 = this.boxedN1 + this.boxedN2;
        this.boxedI1 = this.boxedN1 - this.boxedN2;
        this.boxedN1 = this.boxedN1 * this.boxedN2;
        this.boxedN1 = this.boxedN1 / this.boxedN2;
        this.boxedN1 = this.boxedN1 % this.boxedN2;
        this.boxedN1 = this.boxedN1 ** this.boxedN2;
        
        this.boxedN1 += this.boxedN2;
        this.boxedN1 *= this.boxedN1;
        this.boxedN1 /= this.boxedN2;
        this.boxedN1 %= this.boxedN2;
    }

    //
    // Local Attributes

    void unboxedLocals() {
        variable Integer n1 = 0;
        variable Integer n2 = 0;
        variable Integer i1 = +0;
        
        n1++;
        ++n1;
        n1--;
        --n1;
        
        i1 = +n1;
        i1 = -n1;
        
        n1 = n1 + n2;
        i1 = n1 - n2;
        n1 = n1 * n2;
        n1 = n1 / n2;
        n1 = n1 % n2;
        n1 = n1 ** n2;
        
        n1 += n2;
        n1 *= n1;
        n1 /= n2;
        n1 %= n2;
    }

    void unboxedLocalGetters() {
        variable Integer n1 = 0;
        variable Integer n2 = 0;
        variable Integer i1 = +0;
        Integer getterN1 {
            return n1; 
        }assign getterN1 {
            n1 = getterN1; 
        }
        Integer getterN2 {
            return n2; 
        }assign getterN2 {
            n2 = getterN2; 
        }
        Integer getterI1 {
            return i1; 
        }assign getterI1 {
            i1 = getterI1; 
        }
                
        getterN1++;
        ++getterN1;
        getterN1--;
        --getterN1;
        
        getterI1 = +getterN1;
        getterI1 = -getterN1;
        
        getterN1 = getterN1 + getterN2;
        getterI1 = getterN1 - getterN2;
        getterN1 = getterN1 * getterN2;
        getterN1 = getterN1 / getterN2;
        getterN1 = getterN1 % getterN2;
        getterN1 = getterN1 ** getterN2;
        
        getterN1 += getterN2;
        getterN1 *= getterN1;
        getterN1 /= getterN2;
        getterN1 %= getterN2;
    }

    void boxedLocals(){
        variable MyInteger n1 = initN;
        variable MyInteger n2 = initN;
        variable MyInteger i1 = initI;

        n1++;
        ++n1;
        n1--;
        --n1;
        
        i1 = +n1;
        i1 = -n1;
        
        n1 = n1 + n2;
        i1 = n1 - n2;
        n1 = n1 * n2;
        n1 = n1 / n2;
        n1 = n1 % n2;
        n1 = n1 ** n2;
        
        n1 += n2;
        n1 *= n1;
        n1 /= n2;
        n1 %= n2;
    }

    //
    // Toplevel Attributes

    void unboxedToplevel() {
        toplevelN1++;
        ++toplevelN1;
        toplevelN1--;
        --toplevelN1;
        
        toplevelI1 = +toplevelN1;
        toplevelI1 = -toplevelN1;
        
        toplevelN1 = toplevelN1 + toplevelN2;
        toplevelI1 = toplevelN1 - toplevelN2;
        toplevelN1 = toplevelN1 * toplevelN2;
        toplevelN1 = toplevelN1 / toplevelN2;
        toplevelN1 = toplevelN1 % toplevelN2;
        toplevelN1 = toplevelN1 ** toplevelN2;
        
        toplevelN1 += toplevelN2;
        toplevelN1 *= toplevelN1;
        toplevelN1 /= toplevelN2;
        toplevelN1 %= toplevelN2;
    }

    void unboxedToplevelGetter() {
        toplevelGetterN1++;
        ++toplevelGetterN1;
        toplevelGetterN1--;
        --toplevelGetterN1;
        
        toplevelGetterI1 = +toplevelGetterN1;
        toplevelGetterI1 = -toplevelGetterN1;
        
        toplevelGetterN1 = toplevelGetterN1 + toplevelGetterN2;
        toplevelGetterI1 = toplevelGetterN1 - toplevelGetterN2;
        toplevelGetterN1 = toplevelGetterN1 * toplevelGetterN2;
        toplevelGetterN1 = toplevelGetterN1 / toplevelGetterN2;
        toplevelGetterN1 = toplevelGetterN1 % toplevelGetterN2;
        toplevelGetterN1 = toplevelGetterN1 ** toplevelGetterN2;
        
        toplevelGetterN1 += toplevelGetterN2;
        toplevelGetterN1 *= toplevelGetterN1;
        toplevelGetterN1 /= toplevelGetterN2;
        toplevelGetterN1 %= toplevelGetterN2;
    }
}

@nomodel
class ArithmeticOperatorsSub(MyInteger initN, MyInteger initI) extends ArithmeticOperators(initN, initI) {
    shared actual void unboxedQualifiedGetterAttributes() {
        super.getterN1++;
        ++super.getterN1;
        super.getterN1--;
        --super.getterN1;
        
        super.getterI1 = +super.getterN1;
        super.getterI1 = -super.getterN1;
        
        super.getterN1 = super.getterN1 + super.getterN2;
        super.getterI1 = super.getterN1 - super.getterN2;
        super.getterN1 = super.getterN1 * super.getterN2;
        super.getterN1 = super.getterN1 / super.getterN2;
        super.getterN1 = super.getterN1 % super.getterN2;
        super.getterN1 = super.getterN1 ** super.getterN2;
        
        super.getterN1 += super.getterN2;
        super.getterN1 *= super.getterN1;
        super.getterN1 /= super.getterN2;
        super.getterN1 %= super.getterN2;
    }

    shared actual void boxedQualifiedAttributes() {
        super.boxedN1++;
        ++super.boxedN1;
        super.boxedN1--;
        --super.boxedN1;
        
        super.boxedI1 = +super.boxedN1;
        super.boxedI1 = -super.boxedN1;
        
        super.boxedN1 = super.boxedN1 + super.boxedN2;
        super.boxedI1 = super.boxedN1 - super.boxedN2;
        super.boxedN1 = super.boxedN1 * super.boxedN2;
        super.boxedN1 = super.boxedN1 / super.boxedN2;
        super.boxedN1 = super.boxedN1 % super.boxedN2;
        super.boxedN1 = super.boxedN1 ** super.boxedN2;
        
        super.boxedN1 += super.boxedN2;
        super.boxedN1 *= super.boxedN1;
        super.boxedN1 /= super.boxedN2;
        super.boxedN1 %= super.boxedN2;
    }
}