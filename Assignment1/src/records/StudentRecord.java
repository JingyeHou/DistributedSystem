package records;

import java.util.List;

public class StudentRecord extends Record{
    private List<Course> CoursesRegistered;
    private Status status;
    private static int id = 0;

    public StudentRecord(String firstName, String lastName, List<Course> coursesRegistered, Status status) {
        super(firstName, lastName);
        setJob("Student");
        CoursesRegistered = coursesRegistered;
        id++;
        this.setRecordID("SR" + String.format("%5d", id).replace(" ", "0"));
        this.status = status;
    }

    public void setRecord(String fieldName, String value) {
        if (fieldName.equals("CoursesRegistered")) {
            if (value.equals("maths")) {
                List<Course> coursesRegistered = getCoursesRegistered();
                coursesRegistered.add(Course.maths);
                setCoursesRegistered(coursesRegistered);
            }
            if (value.equals("french")) {
                List<Course> coursesRegistered = getCoursesRegistered();
                coursesRegistered.add(Course.french);
                setCoursesRegistered(coursesRegistered);
            }
            if (value.equals("science")) {
                List<Course> coursesRegistered = getCoursesRegistered();
                coursesRegistered.add(Course.science);
                setCoursesRegistered(coursesRegistered);
            }
        }

        if (fieldName.equals("Status")) {
            if (value.equals("active")) setStatus(Status.active);
            if (value.equals("inactive")) setStatus(Status.inactive);
        }
    }

    public List<Course> getCoursesRegistered() {
        return CoursesRegistered;
    }

    public void setCoursesRegistered(List<Course> coursesRegistered) {
        CoursesRegistered = coursesRegistered;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}


