import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;

/**
 * Project: Coursera Algorithms by Standford University: Graph Search, Shortest Paths, and Data Structures.
 * Description: Week 4 Programming Assignment.
 *              The goal of this problem is to implement a variant of the 2-SUM algorithm covered
 *              in this week's lectures.
 *
 *              The file(2sum.txt in /data folder) contains 1 million integers,
 *              both positive and negative (there might be some repetitions!).
 *              This is your array of integers, with the i_th row of the file specifying
 *              the i_th entry of the array.
 *
 *              Your task is to compute the number of target values t in the interval
 *              [-10000,10000] (inclusive) such that there are distinct numbers x, y
 *              in the input file that satisfy x + y = t
 *              (NOTE: ensuring distinctness requires a one-line addition to the algorithm from lecture.)
 *
 *              Write your numeric answer (an integer between 0 and 20001) in the space provided.
 *
 * @author : Zitao He
 * @date : 2021-01-27 21:30
 **/
public class TwoSumFinder {
    ArrayList<BigInteger> numbers;

    public TwoSumFinder(String inputFileName) throws FileNotFoundException {
        Scanner fileScanner;
        HashSet<BigInteger> rawNumbers = new HashSet<>();
        numbers = new ArrayList<>();
        try{
            fileScanner = new Scanner(new File(inputFileName));
        }
        catch (IOException e){
            throw new FileNotFoundException("Error: Input file is not found.");
        }

        while(fileScanner.hasNextLine()){
           rawNumbers.add(new BigInteger(fileScanner.nextLine()));
        }
        numbers.addAll(rawNumbers);
        Collections.sort(numbers);
    }

    public int numOfTwoSum(int low, int high){
        HashSet<BigInteger> uniqueSums = new HashSet<>();
        int[] sumBounds = leftRightBounds(numbers,
                new BigInteger(String.valueOf(low)), new BigInteger(String.valueOf(high)));

        for (BigInteger x : numbers){
            BigInteger lowBig = new BigInteger(String.valueOf(low));
            BigInteger highBig = new BigInteger(String.valueOf(high));

            BigInteger yLeft = lowBig.subtract(x);
            BigInteger yRight = highBig.subtract(x);

            int[] yBounds = leftRightBounds(numbers, yLeft, yRight);
            for (int i = yBounds[0]; i <= yBounds[1]; i++){ //x and y are not allowed to be equal to calculate sum
                if (!x.equals(numbers.get(i))){
                    BigInteger sum = x.add(numbers.get(i));
                    uniqueSums.add(sum);
                }
            }
        }
        return uniqueSums.size();
    }

    public int[] leftRightBounds(ArrayList<BigInteger> sortedList, BigInteger leftNum, BigInteger rightNum){
        int[] bounds = new int[2];
        int leftIdx = Collections.binarySearch(sortedList, leftNum);//return (-(insertion point) - 1) if number not found.
        int rightIdx = Collections.binarySearch(sortedList, rightNum);
        bounds[0] = (leftIdx >= 0) ? leftIdx : (-1)*leftIdx - 1;
        bounds[1] = (rightIdx >= 0) ? rightIdx : (-1)*rightIdx - 2; //right index is handled differently
        return bounds;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //TwoSumFinder tester = new TwoSumFinder("data/2sum-test1.txt");
        //System.out.println("Total number of valid two sums: " + tester.numOfTwoSum(3, 10)); //expected 8

        //TwoSumFinder tester = new TwoSumFinder("data/2sum-test2.txt");
        //System.out.println("Total number of valid two sums: " + tester.numOfTwoSum(0, 4)); //expected 2

        TwoSumFinder tester = new TwoSumFinder("data/2sum.txt");
        System.out.println("Total number of valid two sums: " + tester.numOfTwoSum(-10000, 10000)); //correct answer is 427
    }
}
