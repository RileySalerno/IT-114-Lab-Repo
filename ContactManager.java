import java.util.ArrayList;
import java.util.Scanner;

public class ContactManager {

    // --- TODO: REGEX HELPER METHOD ---
    public static String formatPhoneNumber(String raw) {
        // 1. Strip non-digits   
        String onlyNumbers = raw.replaceAll("\\D", "");
        // 2. Check length and format
            // Regex to group: (3 digits)(3 digits)(4 digits) -> $1-$2-$3
        if (onlyNumbers.length() == 10) {
            return onlyNumbers.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        }
        
        return "Invalid Number";

    }

    
    public static void main(String[] args) {
        ArrayList<Contact> contacts = new ArrayList<>();

        // Contacts to be sanitized and sorted
        contacts.add(new Contact("Zack Morris", "zack@bayside.edu", "555.123.4567"));
        contacts.add(new Contact("Alice Smith", "alice@test.com", "(555) 999-8888"));
        contacts.add(new Contact("Bob Jones", "bob@test.com", "5551112222"));

        System.out.println("--- Cleaning Data ---");
       
        //TODO: CLEAN DATA (Loop and Format)
        for (Contact c : contacts) {
            String clean = formatPhoneNumber(c.getPhone());
            c.setPhone(clean);
        }
        
        System.out.println("--- Sorting Data ---");
        
        //TODO: SORT DATA (Bubble Sort)
        
        for (int i = 0; i < contacts.size() - 1; i++) {
            for (int j = 0; j < contacts.size() - 1 - i; j++) {

                String name1 = contacts.get(j).getName();
                String name2 = contacts.get(j + 1).getName();

                if (name1.compareTo(name2) > 0) {

                    Contact temp = contacts.get(j);
                    contacts.set(j, contacts.get(j + 1));
                    contacts.set(j + 1, temp);
                }
            }
        }
        
        // Print sorted list
        for (Contact c : contacts) {
            System.out.println(c);
        }

        //Input name to search for
        System.out.println("\n--- Search ---");
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a name to find: ");
        String searchName = scan.nextLine();
        
        boolean found = false;
        
        //TODO: Search Data for name inputted
        for (Contact c : contacts) {
            if (c.getName().equalsIgnoreCase(searchName)) {
                System.out.println("Found:" + c.getName() + " - " + c.getPhone());
                found = true;
                break;
            }
        }
        
        
        
        if (!found) { //Print not found if name is misspelled or not in list
            System.out.println("Contact not found.");
        }
    }
}