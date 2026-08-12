import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Scanner;

public class Library {

    private ArrayList<LibraryItem> catalog = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();

    public boolean registerItem(LibraryItem item) {

        if (!catalog.isEmpty()) {
            boolean isDuplicate = false;

            for (LibraryItem libraryItem : catalog) {

                if (libraryItem.getItemId().equals(item.getItemId())) {
                    isDuplicate = true;
                    break;
                }
            }

            if (isDuplicate) {

                System.out.println("A library item with the same ID already exists");
                return false;
            }
        }

        catalog.add(item);
        return true;
    }

    public LibraryItem findItem(String itemId) {

        if (catalog.isEmpty()) {

            return null;
        } else {

            for (LibraryItem libraryItem : catalog) {

                if (libraryItem.getItemId().equals(itemId)) {
                    return libraryItem;
                }
            }

            return null;
        }
    }

    public void searchItemById(Scanner scanner) {
        String itemId;
        System.out.println("Enter the Item ID you want to search for: ");
        itemId = scanner.nextLine();
        LibraryItem item = findItem(itemId);
        if (item != null) {
            System.out.println(item);
        }
    }

    public Member findMember(String membershipId) {

        if (members.isEmpty()) {

            return null;
        } else {

            for (Member member : members) {

                if (member.getMembershipId().equals(membershipId)) {
                    return member;
                }
            }

            return null;
        }
    }

    public Member findMemberByName(String name) {

        if (members.isEmpty()) {

            return null;
        } else {

            for (Member member : members) {

                if (member.getName().equals(name)) {
                    return member;
                }
            }

            return null;
        }
    }

    public boolean lendItem(String itemId, String membershipId) {

        LibraryItem item = findItem(itemId);
        Member member = findMember(membershipId);
        if (item == null) {

            System.out.println("No item found with ID: " + itemId);
            return false;
        }
        if (member == null) {

            System.out.println("No member found with ID: " + membershipId);
            return false;
        }
        if (!member.canBorrow()) {

            System.out.println("Member with ID: " + membershipId + " cannot borrow items");
            return false;
        }
        if (!item.lendItem(member.getName())) {

            return false;
        }
        member.recordBorrowing();
        return true;
    }

    private double calculateReturnCharge(LibraryItem item, Member member, int overdueDays) {

        if (overdueDays > 0) {

            double baseFine = item.calculateFine(overdueDays);
            double waivedFine = member.applyWaiver(baseFine);
            double totalCharge = waivedFine + LibraryItem.getAdministrativeCharge();
            member.chargeFine(totalCharge);
            return totalCharge;
        } else {

            return 0.0;
        }
    }

    public boolean processReturn(String itemId, int overdueDays) {

        LibraryItem item = findItem(itemId);

        if (item == null) {

            System.out.println("No item found with ID: " + itemId);
            return false;
        }
        if (item.getItemStatus() != ItemStatus.ON_LOAN) {

            System.out.println("Item with ID: " + itemId + " is not on loan");
            return false;
        }
        if (overdueDays < 0) {

            System.out.println("Invalid overdue days");
            return false;
        }

        Member borrower = findMemberByName(item.getBorrowerName());

        if (borrower == null) {

            return false;
        }

        calculateReturnCharge(item, borrower, overdueDays);

        borrower.recordReturn();
        item.returnItem();
        return true;
    }

    public boolean attemptRenewal(String itemId) {

        LibraryItem item = findItem(itemId);

        if (item == null) {

            System.out.println("No item found with ID: " + itemId);
            return false;
        }
        if (item instanceof Renewable renewable) {

            return renewable.renew();
        }
        return false;
    }

    public void listCatalog() {

        if (catalog.isEmpty()) {

            System.out.println("There are no items in the catalog");
        } else {

            int index = 1;
            for (LibraryItem item : catalog) {

                System.out.print(index++ + ". ");
                System.out.println(item);
            }
        }
    }

    public void listItemsByStatus(ItemStatus status) {

        if (catalog.isEmpty()) {

            System.out.println("There are no items in the catalog");
        } else {
            boolean isFound = false;

            int index = 1;
            for (LibraryItem item : catalog) {

                if (item.getItemStatus() == status) {

                    System.out.print(index++ + ". ");
                    System.out.println(item);
                    isFound = true;
                }
            }

            if (!isFound) {

                System.out.println("No items found with status: " + status);
            }
        }
    }

    public void listMembers() {

        if (members.isEmpty()) {

            System.out.println("There are no members registered in the library");
        } else {

            int index = 1;
            for (Member member : members) {

                System.out.print(index++ + ". ");
                System.out.println(member);
            }
        }
    }

