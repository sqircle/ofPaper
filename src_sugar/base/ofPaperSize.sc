/**
 * @name Size
 *
 * @class The Size object is used to describe the size or dimensions of
 * somethign, through its {@link #width} and {@link #height} properties.
 *
 */
class Size
	// Tell Base.read that the Point constructor supports reading with index
	[public]
	_readIndex: true
	width, height: int

	Size()
		@width = @height = 0
		if @__read
			@__read = 1 

	Size(width: int)
		@width  = width
		@height = 0
		// if (@__read)
		// 	@__read = hasHeight ? 2 : 1


	/**
	 * Creates a Size object with the given width and height values.
	 *
	 * @name Size#initialize
	 * @param {Number} width the width
	 * @param {Number} height the height
	 */
	Size(width: int, height: int)
		@width  = width
		@height = height

	/**
	 * Creates a Size object using the numbers in the given array as
	 * dimensions.
	 *
	 * @name Size#initialize
	 * @param {Array} array
	 */	
	Size(vect: vector<int>)
		@width  = vect[0]
		@height = vect[1]

	/**
	 * Creates a Size object using the coordinates of the given Size object.
	 *
	 * @name Size#initialize
	 * @param {Size} size
	 */
	Size(size: Size*)
		@width  = size->width
		@height = size->height

	/**
	 * Creates a Size object using the {@link Point#x} and {@link Point#y}
	 * values of the given Point object.
	 *
	 * @name Size#initialize
	 * @param {Point} point
	 */
  Size(point: Point*)
  	@width  = point->width
		@height = point->height

	/**
	 * The width of the size
	 *
	 * @name Size#width
	 * @type Number
	 */

	/**
	 * The height of the size
	 *
	 * @name Size#height
	 * @type Number
	 */
	Size* set(width: int, height: int)
		@width  = width
		@height = height
		return @

	/**
	 * Checks whether the width and height of the size are equal to those of the
	 * supplied size.
	 *
	 * @param {Size}
	 * @return {Boolean}
	 */
	bool equals(size: Size*)
		return size == this or size and (@width == size->width
				and @height == size->height) or false

	bool equals(size: vector<int>)
		 return (@width == size[0] and @height == size[1]) or false

	/**
	 * Returns a copy of the size.
	 */
	Size* clone(
		return new Size(@width, @height)

	/**
	 * @return {String} a string representation of the size
	 */
	string toString()
		ret := "{ width: "
	  ret << @width
		ret	<< ", height: " 
		ret	<< @height + " } "

		return ret

	vector<int,2> _serialize()
		return {@width, @height}

	/**
	 * Returns the addition of the width and height of the supplied size to the
	 * size as a new size. The object itself is not modified!
	 *
	 * @name Size#add
	 * @function
	 * @operator
	 * @param {Size} size the size to add
	 * @return {Size} the addition of the two sizes as a new size
	 */
	Size* add(size: Size*)
		//size = Size.read(arguments)
		return new Size(@width + size->width, @height + size->height)

	/**
	 * Returns the subtraction of the width and height of the supplied size from
	 * the size as a new size. The object itself is not modified!
	 *
	 * @name Size#subtract
	 * @function
	 * @operator
	 * @param {Size} size the size to subtract
	 * @return {Size} the subtraction of the two sizes as a new size
	 */
	Size* subtract(size: Size*)
		// size = Size.read(arguments)
		return new Size(@width - size->width, @height - size->height)

	/**
	 * Returns the multiplication of the width and height of the supplied size
	 * with the size as a new size. The object itself is not modified!
	 *
	 * @name Size#multiply
	 * @function
	 * @operator
	 * @param {Size} size the size to multiply by
	 * @return {Size} the multiplication of the two sizes as a new size
	 */
	Size* multiply(size: Size*)
		// size = Size.read(arguments)
		return new Size(@width * size->width, @height * size->height)

	/**
	 * Returns the division of the width and height of the supplied size by the
	 * size as a new size. The object itself is not modified!
	 *
	 * @name Size#divide
	 * @function
	 * @operator
	 * @param {Size} size the size to divide by
	 * @return {Size} the division of the two sizes as a new size
	 */
	Size* divide(size: Size*)
		// size = Size.read(arguments)
		return new Size(@width / size->width, @height / size->height)

	/**
	 * The modulo operator returns the integer remainders of dividing the size
	 * by the supplied size as a new size.
	 *
	 * @name Size#modulo
	 * @function
	 * @operator
	 * @param {Size} size
	 * @return {Size} the integer remainders of dividing the sizes by each
	 *                 other as a new size
	 */
	Size* modulo(size: Size*)
		// size = Size.read(arguments)
		return new Size(@width % size->width, @height % size->height)

	Size* negate()
		return new Size(-@width, -@height)

	/**
	 * {@grouptitle Tests}
	 * Checks if this size has both the width and height set to 0.
	 *
	 * @return {Boolean} {@true both width and height are 0}
	 */
	bool isZero()
		return Numerical::isZero(@width) and Numerical::isZero(@height)

	/**
	 * Checks if the width or the height of the size are NaN.
	 *
	 * @return {Boolean} {@true if the width or height of the size are NaN}
	 */
	bool isNaN()
		return @width == NULL or @height == NULL

	/**
	 * {@grouptitle Math Functions}
	 *
	 * Returns a new size with rounded {@link #width} and {@link #height}
	 * values. The object itself is not modified!
	 *
	 * @name Size#round
	 * @function
	 * @return {Size}
	 */
	Size* round()
		return new Size(round(@width), round(@height))

	/**
	 * Returns a new size with the nearest greater non-fractional values to the
	 * specified {@link #width} and {@link #height} values. The object itself is
	 * not modified!
	 *
	 * @name Size#ceil
	 * @function
	 * @return {Size}
	 */
	Size* ceil()
		return new Size(ceil(@width), ceil(@height))

	/**
	 * Returns a new size with the nearest smaller non-fractional values to the
	 * specified {@link #width} and {@link #height} values. The object itself is
	 * not modified!
	 *
	 * @name Size#floor
	 * @function
	 * @return {Size}
	 */
	Size* floor()
		return new Size(floor(@width), floor(@height))

	/**
	 * Returns a new size with the absolute values of the specified
	 * {@link #width} and {@link #height} values. The object itself is not
	 * modified!
	 *
	 * @name Size#abs
	 * @function
	 * @return {Size}
	 */
	Size* abs()
		return new Size(abs(@width), abs(@height))

	/**
	 * Returns a new size object with the smallest {@link #width} and
	 * {@link #height} of the supplied sizes.
	 *
	 * @static
	 * @param {Size} size1
	 * @param {Size} size2
	 * @returns {Size} the newly created size object
	 */
	static Size* min(size1: Size*, size2: Size*)
		return new Size(min(size1->width, size2->width), min(size1->height, size2->height))

	/**
	 * Returns a new size object with the largest {@link #width} and
	 * {@link #height} of the supplied sizes.
	 *
	 * @static
	 * @param {Size} size1
	 * @param {Size} size2
	 * @returns {Size} the newly created size object
	 */
	static Size* max(size1: Size*, size2: Size*) {
		return new Size(max(size1->width,  size2->width), max(size1->height, size2->height))

	/**
	 * Returns a size object with random {@link #width} and {@link #height}
	 * values between {@code 0} and {@code 1}.
	 *
	 * @returns {Size} the newly created size object
	 * @static
	 */
	static Size* random()
		return new Size(random(), random())
