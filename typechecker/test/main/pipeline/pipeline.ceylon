
void pipelineTests() {
    "hello world"
        |> String.size
        |> ((i) => Integer.format(i, 16)) 
        |> String.uppercased 
        |> String.trimmed 
        |> print;
    
    $type:"Boolean"
    "hello world"
            |> String.size
            |> ((i) => Integer.format(i, 16)) 
            |> String.empty;
    
    $error 
    1
        |> String.size
        |> ((i) => Integer.format(i, 16)) 
        |> String.uppercased 
        |> String.trimmed 
        |> print;
    
    $error 
    "hello world"
        |> String.string
        |> ((i) => Integer.format(i, 16)) 
        |> String.uppercased 
        |> String.trimmed 
        |> print;
    
    $type:"Anything(String)"
    value fun 
        = String.size
        >|> ((i) => Integer.format(i, 16)) 
        >|> String.uppercased 
        >|> String.trimmed 
        >|> print;
    
    $type:"Boolean({Object*})"
    value nun 
        = ", ".join
        >|> String.size
        >|> ((i) => Integer.format(i, 16)) 
        >|> String.trimmed 
        >|> String.empty;
    
    $error
    value gun 
        = String.string
        >|> ((i) => Integer.format(i, 16)) 
        >|> String.uppercased 
        >|> String.trimmed 
        >|> print;
    
    $type:"Integer(String)" 
    value sun 
        = String.size 
        >|> ((i) => Integer.format(i, 16)) 
        >|> ((s) => s.size);
}
