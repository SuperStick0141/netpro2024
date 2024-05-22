import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class CheckLocalAddress {
    public static void main(String[] args) {
        try {
            // ネットワークインターフェースを取得
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                
                // ネットワークインターフェースに関連するIPアドレスを取得
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    
                    // IPv4アドレスのみを対象にする
                    if (inetAddress instanceof java.net.Inet4Address && !inetAddress.isLoopbackAddress()) {
                        System.out.println("IPv4 address: " + inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}