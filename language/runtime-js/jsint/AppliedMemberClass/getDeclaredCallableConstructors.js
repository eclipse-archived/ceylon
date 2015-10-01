function(anntypes,$m) {
  var mm=getrtmm$$(this.tipo);
  //Member Type Name
  var mtn=mm.d[mm.d.length-1];
  if (mtn.indexOf('$')>0)mtn=mtn.substring(0,mtn.indexOf('$'));
  var startingType=this.container;
  while (is$(startingType,{t:ClassOrInterface$meta$model})) {
    var pn=startingType.declaration.name;
    mtn+='$'+startingType.declaration.name;
    startingType=startingType.container;
  }
  var pref=mtn+"_";
  var cs=coiclsannconstrs$(this,anntypes,pref,'$cn',$m.Arguments$getDeclaredCallableConstructors,this.$$targs$$.Type$AppliedMemberClass);
  for (var i=0;i<cs.length;i++) {
    var r=AppliedMemberClassCallableConstructor$jsint(cs[i].tipo,
               {Type$AppliedMemberClassCallableConstructor:this.$$targs$$.Type$AppliedMemberClass,
                Container$AppliedMemberClassCallableConstructor:this.$$targs$$.Container$AppliedMemberClass,
                Arguments$AppliedMemberClassCallableConstructor:cs[i].args},undefined,this.$targs);
    r.$cont=this;
    if (cs[i].fakeConstr)r.fakeConstr$=true;
    cs[i]=r;
  }
  return cs.length===0?empty():ArraySequence(cs,{Element$ArraySequence:{t:MemberClassCallableConstructor$meta$model,
    a:{Type$MemberClassCallableConstructor:this.$$targs$$.Type$AppliedClass,Arguments$MemberClassCallableConstructor:{t:Nothing},
       Container$MemberClassCallableConstructor:this.$$targs$$.Container$AppliedMemberClass}}});
}
