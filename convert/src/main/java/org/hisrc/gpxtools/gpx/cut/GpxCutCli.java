package org.hisrc.gpxtools.gpx.cut;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;

import javax.xml.bind.JAXBException;

import org.hisrc.gpxtools.args4j.spi.ZonedDateTimeOptionHandler;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class GpxCutCli {

	public static class Configuration {

		@Option(name = "-?", aliases = { "-help", "--help" }, help = true, usage = "Prints this help message")
		private boolean help;

		@Option(name = "-s", aliases = {
				"--start" }, metaVar = "TIME_OFF", usage = "Start date/time (example 2018-01-01T05:43:15Z)", required = false, handler = ZonedDateTimeOptionHandler.class)
		private ZonedDateTime start;

		@Option(name = "-e", aliases = {
				"--end" }, metaVar = "TIME_STOP", usage = "End date/time, (example 2018-01-01T05:43:15Z)", required = false, handler = ZonedDateTimeOptionHandler.class)
		private ZonedDateTime end = null;

		@Argument(metaVar = "INPUT", required = true, index = 0, usage = "Input GPX file, required")
		private File inputGpxFile = null;

		@Argument(metaVar = "OUTPUT", required = false, index = 1, usage = "Output GPX file, defaults to Cut-<INPUT>")
		private File outputGpxFile = null;

		public boolean isHelp() {
			return help;
		}

		public ZonedDateTime getStart() {
			return start;
		}

		public ZonedDateTime getEnd() {
			return end;
		}

		public File getInputGpxFile() {
			return inputGpxFile;
		}

		public File calculateOutputGpxFile() {
			if (outputGpxFile != null) {
				return outputGpxFile;
			} else {
				final File inputGpxDirectory = this.inputGpxFile.getParentFile();
				final String inputGpxFileName = this.inputGpxFile.getName();
				final String outputGpxFileName = "Cut-" + inputGpxFileName;
				final File outputGpxFile = new File(inputGpxDirectory, outputGpxFileName);
				return outputGpxFile;
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
				final GpxFileCutService fileCutService = new GpxFileCutService();
				fileCutService.cut(configuration.getInputGpxFile(), configuration.getStart(), configuration.getEnd(),
						configuration.calculateOutputGpxFile());
			}
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			printUsage(parser);
		}
	}

	private static void printUsage(CmdLineParser parser) {
		System.out.println(
				"Usage: java -cp gpx-tools.jar " + GpxCutCli.class.getName() + " [options...] [INPUT] [OUTPUT]");
		parser.printUsage(System.out);
		System.out.println();
	}

}
