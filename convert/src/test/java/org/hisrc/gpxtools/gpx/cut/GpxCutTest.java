package org.hisrc.gpxtools.gpx.cut;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hisrc.gpxtools.gpx.v_1_1.GpxType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import java.time.ZonedDateTime;

public class GpxCutTest {

	private GpxCutService sut = new GpxCutService();

	@Test
	public void cuts() throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(GpxType.class.getPackage().getName());
		Unmarshaller unmarshaller = context.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<GpxType> unmarshalled = (JAXBElement<GpxType>) unmarshaller
				.unmarshal(getClass().getResource("/00.gpx"));
		GpxType gpx = unmarshalled.getValue();

		GpxType cutGpx = sut.cut(gpx, ZonedDateTime.parse("2018-09-04T06:17:51Z"), ZonedDateTime.parse("2018-09-04T06:17:56Z"));
		assertThat(cutGpx.getTrk().get(0).getTrkseg().get(0).getTrkpt()).hasSize(6);
	}
}
