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
@nomodel
interface ConcreteAttribute<A> {
    A? aNonShared {
        return null;
    }
    shared A? aShared {
        return aNonShared;
    }
    shared formal A? aFormal;
    shared variable formal A? aVariableFormal;
    shared default A? aDefault {
        return null;
    }
}
@nomodel
void concreteAttributeCallsite(ConcreteAttribute<Integer|Float> a) {
    variable Nothing|Integer|Float v;
    v = a.aShared;
    v = a.aFormal;
    v = a.aVariableFormal;
    a.aVariableFormal = null;
    a.aVariableFormal = 1;
    a.aVariableFormal = 1.0;
    v = a.aDefault;
}
@nomodel
class ConcreteAttributeGetterImpl<B>() satisfies ConcreteAttribute<B> {
    shared actual B? aFormal {
        return null;
    }
    shared actual B? aVariableFormal {
        return null;
    }
    assign aVariableFormal {
    }
    shared actual B? aDefault {
        return null;
    } 
    assign aDefault {
    }
}
@nomodel
void concreteAttributeGetterImplCallsite(ConcreteAttributeGetterImpl<Integer|Float> a) {
    variable Nothing|Integer|Float v;
    v = a.aShared;
    v = a.aFormal;
    v = a.aVariableFormal;
    a.aVariableFormal = null;
    a.aVariableFormal = 1;
    a.aVariableFormal = 1.0;
    v = a.aDefault;
    a.aDefault = null;
    a.aDefault = 1;
    a.aDefault = 1.0;
}
@nomodel
class ConcreteAttributeValueImpl<C>() satisfies ConcreteAttribute<C> {
    shared actual C? aFormal = null;
    shared variable actual C? aVariableFormal = null;
    shared variable actual C? aDefault = null;
}
@nomodel
void concreteAttributeValueImplCallsite(ConcreteAttributeValueImpl<Integer|Float> a) {
    variable Nothing|Integer|Float v;
    v = a.aShared;
    v = a.aFormal;
    v = a.aVariableFormal;
    a.aVariableFormal = null;
    a.aVariableFormal = 1;
    a.aVariableFormal = 1.0;
    v = a.aDefault;
    a.aDefault = null;
    a.aDefault = 1;
    a.aDefault = 1.0;
}