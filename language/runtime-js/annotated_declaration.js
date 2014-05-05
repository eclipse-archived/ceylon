//Add-on to AnnotatedDeclaration
AnnotatedDeclaration$meta$declaration.$$.prototype.annotations=function ($$$mptypes) {
  var ans = [];
  var _mdl = getrtmm$$(this.tipo);
  var _ans = _mdl.$an;
  if (typeof(_ans)==='function') {
    _ans = _ans();
    _mdl.$an=_ans;
  }
  for (var i=0; i<_ans.length;i++) {
    if (is$(_ans[i], $$$mptypes.Annotation$annotations)) {
      ans.push(_ans[i]);
    }
  }
  return ans.length == 0 ? getEmpty() : ans.reifyCeylonType($$$mptypes.Annotation$annotations);
};
AnnotatedDeclaration$meta$declaration.$$.prototype.annotations.$crtmm$=function(){return{mod:$CCMM$,
  $t:{t:Sequential,a:{Element$Iterable:'Annotation'}},$ps:[],$cont:AnnotatedDeclaration$meta$declaration,$tp:{Annotation$annotations:{'var':'out','satisfies':[{t:Annotation,a:{Value$Annotation:'Annotation'}}]}},$an:function(){return[shared(),formal()];},d:['ceylon.language.meta.declaration','AnnotatedDeclaration','$m','annotations']};};
