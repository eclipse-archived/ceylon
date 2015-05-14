function getMember(name$3,$$$mptypes){
  var m = this._pkg[name$3];
  if (m) {
    var mt = m['mt'];
    //There's a member alright, but check its type
    if ((mt==='a'||mt==='g'||mt==='s')&&extendsType($$$mptypes.Kind$getMember,{t:FunctionOrValueDeclaration$meta$declaration})) {
      return mt==='s'?OpenSetter(OpenValue$jsint(this, m)):OpenValue$jsint(this, m);
    } else if (mt==='m'&&extendsType($$$mptypes.Kind$getMember,{t:FunctionOrValueDeclaration$meta$declaration})){
      return OpenFunction$jsint(this, m);
    } else if (mt==='c'&&(extendsType($$$mptypes.Kind$getMember,{t:NestableDeclaration$meta$declaration}))){
      return openClass$jsint(this, m);
    } else if (mt==='i'&&extendsType($$$mptypes.Kind$getMember,{t:NestableDeclaration$meta$declaration})){
      return OpenInterface$jsint(this, m);
    } else if (mt==='als'&&extendsType($$$mptypes.Kind$getMember,{t:AliasDeclaration$meta$declaration})){
      return OpenAlias$jsint(_findTypeFromModel(this,m));
    } else if (mt==='o') {
      if (extendsType($$$mptypes.Kind$getMember,{t:ValueDeclaration$meta$declaration})) {
        return OpenValue$jsint(this, m);
      } else if (extendsType($$$mptypes.Kind$getMember,{t:NestableDeclaration$meta$declaration})) {
        return openClass$jsint(this, m);
      }
    }
    console.log("WTF do I do with this " + name$3 + " metatype " + mt + " Kind " + $$$mptypes.Kind$getMember);
  }
  return null;
}
