//Add-on to AnnotatedDeclaration            //MethodDeclaration annotations at caca.ceylon (82:2-83:72)
AnnotatedDeclaration$meta$declaration.$$.prototype.annotations=function ($$$mptypes) {
    var $$openInterface=this;
    var ans = [];
    var _mdl = $$openInterface.tipo.$$metamodel$$;
    if (typeof(_mdl)==='function') {
      _mdl = _mdl();
      $$openInterface.tipo.$$metamodel$$=_mdl;
    }
    var _ans = _mdl.$an;
    if (typeof(_ans)==='function') {
      _ans = _ans();
      _mdl.$an=_ans;
    }
    for (var i=0; i<_ans.length;i++) {
      if (isOfType(_ans[i], $$$mptypes.Annotation)) {
        ans.push(_ans[i]);
      }
    }
    return ans.length == 0 ? getEmpty() : ans.reifyCeylonType({Element:$$$mptypes.Annotation});
};
AnnotatedDeclaration$meta$declaration.$$.prototype.annotations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:'Annotation'}},$ps:[],$cont:AnnotatedDeclaration$meta$declaration,$tp:{Annotation:{'var':'out','satisfies':[{t:Annotation,a:{Value:'Annotation'}}]}},$an:function(){return[shared(),formal()];},d:['ceylon.language.meta.declaration','AnnotatedDeclaration','$m','annotations']};};
