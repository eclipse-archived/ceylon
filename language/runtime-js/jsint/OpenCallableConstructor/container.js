if (this.cont$===undefined) {
  var cc=getrtmm$$(this.tipo);
  if (this.defaultConstructor && cc.d[cc.d.length-2]!=='$cn') {
    //fake constructor
    this.cont$=openClass$jsint(this.containingPackage,this.tipo);
  } else {
    cc=cc.$cont;
    this.cont$=openClass$jsint(this.containingPackage,cc);
  }
}
return this.cont$;
