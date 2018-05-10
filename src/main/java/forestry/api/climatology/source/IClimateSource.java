/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.climatology.source;

import forestry.api.climate.ClimateType;
import forestry.api.climate.IClimateState;
import forestry.api.climatology.IClimateLogic;
import forestry.api.core.INbtReadable;
import forestry.api.core.INbtWritable;

/**
 * A climate source is stored in a {@link IClimateSourceProxy}. It is used by the {@link IClimateSourceLogic}s to change the climate of a {@link IClimateLogic}.
 * One {@link IClimateSource} can only be used by one {@link IClimateSourceLogic} at the same time.
 */
public interface IClimateSource<P extends IClimateSourceProxy> extends INbtWritable, INbtReadable {

	/**
	 * The range of this source.
	 */
	float getBoundaryModifier(ClimateType type, boolean boundaryUp);

	/**
	 * @return true if this source affects this sourceType of climate.
	 */
	boolean affectsClimateType(ClimateType type);

	/**
	 * @param previousState the {@link IClimateState} that the source has to work on.
	 * @param targetState   the by the {@link IClimateSourceLogic} targeted {@link IClimateState}.
	 */
	IClimateState work(IClimateLogic logic, IClimateState previousState, IClimateState targetState, IClimateState currentState);

	/**
	 * @return true if source has changed the climate at the last work circle.
	 */
	boolean isActive();

	/**
	 * @return A copy of the current state of this source.
	 */
	IClimateState getState();

	P getProxy();

}