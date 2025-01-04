import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

public class JsonToCSVServiceV2 extends JsonToCSVService {

    private static Scanner sc;  
    private static Gson jsonToStudentsObjct;
    private static ExecutorService executor;

    static {
        sc = new Scanner(System.in);
        jsonToStudentsObjct = new Gson();
    }

    private static class Task implements Callable<String> {
        private Runnable task;
        private String name;

        Task(Runnable task, String name) {
            this.task = task;
            this.name = name;
        }

        @Override
        public String call() throws Exception {
            task.run();
            return "\n" + this.name + " completed..";
        }
    }

    private static String fromJsonToJsonString (String fileLocation) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileLocation));
        StringBuffer sb = new StringBuffer();
        String line;
        while((line = br.readLine()) != null) sb.append(line);
        br.close();
        return sb.toString();
    }

    public static void multiLoadJson() {

        // Accpect No. of Files (1 - 10)
        System.out.println("\ninfo ==> Enter No. of Json Files (1 - 10): ");
        byte nFiles;
        while ( (nFiles = sc.nextByte()) > 10 || nFiles < 1 ) System.out.println("\nEnter (1 - 10) Valid Number:");

        // Accpect Json File locations and load to StudentsData Object (students objects)
        List<StudentsData.Student> students = new LinkedList<>();
        while(nFiles != 0) {
            System.out.printf("\ninfo ==> (%d) Please provide Json file location: ", nFiles);
            String fileLocation = sc.next();
            try {
                StudentsData studentsData = jsonToStudentsObjct.fromJson(fromJsonToJsonString(fileLocation), StudentsData.class);
                students.addAll(studentsData.getStudents());
                nFiles--;
            } catch (Exception e) { System.out.println("\nerror ==> Provide valid Json file location with valid Json structure.."); }
        }

        // divide all students objects into chunks and create task for each chunk to load into DB
        int chunkSize = students.size()/10 == 0? 1 : students.size()/10;
        List<Task> tasks = new LinkedList<Task>();
        Optional<JdbcDB> db = JdbcDB.connect();
        for( int i=0; i<students.size(); i+=chunkSize ) {
            int fromIndex = i;
            int toIndex = Math.min(i+chunkSize, students.size());
            tasks.add(new Task (() -> {
                if(db.isPresent()) {
                    try {
                        JsonToCSVService.parallelPushToDB(db.get(), students.subList(fromIndex, toIndex));
                    } catch (InterruptedException e) {
                        System.out.println("error ==> db failed to response..");
                    }
                }
            }, "Task" + i/chunkSize));

        }

        // exucute all chunks
        try {
            executor = Executors.newFixedThreadPool(5);
            executor.invokeAll(tasks);
            System.out.println("\ninfo ==> All Tasks completed.. (Json fiels loaded to DB)");
        } catch (InterruptedException e) {
            System.out.println("\nerror ==> tasks failed to excute..");
        }

        // close executor after all tasks ended
        executor.shutdown();
    }
}
