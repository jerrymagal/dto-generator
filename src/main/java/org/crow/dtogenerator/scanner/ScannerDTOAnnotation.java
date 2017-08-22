package org.crow.dtogenerator.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.crow.dtogenerator.annotation.DTO;
import org.crow.dtogenerator.annotation.DtoProperty;
import org.crow.dtogenerator.model.Property;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import jd.common.loader.DirectoryLoader;
import jd.common.preferences.CommonPreferences;
import jd.common.printer.text.PlainTextPrinter;
import jd.core.loader.LoaderException;
import jd.core.process.DecompilerImpl;

public class ScannerDTOAnnotation {
	
	private static final String CLASS = ".class";
	private static final String JAVA = ".java";

	private String dtoName;
	private DTO dtoAnnotation;
	private String deployDir;
	private String dtoClassName;
	private String dtoJavaName;
	private String packageName;
	
	public static void main(String[] args) {
		new ScannerDTOAnnotation().findAnnotatedClasses("org.crow.dtogenerator");
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
		
			Class<?> classe = null;
			
			try {
				classe = Class.forName(definition.getBeanClassName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			this.dtoAnnotation = classe.getAnnotation(DTO.class);
			
			packageName = getPackage(classe);
			initProperties(classe.getSimpleName());
			
			if(!dtoAnnotation.toRebuild() && existsFile()) {
				return;
			}

			File file = new File(deployDir);
			buildClass(file, classe);
			decompilerClass(file);
			addPackage(file);
	}
	
	private void initProperties(String className) {
		final String property = System.getProperty("user.dir");
		final String source = "src\\main\\java";
		this.deployDir = property+"\\"+source+"\\"+packageName.replace(".", "\\");
		this.dtoName = className + "DTO";
		this.dtoClassName = dtoName + CLASS;
		this.dtoJavaName = dtoName + JAVA;
	}

	private boolean existsFile() {
		File file = FileUtils.getFile(this.deployDir + "\\" + dtoJavaName);
		return file.exists();
	}
	
	private void buildClass(File file, Class<?> classe) {
		
		ClassPool dtoClassGenerator = ClassPool.getDefault();
		
		CtClass dtoClass = dtoClassGenerator.makeClass(dtoName);
		
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
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void decompilerClass(File file) {
		DirectoryLoader loader;
		try {
			DecompilerImpl decompilerImpl = new DecompilerImpl();
			CommonPreferences commonPreferences = new CommonPreferences();
			loader = new DirectoryLoader(file);
			PrintStream printStream = new PrintStream(deployDir + "\\" + dtoJavaName);
			PlainTextPrinter plainTextPrinter = new PlainTextPrinter(commonPreferences, printStream);
			
			decompilerImpl.decompile(commonPreferences, loader, plainTextPrinter, dtoClassName);
		} catch (LoaderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void addPackage(File file) {
		try {
			FileUtils.getFile(file, dtoClassName).delete();
			File javaFile = FileUtils.getFile(file, dtoJavaName);
			List<String> readLines = FileUtils.readLines(javaFile, Charset.defaultCharset());
			readLines.add(0, "package " + packageName + "\n");
			FileUtils.writeLines(javaFile, readLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getPackage(Class<?> classe) {
		String packageDto = dtoAnnotation.packageName();
		return packageDto.isEmpty() ? classe.getPackage().getName() : packageDto;
	}
}
