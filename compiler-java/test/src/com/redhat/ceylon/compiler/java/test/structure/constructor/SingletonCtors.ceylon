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
class SingletonCtors {
    shared actual String string;
    shared new one {
        string="one";
    }
    shared new two {
        string="two";
    }
    
    shared Integer use(SingletonCtors inst) {
        switch(inst)
        case(SingletonCtors.one) {
            return 1;
        }
        case(SingletonCtors.two) {
            return 2;
        }
        else{
            return 3;
        }
    }
    shared Integer use2(SingletonCtors inst) {
        switch(inst)
        case(one) {
            return 1;
        }
        case(two) {
            return 2;
        }
        else{
            return 3;
        }
    }
}
@noanno
class ClassMemberSingletonCtors() {
    shared class MemberClass {
        shared actual String string;
        shared new one {
            string="one";
        }
        new nonShared {
            string="nonShared";
        }
    }
    void use(ClassMemberSingletonCtors other) {
        assert(other.MemberClass.one != MemberClass.one);
        assert(other.MemberClass.one != this.MemberClass.one);
    }
}
@noanno
Basic localSingletonCtors() {
    class LocalClass {
        shared actual String string;
        shared new one {
            string="one";
        }
    }
    return LocalClass.one;
}
@noanno
void singletonCtors() {
    Basic o1 = localSingletonCtors();
    Basic o2 = localSingletonCtors();
    assert(!o1===o2);
}