if (this.$constrs$===undefined) {
  var mm=getrtmm$$(this.tipo);
  var prefix=mm.d[mm.d.length-1]+'_';
  var ccc=[];
  for (k in this.tipo) {
    if (k.startsWith(prefix)) {
      mm=getrtmm$$(this.tipo[k]);
      if (mm.d[mm.d.length-2]!=='$cn')continue;
      if (mm.ps===undefined) {
        var r=AppliedValueConstructor$jsint(this.tipo[k],
              {Type$AppliedValueConstructor:this.$$targs$$.Type$AppliedClass});
      } else {
        var args=mm.ps?tupleize$params(mm.ps,this.$targs):empty();
        var r=AppliedCallableConstructor$jsint(this.tipo[k],{Type$AppliedCallableConstructor:this.$$targs$$.Type$AppliedClass,
            Arguments$AppliedCallableConstructor:args},undefined,this.$targs);
      }
      ccc.push(r);
    }
  }
  this.$constrs$=ccc.length===0?empty():ArraySequence(ccc,{Element$ArraySequence:{t:'u',l:[
    {t:FunctionModel$meta$model,a:{Type$FunctionModel:this.$$targs$$.Type$AppliedClass,Arguments:{t:Nothing}}},
    {t:ValueModel$meta$model,a:{Type$ValueModel:this.$$targs$$.Type$AppliedClass}}]}});
}
return this.$constrs$;

