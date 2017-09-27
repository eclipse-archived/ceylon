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
        shared new (Integer i=1) {
        }
        shared new other(Integer i=2) {
        }
    }
    shared class SharedMember {
        shared new (Integer i=3) {
        }
        shared new other(Integer i=4) {
        }
    }
    void use(ClassMemberClassCtor other) {
        Member(0);
        Member{i=1;};
        Member{};
        Member(2);
        Member{i=3;};
        Member{};
        Member.other(4);
        Member.other{i=5;};
        Member.other{};
        SharedMember(6);
        SharedMember{i=7;};
        SharedMember(8);
        SharedMember{i=9;};
        SharedMember.other(10);
        SharedMember.other{i=11;};
        
        this.Member(12);
        this.Member{i=13;};
        this.Member{};
        this.Member(14);
        this.Member{i=15;};
        this.Member{};
        this.Member.other(16);
        this.Member.other{i=17;};
        this.Member.other{};
        this.SharedMember(18);
        this.SharedMember{i=19;};
        this.SharedMember(20);
        this.SharedMember{i=21;};
        this.SharedMember.other(22);
        this.SharedMember.other{i=23;};
        
        other.Member(24);
        other.Member{i=25;};
        other.Member{};
        other.Member(26);
        other.Member{i=27;};
        other.Member{};
        other.Member.other(28);
        other.Member.other{i=29;};
        other.Member.other{};
     }
    
}
void useClassMemberClassCtor(ClassMemberClassCtor other) {
    value i = 0;
    other.SharedMember(i);
    other.SharedMember{i=1;};
    other.SharedMember(i);
    other.SharedMember{i=1;};
    other.SharedMember.other(i);
    other.SharedMember.other{i=1;};
}
