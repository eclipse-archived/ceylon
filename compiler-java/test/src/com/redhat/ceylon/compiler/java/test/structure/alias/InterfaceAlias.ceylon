shared interface InterfaceAlias_Foo{}

shared interface InterfaceAlias => InterfaceAlias_Foo;

@nomodel
void interfaceAliasMethod(InterfaceAlias foo){
}