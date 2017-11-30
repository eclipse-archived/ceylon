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
@noanno
abstract class MyByte()
        extends Object()
        satisfies Binary<MyByte> & 
            Invertible<MyByte> &
            Enumerable<MyByte> {}

@noanno
variable Byte toplevelByte1 = 0.byte;
@noanno
variable Byte toplevelByte2 = 0.byte;
@noanno
variable Byte toplevelByte3 = +0.byte;

@noanno 
Byte toplevelGetterByte1 {
    return toplevelByte1; 
}assign toplevelGetterByte1 {
    toplevelByte1 = toplevelGetterByte1; 
}
@noanno 
Byte toplevelGetterByte2 {
    return toplevelByte2; 
}assign toplevelGetterByte2 {
    toplevelByte2 = toplevelGetterByte2; 
}
@noanno 
Byte toplevelGetterByte3 {
    return toplevelByte3; 
}assign toplevelGetterByte3 {
    toplevelByte3 = toplevelGetterByte3; 
}

@noanno
class ArithmeticOperators(MyByte initN, MyByte initI) {
    shared variable Byte n1 = 0.byte;
    shared variable Byte n2 = 0.byte;
    shared variable Byte i1 = +0.byte;
    
    shared Byte getterByte1 {
        return n1; 
    }assign getterByte1 {
        n1 = getterByte1; 
    }
    shared Byte getterByte2 {
        return n2; 
    }assign getterByte2 {
        n2 = getterByte2; 
    }
    shared Byte getterByte3 {
        return i1; 
    }assign getterByte3 {
        i1 = getterByte3; 
    }

    shared variable MyByte boxedByte1 = initN;
    shared variable MyByte boxedByte2 = initN;
    shared variable MyByte boxedByte3 = initI;

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
        
        n1 += n2;
    }

    void unboxedGetterAttributes() {
        getterByte1++;
        ++getterByte1;
        getterByte1--;
        --getterByte1;
        
        getterByte3 = +getterByte1;
        getterByte3 = -getterByte1;
        
        getterByte1 = getterByte1 + getterByte2;
        getterByte3 = getterByte1 - getterByte2;
        
        getterByte1 += getterByte2;
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
        
        this.n1 += this.n2;
    }

    shared default void unboxedQualifiedGetterAttributes() {
        this.getterByte1++;
        ++this.getterByte1;
        this.getterByte1--;
        --this.getterByte1;
        
        this.getterByte3 = +this.getterByte1;
        this.getterByte3 = -this.getterByte1;
        
        this.getterByte1 = this.getterByte1 + this.getterByte2;
        this.getterByte3 = this.getterByte1 - this.getterByte2;
        
        this.getterByte1 += this.getterByte2;
    }

    shared default void boxedQualifiedAttributes() {
        this.boxedByte1++;
        ++this.boxedByte1;
        this.boxedByte1--;
        --this.boxedByte1;
        
        this.boxedByte3 = +this.boxedByte1;
        this.boxedByte3 = -this.boxedByte1;
        
        this.boxedByte1 = this.boxedByte1 + this.boxedByte2;
        this.boxedByte3 = this.boxedByte1 - this.boxedByte2;
        
        this.boxedByte1 += this.boxedByte2;
    }

    //
    // Local Attributes

    void unboxedLocals() {
        variable Byte n1 = 0.byte;
        variable Byte n2 = 0.byte;
        variable Byte i1 = +0.byte;
        
        n1++;
        ++n1;
        n1--;
        --n1;
        
        i1 = +n1;
        i1 = -n1;
        
        n1 = n1 + n2;
        i1 = n1 - n2;
        
        n1 += n2;
    }

    void unboxedLocalGetters() {
        variable Byte n1 = 0.byte;
        variable Byte n2 = 0.byte;
        variable Byte i1 = +0.byte;
        Byte getterByte1 {
            return n1; 
        }assign getterByte1 {
            n1 = getterByte1; 
        }
        Byte getterByte2 {
            return n2; 
        }assign getterByte2 {
            n2 = getterByte2; 
        }
        Byte getterByte3 {
            return i1; 
        }assign getterByte3 {
            i1 = getterByte3; 
        }
                
        getterByte1++;
        ++getterByte1;
        getterByte1--;
        --getterByte1;
        
        getterByte3 = +getterByte1;
        getterByte3 = -getterByte1;
        
        getterByte1 = getterByte1 + getterByte2;
        getterByte3 = getterByte1 - getterByte2;
        
        getterByte1 += getterByte2;
    }

    void boxedLocals(){
        variable MyByte n1 = initN;
        variable MyByte n2 = initN;
        variable MyByte i1 = initI;

        n1++;
        ++n1;
        n1--;
        --n1;
        
        i1 = +n1;
        i1 = -n1;
        
        n1 = n1 + n2;
        i1 = n1 - n2;
        
        n1 += n2;
    }

    //
    // Toplevel Attributes

    void unboxedToplevel() {
        toplevelByte1++;
        ++toplevelByte1;
        toplevelByte1--;
        --toplevelByte1;
        
        toplevelByte3 = +toplevelByte1;
        toplevelByte3 = -toplevelByte1;
        
        toplevelByte1 = toplevelByte1 + toplevelByte2;
        toplevelByte3 = toplevelByte1 - toplevelByte2;
        
        toplevelByte1 += toplevelByte2;
    }

    void unboxedToplevelGetter() {
        toplevelGetterByte1++;
        ++toplevelGetterByte1;
        toplevelGetterByte1--;
        --toplevelGetterByte1;
        
        toplevelGetterByte3 = +toplevelGetterByte1;
        toplevelGetterByte3 = -toplevelGetterByte1;
        
        toplevelGetterByte1 = toplevelGetterByte1 + toplevelGetterByte2;
        toplevelGetterByte3 = toplevelGetterByte1 - toplevelGetterByte2;
        
        toplevelGetterByte1 += toplevelGetterByte2;
    }
}

@noanno
class ArithmeticOperatorsSub(MyByte initN, MyByte initI) extends ArithmeticOperators(initN, initI) {
    shared actual void unboxedQualifiedGetterAttributes() {
        super.getterByte1++;
        ++super.getterByte1;
        super.getterByte1--;
        --super.getterByte1;
        
        super.getterByte3 = +super.getterByte1;
        super.getterByte3 = -super.getterByte1;
        
        super.getterByte1 = super.getterByte1 + super.getterByte2;
        super.getterByte3 = super.getterByte1 - super.getterByte2;
        
        super.getterByte1 += super.getterByte2;
    }

    shared actual void boxedQualifiedAttributes() {
        super.boxedByte1++;
        ++super.boxedByte1;
        super.boxedByte1--;
        --super.boxedByte1;
        
        super.boxedByte3 = +super.boxedByte1;
        super.boxedByte3 = -super.boxedByte1;
        
        super.boxedByte1 = super.boxedByte1 + super.boxedByte2;
        super.boxedByte3 = super.boxedByte1 - super.boxedByte2;
        
        super.boxedByte1 += super.boxedByte2;
    }
}