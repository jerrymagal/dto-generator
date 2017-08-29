package org.crow.dtogenerator.scanner;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.crow.dtogenerator.annotation.DTO;
import org.crow.dtogenerator.annotation.DtoProperty;
import org.crow.dtogenerator.model.Property;
import org.crow.dtogenerator.scanner.util.Decompiler;
import org.crow.dtogenerator.scanner.util.ScannerFile;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ClassFilter;
import net.sf.corn.cps.PackageNameFilter;

public class ScannerDTOAnnotation {
	
	private static final Logger LOGGER = Logger.getLogger(ScannerDTOAnnotation.class);
	
	private Scanner scanner;
	
	public void execute(String deployDir, String packageClass) {
		
		this.scanner = new Scanner(deployDir);
		
		LOGGER.info("Init DTO Scanner");
		
		List<Class<?>> scanClasses = CPScanner.scanClasses(new PackageNameFilter(packageClass),
							  new ClassFilter().appendAnnotation(DTO.class));
		
		if(scanClasses == null || scanClasses.isEmpty()) {
			LOGGER.info("DTO Annotation Not Found");
			return;
		}
		
		for (Class<?> classe : scanClasses) {
			this.scanner.setDtoAnnotation(classe.getAnnotation(DTO.class));
			createDtoClass(classe);
		}
	}

	private void createDtoClass(Class<?> classe) {
		
		initScannerProperties(classe);
		boolean stopScan = !this.scanner.getDtoAnnotation().toRebuild() && ScannerFile.existsFile(this.scanner);
		
		if(stopScan) {
			return;
		}

		File file = new File(this.scanner.getPackageDeploy());
		buildClass(file, classe);
		Decompiler.decompile(file, this.scanner);
		ScannerFile.addPackage(file, this.scanner);
	}
	
	private void initScannerProperties(Class<?> classe) {
		this.scanner.setDtoName(classe.getSimpleName());
		this.scanner.setPackageName(getPackage(classe));
	}

	private void buildClass(File file, Class<?> classe) {
		
		ClassPool dtoClassGenerator = ClassPool.getDefault();
		
		CtClass dtoClass = dtoClassGenerator.makeClass(this.scanner.getDtoName());
		
		try {
			
			for(Field field : classe.getDeclaredFields()) {
				
				DtoProperty annotation = field.getAnnotation(DtoProperty.class);
				
				if(annotation != null) {
					
					CtClass typeField = null;
					
					Class<?> type = annotation.type();
					
					if(type.newInstance() instanceof Property) {
						typeField = dtoClassGenerator.get(field.getType().getName());
					} else {
						typeField = dtoClassGenerator.get(type.getName());
					}
					
					CtField dtoField = new CtField(typeField, field.getName(), dtoClass);
					dtoField.setModifiers(Modifier.PRIVATE);
					dtoClass.addField(dtoField);
					
					CtMethod getter = CtNewMethod.getter("get" + StringUtils.capitalize(field.getName()), dtoField);
					dtoClass.addMethod(getter);
					
					CtMethod setter = CtNewMethod.setter("set" + StringUtils.capitalize(field.getName()), dtoField);
					dtoClass.addMethod(setter);
				}
				
			}
			
			dtoClass.writeFile(file.getAbsolutePath());
			LOGGER.info("Write DTO File in" + file.getAbsolutePath());
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private String getPackage(Class<?> classe) {
		String packageDto = scanner.getDtoAnnotation().packageName();
		return packageDto.isEmpty() ? classe.getPackage().getName() : packageDto;
	}
}
