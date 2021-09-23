package io.github.vexagonverp.keycloaktoken;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(KeycloakTokenConfiguration.class)
public @interface EnableKeycloakTokenConfiguration {
}
