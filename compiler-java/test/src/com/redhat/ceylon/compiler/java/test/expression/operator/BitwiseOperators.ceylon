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
abstract class BWMyInteger()
        extends Object()
        satisfies Castable<BWMyInteger|Float> &
                  Binary<BWMyInteger> &
                  Integral<BWMyInteger> &
                  Exponentiable<BWMyInteger, BWMyInteger>{}

@nomodel
variable Integer bwToplevelN1 := 0;
@nomodel
variable Integer bwToplevelN2 := 0;
@nomodel
variable Integer bwToplevelI1 := 0;

@nomodel 
Integer bwToplevelGetterN1 {
    return bwToplevelN1; 
}assign bwToplevelGetterN1 {
    bwToplevelN1 := bwToplevelGetterN1; 
}
@nomodel 
Integer bwToplevelGetterN2 {
    return bwToplevelN2; 
}assign bwToplevelGetterN2 {
    bwToplevelN2 := bwToplevelGetterN2; 
}
@nomodel 
Integer bwToplevelGetterI1 {
    return bwToplevelI1; 
}assign bwToplevelGetterI1 {
    bwToplevelI1 := bwToplevelGetterI1; 
}

@nomodel
class BitwiseOperators(BWMyInteger initN, BWMyInteger initI) {
    variable Integer n1 := 0;
    variable Integer n2 := 0;
    variable Integer i1 := 0;
    
    Integer getterN1 {
        return n1; 
    }assign getterN1 {
        n1 := getterN1; 
    }
    Integer getterN2 {
        return n2; 
    }assign getterN2 {
        n2 := getterN2; 
    }
    Integer getterI1 {
        return i1; 
    }assign getterI1 {
        i1 := getterI1; 
    }

    variable BWMyInteger boxedN1 := initN;
    variable BWMyInteger boxedN2 := initN;
    variable BWMyInteger boxedI1 := initI;

    //
    // Class Attributes

    void unboxedAttributes() {

        i1 := n1.not;

        n1 := n1.and(n2);
        i1 := n1.or(n2);
        n1 := n1.xor(n2);
        n1 := n1.leftLogicalShift(n2);
        n1 := n1.rightLogicalShift(n2);
        n1 := n1.rightArithmeticShift(n2);

    }

    void unboxedGetterAttributes() {

        getterI1 := getterN1.not;

        getterN1 := getterN1.and(getterN2);
        getterI1 := getterN1.or(getterN2);
        getterN1 := getterN1.xor(getterN2);
        getterN1 := getterN1.leftLogicalShift(getterN2);
        getterN1 := getterN1.rightLogicalShift(getterN2);
        getterN1 := getterN1.rightArithmeticShift(getterN2);

    }

    //
    // Qualified Class Attributes

    void unboxedQualifiedAttributes() {

        this.i1 := this.n1.not;

        this.n1 := this.n1.and(this.n2);
        this.i1 := this.n1.or(this.n2);
        this.n1 := this.n1.xor(this.n2);
        this.n1 := this.n1.leftLogicalShift(this.n2);
        this.n1 := this.n1.rightLogicalShift(this.n2);
        this.n1 := this.n1.rightArithmeticShift(this.n2);

    }

    void unboxedQualifiedGetterAttributes() {

        this.getterI1 := this.getterN1.not;

        this.getterN1 := this.getterN1.and(this.getterN2);
        this.getterI1 := this.getterN1.or(this.getterN2);
        this.getterN1 := this.getterN1.xor(this.getterN2);
        this.getterN1 := this.getterN1.leftLogicalShift(this.getterN2);
        this.getterN1 := this.getterN1.rightLogicalShift(this.getterN2);
        this.getterN1 := this.getterN1.rightArithmeticShift(this.getterN2);

    }

    void boxedQualifiedAttributes() {

        this.boxedI1 := this.boxedN1.not;

        this.boxedN1 := this.boxedN1.and(this.boxedN2);
        this.boxedI1 := this.boxedN1.or(this.boxedN2);
        this.boxedN1 := this.boxedN1.xor(this.boxedN2);
        this.boxedN1 := this.boxedN1.leftLogicalShift(1);
        this.boxedN1 := this.boxedN1.rightLogicalShift(1);
        this.boxedN1 := this.boxedN1.rightArithmeticShift(1);

    }

    //
    // Local Attributes

    void unboxedLocals() {
        variable Integer n1 := 0;
        variable Integer n2 := 0;
        variable Integer i1 := 0;

        i1 := n1.not;

        n1 := n1.and(n2);
        i1 := n1.or(n2);
        n1 := n1.xor(n2);
        n1 := n1.leftLogicalShift(n2);
        n1 := n1.rightLogicalShift(n2);
        n1 := n1.rightArithmeticShift(n2);

    }

    void unboxedLocalGetters() {
        variable Integer n1 := 0;
        variable Integer n2 := 0;
        variable Integer i1 := 0;
        Integer getterN1 {
            return n1; 
        }assign getterN1 {
            n1 := getterN1; 
        }
        Integer getterN2 {
            return n2; 
        }assign getterN2 {
            n2 := getterN2; 
        }
        Integer getterI1 {
            return i1; 
        }assign getterI1 {
            i1 := getterI1; 
        }

        getterI1 := getterN1.not;

        getterN1 := getterN1.and(getterN2);
        getterI1 := getterN1.or(getterN2);
        getterN1 := getterN1.xor(getterN2);
        getterN1 := getterN1.leftLogicalShift(getterN2);
        getterN1 := getterN1.rightLogicalShift(getterN2);
        getterN1 := getterN1.rightArithmeticShift(getterN2);

    }

    void boxedLocals(){
        variable BWMyInteger n1 := initN;
        variable BWMyInteger n2 := initN;
        variable BWMyInteger i1 := initI;

        i1 := n1.not;

        n1 := n1.and(n2);
        i1 := n1.or(n2);
        n1 := n1.xor(n2);
        n1 := n1.leftLogicalShift(1);
        n1 := n1.rightLogicalShift(1);
        n1 := n1.rightArithmeticShift(1);

    }

    //
    // bwToplevel Attributes

    void unboxedToplevel() {

        bwToplevelI1 := bwToplevelN1.not;

        bwToplevelN1 := bwToplevelN1.and(bwToplevelN2);
        bwToplevelI1 := bwToplevelN1.or(bwToplevelN2);
        bwToplevelN1 := bwToplevelN1.xor(bwToplevelN2);
        bwToplevelN1 := bwToplevelN1.leftLogicalShift(bwToplevelN2);
        bwToplevelN1 := bwToplevelN1.rightLogicalShift(bwToplevelN2);
        bwToplevelN1 := bwToplevelN1.rightArithmeticShift(bwToplevelN2);

    }

    void unboxedToplevelGetter() {

        bwToplevelGetterI1 := bwToplevelGetterN1.not;

        bwToplevelGetterN1 := bwToplevelGetterN1.and(bwToplevelGetterN2);
        bwToplevelGetterI1 := bwToplevelGetterN1.or(bwToplevelGetterN2);
        bwToplevelGetterN1 := bwToplevelGetterN1.xor(bwToplevelGetterN2);
        bwToplevelGetterN1 := bwToplevelGetterN1.leftLogicalShift(bwToplevelGetterN2);
        bwToplevelGetterN1 := bwToplevelGetterN1.rightLogicalShift(bwToplevelGetterN2);
        bwToplevelGetterN1 := bwToplevelGetterN1.rightArithmeticShift(bwToplevelGetterN2);

    }
}
