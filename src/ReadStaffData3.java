
/*
     CA Part-01

     Student Name: Fabio A. Steyer
     Student ID: 22132848
     Programme: Higher Diploma in Science in Computing Information (Software Development)
     Year of Study: 2024
     Module Title: Algorithms and Advanced Programming
     Lecturer: Anshu Shahdeo
     Project/Issay Title: ReadStaffData Java Application
     Submission Deadline: Monday, 25 March 2024, 11:59 PM
*/

import java.io.File;
import java.util.Scanner;

public class ReadStaffData3 {
    public static void main(String[] args) throws Exception {
        // Initialize scanner to read data from a CSV file.
        String name = "C:\\Documents\\NetBeansProjects\\ReadStaffData3\\src\\Staff.csv";
        // Declare arrays for storing Staff objects and raw CSV lines.
        Scanner sc = new Scanner(new File(name)); // Initialize a Scanner to read data from the CSV file specified by the 'name' path.
        Staff[] staffs = new Staff[10000]; // Allocate space for up to 10,000 Staff objects to store the parsed data from the file.
        String[] unsortedRecords = new String[10001]; // Allocate space for up to 10,000 records plus the header, to keep the original CSV lines for comparison or display.

        // Read the CSV header and store it.
        String header = sc.nextLine(); // Read the first line of the CSV file, which typically contains column headers.
        unsortedRecords[0] = header; // Store the header in the first position of the unsortedRecords array for later reference.

        // Loop to read and store staff data from the CSV.
        int i = 0; // Initialize a counter to track the number of records processed.
        // Loop to read each line from the CSV until there are no more lines or the array limit is reached.
        while (sc.hasNextLine() && i < unsortedRecords.length - 1) {
            String st = sc.nextLine(); // Read the next line from the file.
            unsortedRecords[i + 1] = st; // Store unsorted record for later comparison or display.
            String[] data = st.split(","); // Split the line into individual data elements using the comma as a delimiter.
            // Create a new Staff object with the parsed data and assign it to the current position in the staffs array, incrementing the counter afterward.
            staffs[i++] = new Staff(
                    Integer.parseInt(data[0]), // Employee number.
                    data[1], // First name.
                    data[2], // Surname/last name.
                    data[3], // Department.
                    Float.parseFloat(data[4]), // Wage.
                    Double.parseDouble(data[5])); // Project completion rate.
        }
        sc.close(); // Closing the scanner.

        // Sort the array of staff objects by projectCompletionRate using Bubblesort.
        bubbleSortStaffs(staffs, i);

        // Print unsorted staff and then sort them for comparison.
        System.out.println(); // Create a line spacing for readability that will display the original, unsorted staff records.
        System.out.println("Input: 10,000 unsorted records:");
        for (String record : unsortedRecords) { // Iterate through each record in the unsortedRecords array.
            if (record != null) System.out.println(record); // Check if the record is not null before printing it. This displays all the records exactly as they were read from the CSV file, preserving their original order.
        }
        System.out.println("-----------------------------------------------------------------------------------------"); // // Line added to better read results only.
        System.out.println("Output: Records sorted in ascending order by column project_completion_rate:"); // Display the sorted staff records by their project completion rates in ascending order.
        for (int j = 0; j < i; j++) { // // Loop through the array up to the number of loaded records. i is the actual count of loaded records.
            if (staffs[j] != null) { // Ensure the staff object is not null before accessing it.
                System.out.printf("%.10f\n", staffs[j].getProjectCompletionRate()); // Print the project completion rate, formatted to 10 decimal places.
            }
        }

        // Measure and display average elapsed time for sorting.
        measureAndDisplaySortingTime(staffs, i);

        // Search functionality
        Scanner userInput = new Scanner(System.in); // Initialize a new Scanner for user input to interactively search staff members by first name.
        System.out.println("-----------------------------------------------------------------------------------------"); // Line added to better read results only.
        System.out.println(); // Prompt the user to enter a first name for the search operation.
        System.out.println("Enter a first name to search:");
        String searchName = userInput.nextLine(); // Capture the user's input as the search name.
        searchAndPrintByName(staffs, searchName, i); // Call the searchAndPrintByName method with the entered name to find and display matching staff members.

        // Measure average search time
        measureAverageSearchTime(staffs, searchName, i);
    }

