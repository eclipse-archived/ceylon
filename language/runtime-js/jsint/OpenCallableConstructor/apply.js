function $_apply(targs,mt) {
  var cc=this.container;
  var mm=getrtmm$$(this.tipo);
  if (is$(cc.container,{t:ClassOrInterfaceDeclaration$meta$declaration})) {
    throw TypeApplicationException$meta$model("Not a member constructor");
  } else {
    var fakeConstructor=mm.d[mm.d.length-2]!=='$cn';
    var _t=tparms2targs$(fakeConstructor?this.tipo:mm.$cont,targs);
    cc=AppliedCallableConstructor$jsint(this.tipo,
      {Type$AppliedCallableConstructor:mt.Result$apply,
       Arguments$AppliedCallableConstructor:mt.Arguments$apply});
    if (fakeConstructor)cc.fakeConstr$=true;
    return cc;
  }
}
