function(anntypes,$m){
  return this.getDeclaredCallableConstructors(anntypes,{Arguments$getDeclaredCallableConstructors:$m.Arguments$getCallableConstructors}
  ).select(function(e){return (getrtmm$$(e.tipo).pa&1)>0||e.fakeConstr$;});
}
