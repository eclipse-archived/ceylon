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
void switchValues(String? name, Integer num, Character ch) {
    if (exists name) {
        switch (name)
        case ("Tako", "Gavin") { print("Spain"); }
        case ("Emmanuel", "Stef", "David") { print("France"); }
        else { print("Somewhere else"); }
    }

    switch (name)
    case ("Tako", "Gavin") { print("Spain"); }
    case ("Emmanuel", "Stef", "David") { print("France"); }
    case (null) { print("Nowhere"); }
    else { print("Somewhere else"); }
    
    switch (num)
    case (-1) { print("negative identity"); }
    case (0) { print("additive identity"); }
    case (1) { print("multiplicative identity"); }
    else { print("something else"); }
    
    switch (ch)
    case('+') { print("plus"); }
    case('-') { print("minus"); }
    case('0', '1', '2', '3', '4', '5', '6', '7', '8', '9') { print("digit"); }
    else { print("invalid character"); }

    Anything val = num;
    switch(val)
    case (1) { print("integer 1"); }
    case ("1") { print("string 1"); }
    case ('1') { print("character 1"); }
    else { print("something else"); }
}