package org.hisrc.gpxtools.ffmpeg_gpx.cut;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hisrc.gpxtools.cli.GpxToJson;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class GpxVideoCutCli {

	public static class Configuration {

		@Option(name = "-?", aliases = { "-help", "--help" }, help = true, usage = "Prints this help message")
		private boolean help;

		@Option(name = "-ffprobe", aliases = {
				"--ffprobe-path" }, metaVar = "FFPROBE_PATH", usage = "Path of the ffprobe executable, optional, defaults to ffprobe", required = false)
		private String ffprobe = "ffprobe";

		@Option(name = "-ffmpeg", aliases = {
				"--ffmpeg-path" }, metaVar = "FFMPEG_PATH", usage = "Path of the ffmpeg executable, optional, defaults to ffmpeg", required = false)
		private String ffmpegPath = "ffmpeg";

		@Argument(metaVar = "INPUT_GPX_FILE", required = true, index = 0, usage = "Input GPX file, required")
		private File inputGpxFile = null;

		@Argument(metaVar = "INPUT_VIDEO_FILE", required = true, index = 1, usage = "Input video file, required")
		private File inputVideoFile = null;

		@Argument(metaVar = "INPUT_CUT_MOMENTS_FILE", required = true, index = 2, usage = "Input cut moments file, required")
		private File inputCutMomentsFile = null;

		@Argument(metaVar = "OUTPUT_DIRECTORY", required = false, index = 3, usage = "Output directory, defaults to the directory of <INPUT_GPX_FILE>")
		private File outputDirectory = null;

		public boolean isHelp() {
			return help;
		}

		public String getFfprobePath() {
			return ffprobe;
		}

		public String getFfmpegPath() {
			return ffmpegPath;
		}

		public File getInputVideoFile() {
			return inputVideoFile;
		}

		public File getInputGpxFile() {
			return inputGpxFile;
		}

		public File getInputCutMomentsFile() {
			return inputCutMomentsFile;
		}

		public File calculateOutputDirectory() {
			if (outputDirectory != null) {
				return outputDirectory;
			} else {
				return this.inputGpxFile.getParentFile();
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
				final GpxFfmpegFileCutService cutService = new GpxFfmpegFileCutService(configuration.getFfprobePath(),
						configuration.getFfmpegPath());
				cutService.cut(configuration.getInputGpxFile(), configuration.getInputVideoFile(),
						configuration.getInputCutMomentsFile(), configuration.calculateOutputDirectory());
			}
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			printUsage(parser);
		}
	}

	private static void printUsage(CmdLineParser parser) {
		System.out.println("Usage: java -cp gpx-tools.jar " + GpxToJson.class.getName()
				+ " [options...] [INPUT_GPX_FILE] [INPUT_VIDEO_FILE] [INPUT_CUT_MOMENTS_FILE] [OUTPUT_DIRECTORY]");
		parser.printUsage(System.out);
		System.out.println();
	}

}
