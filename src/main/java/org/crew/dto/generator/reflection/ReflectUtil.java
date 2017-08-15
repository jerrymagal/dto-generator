package org.crew.dto.generator.reflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.crew.dto.generator.annotation.DTOProperty;

public abstract class ReflectUtil {

	/*
	public void listenToEvent(MediatorEvent event) {
		if (isChildren(event.getMBean())) {
			if (event instanceof TelaMenuItemCreatedEvent) {
				setAttributesOnChildren(event.getMBean());
			} else if (event instanceof TelaMenuItemDestroyedEvent) {
				setAttributesOnParent(event.getMBean());
			}
		}
	}

	protected boolean isChildren(TelaMenuItemMBean mbean) {
		for (Class<?> clazz : getChildrenClasses()) {
			if (mbean.getClass().equals(clazz)) {
				return true;
			}
		}
	  
	  return false; }
	 */

	protected static Map<String, Field> montaMapaCamposAnotados(Class<?> classe) {
		Map<String, Field> mapa = new HashMap<String, Field>();

		Class<?> clazz = classe;
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(DTOProperty.class)) {
					DTOProperty att = field.getAnnotation(DTOProperty.class);
					mapa.put(att.name(), field);
				}
			}

			clazz = clazz.getSuperclass();
		}

		return mapa;
	}

	protected static void setAttributesOnChildren(Object clazz, Object dto) {
		Map<String, Field> mapaPai = montaMapaCamposAnotados(clazz.getClass());
		Map<String, Field> mapaFilho = montaMapaCamposAnotados(dto.getClass());

		for (String key : mapaPai.keySet()) {
			if (mapaFilho.containsKey(key)) {
				Field fieldPai = mapaPai.get(key);
				Field fieldFilho = mapaFilho.get(key);

				setField(clazz, dto, fieldFilho, fieldPai);
			}
		}
	}

	protected static void setField(Object value, Object target, Field fieldTarget, Field fieldValue) {
		DTOProperty attValue = fieldValue.getAnnotation(DTOProperty.class);
		DTOProperty attTarget = fieldTarget.getAnnotation(DTOProperty.class);
		
		if (!attTarget.readOnly()) {
			fieldTarget.setAccessible(true);
			fieldValue.setAccessible(true);

			try {
				Object valor = fieldValue.get(value);
				
				
				String name = attValue.name();
				Field field;
				try {
					Class<?> clazz = attTarget.getClass();
					Field[] fields = clazz.getDeclaredFields();
					field = clazz.getDeclaredField(name);
					

					String property = attValue.property();
					Object valor2 = field.get(property);
					
					//Object cast = convertInstanceOfObject(valor, fieldTarget.getType());
					
					fieldTarget.set(target, valor2);
					
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} 
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
	    try {
	        return clazz.cast(o);
	    } catch(ClassCastException e) {
	        return null;
	    }
	}
	
	protected void setAttributesOnParent(Object clazz, Object dto) {
		Map<String, Field> mapaFilho = montaMapaCamposAnotados(clazz.getClass());
		Map<String, Field> mapaPai = montaMapaCamposAnotados(dto.getClass());

		for (String key : mapaPai.keySet()) {
			if (mapaFilho.containsKey(key)) {
				Field fieldPai = mapaPai.get(key);
				Field fieldFilho = mapaFilho.get(key);

				setField(clazz, dto, fieldPai, fieldFilho);
			}
		}
	}

	public static void buildDTO(Object model, Object dto) {
		/*Map<String, Field> montaMapaCamposAnotados = montaMapaCamposAnotados(model.getClass());
		
		for (Entry<String, Field> entry : montaMapaCamposAnotados.entrySet()) {
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		}*/
		
		setAttributesOnChildren(model, dto);
	}

}
