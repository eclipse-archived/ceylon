function() {
  if (this.declcn$===undefined) {
    var mm=getrtmm$$(this.tipo),mdl=get_model(mm);
    if (mdl.$cn) {
      var c=[];
      for (var m in this.tipo) {
        if (m.indexOf('_')>0) {
          m=getnpmem$(this.tipo,m);
          mm=m&&getrtmm$$(m);
          if (mm && mm.d[mm.d.length-2]==='$cn') {
            c.push((mm.ps===undefined?OpenValueConstructor$jsint:OpenCallableConstructor$jsint)(this.pkg_,m));
          }
        }
      }
      this.declcn$=ArraySequence(c,{Element$ArraySequence:{t:ConstructorDeclaration$meta$declaration}});
    } else {
      this.declcn$=empty();
    }
  }
  return this.declcn$;
}
