package org.hisrc.gpxtools.gpx.v_1_1;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class UnmarshallerTest {

	@Test
	public void createsUnmarshaller() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(GpxType.class.getPackage().getName());
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		assertThat(unmarshaller).isNotNull();
	}

	@Test
	public void unmarshallsSample() throws JAXBException {

		final JAXBContext context = JAXBContext.newInstance(GpxType.class.getPackage().getName());
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final Object unmarshalled = unmarshaller.unmarshal(getClass().getResource("00.gpx"));
		assertThat(unmarshalled).isExactlyInstanceOf(JAXBElement.class);
		@SuppressWarnings("rawtypes")
		final Object payload = ((JAXBElement) unmarshalled).getValue();
		assertThat(payload).isExactlyInstanceOf(GpxType.class);
		GpxType gpx = (GpxType) payload;
		List<TrkType> trks = gpx.getTrk();
		assertThat(trks).hasSize(1);
		TrkType trk = trks.get(0);
		List<TrksegType> trksegs = trk.getTrkseg();
		assertThat(trksegs).hasSize(1);
		TrksegType trkseg = trksegs.get(0);
		List<WptType> trkpts = trkseg.getTrkpt();
		assertThat(trkpts).hasSize(1168);
	}
}
