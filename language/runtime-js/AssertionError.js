function AssertionError(msg,cause,that){
  $i$AssertionError();
  if (cause===undefined)cause=null;
  if(that===undefined) {
    that=new Error(msg);
    that.getT$name=function(){return AssertionError.$$.prototype.constructor.T$name; };
    that.getT$all=function(){return AssertionError.$$.prototype.constructor.T$all; };
    that.equals=function(o){return this===o;};
    that.description=msg;
    that.message=msg;
    that.string='ceylon.language::AssertionError "'+msg+'"';
    that.printStackTrace=function(){printStackTrace(that);}
    that.addSuppressed=Throwable.$$.prototype.addSuppressed;
    that.suppressed=empty();
  }
  that.$sm_=msg;
  Throwable(msg,cause,that);
  return that;
}

