import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.github.cdimascio.dotenv.Dotenv;


public class JdbcDB {

    private static Optional<Connection> con = Optional.ofNullable(null);
    private static Optional<JdbcDB> jdbcDb = Optional.ofNullable(null);

    private JdbcDB () {}

    // Factory method
    static Optional<JdbcDB> connect() {
        if(con.isPresent()) return jdbcDb;
        Dotenv env = Dotenv.load();
        String jdbcUrl = env.get("DB_URL");
        String username = env.get("UNAME");
        String password = env.get("PASS");
        try  {
            Connection con = DriverManager.getConnection(jdbcUrl,username,password);
            System.out.println("info ==> DB connected..");
            JdbcDB.con = Optional.ofNullable(con);
            JdbcDB.jdbcDb = Optional.of(new JdbcDB());
            return JdbcDB.jdbcDb;
        } catch (SQLException e) {
            System.out.println("info ==> DB connection failed...");
            return JdbcDB.jdbcDb;
        }
    }

    public boolean updateStudent(StudentsData.Student student) {
        boolean transaction = true;
        if(con.isPresent()) {
            try {
                PreparedStatement ps = con.get().prepareStatement("insert into students (sid, sname, marks) values (?, ?, ?)"+
                "ON DUPLICATE KEY UPDATE sname = VALUES(sname), marks = VALUES(marks)");
                ps.setInt(1, student.getId());
                ps.setString(2, student.getName());
                ps.setInt(3, student.getMarks());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                transaction = false;
            }
        }
        return transaction;
    }

    public boolean updateStudents(List<StudentsData.Student> students) {
        boolean transaction = true;
        if(con.isPresent()) {
            PreparedStatement ps;
            try {
                ps = con.get().prepareStatement("insert into students (sid, sname, marks) values (?, ?, ?)"+
                "ON DUPLICATE KEY UPDATE sname = VALUES(sname), marks = VALUES(marks)");
                for (StudentsData.Student s : students) {
                    ps.setInt(1, s.getId());
                    ps.setString(2, s.getName());
                    ps.setInt(3, s.getMarks());
                    ps.addBatch();
                }
                ps.executeBatch();
            } catch (SQLException e) {
                transaction = false;
                e.printStackTrace();
            }
        }
        return transaction;
    }

    public List<StudentsData.Student> readStudents() {
        List<StudentsData.Student> students = new ArrayList<>();
        if(con.isPresent()) {
            try {
                Statement st = con.get().createStatement();
                ResultSet rs = st.executeQuery("select * from students");
                while(rs.next()) {
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    int marks = rs.getInt(3);
                    StudentsData.Student student = new StudentsData.Student(id,name,marks);
                    students.add(student);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return students;
    } 

    public void deleteAllStudents () {
        if(con.isPresent()) {
            try {
                Statement st = con.get().createStatement();
                st.execute("TRUNCATE TABLE students");
                System.out.println("info ==> All students deleted from DB");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error ==> deletion failed..");
            }
        }
    }
}
