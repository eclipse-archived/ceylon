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
T bug2240GetSome<T>(){ return nothing; }

@noanno
Return bug2240<Return, Params, First, Rest>
        (Return(*Tuple<Params, First, Rest>) func)
        given First satisfies Params
        given Rest satisfies Params[]
{
    value firstValue = bug2240GetSome<First>();
    // check if one Argument function
    if (is Return(First) func) {  // Always return false, well in fact works when callFuncWithSome(`testFunc`) !
        return func(firstValue); // fails with error "ceylon run: expected (Object)Object but found (Object,Object)Object"
    }
    else {
        value littleFunc = curry(func)(firstValue); // OK there is no check with no arg function
        assert (is Return(*Tuple<Params,Params,Anything>) littleFunc); // always fails 
        return bug2240(littleFunc);
    }
}
