import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TaskServerWhile {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("ポートを入力してください(5000など) → ");
            int port = scanner.nextInt();
            scanner.close();
            System.out.println("localhostの" + port + "番ポートで待機します");
            ServerSocket server = new ServerSocket(port);

            while (true) {
                Socket socket = server.accept();
                System.out.println("接続しました。相手の入力を待っています......");

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                TaskObject task = (TaskObject) ois.readObject();

                if (task.getResult() <= 1) {
                    System.out.println("サーバーを終了します...");
                    ois.close();
                    socket.close();
                    break;
                }

                task.exec();
                int result = task.getResult();
                System.out.println("計算結果: " + result);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(task);
                oos.flush();

                ois.close();
                oos.close();
                socket.close();
            }
            server.close();

        } catch (BindException be) {
            be.printStackTrace();
            System.out.println("ポート番号が不正、ポートが使用中です");
            System.err.println("別のポート番号を指定してください(6000など)");
        } catch (Exception e) {
            System.err.println("エラーが発生したのでプログラムを終了します");
            throw new RuntimeException(e);
        }
    }
}