package org.crew.dto.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import jd.common.loader.DirectoryLoader;
import jd.common.preferences.CommonPreferences;
import jd.common.printer.text.PlainTextPrinter;
import jd.core.loader.LoaderException;
import jd.core.process.DecompilerImpl;

public class Teste {

	/**
	 * Cria jm .class, em seguida decompila em um arquivo .java
	 * @param args
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 * @throws IOException
	 * @throws LoaderException
	 */
	public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, LoaderException {
		// TODO Auto-generated method stub
		String jbosstestDeployDir = "D:\\workspace_pessoal\\dto-generator\\src\\main\\java\\org\\crew\\dto\\generator";
		System.out.println("jbosstestDeployDir = " + jbosstestDeployDir);
		File libDir = new File(jbosstestDeployDir);
		System.out.println("libDir = " + libDir.getAbsolutePath());
		// Create a SimpleResponseDTO class with a static serialVersionUID of 1L
		// ClassPool defaultPool = ClassPool.getDefault();
		ClassPool classes1Pool = ClassPool.getDefault();
		// ClassPool classes1Pool = new ClassPool(defaultPool);
		CtClass info = classes1Pool.makeClass("org.jboss.test.scoped.interfaces.dto.SimpleResponseDTO");
		info.addInterface(classes1Pool.get("java.io.Serializable"));
		CtClass s = classes1Pool.get("java.lang.String");
		CtField firstName = new CtField(s, "firstName", info);
		firstName.setModifiers(Modifier.PRIVATE);
		info.addField(firstName);
		CtMethod getFirstName = CtNewMethod.getter("getFirstName", firstName);
		getFirstName.setModifiers(Modifier.PUBLIC);
		info.addMethod(getFirstName);
		CtMethod setFirstName = CtNewMethod.setter("setFirstName", firstName);
		setFirstName.setModifiers(Modifier.PUBLIC);
		info.addMethod(setFirstName);
		CtClass s2 = classes1Pool.get("java.lang.String");
		CtField lastName = new CtField(s2, "lastName", info);
		lastName.setModifiers(Modifier.PRIVATE);
		info.addField(lastName);
		CtMethod getLastName = CtNewMethod.getter("getLastName", lastName);
		getLastName.setModifiers(Modifier.PUBLIC);
		info.addMethod(getLastName);
		CtMethod setLastName = CtNewMethod.setter("setLastName", lastName);
		setLastName.setModifiers(Modifier.PUBLIC);
		info.addMethod(setLastName);
		// CtClass s3 = classes1Pool.get("java.lang.Long");
		// CtField serialVersion = new CtField(s3, "serialVersionUID", info);
		CtField serialVersion = new CtField(CtClass.longType, "serialVersionUID", info);
		serialVersion.setModifiers(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL);
		long serialVerionUID = 2L;
		info.addField(serialVersion, CtField.Initializer.constant(serialVerionUID));

		info.writeFile(libDir.getAbsolutePath());
		
		
		String replace = info.getName().replace(".", "\\");
		System.out.println(replace);
		
		DecompilerImpl decompilerImpl = new DecompilerImpl();
		CommonPreferences commonPreferences = new CommonPreferences();
		DirectoryLoader loader = new DirectoryLoader(new File(libDir.getAbsolutePath()));
		
		PrintStream printStream = new PrintStream("D:\\test.java");
		PlainTextPrinter plainTextPrinter = new PlainTextPrinter(commonPreferences, printStream);
		
		decompilerImpl.decompile(commonPreferences, loader, plainTextPrinter, replace+".class");
			
	}

}
