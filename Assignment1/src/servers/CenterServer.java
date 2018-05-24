package servers;

import java.util.LinkedList;
import java.util.List;

public class CenterServer {
    public static void main(String[] args) {
        List<Server> servers = new LinkedList<Server>();
        servers.add(new Server(6666, "MTL"));
        servers.add(new Server(8888, "LVL"));
        servers.add(new Server(7777, "DDO"));

        for (Server server : servers) {
            server.start();
        }
    }
}
