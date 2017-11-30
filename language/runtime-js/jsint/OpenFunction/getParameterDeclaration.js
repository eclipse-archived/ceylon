function (nm){
  var pd=this.parameterDeclarations;
  for (var i=0; i < pd.size; i++) {
    if (nm.equals(pd[i].name))return pd[i];
  }
  return null;
}
