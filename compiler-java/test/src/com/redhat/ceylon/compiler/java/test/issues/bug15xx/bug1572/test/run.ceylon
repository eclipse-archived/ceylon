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
import ceylon.language.meta {
    modules
}
import ceylon.language.meta.declaration {
    Module,
    ClassDeclaration
}

import org.jboss.modules {
    JBossModule=Module {
        ceylonModuleLoader=callerModuleLoader
    },
    ModuleIdentifier {
        createModuleIdentifier=create
    }
}

Module? loadModule(String name, String version) {
    loadModuleInClassPath(name, version);
    return modules.find(name, version);
}

void loadModuleInClassPath(String modName, String modVersion) {
    value modIdentifier = createModuleIdentifier(modName, modVersion);
    value mod = ceylonModuleLoader.loadModule(modIdentifier);
}

shared void run() {
    assert(exists mod = loadModule("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1572.mod", "1"));
    for (pkg in mod.members) {
        for (klass in pkg.annotatedMembers<ClassDeclaration, SharedAnnotation>()){
            print(klass.annotatedMemberDeclarations<ClassDeclaration, SharedAnnotation>());
        }
    }
}