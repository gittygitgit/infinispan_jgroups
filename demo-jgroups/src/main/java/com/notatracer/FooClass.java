package com.notatracer;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class FooClass {

	public static void main(String[] args) {
		try {
			for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en
					.hasMoreElements();) {
				NetworkInterface intf = (NetworkInterface) en.nextElement();
				System.out.println(intf.getDisplayName());
				System.out.println(intf.getName());
				Enumeration e2 = intf.getInetAddresses();
				while (e2.hasMoreElements()) {
					InetAddress ia = (InetAddress)e2.nextElement();
					System.out.println(ia.getCanonicalHostName());
					System.out.println(ia.getHostName());
					System.out.println(ia.getHostAddress());

				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
