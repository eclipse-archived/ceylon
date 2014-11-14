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
        shared new Member(Integer i=0) {
        }
        shared new Other(Integer i=1) {
        }
    }
    shared class SharedMember {
        shared new SharedMember(Integer i=2) {
        }
        shared new Other(Integer i=3) {
        }
    }
    void use(InterfaceMemberClassCtor other) {
        Member(0);
        Member.Member(1);
        Member.Other(2);
        SharedMember(3);
        SharedMember.SharedMember(4);
        SharedMember.Other(5);
         
        this.Member(6);
        this.Member.Member(7);
        this.Member.Other(8);
        this.SharedMember(9);
        this.SharedMember.SharedMember(10);
        this.SharedMember.Other(11);
        
        other.Member(12);
        other.Member.Member(13);
        other.Member.Other(14);
        other.SharedMember(15);
        other.SharedMember.SharedMember(16);
        other.SharedMember.Other(17);
    }
}
class InterfaceMemberClassCtorImpl() satisfies InterfaceMemberClassCtor {
    
}

void useInterfaceMemberClassCtor(InterfaceMemberClassCtor other) {
    other.SharedMember(0);
    other.SharedMember.SharedMember(0);
    other.SharedMember.Other(0);
}
