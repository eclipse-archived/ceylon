function find(name,version){
    var modname = name + "/" + (version?version:"unversioned");
    var lm = $loadedModules$[modname];
    if (!lm) {
      var mpath;
      if (name === 'default' && version=='unversioned') {
        mpath = name + "/" + name;
      } else {
        mpath = name.replace(/\./g,'/') + '/' + version + "/" + name + "-" + version;
      }
      try {lm = require(mpath);}catch(e){return null;}
    }
    if (lm && lm.$CCMM$) {
      lm = Modulo(lm);
      $loadedModules$[modname] = lm;
    }
    return lm === undefined ? null : lm;
}
