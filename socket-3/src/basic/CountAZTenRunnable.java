package basic;
public class CountAZTenRunnable implements Runnable {
    private char c;

    public void setChar(char c) {
        this.c = c;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(c + String.valueOf(i));
            try {
                Thread.sleep(100); // 少し待つことで出力が混乱しないようにする
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[26];

        for (int i = 0; i < 26; i++) {
            CountAZTenRunnable ct = new CountAZTenRunnable();
            char c = (char)(97 + i); // a = 97, b = 98, ..., z = 122
            ct.setChar(c);

            threads[i] = new Thread(ct, "th-" + c);
            threads[i].start();
        }

        for (Thread th : threads) {
            try {
                th.join(); // 全てのスレッドが終了するまで待つ
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }
}