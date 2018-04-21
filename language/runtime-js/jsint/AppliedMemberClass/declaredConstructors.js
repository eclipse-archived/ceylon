if (this.$constrs$===undefined) {
  var mm=getrtmm$$(this.tipo);
  var mtn=mm.d[mm.d.length-1];
  if (mtn.indexOf('$')>0)mtn=mtn.substring(0,mtn.indexOf('$'));
  var startingType=this.container;
  while (is$(startingType,{t:ClassOrInterface$meta$model})) {
    var pn=startingType.declaration.name;
    mtn+='$'+startingType.declaration.name;
    startingType=startingType.container;
  }
  var ccc=[];
  for (k in this.tipo) {
    if (k.startsWith(mtn+'$c_') || k.startsWith(mtn+'_')) {
      mm=getrtmm$$(this.tipo[k]);
      if (mm.d[mm.d.length-2]!=='$cn')continue;
      if (mm.ps===undefined) {
        var r=AppliedMemberClassValueConstructor$jsint(this.tipo[k],
              {Container$AppliedMemberClassValueConstructor:this.$a$.Container$AppliedMemberClass,
              Type$AppliedMemberClassValueConstructor:this.$a$.Type$AppliedMemberClass});
      } else {
        var args=mm.ps?tupleize$params(mm.ps,this.$targs):empty();
        var r=AppliedMemberClassCallableConstructor$jsint(this.tipo[k],
              {Type$AppliedMemberClassCallableConstructor:this.$a$.Type$AppliedMemberClass,
               Container$AppliedMemberClassCallableConstructor:this.$a$.Container$AppliedMemberClass,
               Arguments$AppliedMemberClassCallableConstructor:args},undefined,this.$targs);
      }
      ccc.push(r);
    }
  }
  var targ={t:'u',l:[
    {t:FunctionModel$meta$model,a:{Type$FunctionModel:this.$a$.Type$AppliedMemberClass,Arguments:{t:Nothing}}},
    {t:ValueModel$meta$model,a:{Type$ValueModel:this.$a$.Type$AppliedMemberClass}}]};
  this.$constrs$=$arr$sa$(ccc,targ);
}
return this.$constrs$;

