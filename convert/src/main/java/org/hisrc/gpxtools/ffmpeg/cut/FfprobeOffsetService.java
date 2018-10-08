package org.hisrc.gpxtools.ffmpeg.cut;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

public class FfprobeOffsetService {

	private final FFprobe ffprobe;

	public FfprobeOffsetService(String ffprobePath) {
		Objects.requireNonNull(ffprobePath, "ffprobePath must not be null.");
		try {
			this.ffprobe = new FFprobe(ffprobePath);
		} catch (IOException ioex) {
			throw new ExceptionInInitializerError(ioex);
		}
	}

	public long calculateOffsetInMillis(File inputVideoFile, ZonedDateTime offsetTime) throws IOException {
		Objects.requireNonNull(inputVideoFile, "inputVideoFile must not be null.");
		Objects.requireNonNull(offsetTime, "offsetTime must not be null.");

		FFmpegProbeResult probeResult = ffprobe.probe(inputVideoFile.getAbsolutePath());
		FFmpegFormat format = probeResult.getFormat();

		ZonedDateTime creationTime = ZonedDateTime.parse(format.tags.get("creation_time"));

		final double inputVideoDuration = format.duration;
		final ZonedDateTime inputVideoStartTime = creationTime.minus(Math.round(Math.floor(inputVideoDuration * 1000)),
				ChronoUnit.MILLIS);

		return Duration.between(inputVideoStartTime, offsetTime).toMillis();
	}
}
