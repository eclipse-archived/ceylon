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
import java.io{IOException}

class CeylonException() extends JavaException() {
}
class CeylonExceptionMessage() extends JavaExceptionMessage() {
}
class CeylonError() extends JavaError() {
}
class CeylonErrorMessage() extends JavaErrorMessage() {
}
class CeylonExceptionOverrides() extends JavaException() {
    shared actual String message => "";
}
class CeylonExceptionMessageOverrides() extends JavaExceptionMessage() {
    shared actual String message => "";
}
class CeylonErrorOverrides() extends JavaError() {
    shared actual String message => "";
}
class CeylonErrorMessageOverrides() extends JavaErrorMessage() {
    shared actual String message => "";
}

void javaExceptionMessage() {
    // these should have null check
    variable String s = IOException().message;
    s = JavaException().message;
    s = JavaError().message;
    s = JavaExceptionMessage().message;
    s = JavaErrorMessage().message;
    
    s = CeylonException().message;
    s = CeylonError().message;
    s = CeylonExceptionMessage().message;
    s = CeylonErrorMessage().message;
    
    // these should not have null check
    s = CeylonExceptionOverrides().message;
    s = CeylonErrorOverrides().message;
    s = CeylonExceptionMessageOverrides().message;
    s = CeylonErrorMessageOverrides().message;
}