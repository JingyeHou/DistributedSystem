package records;

public class TeacherRecord extends Record {
    private String address;
    private String phone;
    private Course specialization;
    private Location location;
    private static int id = 0;

    public TeacherRecord(String firstName, String lastName, String address, String phone, Course specialization, Location location) {
        super(firstName, lastName);
        setJob("Teacher");
        id++;
        this.setRecordID("TR" + String.format("%5d", id).replace(" ", "0"));
        this.address = address;
        this.phone = phone;
        this.specialization = specialization;
        this.location = location;
    }

    public void setRecord(String fieldName, String value) {
        if (fieldName.equals("address")) setAddress(value);
        if (fieldName.equals("phone")) setPhone(value);
        if (fieldName.equals("Course")) {
            if (value.equals("maths")) setSpecialization(Course.maths);
            if (value.equals("french")) setSpecialization(Course.french);
            if (value.equals("science")) setSpecialization(Course.science);
        }
        if (fieldName.equals("Location")) {
            if (value.equals("MTL")) setLocation(Location.mtl);
            if (value.equals("LVL")) setLocation(Location.lvl);
            if (value.equals("DDO")) setLocation(Location.ddo);
        }
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
