function resourceByPath(_path) {
  var mpath;
  var sep = getOperatingSystem().fileSeparator;
  if (this.name === 'default' && this.version=='unversioned') {
    mpath = this.name;
  } else {
    mpath = this.name.replace(/\./g,sep) + sep + this.version;
  }
  if (_path[0]===sep) {
    mpath += _path;
  } else {
    mpath += sep + _path;
  }
  if (getRuntime().name === 'node.js') {
    var _fr=require;//this is so that requirejs leaves us the fuck alone
    var pm=_fr('path');
    var mods=process.env.NODE_PATH.split(getOperatingSystem().pathSeparator);
    var fs=_fr('fs');
    for (var i=0; i<mods.length; i++) {
      var fp = pm.resolve(mods[i], mpath);
      if (fs.existsSync(fp)) {
        var f = fs.statSync(fp);
        if (f && f.isFile()) {
          return JsResource('file:'+fp);
        }
      }
    }
  } else if (getRuntime().name === 'Browser') {
    return JsResource(require.toUrl(mpath));
  } else {
    print("Resources unsupported in this environment.");
  }
  return null;
}
