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
            
            //AttributeDecl uri at caca.ceylon (2:2-2:26)
            defineAttr($$jsResource,'uri',function(){return this.uri_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:String$},$cont:JsResource,$an:function(){return[shared(),actual()];},d:['ceylon.language','Resource','$at','uri']};});
            
            //AttributeDecl size at caca.ceylon (3:2-3:34)
            defineAttr($$jsResource,'size',function(){
                var $$jsResource=this;
                return (-(1));
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Integer},$cont:JsResource,$an:function(){return[shared(),actual()];},d:['ceylon.language','Resource','$at','size']};});
            
            $$jsResource.textContent=function textContent(encoding$2){
                var $$jsResource=this;
                if(encoding$2===undefined){encoding$2=$$jsResource.textContent$defs$encoding(encoding$2);}
                throw wrapexc(Exception(String$("IMPL!",5)),'5:4-5:28','caca.ceylon');
            };$$jsResource.textContent.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:String$},$ps:[{$nm:'encoding',$mt:'prm',$def:1,$t:{t:String$},$an:function(){return[];}}],$cont:JsResource,$an:function(){return[shared(),actual()];},d:['ceylon.language','Resource','$m','textContent']};};
        })(JsResource.$$.prototype);
    }
    return JsResource;
}
$init$JsResource();
