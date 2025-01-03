import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.gson.Gson;


public class JsonToCSVService {

    static int poolSize = 4;

    @SuppressWarnings("resource")
    public static void loadJson() {

        Scanner sc = new Scanner(System.in);
        Optional<JdbcDB> db = JdbcDB.connect();
        StudentsData students;
        BufferedReader br;
        Gson jsonToStudents = new Gson();
        String fileUrl;

        while(true) {
            System.out.println("info ==> Please provide Json file location:");
            fileUrl = sc.next();
            try {
                String line;
                StringBuilder sb = new StringBuilder();

                br = new BufferedReader(new FileReader(fileUrl));
                while((line = br.readLine()) != null) 
                    sb.append(line);

                students = jsonToStudents.fromJson(sb.toString(),StudentsData.class);
                
                if(db.isPresent()) {
                    parallelPushToDB(db.get(),students.getStudents());
                    System.out.println("\ninfo ==> File updated to DB\n\n");
                    break;
                } else { System.out.println("info ==> db failed to response..");}

            } catch (FileNotFoundException e) {
                System.out.println("info ==> what buddy! provide valid json file location..");
            } catch (IOException e) {
                System.out.println("info ==> sorry buddy! file reading failed..");
            } catch (InterruptedException e) {
                  System.out.println("info ==> Failed to upload as chunks..");
            }
        }
    }

    private static void parallelPushToDB(JdbcDB dbRef, List<StudentsData.Student> students) throws InterruptedException {
        
        int chunkLength = students.size()/4;
        
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        
        executor.submit(() -> { chunkPush(dbRef, students, 0, chunkLength); });
        executor.submit(() -> { chunkPush(dbRef, students, chunkLength*2-chunkLength, chunkLength*2); });
        executor.submit(() -> { chunkPush(dbRef, students, chunkLength*3-chunkLength, chunkLength*3); });
        executor.submit(() -> { chunkPush(dbRef, students, students.size()-chunkLength, students.size()); });
        executor.shutdown();
       
    }

    private static synchronized void chunkPush(JdbcDB dbRef, List<StudentsData.Student> students, int toIndex, int fromtIndex) {
        List<StudentsData.Student> chunk = students.subList(toIndex, fromtIndex);
        dbRef.updateStudents(chunk);
    }


    public static void deleteStudents() {
        Optional<JdbcDB> db = JdbcDB.connect();
        db.ifPresent((c) -> {
            c.deleteAllStudents();
        });
    }

    public static void getStudentsCSV() {
        Optional<JdbcDB> db = JdbcDB.connect();
        db.ifPresent((c) -> {
            try {
                List<StudentsData.Student> list = c.readStudents();
                String homeDirectory = System.getProperty("user.home");
                FileWriter fw = new FileWriter(homeDirectory + "/students.csv");
                BufferedWriter bw = new BufferedWriter(fw);
                for( StudentsData.Student s : list) {
                    bw.write(s.toString());
                }
                bw.close();
                System.out.println("info ==> students.csv file downloaded at : "+homeDirectory);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("error ==> CSV extraction failed..");
            }
        });

    }


}
