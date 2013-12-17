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
import java.util { Random }

@noanno
void bug1507() {
    try {
        Bug1507IntWrapper i;
        if (Random().nextBoolean()) {
            i = Bug1507IntWrapper(2);
        } else {
            for (j in 0..Random().nextInt(10)) {
                if (is Bug1507IntWrapper k = bug1507generateInt(j == 0)) {
                    i = k;
                    process.write("J is ``j``");
                    break;
                } else {
                    process.write("Nope");
                }
            } else {
                throw;
            }
        }
        process.write(i.wrappedInt.minus(1).string);
    } catch (e) {
        process.write(e.string);
    }
}

@noanno
class Bug1507IntWrapper(wrappedInt) {
    shared Integer wrappedInt;
    string => wrappedInt.string;
}

@noanno
Bug1507IntWrapper? bug1507generateInt(Boolean b) {
    if (b) {
        return Bug1507IntWrapper(5);
    }
    return null;
}