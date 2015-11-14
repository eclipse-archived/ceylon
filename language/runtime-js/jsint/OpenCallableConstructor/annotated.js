function($mpt){
  if (this.defaultConstructor) {
    var mm=getrtmm$$(this.tipo);
    if (mm.d[mm.d.length-1] !== '$def') {
      //fake constructor
      if (extendsType($mpt.Annotation$annotated,{t:SharedAnnotation}))return true;
      if (extendsType($mpt.Annotation$annotated,{t:DocAnnotation}))return false;
    }
  }
  var x=annd$annotations(this,{Annotation$annotations:$mpt.Annotation$annotated});
  return x&&x.size>0;
}
