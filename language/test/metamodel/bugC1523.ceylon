import ceylon.language.meta { modules }

@test
shared void bugC1523() {
    try{
        print("by `` `module modules.optional`.annotations<AuthorsAnnotation>() ``");
        assert(false);
    }catch(AssertionError x){
        assert(x.message == "Module modules.optional/1 is not available");
    }

    value cm = modules.find("modules.optional", "1");
    if (exists cm) {
        print("by ``cm.annotations<AuthorsAnnotation>()``");
    } else {
        print ("not found");
    }

    if(exists default = modules.default){
        default.annotations<AuthorsAnnotation>();
    }else{
        print("No default module available, not testing for its annotations");
    }
    if(exists jdk = modules.find("java.base", "7")){
        jdk.annotations<AuthorsAnnotation>();
    }else{
        print("No JDK available, not testing for its annotations");
    }
}