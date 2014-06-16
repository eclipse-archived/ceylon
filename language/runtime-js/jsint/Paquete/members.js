function members($$$mptypes){
  var filter=[];
  if (extendsType({t:FunctionDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('m');
  if (extendsType({t:ValueDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('a','g','o', 's');
  if (extendsType({t:ClassDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('c');
  if (extendsType({t:InterfaceDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('i');
  if (extendsType({t:AliasDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('als');
  var r=[];
  for (var mn in this._pkg) {
    var m = this._pkg[mn];
    var mt = m['$mt'];
    if (filter.indexOf(mt)>=0 && m['$an'] && m['$an']['shared']) {
      if (mt === 'm') {
        r.push(OpenFunction(this, m));
      } else if (mt==='c') {
        r.push(OpenClass(this, m));
      } else if (mt==='i') {
        r.push(OpenInterface(this, m));
      } else if (mt==='a'||mt==='g'||mt==='o') {
        r.push(OpenValue(this, m));
      } else if (mt==='s') {
        r.push(OpenSetter(OpenValue(this, m)));
      } else if (mt==='als') {
        r.push(OpenAlias(_findTypeFromModel(this,m)));
      }
    }
  }
  return r.length===0?getEmpty():ArraySequence(r,{Element$ArraySequence:$$$mptypes.Kind$members});
}
