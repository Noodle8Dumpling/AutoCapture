package optimizationprogram.mainPro;


import optimizationprogram.GUICode.CaptureGUI;
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
 * 创建人： 郝京
 * <p>
 * 创建日期：2018-03-17
 * <p>
 * 文件描述：抓包
 */
public class CapturePkgs {
    AndroidDriver driver;
    SelectedAppInfo sai;
    GUIInfo gi;
    PkgInfo bean;
    String packageInfo = "";
    String urlStr;
    String appName;
    int count = 0;
    File file;


    public CapturePkgs(AndroidDriver driver, SelectedAppInfo sai, GUIInfo gi) {
        this.driver = driver;
        this.sai = sai;
        this.gi = gi;
    }

    public PkgInfo processData(PkgInfo bean) {
        if (bean.getOccurTime() == null) {
            bean.setOccurTime("");
        }
        if (bean.getIp_version() == null) {
            bean.setIp_version("");
        }
        if (bean.getIp_source() == null) {
            bean.setIp_source("");
        }
        if (bean.getIp_destination() == null) {
            bean.setIp_destination("");
        }
        if (bean.getTcp_source() == null) {
            bean.setTcp_source("");
        }
        if (bean.getTcp_destination() == null) {
            bean.setTcp_destination("");
        }
        if (bean.getHttp_RequestMethod() == null) {
            bean.setHttp_RequestMethod("");
        }
        if (bean.getHttp_RequestUrl() == null) {
            bean.setHttp_RequestUrl("");
        }
        if (bean.getHttp_RequestVersion() == null) {
            bean.setHttp_RequestVersion("");
        }
        if (bean.getHttp_HOST() == null) {
            bean.setHttp_HOST("");
        }
        if (bean.getHttp_USERAGENT() == null) {
            bean.setHttp_USERAGENT("");
        }
        if (bean.getHttp_CONTENTTYPE() == null) {
            bean.setHttp_CONTENTTYPE("");
        }
        if (bean.getHttp_CONTENTLENGTH() == null) {
            bean.setHttp_CONTENTLENGTH("");
        }
        if (bean.getHttp_CONNECTION() == null) {
            bean.setHttp_CONNECTION("");
        }
        if (bean.getHttp_CACHECONTROL() == null) {
            bean.setHttp_CACHECONTROL("");
        }
        if (bean.getHttp_COOKIE() == null) {
            bean.setHttp_COOKIE("");
        }
        if (bean.getHttp_ACCEPTLANGUAGE() == null) {
            bean.setHttp_ACCEPTLANGUAGE("");
        }
        if (bean.getHttp_ACCEPTENCODING() == null) {
            bean.setHttp_ACCEPTENCODING("");
        }
        if (bean.getHttp_ACCEPT() == null) {
            bean.setHttp_ACCEPT("");
        }
        return bean;
    }


