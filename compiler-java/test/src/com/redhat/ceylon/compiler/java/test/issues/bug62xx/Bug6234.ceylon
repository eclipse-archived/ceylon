variable Integer count=0;

class Counter()
{
    shared Integer currentValue
    {
        return count;
    }
    
    shared void increment()
    {
        count++;
    }
    
    variable Integer count;
}