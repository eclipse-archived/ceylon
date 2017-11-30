String hello = "hello";
String goodbye = "goodbye";

void elsecase(String string, Object obj) {
    $error switch (string)
    case (hello) {}
    case (goodbye) {}
    else {}
    
    $error switch (string)
    case (hello) {}
    case (is String) {}
    else {}
    
    $error switch (string)
    case (is String) {}
    case (hello) {}
    else {}
    
    $error switch (string)
    case ("hello") {}
    case (is String) {}
    else {}
    
    $error switch (obj)
    case (is String) {}
    case (is Object) {}

    switch (string)
    case (hello) {}
    else case (goodbye) {}
    else {}
    
    switch (string)
    case (hello) {}
    else case (is String) {}
    else {}
    
    switch (string)
    case (is String) {}
    else case (hello) {}
    else {}
    
    switch (string)
    case ("hello") {}
    else case (is String) {}
    else {}
    
    switch (obj)
    case (is String) {}
    else case (is Object) {}
}
