package org.crew.dto.generator.scanner;

import java.lang.reflect.Field;

import org.crew.dto.generator.annotation.DTO;
import org.crew.dto.generator.annotation.DtoProperty;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * Classe que scanneia determinado pacote e encontra as classes anotadas com {@link org.crew.dto.generator.annotation.DTO} 
 * bem como os métodos anotados com {@link org.crew.dto.generator.annotation.DtoProperty} 
 * @author alexandreg.rolim
 */
public class ScannerDTOAnnotation {
	
	public static void main(String[] args) {
		new ScannerDTOAnnotation().findAnnotatedClasses("org.crew.dto.generator");
	}

	public void findAnnotatedClasses(String packageName) {
		ClassPathScanningCandidateComponentProvider provider = createComponentScanner();
		
		for(BeanDefinition definition : provider.findCandidateComponents(packageName)) {
			createDtoClass(definition);
		}
	}

	private ClassPathScanningCandidateComponentProvider createComponentScanner() {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(DTO.class));
		return provider;
	}

	private void createDtoClass(BeanDefinition definition) {
		
		try {
			
			Class<?> classe = Class.forName(definition.getBeanClassName());
			
			// TODO criar método para buscar o caminho absoluto do .java
			
			String packageName = getPackage(classe);
			
			System.out.println("Pacote: " + packageName);
			System.out.println("Classe: " + classe.getName());
			
			
			for(Field field : classe.getDeclaredFields()) {
				System.out.print("Atributo: " + field.getName() + " / " + field.getType().getSimpleName());
				
				DtoProperty annotation = field.getAnnotation(DtoProperty.class);
				
				if(annotation != null)
					System.out.println(" / Tem anotação :)");
				else
					System.out.println(" / Não tem anotação");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	private String getPackage(Class<?> classe) {
		DTO dtoAnnotation = classe.getAnnotation(DTO.class);
		String packageDto = dtoAnnotation.packageName();
		return packageDto.isEmpty() ? classe.getPackage().getName() : packageDto;
	}
}
