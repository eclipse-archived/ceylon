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
        value i = 0;
        Member(i);
        Member{i=1;};
        Member{};
        Member.Member(i);
        Member.Member{i=1;};
        Member.Member{};
        Member.Other(i);
        Member.Other{i=1;};
        Member.Other{};
        SharedMember(i);
        SharedMember{i=1;};
        SharedMember.SharedMember(i);
        SharedMember.SharedMember{i=1;};
        SharedMember.Other(i);
        SharedMember.Other{i=1;};
         
        this.Member(i);
        this.Member{i=1;};
        this.Member{};
        this.Member.Member(i);
        this.Member.Member{i=1;};
        this.Member.Member{};
        this.Member.Other(i);
        this.Member.Other{i=1;};
        this.Member.Other{};
        this.SharedMember(i);
        this.SharedMember{i=1;};
        this.SharedMember.SharedMember(i);
        this.SharedMember.SharedMember{i=1;};
        this.SharedMember.Other(i);
        this.SharedMember.Other{i=1;};
        
        other.Member(i);
        other.Member{i=1;};
        other.Member{};
        other.Member.Member(i);
        other.Member.Member{i=1;};
        other.Member.Member{};
        other.Member.Other(i);
        other.Member.Other{i=1;};
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
