package ukSim.utils;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import jns.Simulator;
import jns.dynamic.PacketSender;
import jns.dynamic.DynamicSchedulerImpl;
import jns.element.IPPacket;
import jns.element.Node;
import jns.element.Element;
import jns.util.IPAddr;
import jns.util.Protocols;

public class AssignProtocols {
	private Simulator sim; 
	private Vector<Node> nodes;
	private HashMap<String,Node> ipMap;
	
	public AssignProtocols(){
		sim = Simulator.getInstance();
		ipMap = new HashMap<String,Node>();
		nodes = this.enumerateNodes(sim.enumerateElements());
	}
	
	public void assignDNS(){
		
	}
	
	public void assignHTTP(){
		
	}
	
	private Vector<Node> enumerateNodes(Enumeration<Element> elements){
		Vector<Node> nodes = new Vector<Node>();
		while(elements.hasMoreElements()){
			Element e = (Element) elements.nextElement();
			if(e instanceof Node){
				Node n = (Node)e;
				String ip = n.getIPHandler().getAddress().toString();
				nodes.add(n);
				this.ipMap.put(ip, (Node)e);
			}
		}
		System.out.println(nodes.size());
		return nodes;
	}
	
	public void sendPacket(Node src, Node dest){
		System.out.println("sendPacket");
		IPPacket ip = new IPPacket();
		
		src.dump();
		dest.dump();
		byte [] data = "Lorem ipsum dolur sit amet".getBytes();
		PacketSender p = new PacketSender(src.getIPHandler(), dest.getIPHandler().getAddress(), 10, data);
		p.execute();
//		src.getIPHandler().send(src.getIPHandler().getAddress(), dest.getIPHandler().getAddress(), 512, "abcde", Protocols.UDP);
	}
	
	public Node getNodeByID(String ipAdd){
		return this.ipMap.get(ipAdd);
	}
	
}
