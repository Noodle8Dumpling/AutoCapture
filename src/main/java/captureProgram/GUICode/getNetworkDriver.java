package captureProgram.GUICode;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import java.util.ArrayList;
import java.util.List;

public class getNetworkDriver {
    public void getComboBoxValue() {
        String comboBoxValue = "";
        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with
        // NICs
        StringBuilder errbuf = new StringBuilder();
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            //CaptureGUI.netWorkComboValue = null;
            CaptureGUI.netWorkComboValue = null;
        }

        int i = 0;
        for (PcapIf device : alldevs) {
            String description = (device.getDescription() != null) ? device.getDescription()
                    : "No description available";
            //System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
            //System.out.println(device.getName().substring(device.getName().indexOf("{")) + "|||" + description);
            comboBoxValue = comboBoxValue + "["
                    + description + "]" + device.getName().
                    substring(device.getName().indexOf("{")) + ";";

        }

        if (comboBoxValue.contains(";")) {
            if (comboBoxValue.split(";").length >= 2){
                comboBoxValue = comboBoxValue.substring(0, comboBoxValue.length() - 1);
                //CaptureGUI.netWorkComboValue = comboBoxValue.split(";");
                CaptureGUI.netWorkComboValue = comboBoxValue.split(";");
            }else{
                //CaptureGUI.netWorkComboValue = new String[1];
                //CaptureGUI.netWorkComboValue[0] = comboBoxValue.substring(0, comboBoxValue.length() - 1);
                CaptureGUI.netWorkComboValue = new String[1];
                CaptureGUI.netWorkComboValue[0] = comboBoxValue.substring(0, comboBoxValue.length() - 1);
            }

        }else {
            //CaptureGUI.netWorkComboValue = new String[1];
            //CaptureGUI.netWorkComboValue[0] = "<无>";
            CaptureGUI.netWorkComboValue = new String[1];
            CaptureGUI.netWorkComboValue[0] = "<无>";
        }

    }

}
