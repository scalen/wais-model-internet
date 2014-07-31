/**
 * StopSimulation.java
 *
 *
 *
 * @author Einar Vollset <einar.vollset@ncl.ac.uk>
 *
 */
package jns.dynamic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StopSimulation
{

    public static void main(String[] args)
    {
        try
        {

                Registry reg = LocateRegistry.getRegistry(DynamicScheduler.SERVER_PORT_NO);
                DynamicScheduler m_scheduler = (DynamicScheduler) reg.lookup("DynamicScheduler");
                m_scheduler.stop();
                System.out.println("Stopped simulation!");
        }

        catch(RemoteException e)
            {
                e.printStackTrace();
            }



        catch(NotBoundException e)
        {
            System.err.println("DynamicScheduler not bound on rmi://" + DynamicScheduler.SERVER_HOST_NAME + ":" + DynamicScheduler.SERVER_PORT_NO + "/DynamicScheduler");
            System.exit(-1);
        }


    }
}
