//Add-on to AnnotatedDeclaration
AnnotatedDeclaration$meta$declaration.$$.prototype.annotations=function ($$$mptypes) {
  var mdl = getrtmm$$(this.tipo)||this.meta;
  var _ans = allann$(mdl);
  var ans=[];
  for (var i=0; i<_ans.length;i++) {
    if (is$(_ans[i], $$$mptypes.Annotation$annotations)) {
      ans.push(_ans[i]);
    }
  }
  return ans.length == 0 ? empty() : ArraySequence(ans,{Element$ArraySequence:$$$mptypes.Annotation$annotations});
};
AnnotatedDeclaration$meta$declaration.$$.prototype.annotations.$crtmm$=function(){return{mod:$CCMM$,
  $t:{t:Sequential,a:{Element$Iterable:'Annotation'}},ps:[],$cont:AnnotatedDeclaration$meta$declaration,tp:{Annotation$annotations:{dv:'out',sts:[{t:Annotation,a:{Value$Annotation:'Annotation'}}]}},an:[],d:['ceylon.language.meta.declaration','AnnotatedDeclaration','$m','annotations']};};
