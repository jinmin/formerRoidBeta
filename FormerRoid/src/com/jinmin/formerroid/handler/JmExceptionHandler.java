package com.jinmin.formerroid.handler;

import java.io.PrintWriter;
import java.io.StringWriter;

public class JmExceptionHandler {

   public static String getStackTraceToString(Exception e) {
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String exceptionAsStrting = sw.toString();
      return exceptionAsStrting;
   }
}
