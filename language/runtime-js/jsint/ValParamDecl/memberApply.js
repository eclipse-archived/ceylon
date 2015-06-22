function memberApply(c,$mpt){
  var r=OpenValue$jsint.$$.prototype.memberApply.call(this,c,$mpt);
  if (!(this.at$ && this.at$.shared)) {
    var n=this.name;
    var nono=function(){
      throw StorageException$meta$model("Attribute " + n + " is neither captured nor shared so it has no physical storage allocated and cannot be read by the metamodel");
    }
    nono.$crtmm$=r.$crtmm$;
    r=nono;
  }
  return r;
}
