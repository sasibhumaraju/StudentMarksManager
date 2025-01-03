import java.util.List;

public class StudentsData {
    private List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Student s : students) {
            sb.append(s);
        }
        return "StudentsData:\n" + sb.toString() + "\n";
    }

    public static class Student {
        private int id;
        private String name;
        private int marks;

        public Student(int id, String name, int marks) {
            this.id = id;
            this.name = name;
            this.marks = marks;
        }


        public Student() {}


        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMarks() {
            return marks;
        }

        public void setMarks(int marks) {
            this.marks = marks;
        }

        @Override
        public String toString() {
            return id +","+name+","+marks+"\n";
        }
    }
}
