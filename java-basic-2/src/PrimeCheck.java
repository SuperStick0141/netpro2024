public class PrimeCheck 
{
    

    public static void main(String[] args) 
    {
        int[] PrimeCount = new int[] {0,0,0,0};//1.3.7.9
        int[][] PrimePair = new int[4][4];
        int   PrimeWait=0;
        for(int N=3;N<100000;N++)
        {
            boolean PrimeCheck = true;
            for(int M=3;M<N;M++) { if(N%M==0) {PrimeCheck = false;}}
            if(PrimeCheck) 
            {
                String PrimeOne = OnePick(N);
                if(PrimeOne.contains("1")){PrimeCount[0]++;}
                else if(PrimeOne.contains("3")){PrimeCount[1]++;}
                else if(PrimeOne.contains("7")){PrimeCount[2]++;}
                else if(PrimeOne.contains("9")){PrimeCount[3]++;}
            if(PrimeWait==0){PrimeWait=N;}
            else
            {
                int PriOne = 0;
                String PrimeOneOne = OnePick(PrimeWait);
                if(PrimeOneOne.contains("1")){PriOne=0;}
                else if(PrimeOneOne.contains("3")){PriOne=1;}
                else if(PrimeOneOne.contains("7")){PriOne=2;}
                else if(PrimeOneOne.contains("9")){PriOne=3;}
                int PriTwo = 0;
                String PrimeOneTwo = OnePick(N);
                if(PrimeOneTwo.contains("1")){PriTwo=0;}
                else if(PrimeOneTwo.contains("3")){PriTwo=1;}
                else if(PrimeOneTwo.contains("7")){PriTwo=2;}
                else if(PrimeOneTwo.contains("9")){PriTwo=3;}
                PrimePair[PriOne][PriTwo]++;
                PrimeWait=0;
            }
            }
        }
        for(int N=0;N<4;N++)
        {
            System.out.println(ReturnPoint(N)+"の数は"+PrimeCount[N]+"個");
        }
        System.out.println("以下は組み合わせのランキングです");

        int Max=0;
        int Before=9999999;
        int NN=0;
        int MM=0;
        for(int I=1;I<17;I++)
        {
            for(int N=0;N<4;N++){ for(int M=0;M<4;M++)  { if(PrimePair[N][M]<Before && PrimePair[N][M]>Max) {Max=PrimePair[N][M];NN=N;MM=M;}}}
            System.out.println(I+"位 "+ReturnPoint(NN)+"-"+ReturnPoint(MM)+"の組み合わせで"+PrimePair[NN][MM]+"個" );
            Before=Max;
            Max=0;
        }
        
        
	}
    private static String OnePick(int Check)//数値の下一桁を出す
    {
        String Str = Integer.toString(Check);
        String Pick = Str.substring(Str.length()-1,Str.length());
        return Pick;
    }
    private static int ReturnPoint(int Check)
    {
        if(Check==0){return 1;}
        else if(Check==1){return 3;}
        else if(Check==2){return 7;}
        else if(Check==3){return 9;}
        else {return 0;}
    }
}
