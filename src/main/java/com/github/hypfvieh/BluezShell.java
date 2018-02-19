package com.github.hypfvieh;

import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hypfvieh.bluetooth.DeviceManager;
import com.github.hypfvieh.commands.adapter.SelectAdapter;
import com.github.hypfvieh.commands.adapter.ShowBluetoothAdapters;
import com.github.hypfvieh.commands.device.ScanDevices;
import com.github.hypfvieh.commands.device.ShowDevices;
import com.github.hypfvieh.commands.init.ShellDeInitializeCommand;
import com.github.hypfvieh.commands.init.ShellInitializeCommand;
import com.github.hypfvieh.shell.EmbeddedShell;

public class BluezShell {

    
    public static void main(String[] _args) {
        
//        ClassLoader cl = ClassLoader.getSystemClassLoader();
//
//        URL[] urls = ((URLClassLoader)cl).getURLs();
//
//        String collect = Arrays.stream(urls).map(u -> u.getFile()).collect(Collectors.joining(":"));
//        System.out.println(collect);
        
        
        Logger logger = LoggerFactory.getLogger(BluezShell.class);
        logger.debug("Initializing Shell");
        
                
        
        try(EmbeddedShell shell = new EmbeddedShell(System.in, System.out, System.err))  {
            // initialize the shell
            shell.initialize(new ShellInitializeCommand(), new ShellDeInitializeCommand());
            // register our commands
            
            // adapter commands
            shell.registerCommand(new ShowBluetoothAdapters());
            shell.registerCommand(new SelectAdapter());
            
            // device commands
            shell.registerCommand(new ScanDevices());
            shell.registerCommand(new ShowDevices());
            
            // start shell
            shell.start("bluezShell > ");
        } catch (Exception _ex) {
            // EndOfFileException will occur when using CTRL+D to exit shell
            // UserInterruptException will occur when using CTRL+C
            if (! (_ex instanceof EndOfFileException) && !(_ex instanceof UserInterruptException)) { 
                System.err.println("Error: (" + _ex.getClass().getSimpleName() + "): " + _ex.getMessage());
            }            
        } finally {
            DeviceManager.getInstance().closeConnection();
            logger.debug("Deinitializing Shell");
        }

    }
}

