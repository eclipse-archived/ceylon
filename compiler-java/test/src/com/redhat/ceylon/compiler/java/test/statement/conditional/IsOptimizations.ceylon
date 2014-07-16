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
class IsOptInvar<T>() {}
@noanno
class IsOptCov<out T>() {}
@noanno
class IsOptContra<in T>() {}
@noanno
class IsOptTop() {}
@noanno
class IsOptMiddle() extends IsOptTop() {}
@noanno
class IsOptInvarBound<T>() 
        given T satisfies IsOptMiddle{}
@noanno
class IsOptCovBound<out T>() 
        given T satisfies IsOptMiddle{}
@noanno
class IsOptContraBound<in T>() 
        given T satisfies IsOptMiddle{}

@noanno
abstract class IsOptConstrained<T>() of IsOptConstrainedSub<T> {}
@noanno
class IsOptConstrainedSub<T>() extends IsOptConstrained<T>(){}
@noanno
abstract class IsOptSelfType<T>() of T 
        given T satisfies IsOptSelfType<T> {
}
@noanno
class IsOpt() {
    void testClassShortcircuit(Anything val, IsOptCov<Anything> val2) {
        if (is IsOptCov<Integer> val) {}// instanceof shortcircuit
        if (is IsOptCov<Integer> val2) {}// no shortcircuit
    }
    
    void testTpShortcircuit<Y>(Anything val, Integer val2) 
            given Y satisfies Integer{
        if (is Y val) {}// instanceof shortcircuit
        if (is Y val2) {}// no shortcircuit
    }
    
    void testTypeParamAnything<T>(Anything val) {
        if (is IsOptInvar<Anything> val) {// reified because invar
        } 
        if (is IsOptCov<Anything> val) {// instanceof IsOptGeneric
        }
        if (is IsOptContra<Anything> val) {// reified because contra
        }
    }
    
    void testTypeParamSupertypeOfUpperBound(Anything val) {
        if (is IsOptInvarBound<IsOptTop> val) {// reified because invar
        }
        if (is IsOptCovBound<IsOptTop> val) {// instanceof IsOptGeneric
        }
        if (is IsOptContraBound<IsOptTop> val) {// reified because contra
        }
    }
    
    void testConstrainedTypes<T>(Anything val) {
        if (is IsOptConstrained<T> val) {// reified
        } else if (is IsOptConstrained<Integer|String> val) {// reified
        } else if (is IsOptConstrained<Boolean> val) {// reified
        }
    }
    
    void testSelfTypes<T>(Anything val) {
        if (is IsOptSelfType<T> val) {//reified
        } else if (is IsOptSelfType<Integer|String> val) {//potentially optimizable
        } else if (is IsOptSelfType<Boolean> val) {// optimizable via self type
        }
    }
    
    void testComplement<T>(Integer|String|IsOptInvar<T> val) {
        if (is IsOptInvar<T> val) {// ! (instanceof String || instanceof Integer)
        } else if (is IsOptInvar<Integer> val) {// reified
        } 
    }
    
    void testComplement2<T>(Integer|String|IsOptInvar<String>|IsOptInvar<Integer> val) {
        if (is IsOptInvar<String>|IsOptInvar<Integer> val) {// ! (instanceof String || instanceof Integer)
        } 
        if (is IsOptInvar<Integer> val) {// reified
        }
    }
}