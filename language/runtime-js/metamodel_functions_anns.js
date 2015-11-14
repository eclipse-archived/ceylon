function find$ann(cont,ant) {
  var _m=getrtmm$$(cont);
  if (!(_m && _m.an))return null;
  if (typeof(_m.an)==='function')_m.an=_m.an();
  for (var i=0; i < _m.an.length; i++) {
    if (is$(_m.an[i],{t:ant}))return _m.an[i];
  }
  return null;
}
function allann$(mm) {
  if (typeof(mm.an)==='function')mm.an=mm.an();
  var a=getAnnotationsForBitmask(mm.pa);
  if (a) {
    if (mm.an) {
      for (var i=0;i<mm.an.length;i++)a.push(mm.an[i]);
    }
  } else {
    a=mm.an;
  }
  return a||[];
}
function getAnnotationBitmask(t) {
  var mask = 0;
  mask |= extendsType({t:SharedAnnotation},t)?1:0;
  mask |= extendsType({t:ActualAnnotation},t)?2:0;
  mask |= extendsType({t:FormalAnnotation},t)?4:0;
  mask |= extendsType({t:DefaultAnnotation},t)?8:0;
  mask |= extendsType({t:SealedAnnotation},t)?16:0;
  mask |= extendsType({t:FinalAnnotation},t)?32:0;
  mask |= extendsType({t:NativeAnnotation},t)?64:0;
  mask |= extendsType({t:LateAnnotation},t)?128:0;
  mask |= extendsType({t:AbstractAnnotation},t)?256:0;
  mask |= extendsType({t:AnnotationAnnotation},t)?512:0;
  mask |= extendsType({t:VariableAnnotation},t)?1024:0;
  return mask;
}
function getAnnotationsForBitmask(bits) {
  var ans=[];
  if (bits&1)ans.push(shared());
  if (bits&2)ans.push(actual());
  if (bits&4)ans.push(formal());
  if (bits&8)ans.push($_default());
  if (bits&16)ans.push(sealed());
  if (bits&32)ans.push($_final());
  if (bits&64)ans.push($_native());
  if (bits&128)ans.push(late());
  if (bits&256)ans.push($_abstract());
  if (bits&512)ans.push(annotation());
  if (bits&1024)ans.push(variable());
  return ans;
}
//Retrieve the docs from the specified path of the compile-time model
function doc$(root, path, anPath) {
  path = path?path.split(':'):[];
  path.push(anPath||'an', 'doc',0);
  if (path[0]==='$')path[0]='ceylon.language';
  var e = typeof(root)==='function'?root():root;
  for (var i=0; i < path.length; i++) {
    var k = path[i];
    e = e[path[i]];
    if (e===undefined)return doc('<missing>');
  }
  return doc(e);
}
ex$.doc$=doc$;

