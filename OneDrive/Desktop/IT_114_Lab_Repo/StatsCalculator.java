public class StatsCalculator {

    public static void main(String[] args) {

        double[][] scores = {
            {85.5, 90.0, 78.5, 92.0},
            {76.0, 88.5, 90.0, 85.0},
            {95.0, 92.0, 94.5, 98.0},
            {60.0, 70.5, 65.0, 72.0},
            {82.0, 84.0, 80.0, 88.0}
        };

        System.out.println("--- GradeBook Statistics ---");

        calculateStudentAverages(scores);
        calculateAssignmentAverages(scores);
        findHighestScore(scores);
    }

    // Iterate through rows (students)
    public static void calculateStudentAverages(double[][] data) {
        System.out.println("Student Averages:");

        for (int r = 0; r < data.length; r++) {
            double sum = 0;

            for (int c = 0; c < data[r].length; c++) {
                sum += data[r][c];
            }

            double average = sum / data[r].length;
            System.out.printf("Student %d: %.2f%n", r + 1, average);
        }
    }

    // Iterate through columns (assignments)
    public static void calculateAssignmentAverages(double[][] data) {
        System.out.println("Assignment Averages:");

        for (int c = 0; c < data[0].length; c++) {
            double sum = 0;

            for (int r = 0; r < data.length; r++) {
                sum += data[r][c];
            }

            double average = sum / data.length;
            System.out.printf("Assignment %d: %.2f%n", c + 1, average);
        }
    }

    public static void findHighestScore(double[][] data) {

        double highestScore = data[0][0];
        int studentNumber = 0;
        int assignmentNumber = 0;

        for (int r = 0; r < data.length; r++) {
            for (int c = 0; c < data[r].length; c++) {
                if (data[r][c] > highestScore) {
                    highestScore = data[r][c];
                    studentNumber = r;
                    assignmentNumber = c;
                }
            }
        }

        System.out.printf("Highest Score in Class: %.2f (Student %d, Assignment %d)%n",
                highestScore, studentNumber + 1, assignmentNumber + 1);
    }
}
