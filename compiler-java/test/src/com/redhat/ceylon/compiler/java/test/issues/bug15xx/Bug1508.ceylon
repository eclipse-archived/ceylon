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
interface Bug1508It<A,B> {
    shared A&B attr => nothing;
    shared A&B method() => nothing;
}

@noanno
Y?(X?) bug1508<X,Y>(Y(X) fun)
  given X satisfies Object{
    // more general test
    Bug1508It<Y,Y> it = nothing;
    value f3 = Bug1508It<Y,Y>.attr;
    value f4 = it.attr;
    value f5 = Bug1508It<Y,Y>.method;
    value f6 = it.method();
    // original test
    return compose(Iterable<Y>.first, 
                   compose(shuffle(Iterable<X>.map<Y>)(fun), 
                           emptyOrSingleton<X>));
}
