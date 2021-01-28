import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * Project: Coursera Algorithms by Standford University: Graph Search, Shortest Paths, and Data Structures.
 * Description: Week 3 Programming Assignment.
 *              This class finds the sum of medians in integer streams in which one number is pushed in
 *              each of 10000 time steps.
 *              The goal of this problem is to implement the "Median Maintenance" algorithm
 *              (covered in the Week 3 lecture on heap applications).
 *              The text file contains a list of the integers from 1 to 10000 in unsorted order.
 *              You should treat this as a stream of numbers, arriving one by one.
 *              Letting x_i denote the i th number of the file, the k th median m_k is defined as
 *              the median of the numbers x_1, x_2......x_k.
 *              (So, if k is odd, then m_k is ((k+1)/2)th smallest number among x_1, x_2......x_k;
 *              if k is even, then m_k is the (k/2)th smallest number among x_1, x_2......x_k)
 *
 *              The expected answer of the assignment is the sum of these 10000 medians, modulo 10000
 *              (i.e., only the last 4 digits).
 *              That is, you should compute (m_1+m_2+m_3 +......+ m_10000) mod 10000.
 *
 *              To solve this problem, two heaps(represented as array) are created.
 *              Heap_low stores first half of elements and Heap_high stores the second half.
 *
 * @author : Zitao He
 * @date : 2021-01-25 20:13
 **/
public class StreamMedianFinder {
    int[] numbers;
    ArrayList<Integer> heapLow; //Heap with max as root
    ArrayList<Integer> heapHigh; //Heap with min as root

    /**
     * Constructor reads in data and build integer array numbers
     * @param streamSize the size of the stream (number of distinct integers in data file)
     * @param inputFileName the file name of data file
     * @throws FileNotFoundException throws exception when file is not found
     */
    public StreamMedianFinder(int streamSize, String inputFileName) throws FileNotFoundException{
        numbers = new int[streamSize];
        heapLow = new ArrayList<>();
        heapHigh = new ArrayList<>();
        Scanner fileScanner;
        int index = 0;

        try{
            fileScanner = new Scanner(new File(inputFileName));
        }
        catch (IOException e){
            throw new FileNotFoundException("Error: Input file is not found.");
        }
        while(fileScanner.hasNextLine()){
            numbers[index++] = Integer.parseInt(fileScanner.nextLine());
        }
        System.out.println(Arrays.toString(numbers));
    }

    /**
     * Calculate the median sum of integer stream
     * @return integer stream median sum
     */
    public int medianSum(){
        heapLow.add(numbers[0]);//Add the first number to heap low
        int medianSum = heapLow.get(0);//When there is only one element in heaps, that element is median
        for (int i = 1; i<numbers.length; i++){ //Iteration starts from second number
            int numToAdd = numbers[i];
            if(numToAdd < heapLow.get(0)){ //If new element is smaller than median, insert to heap low
                maxHeapInsert(heapLow, numToAdd);
            }
            else {
                minHeapInsert(heapHigh, numToAdd);
            }
            heapRearrange();
            medianSum += heapLow.get(0);
        }
        return medianSum;
    }

    /**
     * Insert an integer into a min heap (root value is min value)
     * @param heap min heap to be inserted
     * @param numberToAdd integer to be inserted
     */
    private void minHeapInsert(ArrayList<Integer> heap, int numberToAdd){
        heap.add(numberToAdd); //Add new element of end of list;
        int numberIdx = heap.size() - 1;
        //Processing bubble up after end insertion
        while(numberIdx > 0){
            int parentIdx = (numberIdx+1)/2 - 1;
            if (heap.get(parentIdx) > heap.get(numberIdx)){ //If parent node is larger than number to add.

                Collections.swap(heap, numberIdx, parentIdx);
                numberIdx = parentIdx;
            }
            else{break;}
        }
    }

