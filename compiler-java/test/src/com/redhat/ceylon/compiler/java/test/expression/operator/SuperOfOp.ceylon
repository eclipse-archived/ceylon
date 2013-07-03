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
interface SuperOfTop {
    shared default Integer a {
        return 0;
    }
    assign a {
    }
    shared default Integer m() {
        return 0;
    }
}
@noanno
interface SuperOfLeft satisfies SuperOfTop {
    shared actual default Integer a {
        return 1;
    }
    assign a {
    }
    shared actual default Integer m() {
        return (super).m() + (super).a++;
    }
}
@noanno
interface SuperOfRight satisfies SuperOfTop {
    shared actual default Integer a {
        return 2;
    }
    assign a {
    }
    shared actual default Integer m() {
        return (super of SuperOfTop).m() + ((super) of SuperOfTop).a;
    }
}
@noanno
interface SuperOfBottom satisfies SuperOfLeft & SuperOfRight {
    shared actual default Integer a {
        return (super of SuperOfLeft).a + (super of SuperOfRight).a;
    }
    assign a {
        (super of SuperOfLeft).a = a;
        (super of SuperOfRight).a = a;
    }
    shared actual default Integer m() {
        (super of SuperOfLeft).a+=1;
        (super of SuperOfRight).a++;
        --(super of SuperOfRight).a;
        return (super of SuperOfLeft).m() 
            + (super of SuperOfLeft).a
            + (super of SuperOfRight).m() 
            + (super of SuperOfRight).a;
    }
}
@noanno
class SuperOfBottomClass() satisfies SuperOfLeft & SuperOfRight {
    shared actual default Integer a {
        return (super of SuperOfLeft).a + (super of SuperOfRight).a;
    }
    assign a {
        (super of SuperOfLeft).a = a;
        (super of SuperOfRight).a = a;
    }
    shared actual default Integer m() {
        return (super of SuperOfLeft).m() 
            + (super of SuperOfLeft).a
            + (super of SuperOfRight).m() 
            + (super of SuperOfRight).a;
    }
}
@noanno
class SuperOfLeftClass() satisfies SuperOfLeft {
    
    shared actual default Integer m() {
        return (super).m() + 2;
    }
}
@noanno
class SuperOfBottomLeftClass() extends SuperOfLeftClass() satisfies SuperOfRight {
    shared actual default Integer a {
        return (super of SuperOfLeft).a + (super of SuperOfLeftClass).a;
    } 
    assign a {
        (super of SuperOfLeft).a = a;
        (super of SuperOfLeft).a = a;
    }
    shared actual default Integer m() {
        return (super of SuperOfLeftClass).m()
            + (super of SuperOfLeftClass).a 
            + (super of SuperOfRight).m()
            + (super of SuperOfRight).a;
    }
}