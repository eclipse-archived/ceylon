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
interface NamedArgumentInvocationInterfaceMember {
    class Inner(foo, bar = foo.size) {
        shared String foo;
        shared Integer bar;
    }
    class InnerWithCtor {
        shared String foo;
        shared Integer bar;
        shared new () {
            this.foo = "";
            this.bar = 0;
        }
        shared new ctor(String foo, Integer bar = foo.size) {
            this.foo = foo;
            this.bar = bar;
        }
    }
    
    void func() {
        value i = Inner {
            foo = "foo";
        };
        value i2 = this.Inner {
            foo = "foo";
        };
        value i3 = InnerWithCtor.ctor {
            foo = "foo";
        };
        value i4 = this.InnerWithCtor.ctor {
            foo = "foo";
        };
    }
}