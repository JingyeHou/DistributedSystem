package servers;

import manager.Manager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class Server extends Thread{
    private int port;
    private String ServerLocation;
    private Manager manager;
    private static Map<String, Integer> serverStore = new HashMap<String, Integer>();

    public Server(int port, String serverLocation) {
        this.port = port;
        ServerLocation = serverLocation;
        serverStore.put(serverLocation, port);
    }


    @Override
    public void run() {
        super.run();
        build();
    }

    private void build() {
        DatagramSocket aSocket = null;
        try {
            manager = new Manager();
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind(ServerLocation, manager);
            aSocket = new DatagramSocket(port);
            System.out.println(ServerLocation + "'s server is started");
            while (true) {
                String recordInfo = getServerLocation() + " " + manager.getCounts();
                byte[] buffer = new byte[recordInfo.length()];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                DatagramPacket reply = new DatagramPacket(recordInfo.getBytes(), recordInfo.length(), request.getAddress(), request.getPort());
                aSocket.send(reply);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (aSocket != null) aSocket.close();
        }
    }

    public static Map<String, Integer> getServerStore() {
        return serverStore;
    }

    public int getPort() {
        return port;
    }

    public String getServerLocation() {
        return ServerLocation;
    }

    public Manager getManager() {
        return manager;
    }
}
