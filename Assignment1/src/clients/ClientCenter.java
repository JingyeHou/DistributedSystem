package clients;

public class ClientCenter {
    public static void main(String[] args) throws Exception{
        ClientCenter clientCenter = new ClientCenter();
        clientCenter.run();
    }

    public void run() throws Exception{
        Client MTLClient = new Client("MTL");
        Client MTLClient2 = new Client("MTL");
        Client LVLClient = new Client("LVL");

        MTLClient.createSR("Jingye", "Hou", "maths|french", "active");
        MTLClient.createSR("Wenjin", "Chu", "science", "active");
        MTLClient2.createSR("Junpin", "Deng", "french|science", "inactive");
//        MTLClient.editRecord("SR00001", "Status", "active");

        MTLClient.start();
        MTLClient2.start();
        System.out.println( MTLClient.getManager().format());
        System.out.println(MTLClient.getManager().getRecordCounts());

    }
}
