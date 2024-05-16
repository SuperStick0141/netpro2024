import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class NekoServer 
{
    private static String serverProcess(String content) 
    {
        String Retu="「";

        

        Random random = new Random(0);
        for(int N=0;N<content.length();N+=2)
        {
            String Ren = content.substring(N, Math.min(N + 2, content.length()));
            if(Ren.contains("。")){Retu+="。";}
            else if(Ren.contains("、")){Retu+="、";}
            else if(Ren.contains("！")){Retu+="！";}
            else if(Ren.contains("？")){Retu+="？";}
            else
            {
                int R=random.nextInt(3);
                if(R==0){Retu+="にゃ";}
                else if(R==1){Retu+="にゃー";}
                else if(R==2){Retu+="にゃん";}
            }
        }
        Retu+="」";
        return Retu;
    }

    public static void main(String arg[]) {
        try {
            /* 通信の準備をする */
            Scanner scanner = new Scanner(System.in);
            System.out.print("ポートを入力してください(5000など) → ");
            int port = scanner.nextInt();
            scanner.close();
            System.out.println("localhostの" + port + "番ポートで待機します");
            ServerSocket server = new ServerSocket(port); // ポート番号を指定し、クライアントとの接続の準備を行う

            Socket socket = server.accept(); // クライアントからの接続要求を待ち、
            // 要求があればソケットを取得し接続を行う
            System.out.println("接続しました。相手の入力を待っています......");

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            NekoReturn language = (NekoReturn) ois.readObject();// Integerクラスでキャスト。

            String msgReturn = language.getMessage();
            System.out.println("相手からのあいさつ\n" + msgReturn);
            String languageFromClient = language.getContent();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
            System.out.println("猫語で喋る言葉は" + languageFromClient);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            NekoReturn response = new NekoReturn();
            response.setMessage("サーバーです。あなたから送られた言葉は・・・\n" + languageFromClient + "\n猫語に変換しますと、");
            response.setContent(serverProcess(languageFromClient));

            oos.writeObject(response);
            oos.flush();

            // close処理

            ois.close();
            oos.close();
            // socketの終了。
            socket.close();
            server.close();

        } // エラーが発生したらエラーメッセージを表示してプログラムを終了する
        catch (BindException be) {
            be.printStackTrace();
            System.out.println("ポート番号が不正、ポートが使用中です");
            System.err.println("別のポート番号を指定してください(6000など)");
        } catch (Exception e) {
            System.err.println("エラーが発生したのでプログラムを終了します");
            throw new RuntimeException(e);
        }
    }
}
