@nomodel
shared Absent|Value bug973_first<Value,Absent>(Iterable<Value,Absent> values)
        given Absent satisfies Null 
        => values.first;
@nomodel
Integer bug973_ff=bug973_first { for (x in 1..5) x };