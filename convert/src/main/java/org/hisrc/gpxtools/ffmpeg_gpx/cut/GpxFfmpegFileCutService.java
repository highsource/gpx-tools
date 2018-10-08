package org.hisrc.gpxtools.ffmpeg_gpx.cut;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.hisrc.gpxtools.ffmpeg.cut.FfmpegCutService;
import org.hisrc.gpxtools.ffmpeg_gpx.cut.model.CutMoment;
import org.hisrc.gpxtools.gpx.cut.GpxFileCutService;
import org.hisrc.gpxtools.gpx.v_1_1.GpxType;
import org.hisrc.gpxtools.gpx.v_1_1.TrkType;
import org.hisrc.gpxtools.gpx.v_1_1.TrksegType;
import org.hisrc.gpxtools.gpx.v_1_1.WptType;

public class GpxFfmpegFileCutService {

	private GpxFfmpegCutMomentsService cutMomentsService = new GpxFfmpegCutMomentsService();
	private GpxFileCutService gpxFileCutService = new GpxFileCutService();
	private FfmpegCutService videoFileCutService;

	public GpxFfmpegFileCutService(String ffprobePath, String ffmpegPath) {
		Objects.requireNonNull(ffprobePath, "ffprobePath must not be null.");
		Objects.requireNonNull(ffmpegPath, "ffmpegPath must not be null.");
		this.videoFileCutService = new FfmpegCutService(ffprobePath, ffmpegPath);
	}

	public void cut(File inputGpxFile, File inputVideoFile, File cutMomentsFile, File outputDirectory)
			throws IOException, JAXBException {
		final List<CutMoment> loadCutMoments = cutMomentsService.loadCutMoments(cutMomentsFile);

		for (int index = 0; index < loadCutMoments.size() - 1; index++) {
			final CutMoment startCutMoment = loadCutMoments.get(index);
			final CutMoment endCutMoment = loadCutMoments.get(index + 1);

			final String inputGpxFilename = inputGpxFile.getName();
			final String inputVideoFilename = inputVideoFile.getName();
			String startId = startCutMoment.getId();
			String endId = endCutMoment.getId();
			if (!Objects.equals(startId, endId)) {
				final String outputGpxFilename = startId + "-" + endId + "-" + inputGpxFilename;
				final String outputVideoFilename = startId + "-" + endId + "-" + inputVideoFilename;
				final File outputGpxFile = new File(outputDirectory, outputGpxFilename);
				final File outputVideoFile = new File(outputDirectory, outputVideoFilename);
				final ZonedDateTime startCutMomentTime = startCutMoment.getTime();
				final ZonedDateTime endCutMomentTime = endCutMoment.getTime();
				final GpxType outputGpx = gpxFileCutService.cut(inputGpxFile, startCutMomentTime, endCutMomentTime,
						outputGpxFile);

				ZonedDateTime start = createWaypointsStream(outputGpx).map(WptType::getTime).filter(Objects::nonNull)
						.map(XMLGregorianCalendar::toXMLFormat).map(ZonedDateTime::parse).min(Comparator.naturalOrder())
						.orElse(startCutMomentTime);

				ZonedDateTime end = createWaypointsStream(outputGpx).map(WptType::getTime).filter(Objects::nonNull)
						.map(XMLGregorianCalendar::toXMLFormat).map(ZonedDateTime::parse).max(Comparator.naturalOrder())
						.orElse(startCutMomentTime);

				this.videoFileCutService.cut(inputVideoFile, start, end, outputVideoFile);

			}
		}
	}

	private Stream<WptType> createWaypointsStream(final GpxType gpx) {
		return gpx.getTrk().stream().map(TrkType::getTrkseg).flatMap(Collection::stream).map(TrksegType::getTrkpt)
				.flatMap(Collection::stream);
	}
}
