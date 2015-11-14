function(_path) {
  var isdefmod=this.name==='default' && this.version==='unversioned';
  var mpath;
  if (isdefmod) {
    mpath = this.name;
  } else {
    mpath = this.name.replace(/\./g, '/');
  }
  var path = 'module-resources';
  if (_path[0]==='/') {
    path += _path;
  } else {
    if (isdefmod) {
      path += '';
    } else {
      path += '/';
      path += mpath;
    }
    path += '/' + _path;
  }
  if (runtime().name==='node.js') {
    var sep = operatingSystem().fileSeparator;
    path = mpath + (isdefmod?'/':'/'+this.version+'/') + path;
    path = path.replace(/\\/g,sep);
    var _fr=require;//this is so that requirejs leaves us alone
    var pm=_fr('path');
    var mods=process.env.NODE_PATH.split(operatingSystem().pathSeparator);
    var fs=_fr('fs');
    for (var i=0; i<mods.length; i++) {
      var fp = pm.resolve(mods[i], path);
      if (fs.existsSync(fp)) {
        var f = fs.statSync(fp);
        if (f && f.isFile()) {
          return JsResource$jsint('file:'+fp);
        }
      }
    }
    return null;
  } else if (runtime().name==='Browser') {
    return JsResource$jsint(require.toUrl(mpath));
  } else {
    throw AssertionError("resources loading not yet supported in this environment: " + runtime().name);
  }
}
