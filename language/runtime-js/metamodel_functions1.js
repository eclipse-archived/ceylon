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

function pushTypes(list, types,targ$2) {
  for (var i=0; i<types.length; i++) {
    var t = types[i];
    if (t.t === 'u') {
      list.push(applyUnionType(t, targ$2));
    } else if (t.t === 'i') {
      list.push(applyIntersectionType(t, targ$2));
    } else if (typeof(t)==='string') {
      list.push(typeLiteral$meta({Type$typeLiteral:targ$2[t]},targ$2));
    } else {
      list.push(typeLiteral$meta({Type$typeLiteral:t},targ$2));
    }
  }
  return list;
}

function applyUnionType(ut,targ$2) { //return AppliedUnionType
  var cases = pushTypes([], ut.l,targ$2);
  return AppliedUnionType$jsint(ut, cases.rt$(ut), {Union$AppliedUnionType:ut});
}
function applyIntersectionType(it,targ$2) { //return AppliedIntersectionType
  var sats = pushTypes([], it.l,targ$2);
  return AppliedIntersectionType$jsint(it, sats.rt$(it), {Union$AppliedIntersectionType:it});
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
//Shortcut for modules$meta().find(m/v).findPackage(p)
function fmp$(m,v,p) {
  return modules$meta().find(m,v).findPackage(p);
}
ex$.fmp$=fmp$;
function lmp$(x,p) {
  return Modulo$jsint(x).findPackage(p);
}
ex$.lmp$=lmp$;
//Some type arithmetic for metamodel queries
//rt=real type, ta=Container type argument
function mmfca$(rt, ta) {
  if (!ta.t.$$)return rt;
  ta=ta.t;
  if (extendsType({t:rt},{t:ta}))return ta;
  if (extendsType({t:ta},{t:rt}))return rt;
  var mm=getrtmm$$(ta);
  if (mm) {
    //Supertypes
    var st;while((st=mm['super'])) {
      if (extendsType({t:rt},st)){
        return st.t;
      }
      mm=getrtmm$$(st.t);
    }
    //Satisfied types
    mm=getrtmm$$(ta);
    var sts=mm['sts'];
    if (sts) for(var i=0;i<sts.length;i++) {
      st=sts[i];
      if (st.t.$$ && extendsType({t:rt},st))return st.t;
      //TODO recurse st?
    }
  }
  return rt;
}
ex$.mmfca$=mmfca$;
//Retrieve a prototype's member, UNLESS it's a property
function getnpmem$(proto,n) {
  if (n.substring(0,6)==='$prop$')return undefined;
  if (proto['$prop$get'+n[0].toUpperCase()+n.substring(1)])return undefined;
  return proto[n];
}
