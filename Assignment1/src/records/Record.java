package records;

public class Record {
    private String firstName;
    private String lastName;
    private String job;
    private String recordID;

    public Record(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = "";
        this.recordID = "";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " " + recordID;
    }
}
