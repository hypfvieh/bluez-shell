package com.github.hypfvieh.commands.device;

import static org.jline.utils.AttributedStyle.BLUE;
import static org.jline.utils.AttributedStyle.DEFAULT;
import static org.jline.utils.AttributedStyle.YELLOW;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.freedesktop.dbus.UInt16;
import org.jline.terminal.Terminal;

import com.github.hypfvieh.bluetooth.DeviceManager;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothDevice;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattService;
import com.github.hypfvieh.commands.base.AbstractCommand;
import com.github.hypfvieh.commands.base.CommandArg;
import com.github.hypfvieh.commands.base.ICommand;
import com.github.hypfvieh.helper.UuidResolver;
import com.github.hypfvieh.shell.ShellFormatter;

public class ShowDevices extends AbstractCommand {

    @Override
    public String[] execute(List<String> _arguments, Terminal _terminal) throws InterruptedIOException {
        
        List<String> results = new ArrayList<>();
        ShellFormatter sf = new ShellFormatter(_terminal);
        
        List<BluetoothDevice> devices;
        boolean detailed = false;
        if (_arguments != null && !_arguments.isEmpty()) {
            String arg = _arguments.get(0);
            detailed = true;
            devices = DeviceManager.getInstance().getDevices().stream().filter(d -> d.getAddress().equals(arg) || d.getName().equals(arg)).collect(Collectors.toList());
        } else {
            devices = DeviceManager.getInstance().getDevices();
        }
        
        results = listDevices(sf, devices, detailed);
        
        return results.toArray(new String[0]);
    }

    static List<String> listDevices(ShellFormatter _sf, List<BluetoothDevice> _devices, boolean _detailed) {
        List<String> results = new ArrayList<>();
        
        if (_devices == null || _sf == null) {
            return null;
        }
        
        for (BluetoothDevice dev : _devices) {
            
            results.add("Name: " + _sf.printInColor(dev.getName(), DEFAULT.foreground(YELLOW)));
            results.add("Alias: " + dev.getAddress());
            results.add("MAC address: " + _sf.printInColor("[", DEFAULT.foreground(BLUE)) 
                                           + _sf.printInColor(dev.getAddress(), DEFAULT.foreground(YELLOW))
                                           + _sf.printInColor("]", DEFAULT.foreground(BLUE)));
            results.add("");
            results.add("DBUS path: " + dev.getDbusPath());
            results.add("ModAlias: " + dev.getModAlias());
            results.add("BluetoothClass: " + dev.getBluetoothClass());
            results.add("Appearance: " + dev.getAppearance());
            results.add("");
            
            results.add("RSSI: " + dev.getRssi());
            results.add("TX Power: " + dev.getTxPower());
            results.add("");
            results.add("Connected: " + dev.isConnected());
            results.add("Blocked: " + dev.isBlocked());
            results.add("Paired: " + dev.isPaired());
            results.add("LegacyPairing: " + dev.isLegacyPairing());
            results.add("Trusted: " + dev.isTrusted());
            results.add("ServiceResolved: " + dev.isServiceResolved());
            
            if (_detailed) {
                results.add("");
                results.add("    UUIDS:");
                results.add("    " + StringUtils.repeat("-", ICommand.DEFAULT_SHELL_WIDTH -4));
                
                for (String uuid : dev.getUuids()) {
                    String nameFromUuid = UuidResolver.getInstance().getNameFromUuid(uuid);
                    String line = null;
                    if (StringUtils.isBlank(nameFromUuid)) {
                        line = "    " + uuid;                    
                    } else {
                        line = "    " 
                                + uuid 
                                + "   "
                                +  _sf.printInColor("[", DEFAULT.foreground(BLUE)) 
                                + _sf.printInColor(nameFromUuid, DEFAULT.foreground(YELLOW)) 
                                + _sf.printInColor("]", DEFAULT.foreground(BLUE));
                    }
                    results.add(line);
                }
                
                results.add("");
                results.add("    Manufacturer Data:");
                results.add("    " + StringUtils.repeat("-", ICommand.DEFAULT_SHELL_WIDTH -4));
                
                Map<UInt16, byte[]> manufacturerData = dev.getManufacturerData();
                for (Entry<UInt16, byte[]> entry : manufacturerData.entrySet()) {
                    results.add("    " + entry.getKey() + "  ->  " + entry.getValue());
                }
                results.add("");
                
                Map<String, byte[]> serviceData = dev.getServiceData();
                results.add("    Service Data:");
                results.add("    " + StringUtils.repeat("-", ICommand.DEFAULT_SHELL_WIDTH -4));
                
                for (Entry<String, byte[]> entry : serviceData.entrySet()) {
                    results.add("    " + entry.getKey() + "  ->  " + entry.getValue());
                }
                results.add("");

                results.add("");
                results.add("GATT Services:");
                results.add("    " + StringUtils.repeat("-", ICommand.DEFAULT_SHELL_WIDTH -4));

                List<BluetoothGattService> gattServices = dev.getGattServices();
                for (BluetoothGattService gatt : gattServices) {
                    results.add("   " + gatt.getUuid());
                }
            }
            
        }
        
        return results;
    }

    @Override
    public String getCommandName() {
        return "showDevices";
    }

    @Override
    public String getDescription() {
        return "Show details for all found devices, or only for the matching device(s) (MAC address or name/alias)";
    }

    @Override
    public List<CommandArg> getCommandArgs() {
        return Arrays.asList(new CommandArg("MacOrAliasName", true));
    }

    @Override
    public String getCmdGroup() {
        return "Device Actions";
    }
}
