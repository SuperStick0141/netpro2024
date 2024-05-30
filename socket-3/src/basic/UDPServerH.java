package basic;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServerH {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String receivedString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received: " + receivedString);

                String upperString = receivedString.toUpperCase();
                byte[] sendData = upperString.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket);
                System.out.println("Sent: " + upperString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}