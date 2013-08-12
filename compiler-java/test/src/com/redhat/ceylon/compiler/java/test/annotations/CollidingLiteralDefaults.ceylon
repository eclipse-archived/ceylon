import ceylon.language.model{SequencedAnnotation}
import ceylon.language.model.declaration{FunctionDeclaration}

@nomodel
final annotation class CollidingLiteralDefaults(shared Integer i1, shared Integer i2) 
    satisfies SequencedAnnotation<CollidingLiteralDefaults, FunctionDeclaration>{
}
@nomodel
annotation CollidingLiteralDefaults collidingLiteralDefaults(Integer i1=2) => CollidingLiteralDefaults(1, i1);

@nomodel
collidingLiteralDefaults
void collidingLiteralDefaults_callsite(){
}