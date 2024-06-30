import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient 
{
    public static void main(String[] args) 
    {
        final String SERVER_IP = "localhost";
        final int SERVER_PORT = 12345;

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) 
        {

            Thread receiveThread = new Thread(() -> 
            {
                try 
                {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {System.out.println(serverResponse);}
                } 
                catch (Exception e) {e.printStackTrace();}
            });
            receiveThread.start();

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {out.println(userInput);}
        } 
        catch (Exception e) {e.printStackTrace();}
    }
}
