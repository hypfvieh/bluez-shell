package com.github.hypfvieh.commands.adapter;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jline.terminal.Terminal;
import static org.jline.utils.AttributedStyle.*;

import com.github.hypfvieh.bluetooth.DeviceManager;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothAdapter;
import com.github.hypfvieh.commands.base.AbstractCommand;
import com.github.hypfvieh.helper.UuidResolver;
import com.github.hypfvieh.shell.ShellFormatter;

public class ShowBluetoothAdapters extends AbstractCommand {

    @Override
    public String[] execute(List<String> _arguments, Terminal _terminal) throws InterruptedIOException {
        
        List<BluetoothAdapter> adapters = DeviceManager.getInstance().getAdapters();
        
        List<String> results = new ArrayList<>();
        ShellFormatter sf = new ShellFormatter(_terminal);
        
        results.add("");
        results.add(StringUtils.repeat("=", DEFAULT_SHELL_WIDTH));
        results.add("");
        
        for (BluetoothAdapter bluetoothAdapter : adapters) {
            
            results.add(sf.printInColor("DeviceName: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.getDeviceName());
            results.add(sf.printInColor("Name: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.getName());
            results.add(sf.printInColor("Alias: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.getAlias());
            results.add(sf.printInColor("Address: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.getAddress());
            results.add(sf.printInColor("DBusPath: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.getDbusPath());
            results.add("");
            results.add(sf.printInColor("DeviceClass: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.getDeviceClass());
            results.add(sf.printInColor("ModAlias: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.getModAlias());
            results.add("");
            results.add(sf.printInColor("DiscoverableTimeout: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.getDiscoverableTimeout());
            results.add(sf.printInColor("PairableTimeout: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.getPairableTimeout());

            results.add("");
            results.add(sf.printInColor("Pairable: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.isPairable());
            results.add(sf.printInColor("Powered: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.isPowered());
            results.add(sf.printInColor("Discoverable: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.isDiscoverable());
            results.add(sf.printInColor("Discovering: ", DEFAULT.foreground(YELLOW)) + bluetoothAdapter.isDiscovering());
            
            
            results.add("");
            results.add("UUIDs:");
            for (String uid : bluetoothAdapter.getUuids()) {
                String nameFromUuid = UuidResolver.getInstance().getNameFromUuid(uid);
                String line = null;
                if (StringUtils.isBlank(nameFromUuid)) {
                    line = "\t" + uid;                    
                } else {
                    line = "\t" + uid + "   "+  sf.printInColor("[", DEFAULT.foreground(BLUE)) + sf.printInColor(nameFromUuid, DEFAULT.foreground(YELLOW)) + sf.printInColor("]", DEFAULT.foreground(BLUE));
                }
                results.add(line);
            }
            results.add(StringUtils.repeat("-", DEFAULT_SHELL_WIDTH));            
        }
        results.add("");
        results.add("Found " + adapters.size() + " bluetooth adapter(s).");
        results.add("");
        return results.toArray(new String[0]);
    }

    @Override
    public String getCommandName() {
        return "showAdapters";
    }

    @Override
    public String getDescription() {
        return "Displays a list of all bluetooth adapters found in bluez";
    }

    @Override
    public String getCmdGroup() {
        return "Adapter Action";
    }

    
}

