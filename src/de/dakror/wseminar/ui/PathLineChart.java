package de.dakror.wseminar.ui;

import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;

/**
 * @author Maximilian Stark | Dakror
 *
 */
public class PathLineChart<X, Y> extends LineChart<X, Y> {
	public PathLineChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
		super(xAxis, yAxis);
	}
	
	public PathLineChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<Series<X, Y>> data) {
		super(xAxis, yAxis, data);
	}
	
	public Node getChartLegend() {
		return super.getLegend();
	}
}
