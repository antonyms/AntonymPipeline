/*******************************************************************************
** /*******************************************************************************
* Copyright 2012
* FG Language Technologie
* Technische Universitaet Darmstadt
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/

package org.jobimtext.hadoop.mapreducer;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class SumReducer {

	public static class IntSumReducer extends MapReduceBase implements
			Reducer<Text, IntWritable, Text, IntWritable> {

		public void reduce(Text key, Iterator<IntWritable> values,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			int sum = 0;
			while (values.hasNext()) {
				sum += values.next().get();
			}
			output.collect(key, new IntWritable(sum));

		}

	}

	public static class DoubleSumReducer extends MapReduceBase implements
			Reducer<Text, DoubleWritable, Text, DoubleWritable> {

		public void reduce(Text key, Iterator<DoubleWritable> values,
				OutputCollector<Text, DoubleWritable> output, Reporter reporter)
				throws IOException {
			double sum = 0;
			while (values.hasNext()) {
				sum += values.next().get();
			}
			output.collect(key, new DoubleWritable(sum));

		}

		public static class FloatSumReducer extends MapReduceBase implements
				Reducer<Text, FloatWritable, Text, FloatWritable> {

			public void reduce(Text key, Iterator<FloatWritable> values,
					OutputCollector<Text, FloatWritable> output,
					Reporter reporter) throws IOException {
				float sum = 0;
				while (values.hasNext()) {
					sum += values.next().get();
				}
				output.collect(key, new FloatWritable(sum));

			}
		}
	}
}
