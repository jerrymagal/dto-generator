package org.crow.dtogenerator.scanner.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.crow.dtogenerator.scanner.Scanner;

public final class ScannerFile {
	
	public static void addPackage(File file, Scanner scanner) {
		try {
			FileUtils.getFile(file, scanner.getDtoClassName()).delete();
			File javaFile = FileUtils.getFile(file, scanner.getDtoJavaName());
			List<String> readLines = FileUtils.readLines(javaFile, Charset.defaultCharset());
			readLines.add(0, "package " + scanner.getPackageName() + "; \n");
			FileUtils.writeLines(javaFile, readLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean existsFile(Scanner scanner) {
		File file = FileUtils.getFile(scanner.getPackageDeploy() + "\\" + scanner.getDtoJavaName());
		return file.exists();
	}
}
