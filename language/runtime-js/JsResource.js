function JsResource(uri,$$jsResource){
  $init$JsResource();
  if($$jsResource===undefined)$$jsResource=new JsResource.$$;
  $$jsResource.uri_=uri;
  Resource($$jsResource);
  $$jsResource.$prop$getUri={$crtmm$:function(){return{mod:$CCMM$,$t:{t:String$},$cont:JsResource,$an:function(){return[shared(),actual()];},d:['$','Resource','$at','uri']};}};
  $$jsResource.$prop$getUri.get=function(){return uri};
  $$jsResource.$prop$getSize={$crtmm$:function(){return{mod:$CCMM$,$t:{t:Integer},$cont:JsResource,$an:function(){return[shared(),actual()];},d:['$','Resource','$at','size']};}};
  $$jsResource.$prop$getSize.get=function(){return size};
  return $$jsResource;
}
JsResource.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},$ps:[{$nm:'uri',$mt:'prm',$t:{t:String$},$an:function(){return[shared(),actual()];}}],satisfies:[{t:Resource}],d:['$','Resource']};};
function $init$JsResource(){
  if(JsResource.$$===undefined){
    initTypeProto(JsResource,'JsResource',Basic,Resource);
    (function($$jsResource){
      $defat($$jsResource,'uri',function(){return this.uri_;},undefined,function(){return{mod:$CCMM$,$t:{t:String$},$cont:JsResource,$an:function(){return[shared(),actual()];},d:['$','Resource','$at','uri']};});
            
      $defat($$jsResource,'size',function(){
        var $elf=this;
        if (getRuntime().name === 'node.js') {
          var _fr=rq$;
          var _fp=$elf.uri;
          if (_fp.substring(0,5)==='file:')_fp=_fp.substring(_fp.indexOf(':')+1);
          return _fr('fs').statSync(_fp).size;
        } else if (getRuntime().name === 'Browser') {
          alert('Resource.size not implemented yet');
        } else {
          print("Resource handling unsupported in this JS platform.");
        }
        return -1;
      },undefined,function(){return{mod:$CCMM$,$t:{t:Integer},$cont:JsResource,$an:function(){return[shared(),actual()];},d:['$','Resource','$at','size']};});
            
      $$jsResource.textContent=function textContent(encoding$2){
        var $elf=this;
        if(encoding$2===undefined){encoding$2=$elf.textContent$defs$encoding(encoding$2);}
        if (getRuntime().name === 'node.js') {
          var _fr=rq$;
          var fs=_fr('fs');
          encoding$2=encoding$2.toLowerCase();
          if(encoding$2.initial(4)==="utf-")encoding$2='utf'+encoding$2.substring(4);
          var _fp=$elf.uri;
          if (_fp.substring(0,5)==='file:')_fp=_fp.substring(_fp.indexOf(':')+1);
          var t = fs.readFileSync(_fp, encoding$2);
          return String$(t);
        } else if (getRuntime().name === 'Browser') {
          alert("Resource.textContent() not implemented yet");
        } else {
          throw Error("Resource handling unsupported in this JS platform");
        }
      };$$jsResource.textContent.$crtmm$=function(){return{mod:$CCMM$,$t:{t:String$},$ps:[{$nm:'encoding',$mt:'prm',$def:1,$t:{t:String$},$an:function(){return[];}}],$cont:JsResource,$an:function(){return[shared(),actual()];},d:['$','Resource','$m','textContent']};};
        })(JsResource.$$.prototype);
    }
    return JsResource;
}
$init$JsResource();
