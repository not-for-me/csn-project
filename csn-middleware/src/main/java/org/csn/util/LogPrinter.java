package org.csn.util;

import org.slf4j.Logger;

public class LogPrinter {

	public static void printErrorLog(Logger logger, String classType,
			String errorMsg) {
		logger.error("Exception Class: " + classType + "\nException Message: "
				+ errorMsg);
	}
}
