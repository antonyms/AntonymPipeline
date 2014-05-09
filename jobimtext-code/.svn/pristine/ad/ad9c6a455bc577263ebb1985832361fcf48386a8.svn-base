package com.ibm.bluej.util.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CollectionUtil {

  /**
   * Given a sequence, partitions it into roughly numFolds equal sized folds. The last fold will
   * contain fewer items than the others if the number of items do not divide evenly.
   */
  public static <T> List<Collection<T>> partitionToFolds(Collection<T> seq, int numFolds) {
    List<Collection<T>> folds = new ArrayList<Collection<T>>(numFolds);
    for (int i = 0; i < numFolds; i++) {
      folds.add(new LinkedList<T>());
    }

    int index = 0;
    for (T element : seq) {
      folds.get(index % numFolds).add(element);
      index++;
    }
    return folds;
  }

  /**
   * This helps you convert a collection of items into a series of train/test divisions for the
   * purpose of running cross-validation or jack-knifing.
   * 
   * First, this splits the data into numDivisions folds. Given a sequence of items, produces a list
   * of divisions. Each division contains a pair of collections which represent the training and
   * testing set. Each fold of the data will be used as the test set in only one division.
   */
  public static <T> List<Pair<Collection<T>, Collection<T>>> partitionToCrossvalidatedTrainTestDivisions(
          Collection<T> seq, int numDivisions) {
    List<Pair<Collection<T>, Collection<T>>> divisions = new ArrayList<Pair<Collection<T>, Collection<T>>>(numDivisions);
    List<Collection<T>> folds = partitionToFolds(seq, numDivisions);

    for (int i = 0; i < numDivisions; i++) {
      Collection<T> test = folds.get(i);
      Collection<T> train = new LinkedList<T>();
      for (int j = 0; j < numDivisions; j++) {
        if (i == j) {
          continue;
        }
        train.addAll(folds.get(j));
      }
      divisions.add(Pair.of(train, test));
    }

    return divisions;
  }
}
