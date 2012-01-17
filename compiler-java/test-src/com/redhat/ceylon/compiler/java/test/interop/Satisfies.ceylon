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
import java.io{...}
import java.lang{JBoolean = Boolean}

@nomodel
@error
class SatisfiesFileFilter() satisfies FileFilter {
    @error
    shared actual Boolean accept(@error File f) {
        return true;
    }
}

@nomodel
@error
class SatisfiesFilenameFilter() satisfies FilenameFilter {
    @error
    shared actual Boolean accept(@error File dir, String name) {
        return true;
    }
}

@nomodel
@error
void test() {
    @error
    File f1 = File("file1");
    @error
    f1.listFiles(SatisfiesFileFilter());
    @error
    f1.listFiles(SatisfiesFilenameFilter());
}

@nomodel
class JavaInterfaceImpl() satisfies JavaInterface<Boolean> {
    shared actual Boolean booleanMethod(Boolean b){ return b; }
    shared actual JBoolean boxedBooleanMethod(JBoolean b){ return b; }
    shared actual Boolean ceylonBooleanMethod(Boolean b){ return b; }
    shared actual Boolean classTypeParamMethod(Boolean b){ return b; }

    shared actual M methodTypeParamMethod<M>(M b){ return b; }
}