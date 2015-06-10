
import jns.command.Command;

import jns.dynamic.DynamicSchedulerImpl;
import jns.util.IPAddr;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Scanner;
 
public class PacketTransfer {

	public static void main(String[] args)throws FileNotFoundException, RemoteException, UnknownHostException
    {
        
Scanner scanner = new Scanner(new File("resources/routes.txt"));
        
        //Set the delimiter used in file
        scanner.useDelimiter(",");
        
        
       // System.out.print(scanner.next());
         
        //Get all tokens and store them in some data structure
        //I am just printing them
        //String a = scanner.next().replace(".", ",");
        //System.out.print(a);
        while (scanner.hasNextLine()) 
        {
        
        	System.out.print(scanner.next()); //.replace(".", ","));
        }
         
        //Do not forget to close the scanner  
        scanner.close();
        
        IPAddr nodeA = new IPAddr(192,168,0,1);
        IPAddr nodeB = new IPAddr(192,168,0,2);

        DynamicSchedulerImpl sch = new DynamicSchedulerImpl("PacketTransfer", 500000, 0.008, 0.0, new IPAddr(255,255,255,0));
        sch.addNode(nodeA);
        sch.addNode(nodeB);
        sch.addLink(nodeA, nodeB);
        sch.start();
        byte[] data = {0,1,2,3,4,5,6,7,8,9};
        
        sch.scheduleUnicast(InetAddress.getByName(nodeA.toString()), InetAddress.getByName(nodeB.toString()), data);
        System.out.println("Packet transfer successful");
    }
}


