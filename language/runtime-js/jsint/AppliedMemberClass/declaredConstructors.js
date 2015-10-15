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
  var prefix=mtn+"_";
  var ccc=[];
  for (k in this.tipo) {
    if (k.startsWith(prefix)) {
      mm=getrtmm$$(this.tipo[k]);
      if (mm.d[mm.d.length-2]!=='$cn')continue;
      if (mm.ps===undefined) {
        var r=AppliedMemberClassValueConstructor$jsint(this.tipo[k],
              {Container$AppliedMemberClassValueConstructor:this.$$targs$$.Container$AppliedMemberClass,
              Type$AppliedMemberClassValueConstructor:this.$$targs$$.Type$AppliedMemberClass});
      } else {
        var args=mm.ps?tupleize$params(mm.ps,this.$targs):empty();
        var r=AppliedMemberClassCallableConstructor$jsint(this.tipo[k],
              {Type$AppliedMemberClassCallableConstructor:this.$$targs$$.Type$AppliedMemberClass,
               Container$AppliedMemberClassCallableConstructor:this.$$targs$$.Container$AppliedMemberClass,
               Arguments$AppliedMemberClassCallableConstructor:args},undefined,this.$targs);
      }
      ccc.push(r);
    }
  }
  this.$constrs$=ccc.length===0?empty():ArraySequence(ccc,{Element$ArraySequence:{t:'u',l:[
    {t:FunctionModel$meta$model,a:{Type$FunctionModel:this.$$targs$$.Type$AppliedMemberClass,Arguments:{t:Nothing}}},
    {t:ValueModel$meta$model,a:{Type$ValueModel:this.$$targs$$.Type$AppliedMemberClass}}]}});
}
return this.$constrs$;

