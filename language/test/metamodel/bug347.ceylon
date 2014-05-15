import ceylon.language.meta { modules }
import ceylon.language.meta.declaration { ClassDeclaration }

shared final annotation class TestAnnotation()
        satisfies OptionalAnnotation<TestAnnotation, ClassDeclaration> {
}

@test
shared void bug347() {
    for (value m in modules.list) {
        if(m.name.startsWith("org.apache")){
            continue;
        }
        for (value member in m.members) {
            value components = member.annotatedMembers<ClassDeclaration, TestAnnotation>();
            assert(components == []);
        }
    }
}
