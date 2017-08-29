package org.crow.dtogenerator.scanner.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.crow.dtogenerator.scanner.Scanner;

import jd.common.loader.DirectoryLoader;
import jd.common.preferences.CommonPreferences;
import jd.common.printer.text.PlainTextPrinter;
import jd.core.loader.LoaderException;
import jd.core.process.DecompilerImpl;

public final class Decompiler {
	
	private static final Logger LOGGER = Logger.getLogger(Decompiler.class);
	
	public static void decompile(File file, Scanner scanner) {
		DirectoryLoader loader;
		try {
			
			DecompilerImpl decompilerImpl = new DecompilerImpl();
			CommonPreferences commonPreferences = new CommonPreferences();
			loader = new DirectoryLoader(file);
			PrintStream printStream = new PrintStream(scanner.getPackageDeploy() + "//" + scanner.getDtoJavaName());
			PlainTextPrinter plainTextPrinter = new PlainTextPrinter(commonPreferences, printStream);
			
			decompilerImpl.decompile(commonPreferences, loader, plainTextPrinter, scanner.getDtoClassName());
			
			LOGGER.info("Decompiler " + scanner.getDtoClassName() + " to " + scanner.getDtoJavaName());
		} catch (LoaderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
