package clients;

import records.*;
import manager.ManagerInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Client extends Thread{
    private String clientLocation;
    private ManagerInterface manager;
    static private int MTLID = 0;
    static private int LVLID = 0;
    static private int DDOID = 0;
    private int id;
    private int port;
    private List<Record> records;
    private List<EditRecordInFo> editRecordInFos;
    private Logger logger;
    FileHandler fileHandler = null;

    public Client(String clientLocation) {
        this.clientLocation = clientLocation;
        if (clientLocation.equals("MTL")) {
            MTLID++;
            this.id = MTLID;
            this.port = 6666;
        }
        if (clientLocation.equals("DDO")) {
            DDOID++;
            this.id = DDOID;
            this.port = 7777;
        }
        if (clientLocation.equals("LVL")) {
            LVLID++;
            this.id = LVLID;
            this.port = 8888;
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(this.port);
            this.manager = (ManagerInterface) registry.lookup(clientLocation);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        this.records = new ArrayList<Record>();
        this.editRecordInFos = new ArrayList<EditRecordInFo>();
        this.logger  = Logger.getLogger(getID() + "ClientLogger");
        this.logger.setUseParentHandlers(false);
    }

    public String getClientLocation() {
        return clientLocation;
    }

    public void setClientLocation(String clientLocation) {
        this.clientLocation = clientLocation;
    }

    public ManagerInterface getManager() {
        return manager;
    }

    public void createSR(String firstName, String lastName, String courses, String status) {
        Record record = new Record();
        if (status.equals("active")) record.setStatus(Status.active);
        if (status.equals("inactive")) record.setStatus(Status.inactive);
        record.setName(firstName, lastName);
        record.setCourses(parseCourse(courses));
        record.setSRFlag(true);
        records.add(record);
    }

    public void createTR(String firstName, String lastName, String address, String phone,  String course, String location){
        Record record = new Record();
        record.setName(firstName, lastName);
        record.setAddress(address);
        record.setPhone(phone);
        if (course.equals("maths")) record.setSpecialization(Course.maths);
        if (course.equals("french")) record.setSpecialization(Course.french);
        if (course.equals("science")) record.setSpecialization(Course.science);
        if (location.equals("LVL")) record.setLocation(Location.lvl);
        if (location.equals("MTL")) record.setLocation(Location.mtl);
        if (location.equals("DDO")) record.setLocation(Location.ddo);
        record.setTRFlag(true);
        records.add(record);
    }

    public String getRecordCounts() throws RemoteException{
        return this.manager.getRecordCounts();
    }

    public void editRecord(String recordID, String fieldName, String newValue) {
        editRecordInFos.add( new EditRecordInFo(recordID, fieldName, newValue));
    }

    private List<Course> parseCourse(String courses) {
        List<Course> list = new ArrayList<Course>();
        String[] courseList = courses.split("\\|");
        for (String course : courseList) {
            if (course.equals("maths")) list.add(Course.maths);
            if (course.equals("french")) list.add(Course.french);
            if (course.equals("science")) list.add(Course.science);
        }
        return list;
    }


    @Override
    public void run() {
        super.run();
        try {
            fileHandler = new FileHandler("./logs/client/" + this.getID() + "Client.log");
            logger.addHandler(fileHandler);
//            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    Calendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(record.getMillis());
                    return record.getLevel()
                            + logTime.format(cal.getTime())
                            + " || "
                            + record.getSourceClassName().substring(
                            record.getSourceClassName().lastIndexOf(".")+1,
                            record.getSourceClassName().length())
                            + "."
                            + record.getSourceMethodName()
                            + "() : "
                            + record.getMessage() + "\n";
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Record record : records) {
            if (record.isSRFlag()) {
                try {
                    manager.createSRecord(record.getFirstName(), record.getLastName(), record.getCourses(), record.getStatus());
                    logger.info("the Student Record is created Successfully");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            if (record.isTRFlag()) {
                try {
                    manager.createTRecord(record.getFirstName(), record.getLastName(), record.getAddress(), record.getPhone(), record.getSpecialization(), record.getLocation());
                    logger.info("the Teacher Record is created Successfully");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        if (editRecordInFos != null) {
            for (EditRecordInFo editRecordInFo : editRecordInFos) {
                try {
                    this.manager.editRecord(editRecordInFo.getRecordID(), editRecordInFo.getFieldName(), editRecordInFo.getNewValue());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getID() {
        if (clientLocation.equals("MTL")) return "MTL" + String.format("%5d", id).replace(" ", "0");
        if (clientLocation.equals("DDO")) return "DDO" + String.format("%5d", id).replace(" ", "0");
        if (clientLocation.equals("LVL")) return "LVL" + String.format("%5d", id).replace(" ", "0");
        else return "";
    }

    class EditRecordInFo {
        private String recordID;
        private String fieldName;
        private String newValue;

        public EditRecordInFo(String recordID, String fieldName, String newValue) {
            this.recordID = recordID;
            this.fieldName = fieldName;
            this.newValue = newValue;
        }

        public String getRecordID() {
            return recordID;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getNewValue() {
            return newValue;
        }
    }
    class Record {
        private String firstName;
        private String lastName;
        private List<Course> courses;
        private Status status;
        private String address;
        private String phone;
        private Course specialization;
        private Location location;
        private boolean SRFlag;
        private boolean TRFlag;

        public Record() {
            this.SRFlag = false;
            this.TRFlag = false;
        }

        public boolean isSRFlag() {
            return SRFlag;
        }

        public void setSRFlag(boolean SRFlag) {
            this.SRFlag = SRFlag;
        }

        public boolean isTRFlag() {
            return TRFlag;
        }

        public void setTRFlag(boolean TRFlag) {
            this.TRFlag = TRFlag;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setName(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getLastName() {
            return lastName;
        }


        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Course getSpecialization() {
            return specialization;
        }

        public void setSpecialization(Course specialization) {
            this.specialization = specialization;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }
}
