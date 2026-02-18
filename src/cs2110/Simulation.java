package cs2110;

import cs2110.WebsiteData.VisitRecord;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Simulation {

    /**
     * Reads data from a text file `fileName` and returns an array of `VisitRecord`s.
     */
    static VisitRecord[] fromFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        // Count number of lines in file
        int numRecords = 0;
        while (br.readLine() != null) {
            numRecords++;
        }
        br.close();

        VisitRecord[] records = new VisitRecord[numRecords];

        // Parse data to create array
        br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();
        int i = 0;
        while (line != null) {
            String[] parts = line.split(",");
            String userID = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1]);
            records[i] = new VisitRecord(userID, timestamp);
            i++;
            line = br.readLine();
        }
        br.close();

        return records;
    }

    /**
     * Prints the entries of `records` one per line.
     */
    static void printRecordArray(VisitRecord[] records) {
        for (int i = 0; i < records.length; i++) {
            System.out.println( (i+1) + ": " + records[i].userID() + " @ " +
                records[i].timestamp());
        }
    }

    /**
     * Main method to experiment with `WebsiteData`. Feel free to add to or modify this method to
     * run additional queries and "playtest" your implementations. You are not submitting this
     * file. We have provided three ".txt" data files that you can load in this method. Feel free
     * to also develop your own, but make sure to stick to the format of the existing ones.
     */
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Which data file would you like to load (e.g., small.txt)? ");
            VisitRecord[] records = fromFile(in.nextLine());

            System.out.println("Loaded " + records.length + " visit records:\n");
            printRecordArray(records);

            WebsiteData sampleData = new WebsiteData(records);
            System.out.println("\nTotal distinct users: " + sampleData.countDistinctUsers());

            System.out.println("\nFirst visit per user: ");
            printRecordArray(sampleData.firstVisitPerUser());

            // Add more queries to this branch to experiment with your code further.
        } catch (IOException e) {
            System.out.println("Error loading your data: " + e.getMessage());
        }
    }
}
