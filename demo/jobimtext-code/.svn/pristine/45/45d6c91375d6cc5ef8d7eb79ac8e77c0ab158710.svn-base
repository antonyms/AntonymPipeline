/* This file is distributed as part of Chris Biemann's Chinese Whispers Disambiguation package.
 * It is distributed under the Apache License Version 2.0, January 2004 http://www.apache.org/licenses/
 * See LICENSE.TXT for details */


package de.uni_leipzig.asv.coocc;

import java.util.List;
import java.util.Map;

/**
 * This interface summarizes BinFileMultColFile and BinFileMultColRam.
 *
 * @author Stefan Bordag
 */
public interface BinFileMultCol
{

	/**
	 * For a given number returns the row
	 * @param wordNr Integer
	 * @return List::Integer[]
	 */
	public List getData(Integer wordNr);

	/**
	 * Returns the data for the given number up to the given maximum.
	 * @param wordNr
	 * @param maxItems
	 * @return
	 */
	public List getData(Integer number, int maxItems);

	/**
	 * Returns the data for the given number up to the given maximum.
	 * @param number = the first column
	 * @param myColumns = how many columns to return
	 * @param maxItems = at max this count of items
	 * @return
	 */
	public List getData(Integer number, int myColumns, int maxItems);

	/**
	 * Returns the data for the given number up to the given maximum.
	 * @param number = the first column
	 * @param myColumns = how many columns to return
	 * @param maxItems = at max this count of items
	 * @param minCols = for each column each value minimum is specified here
	 * @param maxCols = for each column each value maximum is specified here
	 * @return
	 */
	public List getData(Integer number, int myColumns, int maxItems, int[] minCols, int[] maxCols);

	/**
	 * Returns the data for the given number up to the given maximum in a list.
	 * @param wordNr The number of the item of which to return the columns
	 * @param maxItems How many items to return at max
	 * @param minCols for each column the minimum allowed value (the whoe row will
	 * otherwise be omitted)
	 * @param maxCols for each column the maximum allowed value (the whoe row will
	 * otherwise be omitted)
	 * @return List::Integer[]
	 */
	public List getData(Integer number, int maxItems, int[] minCols, int[] maxCols);

	/**
	 * Returns the data for the given number up to the given maximum in a map.
	 * @param wordNr The number of the item of which to return the columns
	 * @return Map::Integer->Integer[]
	 */
	public Map getDataAsMap(Integer wordNr);

	/**
	 * Returns the data for the given number up to the given maximum.
	 * @param wordNr The number of the item of which to return the columns
	 * @param myColumns How many columns to return
	 * @param maxItems How many items to return at max
	 * @return Map::Integer->Integer[]
	 */
	public Map getDataAsMap(Integer number, int myColumns, int maxItems);

	/**
	 * Returns the data for the given number up to the given maximum in a map.
	 * @param wordNr The number of the item of which to return the columns
	 * @param maxItems How many items to return at max
	 * @return Map::Integer->Integer[]
	 */
	public Map getDataAsMap(Integer number, int maxItems);

	/**
	 * Returns the data for the given number up to the given maximum in a map.
	 * @param wordNr The number of the item of which to return the columns
	 * @param myColumns How many columns to return
	 * @param maxItems How many items to return at max
	 * @param minCols for each column the minimum allowed value (the whoe row will
	 * otherwise be omitted)
	 * @param maxCols for each column the maximum allowed value (the whoe row will
	 * otherwise be omitted)
	 * @return Map::Integer->Integer[]
	 */
	public Map getDataAsMap(Integer number, int myColumns, int maxItems, int[] minCols, int[] maxCols);

	/**
	 * Returns the data for the given number up to the given maximum in a map.
	 * @param wordNr The number of the item of which to return the columns
	 * @param myColumns How many columns to return
	 * @param maxItems How many items to return at max
	 * otherwise be omitted)
	 * @param maxCols for each column the maximum allowed value (the whoe row will
	 * otherwise be omitted)
	 * @return Map::Integer->Integer[]
	 */
	public Map getDataAsMap(Integer number, int maxItems, int[] minCols, int[] maxCols);

	/**
	 * Column here is the column in the initial datafile, count begins with 1.
	 * Return the specified column completely.
	 * @param number
	 * @param col
	 * @return List::Integer
	 */
	public List getSingleColumn(Integer number, int col);

	public List getSingleColumn(Integer number, int maxItems, int col);

}
