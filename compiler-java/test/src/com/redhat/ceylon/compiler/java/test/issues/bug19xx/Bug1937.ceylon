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
void bug1937_check(Boolean boolean) {}

@noanno
void bug1937(){
    bug1937_check(if (true) then false else true);
    Boolean b = if (true) then false else true;

    Object obj="Hey";
    value b1 = !(if (is Integer obj) then obj.successor else null) exists;
    value b2 = if (is String obj, exists c=obj[2], c=='y') then true else false;
    value b3 = if (obj=="HEY") then false else if (obj==1) then false else true;
    value b4 = if (is Integer obj) then false else if (is String obj, exists c=obj[2], c=='y') then true else false;
    value b5 = if (obj=="nay") then false else if (is String obj, exists c=obj[2], c=='y') then true else false;
    
    bug1937_check(switch (1) case (1) false else true);
    Boolean s = switch (1) case (1) false else true;
}
