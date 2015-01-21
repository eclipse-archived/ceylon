function members($$$mptypes){
  var filter=[];
  if (extendsType({t:FunctionDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('m');
  if (extendsType({t:ValueDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('a','g','s');
  if (extendsType({t:ClassDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('c','o');
  if (extendsType({t:InterfaceDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('i');
  if (extendsType({t:AliasDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('als');
  var r=[];
  for (var mn in this._pkg) {
    var m = this._pkg[mn];
    var mt = m.mt;
    if (filter.indexOf(mt)>=0 && (m.pa&1)) {
      if (mt === 'm') {
        r.push(OpenFunction(this, m));
      } else if (mt==='c'||mt==='o') {
        r.push(OpenClass$jsint(this, m));
      } else if (mt==='i') {
        r.push(OpenInterface$jsint(this, m));
      } else if (mt==='a'||mt==='g') {
        r.push(OpenValue$jsint(this, m));
      } else if (mt==='s') {
        r.push(OpenSetter(OpenValue$jsint(this, m)));
      } else if (mt==='als') {
        r.push(OpenAlias$jsint(_findTypeFromModel(this,m)));
      }
    }
  }
  return r.length===0?empty():ArraySequence(r,{Element$ArraySequence:$$$mptypes.Kind$members});
}
