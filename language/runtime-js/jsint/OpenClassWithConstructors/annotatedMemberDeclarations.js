function(m,i) {
  var r=ClassOrInterfaceDeclaration$meta$declaration.$$.prototype.annotatedMemberDeclarations.call(this,m,i);
  if (extendsType(m.Kind$annotatedMemberDeclarations,{t:ConstructorDeclaration$meta$declaration})) {
    var callablesOnly=extendsType(m.Kind$annotatedMemberDeclarations,{t:CallableConstructorDeclaration$meta$declaration});
    var valuesOnly=extendsType(m.Kind$annotatedMemberDeclarations,{t:ValueConstructorDeclaration$meta$declaration});
    var mm=getrtmm$$(this.tipo);
    var prefix=mm.d[mm.d.length-1]+'_';
    var ccc=[];
    for (k in this.tipo) {
      if (k.startsWith(prefix)) {
        mm=getrtmm$$(this.tipo[k]);
        if (mm.ps===undefined) {
          if (!callablesOnly) {
            ccc.push(OpenValueConstructor$jsint(this.package, this.tipo[k]));
          }
        } else if (!valuesOnly) {
          ccc.push(OpenCallableConstructor$jsint(this.package,this.tipo[k]));
        }
      }
    }
    return r.append(ArraySequence(ccc,{Element$ArraySequence:m.Kind$annotatedMemberDeclarations}),{Other$append:m.Kind$annotatedMemberDeclarations});
  }
  return r;
}
