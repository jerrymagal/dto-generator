package org.crow.dtogenerator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.crow.dtogenerator.model.Property;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DtoProperty {
	
	Class<?> type() default Property.class;
	
	String name() default "";
}
