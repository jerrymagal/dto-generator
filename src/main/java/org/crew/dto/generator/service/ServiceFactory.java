package org.crew.dto.generator.service;

import java.lang.reflect.Type;

import org.crew.dto.generator.interceptor.ModelInterceptor;

public class ServiceFactory {


	public static <T> T getService(Class<VeiculoService> classTemplate) {
		try {
			Object template = createClass(classTemplate);
			return (T) getService(template);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static <T> T getService(T template) {
		
		try {
			
			Class interfaceClazz = getInterface(template);
			Class templateClazz = getTemplateClazz(template);
			Object modelClazz = createClass(templateClazz);	
			
			return (T) ModelInterceptor.getProxy(modelClazz, interfaceClazz);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static <T> Class getInterface(T template) throws ClassNotFoundException {
		Type tType = template.getClass().getInterfaces()[0];
		//Parse it as String
		String className = tType.toString().split(" ")[1];
		Class clazz = Class.forName(className);
		return clazz;
	}
	
	private static <T> T createClass(Class<T> clazz) throws InstantiationException, IllegalAccessException{
        return clazz.newInstance();
    }
	
	private static <T> Class getTemplateClazz(T template) throws ClassNotFoundException {
		Type mySuperclass = template.getClass();//.getGenericSuperclass();
		//Type tType = ((ParameterizedType)mySuperclass).getActualTypeArguments()[0];
		//Parse it as String
		String className = mySuperclass.toString().split(" ")[1];
		Class clazz = Class.forName(className);
		return clazz;
	}

}
