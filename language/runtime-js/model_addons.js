defineAttr(ClassOrInterface$model.$$.prototype,'interfaces',function(){
  var ints = this.tipo.$$metamodel$$['satisfies'];
  if (ints && ints.length) {
    var rv = [];
    for (var i=0; i < ints.length; i++) {
      var ifc = ints[i].t;
      var mm = ifc.$$metamodel$$;
      if (typeof(mm)==='function') {
        mm = mm();
        ifc.$$metamodel$$=mm;
      }
      rv.push(AppliedInterface(ifc, {Type:{t:Anything}}));
    }
    return rv.reifyCeylonType({Absent:{t:Null},Element:{t:Interface$model,a:{Type:{t:Anything}}}});
  }
  return getEmpty();
},undefined,function(){
  return{mod:$$METAMODEL$$,$t:{t:Sequential,a:{Element:{t:Interface$model,a:{Type:{t:Anything}}}}},$cont:ClassOrInterface$model,
  $an:function(){return[shared(),formal()];},d:['ceylon.language.model','ClassOrInterface','$at','interfaces']};
});

