package edu.antonym.nn.training;

import cc.mallet.optimize.Optimizable;
import cc.mallet.types.MatrixOps;

public class RegularizedOptimizable implements Optimizable.ByGradientValue {
	Optimizable.ByGradientValue base;

	double target;

	boolean cacheDirty;
	double cachedValue;
	double[] cachedGradient;

	public RegularizedOptimizable(Optimizable.ByGradientValue base,
			double target) {
		this.base = base;
		this.target = target;

		this.cacheDirty = true;

		cachedGradient = new double[base.getNumParameters()];
	}

	@Override
	public int getNumParameters() {
		return base.getNumParameters();
	}

	@Override
	public double getParameter(int arg0) {
		return base.getParameter(arg0);
	}

	@Override
	public void getParameters(double[] arg0) {
		base.getParameters(arg0);
	}

	@Override
	public void setParameter(int arg0, double arg1) {
		base.setParameter(arg0, arg1);
		cacheDirty = true;
	}

	@Override
	public void setParameters(double[] arg0) {
		setParameters(arg0);
		cacheDirty = true;
	}

	void refreshCache() {
		cachedValue = base.getValue();
		base.getValueGradient(cachedGradient);
		double[] parameters = new double[base.getNumParameters()];
		double norm = MatrixOps.twoNormSquared(parameters) - target;
		if (norm > 0) {
			cachedValue -= norm;
			for (int i = 0; i < parameters.length; i++) {
				cachedGradient[i] -= 2 * parameters[i];
			}
		} else {
			cachedValue += norm;
			for (int i = 0; i < parameters.length; i++) {
				cachedGradient[i] += 2 * parameters[i];
			}
		}
		cacheDirty = false;
	}

	@Override
	public double getValue() {
		if (cacheDirty) {
			refreshCache();
		}
		return cachedValue;

	}

	@Override
	public void getValueGradient(double[] arg0) {
		if (cacheDirty) {
			refreshCache();
		}
		System.arraycopy(cachedGradient, 0, arg0, 0, cachedGradient.length);
	}

}
