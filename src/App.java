import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
      
      boolean exit = false;
      Scanner sc = new Scanner(System.in);
      System.out.println("\n\nWelcome Buddy...\n-- Student Marks Persistent Json File Loader & CSV File Extractor --\n");
      
      while(!exit){
        System.out.println("\n\nSelect Options: (number)\n1. Load JSON File\n2. Multi Json Load \n3. Get CSV File\n4. delete all students in DB \n5. Exit");
        int option = sc.nextInt();
        switch(option) {
          case 1: { JsonToCSVService.loadJson();} break;
          case 2: { JsonToCSVServiceV2.multiLoadJson();} break;
          case 3: { JsonToCSVService.getStudentsCSV();} break;
          case 4: { JsonToCSVService.deleteStudents();} break;
          case 5: exit = true; continue;
          default: System.out.println("\nEnter calid option.. Buddy :)");
        }
      }
         
      System.out.println("\nThank You Buddy.. Bye bye.. ");
      sc.close();
        
    }
}
