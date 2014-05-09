package com.ibm.bluej.util.common.visualization;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

import com.ibm.bluej.util.common.*;

public class FunctionCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private FunSV<Pair<Component,Object>> f;
	
	public FunctionCellRenderer() {}
	
	public void setFunction(FunSV<Pair<Component,Object>> f) {
		this.f = f;
	}
	
	public FunctionCellRenderer(FunSV<Pair<Component,Object>> f) {
		this.f = f;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Pair<Component,Object> p = 
				Pair.of(super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column), value);
		if (f != null) f.f(p);
		return p.first;
	}
}
