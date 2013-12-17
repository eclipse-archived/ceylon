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
abstract shared class Bug1510A() of bug1510a {}
@noanno
object bug1510a extends Bug1510A(){}

@noanno
abstract shared class Bug1510B() of bug1510b {}
@noanno
object bug1510b extends Bug1510B(){}

@noanno
class Bug1510Pair(Bug1510A a, Bug1510B b){}

@noanno
void bug1510(){
    value data = {
        for(v1 in `class Bug1510A`.caseTypes)
            if(is Bug1510A v1) // Notice this is interleaved between for-loops
                for(v2 in `class Bug1510B`.caseTypes)
                    if(is Bug1510B v2)
                        Bug1510Pair(v1, v2)
    };
    assert(data.empty);
    assert(data.string == "{}");
}
