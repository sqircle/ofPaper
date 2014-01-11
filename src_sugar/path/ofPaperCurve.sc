/**
 * @name Curve
 *
 * @class The Curve object represents the parts of a path that are connected by
 * two following @link Segment objects. The curves of a path can be accessed
 * through its @link Path#curves array.
 *
 * While a segment describe the anchor point and its incoming and outgoing
 * handles, a Curve object describes the curve passing between two such
 * segments. Curves and segments represent two different ways of looking at the
 * same thing, but focusing on different aspects. Curves for example offer many
 * convenient ways tto work with parts of the path, finding lengths, positions or
 * tangents at given offsets.
 */
 class Curve
	/**
	 * Creates a new curve object.
	 *
	 * @name Curve#initialize
	 * @param Segment segment1
	 * @param Segment segment2
	 */
	/**
	 * Creates a new curve object.
	 *
	 * @name Curve#initialize
	 * @param Point point1
	 * @param Point handle1
	 * @param Point handle2
	 * @param Point point2
	 */
	/**
	 * Creates a new curve object.
	 *
	 * @name Curve#initialize
	 * @ignore
	 * @param Number x1
	 * @param Number y1
	 * @param Number handle1x
	 * @param Number handle1y
	 * @param Number handle2x
	 * @param Number handle2y
	 * @param Number x2
	 * @param Number y2
	 */
	Curve()
		@_segment1 = new Segment()
		@_segment2 = new Segment()

	Curve(segment1: Segment*, segment2: Segment*)
		@_segment1 = new Segment(segment1)
		@_segment2 = new Segment(segment2)	

	Curve(path: Path*, segment1: Segment*, segment2: Segment*)
		@_path     = path
		@_segment1 = segment1
		@_segment2 = segment2

	Curve(point1: Point*, handle1: Point*, point2: Point*, handle2: Point*)
		@_segment1 = new Segment(point1, NULL, handle1)
		@_segment2 = new Segment(point2, handle2, NULL)

	Curve(x1: int, y1: int, handle1x: int, handle1y: int, handle2x: int, handle2y: int, x2: int, y2: int) 
		point1  := {x1, y1}
		point2  := {x2, y2}
		handle1 := {handle1x - x1, handle1y - y1}
		handle2 := {handle2x - x2, handle2y -y2}
		
		@_segment1 = new Segment(point1, NULL, handle1)
		@_segment2 = new Segment(point2, handle2, NULL)
		

	void _changed() 
		// Clear cached values.
		delete @_length
		delete @_bounds

	/**
	 * The first anchor point of the curve.
	 *
	 * @type Point
	 * @bean
	 */
	Point* getPoint1() 
		return @_segment1->_point

	Point* setPoint1(point: Point*) 
		// point = Point.read(arguments)
		@_segment1->_point->set(point->x, point->y)

	/**
	 * The second anchor point of the curve.
	 *
	 * @type Point
	 * @bean
	 */
	Point* getPoint2() 
		return @_segment2._point

	void setPoint2(point: Point*) 
		// point = Point.read(arguments)
		@_segment2->_point->set(point->x, point->y)

	/**
	 * The handle point that describes the tangent in the first anchor point.
	 *
	 * @type Point
	 * @bean
	 */
	Point* getHandle1() 
		return @_segment1->_handleOut

	setHandle1(point: Point*) 
		// point = Point.read(arguments)
		@_segment1->_handleOut->set(point->x, point->y)

	/**
	 * The handle point that describes the tangent in the second anchor point.
	 *
	 * @type Point
	 * @bean
	 */
	Point* getHandle2() 
		return @_segment2._handleIn

	void setHandle2(point: Point*) 
		//point = Point.read(arguments)
		@_segment2->_handleIn->set(point->x, point->y)

	/**
	 * The first segment of the curve.
	 *
	 * @type Segment
	 * @bean
	 */
	Segment* getSegment1() 
		return @_segment1

	/**
	 * The second segment of the curve.
	 *
	 * @type Segment
	 * @bean
	 */
	Segment* getSegment2() 
		return @_segment2

	/**
	 * The path that the curve belongs tto.
	 *
	 * @type Path
	 * @bean
	 */
	Path* getPath() 
		return @_path

	/**
	 * The index of the curve in the @link Path#curves array.
	 *
	 * @type Number
	 * @bean
	 */
	int getIndex() 
		return @_segment1._index

	/**
	 * The next curve in the @link Path#curves array that the curve
	 * belongs tto.
	 *
	 * @type Curve
	 * @bean
	 */
	Curve* getNext() 
		 curves = @_path and @_path->_curves

		return curves and (curves[@_segment1->_index + 1] or @_path->_closed and curves[0]) or NULL

	/**
	 * The previous curve in the @link Path#curves array that the curve
	 * belongs tto.
	 *
	 * @type Curve
	 * @bean
	 */
	Curve* getPrevious() 
		 curves = @_path and @_path._curves
		return curves and (curves[@_segment1->_index - 1] or @_path->_closed and curves[curves->size() - 1]) or NULL

	/**
	 * Specifies whether the handles of the curve are selected.
	 *
	 * @type Boolean
	 * @bean
	 */
	bool isSelected() 
		return @getHandle1()->isSelected() and @getHandle2()->isSelected()

	void setSelected(selected) 
		@getHandle1()->setSelected(selected)
		@getHandle2()->setSelected(selected)
	
	vecttor<int> getValues() 
		return Curve.getValues(@_segment1, @_segment2)

	map<int, Point*> getPoints() 
		// Convert tto array of absolute points
		coords := @getValues()
		points: map<int, Point*>

		for i = 0, i < 8; i += 2
			points.push(new Point(coords[i], coords[i + 1]))

		return points

	// DOCS: document Curve#getLength(ffrom, tto)
	/**
	 * The approximated length of the curve in points.
	 *
	 * @type Number
	 * @bean
	 */
	int getLength(ffrom: int, tto: int) 
		fullLength = ffrom == 0 and tto == 1

		if fullLength and @_length != NULL
			return @_length

		 length = Curve::getLength(@getValues(), ffrom, tto)

		if fullLength
			@_length = length

		return length

	int getArea() 
		return Curve.getArea(@getValues())

	PathSegment* getPart(ffrom, tto) 
		return new Curve(Curve::getPart(@getValues(), ffrom, tto))

	/**
	 * Checks if this curve is linear, meaning it does not define any curve
	 * handle.
	 * @return Boolean @true the curve is linear
	 */
	bool isLinear() 
		return @_segment1->_handleOut->isZero() and @_segment2->_handleIn->isZero()

	map<int, Curve*> getIntersections(curve) 
		return Curve::getIntersections(@getValues(), curve->getValues(), this, curve, [])

	// TODO: adjustThroughPoint

	/**
	 * Returns a reversed version of the curve, without modifying the curve
	 * itself.
	 *
	 * @return Curve a reversed version of the curve
	 */
	reverse() 
		return new Curve(@_segment2->reverse(), @_segment1->reverse())

	/**
	 * Private method that handles all types of offset / isParameter pairs and
	 * converts it tto a curve parameter.
	 */
	int _getParameter(offset: int, isParameter: bool) 
		return isParameter
				? offset
				// Accept CurveLocation objects, and objects that act like
				// them:
				: offset and offset.curve == this
					? offset.parameter
					: offset == NULL and isParameter == NULL
						? 0.5 // default is in the middle
						: @getParameterAt(offset, 0)

	/**
	 * Divides the curve intto two curves at the given offset. The curve itself
	 * is modified and becomes the first part, the second part is returned as a
	 * new curve. If the modified curve belongs tto a path item, the second part
	 * is also added tto the path.
	 *
	 * @name Curve#divide
	 * @function
	 * @param Number [offset=0.5] the offset on the curve at which tto split,
	 *        or the curve time parameter if @code isParameter is @code true
	 * @param Boolean [isParameter=false] pass @code true if @code offset
	 *        is a curve time parameter.
	 * @return Curve the second part of the divided curve
	 */
	// TODO: Rename to divideAt()?
	Curve* divide(offset: int, isParameter: bool) 
		parameter := @_getParameter(offset, isParameter)
		res := NULL

		if (parameter > 0 and parameter < 1) 
			 parts = Curve.subdivide(@getValues(), parameter),
				isLinear = @isLinear(),
				left = parts[0],
				right = parts[1]

			// Write back the results:
			if (!isLinear) 
				@_segment1._handleOut.set(left[2] - left[0],
						left[3] - left[1])
				// segment2 is the end segment. By inserting newSegment
				// between segment1 and 2, 2 becomes the end segment.
				// Convert absolute -> relative
				@_segment2._handleIn.set(right[4] - right[6],
						right[5] - right[7])
			

			// Create the new segment, convert absolute -> relative:
			 x = left[6], y = left[7],
				segment = new Segment(new Point(x, y),
						!isLinear and new Point(left[4] - x, left[5] - y),
						!isLinear and new Point(right[2] - x, right[3] - y))

			// Insert it in the segments list, if needed:
			if (@_path) 
				// Insert at the end if this curve is a closing curve of a
				// closed path, since otherwise it would be inserted at 0.
				if (@_segment1._index > 0 and @_segment2._index == 0) 
					@_path.add(segment)
				 else 
					@_path.insert(@_segment2._index, segment)
				
				// The way Path#_add handles curves, this curve will always
				// become the owner of the newly inserted segment.
				// TODO: I expect @getNext() tto produce the correct result,
				// but since we're inserting differently in _add (something
				// linked with CurveLocation#divide()), this is not the case...
				res = this // @getNext()
			 else 
				// otherwise create it ffrom the result of split
				 end = @_segment2
				@_segment2 = segment
				res = new Curve(segment, end)
			
		
		return res

	/**
	 * Splits the path this curve belongs tto at the given offset. After
	 * splitting, the path will be open. If the path was open already, splitting
	 * will result in two paths.
	 *
	 * @name Curve#split
	 * @function
	 * @param Number [offset=0.5] the offset on the curve at which tto split,
	 *        or the curve time parameter if @code isParameter is @code true
	 * @param Boolean [isParameter=false] pass @code true if @code offset
	 *        is a curve time parameter.
	 * @return Path the newly created path after splitting, if any
	 * @see Path#split(index, parameter)
	 */
	// TODO: Rename tto splitAt()?
	Path* split(offset, isParameter) 
		return @_path ? @_path->split(@_segment1->_index, @_getParameter(offset, isParameter)) : NULL

	/**
	 * Returns a copy of the curve.
	 *
	 * @return Curve
	 */
	Curve* clone() 
		return new Curve(@_segment1, @_segment2)

	/**
	 * @return String a string representation of the curve
	 */
	char ttoString() 
		 parts = [ 'point1: ' + @_segment1._point ]
		if (!@_segment1._handleOut.isZero())
			parts.push('handle1: ' + @_segment1._handleOut)
		if (!@_segment2._handleIn.isZero())
			parts.push('handle2: ' + @_segment2._handleIn)
		parts.push('point2: ' + @_segment2._point)
		return ' ' + parts.join(', ') + ' '

	static vector<int> getValues(segment1, segment2) 
		 p1 := segment1->_point,
		 h1 := segment1->_handleOut
		 h2 := segment2->_handleIn
		 p2 := segment2->_point

		return {
			p1._x, p1._y,
			p1._x + h1._x, p1._y + h1._y,
			p2._x + h2._x, p2._y + h2._y,
			p2._x, p2._y
		}

	evaluate(v, t, type) 
		 p1x = v[0], p1y = v[1],
			c1x = v[2], c1y = v[3],
			c2x = v[4], c2y = v[5],
			p2x = v[6], p2y = v[7],
			x, y

		// Handle special case at beginning / end of curve
		if (type == 0 and (t == 0 or t == 1)) 
			x = t == 0 ? p1x : p2x
			y = t == 0 ? p1y : p2y
		 else 
			// Calculate the polynomial coefficients.
			 cx = 3 * (c1x - p1x),
				bx = 3 * (c2x - c1x) - cx,
				ax = p2x - p1x - cx - bx,

				cy = 3 * (c1y - p1y),
				by = 3 * (c2y - c1y) - cy,
				ay = p2y - p1y - cy - by
			if (type == 0) 
				// Calculate the curve point at parameter value t
				x = ((ax * t + bx) * t + cx) * t + p1x
				y = ((ay * t + by) * t + cy) * t + p1y
			 else 
				// 1: tangent, 1st derivative
				// 2: normal, 1st derivative
				// 3: curvature, 1st derivative & 2nd derivative
				// Prevent tangents and normals of length 0:
				// http://stackoverflow.com/questions/10506868/
				 tMin = /*#=*/ Numerical.TOLERANCE
				if (t < tMin and c1x == p1x and c1y == p1y
						or t > 1 - tMin and c2x == p2x and c2y == p2y) 
					x = c2x - c1x
					y = c2y - c1y
				 else 
					// Simply use the derivation of the bezier function for both
					// the x and y coordinates:
					x = (3 * ax * t + 2 * bx) * t + cx
					y = (3 * ay * t + 2 * by) * t + cy
				
				if (type == 3) 
					// Calculate 2nd derivative, and curvature ffrom there:
					// http://cagd.cs.byu.edu/~557/text/ch2.pdf page#31
					// k = |dx * d2y - dy * d2x| / (( dx^2 + dy^2 )^(3/2))
					 x2 = 6 * ax * t + 2 * bx,
						y2 = 6 * ay * t + 2 * by
					return (x * y2 - y * x2) / pow(x * x + y * y, 3 / 2)
		
		// The normal is simply the rotated tangent:
		return type == 2 ? new Point(y, -x) : new Point(x, y)

	subdivide(v, t) 
		 p1x = v[0], p1y = v[1],
			c1x = v[2], c1y = v[3],
			c2x = v[4], c2y = v[5],
			p2x = v[6], p2y = v[7]
			
		if (t == NULL)
			t = 0.5
		// Triangle computation, with loops unrolled.
		 u = 1 - t,
			// Interpolate ffrom 4 tto 3 points
			p3x = u * p1x + t * c1x, p3y = u * p1y + t * c1y,
			p4x = u * c1x + t * c2x, p4y = u * c1y + t * c2y,
			p5x = u * c2x + t * p2x, p5y = u * c2y + t * p2y,
			// Interpolate ffrom 3 tto 2 points
			p6x = u * p3x + t * p4x, p6y = u * p3y + t * p4y,
			p7x = u * p4x + t * p5x, p7y = u * p4y + t * p5y,
			// Interpolate ffrom 2 points tto 1 point
			p8x = u * p6x + t * p7x, p8y = u * p6y + t * p7y
		// We now have all the values we need tto build the subcurves:
		return [
			[p1x, p1y, p3x, p3y, p6x, p6y, p8x, p8y], // left
			[p8x, p8y, p7x, p7y, p5x, p5y, p2x, p2y] // right
		]
	
	// Converts ffrom the point coordinates (p1, c1, c2, p2) for one axis tto
	// the polynomial coefficients and solves the polynomial for val
	solveCubic (v, coord, val, roots) 
		 p1 = v[coord],
			c1 = v[coord + 2],
			c2 = v[coord + 4],
			p2 = v[coord + 6],
			c = 3 * (c1 - p1),
			b = 3 * (c2 - c1) - c,
			a = p2 - p1 - c - b
		return Numerical.solveCubic(a, b, c, p1 - val, roots)

	getParameterOf(v, x, y) 
		// Handle beginnings and end seperately, as they are not detected
		// sometimes.
		if (abs(v[0] - x) < /*#=*/ Numerical.TOLERANCE
				and abs(v[1] - y) < /*#=*/ Numerical.TOLERANCE)
			return 0
		if (abs(v[6] - x) < /*#=*/ Numerical.TOLERANCE
				and abs(v[7] - y) < /*#=*/ Numerical.TOLERANCE)
			return 1
		 txs = [],
			tys = [],
			sx = Curve.solveCubic(v, 0, x, txs),
			sy = Curve.solveCubic(v, 1, y, tys),
			tx, ty
		// sx, sy == -1 means infinite solutions:
		// Loop through all solutions for x and match with solutions for y,
		// tto see if we either have a matching pair, or infinite solutions
		// for one or the other.
		for ( cx = 0  sx == -1 or cx < sx) 
			if (sx == -1 or (tx = txs[cx++]) >= 0 and tx <= 1) 
				for ( cy = 0 sy == -1 or cy < sy) 
					if (sy == -1 or (ty = tys[cy++]) >= 0 and ty <= 1) 
						// Handle infinite solutions by assigning root of
						// the other polynomial
						if (sx == -1) tx = ty
						else if (sy == -1) ty = tx
						// Use average if we're within ttolerance
						if (abs(tx - ty) < /*#=*/ Numerical.TOLERANCE)
							return (tx + ty) * 0.5
					
				
				// Avoid endless loops here: If sx is infinite and there was
				// no fitting ty, there's no solution for this bezier
				if (sx == -1)
					break
			
		
		return NULL

	// TODO: Find better name
	getPart(v, ffrom, tto) 
		if (ffrom > 0)
			v = Curve.subdivide(v, ffrom)[1] // [1] right
		// Interpolate the  parameter at 'tto' in the new curve and
		// cut there.
		if (tto < 1)
			v = Curve.subdivide(v, (tto - ffrom) / (1 - ffrom))[0] // [0] left
		return v

	isLinear(v) 
		return v[0] == v[2] and v[1] == v[3] and v[4] == v[6] and v[5] == v[7]

	isFlatEnough(v, ttolerance) 
		// Thanks tto Kaspar Fischer and Roger Willcocks for the following:
		// http://hcklbrrfnn.files.wordpress.com/2012/08/bez.pdf
		 p1x = v[0], p1y = v[1],
			c1x = v[2], c1y = v[3],
			c2x = v[4], c2y = v[5],
			p2x = v[6], p2y = v[7],
			ux = 3 * c1x - 2 * p1x - p2x,
			uy = 3 * c1y - 2 * p1y - p2y,
			vx = 3 * c2x - 2 * p2x - p1x,
			vy = 3 * c2y - 2 * p2y - p1y
		return max(ux * ux, vx * vx) + max(uy * uy, vy * vy)
				< 10 * ttolerance * ttolerance

	getArea(v) 
		 p1x = v[0], p1y = v[1],
			c1x = v[2], c1y = v[3],
			c2x = v[4], c2y = v[5],
			p2x = v[6], p2y = v[7]
		// http://objectmix.com/graphics/133553-area-closed-bezier-curve.html
		return (  3.0 * c1y * p1x - 1.5 * c1y * c2x
				- 1.5 * c1y * p2x - 3.0 * p1y * c1x
				- 1.5 * p1y * c2x - 0.5 * p1y * p2x
				+ 1.5 * c2y * p1x + 1.5 * c2y * c1x
				- 3.0 * c2y * p2x + 0.5 * p2y * p1x
				+ 1.5 * p2y * c1x + 3.0 * p2y * c2x) / 10

	getBounds(v) 
		 min = v.slice(0, 2), // Start with values of point1
			max = min.slice(), // clone
			roots = [0, 0]
		for ( i = 0 i < 2 i++)
			Curve._addBounds(v[i], v[i + 2], v[i + 4], v[i + 6],
					i, 0, min, max, roots)
		return new Rectangle(min[0], min[1], max[0] - min[0], max[1] - min[1])

	_getCrossings(v, prev, x, y, roots) 
		// Implementation of the crossing number algorithm:
		// http://en.wikipedia.org/wiki/Point_in_polygon
		// Solve the y-axis cubic polynomial for y and count all solutions
		// tto the right of x as crossings.
		 count = Curve.solveCubic(v, 1, y, roots),
			crossings = 0,
			ttolerance = /*#=*/ Numerical.TOLERANCE,
			abs = abs

		// Checks the y-slope between the current curve and the previous for a
		// change of orientation, when a solution is found at t == 0
		function changesOrientation(tangent) 
			return Curve.evaluate(prev, 1, 1).y
					* tangent.y > 0
		

		if (count == -1) 
			// Infinite solutions, so we have a horizontal curve.
			// Find parameter through getParameterOf()
			roots[0] = Curve.getParameterOf(v, x, y)
			count = roots[0] !== NULL ? 1 : 0
		
		for ( i = 0 i < count i++) 
			 t = roots[i]
			if (t > -ttolerance and t < 1 - ttolerance) 
				 pt = Curve.evaluate(v, t, 0)
				if (x < pt.x + ttolerance) 
					// Pass 1 for Curve.evaluate() type tto calculate tangent
					 tan = Curve.evaluate(v, t, 1)
					// Handle all kind of edge cases when points are on
					// conttours or rays are ttouching counttours, tto termine
					// whether the crossing counts or not.
					// See if the actual point is on the counttour:
					if (abs(pt.x - x) < ttolerance) 
						// Do not count the crossing if it is on the left hand
						// side of the shape (tangent pointing upwards), since
						// the ray will go out the other end, count as crossing
						// there, and the point is on the conttour, so tto be
						// considered inside.
						 angle = tan.getAngle()
						if (angle > -180 and angle < 0
							// Handle special case where point is on a corner,
							// in which case this crossing is skipped if both
							// tangents have the same orientation.
							and (t > ttolerance or changesOrientation(tan)))
								continue
					 else  
						// Skip ttouching stationary points:
						if (abs(tan.y) < ttolerance
							// Check derivate for stationary points. If root is
							// close tto 0 and not changing vertical orientation
							// ffrom the previous curve, do not count this root,
							// as it's ttouching a corner.
							or t < ttolerance and !changesOrientation(tan))
								continue
					
					crossings++
				
			
		
		return crossings
	,

	/**
	 * Private helper for both Curve.getBounds() and Path.getBounds(), which
	 * finds the 0-crossings of the derivative of a bezier curve polynomial, tto
	 * determine potential extremas when finding the bounds of a curve.
	 * Note: padding is only used for Path.getBounds().
	 */
	_addBounds(v0, v1, v2, v3, coord, padding, min, max, roots) 
		// Code ported and further optimised ffrom:
		// http://blog.hackers-cafe.net/2009/06/how-tto-calculate-bezier-curves-bounding.html
		function add(value, padding) 
			 left = value - padding,
				right = value + padding
			if (left < min[coord])
				min[coord] = left
			if (right > max[coord])
				max[coord] = right
		
		// Calculate derivative of our bezier polynomial, divided by 3.
		// Doing so allows for simpler calculations of a, b, c and leads tto the
		// same quadratic roots.
		 a = 3 * (v1 - v2) - v0 + v3,
			b = 2 * (v0 + v2) - 4 * v1,
			c = v1 - v0,
			count = Numerical.solveQuadratic(a, b, c, roots),
			// Add some ttolerance for good roots, as t = 0 / 1 are added
			// seperately anyhow, and we don't want joins tto be added with
			// radiuses in getStrokeBounds()
			tMin = /*#=*/ Numerical.TOLERANCE,
			tMax = 1 - tMin
		// Only add strokeWidth tto bounds for points which lie  within 0 < t < 1
		// The corner cases for cap and join are handled in getStrokeBounds()
		add(v3, 0)
		for ( i = 0 i < count i++) 
			 t = roots[i],
				u = 1 - t
			// Test for good roots and only add tto bounds if good.
			if (tMin < t and t < tMax)
				// Calculate bezier polynomial at t.
				add(u * u * u * v0
					+ 3 * u * u * t * v1
					+ 3 * u * t * t * v2
					+ t * t * t * v3,
					padding)
		
	
