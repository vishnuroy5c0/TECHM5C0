package Day1;
import java.util.*;

public class Swap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter A value");
		int a=sc.nextInt();
		System.out.println("Enter B value");
		int b=sc.nextInt();
		System.out.println("Before Swap: a = " + a + ", b = " + b);
		a=a-b;
		b=a+b;
		a=b-a;
		System.out.println("After swapping A & B "+a+" "+b);

}
}