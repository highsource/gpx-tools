package org.hisrc.gpxtools.args4j.spi;

import java.time.ZonedDateTime;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OneArgumentOptionHandler;
import org.kohsuke.args4j.spi.Setter;

public class ZonedDateTimeOptionHandler extends OneArgumentOptionHandler<ZonedDateTime> {

	public ZonedDateTimeOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super ZonedDateTime> setter) {
		super(parser, option, setter);
	}

	@Override
	protected ZonedDateTime parse(String argument) throws NumberFormatException, CmdLineException {
		return ZonedDateTime.parse(argument);
	}
}
