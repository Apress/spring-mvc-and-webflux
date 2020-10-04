package com.apress.prospringmvc.bookstore.web.support;

import java.lang.annotation.*;

/**
 * Annotation to mark a method argument or return value to be retrieved or stored in the {@code javax.servlet.http.HttpSession}.
 * 
 * @author Marten Deinum

 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionAttribute {

    /**
     * The name of the session attribute to bind to.
     */
    String value() default "";

    /**
     * Whether the parameter is required.
     */
    boolean required() default true;

    /**
     * Wheter attribute needs to be exposed as model attribtue.
     */
    boolean exposeAsModelAttribute() default false;
}
