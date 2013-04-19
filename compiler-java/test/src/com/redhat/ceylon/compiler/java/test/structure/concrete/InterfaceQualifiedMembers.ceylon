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
interface InterfaceQualifiedIncrement {
    shared formal variable Integer i1;
    shared formal Integer m1();
    
    shared default Integer i2 {
        return 0;
    }
    assign i2 {
    }
    shared default Integer m2() {
        return 0;
    }
    
    shared Integer i3 {
        return 0;
    }
    assign i3 {
    }
    shared Integer m3() {
        return 0;
    } 
    
    Integer i4 {
        return 0;
    } assign i4 {
        
    }
    Integer m4() {
        return 0;
    }
    
    void inside(InterfaceQualifiedIncrement ii) {
        variable Integer x;
        x = ii.m1();
        x = ii.m2();
        x = ii.m3();
        x = ii.m4(); 
        x = 1 + ii.i1;
        x = 1 + ii.i2;
        x = 1 + ii.i3;
        x = 1 + ii.i4;
        ii.i1++;
        ii.i2++;
        ii.i3++;
        ii.i4++;
        ++ii.i1;
        ++ii.i2;
        ++ii.i3;
        ++ii.i4;
    }
    
    class InnerC() {
        void inside(InterfaceQualifiedIncrement ii) {
            variable Integer x;
            x = ii.m1();
            x = ii.m2();
            x = ii.m3();
            x = ii.m4(); 
            x = 1 + ii.i1;
            x = 1 + ii.i2;
            x = 1 + ii.i3;
            x = 1 + ii.i4;
            ii.i1++;
            ii.i2++;
            ii.i3++;
            ii.i4++;
            ++ii.i1;
            ++ii.i2;
            ++ii.i3;
            ++ii.i4;
        }
    }
    
    interface InnerI {
        void inside(InterfaceQualifiedIncrement ii) {
            variable Integer x;
            x = ii.m1();
            x = ii.m2();
            x = ii.m3();
            x = ii.m4(); 
            x = 1 + ii.i1;
            x = 1 + ii.i2;
            x = 1 + ii.i3;
            x = 1 + ii.i4;
            ii.i1++;
            ii.i2++;
            ii.i3++;
            ii.i4++;
            ++ii.i1;
            ++ii.i2;
            ++ii.i3;
            ++ii.i4;
        }
    }
}
@noanno
void outsideInterfaceQualifiedIncrement(InterfaceQualifiedIncrement ii) {
    variable Integer x;
    x = ii.m1();
    x = ii.m2();
    x = ii.m3();
    //x = ii.m4(); m4 not shared 
    x = 1 + ii.i1;
    x = 1 + ii.i2;
    x = 1 + ii.i3;
    // x = 1 + ii.i4; i4 not shared
    ii.i1++;
    ii.i2++;
    ii.i3++;
    // ii.i4++; i4 not shared
    ++ii.i1;
    ++ii.i2;
    ++ii.i3;
    //++ii.i4; i4 not shared
}

