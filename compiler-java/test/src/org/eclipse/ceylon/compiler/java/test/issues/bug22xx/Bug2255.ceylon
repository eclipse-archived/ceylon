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
import java.util { Locale { fr = \iFRENCH } }

@noanno
void bug2254() {
    String? name1 = if (true) then "a" else fr.displayName;
    String? name1b = if (true) then fr.displayName else fr.displayName;
    String name1c = if (true) then fr.displayName else fr.displayName;
    Object name1d = if (true) then fr.displayName else fr.displayName;
    Anything name1e = if (true) then fr.displayName else fr.displayName;
    
    String? name2 = true then "a" else fr.displayName;
    String? name3 = true then fr.displayName else fr.displayName;
    String name3b = true then fr.displayName else fr.displayName;
    Object name3c = true then fr.displayName else fr.displayName;
    Anything name3d = true then fr.displayName else fr.displayName;
    
    String? name4 = switch (true) case(true) "a" else fr.displayName;
    String? name4b = switch (true) case(true) fr.displayName else fr.displayName;
    String name4c = switch (true) case(true) fr.displayName else fr.displayName;
    Object name4d = switch (true) case(true) fr.displayName else fr.displayName;
    Anything name4e = switch (true) case(true) fr.displayName else fr.displayName;

}
