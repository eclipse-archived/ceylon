@noanno
void bug6682(){
    value j = Bug6682Java<Integer>();
    
    // cov
    @type:"String(Bug6682Java<out String>?)"
    value f1 = j.getParameterValue<String>;

    @type:"Bug6682Java<out String>(Bug6682Java<out String>?)"
    value f1b = j.getParameterValue2<String>;
    
    @type:"Bug6682Java<in String>(Bug6682Java<in String>?)"
    value f2 = j.getParameterValueNo1<String>;

    @type:"String(Bug6682Java<String>?, String?)"
    value f3 = j.getParameterValueNo2<String>;

    @type:"String(Bug6682Java<out String>?)"
    value f4 = j.getParameterValueNo3<String>;

    @type:"String(Bug6682Java<in String>?)"
    value f5 = j.getParameterValueNo4<String>;

    // contra
    @type:"Bug6682Java<in String>(Bug6682Java<in String>?)"
    value fin1b = j.getParameterValueIn<String>;
    
    @type:"Bug6682Java<out String>(Bug6682Java<out String>?)"
    value fin2 = j.getParameterValueInNo1<String>;
    
    @type:"String(Bug6682Java<String>?, String?)"
    value fin3 = j.getParameterValueInNo2<String>;
    
    @type:"String(Bug6682Java<out String>?)"
    value fin4 = j.getParameterValueInNo3<String>;
    
    @type:"String(Bug6682Java<in String>?)"
    value fin5 = j.getParameterValueInNo4<String>;
}