    public int countItemsOnLoan() {

        if (catalog.isEmpty()) {

            return 0;
        } else {

            int count = 0;

            for (LibraryItem item : catalog) {

                if (item.getItemStatus() == ItemStatus.ON_LOAN) {

                    count++;
                }
            }
            return count;
        }
    }

    public double getLoanRate() {

        if (catalog.isEmpty()) {

            return 0.0;
        }

        return countItemsOnLoan() * 100.0 / catalog.size();
    }

    public double getTotalOutstanding() {

        if (members.isEmpty()) {

            return 0.0;
        }
        double total = 0.0;

        for (Member member : members) {

            total += member.getBalanceOwed();
        }

        return total;
    }

    public double getProjectedFines(int overdueDays) {

        if (members.isEmpty()) {

            return 0.0;
        }
        double total = 0.0;

        for (LibraryItem item : catalog) {

            if (item.getItemStatus() == ItemStatus.ON_LOAN) {

                total += item.calculateFine(overdueDays);
            }
        }

        return total;
    }

    public void printLibraryReport() {
        System.out.println("========== LIBRARY REPORT ==========");
        System.out.println();
        System.out.println("Catalog Size: " + catalog.size());
        System.out.println("Total Items Ever Cataloged: " + LibraryItem.getTotalItemsCatalogued());
        System.out.println("Items Currently on Loan: " + countItemsOnLoan());
        System.out.printf("Loan Rate: %.2f%%\n", getLoanRate());
        System.out.println("Outstanding Fines: " + getTotalOutstanding());
        System.out.println("Projected Fines for 7 days: " + getProjectedFines(7));
        System.out.println();
        System.out.println("====================================");

    }

    public void printMenu() {

        System.out.println("|==================================|");
        System.out.println("|   BAYT AL HEKMA LIBRARY SYSTEM   |");
        System.out.println("|==================================|");
        System.out.println("|                                  |");
        System.out.println("| ITEM OPERATIONS                  |");
        System.out.println("| 1. View Catalog                  |");
        System.out.println("| 2. Search Item by ID             |");
        System.out.println("| 3. View Items by Status          |");
        System.out.println("| 4. Register New Item             |");
        System.out.println("| 5. Mark Item as Reserved         |");
        System.out.println("| 6. Mark Item as Lost             |");
        System.out.println("| 7. Restore Item                  |");
        System.out.println("|                                  |");
        System.out.println("| MEMBER OPERATIONS                |");
        System.out.println("| 8. Register New Member           |");
        System.out.println("| 9. Register Old Member           |");
        System.out.println("| 10. View All Members             |");
        System.out.println("| 11. Pay Outstanding Fines        |");
        System.out.println("|                                  |");
        System.out.println("| CIRCULATION OPERATIONS           |");
        System.out.println("| 12. Borrow Item                  |");
        System.out.println("| 13. Return Item                  |");
        System.out.println("| 14. Renew Loan                   |");
        System.out.println("|                                  |");
        System.out.println("| REPORTS                          |");
        System.out.println("| 15. Library Report               |");
        System.out.println("|                                  |");
        System.out.println("| 0. Exit                          |");
        System.out.println("|==================================|");
    }

