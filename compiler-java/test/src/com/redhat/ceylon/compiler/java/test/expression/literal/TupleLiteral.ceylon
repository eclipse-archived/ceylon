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
class TupleLiteral<T>() {
    shared void m() {
        [] empty = [];
        [String] stringSingleton = [""];
        [Integer] intSingleton = [1];
        [T] tSingleton = [nothing];
        [String,String] stringPair = ["",""];
        
        [String*] comprehension = [ for (s in stringPair) s ];
        [String+] nonEmptyComprehension = [ for (s in stringPair) s ];
        
        [String,String] spreadTuple1 = ["s", *stringSingleton];
        [String,Integer] spreadTuple2 = ["i", *intSingleton];
        [Boolean,String,Integer] spreadTuple3 = [true, *["bar", 1]];
        [String,String,String] spreadTuple4 = ["foo", *["bar", "baz"]];
        
        [String,String|Integer*] spreadTuple5 = ["foo2", *spreadTuple2];
        [String] spreadTuple6 = ["s", *[]];
        
        {Integer+} iter = nothing;
        [Integer+] spreadIter = [*iter];
        [Integer*] spreadIter2 = [*iter];
        
        {Integer*} iter2 = nothing;
        [Integer*] spreadIter3 = [*iter2];
    }
}