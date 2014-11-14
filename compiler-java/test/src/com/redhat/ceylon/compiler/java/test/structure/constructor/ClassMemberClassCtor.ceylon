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
class ClassMemberClassCtor() {
    class Member {
        shared new Member(Integer i=1) {
        }
        shared new Other(Integer i=2) {
        }
    }
    shared class SharedMember {
        shared new SharedMember(Integer i=3) {
        }
        shared new Other(Integer i=4) {
        }
    }
    void use(ClassMemberClassCtor other) {
        Member(0);
        Member{i=1;};
        Member{};
        Member.Member(2);
        Member.Member{i=3;};
        Member.Member{};
        Member.Other(4);
        Member.Other{i=5;};
        Member.Other{};
        SharedMember(6);
        SharedMember{i=7;};
        SharedMember.SharedMember(8);
        SharedMember.SharedMember{i=9;};
        SharedMember.Other(10);
        SharedMember.Other{i=11;};
        
        this.Member(12);
        this.Member{i=13;};
        this.Member{};
        this.Member.Member(14);
        this.Member.Member{i=15;};
        this.Member.Member{};
        this.Member.Other(16);
        this.Member.Other{i=17;};
        this.Member.Other{};
        this.SharedMember(18);
        this.SharedMember{i=19;};
        this.SharedMember.SharedMember(20);
        this.SharedMember.SharedMember{i=21;};
        this.SharedMember.Other(22);
        this.SharedMember.Other{i=23;};
        
        other.Member(24);
        other.Member{i=25;};
        other.Member{};
        other.Member.Member(26);
        other.Member.Member{i=27;};
        other.Member.Member{};
        other.Member.Other(28);
        other.Member.Other{i=29;};
        other.Member.Other{};
     }
    
}
void useClassMemberClassCtor(ClassMemberClassCtor other) {
    value i = 0;
    other.SharedMember(i);
    other.SharedMember{i=1;};
    other.SharedMember.SharedMember(i);
    other.SharedMember.SharedMember{i=1;};
    other.SharedMember.Other(i);
    other.SharedMember.Other{i=1;};
}
