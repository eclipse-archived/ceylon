function members($$$mptypes){
  var filter=[];
  var isValue=false;
  if (extendsType({t:FunctionDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('m');
  if (extendsType({t:ValueDeclaration$meta$declaration},$$$mptypes.Kind$members)) {
    filter.push('a','g','s','o');
    isValue=true;
  }
  if (extendsType({t:ClassDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('c','o');
  if (extendsType({t:InterfaceDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('i');
  if (extendsType({t:AliasDeclaration$meta$declaration},$$$mptypes.Kind$members))filter.push('als');
  var r=[];
  var unsh=this.container.meta['$pkgunsh$'+this.name.$_replace('.','$')];
  for (var mn in this._pkg) {
    var m = this._pkg[mn];
    var mt = m.mt;
    if (filter.indexOf(mt)>=0) {
      if ((m.pa&1)===0) {
        var priv=unsh && unsh[mn];
        if (priv) {
          m=priv;
        } else {
          mt='';
        }
      }
      if (mt === 'm') {
        r.push(OpenFunction$jsint(this, m));
      } else if (mt==='c'||(mt==='o'&&!isValue)) {
        r.push(openClass$jsint(this, m));
      } else if (mt==='i') {
        r.push(OpenInterface$jsint(this, m));
      } else if (mt==='a'||mt==='g'||(mt==='o'&&isValue)) {
        r.push(OpenValue$jsint(this, m));
      } else if (mt==='s') {
        r.push(OpenSetter(OpenValue$jsint(this, m)));
      } else if (mt==='als') {
        r.push(OpenAlias$jsint(m.$crtmm$?m:_findTypeFromModel(this,m)));
      }
    }
  }
  return r.length===0?empty():ArraySequence(r,{Element$ArraySequence:$$$mptypes.Kind$members});
}