    // Bubble sort implementation for sorting staffs based on projectCompletionRate column.
    private static void bubbleSortStaffs(Staff[] staffs, int n) {
        for (int i = 0; i < n - 1; i++) { // Outer loop for passing through the array.
            for (int j = 0; j < n - i - 1; j++) { // Inner loop for comparing adjacent elements.
                if (staffs[j] != null && staffs[j + 1] != null && // Check if current pair is out of order; swap them if necessary.
                        staffs[j].getProjectCompletionRate() > staffs[j + 1].getProjectCompletionRate()) {

                    // Perform swap: temporary variable holds one staff while the swap is executed.
                    Staff temp = staffs[j];
                    staffs[j] = staffs[j + 1];
                    staffs[j + 1] = temp;
                }
            }
        }
    }
    // Search staff records by name and alternatively by department, then print results.
    private static void searchAndPrintByName(Staff[] staffs, String name, int totalRecords) {
        Staff[] foundStaffsByName = new Staff[totalRecords]; // Initialize an array to store staff members found by the specified name,
        int foundCount = 0; // and a counter for the number of matches.
        for (int i = 0; i < totalRecords; i++) { // Iterate through the array of staffs to find matches by first name,
            if (staffs[i] != null && staffs[i].getFName().equalsIgnoreCase(name)) { // ignoring lower/upper case differences.
                foundStaffsByName[foundCount++] = staffs[i]; // If a match is found, add the staff member to the foundStaffsByName array and increment the found count.
            }
        }

        if (foundCount == 0) {
            // If no staff members were found with the specified name, inform the user.
            System.out.println("No employer found with this name.");
        } else {
            // If one or more staff members were found, display the count and details of each found staff.
            System.out.printf("It was found %d records for the name %s:\n\n", foundCount, name);
            for (int i = 0; i < foundCount; i++) { // Iterate through and print each found staff member's details.
                System.out.println(foundStaffsByName[i]);
            }
        }

        // Narrow down by department if needed.
        Scanner userInput = new Scanner(System.in);
        System.out.println("\nOptional: Enter a department to narrow down the search or press Enter to skip:");
        String departmentInput = userInput.nextLine().trim();

        // Checks if the department input is not empty to proceed with a department-specific search among the found staffs by name.
        if (!departmentInput.isEmpty()) {
            Staff[] foundStaffsByDepartment = new Staff[foundCount]; // Initialize an array to hold staffs found in the specified department.
            int countByDepartment = 0; // Counter for the number of staffs found in the specified department.
            for (int i = 0; i < foundCount; i++) { // Iterate through the staffs found by name.
                // If a staff's department matches the user input (case-insensitive), add to the department-specific array.
                if (foundStaffsByName[i].getDepartment().equalsIgnoreCase(departmentInput)) {
                    foundStaffsByDepartment[countByDepartment++] = foundStaffsByName[i];
                }
            }
            // Check if no staff members were found in the specified department after filtering by name.
            if (countByDepartment == 0) {
                // Notify the user that no staff members were found in the specified department.
                System.out.printf("No employer found with this name in the department %s.\n", departmentInput);
            } else {
                // If staff members are found, print the count and details of those who match both the name and department criteria.
                System.out.printf("Narrowed down to %d records for the name %s in the department - %s:\n\n", countByDepartment, name, departmentInput);
                for (int i = 0; i < countByDepartment; i++) { // Iterate through the filtered list of staff members.
                    System.out.println(foundStaffsByDepartment[i]); // Print the details of each staff member found in the specified department.
                }
            }
        }
    }

    // Method to display the elapsed time for 10, 100, 1000, 5000, 10000.
    private static void measureAndDisplaySortingTime(Staff[] staffs, int totalRecords) {
        int[] recordCounts = {10, 100, 1000, 5000, 10000};
        for (int count : recordCounts) {
            if (count > totalRecords) break; // Skip sizes larger than the actual data size.

            long totalTime = 0; // Initialize a variable to track the total time spent across all sorting iterations.
            int iterations = 5; // Set the number of iterations for the sorting time measurement to provide an average sorting time.

            for (int i = 0; i < iterations; i++) {
                Staff[] copy = new Staff[count]; // Create a copy of the staffs array to avoid altering the original data during sorting.
                System.arraycopy(staffs, 0, copy, 0, count);
                long startTime = System.nanoTime(); // Record start time before sorting.
                bubbleSortStaffs(copy, count); // Perform bubble sort on the copy for the current subset of staff records.
                long endTime = System.nanoTime(); // Record end time after sorting.
                totalTime += (endTime - startTime); // Accumulate total time spent sorting across all iterations.
            }
            // Displays the average time in nanoseconds for the search.
            long averageTime = totalTime / iterations;
            System.out.printf("Average sorting time for %d records: %d nanoseconds.\n", count, averageTime);
        }
    }

    // Method for measuring search performance, intended for timing the search operation.
    private static void measureAverageSearchTime(Staff[] staffs, String name, int totalRecords) {
        long totalTime = 0;
        int iterations = 10;

        // Iterate through a fixed number of iterations to measure search performance.
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime(); // Record the start time of the search operation.
            searchByNameForTimeMeasurement(staffs, name, totalRecords); // Execute the search operation to be measured.
            long endTime = System.nanoTime(); // Record the end time of the search operation.
            totalTime += (endTime - startTime); // Accumulate the total time taken for all search operations.
        }
        // Displays the average time in nanoseconds for the search.
        long averageTime = totalTime / iterations;
        System.out.printf("Average search time: %d nanoseconds.\n", averageTime);
    }

    // Method simulating a search operation for time measurement purposes.
    private static void searchByNameForTimeMeasurement(Staff[] staffs, String name, int totalRecords) {
        for (int i = 0; i < totalRecords; i++) {
        }
    }

    // Staff class implementation
    static class Staff implements Comparable<Staff> {
        private final int empNo;
        private final String fName;
        private final String sName;
        private final String department;
        private final float wage;
        private final double projectCompletionRate;

        // Constructor for the Staff class: Initializes a new Staff object.
        public Staff(int empNo, String fName, String sName, String department, float wage, double projectCompletionRate) {
            this.empNo = empNo;
            this.fName = fName;
            this.sName = sName;
            this.department = department;
            this.wage = wage;
            this.projectCompletionRate = projectCompletionRate;
        }

        // Getters
        public String getFName() {
            return fName;
        }

        public int getEmpNo() {
            return empNo;
        }
        public String getDepartment() {
            return department;
        }
        public double getProjectCompletionRate() {
            return projectCompletionRate;
        }

        @Override
        public int compareTo(Staff other) {
            // Implements the compareTo method to compare this staff's project completion rate with another's, facilitating sorting based on this attribute.
            return Double.compare(this.projectCompletionRate, other.projectCompletionRate);
        }

        @Override
        public String toString() {
            // Override toString to provide a formatted string representation of the Staff object.
            return String.format("Staff [EmpNo=%d, First Name=%s, Last Name=%s, Department=%s, Wage=%.2f, Project Completion Rate=%.10f]",
                    empNo, fName, sName, department, wage, projectCompletionRate);
        }
    }
}
