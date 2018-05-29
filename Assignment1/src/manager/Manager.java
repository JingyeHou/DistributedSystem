package manager;

import observer.Observer;
import records.*;
import servers.Server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Manager extends UnicastRemoteObject implements ManagerInterface {

    Map<String, List<Record>> data = new HashMap<String, List<Record>>();
    Map<String, Record> persons = new HashMap<String, Record>();
    private List<Observer> observers;

    public Manager() throws RemoteException {
        super();
        observers = new ArrayList<>();
    }

    public void attach(Observer observer){
        observers.add(observer);
    }

    public void  deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyAllObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void setMessage(String message) {
        notifyAllObservers(message);
    }

    public void createTRecord(String firstName, String lastName, String address, String phone, Course specialization, Location location) throws RuntimeException{
        TeacherRecord teacherRecord = new TeacherRecord(firstName, lastName, address, phone, specialization, location);
        putIntoMap(teacherRecord);
        persons.put(teacherRecord.getRecordID(), teacherRecord);
        notifyAllObservers("the Teacher Record is created Successfully");
    }

    public void createSRecord(String firstName, String lastName, List<Course> courseRegistered, Status status) throws RuntimeException {
        StudentRecord studentRecord = new StudentRecord(firstName, lastName, courseRegistered, status);
        putIntoMap(studentRecord);
        persons.put(studentRecord.getRecordID(), studentRecord);
        notifyAllObservers("the Student Record is created Successfully");
    }

    public String getRecordCounts() throws RemoteException {
        String str = "";
        Map<String, Integer> serverStore = Server.getServerStore();
        for (Map.Entry<String, Integer> integerStringEntry : serverStore.entrySet()) {
            DatagramSocket aSocket = null;
            try {
                aSocket = new DatagramSocket();
                byte[] m = "".getBytes();
                InetAddress aHost = InetAddress.getByName("localhost");
                int serverPort = integerStringEntry.getValue();
                DatagramPacket request = new DatagramPacket(m, "".length(), aHost, serverPort);
                aSocket.send(request);

                byte[] buffer = new byte[6];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);
                str += new String(reply.getData()) + " ";
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public void editRecord(String recordID, String fieldName, String newValue) throws RemoteException{
        Record person = persons.get(recordID);
        if (person == null) {
            return;
        }
        if (person.getJob() == "Student") {
            StudentRecord student = (StudentRecord) person;
            student.setRecord(fieldName, newValue);
            persons.put(recordID, student);
        }

        if (person.getJob() == "Teacher") {
            TeacherRecord teacher = (TeacherRecord) person;
            teacher.setRecord(fieldName, newValue);
            persons.put(recordID, teacher);
        }
    }

    public String getCounts() throws RemoteException {
        int counts = 0;
        for (List<Record> records : data.values()) {
            counts += records.size();
        }
        return counts + "";
    }

    private void putIntoMap(Record record) {
        String alpha = record.getLastName().substring(0, 1);
        if (data.get(alpha) == null) {
            data.put(alpha, new ArrayList<Record>());
        }
        data.get(alpha).add(record);
    }

    public String format(){
        String str = "";
        for (Map.Entry<String, List<Record>> stringListEntry : data.entrySet()) {
            str += stringListEntry.getKey() + ": ";
            for (Record record : stringListEntry.getValue()) {
                if(record.getJob().equals("Student")) {
                    StudentRecord student = (StudentRecord) record;
                    str += record.toString() + "  " + student.getStatus() + "  ";
                }
                if (record.getJob().equals("Teacher")) {
                    TeacherRecord teacher = (TeacherRecord) record;
                    str += record.toString() + "  " + teacher.getAddress() + " " + teacher.getPhone() + " " + teacher.getLocation() + "  ";
                }
            }
            str += "\n";
        }
        return str;
    }
}
