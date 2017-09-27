import ceylon.language.meta { modules }

import com.ceylon.java9 { Test }
import ceylon.language.meta.declaration {
    NestableDeclaration
}

shared void run(){
    print("Hello from Ceylon!");
    for(mod in modules.list){
        print("Loaded module: ``mod.name``");
    }
    print(`module`);
    print(`package`.members<NestableDeclaration>());
    print(`function run`);
    Test();
}