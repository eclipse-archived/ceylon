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
        shared new Member(Integer i) {
        }
        shared new Other(Integer i) {
        }
    }
    shared class SharedMember {
        shared new SharedMember(Integer i) {
        }
        shared new Other(Integer i) {
        }
    }
    void use(InterfaceMemberClassCtor other) {
        /*Member(0);
        Member.Member(0);
        Member.Other(0);*/
        SharedMember(0);
        SharedMember.SharedMember(0);
        /*SharedMember.Other(0);
         
        this.Member(0);
        this.Member.Member(0);
        this.Member.Other(0);*/
        this.SharedMember(0);
        /*this.SharedMember.SharedMember(0);
        this.SharedMember.Other(0);
        
        other.Member(0);
        other.Member.Member(0);
        other.Member.Other(0);
        other.SharedMember(0);
        other.SharedMember.SharedMember(0);
        other.SharedMember.Other(0);*/
    }
}/*
void useInterfaceMemberClassCtor(InterfaceMemberClassCtor other) {
    other.SharedMember(0);
    other.SharedMember.SharedMember(0);
    other.SharedMember.Other(0);
}
*/