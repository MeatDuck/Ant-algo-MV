package controller;

import java.math.BigDecimal;
import java.util.List;

import org.ojalgo.finance.portfolio.MarkowitzModel;
import org.ojalgo.matrix.BigMatrix;

public class Solver {
	final MarkowitzModel markowitzModel;

	public Solver(BigMatrix cov, BigMatrix expectedExcessReturns) {
		markowitzModel = new MarkowitzModel(cov, expectedExcessReturns);
	}

	public void setShortingAllowed(Boolean isShorting) {
		markowitzModel.setShortingAllowed(isShorting);
	}

	public void setLowerLimit(int i, BigDecimal lowLimit) {
		markowitzModel.setLowerLimit(i, lowLimit);
	}

	public void setUpperLimit(int i, BigDecimal upperLimit) {
		markowitzModel.setUpperLimit(i, upperLimit);
	}

	public void setRiskAversion(double aFactor) {
		markowitzModel.setRiskAversion(aFactor);
	}

	public Solution solve() {
		Solution solution = new Solution();
		solution.weights = markowitzModel.getWeights();
		solution.meanReturn = markowitzModel.getMeanReturn();
		solution.returnVariance = markowitzModel.getReturnVariance();
		return solution;
	}

	class Solution {
		List<BigDecimal> weights;
		double meanReturn;
		double returnVariance;
		
		@Override
		public String toString() {
			StringBuilder out = new StringBuilder("=======assets========\n");
			for (int i = 0; i < weights.size(); i++) {
				out.append(weights.get(i));
				out.append("\n");
			}

			out.append("=======result=========\n");
			out.append("Mean return ");
			out.append(String.format("%.10f", meanReturn));
			out.append("\n");
			out.append("Return variance ");
			out.append(String.format("%.10f", markowitzModel.getReturnVariance()));
			return out.toString();
		}
	}
}
