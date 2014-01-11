/**
 * @name PathItem
 *
 * @class The PathItem class is the base for any items that describe paths
 * and offer standardised methods for drawing and path manipulation, such as
 * @link Path and @link CompoundPath.
 *
 * @extends Item
 */
class PathItem
	/**
	 * Returns all intersections between two @link PathItem items as an array
	 * of @link CurveLocation objects. @link CompoundPath items are also
	 * supported.
	 *
	 * @param PathItem path the other item to find the intersections to.
	 * @return CurveLocation[] the locations of all intersection between the
	 * paths
	 */
	map<int, CurveLocation*> getIntersections(path: Path*)
		// First check the bounds of the two paths. If they don't intersect,
		// we don't need to iterate through their curves.
		if !@getBounds()->touches(path->getBounds())
			return {}

		locations = map<int, CurveLocation*>

		curves1 := @getCurves()
		curves2 := path->getCurves()
		length2 := curves2.length,
		values2 := {}

		for var i = 0; i < length2; i++
			values2[i] = curves2[i].getValues()

		for var i = 0, l = curves1.length; i < l; i++ 
			curve1  := curves1[i]
			values1 := curve1.getValues()

			for var j = 0; j < length2; j++
				Curve::getIntersections(values1, values2[j], curve1, curves2[j], locations)
		
		return locations

	setPathData(data) 
		// This is a very compact SVG Path Data parser that works both for Path
		// and CompoundPath.

		var parts = data.match(/[a-z][^a-z]*/ig),
			coords,
			relative = false,
			control,
			current = new Point() // the current position

		function getCoord(index, coord, update) 
			var val = parseFloat(coords[index])
			if (relative)
				val += current[coord]
			if (update)
				current[coord] = val
			return val
		

		function getPoint(index, update) 
			return new Point(
				getCoord(index, 'x', update),
				getCoord(index + 1, 'y', update)
			)
		

		// First clear the previous content
		if (@_type === 'path')
			@removeSegments()
		else
			@removeChildren()

		for (var i = 0, l = parts.length i < l i++) 
			var part = parts[i],
				cmd = part[0],
				lower = cmd.toLowerCase()
			// Split at white-space, commas but also before signs.
			// Use positive lookahead to include signs.
			coords = part.slice(1).trim().split(/[\s,]+|(?=[+-])/)
			relative = cmd === lower
			var length = coords.length
			switch (lower) 
			case 'm':
			case 'l':
				for (var j = 0 j < length j += 2)
					this[j === 0 && lower === 'm' ? 'moveTo' : 'lineTo'](
							getPoint(j, true))
				break
			case 'h':
			case 'v':
				var coord = lower == 'h' ? 'x' : 'y'
				for (var j = 0 j < length j++) 
					getCoord(j, coord, true)
					@lineTo(current)
				
				break
			case 'c':
				for (var j = 0 j < length j += 6) 
					@cubicCurveTo(
							getPoint(j),
							control = getPoint(j + 2),
							getPoint(j + 4, true))
				
				break
			case 's':
				// Shorthand cubic bezierCurveTo, absolute
				for (var j = 0 j < length j += 4) 
					@cubicCurveTo(
							// Calculate reflection of previous control points
							current.multiply(2).subtract(control),
							control = getPoint(j),
							getPoint(j + 2, true))
				
				break
			case 'q':
				for (var j = 0 j < length j += 4) 
					@quadraticCurveTo(
							control = getPoint(j),
							getPoint(j + 2, true))
				
				break
			case 't':
				for (var j = 0 j < length j += 2) 
					@quadraticCurveTo(
							// Calculate reflection of previous control points
							control = current.multiply(2).subtract(control),
							getPoint(j, true))
				
				break
			case 'a':
				// TODO: Implement Arcs!
				break
			case 'z':
				@closePath()
				break

	bool _canComposite() 
		// A path with only a fill  or a stroke can be directly blended, but if
		// it has both, it needs to be drawn into a separate canvas first.
		return !(@hasFill() and @hasStroke())
	
	/**
	 * Smooth bezier curves without changing the amount of segments or their
	 * points, by only smoothing and adjusting their handle points, for both
	 * open ended and closed paths.
	 *
	 * @name PathItem#smooth
	 * @function
	 *
	 */

	/**
	 * @grouptitle Postscript Style Drawing Commands
	 *
	 * On a normal empty @link Path, the point is simply added as the path's
	 * first segment. If called on a @link CompoundPath, a new @link Path is
	 * created as a child and the point is added as its first segment.
	 *
	 * @name PathItem#moveTo
	 * @function
	 * @param Point point
	 */

	// DOCS: Document #lineTo()
	/**
	 * @name PathItem#lineTo
	 * @function
	 * @param Point point
	 */

	/**
	 * Adds a cubic bezier curve to the path, defined by two handles and a to
	 * point.
	 *
	 * @name PathItem#cubicCurveTo
	 * @function
	 * @param Point handle1
	 * @param Point handle2
	 * @param Point to
	 */

	/**
	 * Adds a quadratic bezier curve to the path, defined by a handle and a to
	 * point.
	 *
	 * @name PathItem#quadraticCurveTo
	 * @function
	 * @param Point handle
	 * @param Point to
	 */

	// DOCS: Document PathItem#curveTo() 'paramater' param.
	/**
	 * Draws a curve from the position of the last segment point in the path
	 * that goes through the specified @code through point, to the specified
	 * @code to point by adding one segment to the path.
	 *
	 * @name PathItem#curveTo
	 * @function
	 * @param Point through the point through which the curve should go
	 * @param Point to the point where the curve should end
	 * @param Number [parameter=0.5]
	 */

	/**
	 * Draws an arc from the position of the last segment point in the path that
	 * goes through the specified @code through point, to the specified
	 * @code to point by adding one or more segments to the path.
	 *
	 * @name PathItem#arcTo
	 * @function
	 * @param Point through the point where the arc should pass through
	 * @param Point to the point where the arc should end
	 */

	/**
	 * Draws an arc from the position of the last segment point in the path to
	 * the specified point by adding one or more segments to the path.
	 *
	 * @name PathItem#arcTo
	 * @function
	 * @param Point to the point where the arc should end
	 * @param Boolean [clockwise=true] specifies whether the arc should be
	 *        drawn in clockwise direction.
	 */

	/**
	 * Closes the path. When closed, Paper.js connects the first and last
	 * segments.
	 *
	 * @name PathItem#closePath
	 * @function
	 * @see Path#closed
	 */

	/**
	 * @grouptitle Relative Drawing Commands
	 *
	 * If called on a @link CompoundPath, a new @link Path is created as a
	 * child and a point is added as its first segment relative to the
	 * position of the last segment of the current path.
	 *
	 * @name PathItem#moveBy
	 * @function
	 * @param Point vector
	 */

	/**
	 * Adds a segment relative to the last segment point of the path.
	 *
	 * @name PathItem#lineBy
	 * @function
	 * @param Point vector The vector which is added to the position of the
	 *        last segment of the path, to become the new segment.
	 */

	// DOCS: Document Path#curveBy()
	/**
	 * @name PathItem#curveBy
	 * @function
	 * @param Point throughVector
	 * @param Point toVector
	 * @param Number [parameter=0.5]
	 */

	// DOCS: Document Path#arcBy()
	/**
	 * @name PathItem#arcBy
	 * @function
	 * @param Point throughVector
	 * @param Point toVector
	 */
