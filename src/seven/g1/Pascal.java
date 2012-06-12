package seven.g1;

import org.apache.log4j.Logger;

/*
 * Pascal's Triangle
 * A way to calculate/lookup combinations without using factorials and the accompanying huge numbers
 */
public class Pascal {

	private double[][] triangle;	
	
	public Pascal( int s ) {
		triangle = new double[s+1][s+1];
		triangle[0][0] = 1;
		for ( int i = 1; i <= s; i++ ) {
			for ( int j = 0; j <= s; j++ ) {
				if (j-1 >= 0 ) {
					triangle[i][j] = triangle[i-1][j] + triangle[i-1][j-1];
				} else {
					triangle[i][j] = triangle[i-1][j];
				}
			}
		}
	}
	
	/*
	 * Returns the number of combinations of n items taken r at a time
	 * Obviously this will give an error if n or r exceed the dimensions of the array
	 */
	public double getComb ( int n, int r ) {
		return this.triangle[n][r];
	}

	/*
	 * Returns the probability of r or more occurrences of n trials
	 * given the probability p
	 * OK, actually it doesn't take into account probability p
	 */
	public double getProb ( int n, int r, double p ) {
		double prob = 0.0;
		for ( int i = r; i < n; i++ ) {
			prob += (getComb(n,i)/Math.pow(2, n));
		}
		return prob;
	}
}
