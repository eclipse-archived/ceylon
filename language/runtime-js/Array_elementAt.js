function elementAt(i){
  var e=this[i];
  if (e===undefined)return getFinished();
  return e;
}
