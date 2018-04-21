function(meta,mod$) {
  mod$.$anns=mod$.meta.$mod$ans$;
  var mm = mod$.meta.$M$;
  if (typeof(mm)==='function') {
    mm=mm();
  }
  mod$.m$=mm;
  mod$.nm$=mm['$mod-name'];
  mod$.ver$=mm['$mod-version'];
}
