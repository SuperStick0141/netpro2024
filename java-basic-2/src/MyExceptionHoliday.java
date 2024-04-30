import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MyExceptionHoliday
{
	public static void main(String[] args) {

		MyExceptionHoliday myE=new MyExceptionHoliday();
	}



	MyExceptionHoliday()
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
            while(true)
            {
			System.out.println("\n5月の日にちを入力してください");
            String line="";
			line = reader.readLine();
            int A=Integer.parseInt(line);
			test(A);

			System.out.println("\n5月"+A+"日は休日ですね   ");
            }
		}
		 catch (NoHolidayException e) {e.printStackTrace();}
		 catch (IOException e){System.out.println(e);}
	}

	void test(int B) throws NoHolidayException{
		if(B%7!=4 && B%7!=5 && B!=3 && B!=6){throw new NoHolidayException();}
	}
}
