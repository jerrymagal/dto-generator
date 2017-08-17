package org.crew.dto.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention (RetentionPolicy.RUNTIME)  
@Target (ElementType.TYPE)
public @interface DTOClass {
	
	/**
	 * Nome do atributo 
	 */
	String name();
	
}