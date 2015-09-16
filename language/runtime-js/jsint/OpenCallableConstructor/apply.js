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
    var _t=tparms2targs$(getrtmm$$(this.tipo).$cont,targs);
    return AppliedCallableConstructor$jsint(this.tipo,
      {Type$AppliedCallableConstructor:mt.Result$apply,
       Arguments$AppliedCallableConstructor:mt.Arguments$apply});
  }
  throw new TypeError("IMPL OpenCallableConstructor.apply");
}
