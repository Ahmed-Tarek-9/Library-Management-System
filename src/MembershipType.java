import java.lang.reflect.Member;

public enum MembershipType {

    STUDENT(0.25),
    STAFF(0.1),
    PUBLIC(0.0);

    private final double waiverRate;

    MembershipType(double waiverRate) {

        this.waiverRate = waiverRate;
    }

    public double getWaiverRate() {

        return waiverRate;
    }
}
