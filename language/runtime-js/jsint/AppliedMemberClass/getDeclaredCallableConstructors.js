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
  var cs=coiclsannconstrs$(this,anntypes,mtn+'$c_',this.$a$.Type$AppliedMemberClass,$m.Arguments$getDeclaredCallableConstructors);
  if (cs.length===0) {
    cs=coiclsannconstrs$(this,anntypes,mtn+'_',this.$a$.Type$AppliedMemberClass,$m.Arguments$getDeclaredCallableConstructors);
  }
  for (var i=0;i<cs.length;i++) {
    var r=AppliedMemberClassCallableConstructor$jsint(cs[i].tipo,
               {Type$AppliedMemberClassCallableConstructor:this.$a$.Type$AppliedMemberClass,
                Container$AppliedMemberClassCallableConstructor:this.$a$.Container$AppliedMemberClass,
                Arguments$AppliedMemberClassCallableConstructor:cs[i].args},undefined,this.$targs);
    r.$cont=this;
    if (cs[i].fakeConstr)r.fakeConstr$=true;
    cs[i]=r;
  }
  var targ={t:MemberClassCallableConstructor$meta$model,
    a:{Type$MemberClassCallableConstructor:this.$a$.Type$AppliedClass,Arguments$MemberClassCallableConstructor:{t:Nothing},
       Container$MemberClassCallableConstructor:this.$a$.Container$AppliedMemberClass}};
  return $arr$sa$(cs,targ);
}
