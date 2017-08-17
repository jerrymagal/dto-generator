package org.crew.dto.generator.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.crew.dto.generator.annotation.DTOInterceptor;
import org.crew.dto.generator.annotation.DTOPropertyModel;
import org.crew.dto.generator.dto.DTO;

public class ModelInterceptor<T> implements InvocationHandler {

  private T t;

  public ModelInterceptor(T t) {
      this.t = t;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      //System.out.println("before method call : " + method.getName());
      Object result = method.invoke(t, args);
      //System.out.println("after method call : " + method.getName());
      
      Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
      
      for (Annotation annotation : declaredAnnotations) {
		if(annotation.annotationType().equals(DTOInterceptor.class)){
			return new DTO(result);
		}
      }
      
      return result;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getProxy(T t, Class<? super T> interfaceType) {
      ModelInterceptor<T> handler = new ModelInterceptor<T>(t);
      return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class<?>[]{interfaceType}, handler);
  }
}