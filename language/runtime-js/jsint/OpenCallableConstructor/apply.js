function $_apply(targs,mt) {
  var cc=this.container;
  var tt;
  if (is$(cc.container,{t:ClassOrInterfaceDeclaration$meta$declaration})) {
    console.log("TODO crear AppliedMemberClassCallableConstructor para " + this.string);
    /*return AppliedMemberClassCallableConstructor$jsint(this.tipo,
      {Type$AppliedMemberClassCallableConstructor:mt.Result$apply,
       Container$AppliedMemberClassCallableConstructor:{t:cc.container.tipo},
       Arguments$AppliedMemberClassCallableConstructor:mt.Arguments$apply});*/
  } else {
    var mm=getrtmm$$(this.tipo);
    var fakeConstructor=this.defaultConstructor && mm.d[mm.d.length-1]!=='def$';
    var _t=tparms2targs$(fakeConstructor?this.tipo:mm.$cont,targs);
    cc=AppliedCallableConstructor$jsint(this.tipo,
      {Type$AppliedCallableConstructor:mt.Result$apply,
       Arguments$AppliedCallableConstructor:mt.Arguments$apply});
    if (fakeConstructor)cc.fakeConstr$=true;
    return cc;
  }
  throw new TypeError("WTF OpenCallableConstructor.apply");
}
