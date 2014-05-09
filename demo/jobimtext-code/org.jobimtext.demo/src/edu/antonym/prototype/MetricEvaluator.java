package edu.antonym.prototype;

import java.io.IOException;
import java.util.List;

public interface MetricEvaluator {
	public double score(WordMetric metric) throws IOException;
}
