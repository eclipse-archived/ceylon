function(anntypes){
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
  var cs=coiclsannconstrs$(this,anntypes,mtn+'$c_',this.$a$.Type$AppliedMemberClass);
  if (cs.length===0) {
    cs=coiclsannconstrs$(this,anntypes,mtn+'_',this.$a$.Type$AppliedMemberClass);
  }
  for (var i=0;i<cs.length;i++) {
    var r=AppliedMemberClassValueConstructor$jsint(cs[i].tipo,
               {Type$AppliedMemberClassValueConstructor:this.$a$.Type$AppliedMemberClass,
                Container$AppliedMemberClassValueConstructor:this.$a$.Container$AppliedMemberClass});
    r.$cont=this;
    cs[i]=r;
  }
  return $arr$sa$(cs,{t:MemberClassValueConstructor$meta$model,
    a:{Type$MemberClassValueConstructor:this.$a$.Type$AppliedClass,
       Container$MemberClassValueConstructor:this.$a$.Container$AppliedMemberClass}});
}
