package org.hisrc.gpxtools.ffmpeg_gpx.cut.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "time"})
public class CutMoment {

	private final String id;
	private final ZonedDateTime time;
	
	public CutMoment(
			@JsonProperty("id")
			String id,
			@JsonProperty("time")
			ZonedDateTime time) {
		this.id = id;
		this.time = time;
	}
	
	public String getId() {
		return id;
	}
	
	public ZonedDateTime getTime() {
		return time;
	}
}
