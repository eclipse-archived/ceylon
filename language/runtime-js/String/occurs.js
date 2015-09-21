function (e,from,len) {
  var fo=this.firstOccurrence(e,from,len);
  return fo!==null && fo>=0;
}
