package org.crew.dto.generator.reflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.crew.dto.generator.annotation.DTOPropertyModel;
import org.crew.dto.generator.annotation.DTOPropertyTarget;

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
				if (field.isAnnotationPresent(DTOPropertyModel.class)) {
					DTOPropertyModel att = field.getAnnotation(DTOPropertyModel.class);
					mapa.put(att.name(), field);
				}
				/*
				if (field.isAnnotationPresent(DTOPropertyTarget.class)) {
					DTOPropertyTarget att = field.getAnnotation(DTOPropertyTarget.class);
					mapa.put(att.name(), field);
				}*/
			}

			clazz = clazz.getSuperclass();
		}

		return mapa;
	}

	protected static void setAttributesOnChildren(Object model, Object dto) {
		Map<String, Field> mapaModel = montaMapaCamposAnotados(model.getClass());
		//Map<String, Field> mapaDTO = montaMapaCamposAnotados(dto.getClass());

		for (String property : mapaModel.keySet()) {
			
			Field fieldDTO = getFieldDTO(dto, property);
			
			if (fieldDTO != null) {
				Field fieldModel = mapaModel.get(property);

				setField(model, dto, fieldDTO, fieldModel);
			}
		}
	}
	
	private static Field getFieldDTO(Object obj, String property){

		for (Field field : obj.getClass().getDeclaredFields()) {
		    field.setAccessible(true); // You might want to set modifier to public first.
		    
		    if (field.getName().equalsIgnoreCase(property)) {
		       return field; 
		    }
		}
		
		return null;
	}

	protected static void setField(Object model, Object dto, Field fieldDTO, Field fieldModel) {
		DTOPropertyModel attModel = fieldModel.getAnnotation(DTOPropertyModel.class);
		DTOPropertyTarget attTarget = fieldDTO.getAnnotation(DTOPropertyTarget.class);
		
		if (!attTarget.readOnly()) {
			
			fieldDTO.setAccessible(true);
			fieldModel.setAccessible(true);

			try {
				Object valor = fieldModel.get(model);
				
				if(valor != null) {
					Object finalValue = getValueAtribute(valor, attModel.property());
					fieldDTO.set(dto, finalValue);
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

	private static Object getValueAtribute(Object obj, String property) throws IllegalArgumentException, IllegalAccessException {

		for (Field field : obj.getClass().getDeclaredFields()) {
		    field.setAccessible(true); // You might want to set modifier to public first.
		    
		    if (field.getName().equalsIgnoreCase(property)) {
		       return field.get(obj); 
		    }
		}
		
		return null;
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