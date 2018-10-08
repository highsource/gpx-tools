package org.hisrc.gpxtools.ffmpeg;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import java.io.IOException;

import org.junit.Test;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

public class DurationTest {

	@Test
	public void retrievesCorrectDuration() throws IOException {
		FFprobe ffprobe = new FFprobe("ffprobe");
		FFmpegProbeResult probeResult = ffprobe.probe("input\\00.mp4");
		FFmpegFormat format = probeResult.getFormat();
		assertThat(format.duration).isCloseTo(1186.228, offset(0.001));
	}
}
