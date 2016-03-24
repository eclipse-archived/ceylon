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
shared interface Bug801_Interface {
    shared interface MemberInterface {}
}

@noanno
shared class Bug801_Class() {
    shared interface MemberInterface {}
}

@noanno
shared class Bug801_ImplClass() satisfies Bug801_Interface {
    shared class MemberInterfaceImpl() satisfies MemberInterface {}
    shared interface MemberInterface2 satisfies MemberInterface {}
    shared class Foo(){
        shared class MemberInterfaceImpl() satisfies MemberInterface {}
    }
    shared class Bar() satisfies Bug801_Interface {
        shared class MemberInterfaceImpl() satisfies MemberInterface {}
    }
    void method(){
        class MemberInterfaceImpl() satisfies MemberInterface {}
        interface MemberInterface2 satisfies MemberInterface {}
    }
}

@noanno
shared class Bug801_ImplClass2() extends Bug801_Class() {
    shared class MemberInterfaceImpl() satisfies MemberInterface {}
    shared interface MemberInterface2 satisfies MemberInterface {}
    shared class Foo(){
        shared class MemberInterfaceImpl() satisfies MemberInterface {}
    }
    shared class Bar() satisfies Bug801_Interface {
        shared class MemberInterfaceImpl() satisfies MemberInterface {}
    }
    void method(){
        class MemberInterfaceImpl() satisfies MemberInterface {}
        interface MemberInterface2 satisfies MemberInterface {}
    }
}
