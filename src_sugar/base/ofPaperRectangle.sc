/**
 * @name Rectangle
 *
 * @class A Rectangle specifies an area that is enclosed by it's top-left
 * point (x, y), its width, and its height. It should not be confused with a
 * rectangular path, it is not an item.
 */
class Rectangle
	// Tell Base.read that the Rectangle constructor supports reading with index
	// @read corresponds to the number of arguments read.
	[public]
	_readIndex := true
	x, y, width, height: float
	_fixX: float
	_fixY: short
	_fixW, fixH: short

	/**
	 * Creates a Rectangle object.
	 *
	 * @name Rectangle#initialize
	 * @param {Point} point the top-left point of the rectangle
	 * @param {Size} size the size of the rectangle
	 */
	/**
	 * Creates a Rectangle object.
	 *
	 * @name Rectangle#initialize
	 * @param {Object} object an object containing properties to be set on the
	 *        rectangle.
	 */
	/**
	 * Creates a rectangle object from the passed points. These do not
	 * necessarily need to be the top left and bottom right corners, the
	 * constructor figures out how to fit a rectangle between them.
	 *
	 * @name Rectangle#initialize
	 * @param {Point} from The first point defining the rectangle
	 * @param {Point} to The second point defining the rectangle
	 */
	/**
	 * Creates a new rectangle object from the passed rectangle object.
	 *
	 * @name Rectangle#initialize
	 * @param {Rectangle} rt
	 */
  Rectangle()
  	@x = @y = @width = @height = 0
		@read = 0
  
  Rectangle* initRectangle()
	  @read = 1

	/**
	 * Creates a rectangle object.
	 *
	 * @name Rectangle#initialize
	 * @param {Number} x the left coordinate
	 * @param {Number} y the top coordinate
	 * @param {Number} width
	 * @param {Number} height
	 */
	Rectangle* initRectangle(x: int, y: int, width: int, height: int)
		@x      = x
		@y      = y
		@width  = width
		@height = height
		@read   = 4
  
  Rectangle* initRectangle(vect: vector<int>)
	  @x      = vect[0]
		@y      = vect[1]
		@width  = vect[2]
		@height = vect[3]
		@read = 1

	Rectangle* initRectangle(point: Point*, size: Size*)
		@x      = point->x or 0
		@y      = point->y or 0
		@width  = size->width  or 0
		@height = size->height or 0
		@read = 1

	/**
	 * The x position of the rectangle.
	 *
	 * @name Rectangle#x
	 * @type Number
	 */

	/**
	 * The y position of the rectangle.
	 *
	 * @name Rectangle#y
	 * @type Number
	 */

	/**
	 * The width of the rectangle.
	 *
	 * @name Rectangle#width
	 * @type Number
	 */

	/**
	 * The height of the rectangle.
	 *
	 * @name Rectangle#height
	 * @type Number
	 */

	/**
	 * @ignore
	 */
	Rectangle* set(x: int, y: int, width: int, height: int)
		@x = x
		@y = y
		@width = width
		@height = height

		return @

	/**
	 * Returns a copy of the rectangle.
	 */
	Rectangle* clone()
		return new Rectangle(@x, @y, @width, @height)

	/**
	 * Checks whether the coordinates and size of the rectangle are equal to
	 * that of the supplied rectangle.
	 *
	 * @param {Rectangle} rect
	 * @return {Boolean} {@true if the rectangles are equal}
	 */
	bool equals(rect: Rectangle*)
		return rect == @ or rect and @x == rect->x and @y == rect->y
					and @width == rect->width and @height == rect.height
				  or false

	/**
	 * @return {String} a string representation of this rectangle
	 */
	string toString()
		ret: string

	  ret << "{ x: " << @x
		ret << ", y: " << @y
		ret << ", width: " << @width
		ret << ", height: " << @height
		ret << " }"

		return ret

	int[3] _serialize()
		return {@x, @y, @width, @height}

	/**
	 * The top-left point of the rectangle
	 *
	 * @type Point
	 * @bean
	 */
	Point* getPoint(dontLink: bool)
	  if dontLink
	  	return new Point(@x, @y, @, 'setPoint')
	  else
		  return new LinkedPoint(@x, @y, @, 'setPoint')

	LinkedPoint* getPoint()
	  return new LinkedPoint(@x, @y, @, 'setPoint')

	Rectangle* setPoint(point: Point*)
		// point = Point.read(arguments)
		@x = point->x
		@y = point->y
    
    return @

	/**
	 * The size of the rectangle
	 *
	 * @type Size
	 * @bean
	 */
	Size* getSize(dontLink: bool)
	  if dontLink
			return new Size(@width, @height, this, 'setSize')
		else
			return new LinkedSize(@width, @height, this, 'setSize')

  /**
	 * The size of the rectangle
	 *
	 * @type Size
	 * @bean
	 */
	LinkedSize* getSize()
		return new LinkedSize(@width, @height, this, 'setSize')

	Rectangle* setSize(size: Size*)
		// size = Size.read(arguments)
		// Keep track of how dimensions were specified through @_fix*
		// attributes.
		// _fixX / Y can either be 0 (l), 0.5 (center) or 1 (r), and is used as
		// direct factors to calculate the x / y adujstments from the size
		// differences.
		// _fixW / H is either 0 (off) or 1 (on), and is used to protect
		// widht / height values against changes.
		if @_fixX != 0
			@x += (@width - size->width) * @_fixX

		if @_fixY != 0
			@y += (@height - size->height) * @_fixY

		@width  = size->width
		@height = size->height
		@_fixW = 1
		@_fixH = 1

		return @

	/**
	 * {@grouptitle Side Positions}
	 *
	 * The position of the left hand side of the rectangle. Note that this
	 * doesn't move the whole rectangle the right hand side stays where it was.
	 *
	 * @type Number
	 * @bean
	 */
	getLeft()
		return @x

	setLeft(left)
		if @_fixW != 0
			@width -= left - @x

		@x = left
		@_fixX = 0

	/**
	 * The top coordinate of the rectangle. Note that this doesn't move the
	 * whole rectangle: the bottom won't move.
	 *
	 * @type Number
	 * @bean
	 */
	int getTop()
		return @y

	Rectangle* setTop(top: int) 
		if @_fixH != 0
			@height -= top - @y

		@y = top
		@_fixY = 0

		return @

	/**
	 * The position of the right hand side of the rectangle. Note that this
	 * doesn't move the whole rectangle the left hand side stays where it was.
	 *
	 * @type Number
	 * @bean
	 */
	int getRight()
		return @x + @width

	Rectangle* setRight(right: int)
		// Turn _fixW off if we specify two _fixX values
		if @_fixX != NULL and @_fixX != 1
			@_fixW = 0

		if @_fixW != 0
			@x = right - @width
		else
			@width = right - @x

		@_fixX = 1

		return @


	/**
	 * The bottom coordinate of the rectangle. Note that this doesn't move the
	 * whole rectangle: the top won't move.
	 *
	 * @type Number
	 * @bean
	 */
	int getBottom()
		return @y + @height

	Rectangle* setBottom(bottom: int)
		// Turn _fixH off if we specify two _fixY values
		if @_fixY != NULL and @_fixY != 1
			@_fixH = 0

		if @_fixH != 0
			@y = bottom - @height
		else
			@height = bottom - @y
		@_fixY = 1

		return @

	/**
	 * The center-x coordinate of the rectangle.
	 *
	 * @type Number
	 * @bean
	 * @ignore
	 */
	float getCenterX()
		return @x + @width * 0.5

	Rectangle* setCenterX(x: float)
		@x = x - @width * 0.5
		@_fixX = 0.5

	/**
	 * The center-y coordinate of the rectangle.
	 *
	 * @type Number
	 * @bean
	 * @ignore
	 */
	float getCenterY() 
		return @y + @height * 0.5

	Rectangle* setCenterY(y: int) 
		@y = y - @height * 0.5
		@_fixY = 0.5

		return @

	/**
	 * {@grouptitle Corner and Center Point Positions}
	 *
	 * The center point of the rectangle.
	 *
	 * @type Point
	 * @bean
	 */
	getCenter(dontLink: bool)
	  if dontLink
			return new Point(@getCenterX(), @getCenterY(), this, 'setCenter')
		else
			return new LinkedPoint(@getCenterX(), @getCenterY(), this, 'setCenter')

	getCenter()
		return new LinkedPoint(@getCenterX(), @getCenterY(), this, 'setCenter')

	Rectangle* setCenter(point: Point*)
		// point = Point.read(arguments)
		@setCenterX(point->x)
		@setCenterY(point->y)

		// A special setter where we allow chaining, because it comes in handy
		// in a couple of places in core.
		return @

	/**
	 * The top-left point of the rectangle.
	 *
	 * @name Rectangle#topLeft
	 * @type Point
	 */

	/**
	 * The top-right point of the rectangle.
	 *
	 * @name Rectangle#topRight
	 * @type Point
	 */

	/**
	 * The bottom-left point of the rectangle.
	 *
	 * @name Rectangle#bottomLeft
	 * @type Point
	 */

	/**
	 * The bottom-right point of the rectangle.
	 *
	 * @name Rectangle#bottomRight
	 * @type Point
	 */

	/**
	 * The left-center point of the rectangle.
	 *
	 * @name Rectangle#leftCenter
	 * @type Point
	 */

	/**
	 * The top-center point of the rectangle.
	 *
	 * @name Rectangle#topCenter
	 * @type Point
	 */

	/**
	 * The right-center point of the rectangle.
	 *
	 * @name Rectangle#rightCenter
	 * @type Point
	 */

	/**
	 * The bottom-center point of the rectangle.
	 *
	 * @name Rectangle#bottomCenter
	 * @type Point
	 */

	/**
	 * @return {Boolean} {@true the rectangle is empty}
	 */
	bool isEmpty()
		return @width == 0 or @height == 0

	/**
	 * {@grouptitle Geometric Tests}
	 *
	 * Tests if the specified point is inside the boundary of the rectangle.
	 *
	 * @name Rectangle#contains
	 * @function
	 * @param {Point} point the specified point
	 * @return {Boolean} {@true if the point is inside the rectangle's boundary}
	 */

	/**
	 * Tests if the interior of the rectangle entirely contains the specified
	 * rectangle.
	 *
	 * @name Rectangle#contains
	 * @function
	 * @param {Rectangle} rect The specified rectangle
	 * @return {Boolean} {@true if the rectangle entirely contains the specified
	 *                   rectangle}
	 */
	bool contains(rect: Rectangle*)
		// Detect rectangles either by checking for 'width' on the passed object
		// or by looking at the amount of elements in the arguments list,
		// or the passed array:
		return @_containsRectangle(rect)

  bool contains(point: Point*)
		@_containsPoint(point)

	bool _containsPoint(point: Point*)
	  x, y: int
		x = point->x
	  y = point->y
		return x >= @x and y >= @y and x <= @x + @width and y <= @y + @height

	bool _containsRectangle(rect: Rectangle*)
	  x, y: int
		x = rect->x
		y = rect->y
		return x >= @x and y >= @y and x + rect->width <= @x + @width and y + rect->height <= @y + @height

	/**
	 * Tests if the interior of this rectangle intersects the interior of
	 * another rectangle. Rectangles just touching each other are considered
	 * as non-intersecting.
	 *
	 * @param {Rectangle} rect the specified rectangle
	 * @return {Boolean} {@true if the rectangle and the specified rectangle
	 *                   intersect each other}
	 */
	bool intersects(rect: Rectangle*)
		// rect = Rectangle.read(arguments)
		return rect.x + rect.width > @x and rect.y + rect.height > @y and rect.x < @x + @width 
					                          and rect.y < @y + @height

	bool touches(rect: Rectangle*) {
		// rect = Rectangle.read(arguments)
		return rect.x + rect.width >= @x and rect.y + rect.height >= @y and rect.x <= @x + @width
																	   and rect.y <= @y + @height

	/**
	 * {@grouptitle Boolean Operations}
	 *
	 * Returns a new rectangle representing the intersection of this rectangle
	 * with the specified rectangle.
	 *
	 * @param {Rectangle} rect The rectangle to be intersected with this
	 *                         rectangle
	 * @return {Rectangle} the largest rectangle contained in both the specified
	 *                     rectangle and in this rectangle
	 *
	 */
	Rectangle* intersect(rect: Rectangle*)
		// rect = Rectangle.read(arguments)
		x1, y1, x2, y2: int
	  x1 = max(@x, rect->x)
		y1 = max(@y, rect->y)
		x2 = min(@x + @width,  rect->x + rect->width)
		y2 = min(@y + @height, rect->y + rect->height)
		return new Rectangle(x1, y1, x2 - x1, y2 - y1)


	/**
	 * Returns a new rectangle representing the union of this rectangle with the
	 * specified rectangle.
	 *
	 * @param {Rectangle} rect the rectangle to be combined with this rectangle
	 * @return {Rectangle} the smallest rectangle containing both the specified
	 *                     rectangle and this rectangle.
	 */
	Rectangle* unite(rect: Rectangle*)
		// rect = Rectangle.read(arguments)
		x1, y1, x2, y2: int
		x1 = min(@x, rect->x)
		y1 = min(@y, rect->y)
		x2 = max(@x + @width,  rect->x + rect->width)
		y2 = max(@y + @height, rect->y + rect->height)

		return new Rectangle(x1, y1, x2 - x1, y2 - y1)

	/**
	 * Adds a point to this rectangle. The resulting rectangle is the
	 * smallest rectangle that contains both the original rectangle and the
	 * specified point.
	 *
	 * After adding a point, a call to {@link #contains(point)} with the added
	 * point as an argument does not necessarily return {@code true}.
	 * The {@link Rectangle#contains(point)} method does not return {@code true}
	 * for points on the right or bottom edges of a rectangle. Therefore, if the
	 * added point falls on the left or bottom edge of the enlarged rectangle,
	 * {@link Rectangle#contains(point)} returns {@code false} for that point.
	 *
	 * @param {Point} point
	 */
	Rectangle* include(point: Point*)
		// point = Point.read(arguments)
		x1, y1, x2, y2: int
		x1 = min(@x, point->x),
	  y1 = min(@y, point->y),
		x2 = max(@x + @width,  point->x)
		y2 = max(@y + @height, point->y)
		return new Rectangle(x1, y1, x2 - x1, y2 - y1)

	/**
	 * Expands the rectangle by the specified amount in both horizontal and
	 * vertical directions.
	 *
	 * @name Rectangle#expand
	 * @function
	 * @param {Number} amount
	 */
	/**
	 * Expands the rectangle in horizontal direction by the specified
	 * {@code hor} amount and in vertical direction by the specified {@code ver}
	 * amount.
	 *
	 * @name Rectangle#expand^2
	 * @function
	 * @param {Number} hor
	 * @param {Number} ver
	 */
	Rectangle* expand(hor: int, ver: int)
		if (ver === undefined)
			ver = hor

		return new Rectangle(@x - hor / 2, @y - ver / 2, @width + hor, @height + ver)

	Rectangle* expand(hor: int)
		ver := hor

		return new Rectangle(@x - hor / 2, @y - ver / 2, @width + hor, @height + ver)

	/**
	 * Scales the rectangle by the specified amount from its center.
	 *
	 * @name Rectangle#scale
	 * @function
	 * @param {Number} amount
	 */
	/**
	 * Scales the rectangle in horizontal direction by the specified
	 * {@code hor} amount and in vertical direction by the specified {@code ver}
	 * amount from its center.
	 *
	 * @name Rectangle#scale^2
	 * @function
	 * @param {Number} hor
	 * @param {Number} ver
	 */
	Rectangle* scale(hor: int, ver: int)
		return @expand(@width * hor - @width, @height * ver - @height)

	Rectangle* scale(hor: int)
	  var := hor
		return @expand(@width * hor - @width, @height * ver - @height)

return Base.each([
			['Top', 'Left'], ['Top', 'Right'],
			['Bottom', 'Left'], ['Bottom', 'Right'],
			['Left', 'Center'], ['Top', 'Center'],
			['Right', 'Center'], ['Bottom', 'Center']
		],
		function(parts, index) {
			var part = parts.join('')
			// find out if the first of the pair is an x or y property,
			// by checking the first character for [R]ight or [L]eft
			var xFirst = /^[RL]/.test(part)
			// Rename Center to CenterX or CenterY:
			if (index >= 4)
				parts[1] += xFirst ? 'Y' : 'X'
			var x = parts[xFirst ? 0 : 1],
				y = parts[xFirst ? 1 : 0],
				getX = 'get' + x,
				getY = 'get' + y,
				setX = 'set' + x,
				setY = 'set' + y,
				get = 'get' + part,
				set = 'set' + part
			this[get] = function(/* dontLink */) {
				return new (arguments[0] ? Point : LinkedPoint)
						(this[getX](), this[getY](), this, set)
			}
			this[set] = function(point) {
				point = Point.read(arguments)
				this[setX](point.x)
				this[setY](point.y)
			}
		}, {})