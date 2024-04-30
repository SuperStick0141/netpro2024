import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class XmasTreeKadai 
{
    public static void main(String[] args) 
    { 
        int MaxLeaf = 19;
        int TreeThi = 3; 
        int TreeLen = 7;
        String Snow = "+";


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


 try{
    while(true)
    {
        System.out.println("\n木の大きさ、幹の大きさ、木の長さ、雪の柄を入力してください。\n 形式例は 18/3/7/0~2 です \n 終了する場合はqまたはeを入力して下さい");
        String line="";
        line = reader.readLine();
        if(line.equals("q")||line.equals("e"))
        {
            System.out.println("終了します。");
            break;
        }
        String[] Spl = line.split("/");

        MaxLeaf = Integer.parseInt(Spl[0]);
        TreeThi = Integer.parseInt(Spl[1]);
        TreeLen = Integer.parseInt(Spl[2]);
        if(Integer.parseInt(Spl[2])==0){Snow="@";}
        if(Integer.parseInt(Spl[2])==1){Snow="+";}
        if(Integer.parseInt(Spl[2])==2){Snow=".";}
        else {Snow="o";}
    if(MaxLeaf<4 || MaxLeaf>40 || TreeThi<1 || TreeThi>MaxLeaf || TreeLen<1){System.out.println("形式が違ったり、数値が既定の範囲に無いです");}
    else{
    for(int N=0;N<MaxLeaf+TreeLen;N++)
    {
        String Line="";
        if(N<MaxLeaf)
        {
            for(int M=0;M<MaxLeaf*2;M++)
            {
                if(M>=MaxLeaf-N && M<=MaxLeaf+N-1){Line+="*";}
                else if((M<MaxLeaf-N-1 && (M+N)%2==1) ||( M>MaxLeaf+N && (M+N)%2==0)) {Line+=Snow;}
                else {Line+=" ";}
            }
            System.out.println(Line);
        }
        else
        {
            for(int M=0;M<MaxLeaf*2;M++)
            {
                if(M>=(MaxLeaf-TreeThi/2)-1 && M<=(MaxLeaf+TreeThi/2)-1){Line+="*";}
                else {Line+=" ";}
            }
            System.out.println(Line);
        }

    }
    System.out.println(" ");
    }
}


    }
	catch(IOException e) {System.out.println(e);}
    }
}
