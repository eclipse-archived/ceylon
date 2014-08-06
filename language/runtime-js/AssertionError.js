function AssertionError(msg,that){
  $init$AssertionError();
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
    that.suppressed=[].rt$({t:Throwable});
  }
  that.$sm_=msg;
  Throwable(msg,undefined,that);
  return that;
}

