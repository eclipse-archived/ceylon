function flattentype$(l,iu){
  var lt=l.slice(0);
  for (var i=0;i<lt.length;i++) {
    if (lt[i].t===iu) {
      var nt=lt[i].l;
      lt.splice(i,1);
      i--;
      for (var j=0;j<nt.length;j++) {
        addtype$(nt[j],lt);
      }
    }
  }
  return lt;
}
function mut$(l) {
  var l=flattentype$(l,'u');
  return l.length===1?l[0]:{t:'u',l:l};
}
function mit$(l) {
  var l=flattentype$(l,'i');
  return l.length===1?l[0]:{t:'i',l:l};
}
function mtt$(l) {
  return {t:'T',l:l};
}
function addtype$(t,l) {
  for (var i=0;i<l.length;i++) {
    if (extendsType(t,l[i]))return;
  }
  l.push(t);
}
ex$.mut$=mut$;
ex$.mit$=mit$;
ex$.mtt$=mtt$;
