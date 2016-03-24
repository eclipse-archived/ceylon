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
class CtorCaptureInit {
    String name = "Trompon";
    shared String sharedName = "Trompon";
    Integer init;
    shared Integer sharedInit;
    variable Integer count;
    shared variable Integer sharedCount;
    print(name);
    print(sharedName);
    
    String captured;
    String capturedByCtor;
    variable Integer local = 0;
    local+=2; 
    new withAttributes() {
        count = 0;
        sharedCount = 0;
        init = count;
        sharedInit = sharedCount;
        
        captured = "WithAttributes";
        capturedByCtor = "WithAttributes";
        print(capturedByCtor);
    }
    shared new (Integer initial) {
        count = initial;
        sharedCount = initial;
        init = initial;
        sharedInit = initial;
        
        captured = "ConstWithParameter";
    }
    void inc() {
        count++;
    }
    void reset() {
        count = init;
    }
    shared void sharedInc() {
        sharedCount++;
    }
    shared void sharedReset() {
        sharedCount = sharedInit;
    }
    shared String m() {
        return captured;
    }
}