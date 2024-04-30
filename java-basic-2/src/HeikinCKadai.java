import java.util.Random;

public class HeikinCKadai 
{
	public static void main(String[] args)
    {
        Random Ran = new Random();
        final int PassScore = 80;
        class Kamoku 
        {
            int score;
        
            Kamoku(int s) {score = s; }
            public int getScore() {return score;}
        }


            Kamoku[] math = new Kamoku[100];
            for(int N=0;N<math.length;N++)
            {
                math[N]=new Kamoku(Ran.nextInt(101));
            }
    
            int All=0;
            for(int N=0;N<math.length;N++){All+=math[N].getScore();}
            int Ave=All/math.length;
            System.out.println("平均点は" + Ave + "です");
            System.out.println("合格点を" + PassScore + "とすると、合格者の点数は");
            for(int N=PassScore;N<101;N++)
            {  
                for(int M=0;M<math.length;M++)
                {
                    if(math[M].getScore()==N) { System.out.println("'"+math[M].getScore()+"'"); }
                }
            }
        
        
            }




}
