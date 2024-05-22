import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.Socket;
import java.util.Scanner;

public class TaskClientWhile {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("ポートを入力してください(5000など) → ");
            int port = scanner.nextInt();

            while (true) {
                System.out.println("localhostの" + port + "番ポートに接続を要求します");
                Socket socket = new Socket("localhost", port);
                System.out.println("接続されました");

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("入力された数値までの最大素数を計算します。数値を入力してください");
                int number = scanner.nextInt();

                TaskObject task = new TaskObject();
                task.setExecNumber(number);

                oos.writeObject(task);
                oos.flush();

                if (number <= 1) {
                    System.out.println("クライアントを終了します...");
                    oos.close();
                    socket.close();
                    break;
                }

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                TaskObject resultTask = (TaskObject) ois.readObject();
                int result = resultTask.getResult();
                System.out.println("計算結果は: " + result);

                ois.close();
                oos.close();
                socket.close();
            }
            scanner.close();

        } catch (BindException be) {
            be.printStackTrace();
            System.err.println("ポート番号が不正か、サーバが起動していません");
            System.err.println("サーバが起動しているか確認してください");
            System.err.println("別のポート番号を指定してください(6000など)");
        } catch (Exception e) {
            System.err.println("エラーが発生したのでプログラムを終了します");
            throw new RuntimeException(e);
        }
    }
}