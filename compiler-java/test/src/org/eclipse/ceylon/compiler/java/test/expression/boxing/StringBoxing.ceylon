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
class StringBoxing(){
    void m() {
        String s1 = "TEST";
        String s2 = s1.lowercased;
        String s3 = upper(s2);
        String? s4 = upper2(s3);
        String s5 = upper3(s4);
        String s6 = upper3(s4).uppercased;
        s6.compare(s5);
        String s7 = s5.plus(s6);
        
        value b = "ABC".coalesced=="ABC";
    }
    String upper(String s) {
        return s.uppercased;
    }
    String? upper2(String? s) {
        if (exists s) {
            return s.uppercased;
        }
        return null;
    }
    String upper3(String? s) {
        if (exists s) {
            return s.uppercased;
        } else {
            return "";
        }
    }
}