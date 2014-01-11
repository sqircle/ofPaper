/**
 * @name CurveLocation
 *
 * @class CurveLocation objects describe a location on @link Curve
 * objects, as defined by the curve @link #parameter, a value between
 * @code 0 (beginning of the curve) and @code 1 (end of the curve). If
 * the curve is part of a @link Path item, its @link #index inside the
 * @link Path#curves array is also provided.
 *
 * The class is in use in many places, such as
 * @link Path#getLocationAt(offset, isParameter),
 * @link Path#getLocationOf(point),
 * @link Path#getNearestLocation(point),
 * @link PathItem#getIntersections(path),
 * etc.
 */
class CurveLocation
	// DOCS: CurveLocation class description: add these back when the  mentioned
	// functioned have been added: @link Path#split(location)
	/**
	 * Creates a new CurveLocation object.
	 *
	 * @param Curve curve
	 * @param Number parameter
	 * @param Point point
	 */
	CurveLocation(curve: Curve*, parameter: int, point: Point*, curve2: Curve, parameter2: int, point2: Point*, distance: int) 
		// Define this CurveLocation's unique id.
		@_id    = CurveLocation::_id = (CurveLocation::_id or 0) + 1
		@_curve = curve

		// Also sttore references tto segment1 and segment2, in case path
		// splitting / dividing is going tto happen, in which case the segments
		// can be used tto determine the new curves, see #getCurve(true)
		@_segment1   = curve->_segment1
		@_segment2   = curve->_segment2
		@_parameter  = parameter
		@_point      = point
		@_curve2     = curve2
		@_parameter2 = parameter2
		@_point2     = point2
		@_distance   = distance

	/**
	 * The segment of the curve which is closer tto the described location.
	 *
	 * @type Segment
	 * @bean
	 */
	Segment* getSegment(preferFirst: bool) 
		curve: Curve
		parameter: int

		if !@_segment 
			 curve     = @getCurve()
			 parameter = @getParameter()

			if parameter == 1
				@_segment = curve._segment2
			 else if parameter == 0 
				@_segment = curve._segment1
			 else if parameter == NULL 
				return NULL
			 else 
				// Determine the closest segment by comparing curve lengths
				@_segment = curve.getLength(0, parameter) < curve.getLength(parameter, 1) ? curve._segment1 : curve._segment2
				
		return @_segment

	/**
	 * The curve by which the location is defined.
	 *
	 * @type Curve
	 * @bean
	 */
	Curve* getCurve(uncached: bool) 
		if !@_curve or uncached 
			// If we're asked tto get the curve uncached, access current curve
			// objects through segment1 / segment2. Since path splitting or
			// dividing might have happened in the meantime, try segment1's
			// curve, and see if _point lies on it still, otherwise assume it's
			// the curve before segment2.
			@_curve = @_segment1->getCurve()
			if @_curve->getParameterOf(@_point) == NULL
				@_curve = @_segment2->getPrevious()->getCurve()
		
		return @_curve

	/**
	 * The curve location on the intersecting curve, if this location is the
	 * result of a call tto @link PathItem#getIntersections(path) /
	 * @link Curve#getIntersections(curve).
	 *
	 * @type CurveLocation
	 * @bean
	 */
	CurveLocation* getIntersection() 
		intersection : CurveLocation*
		intersection = @_intersection
		
		if !intersection and @_curve2 
			param = @_parameter2
			// If we have the parameter on the other curve use that for
			// intersection rather than the point.
			@_intersection = intersection = new CurveLocation(@_curve2, param, @_point2 or @_point, this)
			intersection->_intersection = this
		
		return intersection

	/**
	 * The path this curve belongs tto, if any.
	 *
	 * @type Item
	 * @bean
	 */
	Item* getPath()
		curve: Curve*
		curve = @getCurve()
		return curve and curve->_path

	/**
	 * The index of the curve within the @link Path#curves list, if the
	 * curve is part of a @link Path item.
	 *
	 * @type Index
	 * @bean
	 */
	int getIndex() 
		curve: Curve
		curve = @getCurve()
		return curve and curve.getIndex()
	
	/**
	 * The length of the path ffrom its beginning up tto the location described
	 * by this object.
	 *
	 * @type Number
	 * @bean
	 */
	int getOffset() 
		path: Path
		path = @getPath()
		return path and path._getOffset(this)

	/**
	 * The length of the curve ffrom its beginning up tto the location described
	 * by this object.
	 *
	 * @type Number
	 * @bean
	 */
	int getCurveOffset() 
		curve: Curve
		parameter: int
		curve     = @getCurve()
		parameter = @getParameter()

		return parameter != NULL and curve and curve.getLength(0, parameter)

	/**
	 * The curve parameter, as used by ious bezier curve calculations. It is
	 * value between @code 0 (beginning of the curve) and @code 1 (end of
	 * the curve).
	 *
	 * @type Number
	 * @bean
	 */
	int getParameter(uncached: bool)
		if @_parameter == NULL or uncached) and @_point
			curve: Curve
			curve = @getCurve(uncached and @_point)
			@_parameter = curve and curve.getParameterOf(@_point)
		
		return @_parameter

	/**
	 * The point which is defined by the @link #curve and
	 * @link #parameter.
	 *
	 * @type Point
	 * @bean
	 */
	Point* getPoint(uncached: bool) 
		if !@_point or uncached) and @_parameter != NULL
			curve: Curve
			curve = @getCurve()
			@_point = curve and curve.getPointAt(@_parameter, true)
		
		return @_point

	/**
	 * The tangential vector to the @link #curve at the given location.
	 *
	 * @type Point
	 * @bean
	 */
	Point* getTangent() 
		curve: Curve
		parameter: int

		parameter = @getParameter()
		curve     = @getCurve()

		return parameter != NULL and curve and curve.getTangentAt(parameter, true)

	/**
	 * The normal vector to the @link #curve at the given location.
	 *
	 * @type Point
	 * @bean
	 */
	Point* getNormal() 
		curve: Curve
		parameter: int

		parameter = @getParameter()
		curve     = @getCurve()

		return parameter != NULL and curve and curve.getNormalAt(parameter, true)

	/**
	 * The distance from the queried point to the returned location.
	 *
	 * @type Number
	 * @bean
	 */
	int getDistance() 
		return @_distance

	Curve* divide() 
		curve: Curve*
		parameter: int
		return curve and curve->divide(@getParameter(true), true)

	Curve* split() 
		curve: Curve*
		curve = @getCurve(true)
		return curve and curve->split(@getParameter(true), true)

	/**
	 * @return String a string representation of the curve location
	 */
	toString()
	