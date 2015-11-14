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
class ComprehensionMemberQualClass() {
    shared Integer next(){
        return 1;
    }
    shared Integer i = 1;
    void m(){
        value l = {for(i in {1}) next() + this.i};
    }
    shared class Inner() {
        shared Integer next(){
            return 1;
        }
        shared Integer i = 1;
        void m(){
            value l = {for(i in {1}) next()  + outer.next() + this.i};
        }
    }
}
@noanno
interface ComprehensionMemberQualInterfaceFormal {
    shared formal Integer next();
    shared formal Integer i;
    void m(){
        value l = {for(i in {1}) next() + this.i};
    }
}
@noanno
interface ComprehensionMemberQualInterfaceNonshared {
    Integer next() => 1;
    Integer i => 1;
    void m(){
        value l = {for(i in {1}) next() + this.i};
    }
}
@noanno
void comprehensionMemberQualLocal() {
    class Local() {
        shared Integer next(){
            return 1;
        }
        shared Integer i = 1;
        void m(){
            value l = {for(i in {1}) next() + this.i};
        }
    }
    Integer next(){
        return 1;
    }
    class Local2() {
        void m(){
            value l = {for(i in {1}) next()};
        }
    }
}
/*@noanno
class ComprehensionThisNonSharedMethods() {
    Integer next(){
        return 1;
    }
    Integer i {
        return 1;
    }
    void m(){
        value l = {for(i in {1}) next() + this.i};
    }
}
@noanno
class ComprehensionFoo() {
    Integer next(){
        return 1;
    }
    Integer i {
        return 1;
    }
    void m(){
        value l = {for(iter in {{}}) for (it in iter) "" };
    }
}*/