import ceylon.language.meta.declaration { NestableDeclaration }
import jvm { Holder { annot, Annot }}

@test
annot
shared void bug6656() {
    print(`package`.annotatedMembers<NestableDeclaration, Annot>());
    print(`class Holder`.annotatedMemberDeclarations<NestableDeclaration, Annot>());
    print(`function bug6656`.annotations());
}