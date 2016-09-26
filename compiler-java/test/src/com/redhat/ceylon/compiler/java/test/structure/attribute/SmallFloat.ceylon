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
suppressWarnings("unusedDeclaration")
class SmallFloat(shared small Float floatp) {
    small variable Float floata = floatp;
    small Float floatad;
    this.floatad = 1.0;
    
    // TODO setters!
    // TODO captured variables
    shared small Float floatgs => 1.0;
    assign floatgs {}
    
    void floatassignmentOps() {
        variable Float double = 1000.0*1000.0*1000.0*1000.0;
        floata = double;
        this.floata = 1.0;
        this.floata = double;
        floatgs = double;
        double = floata;
        floata = 2147483647.0;
        floata = -2147483648.0;
        floata = 2147483648.0;
        floata = -2147483649.0;
        
        small value localfloatgs => 1.0;
        assign localfloatgs {
            
        }
        localfloatgs = double;
        double = localfloatgs;
        
        double = floata = 2147483647.0;
        floata = double = 2147483647.0;
    }
    
    shared small Float floatCalc(small Float floatp) {
        small value intl = this.floatp;
        floata = floatp * this.floata * intl;
        return floata;
    }
    
    shared void intSmallArithmeticOperators() {
        // These should all be transformed as int expressions
        floata = floata + floatp;
        floata = floata - floatp;
        floata = floata * floatp;
        floata = floata / floatp;
        floata = floata ^ floatp;
        
        floata += floatp;
        floata -= floatp;
        floata *= floatp;
        floata /= floatp;
    }
    shared void intMixedArithmeticOperators() {
        // These should all be transformed as long expressions
        value double = 0.0;
        floata = floata + double;
        floata = floata - double;
        floata = floata * double;
        floata = floata / double;
        floata = floata ^ double;
        
        floata += double;
        floata -= double;
        floata *= double;
        floata /= double;
    }
    
    shared void intComparisonOps() {
        
        variable value c = floata == floatp;
        c = floata != floatp;
        c = floata < floatp;
        c = floata <= floatp;
        c = floata > floatp;
        c = floata >= floatp;
        c = 0.0 < floata < floatp;
        c = 0.0 <= floata <= floatp;
        
        variable value cmp = floata <=> floatp;
    }
    
    
    void floatparamDefault(p1=1.0, small Float p2=p1) {
        small Float p1;
    }
    
    void powerMethodIsNotSmall(small Float x, small Float y) {
        small variable value addition = x+y;
        addition = x.plus(y);
        small variable value exponentiation = x^y;
        // because we're calling it as a *method* the following should 
        // dispatch to Integer.power(long, long), not Integer.power(int, int)
        exponentiation = x.power(y);
    }
    
    void argumentConversion() {
        powerMethodIsNotSmall(1.0, 1.0);
        powerMethodIsNotSmall{
            x=1.0; 
            y=1.0;
        };
        powerMethodIsNotSmall{
            x=1.0; 
            y=>1.0;
        };
    }
    
    /*
     shared void intMethods() {
        // TODO plus bitshifting and bit masking
     }
     shared void otherIntTerms() {
        small value x = if (1==1) then 1 else floata;
        small value y = switch(x) case (1) 1 case(2) floata else floatp;
        small value z = let(a = x, b=y) a+b;
        small value c = (1==1) then 1 else 2;
     }
     
     
     
     shared void range() {
        for (x in 0:10) {
            
        }
        //value y = {for (x in 0:10) if (x%2==0) x*2 };
        /* TODO for (x in 0..10) {
            
        }*/
        
     }
     */
    // TODO test every kind of function and value declaration
    // functional parameters!
    
    // TODO stop setting the underlying type directly in the model loader, set the 
    // small bit of the model instead
    
}

