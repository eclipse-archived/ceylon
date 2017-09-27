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
abstract class BWMyByte()
        extends Object()
        satisfies Binary<BWMyByte> & 
            Invertible<BWMyByte> &
            Enumerable<BWMyByte> {}

@noanno
variable Byte bwToplevelByte1 = 0.byte;
@noanno
variable Byte bwToplevelByte2 = 0.byte;
@noanno
variable Byte bwToplevelByte3 = 0.byte;

@noanno 
Byte bwToplevelGetterByte1 {
    return bwToplevelByte1; 
}assign bwToplevelGetterByte1 {
    bwToplevelByte1 = bwToplevelGetterByte1; 
}
@noanno 
Byte bwToplevelGetterByte2 {
    return bwToplevelByte2; 
}assign bwToplevelGetterByte2 {
    bwToplevelByte2 = bwToplevelGetterByte2; 
}
@noanno 
Byte bwToplevelGetterByte3 {
    return bwToplevelByte3; 
}assign bwToplevelGetterByte3 {
    bwToplevelByte3 = bwToplevelGetterByte3; 
}

@noanno
class BitwiseOperators(BWMyByte initN, BWMyByte initI) {
    shared variable Byte n1 = 0.byte;
    shared variable Byte n2 = 0.byte;
    shared variable Byte i1 = 0.byte;
    
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

    shared variable BWMyByte boxedByte1 = initN;
    shared variable BWMyByte boxedByte2 = initN;
    shared variable BWMyByte boxedByte3 = initI;

    //
    // Class Attributes

    void unboxedAttributes() {

        i1 = n1.not;

        n1 = n1.and(n2);
        i1 = n1.or(n2);
        n1 = n1.xor(n2);
        n1 = n1.leftLogicalShift(1);
        n1 = n1.rightLogicalShift(1);
        n1 = n1.rightArithmeticShift(1);

    }

    void unboxedGetterAttributes() {

        getterByte3 = getterByte1.not;

        getterByte1 = getterByte1.and(getterByte2);
        getterByte3 = getterByte1.or(getterByte2);
        getterByte1 = getterByte1.xor(getterByte2);
        getterByte1 = getterByte1.leftLogicalShift(1);
        getterByte1 = getterByte1.rightLogicalShift(1);
        getterByte1 = getterByte1.rightArithmeticShift(1);

    }

    //
    // Qualified Class Attributes

    shared default void unboxedQualifiedAttributes() {

        this.i1 = this.n1.not;

        this.n1 = this.n1.and(this.n2);
        this.i1 = this.n1.or(this.n2);
        this.n1 = this.n1.xor(this.n2);
        this.n1 = this.n1.leftLogicalShift(1);
        this.n1 = this.n1.rightLogicalShift(1);
        this.n1 = this.n1.rightArithmeticShift(1);

    }

    shared default void unboxedQualifiedGetterAttributes() {

        this.getterByte3 = this.getterByte1.not;

        this.getterByte1 = this.getterByte1.and(this.getterByte2);
        this.getterByte3 = this.getterByte1.or(this.getterByte2);
        this.getterByte1 = this.getterByte1.xor(this.getterByte2);
        this.getterByte1 = this.getterByte1.leftLogicalShift(1);
        this.getterByte1 = this.getterByte1.rightLogicalShift(1);
        this.getterByte1 = this.getterByte1.rightArithmeticShift(1);

    }

    shared default void boxedQualifiedAttributes() {

        this.boxedByte3 = this.boxedByte1.not;

        this.boxedByte1 = this.boxedByte1.and(this.boxedByte2);
        this.boxedByte3 = this.boxedByte1.or(this.boxedByte2);
        this.boxedByte1 = this.boxedByte1.xor(this.boxedByte2);
        this.boxedByte1 = this.boxedByte1.leftLogicalShift(1);
        this.boxedByte1 = this.boxedByte1.rightLogicalShift(1);
        this.boxedByte1 = this.boxedByte1.rightArithmeticShift(1);

    }

    //
    // Local Attributes

    void unboxedLocals() {
        variable Byte n1 = 0.byte;
        variable Byte n2 = 0.byte;
        variable Byte i1 = 0.byte;

        i1 = n1.not;

        n1 = n1.and(n2);
        i1 = n1.or(n2);
        n1 = n1.xor(n2);
        n1 = n1.leftLogicalShift(1);
        n1 = n1.rightLogicalShift(1);
        n1 = n1.rightArithmeticShift(1);

    }

