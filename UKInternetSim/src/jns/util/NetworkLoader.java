package jns.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jns.Simulator;
import jns.element.DuplexInterface;
import jns.element.DuplexLink;
import jns.element.Interface;
import jns.element.Link;
import jns.element.Node;

public class NetworkLoader {

	public Simulator loadNetworkFromLinksFile(String filename) throws IOException{
		Simulator sim = Simulator.getInstance();
		
		File file = new File(filename);
		@SuppressWarnings("resource")
		BufferedReader fileReader = new BufferedReader(new FileReader(file));
		
		Map<String, Interface> addresses = new HashMap<String, Interface>();
		Map<UnorderedPair, Link> links = new HashMap<UnorderedPair, Link>();
		
		String line;
		while ((line = fileReader.readLine()) != null){
			String[] strPair = line.split(",");
			Interface source = getInterfaceForIPAddress(strPair[0], addresses),
				destination = getInterfaceForIPAddress(strPair[1], addresses);
			
			UnorderedPair pair = new UnorderedPair(source,destination);
			if (!links.containsKey(pair)){
				Link link = new DuplexLink(1000000,0.001);
				
				source.attach(link, true);
				destination.attach(link, true);
				
				sim.attach(link);
				links.put(pair, link);
			}
		}
		
		return sim;
	}
	
	public Interface getInterfaceForIPAddress(String ip, Map<String, Interface> addresses) {
		if (addresses.containsKey(ip)){
			return addresses.get(ip);
		} else {
			Simulator sim = Simulator.getInstance();
			
			String[] ipParts = ip.split(".");
			Interface iface = new DuplexInterface(
									new IPAddr(
									 		Integer.parseInt(ipParts[0]),
									 		Integer.parseInt(ipParts[1]),
									 		Integer.parseInt(ipParts[2]),
									 		Integer.parseInt(ipParts[3])
									)
								);
		    
		    addresses.put(ip, iface);
		    sim.attach(iface);
		    
		    return iface;
		}
	}
	
	private class UnorderedPair {
		
		private Object one;
		private Object two;
		
		public UnorderedPair (Object o, Object t){
			this.one = o;
			this.two = t;
		}
		
		@Override
		public int hashCode() {
			return one.hashCode() + two.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof UnorderedPair){
				UnorderedPair other = (UnorderedPair) obj;
				return (this.one ==other.one && this.two == other.two) || (this.one == other.two && this.two == other.one);
			}
			return false;
		}
		
	}
	
}
