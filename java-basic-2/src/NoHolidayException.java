public class NoHolidayException  extends Exception
{
    @Override
    public void printStackTrace() {
        super.printStackTrace();
        System.err.println(" この日は休日ではないです。");
    }
}
