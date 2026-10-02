import java.util.Scanner;

public class EventRegistrationSystem {

    // ================= PARTICIPANT DATA =================

    static String[] participantNames = new String[100];
    static String[] participantIds = new String[100];
    static String[] participantEmails = new String[100];

    // ================= TICKET DATA =================

    static String[] ticketTypes = new String[100];
    static int[] ticketQuantities = new int[100];
    static double[] ticketPrices = new double[100];
    static double[] totalAmounts = new double[100];
    static double[] discounts = new double[100];
    static double[] finalAmounts = new double[100];

    static int participantCount = 0;

    // ================= MAIN METHOD =================

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int choice;

        do {

            System.out.println("\n======================================");
            System.out.println(" EVENT REGISTRATION & TICKET SYSTEM");
            System.out.println("======================================");

            System.out.println("1. Register Participant");
            System.out.println("2. Display Registration Summary");
            System.out.println("3. Display All Participants");
            System.out.println("4. Search Participant");
            System.out.println("5. Sort Participants");
            System.out.println("6. Exit");

            System.out.print("\nEnter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    registerParticipant(sc);
                    break;

                case 2:
                    displayRegistrationSummary();
                    break;

                case 3:
                    displayAllParticipants();
                    break;

                case 4:
                    searchParticipant(sc);
                    break;

                case 5:
                    sortParticipants();
                    break;

                case 6:
                    System.out.println("\nThank you for using the system!");
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }

        } while (choice != 6);

