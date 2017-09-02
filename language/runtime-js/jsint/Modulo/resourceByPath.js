function(_path) {
  var isdefmod = this.name==='default' 
	          && this.version==='unversioned';
  var mpath;
  if (isdefmod) {
    mpath = this.name;
  } else {
    mpath = this.name.replace(/\./g, '/');
  }
  var p = "";
  if (_path[0]==='/') {
    p = _path;
  } else {
    if (isdefmod) {
      p = '';
    } else {
      p = '/' + mpath 
    }
    p += '/' + _path;
  }
  var path = 'module-resources' + p;
  var sep = operatingSystem().fileSeparator;
  path = mpath + (isdefmod?'/':'/'+this.version+'/') + path;
  path = path.replace(/\\/g,sep);
  if (runtime().name==='node.js') {
    var _fr=require;//this is so that requirejs leaves us alone
    var pm=_fr('path');
    var mods=process.env.NODE_PATH.split(operatingSystem().pathSeparator);
    var fs=_fr('fs');
    for (var i=0; i<mods.length; i++) {
      var fp = pm.resolve(mods[i], path);
      if (fs.existsSync(fp)) {
        var f = fs.statSync(fp);
        if (f && f.isFile()) {
          return JsResource$jsint('file:'+fp, p, this.meta);
        }
      }
    }
    return null;
  } else if (runtime().name==='Browser') {
    return JsResource$jsint(require.toUrl(path), p, this.meta);
  } else {
    throw AssertionError("resources loading not yet supported in this environment: " + runtime().name);
  }
}
