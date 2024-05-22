public class DinnerFullCourse {

	private Dish[] list = new Dish[5];// [0]-[4]の計5個

	public static void main(String[] args) 
    {
		DinnerFullCourse fullcourse = new DinnerFullCourse();
		fullcourse.eatAll();
	}

	DinnerFullCourse() 
    {
        for(int N =0;N<5;N++)
        {
            list[N]=new Dish();
        }

		list[0].setName("からあげ");list[0].setValune(5);
		list[1].setName("ハンバーグ");list[1].setValune(1);
		list[2].setName("とんかつ");list[2].setValune(2);
		list[3].setName("おひたし");list[3].setValune(20);
		list[4].setName("きんぴらごぼう");list[4].setValune(20);
	}

	void eatAll() 
    {
		String DinnerSt = "";

		for (Dish Food : list) {
			DinnerSt += Food.getName() + "(" + Food.getValune() + ")、";
		}

		System.out.println("今日のご飯は！！！\n"+DinnerSt+"\nでーーーーーす！！！！！");
	}

}