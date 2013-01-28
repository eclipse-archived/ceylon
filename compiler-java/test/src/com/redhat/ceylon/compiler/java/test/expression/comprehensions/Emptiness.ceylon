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
void emptiness() {
    Iterable<Integer> maybeEmpty = {1};
    Iterable<Integer,Nothing> nonEmpty = {1};
    
    value maybeEmptyCompIter = {for (i in maybeEmpty) i};
    value nonEmptyCompIter = {for (i in nonEmpty) i};
    value maybeEmptyCompSeq = [for (i in maybeEmpty) i];
    value nonEmptyCompSeq = [for (i in nonEmpty) i];
    
    emptinessMaybeEmpty{for (i in maybeEmpty) i};
    emptinessNonEmpty{for (i in nonEmpty) i};
}

@nomodel
void emptinessMaybeEmpty(Iterable<Integer> s){}

@nomodel
void emptinessNonEmpty(Iterable<Integer> s){}

// take straight from the spec tests
@nomodel
void emptinessParameterised<Element,Absent>(Iterable<Element,Absent> it) 
        given Absent satisfies Null {
    @type:"Iterable<Element,Absent>" value c1 = { for (e in it) e };
    @type:"Iterable<Tuple<Element,Element,Tuple<Element,Element,Empty>>,Absent>" value c2 = { for (e in it) for (f in it) [e,f] };
    @type:"Iterable<Element&Object,Null>" value c3 = { for (e in it) if (exists e) e };
    @type:"Sequential<Element>" value c4 = [ for (e in it) e ];
}