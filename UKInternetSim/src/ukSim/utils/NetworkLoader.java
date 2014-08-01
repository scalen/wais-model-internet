package ukSim.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jns.Simulator;
import jns.element.DuplexInterface;
import jns.element.DuplexLink;
import jns.element.Interface;
import jns.element.Link;
import jns.element.Node;
import jns.util.IPAddr;

public class NetworkLoader {

	private static final int DEFAULT_BANDWIDTH = 1000000;
	private static final double DEFAULT_DELAY = 0.01;

	public static Simulator loadNetworkFromLinksFile(String filename) throws IOException{
		BufferedReader fileReader = null;
		try {
			Simulator sim = Simulator.getInstance();
			InputStream is = NetworkLoader.class.getClassLoader().getResourceAsStream(filename);
			InputStreamReader isr = new InputStreamReader(is);
			fileReader = new BufferedReader(isr);
			
			
			Map<IPAddr, Node> nodes = new HashMap<IPAddr, Node>();
			Map<UnorderedPair<IPAddr>, Link> links = new HashMap<UnorderedPair<IPAddr>, Link>();
			
			String line;
			while ((line = fileReader.readLine()) != null){
//System.out.println(line);
				
				String[] ips = line.split(",");
				
				List<IPAddr> forwardIPs = new ArrayList<IPAddr>();
				
				for (int i = 0; i < ips.length - 1; i++){
//System.out.println(ips[i]);
					String[] ipParts = ips[i].split("\\.");
					
					IPAddr ip = new IPAddr(
					 		Integer.parseInt(ipParts[0]),
					 		Integer.parseInt(ipParts[1]),
					 		Integer.parseInt(ipParts[2]),
					 		Integer.parseInt(ipParts[3])
					);
					
					forwardIPs.add(ip);
				}
				
				for (int i = 0; i < forwardIPs.size() - 1; i++){
					IPAddr sIP = forwardIPs.get(i);
					IPAddr dIP = forwardIPs.get(i + 1);
//System.out.println(sIP.toString() + "->" + dIP.toString());
					UnorderedPair<IPAddr> pair = new UnorderedPair<IPAddr>(sIP,dIP);
					
					Node sNode = getNodeForIPAddress(sIP, nodes),
						dNode = getNodeForIPAddress(dIP, nodes);
					
					Interface source = null, destination = null;
					if (links.containsKey(pair)){
						Link link = links.get(pair);
						
						if (link.getIncomingInterface().getIPAddr().equals(sIP)){
							source = link.getIncomingInterface();
							destination = link.getOutgoingInterface();
						} else {
							destination = link.getIncomingInterface();
							source = link.getOutgoingInterface();
						}
					} else {
						source = new DuplexInterface(sIP, DEFAULT_BANDWIDTH);
						sNode.attach(source);
						sim.attach(source);
						
						destination = new DuplexInterface(dIP, DEFAULT_BANDWIDTH);
						dNode.attach(destination);
						sim.attach(destination);
						
						Link link = new DuplexLink(DEFAULT_BANDWIDTH,DEFAULT_DELAY);
						// first interface attached is incoming, second is outgoing, implicitly
						source.attach(link, false);
						destination.attach(link, false);
						sim.attach(link);
						
						links.put(pair, link);
					}
					
					for (int n = 0; n < i; n++){
//System.out.println(destination.toString() + "-->>" + forwardIPs.get(n).toString());
						dNode.addRoute(forwardIPs.get(n), new IPAddr(0,0,0,0), destination);
					}
					for (int n = i + 2; n < forwardIPs.size(); n++){
//System.out.println(source.toString() + "-->>" + forwardIPs.get(n).toString());
						sNode.addRoute(forwardIPs.get(n), new IPAddr(0,0,0,0), source);
					}
				}
			}
			
			return sim;
		} catch (IOException e) {
			throw e;
		} finally {
			if (fileReader != null){
				fileReader.close();
			}
		}
	}
	
	public static Node getNodeForIPAddress(IPAddr ip, Map<IPAddr, Node> addresses) {
		if (addresses.containsKey(ip)){
			return addresses.get(ip);
		} else {
			Simulator sim = Simulator.getInstance();
			
			Node node = new Node(ip.toString());
		    
		    addresses.put(ip, node);
		    sim.attach(node);
		    
		    return node;
		}
	}
	
	private static class Pair <T> {
		protected T one;
		protected T two;
		
		public Pair (T o, T t){
			this.one = o;
			this.two = t;
		}
		
		@Override
		public int hashCode() {
			return one.hashCode() * 32 + two.hashCode();
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Pair){
				Pair other = (Pair) obj;
				return this.one.equals(other.one) && this.two.equals(other.two);
			}
			return false;
		}
	}
	
	private static class UnorderedPair<T> extends Pair<T> {
		
		public UnorderedPair (T o, T t){
			super(o,t);
		}
		
		@Override
		public int hashCode() {
			return one.hashCode() + two.hashCode();
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof UnorderedPair){
				UnorderedPair other = (UnorderedPair) obj;
				return super.equals(obj) || (this.one.equals(other.two) && this.two.equals(other.one));
			}
			return false;
		}
		
	}
	
}
