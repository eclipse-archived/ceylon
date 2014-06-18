//Dress up a native JS object as the specified type
//(which should be a Ceylon dynamic interface)
function dre$$(object, type, loc) {
  //If it's already dressed up as the type, leave it alone
  if (is$(object, type))return;
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
  return object;
}
ex$.dre$$=dre$$;
