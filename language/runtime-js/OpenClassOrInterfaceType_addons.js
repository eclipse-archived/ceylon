defineAttr(OpenClassOrInterfaceType$meta$declaration.$$.prototype,'typeArguments',function(){
  var tps=this.declaration.typeParameterDeclarations;
  if (tps && tps.length > 0) {
    var rtps = this.declaration._targs ? this.declaration._targs : this.declaration.tipo.$$metamodel$$.$tp;
    var targs=[];
    for (var i=0; i < tps.length; i++) {
      var tp = rtps[tps[i].name];
      var targ;
      if (typeof(tp)==='string') {
        targ = new OpenTvar(OpenTypeParam(this.declaration.tipo,tp));
      } else {
        targ = OpenTvar(tps[i]);
      }
      targs.push(Entry(tps[i], targ, {Key:{t:TypeParameter$meta$declaration},Item:{t:OpenType$meta$declaration}}));
    }
    return LazyMap(targs.reifyCeylonType({Absent:{t:Null},Element:{t:Entry,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:OpenType$meta$declaration}}}}),{Key:{t:TypeParameter$meta$declaration},Item:{t:OpenType$meta$declaration}});
  }
  return getEmpty();
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Map,a:{Key:{t:TypeParameter$meta$declaration},Item:{t:OpenType$meta$declaration}}},$cont:OpenClassOrInterfaceType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassOrInterfaceType','$at','typeArguments']};});
defineAttr(OpenClassOrInterfaceType$meta$declaration.$$.prototype,'extendedType',function(){
  return this.declaration.extendedType;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{ t:'u', l:[{t:Null},{t:OpenClassType$meta$declaration}]},$cont:OpenClassOrInterfaceType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassOrInterfaceType','$at','extendedType']};});
defineAttr(OpenClassOrInterfaceType$meta$declaration.$$.prototype,'declaration',function(){return this._decl;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:ClassDeclaration$meta$declaration},$cont:OpenClassOrInterfaceType,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassOrInterfaceType','$at','declaration']};});
defineAttr(OpenClassOrInterfaceType$meta$declaration.$$.prototype,'satisfiedTypes',function(){
  return this.declaration.satisfiedTypes;
},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:OpenInterfaceType$meta$declaration}}},$cont:OpenClassOrInterfaceType$meta$declaration,$an:function(){return[shared(),actual()];},d:['ceylon.language.meta.declaration','OpenClassOrInterfaceType','$at','satisfiedTypes']};});
