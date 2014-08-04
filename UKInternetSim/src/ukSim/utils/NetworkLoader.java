package ukSim.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import jns.Simulator;
import jns.dynamic.DynamicSchedulerImpl;
import jns.element.Node;
import jns.util.IPAddr;

public class NetworkLoader {

	private static final int DEFAULT_BANDWIDTH = 1000000;
	private static final double DEFAULT_DELAY = 0.01;
	private static final double DEFAULT_ERROR_RATE = 0.01;

	public static DynamicSchedulerImpl loadNetworkFromLinksFile(String filename, String traceName) throws IOException{
		BufferedReader fileReader = null;
		try {
			Simulator sim = Simulator.getInstance();
			InputStream is = NetworkLoader.class.getClassLoader().getResourceAsStream(filename);
			InputStreamReader isr = new InputStreamReader(is);
			fileReader = new BufferedReader(isr);
			
			DynamicSchedulerImpl sch = new DynamicSchedulerImpl(traceName, DEFAULT_BANDWIDTH, DEFAULT_DELAY, DEFAULT_ERROR_RATE, new IPAddr(0,0,0,0));
			
			String line;
			while ((line = fileReader.readLine()) != null){
				String[] ips = line.split(",");

				if (ips.length < 2){
					continue;
				}
				
				Queue<IPAddr> forwardIPs = new ArrayDeque<IPAddr>();
				Queue<IPAddr> backwardIPs = new ArrayDeque<IPAddr>();
				
				for (int i = 0; i < ips.length; i++){
					String[] ipParts = ips[i].split("\\.");
					
					IPAddr ip = new IPAddr(
					 		Integer.parseInt(ipParts[0]),
					 		Integer.parseInt(ipParts[1]),
					 		Integer.parseInt(ipParts[2]),
					 		Integer.parseInt(ipParts[3])
					);
					
					forwardIPs.offer(ip);
					sch.addNode(ip);
				}
				
				IPAddr destination = forwardIPs.poll(),
					   source = null;
				while (!forwardIPs.isEmpty()){
					if (source != null){
						backwardIPs.offer(source);
					}
					
					source = destination;
					destination = forwardIPs.poll();
					
					sch.addLinkInTrace(source, destination, forwardIPs, backwardIPs);
				}
			}
			
			return sch;
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
