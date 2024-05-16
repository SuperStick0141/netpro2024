import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HowOldAreYou 
{
    public static void main(String[] args) { 

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			// BufferedReader というのは、データ読み込みのクラス(型)
			// クラスの変数を作るには、new を使う。

			// readLine() は、入出力エラーの可能性がある。エラー処理がないとコンパイルできない。
			//  Java では、 try{ XXXXXXXX }  catch(エラーの型 変数) { XXXXXXXXXXXXXXXXXX} と書く
		try {
            while(true)
            {
			System.out.println("\n何歳ですか?   ");
            String line="";
            
			line = reader.readLine();
            
            if(line.equals("q")||line.equals("e"))
            {
                System.out.println("終了します。");
                break;
            }

            int age = Integer.parseInt(line);
            if(age<0 || age>120){System.out.println("すみません。もう一度入力をお願いします");}
			else
            {
			System.out.println("あなたは" + age + "歳ですね。");
            String Gen="";
            if(age>=0 && age <= 6){Gen = "令和";}
            else if(age>6 && age <= 35){Gen = "平成";}
            else if(age>35 && age <= 98){Gen = "昭和";}
            else if(age>98 && age <= 112){Gen = "大正";}
            else if(age>112 && age <= 156){Gen = "明治";}
            System.out.println("という事はあなたは" + Gen+ "生まれですね。");
            System.out.println("2030年は" + (age + 6) + "歳になります。\n");
            }
            }
		}
		catch(IOException e) {
			System.out.println(e);
		}


	}
}
