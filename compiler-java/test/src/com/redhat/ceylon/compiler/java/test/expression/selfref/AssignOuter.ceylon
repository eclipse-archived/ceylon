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
interface AssignOuter {
    Integer a {
        return 0;
    }
    assign a {
    }
    shared formal variable Integer b;
    shared default Integer c {
        return 0;
    }
    assign c {
    }
    shared formal variable Integer? d;
    interface I {
        shared default Integer aDefault {
            return outer.a;
        }
        assign aDefault {
            outer.a = aDefault;
        }
        shared default Integer bDefault {
            return outer.b;
        }
        assign bDefault {
            outer.b = bDefault;
        }
        shared default Integer cDefault {
            return outer.c;
        }
        assign cDefault {
            outer.c = cDefault;
        }
        void m() {
            outer.a++;
            outer.b++;
            outer.c++;
            ++outer.a;
            ++outer.b;
            ++outer.c;
        }
    }
}
@noanno
abstract class AssignOuterClass() {
     Integer a {
        return 0;
    }
    assign a {
    }
    shared formal variable Integer b;
    shared default Integer c {
        return 0;
    }
    assign c {
    }
    interface I {
        shared default Integer aDefault {
            return outer.a;
        }
        assign aDefault {
            outer.a = aDefault;
        }
        shared default Integer bDefault {
            return outer.b;
        }
        assign bDefault {
            outer.b = bDefault;
        }
        shared default Integer cDefault {
            return outer.c;
        }
        assign cDefault {
            outer.c = cDefault;
        }
        void m() {
            outer.a++;
            outer.b++;
            outer.c++;
            ++outer.a;
            ++outer.b;
            ++outer.c;
        }
    }
}