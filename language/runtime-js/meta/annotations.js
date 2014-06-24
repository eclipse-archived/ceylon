/*Native Implementation of annotations() */
function annotations$meta(anntype, progelem, $$$mptypes) {
  if (progelem.tipo)progelem=progelem.tipo;
  var mm = getrtmm$$(progelem);
  if (progelem.$anns)mm={an:typeof(progelem.$anns)==='function'?progelem.$anns():progelem.$anns};
  else if (progelem._anns)mm={an:typeof(progelem._anns)==='function'?progelem._anns():progelem._anns};
  if (!mm)return null;
  //get the bitset annotations
  var anns=getAnnotationsForBitmask(mm.pa);
  if (typeof(mm.an)==='function')mm.an=mm.an();
  if (anns) {
    mm.pa=0;
    if (mm.an) {
      for (var i=0;i<anns.length;i++)mm.an.push(anns[i]);
    } else {
      mm.an=anns;
    }
  }
  if (mm.an) {
    anns=mm.an;
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
    return r.length==0?getEmpty():ArraySequence(r,{Element$ArraySequence:{t:ConstrainedAnnotation,
      a:{Value$ConstrainedAnnotation:$$$mptypes.Value$annotations,Values$ConstrainedAnnotation:$$$mptypes.Values$annotations,
      ProgramElement$ConstrainedAnnotation:$$$mptypes.ProgramElement$annotations}}});
  }
  return null;
}
