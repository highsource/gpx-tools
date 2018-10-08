package org.hisrc.gpxtools.feature.model;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Point extends Geometry<double[]> {

	@JsonCreator
	public Point(@JsonProperty("coordinates") double[] coordinates) {
		super(coordinates);
		Validate.isTrue(coordinates.length >= 2);
	}
}
