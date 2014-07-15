//Add-on to AnnotatedDeclaration
AnnotatedDeclaration$meta$declaration.$$.prototype.annotations=function ($$$mptypes) {
  var mdl = getrtmm$$(this.tipo);
  var ans = [];
  var _ans = getAnnotationsForBitmask(mdl.pa);
  if (typeof(mdl.an)==='function')mdl.an=mdl.an();
  if (_ans) {
    mdl.pa=0;
    if (mdl.an) {
      for (var i=0; i < _ans.length; i++)mdl.an.push(_ans[i]);
    } else {
      mdl.an=_ans;
    }
  }
  _ans=mdl.an;
  for (var i=0; i<_ans.length;i++) {
    if (is$(_ans[i], $$$mptypes.Annotation$annotations)) {
      ans.push(_ans[i]);
    }
  }
  return ans.length == 0 ? getEmpty() : ArraySequence(ans,{Element$ArraySequence:$$$mptypes.Annotation$annotations});
};
AnnotatedDeclaration$meta$declaration.$$.prototype.annotations.$crtmm$=function(){return{mod:$CCMM$,
  $t:{t:Sequential,a:{Element$Iterable:'Annotation'}},ps:[],$cont:AnnotatedDeclaration$meta$declaration,tp:{Annotation$annotations:{'var':'out',sts:[{t:Annotation,a:{Value$Annotation:'Annotation'}}]}},an:[],d:['ceylon.language.meta.declaration','AnnotatedDeclaration','$m','annotations']};};
