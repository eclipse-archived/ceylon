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
shared class Ref<T>(){
    shared void set(Object o){}
}
shared class FileViewProvider(){}

shared class ProgressManager{
    shared static ProgressManager instance = ProgressManager();
    
    shared new (){}
    
    shared void executeNonCancelableSection(Anything() f){}
}

shared class Application(){
    shared FileViewProvider runReadAction(Computable<FileViewProvider> c){
        return nothing;
    }
}

shared interface Computable<T> {
    shared formal T compute();
}

shared Application application = Application();

// should be Bug6840$1$1anonymous_1_
// but is    Bug6840$2$1anonymous_1_
shared class Bug6840() satisfies Correspondence<String, Boolean> {

    print(`Bug6840`);

    defines(String key) => true;
    get(String key) => true;
    
    shared void foo() {
        Ref<FileViewProvider> providerRef = Ref<FileViewProvider>();
        ProgressManager.instance.executeNonCancelableSection(()
            => providerRef.set(
            application.runReadAction(object satisfies Computable<FileViewProvider> {
                compute()
                        => nothing;
            })));
        }
}
