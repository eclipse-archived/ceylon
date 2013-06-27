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
"Alias for stub stuffs"
by("Tomas")
see(`AliasEntry<String>`)
deprecated
shared alias AliasStubs => StubInterface|StubClass|StubException|Null;

"Alias for <i>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</i>"
shared alias AliasEntry<T> given T satisfies Object => Entry<T, T>;

shared AliasStubs aliasStubs = null;

shared AliasEntry<String> aliasEntry = Entry("foo", "bar");

shared class StubClassWithAlias() {

    shared alias InnerAliasNumber => Integer|Float;
    
    shared InnerAliasNumber innerAliasNumber = 0;
    
    shared interface StubClassWithAliasList => List<StubClassWithAlias>;
    
    shared class StubClassWithAliasEntry(Integer i, StubClassWithAlias a) => Entry<Integer, StubClassWithAlias>(i, a); 
    
}