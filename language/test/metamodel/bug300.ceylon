import ceylon.language.meta.declaration { NestableDeclaration }

@test
shared void bug300() {
    for(decl in `package ceylon.language`.members<NestableDeclaration>()){
        String s = decl.name;
    }
    assert(`package ceylon.language`.members<NestableDeclaration>().size > 0);
}
