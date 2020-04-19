import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }


    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

         assertEquals(0, 0); */
        assertEquals(1, CompoundInterest.numYears(2021));
        assertEquals(2, CompoundInterest.numYears(2022));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2022), tolerance);
        assertEquals(90.0, (CompoundInterest.futureValue(100.0, -10.0, 2021)), 0.01);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValueReal(10, 12, 2022, 3), 11.8026496, 0.01);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2022, 10), 0.01);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(14936.38, CompoundInterest.totalSavingsReal(5000, 2022, 10, 5), tolerance);
    }
}


/* Run the unit tests in this file. */