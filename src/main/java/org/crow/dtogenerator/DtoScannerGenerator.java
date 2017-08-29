package org.crow.dtogenerator;

import org.crow.dtogenerator.scanner.ScannerDTOAnnotation;

public final class DtoScannerGenerator {

	public static void main(String[] args) {
		
		String deployDir = args[0];
		String packageClass = args[1];
		
		new ScannerDTOAnnotation().execute(deployDir, packageClass);
	}
}
