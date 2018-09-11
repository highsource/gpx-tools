package org.hisrc.gpxtools.gpx.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hisrc.gpxtools.gpx.model.WaypointFeature;
import org.hisrc.gpxtools.gpx.model.WaypointFeatureCollection;
import org.hisrc.gpxtools.gpx.v_1_1.GpxType;
import org.hisrc.gpxtools.gpx.v_1_1.TrkType;
import org.hisrc.gpxtools.gpx.v_1_1.TrksegType;
import org.hisrc.gpxtools.gpx.v_1_1.WptType;

public class GpxToWaypointFeatureCollectionConverter {

	public WaypointFeatureCollection convert(GpxType gpx) {
		final List<TrkType> trks = gpx.getTrk();
		List<WaypointFeature> waypointFeatures = trks.stream().flatMap(this::convert).collect(Collectors.toList());
		return new WaypointFeatureCollection(waypointFeatures);
	}

	private Stream<WaypointFeature> convert(TrkType trk) {
		return trk.getTrkseg().stream().flatMap(this::convert);
	}

	private Stream<WaypointFeature> convert(TrksegType trkseg) {
		return trkseg.getTrkpt().stream().map(this::convert);
	}

	private WaypointFeature convert(WptType wpt) {
		double lon = wpt.getLon().doubleValue();
		double lat = wpt.getLat().doubleValue();
		String time = wpt.getTime().toString();
		return new WaypointFeature(time, new double[] { lon, lat });
	}
}