    void unboxedLocalGetters() {
        variable Byte n1 = 0.byte;
        variable Byte n2 = 0.byte;
        variable Byte i1 = 0.byte;
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

        getterByte3 = getterByte1.not;

        getterByte1 = getterByte1.and(getterByte2);
        getterByte3 = getterByte1.or(getterByte2);
        getterByte1 = getterByte1.xor(getterByte2);
        getterByte1 = getterByte1.leftLogicalShift(1);
        getterByte1 = getterByte1.rightLogicalShift(1);
        getterByte1 = getterByte1.rightArithmeticShift(1);

    }

    void boxedLocals(){
        variable BWMyByte n1 = initN;
        variable BWMyByte n2 = initN;
        variable BWMyByte i1 = initI;

        i1 = n1.not;

        n1 = n1.and(n2);
        i1 = n1.or(n2);
        n1 = n1.xor(n2);
        n1 = n1.leftLogicalShift(1);
        n1 = n1.rightLogicalShift(1);
        n1 = n1.rightArithmeticShift(1);

    }

    //
    // bwToplevel Attributes

    void unboxedToplevel() {

        bwToplevelByte3 = bwToplevelByte1.not;

        bwToplevelByte1 = bwToplevelByte1.and(bwToplevelByte2);
        bwToplevelByte3 = bwToplevelByte1.or(bwToplevelByte2);
        bwToplevelByte1 = bwToplevelByte1.xor(bwToplevelByte2);
        bwToplevelByte1 = bwToplevelByte1.leftLogicalShift(1);
        bwToplevelByte1 = bwToplevelByte1.rightLogicalShift(1);
        bwToplevelByte1 = bwToplevelByte1.rightArithmeticShift(1);

    }

    void unboxedToplevelGetter() {

        bwToplevelGetterByte3 = bwToplevelGetterByte1.not;

        bwToplevelGetterByte1 = bwToplevelGetterByte1.and(bwToplevelGetterByte2);
        bwToplevelGetterByte3 = bwToplevelGetterByte1.or(bwToplevelGetterByte2);
        bwToplevelGetterByte1 = bwToplevelGetterByte1.xor(bwToplevelGetterByte2);
        bwToplevelGetterByte1 = bwToplevelGetterByte1.leftLogicalShift(1);
        bwToplevelGetterByte1 = bwToplevelGetterByte1.rightLogicalShift(1);
        bwToplevelGetterByte1 = bwToplevelGetterByte1.rightArithmeticShift(1);

    }
}

@noanno
class BitwiseOperatorsSub(BWMyByte initN, BWMyByte initI) extends BitwiseOperators(initN, initI) {
    shared actual void unboxedQualifiedAttributes() {

        super.i1 = super.n1.not;

        super.n1 = super.n1.and(super.n2);
        super.i1 = super.n1.or(super.n2);
        super.n1 = super.n1.xor(super.n2);
        super.n1 = super.n1.leftLogicalShift(1);
        super.n1 = super.n1.rightLogicalShift(1);
        super.n1 = super.n1.rightArithmeticShift(1);

    }

    shared actual void unboxedQualifiedGetterAttributes() {

        super.getterByte3 = super.getterByte1.not;

        super.getterByte1 = super.getterByte1.and(super.getterByte2);
        super.getterByte3 = super.getterByte1.or(super.getterByte2);
        super.getterByte1 = super.getterByte1.xor(super.getterByte2);
        super.getterByte1 = super.getterByte1.leftLogicalShift(1);
        super.getterByte1 = super.getterByte1.rightLogicalShift(1);
        super.getterByte1 = super.getterByte1.rightArithmeticShift(1);

    }

    shared actual void boxedQualifiedAttributes() {

        super.boxedByte3 = super.boxedByte1.not;

        super.boxedByte1 = super.boxedByte1.and(super.boxedByte2);
        super.boxedByte3 = super.boxedByte1.or(super.boxedByte2);
        super.boxedByte1 = super.boxedByte1.xor(super.boxedByte2);
        super.boxedByte1 = super.boxedByte1.leftLogicalShift(1);
        super.boxedByte1 = super.boxedByte1.rightLogicalShift(1);
        super.boxedByte1 = super.boxedByte1.rightArithmeticShift(1);

    }
}
