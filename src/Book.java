public class Book extends LibraryItem implements Renewable {

    private String author;
    private int pageCount;

    private static final int LOAN_PERIOD = 14;
    private static final double COST_PER_OVERDUE_DAY = 5.0;
    private static final int RENEWAL_LIMIT = 2;

    public Book(String itemId, String title, String author, int pageCount) {

        super(itemId, title);
        this.author = author;
        this.pageCount = pageCount;
    }

    public String getAuthor() {
        return author;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public double calculateFine(int overdueDays) {

        return overdueDays * COST_PER_OVERDUE_DAY;
    }

    @Override
    public int getLoanPeriod() {

        return LOAN_PERIOD;
    }

    @Override
    public String getCategory() {

        return "Book";
    }

    @Override
    public boolean renew() {

        return renewItem(RENEWAL_LIMIT);
    }

    @Override
    public int getRenewalLimit() {

        return RENEWAL_LIMIT;
    }
}
