package Day1;
import java.util.Scanner;

public class PowerOf2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a number");
        int a = sc.nextInt();
        sc.close();
        
        if (isPowerOf2(a)) {
            System.out.println("Yes!!! It is a power of 2");
        } else {
            System.out.println("Oh no!!! It is not a power of 2");
        }
    }
    
    public static boolean isPowerOf2(int a) {
        return a > 0 && (a & (a - 1)) == 0;
    }
}
