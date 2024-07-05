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
        new int[]{/*HP*/5,/*ATK*/1,/*DROP*/1},//B
        new int[]{/*HP*/6,/*ATK*/2,/*DROP*/2},//C
        new int[]{/*HP*/7,/*ATK*/2,/*DROP*/3}//D
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
            "スキル①:頑強 (プレイヤーの最大体力+2)\n"+
            "スキル②:勇猛 (プレイヤーの攻撃力+2)\n"+
            "スキル③:解体 (プレイヤーのお肉獲得量+1)\n"+
            "スキル④:俊足 (逃走が確実に成功)\n"+
            "スキル⑤:再生 (休憩の回復量+1)\n"+
            "スキル⑥:鉄壁 (受けるダメージ-1)\n"+
            "スキル⑦:万有 (受けるダメージ+2、与えるダメージ+2)\n"+
            "スキル⑧:暴食 (休憩で使用するお肉+1、回復量+2)\n"+
            "\nこれらから選んでください。数字1~8で回答",PlNum);
            client.CanMove=true; 
            synchronized (ChatServer.class) {while(NextCount==0){try{ChatServer.class.wait();}catch(InterruptedException e){}}}
            GmOneMessage("[GM] もう一つです。",PlNum);
            synchronized (ChatServer.class) {while(NextCount==1){try{ChatServer.class.wait();}catch(InterruptedException e){}}}
            GmOneMessage("[GM] 完了です。",PlNum);
            client.setHP();
            client.CanMove=false; PlNum++;
        }
            GmAllMessage("[GM] 全員決まったのでターンスタートです。");
            Game(1,1);
        }
        else if(Phase==1)
        {//戦闘と休憩
            boolean GameFinish=true;
            for(ClientHandler client : clients){if(client.getHP()<=0){GameFinish=false; Game(2,0);}}
            if(GameFinish)
            {//死人が居なければ一日を始める
            GmAllMessage("\n\n[GM] 一日が始まります。"+Turn+"日目です。");
            if(true)
            {
                /* 環境を左右する時はここ */
                GmAllMessage("\n\n[GM] 今日は晴天です。");
                if(Turn>3)
                {
                    int Ran=random.nextInt(10);
                    if(Turn>Ran)
                    {
                        GmAllMessage("\n\n[GM] 時間が経ちすぎなのでウサギが強くなります");
                        int RanP=random.nextInt(10);
                        if(RanP>2){EnemyStates[0][0]+=1;EnemyStates[1][0]+=1;EnemyStates[2][0]+=1;EnemyStates[3][0]+=1;}
                        else{EnemyStates[0][1]+=1;EnemyStates[1][1]+=1;EnemyStates[2][1]+=1;EnemyStates[3][1]+=1;}
                    }
                }
            }
            GmAllMessage("[GM] 各プレイヤーは食料を調達するか、他の作業をするか選択できます。");
            for(ClientHandler client : clients)
            {//各プレイヤーの行動
                for(ClientHandler Checkclient : clients){if(Checkclient.getHP()<=0){GameFinish=false; Game(2,0);}}
                if(GameFinish)
                {//死人が居なければ行動を行う
                synchronized (ChatServer.class) {NextCount = 0;}
                GmSubMessage("\n[GM] あなたの行動を選択してください","・プレイヤー"+(PlNum+1)+"の番です",PlNum);
                GmOneMessage("獲物を探す場合は[1]、休憩は[2]です",PlNum);
                client.CanMove=true; 
                synchronized (ChatServer.class) {while(NextCount==0){try{ChatServer.class.wait();}catch(InterruptedException e){}}}
                if(NextCount==1)
                {//戦闘開始
                    GmOneMessage("獲物を探します\n",PlNum);
                    int Enemy =random.nextInt(3);
                    String EnemyName="ウサギ";
                    if(Enemy==0){EnemyName="ウサギA";}
                    else if(Enemy==1){EnemyName="ウサギB";}
                    else if(Enemy==2){EnemyName="ウサギC";}
                    else if(Enemy==3){EnemyName="ウサギD";}
                    GmOneMessage("...\n"+EnemyName+"に遭遇しました！",PlNum);
                    for(int HP=EnemyStates[Enemy][0];HP>0;)
                    {//獲物の体力を削るまで戦闘　敗北時にループ解除
                        synchronized (ChatServer.class) {NextCount = 1;}
                        if(client.getHP()<=0){GmSubMessage(EnemyName+"に敗北した...","・プレイヤー"+(PlNum+1)+"が"+EnemyName+"に敗北!",PlNum); break;}
                        GmOneMessage("攻撃(1)か撤退(2)か選んでください",PlNum);
                        synchronized (ChatServer.class) {while(NextCount==1){try{ChatServer.class.wait();}catch(InterruptedException e){}}}
                        if(NextCount==11)
                        {//攻撃→撃破時に終了、失敗で相手の攻撃
                            GmOneMessage("攻撃を行います",PlNum);
                            int MaxDamage=3;
                            if(client.retSkillA()==2||client.retSkillB()==2){MaxDamage+=2;}                            
                            int Damage =random.nextInt(MaxDamage);
                            if(client.retSkillA()==7||client.retSkillB()==7){Damage+=2;}
                            HP-=Damage;
                            GmOneMessage(EnemyName+"に"+Damage+"のダメージ!!  残り"+HP,PlNum);
                            if(HP<=0)
                            {//撃破。戦闘終了。肉取得。
                                GmSubMessage(EnemyName+"を撃破した！","・プレイヤー"+(PlNum+1)+"が"+EnemyName+"を撃破!",PlNum);
                                int PlusMeat = 0;
                                if(client.retSkillA()==3||client.retSkillB()==3){PlusMeat=1;}
                                client.getMeat(EnemyStates[Enemy][2]+PlusMeat);
                                GmOneMessage("お肉を"+(EnemyStates[Enemy][2]+PlusMeat)+"つ獲得",PlNum);
                                break;
                            }
                            GmOneMessage("相手が攻撃を行います",PlNum);
                            int ThatDamage=EnemyStates[Enemy][1];
                            if(client.retSkillA()==6||client.retSkillB()==6){ThatDamage=ThatDamage-1;}
                            if(client.retSkillA()==7||client.retSkillB()==7){ThatDamage=ThatDamage+2;}
                            client.getDamage(ThatDamage);
                            GmOneMessage(ThatDamage+"のダメージを受けた!!\n現在の体力は"+client.getHP()+"です。",PlNum);
                        }
                        else if(NextCount==12)
                        {//逃亡→成功したらループ解除。得る物無。失敗時に相手の攻撃
                            GmOneMessage("逃亡します",PlNum);
                            int Ran=random.nextInt(1);
                            if(client.retSkillA()==4||client.retSkillB()==4){Ran=0;}
                            if(Ran==0){HP=-10;  GmSubMessage(EnemyName+"から逃げ切った","・プレイヤー"+(PlNum+1)+"が"+EnemyName+"から逃走!",PlNum);}
                            else
                            {//相手の攻撃
                                GmOneMessage("逃亡に失敗しました",PlNum);
                                GmOneMessage("相手が攻撃を行います",PlNum);
                                int ThatDamage=EnemyStates[Enemy][1];
                                if(client.retSkillA()==6||client.retSkillB()==6){ThatDamage=ThatDamage-1;}
                                if(client.retSkillA()==7||client.retSkillB()==7){ThatDamage=ThatDamage+2;}
                                client.getDamage(ThatDamage);
                                GmOneMessage(ThatDamage+"のダメージを受けた!!\n現在の体力は"+client.getHP()+"です。",PlNum);
                            }
                        }
                    }
                }
                else if(NextCount==2)
                {//休憩開始
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
        else if(Phase==2)
        {//結果表示
            GmAllMessage("\n\n[GM] 誰かが力尽きたため、ゲーム終了となります！");
            GmAllMessage("[GM] ここから、結果を発表します！");

            int MaxNum=0;
            int PlayerNum=0;
            List<Integer> Used = new ArrayList<>();
            GmAllMessage("---お肉所持量---");
            for(int N=0;N<clients.size();N++)
            {
                for(ClientHandler Check : clients){if(MaxNum<Check.HaveMeat() && !Used.contains(Check.RetPlayer())) {MaxNum=Check.HaveMeat(); PlayerNum=Check.RetPlayer();}}
                GmAllMessage( (N+1)+"位   Player["+PlayerNum+"]   "+MaxNum+"個");
                Used.add(PlayerNum);
                MaxNum=0;
                PlayerNum=0;
            }
            MaxNum=-99;
            Used = new ArrayList<>();
            GmAllMessage("---現在体力---");
            for(int N=0;N<clients.size();N++)
            {
                for(ClientHandler Check : clients){if(MaxNum<Check.getHP() && !Used.contains(Check.RetPlayer())) {MaxNum=Check.getHP(); PlayerNum=Check.RetPlayer();}}
                GmAllMessage( (N+1)+"位   Player["+PlayerNum+"]   "+MaxNum);
                Used.add(PlayerNum);
                MaxNum=-99;
                PlayerNum=0;
            }
            for(ClientHandler client : clients){client.CanMove=true;}
            GmAllMessage("\n\n--ゲーム終了.自由に会話をどうぞ--\n");
            return;
        }
    }


    //全体に向かってアナウンス
    private static void GmAllMessage(String Info)
    {for (ClientHandler client : clients) {client.sendMessage(Info);}}
    //指定した個人に対してアナウンス
    private static void GmOneMessage(String Info,int PlayerN)
    {
        int This=0;
        for (ClientHandler client : clients) {if(This==PlayerN){client.sendMessage(Info);} This++;}
    }
    //指定した個人とその他で別のアナウンス内容
    private static void GmSubMessage(String Info,String SubInfo,int PlayerN)
    {
        int This=0;
        for (ClientHandler client : clients) {if(This==PlayerN){client.sendMessage(Info);}else{client.sendMessage(SubInfo);} This++;}
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
        private int HPMax=10;
        private int Meat;


        private PrintWriter out; // クライアントへの出力用
        private BufferedReader in; // クライアントからの入力用
        public ClientHandler(Socket socket) {this.clientSocket = socket; this.PlayerNum=PlayerCount; CanMove=false;}// コンストラクタ

        @Override
        public void run() 
        {
            try 
            {
                // クライアントからの入出力ストリームを作成
                out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true); // 自動フラッシュ有効
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                while ((inputLine = in.readLine()) != null) 
                { //プレイヤーからの行動受信
                    if(CanMove)
                    {//行動可能状態(自身のターン中であるか)の時に判定
                        try
                        {//各行動
                            String Return = inputLine;
                            if(GamePhase==0&&NextCount==0)
                            {//プレイヤーのスキル設定一つ目
                                int Check=Integer.parseInt(inputLine); 
                                if(Check>0 && Check<9)
                                {
                                    broadcast(inputLine+"を選択"); SkillA=Check; 
                                    NextNum(1);
                                }  
                                else{out.println("数値が範囲外です。");}                         
                            }
                            else if(GamePhase==0&&NextCount==1)
                            {//プレイヤーのスキル設定二つ目
                                int Check=Integer.parseInt(inputLine); 
                                if(Check>0 && Check<9 && Check!=SkillA)
                                {broadcast(inputLine+"を選択"); SkillB=Check; NextNum(2);}  
                                else{out.println("数値が範囲外です。");}                         
                            }
                            else if(GamePhase==1&&NextCount==0)
                            {//戦闘か休憩か選択(お肉が足りない場合は強制戦闘)
                                int Check=Integer.parseInt(inputLine); 
                                String Info="休憩";
                                if(Check==1 || Check==2 )
                                {
                                    if(Check==1){Info="調達";} broadcast(Info+"を選択");  
                                    if(Check==2&&!CanRest()){out.println("...しかしお肉が足りなかった!!!\n強制的に戦闘を行います。"); Check=1;}
                                    NextNum(Check);
                                }  
                                else{out.println("数値が範囲外です。");}   
                            }
                            else if(GamePhase==1&&NextCount==1)
                            {//戦闘時の行動選択(攻撃か逃亡)
                                int Check=Integer.parseInt(inputLine); 
                                if(Check==1 || Check==2){NextNum(Check+10);}  
                                else{out.println("数値が範囲外です。");}   
                            }
                            else if(GamePhase==2){broadcast("[Player "+PlayerNum+"]   " + inputLine);}
                        }
                        catch(Exception e)
                        {//形式外のコメントは「プレイヤ―のなり切ったセリフとして全体に表示」
                            broadcast("[Player "+PlayerNum+"]   " + inputLine);
                        }
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

        //クライアントから全体にメッセージを送る物(OneMessageと統合可能な気がする)
        private void broadcast(String message) 
        {
            synchronized (clients) {
                for (ClientHandler client : clients) 
                {
                    client.sendMessage(message);
                }
            }
        }
        //対象にメッセージを送信
        private void sendMessage(String message) {out.println(message);}
        //クライアントハンドル側からサーバーの進行度数値を変更
        private void NextNum(int A)
        {
            synchronized (ChatServer.class) 
            {
                NextCount=A;
                ChatServer.class.notifyAll();
            }
        }
        

        //以降ゲーム内数値の取得、変更
        public int RetPlayer() {return PlayerNum;} 
        public void getMeat(int GMeat){Meat+=GMeat;}
        public int HaveMeat(){return Meat;}
        public void getDamage(int D){if(D<0){D=0;} HP-=D;}
        public void setHP(){HP=HPMax;}
        public int getHP(){return HP;}
        public boolean CanRest() 
        {
            if(SkillA==8||SkillB==8){if(Meat<=1){return false;}else{return true;}}
            else{if(Meat<=0){return false;}else{return true;}}        
        }
        public void EatMeat()
        { 
            if(SkillA==8||SkillB==8){if(Meat>1){Meat-=2; HP+=5;if(SkillA==5||SkillB==5){HP+=1;} if(HP>HPMax){HP=HPMax;}}}
            else{if(Meat>0){Meat--; HP+=3;if(SkillA==5||SkillB==5){HP+=1;} if(HP>HPMax){HP=HPMax;}}}
        }
        public int retSkillA() {return SkillA;}
        public int retSkillB() {return SkillB;}
    }
}
