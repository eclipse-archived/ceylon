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
class Li2423<out Element>(shared Element* items) {}
@noanno
shared void bug2423() {
    Li2423<Li2423<String>> f = Li2423(Li2423("#1"));
    Li2423<String>? ns = f.items[0];
    //value n = if (is Li2423<Anything> ns) then ns.items else (ns of Li2423<String>?); // ok
    String[]? o = if (is Li2423<Anything> ns) then ns.items else ns; // error
    
    //Incompatible types
    //required: ceylon.language.Sequential<? extends ceylon.language.String>
    //found:    simple.Li<capture#1 of ? extends ceylon.language.String>
}