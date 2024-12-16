import org.w3c.dom.ls.LSOutput;

import java.util.Scanner;

public class Simple_Calculator {

    public static void main(String[] args) {

        Scanner sc=new Scanner(System.in);


        int n1=0, n2 = 0, result,tot;
        System.out.println("1.Addition");
        System.out.println("2.Subtraction");
        System.out.println("3.Multiplucation ");
        System.out.println("4.Division");

        System.out.print("Enter your choce :");
        result=sc.nextInt();

        if(result<=4) {
            System.out.println("Enter two number :");
            n1 = sc.nextInt();
            n2 = sc.nextInt();
        }


            switch (result) {
                case 1:
                    tot = n1 + n2;
                    System.out.println("Addition " + n1 + "+" + n2 + "=" + tot);
                    break;



                case 2:
                    tot = n1 - n2;
                    System.out.println("Subtraction " + n1 + "-" + n2 + "=" + tot);
                    break;

                case 3:
                    tot = n1 * n2;
                    System.out.println("Multiplucation " + n1 + "*" + n2 + "=" + tot);

                    break;

                case 4:
                    tot = n1 / n2;
                    System.out.println("Division " + n1 + "/" + n2 + "=" + tot);
                    break;

                default:
                    System.out.println("Invalid Choice");


        }
    }
}
