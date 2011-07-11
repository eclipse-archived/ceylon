class Foo() {}
class Bar() {}

class UnionTypeInfo(Foo|Bar param){
 Foo|Bar attr;
 shared Foo|Bar sharedAttr = Foo();
 Foo|Bar getter {
  return Foo();
 }
 assign getter {
 }

 shared Foo|Bar sharedGetter {
  return Foo();
 }
 assign sharedGetter {
 }
 
 Foo|Bar method(Foo|Bar methodParam){
  Foo|Bar val = Foo();
  return val;
 }

 shared Foo|Bar sharedMethod(Foo|Bar methodParam){
  Foo|Bar val = Foo();
  return val;
 }
}

shared class SharedUnionTypeInfo(Foo|Bar param){
}

Foo|Bar toplevelAttribute;
shared Foo|Bar sharedToplevelAttribute;

Foo|Bar toplevelGetter {
 return Foo();
}
assign toplevelGetter {
}

shared Foo|Bar toplevelSharedGetter {
 return Foo();
}
assign toplevelSharedGetter {
}

Foo|Bar toplevelMethod(Foo|Bar param){
 return Foo();
}

shared Foo|Bar sharedToplevelMethod(Foo|Bar param){
 return Foo();
}