package org.hisrc.gpxtools.ffmpeg.cut;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class FfmpegCutService {

	private final String ffprobePath;
	private final String ffmpegPath;
	private FfprobeOffsetService offsetService; 

	public FfmpegCutService(String ffprobePath, String ffmpegPath) {
		Objects.requireNonNull(ffprobePath, "ffprobePath must not be null.");
		Objects.requireNonNull(ffmpegPath, "ffmpegPath must not be null.");
		this.ffprobePath = ffprobePath;
		this.ffmpegPath = ffmpegPath;
		this.offsetService = new FfprobeOffsetService(ffprobePath);
	}

	public void cut(File inputVideoFile, ZonedDateTime start, ZonedDateTime end, File outputVideoFile) throws IOException {
		
		long cutStartMillis = this.offsetService.calculateOffsetInMillis(inputVideoFile, start);
		long cutEndMillis = this.offsetService.calculateOffsetInMillis(inputVideoFile, end);
		this.cut(inputVideoFile, cutStartMillis, cutEndMillis, outputVideoFile);
		System.out.println(MessageFormat.format("Cutting [{0}] from [{1}] to [{2}] and saving result to [{3}].",
				inputVideoFile.getAbsolutePath(), start, end, outputVideoFile.getAbsolutePath()));
	}

	private void cut(File inputVideoFile, long cutStartMillis, long cutEndMillis, File outputVideoFile)
			throws IOException {

		FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
		FFmpegBuilder ffmpegBuilder = new FFmpegBuilder()
				//
				.addInput(inputVideoFile.getAbsolutePath())
				//
				.overrideOutputFiles(true)
				//
				.addOutput(outputVideoFile.getAbsolutePath())
				//
				.setStartOffset(cutStartMillis, TimeUnit.MILLISECONDS)
				//
				.setDuration(cutEndMillis - cutStartMillis, TimeUnit.MILLISECONDS)
				//
				.setVideoCodec("copy")
				//
				.disableAudio()
				//
				.done();

		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, new FFprobe(ffprobePath));

		executor.createJob(ffmpegBuilder).run();
	}
}