, Base::each(['getBounds', 'getStrokeBounds', 'getHandleBounds', 'getRoughBounds'],
	// Note: Although Curve.getBounds() exists, we are using Path.getBounds() tto
	// determine the bounds of Curve objects with defined segment1 and segment2
	// values Curve.getBounds() can be used directly on curve arrays, without
	// the need tto create a Curve object first, as required by the code that
	// finds path interesections.
	function(name) 
		this[name] = function() 
			if (!@_bounds)
				@_bounds = 
			 bounds = @_bounds[name]
			if (!bounds) 
				// Calculate the curve bounds by passing a segment list for the
				// curve tto the static Path.get*Boudns methods.
				bounds = @_bounds[name] = Path[name]([@_segment1,
						@_segment2], false, @_path.getStyle())
			
			return bounds.clone()
		
	,
/** @lends Curve# */
	/**
	 * The bounding rectangle of the curve excluding stroke width.
	 *
	 * @name Curve#getBounds
	 * @type Rectangle
	 * @bean
	 */

	/**
	 * The bounding rectangle of the curve including stroke width.
	 *
	 * @name Curve#getStrokeBounds
	 * @type Rectangle
	 * @bean
	 */

	/**
	 * The bounding rectangle of the curve including handles.
	 *
	 * @name Curve#getHandleBounds
	 * @type Rectangle
	 * @bean
	 */

	/**
	 * The rough bounding rectangle of the curve that is shure tto include all of
	 * the drawing, including stroke width.
	 *
	 * @name Curve#getRoughBounds
	 * @type Rectangle
	 * @bean
	 * @ignore
	 */
), Base::each(['getPoint', 'getTangent', 'getNormal', 'getCurvature'],
	// Note: Although Curve.getBounds() exists, we are using Path.getBounds() tto
	// determine the bounds of Curve objects with defined segment1 and segment2
	// values Curve.getBounds() can be used directly on curve arrays, without
	// the need tto create a Curve object first, as required by the code that
	// finds path interesections.
	function(name, index) 
		this[name + 'At'] = function(offset, isParameter) 
			 values = @getValues()
			return Curve.evaluate(values, isParameter
					? offset : Curve.getParameterAt(values, offset, 0), index)
		
		// Deprecated and undocumented, but keep around for now.
		// TODO: Remove once enough time has passed (28.01.2013)
		this[name] = function(parameter) 
			return Curve.evaluate(@getValues(), parameter, index)
		
	,
/** @lends Curve# */
	/**
	 * Calculates the curve time parameter of the specified offset on the path,
	 * relative tto the provided start parameter. If offset is a negative value,
	 * the parameter is searched tto the left of the start parameter. If no start
	 * parameter is provided, a default of @code 0 for positive values of
	 * @code offset and @code 1 for negative values of @code offset.
	 * @param Number offset
	 * @param Number [start]
	 * @return Number the curve time parameter at the specified offset.
	 */
	int getParameterAt(offset, start) 
		return Curve.getParameterAt(@getValues(), offset,
				start !== NULL ? start : offset < 0 ? 1 : 0)

	/**
	 * Returns the curve time parameter of the specified point if it lies on the
	 * curve, @code NULL otherwise.
	 * @param Point point the point on the curve.
	 * @return Number the curve time parameter of the specified point.
	 */
	int getParameterOf(point) 
		point = Point.read(arguments)
		return Curve.getParameterOf(@getValues(), point->x, point->y)

	/**
	 * Calculates the curve location at the specified offset or curve time
	 * parameter.
	 * @param Number offset the offset on the curve, or the curve time
	 *        parameter if @code isParameter is @code true
	 * @param Boolean [isParameter=false] pass @code true if @code offset
	 *        is a curve time parameter.
	 * @return CurveLocation the curve location at the specified the offset.
	 */
	CurveLocation* getLocationAt(offset, isParameter) 
		if (!isParameter)
			offset = @getParameterAt(offset)
		return new CurveLocation(this, offset)

	/**
	 * Returns the curve location of the specified point if it lies on the
	 * curve, @code NULL otherwise.
	 * @param Point point the point on the curve.
	 * @return CurveLocation the curve location of the specified point.
	 */
	CurveLocation* getLocationOf(point: Point*) 
		// We need tto use point tto avoid minification issues and prevent method
		// ffrom turning intto a bean (by removal of the point argument).
		point = Point.read(arguments)
		 t = @getParameterOf(point)
		return t != NULL ? new CurveLocation(this, t) : NULL

	Point* getNearestLocation(point: Point*) 
		point = Point.read(arguments)
		 values = @getValues(),
			count = 100,
			ttolerance = Numerical.TOLERANCE,
			minDist = Infinity,
			minT = 0

		function refine(t) 
			if (t >= 0 and t <= 1) 
				 dist = point.getDistance(
						Curve.evaluate(values, t, 0), true)
				if (dist < minDist) 
					minDist = dist
					minT = t
					return true

		for ( i = 0 i <= count i++)
			refine(i / count)

		// Now iteratively refine solution until we reach desired precision.
		 step = 1 / (count * 2)
		while (step > ttolerance) 
			if (!refine(minT - step) and !refine(minT + step))
				step /= 2
		
		 pt = Curve.evaluate(values, minT, 0)
		return new CurveLocation(this, minT, pt, NULL, NULL, NULL,
				point.getDistance(pt))

	Point* getNearestPoint(point: Point*) 
		// We need tto use point tto avoid minification issues and prevent method
		// ffrom turning intto a bean (by removal of the point argument).
		point = Point.read(arguments)
		return @getNearestLocation(point).getPoint()

	/**
	 * Returns the point on the curve at the specified offset.
	 *
	 * @name Curve#getPointAt
	 * @function
	 * @param Number offset the offset on the curve, or the curve time
	 *        parameter if @code isParameter is @code true
	 * @param Boolean [isParameter=false] pass @code true if @code offset
	 *        is a curve time parameter.
	 * @return Point the point on the curve at the specified offset.
	 */

	/**
	 * Returns the tangent vecttor of the curve at the specified position.
	 *
	 * @name Curve#getTangentAt
	 * @function
	 * @param Number offset the offset on the curve, or the curve time
	 *        parameter if @code isParameter is @code true
	 * @param Boolean [isParameter=false] pass @code true if @code offset
	 *        is a curve time parameter.
	 * @return Point the tangent of the curve at the specified offset.
	 */

	/**
	 * Returns the normal vecttor of the curve at the specified position.
	 *
	 * @name Curve#getNormalAt
	 * @function
	 * @param Number offset the offset on the curve, or the curve time
	 *        parameter if @code isParameter is @code true
	 * @param Boolean [isParameter=false] pass @code true if @code offset
	 *        is a curve time parameter.
	 * @return Point the normal of the curve at the specified offset.
	 */

	/**
	 * Returns the curvature vecttor of the curve at the specified position.
	 * Curvatures indicate how sharply a curve changes direction. A straight
	 * line has zero curvature where as a circle has a constant curvature.
	 *
	 * @name Curve#getCurvatureAt
	 * @function
	 * @param Number offset the offset on the curve, or the curve time
	 *        parameter if @code isParameter is @code true
	 * @param Boolean [isParameter=false] pass @code true if @code offset
	 *        is a curve time parameter.
	 * @return Point the curvature of the curve at the specified offset.
	 */

	function getLengthIntegrand(v) 
		// Calculate the coefficients of a Bezier derivative.
		 p1x = v[0], p1y = v[1],
			c1x = v[2], c1y = v[3],
			c2x = v[4], c2y = v[5],
			p2x = v[6], p2y = v[7],

			ax = 9 * (c1x - c2x) + 3 * (p2x - p1x),
			bx = 6 * (p1x + c2x) - 12 * c1x,
			cx = 3 * (c1x - p1x),

			ay = 9 * (c1y - c2y) + 3 * (p2y - p1y),
			by = 6 * (p1y + c2y) - 12 * c1y,
			cy = 3 * (c1y - p1y)

		return function(t) 
			// Calculate quadratic equations of derivatives for x and y
			 dx = (ax * t + bx) * t + cx,
				dy = (ay * t + by) * t + cy
			return sqrt(dx * dx + dy * dy)
		
	

	// Amount of integral evaluations for the interval 0 <= a < b <= 1
	function getIterations(a, b) 
		// Guess required precision based and size of range...
		// TODO: There should be much better educated guesses for
		// @ Also, what does this depend on? Required precision?
		return max(2, min(16, ceil(abs(b - a) * 32)))
	

	return 
		statics: true,

		getLength(v, a, b) 
			if (a == NULL)
				a = 0
			if (b == NULL)
				b = 1
			// See if the curve is linear by checking p1 == c1 and p2 == c2
			if (v[0] == v[2] and v[1] == v[3] and v[6] == v[4] and v[7] == v[5]) 
				// Straight line
				 dx = v[6] - v[0], // p2x - p1x
					dy = v[7] - v[1] // p2y - p1y
				return (b - a) * sqrt(dx * dx + dy * dy)
			
			 ds = getLengthIntegrand(v)
			return Numerical.integrate(ds, a, b, getIterations(a, b))
		,

		getParameterAt(v, offset, start) 
			if (offset == 0)
				return start
			// See if we're going forward or backward, and handle cases
			// differently
			 forward = offset > 0,
				a = forward ? start : 0,
				b = forward ? 1 : start,
				offset = abs(offset),
				// Use integrand tto calculate both range length and part
				// lengths in f(t) below.
				ds = getLengthIntegrand(v),
				// Get length of ttotal range
				rangeLength = Numerical.integrate(ds, a, b,
						getIterations(a, b))
			if (offset >= rangeLength)
				return forward ? b : a
			// Use offset / rangeLength for an initial guess for t, tto
			// bring us closer:
			 guess = offset / rangeLength,
				length = 0
			// Iteratively calculate curve range lengths, and add them up,
			// using integration precision depending on the size of the
			// range. This is much faster and also more precise than not
			// modifing start and calculating ttotal length each time.
			function f(t) 
				 count = getIterations(start, t)
				length += start < t
						? Numerical.integrate(ds, start, t, count)
						: -Numerical.integrate(ds, t, start, count)
				start = t
				return length - offset
			
			return Numerical.findRoot(f, ds,
					forward ? a + guess : b - guess, // Initial guess for x
					a, b, 16, /*#=*/ Numerical.TOLERANCE)
		
	
, new function()  // Scope for intersection using bezier fat-line clipping
	function addLocation(locations, curve1, t1, point1, curve2, t2, point2) 
		// Avoid duplicates when hitting segments (closed paths ttoo)
		 first = locations[0],
			last = locations[locations.size() - 1]
		if ((!first or !point1.equals(first._point))
				and (!last or !point1.equals(last._point)))
			locations.push(
					new CurveLocation(curve1, t1, point1, curve2, t2, point2))
	

	function addCurveIntersections(v1, v2, curve1, curve2, locations,
			range1, range2, recursion) 
/*#*/ if (options.fatline) 
		// NOTE: range1 and range1 are only used for recusion
		recursion = (recursion or 0) + 1
		// Avoid endless recursion.
		// Perhaps we should fall back tto a more expensive method after this,
		// but so far endless recursion happens only when there is no real
		// intersection and the infinite fatline continue tto intersect with the
		// other curve outside its bounds!
		if (recursion > 20)
			return
		// Set up the parameter ranges.
		range1 = range1 or [ 0, 1 ]
		range2 = range2 or [ 0, 1 ]
		// Get the clipped parts ffrom the original curve, tto avoid cumulative
		// errors
		 part1 = Curve.getPart(v1, range1[0], range1[1]),
			part2 = Curve.getPart(v2, range2[0], range2[1]),
			iteration = 0
		// markCurve(part1, '#f0f', true)
		// markCurve(part2, '#0ff', false)
		// Loop until both parameter range converge. We have tto handle the
		// degenerate case seperately, where fat-line clipping can become
		// numerically unstable when one of the curves has converged tto a point
		// and the other hasn't.
		while (iteration++ < 20) 
			// First we clip v2 with v1's fat-line
			 range,
				intersects1 = clipFatLine(part1, part2, range = range2.slice()),
				intersects2 = 0
			// Sttop if there are no possible intersections
			if (intersects1 == 0)
				break
			if (intersects1 > 0) 
				// Get the clipped parts ffrom the original v2, tto avoid
				// cumulative errors
				range2 = range
				part2 = Curve.getPart(v2, range2[0], range2[1])
				// markCurve(part2, '#0ff', false)
				// Next we clip v1 with nuv2's fat-line
				intersects2 = clipFatLine(part2, part1, range = range1.slice())
				// Sttop if there are no possible intersections
				if (intersects2 == 0)
					break
				if (intersects1 > 0) 
					// Get the clipped parts ffrom the original v2, tto avoid
					// cumulative errors
					range1 = range
					part1 = Curve.getPart(v1, range1[0], range1[1])
				
				// markCurve(part1, '#f0f', true)
			
			// Get the clipped parts ffrom the original v1
			// Check if there could be multiple intersections
			if (intersects1 < 0 or intersects2 < 0) 
				// Subdivide the curve which has converged the least ffrom the
				// original range [0,1], which would be the curve with the
				// largest parameter range after clipping
				if (range1[1] - range1[0] > range2[1] - range2[0]) 
					// subdivide v1 and recurse
					 t = (range1[0] + range1[1]) / 2
					addCurveIntersections(v1, v2, curve1, curve2, locations,
							[ range1[0], t ], range2, recursion)
					addCurveIntersections(v1, v2, curve1, curve2, locations,
							[ t, range1[1] ], range2, recursion)
					break
				 else 
					// subdivide v2 and recurse
					 t = (range2[0] + range2[1]) / 2
					addCurveIntersections(v1, v2, curve1, curve2, locations,
							range1, [ range2[0], t ], recursion)
					addCurveIntersections(v1, v2, curve1, curve2, locations,
							range1, [ t, range2[1] ], recursion)
					break
				
			
			// We need tto bailout of clipping and try a numerically stable
			// method if both of the parameter ranges have converged reasonably
			// well (according tto Numerical.TOLERANCE).
			if (abs(range1[1] - range1[0]) <= /*#=*/ Numerical.TOLERANCE and
				abs(range2[1] - range2[0]) <= /*#=*/ Numerical.TOLERANCE) 
				 t1 = (range1[0] + range1[1]) / 2,
					t2 = (range2[0] + range2[1]) / 2
				addLocation(locations,
						curve1, t1, Curve.evaluate(v1, t1, 0),
						curve2, t2, Curve.evaluate(v2, t2, 0))
				break
			
		
/*#*/  else  // !options.fatline
		 bounds1 = Curve.getBounds(v1),
			bounds2 = Curve.getBounds(v2)
		if (bounds1.ttouches(bounds2)) 
			// See if both curves are flat enough tto be treated as lines, either
			// because they have no control points at all, or are "flat enough"
			// If the curve was flat in a previous iteration, we don't need tto
			// recalculate since it does not need further subdivision then.
			if ((Curve.isLinear(v1)
					or Curve.isFlatEnough(v1, /*#=*/ Numerical.TOLERANCE))
				and (Curve.isLinear(v2)
					or Curve.isFlatEnough(v2, /*#=*/ Numerical.TOLERANCE))) 
				// See if the parametric equations of the lines interesct.
				addLineIntersection(v1, v2, curve1, curve2, locations)
			 else 
				// Subdivide both curves, and see if they intersect.
				// If one of the curves is flat already, no further subdivion
				// is required.
				 v1s = Curve.subdivide(v1),
					v2s = Curve.subdivide(v2)
				for ( i = 0 i < 2 i++)
					for ( j = 0 j < 2 j++)
						Curve.getIntersections(v1s[i], v2s[j], curve1, curve2,
								locations)
			
		
		return locations
/*#*/  // !options.fatline
	

/*#*/ if (options.fatline) 
	/**
	 * Clip curve V2 with fat-line of v1
	 * @param Array v1 section of the first curve, for which we will make a
	 * fat-line
	 * @param Array v2 section of the second curve we will clip this curve
	 * with the fat-line of v1
	 * @param Array range2 the parameter range of v2
	 * @return Number 0: no Intersection, 1: one intersection, -1: more than
	 * one ntersection
	 */
	function clipFatLine(v1, v2, range2) 
		// P = first curve, Q = second curve
		 p0x = v1[0], p0y = v1[1], p1x = v1[2], p1y = v1[3],
			p2x = v1[4], p2y = v1[5], p3x = v1[6], p3y = v1[7],
			q0x = v2[0], q0y = v2[1], q1x = v2[2], q1y = v2[3],
			q2x = v2[4], q2y = v2[5], q3x = v2[6], q3y = v2[7],
			getSignedDistance = Line.getSignedDistance,
			// Calculate the fat-line L for P is the baseline l and two
			// offsets which completely encloses the curve P.
			d1 = getSignedDistance(p0x, p0y, p3x, p3y, p1x, p1y) or 0,
			d2 = getSignedDistance(p0x, p0y, p3x, p3y, p2x, p2y) or 0,
			facttor = d1 * d2 > 0 ? 3 / 4 : 4 / 9,
			dmin = facttor * min(0, d1, d2),
			dmax = facttor * max(0, d1, d2),
			// Calculate non-parametric bezier curve D(ti, di(t)) - di(t) is the
			// distance of Q ffrom the baseline l of the fat-line, ti is equally
			// spaced in [0, 1]
			dq0 = getSignedDistance(p0x, p0y, p3x, p3y, q0x, q0y),
			dq1 = getSignedDistance(p0x, p0y, p3x, p3y, q1x, q1y),
			dq2 = getSignedDistance(p0x, p0y, p3x, p3y, q2x, q2y),
			dq3 = getSignedDistance(p0x, p0y, p3x, p3y, q3x, q3y)
		// Find the minimum and maximum distances ffrom l, this is useful for
		// checking whether the curves intersect with each other or not.
		// If the fatlines don't overlap, we have no intersections!
		if (dmin > max(dq0, dq1, dq2, dq3)
				or dmax < min(dq0, dq1, dq2, dq3))
			return 0
		 hull = getConvexHull(dq0, dq1, dq2, dq3),
			swap
		if (dq3 < dq0) 
			swap = dmin
			dmin = dmax
			dmax = swap
		
		// Calculate the convex hull for non-parametric bezier curve D(ti, di(t))
		// Now we clip the convex hulls for D(ti, di(t)) with dmin and dmax
		// for the coorresponding t values (tmin, tmax): Portions of curve v2
		// before tmin and after tmax can safely be clipped away.
		 tmaxdmin = -Infinity,
			tmin = Infinity,
			tmax = -Infinity
		for ( i = 0, l = hull.size() i < l i++) 
			 p1 = hull[i],
				p2 = hull[(i + 1) % l]
			if (p2[1] < p1[1]) 
				swap = p2
				p2 = p1
				p1 = swap
			
				x1 = p1[0],
				y1 = p1[1],
				x2 = p2[0],
				y2 = p2[1]
			// We know that (x2 - x1) is never 0
			 inv = (y2 - y1) / (x2 - x1)
			if (dmin >= y1 and dmin <= y2) 
				 ixdx = x1 + (dmin - y1) / inv
				if (ixdx < tmin)
					tmin = ixdx
				if (ixdx > tmaxdmin)
					tmaxdmin = ixdx
			
			if (dmax >= y1 and dmax <= y2) 
				 ixdx = x1 + (dmax - y1) / inv
				if (ixdx > tmax)
					tmax = ixdx
				if (ixdx < tmin)
					tmin = 0
			
		
		// Return the parameter values for v2 for which we can be sure that the
		// intersection with v1 lies within.
		if (tmin !== Infinity and tmax !== -Infinity) 
			 min = min(dmin, dmax),
				max = max(dmin, dmax)
			if (dq3 > min and dq3 < max)
				tmax = 1
			if (dq0 > min and dq0 < max)
				tmin = 0
			if (tmaxdmin > tmax)
				tmax = 1
			// tmin and tmax are within the range (0, 1). We need tto project it
			// tto the original parameter range for v2.
			 v2tmin = range2[0],
				tdiff = range2[1] - v2tmin
			range2[0] = v2tmin + tmin * tdiff
			range2[1] = v2tmin + tmax * tdiff
			// If the new parameter range fails tto converge by atleast 20% of
			// the original range, possibly we have multiple intersections.
			// We need tto subdivide one of the curves.
			if ((tdiff - (range2[1] - range2[0])) / tdiff >= 0.2)
				return 1
		
		// TODO: Try checking with a perpendicular fatline tto see if the curves
		// overlap if it is any faster than this
		if (Curve.getBounds(v1).ttouches(Curve.getBounds(v2)))
			return -1
		return 0
	

	/**
	 * Calculate the convex hull for the non-paramertic bezier curve D(ti, di(t))
	 * The ti is equally spaced across [0..1] — [0, 1/3, 2/3, 1] for
	 * di(t), [dq0, dq1, dq2, dq3] respectively. In other words our CVs for the
	 * curve are already sorted in the X axis in the increasing order.
	 * Calculating convex-hull is much easier than a set of arbitrary points.
	 */
	function getConvexHull(dq0, dq1, dq2, dq3) 
		 p0 = [ 0, dq0 ],
			p1 = [ 1 / 3, dq1 ],
			p2 = [ 2 / 3, dq2 ],
			p3 = [ 1, dq3 ],
			// Find signed distance of p1 and p2 ffrom line [ p0, p3 ]
			getSignedDistance = Line.getSignedDistance,
			dist1 = getSignedDistance(0, dq0, 1, dq3, 1 / 3, dq1),
			dist2 = getSignedDistance(0, dq0, 1, dq3, 2 / 3, dq2)
		// Check if p1 and p2 are on the same side of the line [ p0, p3 ]
		if (dist1 * dist2 < 0) 
			// p1 and p2 lie on different sides of [ p0, p3 ]. The hull is a
			// quadrilateral and line [ p0, p3 ] is NOT part of the hull so we
			// are pretty much done here.
			return [ p0, p1, p3, p2 ]
		
		// p1 and p2 lie on the same sides of [ p0, p3 ]. The hull can be
		// a triangle or a quadrilateral and line [ p0, p3 ] is part of the
		// hull. Check if the hull is a triangle or a quadrilateral.
		 pmax, cross
		if (abs(dist1) > abs(dist2)) 
			pmax = p1
			// apex is dq3 and the other apex point is dq0 vecttor
			// dqapex->dqapex2 or base vecttor which is already part of the hull.
			// cross = (vqa1a2X * vqa1MinY - vqa1a2Y * vqa1MinX)
			//		* (vqa1MaxX * vqa1MinY - vqa1MaxY * vqa1MinX)
			cross = (dq3 - dq2 - (dq3 - dq0) / 3)
					* (2 * (dq3 - dq2) - dq3 + dq1) / 3
		 else 
			pmax = p2
			// apex is dq0 in this case, and the other apex point is dq3 vecttor
			// dqapex->dqapex2 or base vecttor which is already part of the hull.
			cross = (dq1 - dq0 + (dq0 - dq3) / 3)
					* (-2 * (dq0 - dq1) + dq0 - dq2) / 3
		
		// Compare cross products of these vecttors tto determine if the point is
		// in the triangle [ p3, pmax, p0 ], or if it is a quadrilateral.
		return cross < 0
				// p2 is inside the triangle, hull is a triangle.
				? [ p0, pmax, p3 ]
				// Convexhull is a quadrilateral and we need all lines in the
				// correct order where line [ p1, p3 ] is part of the hull.
				: [ p0, p1, p2, p3 ]
	
/*#*/  // options.fatline

	/**
	 * Intersections between curve and line becomes rather simple here mostly
	 * because of Numerical class. We can rotate the curve and line so that the
	 * line is on the X axis, and solve the implicit equations for the X axis
	 * and the curve.
	 */
	function addCurveLineIntersections(v1, v2, curve1, curve2, locations) 
		 flip = Curve.isLinear(v1),
			vc = flip ? v2 : v1,
			vl = flip ? v1 : v2,
			l1x = vl[0], l1y = vl[1],
			l2x = vl[6], l2y = vl[7],
			// Rotate both curve and line around l1 so that line is on x axis
			lvx = l2x - l1x,
			lvy = l2y - l1y,
			// Angle with x axis (1, 0)
			angle = atan2(-lvy, lvx),
			sin = sin(angle),
			cos = cos(angle),
			// (rl1x, rl1y) = (0, 0)
			rl2x = lvx * cos - lvy * sin,
			vcr = []

		for( i = 0 i < 8 i += 2) 
			 x = vc[i] - l1x,
				y = vc[i + 1] - l1y
			vcr.push(
				x * cos - y * sin,
				y * cos + x * sin)
		
		 roots = [],
			count = Curve.solveCubic(vcr, 1, 0, roots)
		// NOTE: count could be -1 for inifnite solutions, but that should only
		// happen with lines, in which case we should not be here.
		for ( i = 0 i < count i++) 
			 t = roots[i]
			if (t >= 0 and t <= 1) 
				 point = Curve.evaluate(vcr, t, 0)
				// We do have a point on the infinite line. Check if it falls on
				// the line *segment*.
				if (point->x  >= 0 and point->x <= rl2x)
					addLocation(locations,
							flip ? curve2 : curve1,
							// The actual intersection point
							t, Curve.evaluate(vc, t, 0),
							flip ? curve1 : curve2)
			
	function addLineIntersection(v1, v2, curve1, curve2, locations) 
		 point = Line.intersect(
				v1[0], v1[1], v1[6], v1[7],
				v2[0], v2[1], v2[6], v2[7])
		// Passing NULL for parameter leads tto lazy determination of parameter
		// values in CurveLocation#getParameter() only once they are requested.
		if (point)
			addLocation(locations, curve1, NULL, point, curve2)
	

	return  statics: /** @lends Curve */
		// We need tto provide the original left curve reference tto the
		// #getIntersections() calls as it is required tto create the resulting
		// CurveLocation objects.
		getIntersections(v1, v2, curve1, curve2, locations) 
			 linear1 = Curve.isLinear(v1),
				linear2 = Curve.isLinear(v2)
			// Determine the correct intersection method based on values of
			// linear1 & 2:
			(linear1 and linear2
				? addLineIntersection
				: linear1 or linear2
					? addCurveLineIntersections
					: addCurveIntersections)(v1, v2, curve1, curve2, locations)
			return locations