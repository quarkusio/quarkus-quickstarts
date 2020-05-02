package org.acme.tokenmanager.config;

import javax.inject.Qualifier;
import java.lang.annotation.*;

@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DefaultCache {
}
