package com.wyfx.business.utils;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author johnson liu
 * @date 2020/1/20
 * @description 获取计算机的mac物理地址和网络信息
 */
public class NetworkUtil {
    /**
     * 获取计算机所有的Mac地址
     *
     * @return [30-B4-9E-E6-04-4B, 40-8D-5C-5D-32-61, 32-B4-9E-E6-04-4B, 00-15-5D-EF-31-0C, 4A-15-B1-7E-9A-5F]
     * @throws Exception
     */
    public static List<String> getMacList() throws Exception {
        java.util.Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        StringBuilder sb = new StringBuilder();
        ArrayList<String> tmpMacList = new ArrayList<>();
        while (en.hasMoreElements()) {
            NetworkInterface iface = en.nextElement();
            List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
            for (InterfaceAddress addr : addrs) {
                InetAddress ip = addr.getAddress();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                if (network == null) {
                    continue;
                }
                byte[] mac = network.getHardwareAddress();
                if (mac == null) {
                    continue;
                }
                sb.delete(0, sb.length());
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                tmpMacList.add(sb.toString());
            }
        }
        if (tmpMacList.size() <= 0) {
            return tmpMacList;
        }
        /***去重，别忘了同一个网卡的ipv4,ipv6得到的mac都是一样的，肯定有重复，下面这段代码是。。流式处理***/
        List<String> unique = tmpMacList.stream().distinct().collect(Collectors.toList());
        return unique;
    }

    public static void main(String[] args) {
        /*long a = System.currentTimeMillis();
        System.out.println("进行 multi net address 测试===》");
        try {
            List<String> macs = MacUtil.getMacList();
            long b = System.currentTimeMillis();
            System.out.println("本机的mac网卡的地址有：" + macs);
            System.out.println("总耗时----" + (b - a) + "-----ms");
        }catch (Exception e){
            e.printStackTrace();
        }*/
        try {
            getLocalMac();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取计算机的mac地址
     *
     * @return
     * @throws SocketException
     */
    public static String getLocalMac() throws SocketException, UnknownHostException {
        //获取网卡，获取地址
        InetAddress ia = InetAddress.getLocalHost();
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        System.out.println("mac数组长度：" + mac.length);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            System.out.println("每8位:" + str);
            if (str.length() == 1) {
                sb.append("0" + str);
            } else {
                sb.append(str);
            }
        }
        System.out.println("本机MAC地址:" + sb.toString().toUpperCase());
        return sb.toString().toUpperCase();
    }


}
