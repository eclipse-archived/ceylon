function JsResource(uri,$$jsResource){
  $init$JsResource();
  if($$jsResource===undefined)$$jsResource=new JsResource.$$;
  $$jsResource.uri_=uri;
  Resource($$jsResource);
  $$jsResource.$prop$getUri={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:JsResource,$an:function(){return[shared(),actual()];},d:['ceylon.language','Resource','$at','uri']};}};
  $$jsResource.$prop$getUri.get=function(){return uri};
  $$jsResource.$prop$getSize={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Integer},$cont:JsResource,$an:function(){return[shared(),actual()];},d:['ceylon.language','Resource','$at','size']};}};
  $$jsResource.$prop$getSize.get=function(){return size};
  return $$jsResource;
}
JsResource.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Basic},$ps:[{$nm:'uri',$mt:'prm',$t:{t:String$},$an:function(){return[shared(),actual()];}}],satisfies:[{t:Resource}],d:['ceylon.language','Resource']};};
function $init$JsResource(){
  if(JsResource.$$===undefined){
    initTypeProto(JsResource,'JsResource',Basic,Resource);
    (function($$jsResource){
      defineAttr($$jsResource,'uri',function(){return this.uri_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:JsResource,$an:function(){return[shared(),actual()];},d:['ceylon.language','Resource','$at','uri']};});
            
      defineAttr($$jsResource,'size',function(){
        var $elf=this;
        if (getRuntime().name === 'node.js') {
          return require('fs').statSync($elf.uri).size;
        } else {
          print("Resource handling unsupported in this JS platform.");
        }
        return -1;
      },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Integer},$cont:JsResource,$an:function(){return[shared(),actual()];},d:['ceylon.language','Resource','$at','size']};});
            
      $$jsResource.textContent=function textContent(encoding$2){
        var $elf=this;
        if(encoding$2===undefined){encoding$2=$elf.textContent$defs$encoding(encoding$2);}
        if (getRuntime().name === 'node.js') {
          var fs = require('fs');
          encoding$2=encoding$2.toLowerCase();
          if(encoding$2.initial(4)==="utf-")encoding$2='utf'+encoding$2.substring(4);
          var t = fs.readFileSync($elf.uri, encoding$2);
          return String$(t);
        } else {
          throw Error("Resource handling unsupported in this JS platform");
        }
      };$$jsResource.textContent.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:String$},$ps:[{$nm:'encoding',$mt:'prm',$def:1,$t:{t:String$},$an:function(){return[];}}],$cont:JsResource,$an:function(){return[shared(),actual()];},d:['ceylon.language','Resource','$m','textContent']};};
        })(JsResource.$$.prototype);
    }
    return JsResource;
}
$init$JsResource();
