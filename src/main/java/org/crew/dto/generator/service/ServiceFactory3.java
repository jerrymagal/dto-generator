package org.crew.dto.generator.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.crew.dto.generator.interceptor.ModelInterceptor;
import org.crew.dto.generator.model.Veiculo;

public class ServiceFactory3<T> {

	public Service getService() {
		System.out.println("model service");

		try {
			Class interfaceClazz = getInterface();
			
			//Class templateClazz = getTemplateClazz();
			//T modelClazz = createClass(templateClazz);
			
			return (Service) ModelInterceptor.getProxy((T) this, interfaceClazz);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private Class getInterface() throws ClassNotFoundException {
		Type tType = this.getClass().getInterfaces()[0];
		//Parse it as String
		String className = tType.toString().split(" ")[1];
		Class clazz = Class.forName(className);
		return clazz;
	}
	
	private T createClass(Class<T> clazz) throws InstantiationException, IllegalAccessException{
        return clazz.newInstance();
    }
	
	private Class getTemplateClazz() throws ClassNotFoundException {
		Type mySuperclass = this.getClass().getGenericSuperclass();
		Type tType = ((ParameterizedType)mySuperclass).getActualTypeArguments()[0];
		//Parse it as String
		String className = tType.toString().split(" ")[1];
		Class clazz = Class.forName(className);
		return clazz;
	}
}
