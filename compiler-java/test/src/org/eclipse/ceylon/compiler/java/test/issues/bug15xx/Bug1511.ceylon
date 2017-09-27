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
interface Bug1511A{}
@noanno
interface Bug1511B{}

@noanno
void bug1511Loop(Object[] l) {
    value data = {
        for(v1 in l)
            for(v2 in l)
                if(is Bug1511A v1) // Notice that this is now inside the second for-loop
                    if (is Bug1511B v2)
                        [v1, v2]
    };
    assert(data.empty);
    assert(data.string == "{}");
}

@noanno
void bug1511(){
    bug1511Loop([]);
}
