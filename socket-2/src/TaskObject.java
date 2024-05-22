import java.io.Serializable;

public class TaskObject implements Serializable
{
    private int number;
    private int result;
    public interface ITask
    {
        public void setExecNumber(int x); //クライアントで最初に計算させる数字を入力しておく関数
        public void exec() ; //サーバで計算を実行をさせる関数...計算アルゴリズムが記載される。下記アルゴリズムを参照のこと
        public int getResult(); //クライアントで結果を取り出す関数
    }

    public void setExecNumber(int x) {
        this.number = x;
    }
    public void exec() 
    {
        for (int i = number; i >= 2; i--) 
        {
            boolean PCheck = true;
            if (i <= 1) PCheck = false;
            if (i <= 3) PCheck = true;
            if (i % 2 == 0 || i % 3 == 0) PCheck = false;
            for (int N = 5; N * N <= i; N += 6) {
                if (i % N == 0 || i % (N + 2) == 0) PCheck = false;
            }

            if(PCheck){this.result = i;}

        }
    }
    public int getResult() {
        return result;
    }
}
