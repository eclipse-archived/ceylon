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
interface RVI_Covariant<out T> {
    shared formal T m();
    T privateFinal(){ return m(); }
    shared T sharedFinal(){ return m(); }
    shared default T sharedDefault(){ return m(); }
}
class RVI_Covariant_Super() satisfies RVI_Covariant<Object> {
    shared actual default Object m() {
        return "super";
    }
}
class RVI_Covariant_Sub() extends RVI_Covariant_Super() satisfies RVI_Covariant<String> {
    shared actual String m() {
        return "sub";
    }
}

shared void rvi_run(){ 
    RVI_Covariant<String> cov_string = RVI_Covariant_Sub();
    print(cov_string.m());
    RVI_Covariant<Object> cov_object = RVI_Covariant_Sub();
    print(cov_object.m());
    RVI_Covariant_Super cov_super = RVI_Covariant_Sub();
    print(cov_super.m());
    RVI_Covariant_Sub cov_sub = RVI_Covariant_Sub();
    print(cov_sub.m());
    
    variable String s;
    s = cov_string.sharedFinal();
    s = cov_string.sharedDefault();
    s = cov_sub.sharedFinal();
    s = cov_sub.sharedDefault();
}
