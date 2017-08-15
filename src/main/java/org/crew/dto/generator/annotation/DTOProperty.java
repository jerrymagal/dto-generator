package org.crew.dto.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention (RetentionPolicy.RUNTIME)  
@Target (ElementType.FIELD)
public @interface DTOProperty {

	/**
	 * Nome do atributo 
	 */
	String name();
	
	/**
	 * Nome do atributo 
	 * @return 
	 */
	Class<?> type();

	/*
	 * Indica se deve ser atualizado ou não
	 */
	boolean readOnly() default false;

	String property();

}
