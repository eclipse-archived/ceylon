function(anntypes,$m) {
  var mm=getrtmm$$(this.tipo);
  var pref=mm.d[mm.d.length-1]+"_";
  var cs=coiclsannconstrs$(this,anntypes,pref,'$cn',$m.Arguments$getDeclaredCallableConstructors,this.$$targs$$.Type$AppliedClass);
  for (var i=0;i<cs.length;i++) {
    var r=AppliedCallableConstructor$jsint(cs[i].tipo,{Type$AppliedCallableConstructor:this.$$targs$$.Type$AppliedClass,
                Arguments$AppliedCallableConstructor:cs[i].args},undefined,this.$targs);
    r.cont$=this;
    if (cs[i].fakeConstr)r.fakeConstr$=true;
    cs[i]=r;
  }
  return cs.length===0?empty():ArraySequence(cs,{Element$ArraySequence:{t:CallableConstructor$meta$model,
    a:{Type$CallableConstructor:this.$$targs$$.Type$AppliedClass,Arguments$CallableConstructor:{t:Nothing}}}});
}