    public void run() {

        int choice = 0;
        Scanner scanner = new Scanner(System.in);

        do {

            printMenu();

            do {

                if (choice < 0 || choice > 14) {

                    System.out.println("Invalid choice. Please try again");
                }

                try {

                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {

                    choice = -1;
                    System.out.println("Invalid input. Please enter a number");
                }
            } while (choice < 0 || choice > 15);

            switch (choice) {

                case 1 -> listCatalog();
                case 2 -> searchItemById(scanner);
                case 3 -> viewItemsByStatus(scanner);
                case 4 -> registerNewItem(scanner);
                case 5 -> markItemAsReserved(scanner);
                case 6 -> markItemAsLost(scanner);
                case 7 -> restoreLostItem(scanner);
                case 8 -> registerNewMember(scanner);
                case 9 -> registerOldMember(scanner);
                case 10 -> listMembers();
                case 11 -> payOutstandingFines(scanner);
                case 12 -> borrowItem(scanner);
                case 13 -> returnItem(scanner);
                case 14 -> renewLoan(scanner);
                case 15 -> printLibraryReport();
                case 0 -> System.out.println("Exiting...");
            }
        } while (choice != 0);

        System.out.println("Goodbye!");
    }

    private void renewLoan(Scanner scanner) {

        System.out.println("Enter the item ID: ");
        String itemId = scanner.nextLine();

        attemptRenewal(itemId);
    }

    private void returnItem(Scanner scanner) {

        System.out.println("Enter the item ID: ");
        String itemId = scanner.nextLine();
        int overdueDays;

        do {

            System.out.println("Enter the number of overdue days or 0 if none: ");

            try {

                overdueDays = Integer.parseInt(scanner.nextLine());
                if (overdueDays < 0) {

                    System.out.println("Invalid input. Please enter a positive number");
                }
            } catch (NumberFormatException e) {

                overdueDays = -1;
                System.out.println("Invalid input. Please enter a number");
            }
        } while (overdueDays < 0);

        processReturn(itemId, overdueDays);
    }

    private void borrowItem(Scanner scanner) {

        System.out.println("Enter the member's ID: ");
        String memberId = scanner.nextLine();
        System.out.println("Enter the item ID: ");
        String itemId = scanner.nextLine();
        lendItem(itemId, memberId);
    }

    private void payOutstandingFines(Scanner scanner) {

        System.out.println("Enter the member's ID: ");
        String memberId = scanner.nextLine();

        Member member = findMember(memberId);

        if (member == null) {

            System.out.println("Member not found");
        } else {

            System.out.println("Outstanding Balance: " + member.getBalanceOwed());

            double payment;

            do {

                System.out.println("Enter payment amount: ");
                try {

                    payment = Double.parseDouble(scanner.nextLine());
                    if (payment <= 0) {

                        System.out.println("Invalid input. Please enter a positive number");
                    }
                } catch (NumberFormatException e) {

                    payment = -1;
                    System.out.println("Invalid input. Please enter a positivenumber");
                }
            } while (payment <= 0);

            boolean isPaid = member.payFine(payment);
            if (!isPaid) {

                System.out.println("Payment failed");
            }
        }
    }

    private void registerOldMember(Scanner scanner) {

        boolean isSuccessful = false;
        System.out.println("Enter the member's ID: ");
        String membershipId = scanner.nextLine();
        Member member = findMember(membershipId);

        if (member != null) {

            System.out.println("Member already exists");
        } else {

            System.out.println("Enter the member's name: ");
            String name = scanner.nextLine();

            double balanceOwed = 0.0;
            int itemsHeld = 0;

            do {
                if (balanceOwed < 0) {

                    System.out.println("Invalid input. Please enter 0 or a positive number");
                }
                System.out.println("Enter the member's balance owed: ");
                try {
                    balanceOwed = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException e) {

                    balanceOwed = -1;
                    System.out.println("Invalid input. Please enter a number");
                }
            } while (balanceOwed < 0);

            do {
                if (itemsHeld < 0) {

                    System.out.println("Invalid input. Please enter 0 or a positive number");
                }
                System.out.println("Enter the number of items held by the user: ");
                try {
                    itemsHeld = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {

                    itemsHeld = -1;
                    System.out.println("Invalid input. Please enter a number");
                }
            } while (itemsHeld < 0);

            int membershipType = 0;
            do {

                System.out.println("Enter the membership type: 1. Student, 2. Staff, 3. Public, or 0 to cancel");

                try {

                    membershipType = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {

                    membershipType = -1;
                    System.out.println("Invalid input. Please enter a number");
                }
            } while (membershipType < 0 || membershipType > 3);

            switch (membershipType) {

                case 1 -> {
                    member = new Member(name, membershipId, MembershipType.STUDENT, balanceOwed, itemsHeld);
                    isSuccessful = true;
                }
                case 2 -> {
                    member = new Member(name, membershipId, MembershipType.STAFF, balanceOwed, itemsHeld);
                    isSuccessful = true;
                }
                case 3 -> {
                    member = new Member(name, membershipId, MembershipType.PUBLIC, balanceOwed, itemsHeld);
                    isSuccessful = true;
                }
                case 0 -> System.out.println("Operation cancelled");
            }
            if (isSuccessful) {

                members.add(member);
                System.out.println("Member registered successfully");
            } else {

                System.out.println("Failed to register member");
            }
        }
    }

    private void registerNewMember(Scanner scanner) {

        boolean isSuccessful = false;
        System.out.println("Enter the member's ID: ");
        String membershipId = scanner.nextLine();
        Member member = findMember(membershipId);

        if (member != null) {

            System.out.println("Member already exists");
        } else {

            System.out.println("Enter the member's name: ");
            String name = scanner.nextLine();
            int membershipType = 0;
            do {

                System.out.println("Enter the membership type: 1. Student, 2. Staff, 3. Public, or 0 to cancel");

                try {

                    membershipType = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {

                    membershipType = -1;
                    System.out.println("Invalid input. Please enter a number");
                }
            } while (membershipType < 0 || membershipType > 3);

            switch (membershipType) {

                case 1 -> {
                    member = new Member(name, membershipId, MembershipType.STUDENT);
                    isSuccessful = true;
                }
                case 2 -> {
                    member = new Member(name, membershipId, MembershipType.STAFF);
                    isSuccessful = true;
                }
                case 3 -> {
                    member = new Member(name, membershipId, MembershipType.PUBLIC);
                    isSuccessful = true;
                }
                case 0 -> System.out.println("Operation cancelled");
            }
            if (isSuccessful) {

                members.add(member);
                System.out.println("Member registered successfully");
            } else {

                System.out.println("Failed to register member");
            }
        }
    }

    private void restoreLostItem(Scanner scanner) {

        System.out.println("Enter the item ID to be reserved: ");
        String itemId = scanner.nextLine();
        LibraryItem item = findItem(itemId);

        if (item != null) {

            item.restore();
        }
    }

    private void markItemAsLost(Scanner scanner) {

        System.out.println("Enter the item ID to be reserved: ");
        String itemId = scanner.nextLine();
        LibraryItem item = findItem(itemId);

        if (item != null) {

            item.markLost();
        }
    }

    private void markItemAsReserved(Scanner scanner) {

        System.out.println("Enter the item ID to be reserved: ");
        String itemId = scanner.nextLine();
        LibraryItem item = findItem(itemId);

        if (item != null) {

            item.markReserved();
        }
    }

    private void registerNewItem(Scanner scanner) {

        boolean isSuccessful = false;
        System.out.println("Enter the item ID: ");
        String itemId = scanner.nextLine();
        LibraryItem item = findItem(itemId);

        if (item != null) {

            System.out.println("Item already exists");
        } else {

            System.out.println("Enter the title of the item: ");
            String title = scanner.nextLine();

            int itemType = 0;
            do {
                if (itemType < 0 || itemType > 3) {

                    System.out.println("Invalid input. Please enter a number between 0 and 3");
                }

                System.out.println("Enter the item type: 1. Book, 2. Magazine, 3. DVD, or 0 to cancel");

                try {

                    itemType = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {

                    itemType = -1;
                    System.out.println("Invalid input. Please enter a number");
                }
            } while (itemType < 0 || itemType > 3);

            switch (itemType) {

                case 1 -> {

                    System.out.println("Enter the author's name: ");
                    String author = scanner.nextLine();
                    System.out.println("Enter the page count: ");
                    int pageCount;
                    try {

                        pageCount = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {

                        pageCount = 0;
                        System.out.println("Invalid Input, pageCount defaulted to 0");
                    }

                    item = new Book(itemId, title, author, pageCount);
                    registerItem(item);
                    isSuccessful = true;
                }
                case 2 -> {

                    System.out.println("Enter the issue number: ");
                    int issueNumber;
                    try {

                        issueNumber = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {

                        issueNumber = 0;
                        System.out.println("Invalid input. Issue number defaulted to 0");
                    }

                    item = new Magazine(itemId, title, issueNumber);
                    registerItem(item);
                    isSuccessful = true;
                }
                case 3 -> {

                    System.out.println("Enter the runtime in minutes: ");
                    int runtimeMinutes;
                    try {

                        runtimeMinutes = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {

                        runtimeMinutes = 0;
                        System.out.println("Invalid input. Runtime defaulted to 0");
                    }

                    item = new DVD(itemId, title, runtimeMinutes);
                    registerItem(item);
                    isSuccessful = true;
                }
                case 0 -> System.out.println("Operation cancelled");
            }
        }

        if (isSuccessful) {

            System.out.println("Item registered successfully");
        } else {

            System.out.println("Item registration failed");
        }
    }

    private void viewItemsByStatus(Scanner scanner) {

        int choice = 0;
        do {

            System.out.println("Enter the status you want to search for: 1. Available, 2. On Loan, 3. Reserved, 4. Lost, or 0 to cancel");
            if (choice < 0 || choice > 4) {

                System.out.println("Invalid choice. Please try again");
            }

            try {

                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {

                choice = -1;
                System.out.println("Invalid input. Please enter a number");
            }
        } while (choice < 0 || choice > 4);

        switch (choice) {

            case 1 -> listItemsByStatus(ItemStatus.AVAILABLE);
            case 2 -> listItemsByStatus(ItemStatus.ON_LOAN);
            case 3 -> listItemsByStatus(ItemStatus.RESERVED);
            case 4 -> listItemsByStatus(ItemStatus.LOST);
            case 0 -> System.out.println("Operation cancelled");
        }
    }
}
