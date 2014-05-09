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
class Bug1108(shared String foo, shared String() bar) {}
object process {
    shared String readLine() => nothing;
}
@noanno
void bug1108() {
    class Bug1108Inner(shared String foo, shared String() bar) {}
    String f1 = Bug1108("", process.readLine).foo;
    String b1 = Bug1108("", process.readLine).bar();
    String f2 = Bug1108Inner("", process.readLine).foo;
    String b2 = Bug1108Inner("", process.readLine).bar();
    void m() {
        class Bug1108Local(shared String foo, shared String() bar) {}
        String f3 = Bug1108("", process.readLine).foo;
        String b3 = Bug1108("", process.readLine).bar();
        String f4 = Bug1108Inner("", process.readLine).foo;
        String b4 = Bug1108Inner("", process.readLine).bar();
        String f5 = Bug1108Local("", process.readLine).foo;
        String b5 = Bug1108Local("", process.readLine).bar();
    }
}
