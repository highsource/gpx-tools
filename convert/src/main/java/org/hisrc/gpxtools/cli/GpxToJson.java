package org.hisrc.gpxtools.cli;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hisrc.gpxtools.gpx.model.WaypointFeatureCollection;
import org.hisrc.gpxtools.gpx.service.GpxToWaypointFeatureCollectionConverter;
import org.hisrc.gpxtools.gpx.v_1_1.GpxType;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class GpxToJson {

	private final JAXBContext context;
	{
		try {
			context = JAXBContext.newInstance(GpxType.class.getPackage().getName());
		} catch (JAXBException jaxbex) {
			throw new ExceptionInInitializerError(jaxbex);
		}
	}
	private final GpxToWaypointFeatureCollectionConverter converter = new GpxToWaypointFeatureCollectionConverter();

	public static class Configuration {

		@Option(name = "-?", aliases = { "-help", "--help" }, help = true, usage = "Prints this help message")
		private boolean help;

		// @Option(name = "-i", aliases = {
		// "--input" }, metaVar = "INPUT", usage = "Input GPX file, required", required
		// = true)
		@Argument(metaVar = "INPUT", required = true, index = 0, usage = "Input GPX file, required")
		private File input = null;
		// @Option(name = "-o", aliases = {
		// "--output" }, metaVar = "OUTPUT", usage = "Output JSON file, required",
		// required = true)
		@Argument(metaVar = "OUTPUT", required = false, index = 1, usage = "Output JSON file, defaults to <INPUT>.json")
		private File output = null;

		public boolean isHelp() {
			return help;
		}

		public File getInput() {
			return input;
		}

		public File getOutput() {
			if (output != null) {
				return output;
			} else {
				String inputAbsolutePath = input.getAbsolutePath();
				return new File(inputAbsolutePath.replace(".gpx", ".json"));
			}
		}
	}

	public static void main(String[] args) throws IOException, JAXBException {
		final Configuration configuration = new Configuration();
		CmdLineParser parser = new CmdLineParser(configuration);
		try {
			parser.parseArgument(args);
			if (configuration.isHelp()) {
				printUsage(parser);
			} else {
				final GpxToJson worker = new GpxToJson();
				worker.process(configuration.getInput(), configuration.getOutput());
			}
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			printUsage(parser);
		}
	}

	public void process(File input, File output) throws JAXBException, IOException {
		Unmarshaller unmarshaller = context.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<GpxType> unmarshalled = (JAXBElement<GpxType>) unmarshaller.unmarshal(input);
		GpxType gpx = unmarshalled.getValue();

		WaypointFeatureCollection waypointFeatureCollection = converter.convert(gpx);

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectWriter writer = mapper.writerFor(WaypointFeatureCollection.class).withDefaultPrettyPrinter();

		try (OutputStream os = new FileOutputStream(output)) {
			writer.writeValue(os, waypointFeatureCollection);
		}
	}

	private static void printUsage(CmdLineParser parser) {
		System.out.println("Usage: java -cp gpx-tools-convert.jar " + GpxToJson.class.getName()
				+ " [options...] [INPUT] [OUTPUT]");
		parser.printUsage(System.out);
		System.out.println();
	}
}
