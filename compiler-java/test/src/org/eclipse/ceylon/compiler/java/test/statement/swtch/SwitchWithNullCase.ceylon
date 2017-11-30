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
class SwitchWithNullCase(Boolean? s, String? s2, Character? s3, String|Character? s4) {
        
        switch(s) 
        case(true) {
            print("true");
        }
        case (false) {
            print("false");
        }
        case (null) {
            print("null");
        }
        
        switch(s) 
        case(true) {
            print("true");
        }
        case (false,null) {
            print("false or null");
        }
        
        switch(s)
        case (false,null) {
            print("false or null");
        }
        case(true) {
            print("true");
        }
        
        switch(s2)
        case("true") {
            print("true");
        }
        case ("false",null) {
            print("false or null");
        }
        else {
            print("else");
        }
        
        switch(s2)
        case ("false",null) {
            print("false or null");
        }
        case("true") {
            print("true");
        }
        else {
            print("else");
        }
        
        switch(s3)
        case('t') {
            print("true");
        }
        case ('f',null) {
            print("false or null");
        }
        else {
            print("else");
        }
        
        switch(s3)
        case ('f',null) {
            print("false or null");
        }
        case('t') {
            print("true");
        }
        else {
            print("else");
        }
        
        switch(s4)
        case('t') {
            print("true");
        }
        case ("f",'x',null) {
            print("false or null");
        }
        else {
            print("else");
        }
        
        switch(s3)
        case ('f','x',null) {
            print("false or null");
        }
        case('t') {
            print("true");
        }
        else {
            print("else");
        }
}
@noanno
void switchWithNullCase() {
    SwitchWithNullCase(null, null, null, null);
}