function(meta,mod$) {
  mod$.$anns=mod$.meta.$mod$ans$;
  var mm = mod$.meta.$CCMM$;
  if (typeof(mm)==='function') {
    mm=mm();mod$.meta.$CCMM$=mm;
  }
  mod$.m$=mm;
  mod$.nm$=mm['$mod-name'];
  mod$.ver$=mm['$mod-version'];
}
