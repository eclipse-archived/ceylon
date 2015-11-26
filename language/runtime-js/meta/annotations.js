/*Native Implementation of annotations() */
function annotations$meta(anntype, progelem, $$$mptypes) {
  if (progelem.tipo)
    progelem=progelem.tipo;
  else if (progelem._alias)
    progelem=progelem._alias;
  var mm = getrtmm$$(progelem);
  if (progelem.$anns)mm={an:typeof(progelem.$anns)==='function'?progelem.$anns():progelem.$anns};
  else if (progelem._anns)mm={an:typeof(progelem._anns)==='function'?progelem._anns():progelem._anns};
  if (!mm)return null;
  //get the bitset annotations
  var anns=allann$(mm);
  if (anns) {
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
    var targ={t:ConstrainedAnnotation,a:{Value$ConstrainedAnnotation:$$$mptypes.Value$annotations,
      Values$ConstrainedAnnotation:$$$mptypes.Values$annotations,
      ProgramElement$ConstrainedAnnotation:$$$mptypes.ProgramElement$annotations}};
    return r.length==0?empty():ArraySequence($arr$(r,targ),{Element$ArraySequence:targ});
  }
  return null;
}
