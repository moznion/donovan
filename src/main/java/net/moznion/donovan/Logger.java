package net.moznion.donovan;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
class Logger {
  static void logException(Throwable e) {
    StringWriter exString = new StringWriter();
    e.printStackTrace(new PrintWriter(exString));
    log.error(exString.toString());
  }
}
