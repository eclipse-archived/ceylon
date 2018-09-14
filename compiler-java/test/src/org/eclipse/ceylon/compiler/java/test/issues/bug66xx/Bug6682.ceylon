import java.util { List }

@noanno
void bug6682(){
    List<out String> prod = nothing;
    List<in String> cons = nothing;
    
    @type:"Bug6682List<out String>" Bug6682List(prod);
    @type:"Bug6682List<in String>" Bug6682List(cons);
    
    value bug = Bug6682Java(1);
    
    Bug6682Java<out String> outie = Bug6682Java("");
    Bug6682Java<in String> innie = Bug6682Java("");
    
    @type:"String" bug.getParameterValueNo1(outie);
    //ERROR j.getParameterValueNo2(bug, "");
    @type:"String" bug.getParameterValueNo3(outie);
    //ERROR j.getParameterValueNo4<String>(outie);
    @type:"Nothing" bug.getParameterValueNo4(outie);
    
    @type:"Object" bug.getParameterValueNo1(innie);
    //ERROR j.getParameterValueNo2(bug, "");
    //ERROR j.getParameterValueNo3(innie);
    @type:"String" bug.getParameterValueNo4<String>(innie);
    @type:"Nothing" bug.getParameterValueNo4(innie);
    
    @type:"Bug6682Java<String>" bug.getParameterValue0(outie);
    @type:"Bug6682Java<out String>" bug.getParameterValue1(outie);
    @type:"Bug6682Java<out String>" bug.getParameterValue2(outie);
    @type:"Bug6682Java<in Nothing>" bug.getParameterValue3(outie);
    @type:"List<out Bug6682Java<out String>>" bug.getParameterValue4(outie);
    @type:"Bug6682Java<out List<out String>>" bug.getParameterValue5(outie);
    
    @type:"Bug6682Java<out List<out String>>" bug.getParameterValue6(outie);
    @type:"Bug6682Java<in Nothing>" bug.getParameterValue7(outie);

    @type:"Bug6682Java<in String>" bug.getParameterValue1(innie);
    @type:"Bug6682Java<out Object>" bug.getParameterValue2(innie);
    @type:"Bug6682Java<in String>" bug.getParameterValue3(innie);
    @type:"List<out Bug6682Java<in String>>" bug.getParameterValue4(innie);
    @type:"Bug6682Java<out List<in String>>" bug.getParameterValue5(innie);
    
    @type:"Bug6682Java<out List<in String>>" bug.getParameterValue6(innie);
    @type:"Bug6682Java<in Nothing>" bug.getParameterValue7(innie);
    
    @type:"Map<in String, out String>" bug.getParameterValues(innie, outie);
    @type:"Map<out String, in String>" bug.getParameterValues(outie, innie);
    @type:"Map<out String, out String>" bug.getParameterValues(outie, outie);
    @type:"Map<in String, in String>" bug.getParameterValues(innie, innie);
    
    @type:"Bug6682Java<Bug6682Java<out String>>" Bug6682Java(outie);
    @type:"Bug6682Java<Bug6682Java<in String>>" Bug6682Java(innie);
    

}