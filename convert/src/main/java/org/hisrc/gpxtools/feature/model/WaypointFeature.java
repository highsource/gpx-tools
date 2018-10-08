package org.hisrc.gpxtools.feature.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WaypointFeature extends Feature<Point, double[], WaypointFeature.Properties> {

	public WaypointFeature(String time, long timestamp, double[] coordinates) {
		super(new Point(coordinates), new WaypointFeature.Properties(time, timestamp));
	}

	public static class Properties {

		private final String time;
		private final long timestamp;

		@JsonCreator
		public Properties(@JsonProperty("time") String time, @JsonProperty("ts") long timestamp) {
			this.time = time;
			this.timestamp = timestamp;
		}

		public String getTime() {
			return time;
		}

		public long getTimestamp() {
			return timestamp;
		}
	}
}
