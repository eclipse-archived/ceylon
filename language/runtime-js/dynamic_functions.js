//Dress up a native JS object as the specified type
//(which should be a Ceylon dynamic interface)
function dre$$(object, type, loc) {
  //If it's already dressed up as the type, leave it alone
  if (is$(object, type))return object;
  //If it's already of another type, throw
  if (object.$$ !== undefined)throw new Error("Cannot modify the type of an object at runtime " + loc);
  //Check members
  var actual = Object.getOwnPropertyNames(object);
  var sats = type.t.$$.prototype.getT$all();
  for (var sat in sats) {
    var expected = sats[sat].dynmem$;
    if (typeof(expected)==='function') {
      for (var i=0; i < expected.length; i++) {
        if (actual.indexOf(expected[i])<0) {
          throw new Error("Native object is missing property " + expected[i] + " " + loc);
        }
      }
    }
  }
  object.$$=type.t.$$;
  object.getT$name=function(){return type.t.$$.T$name};
  object.getT$all=function(){return type.t.$$.T$all};
  if (actual.indexOf('string')<0) {
    atr$(object,'string',function(){return object.toString();},undefined,
         $_Object.$$.prototype.$prop$getString.$crtmm$);
  }
  if (actual.indexOf('hash')<0) {
    atr$(object,'hash',function(){return identityHash(object);},undefined,
         $_Object.$$.prototype.$prop$getHash.$crtmm$);
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
