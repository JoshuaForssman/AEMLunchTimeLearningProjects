package tech.innisfree.challenges;

public class EulerProblem1 {

    public static void main(String[] args) {

        System.out.println(sumMultiples());
    }

    public static int sumMultiples(){

        int firstMultiple = 3;
        int secondMultiple = 5;

        int sumValue = 0;

        for (int i = 1; i < 1000; i++) {

            if( ((i % firstMultiple) == 0) || ((i % secondMultiple) == 0)){
                sumValue+= i;
            }
        }

        return sumValue;
    }
}
