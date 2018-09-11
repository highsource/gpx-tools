package org.hisrc.gpxtools.gpx.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WaypointFeature extends Feature<Point, double[], WaypointFeature.Properties> {

	public WaypointFeature(String time, double[] coordinates) {
		super(new Point(coordinates), new WaypointFeature.Properties(time));
	}

	public static class Properties {

		private final String time;

		@JsonCreator
		public Properties(@JsonProperty("time") String time) {
			this.time = time;
		}

		public String getTime() {
			return time;
		}
	}
}
