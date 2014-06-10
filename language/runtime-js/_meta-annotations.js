/*Native Implementation of annotations() */
function annotations$meta(anntype, progelem, $$$mptypes) {
  if (progelem.tipo)progelem=progelem.tipo;
  var mm = getrtmm$$(progelem);
  if (progelem.$anns)mm={$an:typeof(progelem.$anns)==='function'?progelem.$anns():progelem.$anns};
  if (mm && mm.$an) {
    var anns=mm.$an;
    if (typeof(anns) === 'function') {
      anns=anns();
      mm.$an=anns;
    }
    if (anntype.tipo.$$.T$all['ceylon.language::OptionalAnnotation'] !== undefined) {
      //find the first one and return it
      for (var i=0; i < anns.length; i++) {
        if (is$(anns[i], {t:anntype.tipo}))return anns[i];
      }
      return null;
    }
    //gather all annotations of the required type and return them
    var r=[];
    for (var i=0; i < anns.length; i++) {
      if (is$(anns[i], {t:anntype.tipo}))r.push(anns[i]);
    }
    return r.length==0?getEmpty():r.reifyCeylonType({t:ConstrainedAnnotation,
      a:{Value$ConstrainedAnnotation:$$$mptypes.Value$annotations,Values$ConstrainedAnnotation:$$$mptypes.Values$annotations,
      ProgramElement$ConstrainedAnnotation:$$$mptypes.ProgramElement$annotations}});
  }
  return null;
}
