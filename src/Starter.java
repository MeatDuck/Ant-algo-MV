import java.math.BigDecimal;
import java.util.List;

import org.ojalgo.finance.portfolio.MarkowitzModel;
import org.ojalgo.matrix.BigMatrix;

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

		final MarkowitzModel markowitzModel = new MarkowitzModel(cov,
				expectedExcessReturns);

		// setup
		markowitzModel.setShortingAllowed(false);

		// 0 <= |X| <= 1
		for (int i = 0; i < 1; i++) {
			markowitzModel.setLowerLimit(i, new BigDecimal(0.0));
			markowitzModel.setUpperLimit(i, new BigDecimal(1.0));
		}

		double nRisk = 100; //0 - 2000
		markowitzModel.setRiskAversion(nRisk);

		final List<BigDecimal> re = markowitzModel.getWeights();
		System.out.println("=======assets====================");
		for (int i = 0; i < re.size(); i++) {
			System.out.println(re.get(i));
		}

		System.out.println("=======result====================");
		System.out.println("Mean return " + markowitzModel.getMeanReturn());
		System.out.println("Return variance " + markowitzModel.getReturnVariance());
	}
}
