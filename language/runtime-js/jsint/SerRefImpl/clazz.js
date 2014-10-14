function(){
  var o=this.instance();
  if (o === null) return AppliedClass$jsint(?);
  //Get the type
  var t=o.getT$all()[o.getT$name()];
  var mm = getrtmm$(t);
  console.log("CLAZZ " + t + " META " + require('util').inspect(mm));
}
