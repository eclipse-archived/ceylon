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
class MethodIfSyntheticElse() {
    shared Integer m(Integer? int) {
        if(1 == 2){
            if (!exists int) {
                return 0;
            }
            return int+int;
        }
        return int else 2;
    }
    shared Integer m2(Integer? int) {
        if (exists int) {
            return int+1;
        }
        return 0;
    }
    shared Integer m3(Integer? int) {
        if (!is Integer int) {
            return 0;
        }
        return int+1;
    }
    shared Integer m4(Integer? int) {
        if (is Integer int) {
            return int+1;
        }
        return 0;
    }
    shared String m5(Integer|String int) {
        if (is Integer int) {
            return int.string;
        }
        return int;
    }
    shared String m6(Integer|String int) {
        if (!is Integer int) {
            return int;
        }
        return int.string;
    }
    shared String m7([String*] arg) {
        if (nonempty arg) {
            return arg.first;
        }
        [] empt = arg;
        return "";
    }
    shared String m8([String*] arg) {
        if (!nonempty arg) {
            [] empt = arg;
            return "";
        }
        return arg.first;
    }
}