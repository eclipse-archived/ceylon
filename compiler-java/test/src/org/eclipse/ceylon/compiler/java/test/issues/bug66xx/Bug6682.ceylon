@noanno
void bug6682(){
    value j = Bug6682Java<Integer>();
    
    Bug6682Java<out String> bug = nothing;
    
    @type:"String" j.getParameterValueNo1(bug);
    //ERROR j.getParameterValueNo2(bug, "");
    @type:"String" j.getParameterValueNo3(bug);
    @type:"Nothing" j.getParameterValueNo4(bug);
    
    @type:"Bug6682Java<String>" j.getParameterValue0(bug);
    @type:"Bug6682Java<out String>" j.getParameterValue1(bug);
    @type:"Bug6682Java<out String>" j.getParameterValue2(bug);
    @type:"Bug6682Java<in Nothing>" j.getParameterValue3(bug);
    @type:"List<out Bug6682Java<out String>>" j.getParameterValue4(bug);
    @type:"Bug6682Java<out List<out String>>" j.getParameterValue5(bug);
    
    @type:"Bug6682Java<out List<out String>>" j.getParameterValue6(bug);
    @type:"Bug6682Java<in Nothing>" j.getParameterValue7(bug);

}