import ceylon.language.meta.declaration { NestableDeclaration }

@test
shared void bug300() {
    variable value first=true;
    for(decl in `package ceylon.language`.members<NestableDeclaration>()){
        if (first) {
          first=false;
        } else {
          process.write(", ");
        }
        process.write(decl.name);
    }
    process.writeLine();
    assert(`package ceylon.language`.members<NestableDeclaration>().size > 0);
}
