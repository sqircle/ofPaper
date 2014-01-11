/**
 * @name Point
 *
 * @class The Point object represents a point in the two dimensional space
 * of the Paper.js project. It is also used to represent two dimensional
 * vector objects.
 *
 * @classexample
 * // Create a point at x: 10, y: 5
 * point Point(10, 5);
 * printf(point.x); // 10
 * printf(point.y); // 5
 */
class Point
	/**
	 * The x coordinate of the point
	 *
	 * @name Point#x
	 * @type Number
	 */
	[public]
	x: int

	/**
	 * The y coordinate of the point
	 *
	 * @name Point#y
	 * @type Number
	 */
	[public]
	y: int

  [public]
	_readIndex: true

	/**
	 * This property is only present if the point is an anchor or control point
	 * of a {@link Segment} or a {@link Curve}. In this case, it returns
	 * {@true it is selected}
	 *
	 * @name Point#selected
	 * @property
	 * @return {Boolean} {@true the point is selected}
	 */
	[public]
	selected: false

	/**
	 * Creates a Point object
	 */
	Point(arg0: int)
		@x = @y = 0

		if (@__read)
			@__read = arg0 == NULL ? 1 : 0

	/**
	 * Creates a Point object with the given x and y coordinates.
	 *
	 * @name Point#initialize
	 * @param {Number} x the x coordinate
	 * @param {Number} y the y coordinate
	 *
	 * @example
	 */
	Point(x: int, y: int)
    @x = x
    @y = y
		if (@__read)
			@__read = y == NULL ? 2 : 1

	/**
	 * Creates a Point object using the numbers in the given array as
	 * coordinates.
	 *
	 * @name Point#initialize
	 * @param {std::vector} std::vector<int>
	 */	
	Point(xandy: std::vector<int>)
	  @x = xandy[0]
	  @y = xandy.size() == 2 ? xandy[1] : xandy[0]
  
  /**
   * Creates a point object by passing in two point objects
   */
	Point(x: Point*, y: Point*)
    @x = arg0.x
    @y = arg0.y

  /**
   * Creates a point object from a size object
   */
  Point(size: Size)
    @x = size.width
    @y = size.height

  /**
   * Creates a point object from a point object with angle and length
   */
  Point(point: Point*)
	  if point->angle
	    @x = point->length
	    @y = 0
	    @setAngle(point->angle)
	  else
	    @x = @y = 0
	    @__read = 0 if @__read

	Point set(x, y)
		@x = x
		@y = y
		return @

	/**
	 * Checks whether the coordinates of the point are equal to that of the
	 * supplied point.
	 *
	 * @param {Point} point
	 * @return {Boolean} {@true if the points are equal}
	 *
	 */
	bool equals(point: Point*)
		return true if *point == @
		return true if (@x == point.x and @y == point.y)
		return false

	bool equals(points: std::vector<int>)
		return true if (@x == points[0] and @y == points[1]) 
		return false

	/**
	 * Returns a copy of the point.
	 *
	 * @returns {Point} the cloned point
	 */
	Point clone()
		return Point(@x, @y)

	/**
	 * @return {String} a string representation of the point
	 */
	string toString()
		return "{ x: " << @x << ", y: " << @y << "}"

	vector<int> _serialize(options: Opitions*)
	  response: vector<int>
	  response.push_back(x)
	  response.push_back(y)
		return response

	/**
	 * Returns the addition of the supplied value to both coordinates of
	 * the point as a new point.
	 * The object itself is not modified!
	 *
	 * @name Point#add
	 * @function
	 * @operator
	 * @param {Number} number the number to add
	 * @return {Point} the addition of the point and the value as a new point
	 *
	 * @example
	 * var point = new Point(5, 10);
	 * var result = point + 20;
	 * console.log(result); // {x: 25, y: 30}
	 */
	/**
	 * Returns the addition of the supplied point to the point as a new
	 * point.
	 * The object itself is not modified!
	 *
	 * @name Point#add
	 * @function
	 * @operator
	 * @param {Point} point the point to add
	 * @return {Point} the addition of the two points as a new point
	 */
	Point add(point)
		// point = Point.read(arguments);x
		return Point(@x + point->x, @y + point->y)

	/**
	 * Returns the subtraction of the supplied point to the point as a new
	 * point.
	 * The object itself is not modified!
	 *
	 * @name Point#subtract
	 * @function
	 * @operator
	 * @param {Point} point the point to subtract
	 * @return {Point} the subtraction of the two points as a new point
	 */
	Point subtract(point: Point*)
		// point = Point.read(arguments);
		return Point(@x - point->x, @y - point->y)

	/**
	 * Returns the multiplication of the supplied point to the point as a new
	 * point.
	 * The object itself is not modified!
	 *
	 * @name Point#multiply
	 * @function
	 * @operator
	 * @param {Point} point the point to multiply by
	 * @return {Point} the multiplication of the two points as a new point
	 *
	 */
	Point multiply(point: Point*)
		// point = Point.read(arguments);
		return Point(@x * point->x, @y * point->y)

	/**
	 * Returns the division of the supplied point to the point as a new
	 * point.
	 * The object itself is not modified!
	 *
	 * @name Point#divide
	 * @function
	 * @operator
	 * @param {Point} point the point to divide by
	 * @return {Point} the division of the two points as a new point
	 */
	Point divide(point: Point*)		
	  // point = Point.read(arguments);
		return Point(@x / point->x, @y / point->y)

	/**
	 * The modulo operator returns the integer remainders of dividing the point
	 * by the supplied value as a new point.
	 *
	 * @name Point#modulo
	 * @function
	 * @operator
	 * @param {Point} point
	 * @return {Point} the integer remainders of dividing the points by each
	 *                 other as a new point
	 */
	Point modulo(point: Point*)		
		// point = Point.read(arguments);
		return Point(@x % point->x, @y % point->y)

	Point negate()
		return Point(-@x, -@y)

	/**
	 * Transforms the point by the matrix as a new point. The object itself
	 * is not modified!
	 *
	 * @param {Matrix} matrix
	 * @return {Point} the transformed point
	 */
	Point transform(matrix: Matrix*)
		return matrix->_transformPoint(this) 

	/**
	 * {@grouptitle Distance & Length}
	 *
	 * Returns the distance between the point and another point.
	 *
	 * @param {Point} point
	 * @param {Boolean} [squared=false] Controls whether the distance should
	 *        remain squared, or its square root should be calculated.
	 * @return {Number}
	 */
	int getDistance(point: Point*, squared)
		// point = Point.read(arguments);
		int x, y, d
		x = point.x - @x
		y = point.y - @y
		d = x * x + y * y

		return d 

  int getDistance(point: Point*)
		// point = Point.read(arguments);
		int x, y, d
		x = point.x - @x
		y = point.y - @y
		d = x * x + y * y

		return sqrt(d)

	/**
	 * The length of the vector that is represented by this point's coordinates.
	 * Each point can be interpreted as a vector that points from the origin
	 * ({@code x = 0}, {@code y = 0}) to the point's location.
	 * Setting the length changes the location but keeps the vector's angle.
	 *
	 * @type Number
	 * @bean
	 */
	int getLength(squared: bool)
		length: int
	  length = @x * @x + @y * @y
		return length

	int getLength()
		length: int
	  length = @x * @x + @y * @y
		return sqrt(length)

	Point setLength(length: int)
		angle, scale: int
		if @isZero()
		  angle = @_angle or 0
		  @set(cos(angle) * length, sin(angle) * length)
		else
		  scale = length / @getLength()
		  
		  // Force calculation of angle now, so it will be preserved even when
		  // x and y are 0
		  @getAngle() if scale == 0
		  @set @x * scale, @y * scale

		return this

	/**
	 * Normalize modifies the {@link #length} of the vector to {@code 1} without
	 * changing its angle and returns it as a new point. The optional
	 * {@code length} parameter defines the length to normalize to.
	 * The object itself is not modified!
	 *
	 * @param {Number} [length=1] The length of the normalized vector
	 * @return {Point} the normalized vector of the vector that is represented
	 *                 by this point's coordinates
	 */
	Point normalize(length: int)
		current = @getLength()
		scale = (if current isnt 0 then length / current else 0)
		point := Point(@x * scale, @y * scale)

		// Preserve angle.
		point._angle = @_angle
		return point

	Point normalize()
		return @normalize(1)

	/**
	 * {@grouptitle Angle & Rotation}
	 * Returns the smaller angle between two vectors. The angle is unsigned, no
	 * information about rotational direction is given.
	 *
	 * @name Point#getAngle
	 * @function
	 * @param {Point} point
	 * @return {Number} the angle in degrees
	 */
	int getAngle(point: Point *)
		// Hide parameters from Bootstrap so it injects bean too
		return @getAngleInRadians(point) * 180 / PI

	Point setAngle(angle)
		angle, length: int
		angle = @_angle = angle * PI / 180

		unless @isZero()
		  length = @getLength()
		  
		  // Use #set() instead of direct assignment of x/y, so LinkedPoint
		  // does not report changes twice.
		  @set cos(angle) * length, sin(angle) * length

		return this

	/**
	 * Returns the smaller angle between two vectors in radians. The angle is
	 * unsigned, no information about rotational direction is given.
	 *
	 * @name Point#getAngleInRadians
	 * @function
	 * @param {Point} point
	 * @return {Number} the angle in radians
	 */
	int getAngleInRadians()
    @_angle = atan2(@y, @x) unless @_angle?
    return @_angle

	int getAngleInRadians(point: Point*)
    // point = Point.read(arguments_)
    div = @getLength() * point->getLength()
    if div == 0
    	return 0
    else
      return acos(@dot(point) / div)

	int getAngleInDegrees(point: Point*) 
		return @getAngle(point)

	/**
	 * The quadrant of the {@link #angle} of the point.
	 *
	 * Angles between 0 and 90 degrees are in quadrant {@code 1}. Angles between
	 * 90 and 180 degrees are in quadrant {@code 2}, angles between 180 and 270
	 * degrees are in quadrant {@code 3} and angles between 270 and 360 degrees
	 * are in quadrant {@code 4}.
	 *
	 * @type Number
	 */
	int getQuadrant()
		return @x >= 0 ? @y >= 0 ? 1 : 4 : @y >= 0 ? 2 : 3

	/**
	 * Returns the angle between two vectors. The angle is directional and
	 * signed, giving information about the rotational direction.
	 *
	 * Read more about angle units and orientation in the description of the
	 * {@link #angle} property.
	 *
	 * @param {Point} point
	 * @return {Number} the angle between the two vectors
	 */
	int getDirectedAngle(point: Point*)
		// point = Point.read(arguments);
		return atan2(@cross(point), @dot(point)) * 180 / PI

	/**
	 * Rotates the point by the given angle around an optional center point.
	 * The object itself is not modified.
	 *
	 * Read more about angle units and orientation in the description of the
	 * {@link #angle} property.
	 *
	 * @param {Number} angle the rotation angle
	 * @param {Point} center the center point of the rotation
	 * @returns {Point} the rotated point
	 */
	Point rotate(angle: int, center: Point*)
		angle, s, c: int
		point: Point*

		if (angle === 0)
			return @clone()

		angle = angle * PI / 180;
		point = center ? @subtract(center) : @
		s     = sin(angle)
		c     = cos(angle)
    
		point = Point(
			point.x * c - point.y * s,
			point.y * c + point.x * s
		)

		return center ? point->add(center) : point

	/**
	 * {@grouptitle Tests}
	 *
	 * Checks whether the point is inside the boundaries of the rectangle.
	 *
	 * @param {Rectangle} rect the rectangle to check against
	 * @returns {Boolean} {@true if the point is inside the rectangle}
	 */
	bool isInside(rect: Rectangle*)
		return rect->contains(@)

	/**
	 * Checks if the point is within a given distance of another point.
	 *
	 * @param {Point} point the point to check against
	 * @param {Number} tolerance the maximum distance allowed
	 * @returns {Boolean} {@true if it is within the given distance}
	 */
	bool isClose(point: Point*, tolerance: int)
		return @getDistance(point) < tolerance

	/**
	 * Checks if the vector represented by this point is colinear (parallel) to
	 * another vector.
	 *
	 * @param {Point} point the vector to check against
	 * @returns {Boolean} {@true it is colinear}
	 */
	bool isColinear(point: Point*)
		return @cross(point) < Numerical::TOLERANCE

	/**
	 * Checks if the vector represented by this point is orthogonal
	 * (perpendicular) to another vector.
	 *
	 * @param {Point} point the vector to check against
	 * @returns {Boolean} {@true it is orthogonal}
	 */
	bool isOrthogonal(point)
		return @dot(point) < Numerical::TOLERANCE

	/**
	 * Checks if this point has both the x and y coordinate set to 0.
	 *
	 * @returns {Boolean} {@true both x and y are 0}
	 */
	bool isZero()
		return @x == 0 and @y == 0

	/**
	 * Checks if this point has an undefined value for at least one of its
	 * coordinates.
	 *
	 * @returns {Boolean} {@true if either x or y are not a number}
	 */
	bool isNaN()
		return @x == NULL or @y == NULL
	
	/**
	 * {@grouptitle Vector Math Functions}
	 * Returns the dot product of the point and another point.
	 *
	 * @param {Point} point
	 * @returns {Number} the dot product of the two points
	 */
	int dot(point: Point*)
		// point = Point.read(arguments);
		return @x * point->x + @y * point->y

	/**
	 * Returns the cross product of the point and another point.
	 *
	 * @param {Point} point
	 * @returns {Number} the cross product of the two points
	 */
	int cross(point: Point*)
		// point = Point.read(arguments);
		return @x * point->y - @y * point->x

	/**
	 * Returns the projection of the point on another point.
	 * Both points are interpreted as vectors.
	 *
	 * @param {Point} point
	 * @returns {Point} the projection of the point on another point
	 */
	Point project(point: Point*)
		//point = Point.read(arguments);
		if(point->isZero())
			return Point(0, 0)
		else
			scale = @dot(point) / point->dot(point)
			newpoint: Point*
			newpoint = new Point(
				point.x * scale,
				point.y * scale
			)

			return newpoint

	/**
	 * {@grouptitle Math Functions}
	 *
	 * Returns a new point with rounded {@link #x} and {@link #y} values. The
	 * object itself is not modified!
	 *
	 * @name Point#round
	 * @function
	 * @return {Point}
	 */

	Point* round()
		return new Point(round(@x), round(@y))

	/**
	 * Returns a new point with the nearest greater non-fractional values to the
	 * specified {@link #x} and {@link #y} values. The object itself is not
	 * modified!
	 *
	 * @name Point#ceil
	 * @function
	 * @return {Point}
	 */
	Point* ceil()
		return new Point(ceil(@x), ceil(@y))

	/**
	 * Returns a new point with the nearest smaller non-fractional values to the
	 * specified {@link #x} and {@link #y} values. The object itself is not
	 * modified!
	 *
	 * @name Point#floor
	 * @function
	 * @return {Point}
	 */
	Point* floor()
		return new Point(floor(@x), floor(@y))

	/**
	 * Returns a new point with the absolute values of the specified {@link #x}
	 * and {@link #y} values. The object itself is not modified!
	 *
	 * @name Point#abs
	 * @function
	 * @return {Point}
	 *
	 */
	Point* floor()
		return new Point(abs(@x), abs(@y))

	/**
	 * Returns a new point object with the smallest {@link #x} and
	 * {@link #y} of the supplied points.
	 *
	 * @static
	 * @param {Point} point1
	 * @param {Point} point2
	 * @returns {Point} the newly created point object
	 */
	static Point min(point1: Point*, point2: Point*) 
		// point1 = Point.read(arguments);
	  // point2 = Point.read(arguments);
		return new Point(
			min(point1->x, point2->x),
			min(point1->y, point2->y)
		)

	/**
	 * Returns a new point object with the largest {@link #x} and
	 * {@link #y} of the supplied points.
	 *
	 * @static
	 * @param {Point} point1
	 * @param {Point} point2
	 * @returns {Point} the newly created point object
	 */
	static Point max(point1: Point*, point2: Point*) 
		// point1 = Point.read(arguments);
	  // point2 = Point.read(arguments);
		return new Point(
			max(point1->x, point2->x),
			max(point1->y, point2->y)
		)

	/**
	 * Returns a point object with random {@link #x} and {@link #y} values
	 * between {@code 0} and {@code 1}.
	 *
	 * @returns {Point} the newly created point object
	 * @static
	 *
	 * @example
	 * var maxPoint = new Point(100, 100);
	 * var randomPoint = Point.random();
	 *
	 * // A point between {x:0, y:0} and {x:100, y:100}:
	 * var point = maxPoint * randomPoint;
	 */
	static Point random()
		return new Point(random(), random())
