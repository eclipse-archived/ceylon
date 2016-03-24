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
class FuctionalParameter(
    void voidFp(String arg),
    String stringFp(String arg),
    shared void sharedVoidFp(String arg),
    shared String sharedStringFp(String arg)) {
 
    // Check we can call the class functional parameter
    shared void callClassFps() {
        voidFp("");
        voidFp{arg="";};
        sharedVoidFp("");
        sharedVoidFp{arg="";};
        variable String s;
        s = stringFp("");
        s = stringFp{arg="";};
        s = sharedStringFp("");
        s = sharedStringFp{arg="";};
    }
 
    // Check we can call a method functional parameter
    shared void callMethodFps(void voidFp1(String arg),
            String stringFp1(String arg)) {
        voidFp1("");
        voidFp1{ arg = ""; };
        variable String s;
        s = stringFp1("");
        s = stringFp1{ arg = ""; };
    }

    // Method refs taken inside the class can access the Callable field directly.
    @noanno
    void methodRefsFromInside(FuctionalParameter instance) {
        value sharedVoidRef = instance.sharedVoidFp;
        value sharedStringRef = instance.sharedStringFp;
    }    
    
}
// Method refs taken outside the class need to generate an AbstractCallable
@noanno
void methodRefsFromOutside(FuctionalParameter instance) {
    value sharedVoidRef = instance.sharedVoidFp;
    value sharedStringRef = instance.sharedStringFp;
}