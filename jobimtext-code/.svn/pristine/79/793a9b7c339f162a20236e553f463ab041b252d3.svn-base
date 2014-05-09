package com.ibm.bluej.util.common.visualization;

import java.text.*;

import javax.swing.table.*;

public class NumberFormatCellRenderer extends FunctionCellRenderer {
	private static final long serialVersionUID = 1L;
	public NumberFormatCellRenderer() {
		format = new DecimalFormat("0.000");
	}
	public NumberFormatCellRenderer(DecimalFormat f) {
		this.format = f;
	}
	public NumberFormatCellRenderer(String fstr) {
		format = new DecimalFormat(fstr);
	}
	DecimalFormat format;
	public void setValue(Object value) {
		if (value != null)
				value = format.format(value);
		super.setValue(value);
	}
}