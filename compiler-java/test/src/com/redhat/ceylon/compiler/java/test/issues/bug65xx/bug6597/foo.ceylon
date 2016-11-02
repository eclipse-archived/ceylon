import org.springframework.web.bind.annotation {
        restController,
        requestMapping,
        requestBody,
        RequestMethod {
                post = \iPOST,
                get = \iGET
        },
        pathVariable
}
import org.springframework.boot {
        SpringApplication
}

shared void foo(){
    value springApplication = SpringApplication();

}