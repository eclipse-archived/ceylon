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
import java.lang{
    JBoolean = Boolean,
    JInteger = Integer, 
    JLong = Long
}

@noanno
class SatisfiesFileFilter() satisfies FileFilter {
    shared actual Boolean accept(File? f) {
        return true;
    }
}

@noanno
class SatisfiesFilenameFilter() satisfies FilenameFilter {
    shared actual Boolean accept(File? dir, String? name) {
        return true;
    }
}

@noanno
void test() {
    File f1 = File("file1");
    f1.listFiles(SatisfiesFileFilter());
    f1.listFiles(SatisfiesFilenameFilter());
}

@noanno
class JavaInterfaceImpl() satisfies JavaInterface<Boolean,Integer> {
    shared actual Boolean booleanMethod(Boolean b){ return true; }
    shared actual JBoolean boxedBooleanMethod(JBoolean? b){ return JBoolean(true); }
    shared actual Boolean ceylonBooleanMethod(Boolean? b){ return true; }
    shared actual Boolean classTypeParamMethodB(Boolean? b){ return true; }

    shared actual Integer longMethod(Integer i){ return 1; }
    shared actual JLong boxedLongMethod(JLong? i){ return JLong(1); }
    shared actual Integer ceylonIntegerMethod(Integer? i){ return 1; }
    shared actual Integer classTypeParamMethodI(Integer? i){ return 1; }
    
    shared actual Integer intMethod(Integer i){ return 1; }
    shared actual JInteger boxedIntegerMethod(JInteger? i){ return JInteger(1); }
    
    shared actual String stringMethod(String? i){ return ""; }
    shared actual String ceylonStringMethod(String? i){ return ""; }

    shared actual M methodTypeParamMethod<M>(M? b){ return nothing; }
}