    /**
     * Insert an integer into a max heap (root value is max value)
     * @param heap max heap to be inserted
     * @param numberToAdd integer to be inserted
     */
    public void maxHeapInsert(ArrayList<Integer> heap, int numberToAdd){
        heap.add(numberToAdd); //Add new element of end of list;
        int numberIdx = heap.size() - 1;
        //Processing bubble up after end insertion
        while(numberIdx > 0){
            int parentIdx = (numberIdx+1)/2 - 1;
            if (heap.get(parentIdx) < heap.get(numberIdx)){ //If parent node is smaller than number to add.

                Collections.swap(heap, numberIdx, parentIdx);
                numberIdx = parentIdx;
            }
            else{break;}
        }
    }

    /**
     * Extract the root value(min value) of a min heap (root will be deleted)
     * @param heap the min heap to be extracted
     * @return root value(min value)
     */
    private int minHeapRemove(ArrayList<Integer> heap){
        int min = heap.get(0);
        Collections.swap(heap, 0, heap.size()-1);
        heap.remove(heap.size()-1);
        minHeapPercolateDownRoot(heap, 0);
        return min;
    }

    /**
     * Extract the root value(max value) of a max heap (root will be deleted)
     * @param heap the max heap to be extracted
     * @return root value(max value)
     */
    private int maxHeapRemove(ArrayList<Integer> heap){
        int max = heap.get(0);
        Collections.swap(heap, 0, heap.size()-1);
        heap.remove(heap.size()-1);
        maxHeapPercolateDownRoot(heap, 0);
        return max;
    }

    /**
     * Percolate down the root until it meets min heap rules
     * (parent node is always smaller than children node)
     * @param heap min heap to be percolated
     * @param rootIdx array index of root in heap
     */
    private void minHeapPercolateDownRoot(ArrayList<Integer> heap, int rootIdx){
        int leftChildIdx = 2*rootIdx + 1;
        int rightChildIdx = 2*rootIdx + 2;
        int smallestIdx = rootIdx;

        if (leftChildIdx < heap.size() && heap.get(leftChildIdx) < heap.get(smallestIdx)){
            smallestIdx = leftChildIdx;
        }
        if (rightChildIdx < heap.size() && heap.get(rightChildIdx) < heap.get(smallestIdx)){
            smallestIdx = rightChildIdx;
        }
        if (smallestIdx != rootIdx){
            Collections.swap(heap, smallestIdx, rootIdx);
            minHeapPercolateDownRoot(heap, smallestIdx);
        }
    }

    /**
     * Percolate down the root until it meets max heap rules
     * (parent node is always larger than children node)
     * @param heap max heap to be percolated
     * @param rootIdx array index of root in heap
     */
    private void maxHeapPercolateDownRoot(ArrayList<Integer> heap, int rootIdx){
        int leftChildIdx = 2*rootIdx + 1;
        int rightChildIdx = 2*rootIdx + 2;
        int largestIdx = rootIdx;

        if (leftChildIdx < heap.size() && heap.get(leftChildIdx) > heap.get(largestIdx)){
            largestIdx = leftChildIdx;
        }
        if (rightChildIdx < heap.size() && heap.get(rightChildIdx) > heap.get(largestIdx)){
            largestIdx = rightChildIdx;
        }
        if (largestIdx != rootIdx){
            Collections.swap(heap, largestIdx, rootIdx);
            maxHeapPercolateDownRoot(heap, largestIdx);
        }
    }

    /**
     * Re-arrange the size of heap low and heap high such that heap low size is always equal to or
     * one element more than heap high. This method keeps heap low and heap high balanced.
     */
    private void heapRearrange(){
        int lowSize = heapLow.size();
        int highSize = heapHigh.size();
        if (lowSize < highSize){
            maxHeapInsert(heapLow, minHeapRemove(heapHigh)); //Insert the min value of heap high to heap low
        }
        if (lowSize - highSize == 2){
            minHeapInsert(heapHigh, maxHeapRemove(heapLow));
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //StreamMedianFinder test = new StreamMedianFinder(7,"data/Median-test1.txt");
        //StreamMedianFinder test = new StreamMedianFinder(10,"data/Median-test2.txt");
        StreamMedianFinder test = new StreamMedianFinder(10000,"data/Median.txt");

        //answer of Median-test1.txt is Output (sum of medians modulo 10000): 142
        //answer of Median-test2.txt is Output (sum of medians modulo 10000): 9335
        System.out.println("Sum of medians is: " + test.medianSum());
    }

    }
