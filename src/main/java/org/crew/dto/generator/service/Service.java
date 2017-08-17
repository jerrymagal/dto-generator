package org.crew.dto.generator.service;

import org.crew.dto.generator.annotation.DTOInterceptor;

public interface Service<T> {
	
	@DTOInterceptor
	public T recuperar();
	
}
