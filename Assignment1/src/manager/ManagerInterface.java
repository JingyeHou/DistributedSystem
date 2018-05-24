package manager;

import records.Course;
import records.Location;
import records.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ManagerInterface extends Remote{

    void createTRecord(String firstName, String lastName, String address, String phone, Course specialization, Location location) throws RemoteException;
    void createSRecord(String firstName, String lastName, List<Course> courseRegistered, Status status) throws RemoteException;
    void editRecord(String recordID, String fieldName, String newValue) throws RemoteException;
    String getRecordCounts() throws RemoteException;
    String format() throws RemoteException;
}
