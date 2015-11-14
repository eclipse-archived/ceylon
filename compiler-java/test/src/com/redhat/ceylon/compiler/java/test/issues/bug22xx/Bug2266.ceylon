@noanno
object foo2266 {}
@noanno
void bug2266(Boolean b) {
    Object anon1 = if(b) then foo2266 else nothing;
    \Ifoo2266 anon2 = if(b) then foo2266 else nothing;
    value anon3 = if(b) then foo2266 else nothing;
    
    Object anon4 = if(b) then nothing else foo2266;
    \Ifoo2266 anon5 = if(b) then nothing else foo2266;
    value anon6 = if(b) then nothing else foo2266;
    
    Object anon7 = switch(b) case(true) foo2266 else nothing;
    \Ifoo2266 anon8 = switch(b) case(true) foo2266 else nothing;
    value anon9 = switch(b) case(true) foo2266 else nothing;
    
    Object anon10 = switch(b) case(true) nothing else foo2266;
    \Ifoo2266 anon11 = switch(b) case(true) nothing else foo2266;
    value anon12 = switch(b) case(true) nothing else foo2266;
    
    Comparison cmp1 = if(b) then larger else nothing;
    \Ilarger cmp2 = if(b) then larger else nothing;
    value cmp3 = if(b) then larger else nothing;
    
    Comparison cmp4 = if(b) then nothing else larger;
    \Ilarger cmp5 = if(b) then nothing else larger;
    value cmp6 = if(b) then nothing else larger;
    
    Comparison cmp7 = switch(b) case(true) larger else nothing;
    \Ilarger cmp8 = switch(b) case(true) larger else nothing;
    value cmp9 = switch(b) case(true) larger else nothing;
    
    Comparison cmp10 = switch(b) case(true) nothing else larger;
    \Ilarger cmp11 = switch(b) case(true) nothing else larger;
    value cmp12 = switch(b) case(true) nothing else larger;
    
}
