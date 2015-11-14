function(name,types,m){
  return coigetcoi$(this,name,types,{Kind$getClassOrInterface:m.Kind$getDeclaredClassOrInterface,
    Arguments$getClassOrInterface:m.Arguments$getDeclaredClassOrInterface,
    Container$getClassOrInterface:m.Container$getDeclaredClassOrInterface},1);
}
