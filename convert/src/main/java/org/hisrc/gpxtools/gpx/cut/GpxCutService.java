package org.hisrc.gpxtools.gpx.cut;

import static org.jvnet.jaxb2_commons.locator.util.LocatorUtils.item;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.hisrc.gpxtools.gpx.v_1_1.GpxType;
import org.hisrc.gpxtools.gpx.v_1_1.WptType;
import org.jvnet.jaxb2_commons.lang.CopyStrategy2;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.DefaultRootObjectLocator;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;

public class GpxCutService {

	public GpxType cut(GpxType inputGpx, ZonedDateTime start, ZonedDateTime end) {

		final ObjectLocator objectLocator = new DefaultRootObjectLocator(inputGpx);
		final CopyStrategy2 copyStrategy = new JAXBCopyStrategy() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			protected Object copyInternal(ObjectLocator locator, List list) {
				final List copy = new ArrayList(list.size());
				for (int index = 0; index < list.size(); index++) {
					final Object element = list.get(index);
					if (element instanceof WptType) {
						final Object copyElement = copyWaypoint(item(locator, index, element), (WptType) element);
						if (copyElement != null) {
							copy.add(copyElement);
						}

					} else {
						final Object copyElement = copy(item(locator, index, element), element);
						copy.add(copyElement);
					}

				}
				return copy;
			}

			private Object copyWaypoint(ObjectLocator locator, WptType waypoint) {
				XMLGregorianCalendar time = waypoint.getTime();
				if (time != null) {
					final WptType copyWaypoint = (WptType) super.copy(locator, waypoint);

					final String timeString = time.toXMLFormat();

					final ZonedDateTime dateTime = ZonedDateTime.parse(timeString);

					if (!dateTime.isBefore(start) && !dateTime.isAfter(end)) {
						return copyWaypoint;
					} else {
						return null;
					}
				} else {
					return null;
				}
			}

		};
		final GpxType outputGpx = (GpxType) inputGpx.copyTo(objectLocator, new GpxType(), copyStrategy);
		return outputGpx;
	}
}
