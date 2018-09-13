@noanno
void bug6682(){
    value j = Bug6682Java<Integer>();
    
    Bug6682Java<out String> outie = nothing;
    Bug6682Java<in String> innie = nothing;
    
    @type:"String" j.getParameterValueNo1(outie);
    //ERROR j.getParameterValueNo2(bug, "");
    @type:"String" j.getParameterValueNo3(outie);
    //ERROR j.getParameterValueNo4<String>(outie);
    @type:"Nothing" j.getParameterValueNo4(outie);
    
    @type:"Object" j.getParameterValueNo1(innie);
    //ERROR j.getParameterValueNo2(bug, "");
    //ERROR j.getParameterValueNo3(innie);
    @type:"String" j.getParameterValueNo4<String>(innie);
    @type:"Nothing" j.getParameterValueNo4(innie);
    
    @type:"Bug6682Java<String>" j.getParameterValue0(outie);
    @type:"Bug6682Java<out String>" j.getParameterValue1(outie);
    @type:"Bug6682Java<out String>" j.getParameterValue2(outie);
    @type:"Bug6682Java<in Nothing>" j.getParameterValue3(outie);
    @type:"List<out Bug6682Java<out String>>" j.getParameterValue4(outie);
    @type:"Bug6682Java<out List<out String>>" j.getParameterValue5(outie);
    
    @type:"Bug6682Java<out List<out String>>" j.getParameterValue6(outie);
    @type:"Bug6682Java<in Nothing>" j.getParameterValue7(outie);

    @type:"Bug6682Java<in String>" j.getParameterValue1(innie);
    @type:"Bug6682Java<out Object>" j.getParameterValue2(innie);
    @type:"Bug6682Java<in String>" j.getParameterValue3(innie);
    @type:"List<out Bug6682Java<in String>>" j.getParameterValue4(innie);
    @type:"Bug6682Java<out List<in String>>" j.getParameterValue5(innie);
    
    @type:"Bug6682Java<out List<in String>>" j.getParameterValue6(innie);
    @type:"Bug6682Java<in Nothing>" j.getParameterValue7(innie);
    
    @type:"Map<in String, out String>" j.getParameterValues(innie, outie);
    @type:"Map<out String, in String>" j.getParameterValues(outie, innie);
    @type:"Map<out String, out String>" j.getParameterValues(outie, outie);
    @type:"Map<in String, in String>" j.getParameterValues(innie, innie);
}