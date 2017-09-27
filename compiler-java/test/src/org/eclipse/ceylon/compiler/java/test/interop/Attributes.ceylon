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
void attributes() {
    JavaBean java = JavaBean();
    variable Boolean sync;
    sync = java.booleanWithGet;
    java.booleanWithGet = false;

    sync = java.booleanWithIs;
    java.booleanWithIs = false;

    sync = java.oldStyle();
    java.setOldStyle(false);

    variable String syncStr;
    syncStr = java.url;
    java.url = "";

    syncStr = java.urlEncoderForHTML;
    java.urlEncoderForHTML = "";

    sync = java.confusedProperty;
    java.setConfusedProperty("");

    variable Integer syncInt;
    syncInt = java.épardaud;
    java.épardaud = 0;

    sync = java.confusedAttr1;
    java.confusedAttr1 = sync;
    syncInt = java.getConfusedAttr1();
    
    syncInt = java.confusedAttr2;
    java.confusedAttr2 = syncInt;
    sync = java.isConfusedAttr2();

    sync = java.confusedAttr3;
    java.confusedAttr3 = sync;
    sync = java.getConfusedAttr3();
    java.setConfusedAttr3(syncInt);

    syncInt = java.confusedAttr4;
    java.confusedAttr4 = syncInt;
    sync = java.isConfusedAttr4();
    java.setConfusedAttr4(sync);
}

@noanno
class CeylonAttributes() extends JavaBean() {

    shared variable actual Boolean booleanWithGet = false;
    shared variable actual Boolean booleanWithIs = false;
    shared variable actual String url = "";
    shared variable actual String urlEncoderForHTML = "";
    
    shared variable actual Boolean confusedAttr1 = false;
    shared actual Integer getConfusedAttr1() => 1;
    
    shared variable actual Integer confusedAttr2 = 1;
    shared actual Boolean isConfusedAttr2() => false;
    
    shared variable actual Boolean confusedAttr3 = false;
    shared actual Boolean getConfusedAttr3() => false;
    shared actual void setConfusedAttr3(Integer i){}
    
    shared variable actual Integer confusedAttr4 = 1;
    shared actual Boolean isConfusedAttr4() => false;
    shared actual void setConfusedAttr4(Boolean b){}

    void m(){
        variable Boolean sync;
        sync = booleanWithGet;
        booleanWithGet = false;

        sync = booleanWithIs;
        booleanWithIs = false;

        sync = oldStyle();
        setOldStyle(false);

        variable String syncStr;
        syncStr = url;
        url = "";

        syncStr = urlEncoderForHTML;
        urlEncoderForHTML = "";
    }
}