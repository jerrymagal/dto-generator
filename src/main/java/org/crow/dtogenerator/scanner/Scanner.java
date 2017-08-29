package org.crow.dtogenerator.scanner;

import org.crow.dtogenerator.annotation.DTO;

public class Scanner {
	
	private static final String CLASS = ".class";
	private static final String JAVA = ".java";
	private static final String DTO_TYPE = "DTO";

	private String dtoName;
	private DTO dtoAnnotation;
	private String packageName;
	private String deployDir;

	public Scanner(String deployDir) {
		this.deployDir = deployDir;
	}

	public String getDtoName() {
		return this.dtoName;
	}

	public void setDtoName(String dtoName) {
		if(this.dtoAnnotation != null && !this.dtoAnnotation.name().isEmpty()) {
			this.dtoName = this.dtoAnnotation.name() + DTO_TYPE;
		} else {
			this.dtoName = dtoName + DTO_TYPE;
		}
	}

	public DTO getDtoAnnotation() {
		return dtoAnnotation;
	}

	public void setDtoAnnotation(DTO dtoAnnotation) {
		this.dtoAnnotation = dtoAnnotation;
	}

	public String getDtoClassName() {
		return this.dtoName + CLASS;
	}

	public String getDtoJavaName() {
		return this.dtoName + JAVA;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getDeployDir() {
		return deployDir;
	}
	
	public String getPackageDeploy() {
		return this.deployDir + "\\" + this.packageName.replace(".", "\\");
	}
}
