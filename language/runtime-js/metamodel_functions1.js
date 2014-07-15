//From a runtime metamodel, get the model definition by following the path into the module's model.
function get_model(mm) {
  var map=typeof(mm.mod)==='function'?mm.mod():mm.mod;
  var path=mm.d;
  for (var i=0; i < path.length; i++) {
    var _p=path[i];
    if (i===0 && _p==='$')_p='ceylon.language';
    else if (i==path.length-1&&_p==='$set' && map.nm && map.$set)return map;
    map = map[_p];
  }
  return map;
}

function pushTypes(list, types) {
  for (var i=0; i<types.length; i++) {
    var t = types[i];
    if (t.t === 'u') {
      list.push(applyUnionType(t, t.l));
    } else if (t.t === 'i') {
      list.push(applyIntersectionType(t, t.l));
    } else {
      list.push(typeLiteral$meta({Type$typeLiteral:t}));
    }
  }
  return list;
}

function applyUnionType(ut) { //return AppliedUnionType
  var cases = [];
  pushTypes(cases, ut.l);
  return AppliedUnionType$jsint(ut, cases.rt$({t:Type$meta$model}), {Union$UnionType:{t:Anything}});
}
function applyIntersectionType(it) { //return AppliedIntersectionType
  var sats = [];
  pushTypes(sats, it.l);
  return AppliedIntersectionType$jsint(it, sats.rt$({t:Type$meta$model}), {Intersection$IntersectionType:{t:Anything}});
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
  if (bits&256)ans.push(abstract());
  if (bits&512)ans.push(annotation());
  if (bits&1024)ans.push(variable());
  return ans;
}
//Retrieve the docs from the specified path of the compile-time model
function doc$(root, path) {
  path = path.split(':');
  path.push('an','doc',0);
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
//Shortcut for getModules$meta().find(m/v).findPackage(p)
function fmp$(m,v,p) {
  return getModules$meta().find(m,v).findPackage(p);
}
ex$.fmp$=fmp$;
function lmp$(x,p) {
  return Modulo$jsint(x).findPackage(p);
}
ex$.lmp$=lmp$;
