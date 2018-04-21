if (this.$constrs$===undefined) {
  var mm=getrtmm$$(this.tipo);
  var oldPrefix=mm.d[mm.d.length-1]+'_';
  var newPrefix=mm.d[mm.d.length-1]+'$c_';
  var ccc=[];
  for (k in this.tipo) {
    if (k.startsWith(newPrefix) || k.startsWith(oldPrefix)) {
      mm=getrtmm$$(this.tipo[k]);
      if (mm.d[mm.d.length-2]!=='$cn')continue;
      if (mm.ps===undefined) {
        var r=AppliedValueConstructor$jsint(this.tipo[k],
              {Type$AppliedValueConstructor:this.$a$.Type$AppliedClass});
      } else {
        var args=mm.ps?tupleize$params(mm.ps,this.$targs):empty();
        var r=AppliedCallableConstructor$jsint(this.tipo[k],{Type$AppliedCallableConstructor:this.$a$.Type$AppliedClass,
            Arguments$AppliedCallableConstructor:args},undefined,this.$targs);
      }
      ccc.push(r);
    }
  }
  this.$constrs$=$arr$sa$(ccc,{t:'u',l:[
    {t:FunctionModel$meta$model,a:{Type$FunctionModel:this.$a$.Type$AppliedClass,Arguments:{t:Nothing}}},
    {t:ValueModel$meta$model,a:{Type$ValueModel:this.$a$.Type$AppliedClass}}]});
}
return this.$constrs$;