        sc.close();
    }

    // ================= PARTICIPANT REGISTRATION =================

    static void registerParticipant(Scanner sc) {

        if (participantCount >= 100) {
            System.out.println("\nRegistration limit reached.");
            return;
        }

        System.out.println("\n--- Participant Registration ---");

        System.out.print("Enter Participant Name: ");
        participantNames[participantCount] = sc.nextLine();

        System.out.print("Enter Participant ID: ");
        participantIds[participantCount] = sc.nextLine();

        System.out.print("Enter Participant Email: ");
        participantEmails[participantCount] = sc.nextLine();

        // ================= TICKET SELECTION =================

        System.out.println("\n--- Ticket Categories ---");
        System.out.println("1. Regular  - Rs. 500");
        System.out.println("2. VIP      - Rs. 1500");
        System.out.println("3. Student  - Rs. 300");
        System.out.println("4. Premium  - Rs. 2500");

        System.out.print("Select Ticket Category: ");
        int ticketChoice = sc.nextInt();

        switch (ticketChoice) {

            case 1:
                ticketTypes[participantCount] = "Regular";
                ticketPrices[participantCount] = 500;
                break;

            case 2:
                ticketTypes[participantCount] = "VIP";
                ticketPrices[participantCount] = 1500;
                break;

            case 3:
                ticketTypes[participantCount] = "Student";
                ticketPrices[participantCount] = 300;
                break;

            case 4:
                ticketTypes[participantCount] = "Premium";
                ticketPrices[participantCount] = 2500;
                break;

            default:
                System.out.println("Invalid ticket choice.");
                ticketTypes[participantCount] = "Unknown";
                ticketPrices[participantCount] = 0;
        }

        // ================= TICKET QUANTITY =================

        System.out.print("Enter Number of Tickets: ");
        ticketQuantities[participantCount] = sc.nextInt();

        // ================= FEE CALCULATION =================

        totalAmounts[participantCount] =
                ticketPrices[participantCount]
                * ticketQuantities[participantCount];

        System.out.println("\n--- Fee Calculation ---");
        System.out.println("Ticket Price : Rs. "
                + ticketPrices[participantCount]);

        System.out.println("Quantity     : "
                + ticketQuantities[participantCount]);

        System.out.println("Total Amount : Rs. "
                + totalAmounts[participantCount]);

        // ================= DISCOUNT CALCULATION =================

        if (ticketQuantities[participantCount] >= 10) {

            discounts[participantCount] =
                    totalAmounts[participantCount] * 0.15;

        } else if (ticketQuantities[participantCount] >= 5) {

            discounts[participantCount] =
                    totalAmounts[participantCount] * 0.10;

        } else {

            discounts[participantCount] = 0;
        }

        // ================= FINAL AMOUNT =================

        finalAmounts[participantCount] =
                totalAmounts[participantCount]
                - discounts[participantCount];

        System.out.println("\n--- Discount Calculation ---");

        System.out.println("Discount : Rs. "
                + discounts[participantCount]);

        System.out.println("Final Amount : Rs. "
                + finalAmounts[participantCount]);

        participantCount++;

        System.out.println("\nParticipant registered successfully!");
    }

    // ================= REGISTRATION SUMMARY =================

    static void displayRegistrationSummary() {

        if (participantCount == 0) {

            System.out.println("\nNo registration found.");
            return;
        }

        int index = participantCount - 1;

        System.out.println("\n======================================");
        System.out.println("       REGISTRATION SUMMARY");
        System.out.println("======================================");

        System.out.println("Participant ID : "
                + participantIds[index]);

        System.out.println("Name           : "
                + participantNames[index]);

        System.out.println("Email          : "
                + participantEmails[index]);

        System.out.println("\nTicket Type    : "
                + ticketTypes[index]);

        System.out.println("Ticket Price   : Rs. "
                + ticketPrices[index]);

        System.out.println("Quantity       : "
                + ticketQuantities[index]);

        System.out.println("\nTotal Amount   : Rs. "
                + totalAmounts[index]);

        System.out.println("Discount       : Rs. "
                + discounts[index]);

        System.out.println("Final Amount   : Rs. "
                + finalAmounts[index]);

        System.out.println("======================================");
    }

    // ================= DISPLAY ALL PARTICIPANTS =================

    static void displayAllParticipants() {

        if (participantCount == 0) {

            System.out.println("\nNo participants registered.");
            return;
        }

        System.out.println("\n======================================");
        System.out.println("        ALL PARTICIPANTS");
        System.out.println("======================================");

        for (int i = 0; i < participantCount; i++) {

            System.out.println("\nParticipant " + (i + 1));

            System.out.println("ID       : "
                    + participantIds[i]);

            System.out.println("Name     : "
                    + participantNames[i]);

            System.out.println("Email    : "
                    + participantEmails[i]);

            System.out.println("Ticket   : "
                    + ticketTypes[i]);

            System.out.println("Quantity : "
                    + ticketQuantities[i]);

            System.out.println("Amount   : Rs. "
                    + finalAmounts[i]);
        }

        System.out.println("\n======================================");
    }

    // ================= SEARCH PARTICIPANT =================

    static void searchParticipant(Scanner sc) {

        if (participantCount == 0) {

            System.out.println("\nNo participants registered.");
            return;
        }

        System.out.print("\nEnter Participant ID to search: ");
        String searchId = sc.nextLine();

        boolean found = false;

        // Linear Search

        for (int i = 0; i < participantCount; i++) {

            if (participantIds[i].equalsIgnoreCase(searchId)) {

                System.out.println("\n======================================");
                System.out.println("        PARTICIPANT FOUND");
                System.out.println("======================================");

                System.out.println("Participant ID : "
                        + participantIds[i]);

                System.out.println("Name           : "
                        + participantNames[i]);

                System.out.println("Email          : "
                        + participantEmails[i]);

                System.out.println("Ticket Type    : "
                        + ticketTypes[i]);

                System.out.println("Ticket Price   : Rs. "
                        + ticketPrices[i]);

                System.out.println("Quantity       : "
                        + ticketQuantities[i]);

                System.out.println("Total Amount   : Rs. "
                        + totalAmounts[i]);

                System.out.println("Discount       : Rs. "
                        + discounts[i]);

                System.out.println("Final Amount   : Rs. "
                        + finalAmounts[i]);

                System.out.println("======================================");

                found = true;

                break;
            }
        }

        if (!found) {

            System.out.println("\nParticipant not found.");
        }
    }

    // ================= SORT PARTICIPANTS =================

    static void sortParticipants() {

        if (participantCount < 2) {

            System.out.println("\nNot enough participants to sort.");
            return;
        }

        // Sorting participants alphabetically by name

        for (int i = 0; i < participantCount - 1; i++) {

            for (int j = i + 1; j < participantCount; j++) {

                if (participantNames[i]
                        .compareToIgnoreCase(participantNames[j]) > 0) {

                    // Swap Names

                    String tempName = participantNames[i];

                    participantNames[i] = participantNames[j];

                    participantNames[j] = tempName;

                    // Swap IDs

                    String tempId = participantIds[i];

                    participantIds[i] = participantIds[j];

                    participantIds[j] = tempId;

                    // Swap Emails

                    String tempEmail = participantEmails[i];

                    participantEmails[i] = participantEmails[j];

                    participantEmails[j] = tempEmail;

                    // Swap Ticket Types

                    String tempTicket = ticketTypes[i];

                    ticketTypes[i] = ticketTypes[j];

                    ticketTypes[j] = tempTicket;

                    // Swap Quantities

                    int tempQuantity = ticketQuantities[i];

                    ticketQuantities[i] = ticketQuantities[j];

                    ticketQuantities[j] = tempQuantity;

                    // Swap Ticket Prices

                    double tempPrice = ticketPrices[i];

                    ticketPrices[i] = ticketPrices[j];

                    ticketPrices[j] = tempPrice;

                    // Swap Total Amounts

                    double tempTotal = totalAmounts[i];

                    totalAmounts[i] = totalAmounts[j];

                    totalAmounts[j] = tempTotal;

                    // Swap Discounts

                    double tempDiscount = discounts[i];

                    discounts[i] = discounts[j];

                    discounts[j] = tempDiscount;

                    // Swap Final Amounts

                    double tempFinal = finalAmounts[i];

                    finalAmounts[i] = finalAmounts[j];

                    finalAmounts[j] = tempFinal;
                }
            }
        }

        System.out.println("\nParticipants sorted by name successfully!");
    }
}