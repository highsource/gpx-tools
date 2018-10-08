package org.hisrc.gpxtools.gpx.convert;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hisrc.gpxtools.feature.model.WaypointFeatureCollection;
import org.hisrc.gpxtools.gpx.cut.GpxToWaypointFeatureCollectionConverter;
import org.hisrc.gpxtools.gpx.v_1_1.GpxType;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.*;

public class GpxToWaypointFeatureCollectionConverterTest {

	private GpxToWaypointFeatureCollectionConverter converter = new GpxToWaypointFeatureCollectionConverter();

	@Test
	public void converts() throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(GpxType.class.getPackage().getName());
		Unmarshaller unmarshaller = context.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<GpxType> unmarshalled = (JAXBElement<GpxType>) unmarshaller
				.unmarshal(getClass().getResource("/00.gpx"));
		GpxType gpx = unmarshalled.getValue();

		WaypointFeatureCollection waypointFeatureCollection = converter.convert(gpx);

		final ObjectMapper mapper = new ObjectMapper();
		final Writer writer = new StringWriter();
		mapper.writerFor(WaypointFeatureCollection.class).withDefaultPrettyPrinter().writeValue(writer,
				waypointFeatureCollection);
		
		assertThat(writer.toString()).contains("  \"type\" : \"FeatureCollection\"");
	}
}