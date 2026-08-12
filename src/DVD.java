public class DVD extends LibraryItem {

    private int runtimeMinutes;

    private static final int LOAN_PERIOD = 3;
    private static final double COST_PER_OVERDUE_DAY = 15.0;

    public DVD(String itemId, String title, int runtimeMinutes) {

        super(itemId, title);
        this.runtimeMinutes = runtimeMinutes;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
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

        return "DVD";
    }
}
