class Renshu 
{

    // xを2倍にして返す関数
    public int doubleValue(int x) 
    {
        return x * 2;
    }
    public int sumUpToN(int x)
    {
        int Sum=0;
        for(int N=0;N<=x;N++){Sum+=N;}
        return Sum;
    }
    public int sumFromPtoQ(int x,int y)
    {
        if(y<x){return -1;}
        else
        {
            int Sum=0;
            for(int N=x;N<=y;N++){Sum+=N;}
            return Sum;
        }
    }
    public int sumFromArrayIndex(int[] a,int y)
    {
        if(a.length<=y){return -1;}
        else
        {
            int Sum=0;
            for(int N=a[y];N<=a.length;N++){Sum+=N;}
            return Sum;
        }
    }
    public int selectMaxValue(int[] Ar)
    {
        int Max=Ar[0];
        for(int N=0;N<Ar.length;N++) {if(Max<=Ar[N]) {Max=Ar[N];}}
        return Max;
    }
    public int selectMinValue(int[] Ar)
    {
        int Min=Ar[0];
        for(int N=0;N<Ar.length;N++) {if(Min>=Ar[N]) {Min=Ar[N];}}
        return Min;
    }
    public int selectMaxIndex(int[] Ar)
    {
        int Max=Ar[0];
        int MaxInd=0;
        for(int N=0;N<Ar.length;N++) {if(Max<=Ar[N]) {Max=Ar[N]; MaxInd=N;}}
        return MaxInd;
    }
    public int selectMinIndex(int[] Ar)
    {
        int Min=Ar[0];
        int MinInd=0;
        for(int N=0;N<Ar.length;N++) {if(Min>Ar[N]) {Min=Ar[N]; MinInd=N;}}
        return MinInd;
    }
    public void swapArrayElements(int[] Ar,int A,int B)
    {
        int Stock=Ar[A];
        Ar[A]=Ar[B];
        Ar[B]=Stock;
    }
    public boolean swapTwoArrays(int[] ArA,int[] ArB)
    {
        if(ArA.length!=ArB.length){return false;}
        else
        {
            for(int N=0;N<ArA.length;N++)
            {
                int Stock=ArA[N];
                ArA[N]=ArB[N];
                ArB[N]=Stock;}
            return true;
        }
    }

    //ここに続きを実装していく。
}