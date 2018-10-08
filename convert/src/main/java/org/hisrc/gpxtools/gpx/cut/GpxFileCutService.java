package org.hisrc.gpxtools.gpx.cut;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.ZonedDateTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.hisrc.gpxtools.gpx.v_1_1.GpxType;
import org.hisrc.gpxtools.gpx.v_1_1.ObjectFactory;

public class GpxFileCutService {

	private GpxCutService cutService = new GpxCutService();

	private JAXBContext context;
	{
		try {
			context = JAXBContext.newInstance(GpxType.class.getPackage().getName());
		} catch (JAXBException jaxbex) {
			throw new ExceptionInInitializerError(jaxbex);
		}
	}

	private ObjectFactory objectFactory = new ObjectFactory();

	public GpxType cut(File inputGpxFile, ZonedDateTime start, ZonedDateTime end, File outputGpxFile)
			throws IOException {
		final GpxType inputGpx = unmarshalInputGpx(inputGpxFile);
		System.out.println(MessageFormat.format("Cutting [{0}] from [{1}] to [{2}] and saving result to [{3}].",
				inputGpxFile.getAbsolutePath(), start, end, outputGpxFile.getAbsolutePath()));
		final GpxType outputGpx = cutService.cut(inputGpx, start, end);
		marshalOutputGpx(outputGpx, outputGpxFile);
		return outputGpx;
	}

	private GpxType unmarshalInputGpx(File inputGpx) throws IOException {
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			@SuppressWarnings("unchecked")
			JAXBElement<GpxType> unmarshalled = (JAXBElement<GpxType>) unmarshaller.unmarshal(inputGpx);
			GpxType gpx = unmarshalled.getValue();
			return gpx;
		} catch (JAXBException jaxbex) {
			throw new IOException("Error unmarshalling the input GPX.", jaxbex);
		}
	}

	private void marshalOutputGpx(final GpxType outputGpx, final File outputGpxFile) throws IOException {
		try {
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(objectFactory.createGpx(outputGpx), outputGpxFile);
		} catch (JAXBException jaxbex) {
			throw new IOException("Error marshalling the output GPX.", jaxbex);
		}
	}
}
