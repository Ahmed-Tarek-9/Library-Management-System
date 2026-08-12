import java.util.ArrayList;

public class Member {

    private String name;
    private final String membershipId;
    private final MembershipType membershipType;

    private double balanceOwed;
    private int itemsHeld;

    public Member(String name, String membershipId, MembershipType membershipType) {

        this.name = name;
        this.membershipId = membershipId;
        this.membershipType = membershipType;
        this.balanceOwed = 0.0;
        this.itemsHeld = 0;
    }

    public Member(String name, String membershipId, MembershipType membershipType, double balanceOwed, int itemsHeld) {

        this(name, membershipId, membershipType);
        this.balanceOwed = balanceOwed;
        this.itemsHeld = itemsHeld;
    }

    public String getName() {
        return name;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public double getBalanceOwed() {
        return balanceOwed;
    }

    public int getItemsHeld() {
        return itemsHeld;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean canBorrow() {

        return itemsHeld < 3 && balanceOwed <= 100;
    }

    public boolean chargeFine(double amount) {

        if (amount > 0) {

            balanceOwed += amount;
            System.out.println("Fine charged: " + amount);
            return true;
        } else {

            System.out.println("Invalid fine amount");
            return false;
        }
    }

    public boolean payFine(double amount) {

        if (amount > balanceOwed) {

            System.out.println("Payment amount exceeds balance owed");
            return false;
        } else {

            balanceOwed -= amount;
            System.out.println("Payment received: " + amount);
            System.out.println("Balance owed: " + balanceOwed);
            return true;
        }
    }

    public void recordBorrowing() {

        itemsHeld++;
    }

    public void recordReturn() {

        if (itemsHeld > 0) {

            itemsHeld--;
        } else {

            System.out.println("No items to return");
        }
    }

    public double applyWaiver(double fine) {

        double waiver = fine * membershipType.getWaiverRate();
        return fine - waiver;
    }

    @Override
    public String toString() {

        return "Member:\nName: " + name + ", Membership ID: " + membershipId + ", Membership Type: " + membershipType
                + ", Balance owed: " + balanceOwed + ", Items held: " + itemsHeld;
    }
}
