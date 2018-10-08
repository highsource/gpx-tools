package org.hisrc.gpxtools.feature.model;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

public class Feature<G extends Geometry<C>, C, P> {

	private G geometry;
	private P properties;

	public Feature(G geometry, P properties) {
		Validate.notNull(geometry);
		Validate.notNull(properties);
		this.geometry = geometry;
		this.properties = properties;
	}

	public String getType() {
		return "Feature";
	}

	public G getGeometry() {
		return geometry;
	}

	public P getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return getProperties().toString();
	}

	@Override
	public final int hashCode() {
		return Objects.hash(this.geometry, this.properties);
	}

	@Override
	public final boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final Feature<G, C, P> that = (Feature<G, C, P>) object;
		return Objects.equals(this.getGeometry(), that.getGeometry())
				&& Objects.equals(this.getProperties(), that.getProperties());
	}

}
