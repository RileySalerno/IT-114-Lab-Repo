import java.io.*;
import java.net.*;
import java.util.Scanner;

public class RPSclient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8080);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter a Username: ");
        String username = scan.nextLine();

        out.println(username);

        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (Exception e) {}
        }).start();

        while (true) {
            String input = scan.nextLine();
            out.println(input);
        }
    }
}
