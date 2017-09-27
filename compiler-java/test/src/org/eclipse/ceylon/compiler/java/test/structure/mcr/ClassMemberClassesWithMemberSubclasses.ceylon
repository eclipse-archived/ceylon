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
abstract class ClassDefaultMemberClassWithMemberSubclass() {
    shared default class Default(Integer i) {
    }
    shared formal class Formal(Integer i) {
    }
    shared class Shared(Integer i) {
    }
    class NonShared(Integer i) {
    }
    class DefaultSub(Integer i) extends Default(i){
    }
    class FormalSub(Integer i) extends Formal(i){
    }
    class SharedSub(Integer i) extends Shared(i){
    }
    class NonSharedSub(Integer i) extends NonShared(i){
    }
    void m() {
        class LocalDefaultSub(Integer i) extends Default(i) {
        }
        class LocalFormalSub(Integer i) extends Formal(i) {
        }
        class LocalSharedSub(Integer i) extends Shared(i) {
        }
        class LocalNonSharedSub(Integer i) extends NonShared(i) {
        }
    } 
}