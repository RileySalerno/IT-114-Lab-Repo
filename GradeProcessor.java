import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GradeProcessor {

    public static void main(String[] args) {
        
        // Input and Output file names
        String inputFileName = "student_data.txt";
        String outputFileName = "grade_report.txt";

        // TODO 1: Wrap your code in a try-catch block.
        // The catch block should handle 'FileNotFoundException'.
        try {
            // TODO 2: Create a File object and a Scanner to read the input file.
            File inputFile = new File(inputFileName);
            Scanner scan = new Scanner(inputFile);

            // TODO 3: Create a PrintWriter object to write to the output file.
            PrintWriter output = new PrintWriter(outputFileName);

            System.out.println("Processing file...");

            // Write the header to the output file separated by tab characters (\t);
            // Print a dashed line ("--------------------");
            output.println("Name\tAverage\tStatus");
            output.println("--------------------------------");

            // TODO 4: Create a while loop to process the file line by line.
            //   - Read the name (String)
            //   - Read the three scores (int or double)
            //   - Calculate the average
            //   - Determine "Pass" or "Fail" (Pass is >= 70.0)
            //   - Write the formatted line to the output file
            while (scan.hasNext()) {
                String name = scan.next();
                double grade1 = scan.nextDouble();
                double grade2 = scan.nextDouble();
                double grade3 = scan.nextDouble();

                double average = (grade1 + grade2 + grade3) / 3.0;

                String status;
                    if (average >= 70.0) {
                        status = "Pass";
                    } else {
                        status = "Fail";
                    }
                output.printf("%s\t%.1f\t%s\n", name, average, status);
            }


            // TODO 5: Close both the Scanner and the PrintWriter.
            scan.close();
            output.close();
            
            System.out.println("Done! Check " + outputFileName + " for results.");
            // TODO 6: catch block:
        } catch (FileNotFoundException e) {
            System.out.println("Error: Input file 'student_data.txt' was not found.");
        }
    }
}