/**
 * Calculate corporate income tax for a given set of monthly incomes
 * Output CSV list of monthly taxes
 *
 * @author Cody Brookshear (codybrookshear@gmail.com)
 *
 * Compile on cmd line: javac IncomeTaxCalc.java
 * Run on cmd line: java IncomeTaxCalc
 */
public class IncomeTaxCalc {

    private static final float TAX_RATE = 0.2f;     // Tax rate is 20%
    private float cumulativeRoundBalance = 0;       // Tracks cents left over after rounding tax
    private float cumulativeIncome = 0;             // Cumulative income so far
    private float cumulativeTax = 0;                // Cumulative tax paid so far

    /**
     * Exercise the class with a set of monthly income values and print out.
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        // index 0 = January ... index 11 = December
	    int[] monthlyIncomes = new int[] { 86813, -27380, 36814, 96913, -135308,
                -162659, -113682, 213781, 291863, 173176, 223632, 136823 };

        IncomeTaxCalc t = new IncomeTaxCalc();

        // for each month, print out the cumulative tax calculated, separated by commas
	    for (int i = 0; i < monthlyIncomes.length; i++) {
            System.out.print(t.cumulativeTaxCalc(monthlyIncomes[i]));

            if (i < monthlyIncomes.length -1) {
                System.out.print(",");
            }
        }
    }

    /**
     * Calculate cumulative income tax. Assumes the first time it's called,
     * income is for January. There is no reset() method, so currently, you
     * would need to create a new IncomeTaxCalc object to reset cumulative
     * values to 0 in order to calculate a new year.
     * @param income Passed in one month at a time.
     * @return Current tax owed based on cumulative income and tax so far.
     */
    public int cumulativeTaxCalc(int income)
    {
        float tax = income * TAX_RATE; // default tax calculation
        cumulativeIncome += income;

        /**
         * normally, tax is just (income * TAX_RATE), but there are 2 exceptions:
         * 1. cumulative income is positive, and less than current income, so
         *    pay cumulativeIncome * TAX_RATE percent
         * 2. cumulative income is negative, and the absolute value of the
         *    current monthly tax would be greater than cumulative tax, so
         *    pay -(cumulative tax).
         */
        if (cumulativeIncome >= 0)
        {
            if (cumulativeIncome < income) {
                tax = cumulativeIncome * TAX_RATE;
            }
        }
        else // cumulativeIncome < 0
        {
            if (Math.abs(tax) > cumulativeTax) {
                tax = -cumulativeTax;
            }
        }

        cumulativeTax += tax;
        return sequentialRound(tax);
    }

    /**
     * On the first call (for January), we essentially round the value, then
     * save the part rounded off (cents) into cumulativeRoundBalance
     * Then on subsequent calls, we calculate how to most accurately round by
     * re-applying the previously rounded off amount to the new val before
     * rounding again.
     * @param val The value to be rounded
     * @return The value, rounded according to cumulative rounding
     */
    private int sequentialRound(float val)
    {
        val += cumulativeRoundBalance;
        int rounded = Math.round(val);
        cumulativeRoundBalance = val - rounded;
        return rounded;
    }
}
