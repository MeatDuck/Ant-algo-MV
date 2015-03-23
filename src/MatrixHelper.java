import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.ojalgo.access.Access2D.Builder;
import org.ojalgo.matrix.BigMatrix;

public class MatrixHelper {
	/**
	 * get mean of returns
	 * @param returns
	 * @return
	 */
	public static BigMatrix getExcessReturns(BigMatrix returns) {
		Builder<BigMatrix> expectedExcessReturns = BigMatrix.getBuilder(
				(int) returns.countColumns(), 1);

		BigDecimal[] sum = new BigDecimal[(int) returns.countColumns()];
		Arrays.fill(sum, new BigDecimal(0));
		for (int i = 0; i < returns.countRows(); i++) {
			for (int j = 0; j < returns.countColumns(); j++) {
				sum[j] = sum[j].add(returns.get(i, j));
			}
		}

		for (int j = 0; j < returns.countColumns(); j++) {
			expectedExcessReturns.set(j, 0, sum[j].divide(new BigDecimal(
					returns.countRows()), RoundingMode.HALF_UP));
		}

		//System.out.println("Return Matrix:" + expectedExcessReturns.build());
		return expectedExcessReturns.build();
	}

	/**
	 * build covariance mx
	 * @param returns
	 * @return
	 */
	public static BigMatrix makeCovarianceMatrix(BigMatrix returns) {
		int rowNum = (int) returns.countRows();
		int colNum = (int) returns.countColumns();
		double[][] data = new double[rowNum][colNum];
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				data[i][j] = returns.get(i, j).doubleValue();
			}
		}
		Covariance covariance = new Covariance(data);
		RealMatrix mx = covariance.getCovarianceMatrix();

		Builder<BigMatrix> covMx = BigMatrix.getBuilder(colNum, colNum);
		for (int i = 0; i < colNum; i++) {
			for (int j = 0; j < colNum; j++) {
				covMx.set(i, j, mx.getEntry(i, j));
			}
		}

		return covMx.build();
	}

	/**
	 * Parse file
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public static BigMatrix createDoubleMatrixFromFile(String filename)
			throws FileNotFoundException {
		File inFile = new File(filename);
		Scanner in = new Scanner(inFile);

		in.close();

		ArrayList<ArrayList<BigDecimal>> mx = new ArrayList<ArrayList<BigDecimal>>();
		in = new Scanner(inFile);

		int colNum = 0;
		int rowNum = 0;
		while (in.hasNextLine()) {
			String[] currentLine = in.nextLine().trim().split("\\s+");
			mx.add(new ArrayList<BigDecimal>());
			colNum = currentLine.length;
			for (int i = 0; i < currentLine.length; i++) {
				mx.get(rowNum).add(
						new BigDecimal(Double.parseDouble(currentLine[i])));
			}
			rowNum++;
		}
		in.close();

		// fill mx
		Builder<BigMatrix> expectedExcessReturns = BigMatrix.getBuilder(rowNum,
				colNum);
		for (int i = 0; i < mx.size(); i++) {
			for (int j = 0; j < mx.get(i).size(); j++) {
				expectedExcessReturns.set(i, j, mx.get(i).get(j));
			}
		}
		return expectedExcessReturns.build();
	}

	/**
	 * Get returns from prices
	 * @param prices
	 * @return
	 */
	public static BigMatrix getReturns(BigMatrix prices) {
		Builder<BigMatrix> returns = BigMatrix.getBuilder(
				(int) prices.countRows() - 1, (int) prices.countColumns());

		for (int i = 0; i < prices.countRows() - 1; i++) {
			for (int j = 0; j < prices.countColumns(); j++) {
				returns.set(
						i,
						j,
						prices.toBigDecimal(i + 1, j)
								.divide(prices.toBigDecimal(i, j),
										RoundingMode.HALF_UP));
			}
		}
		return returns.build();
	}
}
