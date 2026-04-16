import java.net.*;
import java.util.Scanner;
import java.io.*;

/* ChatClient.java
* A simple client program that connects to a server, and then
* exchanges messages with the server until either side sends "quit" (or an error occurs).
*
* Basic algorithm for the Client:
* 1. Ask for the IP or domain name of the server (127.0.0.1 or localhost)
* 2. Request connection to server on specified host and port.
* 3. Establish connection
* 4. Send message to server (OutputStream)
* 5. Listen for message from server (InputStream)
* 6. Repeat steps 4 and 5 until either side sends "quit" (or error occurs)
* 7. Close connection.
*/

class ChatClient {

    public static void main(String[] args) {

        String ipAddress; // IP address or domain name of Server
        int port = 1728; // The port on which the server listens.

        Socket connection = null; // For communication with the server.

        BufferedReader incoming = null; // Stream for receiving data from server.
        PrintWriter outgoing = null; // Stream for sending data to server.
        String messageOut; // A message to be sent to the server.
        String messageIn; // A message received from the server.

        Scanner userInput = new Scanner(System.in); // Standard input, for reading lines of input from the user.

        // 1. TODO: Get the IP address or domain name of the server from the user.
        System.out.print("Enter server IP: ");
        ipAddress = userInput.nextLine();
        
        
        // 2. Request connection to server on specified host and port
        try {
            System.out.println("Connecting to " + ipAddress + " on port " + port);
            //TODO: Create new socket w/ IP address and port
            connection = new Socket(ipAddress, port);
            incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outgoing = new PrintWriter(connection.getOutputStream(), true);
            System.out.println("Connected.  Enter your first message.");
        }
        // If connection fails (or invalid ip), print error message, close streams and end the program.
        catch (Exception e) {
            System.out.print("There was an error connecting to the server: " + e.getMessage());
            return;
        }
        // 3. Exchange messages with the server.
        try {
            System.out.println("NOTE: Enter 'quit' to end the program.\n");
            while (true) {
                System.out.print("SEND:      ");
                // TODO: Create input and output streams
                messageOut = userInput.nextLine();
                outgoing.println(messageOut);
                
                //TODO: Have client send the first message.
                // If message sent is "quit", then close the connection and streams, print Connection closed and end the program.
                if (messageOut.equalsIgnoreCase("quit")) {
                    System.out.println("Connection Closed");
                    connection.close();
                    break;
                }
                
                // Check for errors while transmitting message.
                if (outgoing.checkError()) {
                    throw new IOException("Error occurred while transmitting message.");
                }
                System.out.println("WAITING...");
                // TODO: Have Client receive a message from the server.
                messageIn = incoming.readLine();
                // If message sent is "quit", then close the connection and streams, print Connection closed and end the program.
                if (messageIn.equalsIgnoreCase("quit")){
                    System.out.println("Connection Closed");
                    connection.close();
                    break;
                }
                System.out.println("RECEIVED MESSAGE: " + messageIn + "\n");
            }
        }
        // TODO: If failed to exchange messages, print error message, close connections and streams, and end the program.
        catch (Exception e) {
            System.out.println("Error during communication: " + e.getMessage());
        }
        
        // 4. Close the connection and end the program, whether error or not.
        finally {
            userInput.close();
            try {
                connection.close();
            } catch (Exception e) {
                // Ignore errors that occur while closing the connection.
                System.out.println("Communication error: " + e.getMessage());

            }
        }

    } // end main()

} // end class ChatClient