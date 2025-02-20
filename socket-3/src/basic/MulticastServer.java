package basic;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {
    public static void main(String[] args) {
        try {
            // マルチキャストグループのIPアドレスとポート番号を指定
            // 自分のPC内でやる場合
            InetAddress group = InetAddress.getByName("224.0.0.1");
            // 隣の人とやる場合
            // InetAddress group = InetAddress.getByName("239.0.0.1");
            int port = 12345;

            // マルチキャストソケットを作成し、指定したグループとポートに参加
            MulticastSocket multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group); // マルチキャストグループに参加

            System.out.println("Server started. Waiting for commands...");

            // 受信用のバッファを作成
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // コマンドを受信して永遠に待機
            while (true) {
                multicastSocket.receive(packet);
                String receivedCommand = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received command: " + receivedCommand);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
