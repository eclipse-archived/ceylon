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
// simplest test case

@noanno
class Bug667TopClass() {
    default shared String? get { throw; }
}

@noanno
class Bug667BottomClass(Nothing n) extends Bug667TopClass() {    
    shared actual Nothing get { return n; }
    shared String? perhaps(){
        return n;
    }
}

// original test case

@noanno
interface Bug667Interface<T> {
    default shared T? get { throw; }
}
 
@noanno
class Bug667Class<Nada>(Nada n) satisfies Bug667Interface<String> 
        given Nada satisfies Null {    
    shared actual Nada get { return n; }
}

@noanno
void bug667Method<Nada>(Nada n) given Nada satisfies Null {
    object x satisfies Bug667Interface<String> {    
        shared actual Nada get { return n; }
    }
}
