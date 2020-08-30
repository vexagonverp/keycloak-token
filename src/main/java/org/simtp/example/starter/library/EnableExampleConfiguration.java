package org.simtp.example.starter.library;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ExampleConfiguration.class)
public @interface EnableExampleConfiguration {
}
