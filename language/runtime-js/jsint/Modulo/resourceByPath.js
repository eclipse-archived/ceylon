function resourceByPath(_path) {
  var mpath;
  if (this.name==='default' && this.version==='unversioned') {
    mpath = this.name;
  } else {
    mpath = this.name.replace(/\./g, '/');
  }
  var path = 'module-resources';
  if (_path[0]==='/') {
    path += _path;
  } else {
    if (this.name==='default' && this.version==='unversioned') {
      path += '';
    } else {
      path += '/';
      path += mpath;
    }
    path += '/' + _path;
  }
  if (getRuntime().name==='node.js') {
    var sep = getOperatingSystem().fileSeparator;
    path = mpath + '/' + this.version + '/' + path;
    path = path.replace(/\\/g,sep);
    var _fr=require;//this is so that requirejs leaves us alone
    var pm=_fr('path');
    var mods=process.env.NODE_PATH.split(getOperatingSystem().pathSeparator);
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
  } else if (getRuntime().name==='Browser') {
    return JsResource$jsint(require.toUrl(mpath));
  } else {
    throw AssertionError("resources loading not yet supported in this environment: " + getRuntime().name);
  }
}
