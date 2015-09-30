function memberApply(ctype,targs,$mpt) {
  var fakeConstructor=false;
  if (this.defaultConstructor) {
    var mm=getrtmm$$(this.tipo);
    fakeConstructor=mm.d[mm.d.length-1]!=='$def';
    if (fakeConstructor && mm.d[mm.d.length-1]!=='$c') {
      throw TypeApplicationException$meta$model("Not a member constructor");
    }
  }
  var c=AppliedMemberClassCallableConstructor$jsint(this.tipo,
    {Type$AppliedMemberClassCallableConstructor:$mpt.Result$memberApply,
     Container$AppliedMemberClassCallableConstructor:$mpt.Container$memberApply,
     Arguments$AppliedMemberClassCallableConstructor:$mpt.Arguments$memberApply});
  if (fakeConstructor)c.fakeConstr$=true;
  return c;
}
