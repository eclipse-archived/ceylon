function getMember(name$3,$$$mptypes){
  var m = this._pkg[name$3];
  if (m) {
    var mt = m['mt'];
    var kind=$$$mptypes.Kind$getMember;
    //There's a member alright, but check its type
    var isNestable=extendsType(kind,{t:NestableDeclaration$meta$declaration});
    if ((mt==='a'||mt==='g'||mt==='s')&&isNestable) {
      return mt==='s'?OpenSetter(OpenValue$jsint(this, m)):OpenValue$jsint(this, m);
    } else if (mt==='m'&&isNestable){
      return OpenFunction$jsint(this, m);
    } else if (mt==='c'&&isNestable){
      return openClass$jsint(this, m);
    } else if (mt==='i'&&isNestable){
      return OpenInterface$jsint(this, m);
    } else if (mt==='als'&&extendsType(kind,{t:AliasDeclaration$meta$declaration})){
      return OpenAlias$jsint(_findTypeFromModel(this,m));
    } else if (mt==='o') {
      if (extendsType(kind,{t:ValueDeclaration$meta$declaration})) {
        return OpenValue$jsint(this, m);
      } else if (isNestable) {
        return openClass$jsint(this, m);
      }
    }
    console.log("WTF do I do with this " + name$3 + " metatype " + mt + " Kind " + kind);
  }
  return null;
}
