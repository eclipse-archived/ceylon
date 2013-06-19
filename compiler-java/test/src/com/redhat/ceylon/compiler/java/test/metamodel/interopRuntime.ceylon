import ceylon.language.metamodel { Class }

void interopRuntime(){
    Class<JavaType,[Boolean,Integer,Integer,Integer,Integer,Float,Float,Character,String,Object]> javaType = `JavaType`;
    
    value instance = javaType(true,1,2,3,4,1.0,2.0,'a',"a","b");
    print(instance);
}