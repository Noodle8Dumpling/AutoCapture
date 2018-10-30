package capture.capt;

import capture.control.ControlApp;
import capture.entity.appInfo;
import io.appium.java_client.android.AndroidDriver;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.tcpip.Http;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 20171221
 *
 * 修改日期： 20180126
 *
 * 修改人： 郝京
 *
 * 修改内容： 注释过滤条件
 */
public class CapturePkg {
    AndroidDriver driver;
    appInfo appInfo;
    String packageInfo;

    public boolean flag = false;
    public int count = 0;

    public CapturePkg(appInfo appInfo, AndroidDriver driver) {
        this.appInfo = appInfo;
        this.driver = driver;
    }

    public void capture() {

        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with
        // NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        Pcap pcap;

        Thread control = new ControlApp(appInfo, driver);
        control.start();


        //final Ip4 ip = new Ip4();
        final Http ht = new Http();
        /***************************************************************************
         * First get a list of devices on this system
         **************************************************************************/
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can’t read list of devices, error is %s", errbuf.toString());
            return;
        }

        System.out.println("Network devices found:");

        //选择无线网卡设备
        PcapIf device = alldevs.get(1); // We know we have atleast 1 device
        System.out.printf("\nChoosing ‘%s’ on your behalf:\n",
                (device.getDescription() != null) ? device.getDescription() : device.getName());

        /***************************************************************************
         * Second we open up the selected device
         **************************************************************************/
        int snaplen = 64 * 1024; // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000; // 10 seconds in millis
        pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening device for capture:" + errbuf.toString());
            return;
        }

		/*
         * Compiling and appplying a filter to network interface
		 */
        PcapBpfProgram filter = new PcapBpfProgram();
        System.out.println(filter.toString());
        //过滤包
        String expression = "tcp port http";
        int optimize = 0;
        int netmask = 0;
        int m = pcap.compile(filter, expression, optimize, netmask);
        if (m != Pcap.OK) {
            System.out.println("Filter error: " + pcap.getErr());
        }
        pcap.setFilter(filter);


        /***************************************************************************
         * Third we create a packet handler which will receive packets from the
         * libpcap loop.
         **************************************************************************/

        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

            public void nextPacket(PcapPacket packet, String user) {
                if ("TERMINATED".equals(control.getState().toString())) {
                    pcap.breakloop();
                }

                /**
                 * 过滤手机系统噪音
                 */
                if (packet.hasHeader(ht) &&
                        packet.toString().contains("Http:") &&
                        packet.toString().contains("HOST = ") &&
                        !packet.toString().contains("xiaomi.com") &&
                        !packet.toString().contains("miui.com") &&
                        !packet.toString().contains("xiaomi.net") &&
                        !packet.toString().contains("http://wifi.shouji.360.cn") &&
                        !packet.toString().contains("NOCONNECTION") &&
                        !packet.toString().contains("umeng")
                        ) {
                    //System.out.printf("数据包头%s\n", packet.getCaptureHeader().size());
                    packageInfo = packageInfo + packet.toString();

                    BufferedReader br = null;
                    FileOutputStream fs = null;
                    PrintStream p1 = null;
                    InputStreamReader ir = null;
                    try {
                        ir = new InputStreamReader(new ByteArrayInputStream(packageInfo.getBytes()));
                        //读取执行命令后的输出结果
                        br = new BufferedReader(ir);
                        String line = null;
                        fs = new FileOutputStream(new File(appInfo.getPkg_outPath()
                                + appInfo.getAppName()
                                + new SimpleDateFormat("yyyyMMddHH")
                                .format(new Date()) + ".txt"));
                        p1 = new PrintStream(fs);
                        while ((line = br.readLine()) != null) {
                            p1.println(new String(line.getBytes("GBK"), "UTF-8"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                ir.close();
                                br.close();
                                p1.close();
                                fs.close();
                            } catch (Exception e) {
                                System.out.println("输出文件时出现异常！");
                                e.printStackTrace();
                            }
                        }
                    }
                    count++;
                }
            }
        };

        /***************************************************************************
         * Fourth we enter the loop and tell it to capture 10 packets. The loop
         * method does a mapping of pcap.datalink() DLT value to JProtocol ID,
         * which is needed by JScanner. The scanner scans the packet buffer and
         * decodes the headers. The mapping is done automatically, although a
         * variation on the loop method exists that allows the programmer to
         * sepecify exactly which protocol ID to use as the data link type for
         * this pcap interface. -2表示可在循环体内设标志跳出循环 -1表示发生异常时自动跳出循环体
         * 0表示一直循环 >0表示循环次数
         **************************************************************************/

        pcap.loop(-2, jpacketHandler, "jNetPcap rocks!");


        System.out.println("本次抓包结束。");
        /***************************************************************************
         * Last thing to do is close the pcap handle
         **************************************************************************/
        pcap.close();


    }
}
