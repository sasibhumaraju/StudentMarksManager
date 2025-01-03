import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
      
      boolean exit = false;
      Scanner sc = new Scanner(System.in);
      System.out.println("\n\nWelcome Buddy...\n-- Student Marks Persistent Json File Loader & CSV File Extractor --\n");
      
      while(!exit){
        System.out.println("\n\nSelect Options: (number)\n1. Load JSON File\n2. Get CSV File\n3. delete all students in DB \n4. Exit");
        int option = sc.nextInt();
        switch(option) {
          case 1: { JsonToCSVService.loadJson();} break;
          case 2: { JsonToCSVService.getStudentsCSV();} break;
          case 3: { JsonToCSVService.deleteStudents();} break;
          case 4: exit = true; continue;
          default: System.out.println("\nEnter calid option.. Buddy :)");
        }
      }
         
      System.out.println("\nThank You Buddy.. Bye bye.. ");
      sc.close();
        
    }
}
