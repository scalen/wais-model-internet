import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import ukSim.utils.AssignProtocols;
import ukSim.utils.NetworkLoader;
import jns.Simulator;
import jns.dynamic.DynamicSchedulerImpl;
import jns.element.Element;
import jns.element.Link;
import jns.element.Node;
import jns.util.IPAddr;

public class Main {

	public static void main(String[] args) {
		DynamicSchedulerImpl sch = null;
		try {
			sch = NetworkLoader.loadNetworkFromLinksFile("uk-routes.txt", "baseTrace");
			sch.attachLargestConnectedSubnet();
			
			Map<Integer, Collection<Node>> cardMap = new HashMap<Integer, Collection<Node>>();
			
			int linktotal = 0;
			
			Enumeration<Element> elements = Simulator.getInstance().enumerateElements();
			if (elements.hasMoreElements()){
				for (Element e = elements.nextElement(); elements.hasMoreElements(); e = elements.nextElement()){
					if (e instanceof Node){
						Node n = (Node) e;
						Collection<Node> list = cardMap.get(n.getIPHandler().getInterfaceCount());;
						if (list == null){
							list = new ArrayList<Node>();
							cardMap.put(n.getIPHandler().getInterfaceCount(), list);
						}
						list.add(n);
					}
					if (e instanceof Link){
						linktotal++;
					}
				}
			}
			SortedSet<Integer> ss = new TreeSet<Integer>();
			ss.addAll(cardMap.keySet());
			for (int card : ss){
				System.out.println("--- CARDINALITY "+ card + " ---");
				for (Node n : cardMap.get(card)){
					IPAddr address = n.getIPHandler().getAddress();
					if (address == null){
						n.dump();
					} else {
						System.out.println(n.getNumber() + " " + address.toString());
					}
				}
			}
			int total = 0;
			for (int card : ss){
				System.out.println("CARDINALITY "+ card + ", COUNT: " + cardMap.get(card).size());
				total += cardMap.get(card).size();
			}
			System.out.println("TOTAL NODE COUNT: "+total);
			System.out.println("TOTAL LINK COUNT: "+linktotal);
			sch.start();
			AssignProtocols ap = new AssignProtocols();
			Node src = ap.getNodeByID("79.69.103.184");
			Node dest = ap.getNodeByID("25.111.36.231");
			ap.sendPacket(src, dest);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (sch != null){
				try {
					sch.stop();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
//
//	    Node src=new Node("Source node");
//	    Node router=new Node("Router");
//	    Node dest=new Node("Destination node");
//	    sim.attach(src);
//	    sim.attach(router);
//	    sim.attach(dest);
//
//
//	    Interface src_iface=new DuplexInterface(new IPAddr(192,168,1,10));
//	    src.attach(src_iface);
//	    sim.attach(src_iface);
//
//	    Interface dest_iface=new DuplexInterface(new IPAddr(128,116,11,20));
//	    dest.attach(dest_iface);
//	    sim.attach(dest_iface);
//
//
//	    Interface route_iface192=new DuplexInterface(new IPAddr(192,168,1,1));
//	    Interface route_iface128=new DuplexInterface(new IPAddr(128,116,11,1));
//
//	    router.attach(route_iface192);
//	    router.attach(route_iface128);
//
//	    sim.attach(route_iface192);
//	    sim.attach(route_iface128);
//
//
//	    Link link_src_router=new DuplexLink(1000000,0.001);
//	    Link link_router_dest=new DuplexLink(64000,0.1);
//
//	    src_iface.attach(link_src_router,true);
//	    route_iface192.attach(link_src_router,true);
//	    sim.attach(link_src_router);
//
//	    route_iface128.attach(link_router_dest,true);
//	    dest_iface.attach(link_router_dest,true);
//	    sim.attach(link_router_dest);
//	    
//
//	    src.addDefaultRoute(src_iface);
//	    dest.addDefaultRoute(dest_iface);
//
//	    router.addRoute(new IPAddr(192,168,1,0),new IPAddr(255,255,255,0),
//	                    route_iface192);
//	    router.addRoute(new IPAddr(128,116,11,0),new IPAddr(255,255,255,0),
//	                    route_iface128);
//	    
	}

}
