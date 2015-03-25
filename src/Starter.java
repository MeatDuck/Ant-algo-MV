import java.math.BigDecimal;

import org.ojalgo.matrix.BigMatrix;

import controller.Solver;

public class Starter {
	public static void main(String[] args) throws Exception {
		BigMatrix priceMx = MatrixHelper
				.createDoubleMatrixFromFile("data/final.txt");
		BigMatrix returnsMx = MatrixHelper.getReturns(priceMx);
		if (returnsMx.isEmpty()) {
			return;
		}

		BigMatrix cov = MatrixHelper.makeCovarianceMatrix(returnsMx);
		BigMatrix expectedExcessReturns = MatrixHelper
				.getExcessReturns(returnsMx);

		Solver markowitzModel = new Solver(cov, expectedExcessReturns);

		// setup
		markowitzModel.setShortingAllowed(false);

		// 0 <= |X| <= 1
		for (int i = 0; i < 1; i++) {
			markowitzModel.setLowerLimit(i, new BigDecimal(0.0));
			markowitzModel.setUpperLimit(i, new BigDecimal(1.0));
		}

		double nRisk = 100; // 0 - 2000
		markowitzModel.setRiskAversion(nRisk);

		System.out.println(markowitzModel.solve());
	}
}
