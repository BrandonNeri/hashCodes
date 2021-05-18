package Lab109;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author Brandon Neri
 * @version 04/08/2021
 *
 * Client.java is a Java class that uses a Polynomial hashCode 
 * method that creates and can compresses an ArrayList of 
 * hashCodes. It includes two methods that keep track of the 
 * amount of total collisions and the max collisions that
 * happen when creating and compressing hashCodes. This data
 * is then printed out in an ASCII table. This program works
 * for any valid space delimited file entered into the program.
 * 
 */
public class Client {

    public static void main(String[] args) {
        
        // Prompt user to enter a file path
        String path = JOptionPane.showInputDialog(null, "Enter a file path:");
        
        // Create an array to store the read in words
        ArrayList<String> words = new ArrayList<>();
        try { // Attempt to read in words from a file with space delimit
            Scanner s = new Scanner(new File(path));
            while (s.hasNext()) { words.add(s.next()); }
        } catch (FileNotFoundException fnfe) { // Catch if file not found
            System.out.println("FileNotFoundException");
        }

        // Header for the Polynomial ASCII table
        String bar = "+---------+--------------------+----------+";
        System.out.println(bar);
        System.out.printf("|   \t%4s\t   |", "Polynomial Collision Table");
        System.out.println("\n" + bar);
        System.out.printf("|  %5s  |  %16s  |  %6s  |", "a", "Total Collisions", "Max");
        System.out.println("\n" + bar);

        // NumberFormat to add commas to larger values in ASCII tables
        NumberFormat nf = NumberFormat.getInstance(Locale.US);

        // Create a new array of the hash codes from the words read in
        for (int i = 30; i < 46; i++) {
            ArrayList<Integer> hashCodes = new ArrayList<>();
            int size = 0;
            for (int j = 0; j < words.size(); j++) {
                // Test "a" values between 30-45 inclusive
                hashCodes.add(size++, polynomialHashCode(words.get(j), i));
            }
            int maxValue = maxDuplicates(hashCodes);
            int duplicatesValue = countDuplicates(hashCodes);
            // Print out row for ASCII table
            System.out.printf("|  %5s  |  %16s  |  %6s  |", i,
                    nf.format(duplicatesValue),
                    nf.format(maxValue));
            System.out.println("\n" + bar);
        }
        System.out.println();

        // Header for the Compression ASCII table
        System.out.println(bar);
        System.out.printf("|   \t%4s\t   |", "Compression Collision Table");
        System.out.println("\n" + bar);
        System.out.printf("|  %5s  |  %16s  |  %6s  |", "p", "Total Collisions", "Max");
        System.out.println("\n" + bar);

        // Create an array of hashCodes to be compressed
        ArrayList<Integer> hashCodes = new ArrayList<>();
        int size = 0;
        for (int j = 0; j < words.size(); j++) {
            hashCodes.add(size++, polynomialHashCode(words.get(j), 31));
        }
        
        // Create a new array of the compressed codes from the hashCodes
        for (int p = 45398; p <= 45408; p++) {
            ArrayList<Integer> compressedCodes = new ArrayList<>();
            for (int k = 0; k < hashCodes.size(); k++) {
                // Test "p" values between 45398-45408 inclusive
                compressedCodes.add(k, madCompression(hashCodes.get(k), 45402, p, 1, 2));
            }
            int maxValue = maxDuplicates(compressedCodes);
            int duplicatesValue = countDuplicates(compressedCodes);
            // Print out row for ASCII table
            System.out.printf("|  %5s |  %16s  |  %6s  |", nf.format(p), 
                    nf.format(duplicatesValue), 
                    nf.format(maxValue));
            System.out.println("\n" + bar);
        }
    }

    /**
     *
     * A method to count and return the number of duplicates in an ArrayList.
     * 
     * @param list an ArrayList of type in filled with integer hash codes
     * @return an integer value of the total of all the duplicate hash codes in a list
     */
    private static int countDuplicates(ArrayList<Integer> list) {
        long duplicates = 0;
        for (int i = 0; i < list.size(); i++) {
            if (Collections.frequency(list, list.get(i)) > 1) {
                duplicates += Collections.frequency(list, list.get(i)) - 1;
                list.removeAll(Collections.singletonList(list.get(i)));
            }
        }
        return (int) duplicates;
    }

    /**
     *
     * A method to return the max number of duplicates found in a given ArrayList.
     * 
     * @param list an ArrayList of type in filled with integer hash codes
     * @return an integer value of the number of most repeated has codes in a list
     */
    private static int maxDuplicates(ArrayList<Integer> list) {
        long max = 0;
        for (int i = 1; i < list.size(); i++) {
            if (Collections.frequency(list, list.get(i)) > max) {
                max = Collections.frequency(list, list.get(i)) - 1;
            }
        }
        return (int) max;
    }

    /**
     *
     * An implementation of a polynomial based hash code summation.
     *
     * @param key a string that is to converted into a hash code
     * @param a an int to use in the polynomial expression
     * @return an integer value of a hash code
     */
    private static int polynomialHashCode(String key, int a) {
        long hashValue = 0;
        for (int i = 0; i < key.length(); i++) {
            hashValue += (long)(key.charAt(i) * Math.pow(a, i));
        }
        return Math.abs((int) hashValue);
    }

    /**
     *
     * An implementation of a compression method for a given set of hashCodes. 
     * 
     * @param hashCode a hashCode value to be compressed
     * @param N the size of the bucket array
     * @param p a given prime integer
     * @param a a random value between 1 and p - 1
     * @param b a random value between 0 and p - 1
     * @return a compressed value of a given hashCode
     */
    private static int madCompression(long hashCode, int N, int p, int a, int b) {
        if (!(a > 0) || !(b >= 0) || !(a <= (p - 1)) || !(b <= (p - 1))) {
            throw new IllegalArgumentException();
        } else {
            return (int)(((Math.abs(a * hashCode) + b) % p) % N);
        }
    }
}
