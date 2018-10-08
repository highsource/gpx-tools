package org.hisrc.gpxtools.ffmpeg.cut;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;

import org.hisrc.gpxtools.args4j.spi.ZonedDateTimeOptionHandler;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class FfmpegCutCli {

	public static class Configuration {

		@Option(name = "-?", aliases = { "-help", "--help" }, help = true, usage = "Prints this help message")
		private boolean help;

		@Option(name = "-ffprobe", aliases = {
				"--ffprobe-path" }, metaVar = "FFPROBE_PATH", usage = "Path of the ffprobe executable, optional, defaults to ffprobe", required = false)
		private String ffprobe = "ffprobe";

		@Option(name = "-ffmpeg", aliases = {
				"--ffmpeg-path" }, metaVar = "FFMPEG_PATH", usage = "Path of the ffmpeg executable, optional, defaults to ffmpeg", required = false)
		private String ffmpegPath = "ffmpeg";

		@Option(name = "-s", aliases = {
				"--start" }, metaVar = "TIME_OFF", usage = "Start date/time (example 2018-01-01T05:43:15Z)", required = false, handler = ZonedDateTimeOptionHandler.class)
		private ZonedDateTime start;

		@Option(name = "-e", aliases = {
				"--end" }, metaVar = "TIME_STOP", usage = "End date/time, (example 2018-01-01T05:43:15Z)", required = false, handler = ZonedDateTimeOptionHandler.class)
		private ZonedDateTime end = null;

		@Argument(metaVar = "INPUT", required = true, index = 0, usage = "Input video file, required")
		private File inputVideoFile = null;

		@Argument(metaVar = "OUTPUT", required = false, index = 1, usage = "Output video file, defaults to <INPUT>-<START>-<START>.<INPUT_EXT>")
		private File outputVideoFileOrDirectory = null;

		public boolean isHelp() {
			return help;
		}

		public String getFfprobePath() {
			return ffprobe;
		}

		public String getFfmpegPath() {
			return ffmpegPath;
		}

		public ZonedDateTime getStart() {
			return start;
		}

		public ZonedDateTime getEnd() {
			return end;
		}

		public File getInputVideoFile() {
			return inputVideoFile;
		}

		public File calculateOutputVideoFile(long startOffsetInMillis, long endOffsetInMillis) throws IOException {
			if (!outputVideoFileOrDirectory.exists() || outputVideoFileOrDirectory.isDirectory()) {
				String inputVideoFileName = inputVideoFile.getName();
				int lastDotPosition = inputVideoFileName.lastIndexOf('.');
				final String name;
				final String extension;
				if (lastDotPosition < 0) {
					name = inputVideoFileName;
					extension = "";
				} else {
					name = inputVideoFileName.substring(0, lastDotPosition);
					extension = inputVideoFileName.substring(lastDotPosition);
				}
				final String outputVideoFileName = name + "-" + startOffsetInMillis + "-" + endOffsetInMillis
						+ extension;
				return new File(outputVideoFileOrDirectory, outputVideoFileName);
			} else {
				return outputVideoFileOrDirectory;
			}
		}

	}

	public static void main(String[] args) throws IOException {
		final Configuration configuration = new Configuration();
		CmdLineParser parser = new CmdLineParser(configuration);
		try {
			parser.parseArgument(args);
			if (configuration.isHelp()) {
				printUsage(parser);
			} else {
				FfprobeOffsetService offsetService = new FfprobeOffsetService(configuration.getFfprobePath());
				long start = offsetService.calculateOffsetInMillis(configuration.getInputVideoFile(),
						configuration.getStart());
				long end = offsetService.calculateOffsetInMillis(configuration.getInputVideoFile(),
						configuration.getEnd());
				FfmpegCutService worker = new FfmpegCutService(configuration.getFfprobePath(),
						configuration.getFfmpegPath());
				final File outputVideoFile = configuration.calculateOutputVideoFile(start, end);
				worker.cut(configuration.getInputVideoFile(), configuration.getStart(), configuration.getEnd(),
						outputVideoFile);
			}
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			printUsage(parser);
		}
	}

	private static void printUsage(CmdLineParser parser) {
		System.out.println(
				"Usage: java -cp gpx-tools.jar " + FfmpegCutCli.class.getName() + " [options...] [INPUT] [OUTPUT]");
		parser.printUsage(System.out);
		System.out.println();
	}

}
