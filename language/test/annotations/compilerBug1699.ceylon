import ceylon.language.meta{
    type, 
    annotations, optionalAnnotation, sequencedAnnotations
}

see(`given Whatever`)
void compilerBug1699Fn<Whatever>() {
}
see(`given Whatever`) 
class CompilerBug1699Class<Whatever>() {
}
see(`given Whatever`) 
interface CompilerBug1699Interface<Whatever> {
}
see(`given Whatever`) 
alias CompilerBug1699Alias<Whatever> => CompilerBug1699Interface<Whatever>;

@test
shared void compilerBug1699() {
    assert(exists fnexpect = `function compilerBug1699Fn`.getTypeParameterDeclaration("Whatever"),
        exists fnsee = annotations(`SeeAnnotation`, `function compilerBug1699Fn`).first,
        exists fnfirst = fnsee.programElements.first,
        fnexpect==fnfirst);
    
    assert(exists classexpect = `class CompilerBug1699Class`.getTypeParameterDeclaration("Whatever"),
        exists classsee = annotations(`SeeAnnotation`, `class CompilerBug1699Class`).first,
        exists classfirst = classsee.programElements.first,
        classexpect==classfirst);
    
    assert(exists ifaceexpect = `interface CompilerBug1699Interface`.getTypeParameterDeclaration("Whatever"),
        exists ifacesee = annotations(`SeeAnnotation`, `interface CompilerBug1699Interface`).first,
        exists ifacefirst = ifacesee.programElements.first,
        ifaceexpect==ifacefirst);
    
    assert(exists aliasexpect = `alias CompilerBug1699Alias`.getTypeParameterDeclaration("Whatever"),
        exists aliassee = annotations(`SeeAnnotation`, `alias CompilerBug1699Alias`).first,
        exists aliasfirst = aliassee.programElements.first,
        aliasexpect==aliasfirst);
    
}