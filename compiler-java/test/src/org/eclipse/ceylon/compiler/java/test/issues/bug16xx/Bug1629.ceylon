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
class Bug1629(Object o, Anything x, [Character*] s) {
    assert (is String captured0 = o);
    assert (exists captured1 = captured0.first);
    assert (nonempty captured2 = s);
    
    assert (is String captured10 = o,
        exists captured11 = "a".first,
        nonempty captured12 = s);
    
    assert( is String o,
        exists x,
        nonempty s);
        
    
    shared String attribute0 => captured0;
    shared Character attribute1 => captured1;
    shared [Character*] attribute2 => captured2;
    
    shared String attribute10 => captured10;
    shared Character attribute11 => captured11;
    shared [Character*] attribute12 => captured12;
    
    shared String attribute20 => o;
    shared Object attribute21 => x;
    shared [Character*] attribute22 => s;
}