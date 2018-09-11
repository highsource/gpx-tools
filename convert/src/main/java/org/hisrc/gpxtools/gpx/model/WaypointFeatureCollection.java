package org.hisrc.gpxtools.gpx.model;

import java.util.List;

public class WaypointFeatureCollection
		extends FeatureCollection<WaypointFeature, Point, double[], WaypointFeature.Properties> {

	public WaypointFeatureCollection(List<WaypointFeature> features) {
		super(features);
	}

}
