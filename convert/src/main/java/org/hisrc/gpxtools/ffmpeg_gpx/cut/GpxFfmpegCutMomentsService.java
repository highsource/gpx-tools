package org.hisrc.gpxtools.ffmpeg_gpx.cut;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hisrc.gpxtools.ffmpeg_gpx.cut.model.CutMoment;
//import org.hisrc.gpxtools.gpx.cut.GpxCutWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class GpxFfmpegCutMomentsService {

	Logger logger = LoggerFactory.getLogger(GpxFfmpegCutMomentsService.class);

	public List<CutMoment> loadCutMoments(File cutMomentsFile) throws IOException {

		final CsvMapper mapper = new CsvMapper();
		mapper.registerModule(new JavaTimeModule());
		final CsvSchema schema = mapper.schemaFor(CutMoment.class).withHeader().withColumnSeparator(';');
		final ObjectReader reader = mapper.readerFor(CutMoment.class).with(schema);
		final List<CutMoment> cutMoments = new ArrayList<>();
		try {
			final Iterator<CutMoment> cutMomentsIterator = reader.readValues(cutMomentsFile);

			while (cutMomentsIterator.hasNext()) {
				final CutMoment cutMoment = cutMomentsIterator.next();
				cutMoments.add(cutMoment);
			}
		} catch (Exception ex) {
			throw new IOException("Could not read deserialize the object.", ex);
		}
		return cutMoments;
	}
}