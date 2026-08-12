public class Magazine extends LibraryItem implements Renewable {

    private int issueNumber;

    private static final int LOAN_PERIOD = 7;
    private static final double COST_PER_OVERDUE_DAY = 3.0;
    private static final int RENEWAL_LIMIT = 1;
    private static final double MAX_COST = 30.0;

    public Magazine(String itemId, String title, int issueNumber) {

        super(itemId, title);
        this.issueNumber = issueNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public double calculateFine(int overdueDays) {

        return Math.min(overdueDays * COST_PER_OVERDUE_DAY, MAX_COST);
    }

    @Override
    public int getLoanPeriod() {

        return LOAN_PERIOD;
    }

    @Override
    public String getCategory() {

        return "Magazine";
    }

    @Override
    public int getRenewalLimit() {

        return RENEWAL_LIMIT;
    }

    @Override
    public boolean renew() {

        return renewItem(RENEWAL_LIMIT);
    }
}
