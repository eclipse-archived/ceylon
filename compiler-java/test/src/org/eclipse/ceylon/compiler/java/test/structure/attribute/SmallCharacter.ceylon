@noanno
class SmallCharacter(shared small Character charp) {
    small variable Character chara = charp;
    small Character charad;
    this.charad = 'a';
    
    // TODO setters!
    // TODO captured variables
    shared small Character chargs => 'a';
    assign chargs {}
    
    void charAssignmentOps() {
        variable Character long = 'a';
        chara = long;
        this.chara = 'a';
        this.chara = long;
        chargs = long;
        long = chara;
        chara = 'a';
        
        small value localchargs => 'a';
        assign localchargs {
            
        }
        localchargs = long;
        long = localchargs;
        
        long = chara = 'a';
        chara = long = 'a';
    }
    
    
    shared void intSmallArithmeticOperators() {
        // These should all be transformed as char expressions
        
        
        chara++;
        chara--;
        ++chara;
        --chara;
        
        this.chara++;
        this.chara--;
        ++this.chara;
        --this.chara;
    }
    
    shared void charComparisonOps() {
        
        variable value c = chara == charp;
        c = chara != charp;
        c = chara < charp;
        c = chara <= charp;
        c = chara > charp;
        c = chara >= charp;
        c = '\0' < chara < charp;
        c = '\0' <= chara <= charp;
        
        variable value cmp = chara <=> charp;
    }
    
    
    void charParamDefault(p1='a', small Character p2=p1) {
        small Character p1;
    }
    
    void powerMethodIsNotSmall(small Character x, small Character y) {
        variable value cmp = x.compare(y);
        cmp = x.compare{ other = y; };
    }
    
    void argumentConversion() {
        powerMethodIsNotSmall('a', 'a');
        powerMethodIsNotSmall{
            x='a'; 
            y='a';
        };
        powerMethodIsNotSmall{
            x='a'; 
            y=>'a';
        };
    }
    
}