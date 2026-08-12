import java.util.ArrayList;

public abstract class LibraryItem {

    private final String itemId;
    private final String title;

    private ItemStatus itemStatus;
    private String borrowerName;

    private int renewalCount;

    private static final String LIBRARY_NAME = "Bayt Al Hekma";
    private static final double ADMINISTRATIVE_CHARGE = 10.0;
    private static int totalItemsCataloged;

    public LibraryItem(String itemId, String title) {

        this.itemId = itemId;
        this.title = title;

        this.itemStatus = ItemStatus.AVAILABLE;
        this.borrowerName = null;
        this.renewalCount = 0;

        totalItemsCataloged++;
    }

    public String getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public static String getLibraryName() {
        return LIBRARY_NAME;
    }

    public static double getAdministrativeCharge() {
        return ADMINISTRATIVE_CHARGE;
    }

    public static int getTotalItemsCatalogued() {
        return totalItemsCataloged;
    }

    public boolean lendItem(String borrowerName) {

        if (this.itemStatus == ItemStatus.AVAILABLE) {

            this.itemStatus = ItemStatus.ON_LOAN;
            this.borrowerName = borrowerName;
            System.out.println(this.title + " has been lent to " + borrowerName);
            return true;
        } else {

            System.out.println(this.title + " cannot be lent as it is not available");
            return false;
        }
    }

    public final void returnItem() {

        this.itemStatus = ItemStatus.AVAILABLE;
        this.borrowerName = null;
        this.renewalCount = 0;
    }

    public boolean markReserved() {

        if (this.itemStatus == ItemStatus.AVAILABLE) {

            this.itemStatus = ItemStatus.RESERVED;
            System.out.println(this.title + " has been marked reserved");
            return true;
        } else {

            System.out.println(this.title + " cannot be marked reserved as it is not available");
            return false;
        }
    }

    public void markLost() {

        this.itemStatus = ItemStatus.LOST;
        System.out.println(this.title + " has been marked lost");
    }

    public boolean restore() {

        if (this.itemStatus == ItemStatus.LOST || this.itemStatus == ItemStatus.RESERVED) {

            this.itemStatus = ItemStatus.AVAILABLE;
            System.out.println(this.title + " has been restored");
            return true;
        } else {

            System.out.println(this.title + " cannot be restored as it is not lost or reserved");
            return false;
        }
    }

    protected void incrementRenewalCount() {

        this.renewalCount++;
    }

    protected void resetRenewalCount() {

        this.renewalCount = 0;
    }

    protected boolean renewItem(int limit) {

        if (this.getItemStatus() != ItemStatus.ON_LOAN) {

            System.out.println(this.getTitle() + " is not on loan");
            return false;
        } else if (this.getRenewalCount() >= limit) {

            System.out.println(this.getTitle() + " has already been renewed " + limit + " times");
            return false;
        } else {

            this.incrementRenewalCount();
            System.out.println(this.getTitle() + " has been renewed");
            return true;
        }
    }

    public abstract double calculateFine(int overdueDays);

    public abstract int getLoanPeriod();

    public abstract String getCategory();

    @Override
    public String toString() {

        String borrower = borrowerName == null ? "not borrowed" : borrowerName;

        return "Library item:\nID: " + itemId + " Category: " + getCategory()
                + ", title: " + title + ", state: "
                + itemStatus + ", borrower: " + borrower + ", loan period: "
                + getLoanPeriod() + ", fine for one day overdue: " + calculateFine(1);
    }
}
