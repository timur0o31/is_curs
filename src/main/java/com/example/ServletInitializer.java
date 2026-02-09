<<<<<<<< HEAD:src/main/java/ServletInitializer.java
========
package com.example;
>>>>>>>> rob:src/main/java/com/example/ServletInitializer.java

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(IsCursApplication.class);
    }

}
