import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.Socket; //ネットワーク関連のパッケージを利用する
import java.util.Scanner;


public class NekoClient 
{

    public static void main(String arg[]) {
        try {

            
            Scanner scanner = new Scanner(System.in);
            System.out.print("ポートを入力してください(5000など) → ");
            int port = scanner.nextInt();
            System.out.println("localhostの" + port + "番ポートに接続を要求します");
            Socket socket = new Socket("localhost", port);
            System.out.println("接続されました");

            System.out.println("あなたの言葉を猫語に変換してあげます！！！");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("あいさつを入力してください ↓");
            String message = scanner.next();


            
            ObjectInputStream ois=null;
            while(true)
            {
            System.out.println("\n\nしゃべった言葉を猫語に変換します ↓");
            String content = scanner.next();
            

            if(content=="end"){break;}

            NekoReturn language = new NekoReturn();
            language.setMessage(message);
            language.setContent(content);

            oos.writeObject(language);
            oos.flush();

            ois = new ObjectInputStream(socket.getInputStream());
            

            NekoReturn okaeshiLanguage = (NekoReturn) ois.readObject();

            String replayMsg = okaeshiLanguage.getMessage();
            System.out.println("私は" + replayMsg);
            String replayContent = okaeshiLanguage.getContent();
            System.out.println(replayContent + "となります！");
            }
            ois.close();
            scanner.close();
            oos.close();
            socket.close();

        } // エラーが発生したらエラーメッセージを表示してプログラムを終了する
        catch (BindException be) {
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
