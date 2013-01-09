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
 
shared String constAbc = "abcdef";

shared String constAbcSingleQuoted = 'abcdef';

shared String constLoremIpsumSingleLine = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";

shared String constLoremIpsumMultiLine = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, 
                                          sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
                                          Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
shared String[] constAbcArray = [
    "abc", 
    "def",
    "xyz"];
    
shared Character constCharA = `A`;

shared Character constCharAngstromSign = `\{#212B}`;
    
shared Integer constNumZero = 0;

shared Integer constNumTwo = constNumZero + 1 + 1;

shared Integer constNumMilion = 1_000_000;

shared Float constNumPI = 3.14;