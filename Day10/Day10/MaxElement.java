package Day10;

public class MaxElement {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int arr[]= {1,8,7,4,6,3};
		int n=arr.length;
		int max=Integer.MIN_VALUE;
		for(int i=0;i<n;i++)
		{
			if(arr[i]>max)
			{
				max=arr[i];
			}
		}
		System.out.println(max);
		
		
		
	}//time complexity o(n)
}
