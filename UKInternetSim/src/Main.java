import jns.Simulator;
import jns.element.DuplexInterface;
import jns.element.DuplexLink;
import jns.element.Interface;
import jns.element.Link;
import jns.element.Node;
import jns.util.IPAddr;


public class Main {

	public static void main(String[] args) {
		Simulator sim=Simulator.getInstance();

	    Node src=new Node("Source node");
	    Node router=new Node("Router");
	    Node dest=new Node("Destination node");
	    sim.attach(src);
	    sim.attach(router);
	    sim.attach(dest);


	    Interface src_iface=new DuplexInterface(new IPAddr(192,168,1,10));
	    src.attach(src_iface);
	    sim.attach(src_iface);

	    Interface dest_iface=new DuplexInterface(new IPAddr(128,116,11,20));
	    dest.attach(dest_iface);
	    sim.attach(dest_iface);


	    Interface route_iface192=new DuplexInterface(new IPAddr(192,168,1,1));
	    Interface route_iface128=new DuplexInterface(new IPAddr(128,116,11,1));

	    router.attach(route_iface192);
	    router.attach(route_iface128);

	    sim.attach(route_iface192);
	    sim.attach(route_iface128);


	    Link link_src_router=new DuplexLink(1000000,0.001);
	    Link link_router_dest=new DuplexLink(64000,0.1);

	    src_iface.attach(link_src_router,true);
	    route_iface192.attach(link_src_router,true);
	    sim.attach(link_src_router);

	    route_iface128.attach(link_router_dest,true);
	    dest_iface.attach(link_router_dest,true);
	    sim.attach(link_router_dest);
	    

	    src.addDefaultRoute(src_iface);
	    dest.addDefaultRoute(dest_iface);

	    router.addRoute(new IPAddr(192,168,1,0),new IPAddr(255,255,255,0),
	                    route_iface192);
	    router.addRoute(new IPAddr(128,116,11,0),new IPAddr(255,255,255,0),
	                    route_iface128);
	    
	    src.dump();
	    System.out.println("-----------------------------------");
	    router.dump();
	    System.out.println("-----------------------------------");
	    dest.dump();
	}

}
