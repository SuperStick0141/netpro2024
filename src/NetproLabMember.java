import java.util.Random;

public class NetproLabMember 
{
    public static void main(String[] args) 
    {

        Random Ran = new Random();
        int[][] ThisInfo = new int[15][4];
        double[] Result= new double[15];
        for(int N=0;N<15;N++)
        {
            //学生の総数は
            ThisInfo[N][0]=110 + Ran.nextInt(21)-10;
            //女子の人数は
            ThisInfo[N][1]= ThisInfo[N][0] * (21+N) / 100;
            //岩井研の配属人数は
            ThisInfo[N][2]= 10 +  Ran.nextInt(7)-3;

            double Kaku=1;
            double All=1;
            double Man=1;
            if(N!=0){Kaku *= Result[N-1];}
            for(int M=0;M<ThisInfo[N][2];M++)
            {
                All *= ThisInfo[N][0]-M;
                Man *= (ThisInfo[N][0]-ThisInfo[N][1])-M;
            }
            Kaku *=    Man/All;
            Result[N]=Kaku;
        }
        for(int N=0;N<15;N++)
        {
            System.out.println((N+1)+"年目の  "+"総数は"+ThisInfo[N][0] +"人。  "+ "女子は"+ThisInfo[N][1]+"人。  "+"岩井研の受付人数は"+ThisInfo[N][2]+"人。  "+"ここまで女子が来ない確率は"+Result[N]);
        }

    }
}
