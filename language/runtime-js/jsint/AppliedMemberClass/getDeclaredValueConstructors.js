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
  var pref=mtn+"_";
  var cs=coiclsannconstrs$(this,anntypes,pref,this.$$targs$$.Type$AppliedMemberClass);
  for (var i=0;i<cs.length;i++) {
    var r=AppliedMemberClassValueConstructor$jsint(cs[i].tipo,
               {Type$AppliedMemberClassValueConstructor:this.$$targs$$.Type$AppliedMemberClass,
                Container$AppliedMemberClassValueConstructor:this.$$targs$$.Container$AppliedMemberClass});
    r.$cont=this;
    cs[i]=r;
  }
  return cs.length===0?empty():ArraySequence(cs,{Element$ArraySequence:{t:MemberClassValueConstructor$meta$model,
    a:{Type$MemberClassValueConstructor:this.$$targs$$.Type$AppliedClass,
       Container$MemberClassValueConstructor:this.$$targs$$.Container$AppliedMemberClass}}});
}
