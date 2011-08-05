@nomodel
interface InterfaceWithConcreteMembers {
 shared default Natural foo(Boolean b){
  return 1;
 }
}

@nomodel
class Impl () satisfies InterfaceWithConcreteMembers {
}