native("jvm")
module com.redhat.ceylon.compiler.java.test.issues.bug65xx.bug6597 "1" {
    import "org.springframework.boot:spring-boot-starter-web" "1.3.5.RELEASE";
    import "org.springframework.boot:spring-boot-starter-undertow" "1.3.5.RELEASE";
    import "org.springframework.boot:spring-boot-starter-data-jpa" "1.3.5.RELEASE";

    import "org.springframework.cloud:spring-cloud-starter-eureka" "1.1.2.RELEASE"; 
    
    import "org.postgresql:postgresql" "9.4.1208";
}