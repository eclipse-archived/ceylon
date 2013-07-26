/*Native Implementation of annotations() */
function annotations$metamodel(anntype, progelem, $$targs$$) {
  var mm = progelem.tipo?progelem.tipo.$$metamodel$$:progelem.attr?progelem.attr.$$metamodel$$:undefined;
  if (mm && mm['$an']) {
    var anns = mm['$an'];
    if (typeof(anns) === 'function') {
      anns = anns();
      mm['$an'] = anns;
    }
    if (anntype.tipo.$$.T$all['ceylon.language.metamodel::OptionalAnnotation'] !== undefined) {
      //find the first one and return it
      for (var i=0; i < anns.length; i++) {
        if (isOfType(anns[i], {t:anntype.tipo}))return anns[i];
      }
      return null;
    }
    //gather all annotatinos of the required type and return them
    var r=[];
    for (var i=0; i < anns.length; i++) {
      if (isOfType(anns[i], {t:anntype.tipo}))r.push(anns[i]);
    }
    return r.length==0?getEmpty():r.reifyCeylonType({Absent:{t:Null},Element:{t:ConstrainedAnnotation$metamodel,
      a:{Value:$$targs$$.Value,Values:$$targs$$.Values,ProgramElement:$$targs$$.ProgramElement}}});
  }
  return null;
}
exports.annotations$metamodel=annotations$metamodel;
