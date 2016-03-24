function(name){
  var pd=this.parameterDeclarations;
  for (var i=0; i < pd.size; i++) {
    if (name.equals(pd.$_get(i).name))return pd.$_get(i);
  }
  return null;
}
