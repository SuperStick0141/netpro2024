import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatServer 
{
    static int PlayerCount=0;
    static int NextCount=0;
    static int GamePhase=0;
    static int[][] EnemyStates=new int[][]
    {
        new int[]{/*HP*/3,/*ATK*/1,/*DROP*/1},//ウサギA
        new int[]{/*HP*/5,/*ATK*/1,/*DROP*/2},//B
        new int[]{/*HP*/6,/*ATK*/1,/*DROP*/3},//C
        new int[]{/*HP*/7,/*ATK*/2,/*DROP*/4}//D
    };
    private static List<ClientHandler> clients = new ArrayList<>();
    private static Random random = new Random();



    public static void main(String[] args) 
    {
        final int PORT = 12345;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and waiting for connections...");

            // クライアントからの接続を待ち、接続があれば新しいスレッドを開始
            while (true) {
                Socket clientSocket = serverSocket.accept();
                if(PlayerCount<2)
                {
                System.out.println("New Player : " + clientSocket);
                PlayerCount++;
                // 新しいスレッドを開始し、クライアントハンドラを実行
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
                clients.add(clientHandler);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),StandardCharsets.UTF_8), true);
                out.println("\nゲームに参加しました 名前は[Player"+PlayerCount+"]です。");
                if(PlayerCount==2){Game(GamePhase,0);}
                }
                else
                {
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),StandardCharsets.UTF_8), true);
                    out.println("参加できませんでした＾＾;.");
                    clientSocket.close();
                }
            }
        } catch (Exception e) {e.printStackTrace();}
    }




    private static void Game(int Phase,int Turn)
    {
        GamePhase=Phase;
        int PlNum=0;
        if(Phase==0)
        {//各スキル決め
            GmAllMessage("[GM] 集まったのでゲームを開始します");
            GmAllMessage("[GM] まずはプレイヤーごとにステータスを設定します。");
            for(ClientHandler client : clients){//各プレイヤーのスキル設定
            synchronized (ChatServer.class) {NextCount = 0;}
            GmSubMessage("[GM] あなたのステータスを変更します","・プレイヤー"+(PlNum+1)+"の番です",PlNum);
            GmOneMessage("[GM] まずはスキルを選択します。スキル内容は以下。\n"+
            "スキル①:以下同じような形式で羅列\n"+
            "\nこれらから選んでください。数字1~8で回答",PlNum);
            client.CanMove=true; 
            synchronized (ChatServer.class) {while(NextCount==0){try{ChatServer.class.wait();}catch(InterruptedException e){}}}
            GmOneMessage("[GM] もう一つです。",PlNum);
            synchronized (ChatServer.class) {while(NextCount==1){try{ChatServer.class.wait();}catch(InterruptedException e){}}}
            GmOneMessage("[GM] 完了です。",PlNum);
            client.setHP(10);
            client.CanMove=false; PlNum++;
        }
            GmAllMessage("[GM] 全員決まったのでターンスタートです。");
            Game(1,1);
        }
        else if(Phase==1)
        {
            boolean GameFinish=true;
            for(ClientHandler client : clients){if(client.getHP()<=0){GameFinish=false; Game(2,0);}}
            if(GameFinish)
            {
            GmAllMessage("\n\n[GM] 一日が始まります。"+Turn+"日目です。");
            if(true){/* 環境を左右する時はここ */GmAllMessage("\n\n[GM] 今日は晴天です。");}
            GmAllMessage("[GM] 各プレイヤーは食料を調達するか、他の作業をするか選択できます。");
            for(ClientHandler client : clients)
            {//各プレイヤーの行動
                for(ClientHandler Checkclient : clients){if(Checkclient.getHP()<=0){GameFinish=false; Game(2,0);}}
                if(GameFinish)
                {
                synchronized (ChatServer.class) {NextCount = 0;}
                GmSubMessage("\n[GM] あなたの行動を選択してください","・プレイヤー"+(PlNum+1)+"の番です",PlNum);
                GmOneMessage("獲物を探す場合は[1]、休憩は[2]です\n",PlNum);
                client.CanMove=true; 
                synchronized (ChatServer.class) {while(NextCount==0){try{ChatServer.class.wait();}catch(InterruptedException e){}}}
                if(NextCount==1){
                    GmOneMessage("獲物を探します\n",PlNum);
                    int Enemy =random.nextInt(3);
                    String EnemyName="ウサギ";
                    if(Enemy==0){EnemyName="ウサギA";}
                    else if(Enemy==1){EnemyName="ウサギB";}
                    else if(Enemy==2){EnemyName="ウサギC";}
                    else if(Enemy==3){EnemyName="ウサギD";}
                    GmOneMessage("...\n"+EnemyName+"に遭遇しました！",PlNum);
                    for(int HP=EnemyStates[Enemy][0];HP>0;)
                    {
                        synchronized (ChatServer.class) {NextCount = 1;}
                        if(client.getHP()<=0){GmSubMessage(EnemyName+"に敗北した...","・プレイヤー"+(PlNum+1)+"が"+EnemyName+"に敗北!",PlNum); break;}
                        GmOneMessage("攻撃(1)か撤退(2)か選んでください",PlNum);
                        synchronized (ChatServer.class) {while(NextCount==1){try{ChatServer.class.wait();}catch(InterruptedException e){}}}
                        if(NextCount==11)
                        {
                            GmOneMessage("攻撃を行います",PlNum);
                            int Damage =random.nextInt(3);
                            HP-=Damage;
                            GmOneMessage(EnemyName+"に"+Damage+"のダメージ!!  残り"+HP,PlNum);
                            if(HP<=0)
                            {
                                GmSubMessage(EnemyName+"を撃破した！","・プレイヤー"+(PlNum+1)+"が"+EnemyName+"を撃破!",PlNum);
                                client.getMeat(EnemyStates[Enemy][2]);
                                GmOneMessage("お肉を"+EnemyStates[Enemy][2]+"つ獲得",PlNum);
                                break;
                            }
                            GmOneMessage("相手が攻撃を行います",PlNum);
                            client.getDamage(EnemyStates[Enemy][1]);
                            GmOneMessage(EnemyStates[Enemy][1]+"のダメージ!!\n現在の体力は"+client.getHP()+"です。",PlNum);
                        }
                        else if(NextCount==12)
                        {
                            GmOneMessage("逃亡します",PlNum);
                            int Ran=random.nextInt(1);
                            if(Ran==0){HP=-10;  GmSubMessage(EnemyName+"から逃げ切った","・プレイヤー"+(PlNum+1)+"が"+EnemyName+"から逃走!",PlNum);}
                            else
                            {
                                GmOneMessage("逃亡に失敗しました",PlNum);
                                GmOneMessage("相手が攻撃を行います",PlNum);
                                client.getDamage(EnemyStates[Enemy][1]);
                                GmOneMessage(EnemyStates[Enemy][1]+"のダメージ!!\n現在の体力は"+client.getHP()+"です。",PlNum);
                            }
                        }
                    }
                }
                else if(NextCount==2){
                    GmOneMessage("休憩します\n",PlNum);
                    client.EatMeat();
                    GmOneMessage("現在の体力は"+client.getHP()+"です。",PlNum);
                }
                client.CanMove=false; PlNum++;
            }
            }
            Game(1,Turn+=1);
           }
        }
        else if(Phase==2){GmAllMessage("\n\n[GM] ゲーム終了です。");}
    }

    private static void GmAllMessage(String Info)
    {for (ClientHandler client : clients) {client.sendMessage(Info);}}
    private static void GmOneMessage(String Info,int PlayerN)
    {
        int This=0;
        for (ClientHandler client : clients) 
        {if(This==PlayerN){client.sendMessage(Info);} This++;}
    }
    private static void GmSubMessage(String Info,String SubInfo,int PlayerN)
    {
        int This=0;
        for (ClientHandler client : clients) 
        {if(This==PlayerN){client.sendMessage(Info);}else{client.sendMessage(SubInfo);} This++;}

    }








    // クライアントハンドラクラス
    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private int PlayerNum;
        private boolean CanMove;

        //ゲーム用
        private int SkillA;
        private int SkillB;
        private int HP;
        private int Meat;

        private PrintWriter out; // クライアントへの出力用
        private BufferedReader in; // クライアントからの入力用

        // コンストラクタ
        public ClientHandler(Socket socket) {this.clientSocket = socket; this.PlayerNum=PlayerCount; CanMove=false;}

        @Override
        public void run() 
        {
            try 
            {
                // クライアントからの入出力ストリームを作成
                out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true); // 自動フラッシュ有効
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

                // クライアントからのメッセージを受信し、全クライアントにブロードキャスト
                String inputLine;
                while ((inputLine = in.readLine()) != null) //プレイヤーからの行動受信
                { 
                    if(CanMove)
                    {
                        try
                        {
                            String Return = inputLine;
                            // クライアントからのメッセージを全クライアントに送信
                            //out.println("Server: " + Return);
                            if(GamePhase==0&&NextCount==0){
                                int Check=Integer.parseInt(inputLine); 
                                if(Check>0 && Check<9)
                                {broadcast(inputLine+"を選択"); SkillA=Check; NextNum(1);}  
                                else{out.println("数値が範囲外です。");}                         
                            }
                            else if(GamePhase==0&&NextCount==1){
                                int Check=Integer.parseInt(inputLine); 
                                if(Check>0 && Check<9 && Check!=SkillA)
                                {broadcast(inputLine+"を選択"); SkillB=Check; NextNum(2);}  
                                else{out.println("数値が範囲外です。");}                         
                            }
                            else if(GamePhase==1&&NextCount==0){
                                
                                int Check=Integer.parseInt(inputLine); 
                                String Info="休憩";
                                if(Check==1 || Check==2)
                                {if(Check==1){Info="調達";} broadcast(Info+"を選択");  NextNum(Check);}  
                                else{out.println("数値が範囲外です。");}   
                            }
                            else if(GamePhase==1&&NextCount==1){
                                int Check=Integer.parseInt(inputLine); 
                                if(Check==1 || Check==2){NextNum(Check+10);}  
                                else{out.println("数値が範囲外です。");}   
                            }
                        }
                        catch(Exception e){broadcast("[Player "+PlayerNum+"]   " + inputLine);}
                    }
                    else{out.println("[GM] 現在あなたのターンではないです");}

                }
            } 
            catch (Exception e) {e.printStackTrace();} 
            finally 
            {
                try {clientSocket.close();/*  接続を閉じる*/} 
                catch (Exception e) {e.printStackTrace();}
            }
        }

        private void broadcast(String message) {
            synchronized (clients) {
                for (ClientHandler client : clients) 
                {
                    client.sendMessage(message);
                }
            }
        }
        private void sendMessage(String message) {
            out.println(message);
        }
        private void NextNum(int A)
        {
            synchronized (ChatServer.class) 
            {
                NextCount=A;
                ChatServer.class.notifyAll();
            }
        }
        
        public void getMeat(int GMeat){Meat+=GMeat;}
        public void getDamage(int D){HP-=D;}
        public void setHP(int H){HP=H;}
        public int getHP(){return HP;}
        public void EatMeat(){ if(Meat>0){Meat--; HP+=3;}}
    }
}
