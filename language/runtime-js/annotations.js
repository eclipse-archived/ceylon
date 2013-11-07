/*Native Implementation of annotations() */
function annotations$meta(anntype, progelem, $$$mptypes) {
  var mm = progelem.tipo?progelem.tipo.$$metamodel$$:progelem.$$metamodel$$;
  if (!mm && $$$mptypes.ProgramElement && extendsType($$$mptypes.ProgramElement,{t:Module$meta$declaration})) {
    var rv = progelem.annotations({Annotation:$$$mptypes.Value});
    if (anntype.tipo.$$.T$all['ceylon.language::OptionalAnnotation'] !== undefined)return rv&&rv.length?rv[0]:null;
    return rv;
  }
  if (typeof(mm) === 'function') {
    mm = mm();
  }
  if (mm && mm.$an) {
    var anns=mm.$an;
    if (typeof(anns) === 'function') {
      anns=anns();
      mm.$an=anns;
    }
    if (anntype.tipo.$$.T$all['ceylon.language::OptionalAnnotation'] !== undefined) {
      //find the first one and return it
      for (var i=0; i < anns.length; i++) {
        if (isOfType(anns[i], {t:anntype.tipo}))return anns[i];
      }
      return null;
    }
    //gather all annotations of the required type and return them
    var r=[];
    for (var i=0; i < anns.length; i++) {
      if (isOfType(anns[i], {t:anntype.tipo}))r.push(anns[i]);
    }
    return r.length==0?getEmpty():r.reifyCeylonType({Absent:{t:Null},Element:{t:ConstrainedAnnotation,
      a:{Value:$$$mptypes.Value,Values:$$$mptypes.Values,ProgramElement:$$$mptypes.ProgramElement}}});
  }
  return null;
}
annotations$meta.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:'Values',$ps:[
  {$nm:'annotationType',$mt:'prm',$t:{t:Class$meta$model,a:{Type:{t:ConstrainedAnnotation,a:{Values:'Values',Value:'Value',ProgramElement:'ProgramElement'}}}}},{$nm:'programElement',$mt:'prm',$t:'ProgramElement'}],
  $tp:{Value:{'satisfies':[{t:ConstrainedAnnotation$meta$model,a:{Values:'Values',Value:'Value',ProgramElement:'ProgramElement'}}]},Values:{},ProgramElement:{'var':'in','satisfies':[{t:Annotated$meta$model}]}},
  $an:function(){return[shared(),$native()];},d:['ceylon.language.meta','annotations']};};
exports.annotations$meta=annotations$meta;
