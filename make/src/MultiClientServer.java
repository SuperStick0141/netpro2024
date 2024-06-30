import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiClientServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private int currentClientIndex = 0;

    public MultiClientServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        System.out.println("Server started.");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                if (clients.size() < 2) {
                    ClientHandler clientHandler = new ClientHandler(socket, clients.size());
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                } else {
                    System.out.println("Maximum clients reached. Server will not accept more clients.");
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void broadcast(String message, int senderIndex) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
        currentClientIndex = (currentClientIndex + 1) % clients.size();
        notifyAll();
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private int clientIndex;

        public ClientHandler(Socket socket, int clientIndex) {
            this.socket = socket;
            this.clientIndex = clientIndex;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                writer.println("You are client " + (clientIndex + 1));
                synchronized (MultiClientServer.this) {
                    while (clients.size() < 2) {
                        MultiClientServer.this.wait();
                    }
                }

                String message;
                while ((message = reader.readLine()) != null) {
                    synchronized (MultiClientServer.this) {
                        while (currentClientIndex != clientIndex) {
                            MultiClientServer.this.wait();
                        }
                        System.out.println("Received message from client " + (clientIndex + 1) + ": " + message);
                        broadcast("Client " + (clientIndex + 1) + ": " + message, clientIndex);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    synchronized (MultiClientServer.this) {
                        clients.remove(this);
                        if (clients.size() < 2) {
                            currentClientIndex = 0;
                            MultiClientServer.this.notifyAll();
                        }
                    }
                    System.out.println("Client disconnected: " + socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMessage(String message) {
            writer.println(message);
        }
    }

    public static void main(String[] args) {
        int port = 12345; // ポート番号を適宜変更する
        MultiClientServer server = new MultiClientServer(port);
        server.startServer();
    }
}