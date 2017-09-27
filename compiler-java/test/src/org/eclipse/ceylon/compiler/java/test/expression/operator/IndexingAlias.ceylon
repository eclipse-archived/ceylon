import java.lang{IntArray}

@noanno
void indexingAlias() {
    alias Alias => IntArray;
    
    Alias arr = nothing;
    Integer x = arr[0];
}