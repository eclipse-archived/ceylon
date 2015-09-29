function printStackTrace(exc, _write) {
  if (_write===undefined)_write=$_process().writeError;
  var _c = className(exc);
  if (exc.message.size > 0) {
    _c += ' "' + exc.message + '"';
  }
  _write(_c);_write(operatingSystem().newline);
  for (var i=0; i<exc.stack_trace.length; i++) {
    var f = exc.stack_trace[i];
    if (f == "CIRCULAR") {
      _write("    (Infinite loop detected)");
      _write(operatingSystem().newline);
      return;
    }
    if (typeof(f)==='string') {
      _write(f);
      _write(operatingSystem().newline);
      f=null;
    }
    var mm = getrtmm$$(f);
    if (mm && mm.d) {
      var _src = '';
      if (i==0) {
        if (exc.$loc && exc.$file) _src = ' (' + exc.$file + " " + exc.$loc + ')';
      }
      var pkn=mm.d[0];
      if (pkn==='$')pkn='ceylon.language';
      _write("    at " + pkn + "::" + mm.d[mm.d.length-1] + _src);_write(operatingSystem().newline);
    }
  }
}
