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
suppressWarnings("unusedDeclaration")
class SmallErrors() {
    @error:"type may not be annotated 'small': 'a' has type 'Integer|Float' (only an 'Integer' or 'Float' may be small)"
    small Integer|Float a = 0;
    @error:"type may not be annotated 'small': 'b' has type 'Integer?' (only an 'Integer' or 'Float' may be small)"
    small Integer? b = 0;
    
    shared small Integer smallFunction() => -2147483649;
    shared small Integer smallFunction2() {
        return -2147483649;
    }
    shared small Integer smallValue => -2147483649;
    shared small Integer smallValue2 {
        return -2147483649;
    }
    
    void specifyAndAssign() {
        small Integer c = 2147483648;
        small Integer d = -2147483649;
        
        small value toobig = 2147483648;
        small value toosmall = -2147483649;
        small Integer toobig2;
        small Integer toosmall2;
        toobig2 = 2147483648;
        toosmall2 = -2147483649;
        // AssignOp
        small variable value s = 0;
        print(s = 2147483648);
        print(s = -2147483649);
        
        small value smallFunction3 => -2147483649;
    }
}
abstract class SmallRefinement() {
    shared formal small Integer little();
    shared formal Integer big();
    
    shared formal void littleParam(small Integer a);
    shared formal void bigParam(Integer a);
}
class SmallRefiner() extends SmallRefinement() {
    shared actual Integer little() => 1;
    //@error:"refining member cannot be made small when refined member is not small:'big' in 'SmallRefinement' is not small, but 'big' in 'SmallRefiner' is"
    shared actual small Integer big() => 2;
    
    shared actual void littleParam(Integer a) {}
    
    shared actual void bigParam(
        //@error:"parameter 'a' of 'bigParam' cannot be annotated small: corresponding parameter 'a' of 'bigParam' is not small"
        small Integer a) {}
}