    public void capture() {

        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with
        // NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        Pcap pcap;

        String tmpStr;


        Thread control = new StartUpApp(driver, sai, gi);
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
        PcapIf device = alldevs.get(Integer.valueOf(gi.getNetworkName())); // We know we have atleast 1 device
        System.out.printf("\nChoosing ‘%s’ on your behalf:\n",
                (device.getDescription() != null) ? device.getDescription() + device.getName() : device.getName());

        /***************************************************************************
         * Second we open up the selected device
         **************************************************************************/
        int snaplen = 64 * 1024; // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 15 * 1000; // 10 seconds in millis
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

        CaptureGUI.textArea_log.append("INFO:开始抓包···\n");
        /***************************************************************************
         * Third we create a packet handler which will receive packets from the
         * libpcap loop.
         **************************************************************************/

        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

            public void nextPacket(PcapPacket packet, String user) {

                if ("TERMINATED".equals(control.getState().toString()) || !CaptureGUI.stopFlag) {
                    pcap.breakloop();
                }

                /**
                 * 过滤手机系统噪音
                 */
                if (packet.hasHeader(ht) &&
                        packet.toString().contains("Http:") &&
                        packet.toString().contains("HOST = ") &&
                        !packet.toString().contains("http://wifi.shouji.360.cn") &&
                        !packet.toString().contains("NOCONNECTION") &&
                        !packet.toString().contains("umeng")
                        ) {
                    bean = new PkgInfo();
                    appName = sai.getAppName().trim();

                    count++;

                    CaptureGUI.capturedTotalPkgs = CaptureGUI.capturedTotalPkgs + 1l;
                    packageInfo = packageInfo + packet.toString();

                    BufferedReader br = null;
                    //FileOutputStream fs = null;
                    FileOutputStream fs2 = null;
                    //PrintStream p1 = null;
                    InputStreamReader ir = null;
                    BufferedWriter write = null;
                    OutputStreamWriter writer = null;

                    try {

                        ir = new InputStreamReader(new ByteArrayInputStream(packageInfo.getBytes()));
                        //读取执行命令后的输出结果
                        br = new BufferedReader(ir);
                        String line = null;
                        file = new File(gi.getDataDestination()
                                + "\\app数据包");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        /*fs = new FileOutputStream(new File(gi.getDataDestination()
                                + "\\app数据包\\" + appName
                                + new SimpleDateFormat("yyyyMMddHH")
                                .format(new Date()) + ".txt"),true);

                        p1 = new PrintStream(fs);*/
                        fs2 = new FileOutputStream(new File(gi.getDataDestination()
                                + "\\app数据包\\" + appName
                                + new SimpleDateFormat("yyyyMMddHH")
                                .format(new Date()) + ".txt"), true);
                        //p2 = new PrintStream(fs2);

                        writer = new OutputStreamWriter(fs2, "utf-8");
                        write = new BufferedWriter(writer);
                        while ((line = br.readLine()) != null) {

                            if (line.contains("Frame:") && line.contains("timestamp =")) {
                                bean.setOccurTime(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("Ip:") && line.contains("version =")) {
                                bean.setIp_version(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("Ip:") && line.contains("source =")) {
                                bean.setIp_source(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("Ip:") && line.contains("destination =")) {
                                bean.setIp_destination(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("Tcp:") && line.contains("source =")) {
                                bean.setTcp_source(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("Tcp:") && line.contains("destination =")) {
                                bean.setTcp_destination(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("RequestMethod")) {
                                bean.setHttp_RequestMethod(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("RequestUrl")) {
                                urlStr = line.substring(line.indexOf("=") + 2, line.length());
                                if (urlStr.length() > 4000) {
                                    bean.setHttp_RequestUrl(urlStr.substring(0, 4000));
                                } else {
                                    bean.setHttp_RequestUrl(urlStr);
                                }
                            }
                            if (line.contains("HOST")) {
                                bean.setHttp_HOST(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("USER-AGENT")) {
                                bean.setHttp_USERAGENT(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("CONTENT-TYPE")) {
                                bean.setHttp_CONTENTTYPE(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("RequestVersion")) {
                                bean.setHttp_RequestVersion(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("CACHE-CONTROL")) {
                                bean.setHttp_CACHECONTROL(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("CONNECTION")) {
                                bean.setHttp_CONNECTION(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("ACCEPT-ENCODING")) {
                                bean.setHttp_ACCEPTENCODING(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("COOKIE")) {
                                bean.setHttp_COOKIE(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("ACCEPT =")) {
                                bean.setHttp_ACCEPT(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("ACCEPT-LANGUAGE")) {
                                bean.setHttp_ACCEPTLANGUAGE(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                            if (line.contains("CONTENT-LENGTH")) {
                                bean.setHttp_CONTENTLENGTH(line.substring(line.indexOf("=") + 2, line.length()));
                            }
                        }
                        bean = processData(bean);
                        if (bean != null) {
                            line = appName + "|"
                                    + bean.getOccurTime() + "|"
                                    + bean.getIp_version() + "|"
                                    + bean.getIp_source() + ":"
                                    + bean.getTcp_source().trim() + "|"
                                    + bean.getIp_destination() + ":"
                                    + bean.getTcp_destination().trim() + "|"
                                    + bean.getHttp_RequestMethod() + "|"
                                    + bean.getHttp_RequestUrl() + "|"
                                    + bean.getHttp_RequestVersion() + "|"
                                    + bean.getHttp_HOST() + "|"
                                    + bean.getHttp_USERAGENT() + "|"
                                    + bean.getHttp_CONTENTTYPE() + "|"
                                    + bean.getHttp_ACCEPT() + "|"
                                    + bean.getHttp_ACCEPTENCODING() + "|"
                                    + bean.getHttp_ACCEPTLANGUAGE() + "|"
                                    + bean.getHttp_CACHECONTROL() + "|"
                                    + bean.getHttp_CONNECTION() + "|"
                                    + bean.getHttp_CONTENTLENGTH() + "|"
                                    + bean.getHttp_COOKIE();
                            write.write(line + "\n");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                write.close();
                                writer.close();
                                ir.close();
                                br.close();

                                //p1.close();
                                //p2.close();
                                fs2.close();
                                //fs.close();
                            } catch (Exception e) {
                                System.out.println("输出文件时出现异常！");
                                e.printStackTrace();
                            }
                        }
                    }

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

        CaptureGUI.textArea_log.append("INFO:本次抓包结束，" + sai.getAppName() +
                "共抓取到" + count + "个数据包。\n");

        //System.out.println("本次抓包结束。");
        /***************************************************************************
         * Last thing to do is close the pcap handle
         **************************************************************************/
        pcap.close();


    }
}
