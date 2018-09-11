package org.hisrc.gpxtools.gpx.service;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hisrc.gpxtools.gpx.model.WaypointFeatureCollection;
import org.hisrc.gpxtools.gpx.v_1_1.GpxType;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GpxToWaypointFeatureCollectionConverterTest {

	private GpxToWaypointFeatureCollectionConverter converter = new GpxToWaypointFeatureCollectionConverter();

	@Test
	public void bbb() throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(GpxType.class.getPackage().getName());
		Unmarshaller unmarshaller = context.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<GpxType> unmarshalled = (JAXBElement<GpxType>) unmarshaller
				.unmarshal(getClass().getResource("00.gpx"));
		GpxType gpx = unmarshalled.getValue();

		WaypointFeatureCollection waypointFeatureCollection = converter.convert(gpx);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writerFor(WaypointFeatureCollection.class).withDefaultPrettyPrinter().writeValue(System.out,
				waypointFeatureCollection);
	}
}