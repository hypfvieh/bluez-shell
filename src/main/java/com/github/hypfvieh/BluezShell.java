package com.github.hypfvieh;

import org.jline.reader.EndOfFileException;
import org.slf4j.LoggerFactory;

import com.github.hypfvieh.shell.EmbeddedShell;

public class BluezShell {

    public static void main(String[] _args) {
        LoggerFactory.getLogger(BluezShell.class).debug("Initializing Shell");

        try(EmbeddedShell shell = new EmbeddedShell(System.in, System.out, System.err))  {
            shell.start("bluezShell > ");
        } catch (Exception _ex) {
            if (! (_ex instanceof EndOfFileException)) { // EndOfFileException will occur when using CTRL+D to exit shell
                System.err.println("Error: " + _ex.getMessage());
            }            
        } finally {
            LoggerFactory.getLogger(BluezShell.class).debug("Deinitializing Shell");
        }

    }
}
