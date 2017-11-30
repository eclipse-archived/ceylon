function(a, that){
  if (typeof(a)==='function')a=a();
  that._alias_ = a;
  //Get model from path
  var mm=getrtmm$$(a);
  that.meta=get_model(mm);
}
