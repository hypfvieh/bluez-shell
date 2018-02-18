package com.github.hypfvieh.commands.device;

import java.io.InterruptedIOException;
import java.util.Arrays;
import java.util.List;

import org.jline.terminal.Terminal;

import com.github.hypfvieh.bluetooth.DeviceManager;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothDevice;
import com.github.hypfvieh.commands.base.AbstractCommand;
import com.github.hypfvieh.commands.base.CommandArg;
import com.github.hypfvieh.shell.ShellFormatter;
import com.github.hypfvieh.util.TypeUtil;

public class ScanDevices extends AbstractCommand {

    @Override
    public String[] execute(List<String> _arguments, Terminal _terminal) throws InterruptedIOException {
        ShellFormatter sf = new ShellFormatter(_terminal);
        if (_arguments == null || _arguments.isEmpty()) {
            return printError(sf, "You have to specify a scan timeout (in seconds)");
        }
        String strArg = _arguments.get(0);
        if (!TypeUtil.isInteger(strArg, false)) {
            return printError(sf, "Given argument '" + strArg + "' is not of type integer");
        }
        
        int delay = Integer.parseInt(strArg);
        DeviceManager.getInstance().scanForBluetoothDevices(delay);
        List<BluetoothDevice> devices = DeviceManager.getInstance().getDevices();
        int foundDevices = devices.size();
        
        List<String> listDevices = ShowDevices.listDevices(sf, devices, false);
        listDevices.add("");
        listDevices.addAll(Arrays.asList(printSuccess(sf, "Scan finished, "+ foundDevices + " device(s) found.")));
        
        return listDevices.toArray(new String[0]);
    }

    @Override
    public String getCommandName() {
        return "scanDevices";
    }

    @Override
    public String getDescription() {
        return "Scans for bluetooth devices nearby for the given amount of seconds";
    }

    @Override
    public List<CommandArg> getCommandArgs() {
        return Arrays.asList(new CommandArg("intervalInSeconds", false));
    }

    @Override
    public String getCmdGroup() {
        return "Device Actions";
    }

    
}
