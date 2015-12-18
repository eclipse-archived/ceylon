//Dress up a native JS object as the specified type
//(which should be a Ceylon dynamic interface)
function dre$$(object, type, loc) {
  //If it's already dressed up as the type, leave it alone
  if (is$(object, type))return object;
  //If it's already of another type, throw
  if (object.$$ !== undefined)throw new Error("Cannot modify the type of an object at runtime " + loc);
  //Check members
  var actual = typeof(object)==='object'?Object.getOwnPropertyNames(object):[];
  var sats = type.t.$$.prototype.getT$all();
  for (var sat in sats) {
    var expected = sats[sat].dynmem$;
    if (expected) {
      for (var i=0; i < expected.length; i++) {
        if (actual.indexOf(expected[i])<0) {
          throw new Error("Native object is missing property " + expected[i] + " " + loc);
        } else {
          var propname="$prop$get"+expected[i][0].uppercased+expected[i].substring(1);
          var proptype=type.t.$$.prototype[propname];
          if (proptype) {
            proptype=getrtmm$$(proptype);
          }
          var val=object[expected[i]];
          if (proptype && proptype.$t && !is$(val,proptype.$t)) {
            if (proptype.$t.t===$_Array) {
              object[expected[i]]=natc$(val,proptype.$t.a.Element$Array,loc);
            } else if (proptype.$t.t===Integer) {
              object[expected[i]]=ndnc$(val,'i',loc);
            } else if (proptype.$t.t===Float) {
              object[expected[i]]=ndnc$(val,'f',loc);
            } else {
              object[expected[i]]=ndtc$(val,proptype.$t,loc);
            }
          }
        }
      }
    }
  }
  object.$$=type.t.$$;
  object.getT$name=function(){return type.t.$$.T$name};
  object.getT$all=function(){return type.t.$$.T$all};
  if (typeof(object)==='object') {
    if (actual.indexOf('string')<0) {
      atr$(object,'string',function(){return object.toString();},undefined,
           $_Object.$$.prototype.$prop$getString.$crtmm$);
    }
    if (actual.indexOf('hash')<0) {
      atr$(object,'hash',function(){return identityHash(object);},undefined,
           $_Object.$$.prototype.$prop$getHash.$crtmm$);
    }
  }
  return object;
}
ex$.dre$$=dre$$;
//print native dynamic object
function pndo$(o) {
  if (o === undefined)print("<undefined>");
  else if (o === null)print("<null>");
  else if (is$(o,{t:Anything}))print(o);
  else if (o.string)print(o.string);
  else {
    print(o.toString());
  }
}
ex$.pndo$=pndo$;
//check if numbers are really numbers
function ndnc$(n,t,loc) {
  if (t==='f') {
    if (typeof(n)==='number')return Float(n);
    if (is$(n,{t:Float}))return n;
  } else if (t==='i') {
    if (typeof(n)==='number')return Math.floor(n);
    if (is$(n,{t:Integer}))return n;
  }
  throw new TypeError('Expected ' + (t==='f'?'Float':'Integer') + ' (' + loc + ')');
}
ex$.ndnc$=ndnc$;
//Check if an object if really of a certain type
function ndtc$(o,t,loc) {
  if (is$(o,t))return o;
  throw new TypeError('Expected ' + qname$(t) + ' (' + loc + ')');
}
ex$.ndtc$=ndtc$;
//Box native array, checking elements' type
function natc$(a,t,loc) {
  if (a===empty())return $arr$([],t);
  if (Array.isArray(a)) {
    for (var i=0;i<a.length;i++) {
      if (!is$(a[i],t)) {
        a[i]=dre$$(a[i],t,loc);
      }
    }
    return $arr$(a,t);
  }
  throw new TypeError('Expected ' + qname$(t) + ' (' + loc + ')');
}
ex$.natc$=natc$;
