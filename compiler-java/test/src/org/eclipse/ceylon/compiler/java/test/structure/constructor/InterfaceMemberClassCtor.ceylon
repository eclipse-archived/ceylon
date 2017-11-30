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
interface InterfaceMemberClassCtor {
    class Member {
        shared new (Integer i=0) {
        }
        shared new other(Integer i=1) {
        }
    }
    shared class SharedMember {
        shared new (Integer i=2) {
        }
        shared new other(Integer i=3) {
        }
    }
    void use(InterfaceMemberClassCtor other) {
        Member(0);
        Member(1);
        Member.other(2);
        SharedMember(3);
        SharedMember(4);
        SharedMember.other(5);
         
        this.Member(6);
        this.Member(7);
        this.Member.other(8);
        this.SharedMember(9);
        this.SharedMember(10);
        this.SharedMember.other(11);
        
        other.Member(12);
        other.Member(13);
        other.Member.other(14);
        other.SharedMember(15);
        other.SharedMember(16);
        other.SharedMember.other(17);
    }
}
class InterfaceMemberClassCtorImpl() satisfies InterfaceMemberClassCtor {
    
}

void useInterfaceMemberClassCtor(InterfaceMemberClassCtor other) {
    other.SharedMember(0);
    other.SharedMember(0);
    other.SharedMember.other(0);
}
