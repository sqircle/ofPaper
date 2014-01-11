struct decomposed
	translation: Point*
	scaling: Point*
	rotation: int
	shearing: int

/**
 * @name Matrix
 *
 * @class An affine transform performs a linear mapping from 2D coordinates
 * to other 2D coordinates that preserves the "straightness" and
 * "parallelness" of lines.
 *
 * Such a coordinate transformation can be represented by a 3 row by 3
 * column matrix with an implied last row of [ 0 0 1 ]. This matrix
 * transforms source coordinates (x,y) into destination coordinates (x',y')
 * by considering them to be a column vector and multiplying the coordinate
 * vector by the matrix according to the following process:
 * <pre>
 *      [ x ]   [ a  b  tx ] [ x ]   [ a * x + b * y + tx ]
 *      [ y ] = [ c  d  ty ] [ y ] = [ c * x + d * y + ty ]
 *      [ 1 ]   [ 0  0  1  ] [ 1 ]   [         1          ]
 * </pre>
 *
 * This class is optimized for speed and minimizes calculations based on its
 * knowledge of the underlying matrix (as opposed to say simply performing
 * matrix multiplication).
 */
class Matrix
	_a, _c, _b, _d, _tx, _ty: int

	/**
	 * Creates a 2D affine transform.
	 *
	 * @param {Number} a the scaleX coordinate of the transform
	 * @param {Number} c the shearY coordinate of the transform
	 * @param {Number} b the shearX coordinate of the transform
	 * @param {Number} d the scaleY coordinate of the transform
	 * @param {Number} tx the translateX coordinate of the transform
	 * @param {Number} ty the translateY coordinate of the transform
	 */
	Matrix* initMatrix(a: int, c: int, b: int, d: int, tx: int, ty: int)
		return @set(a, c, b, d, tx, ty)

  Matrix* initMatrix(arg: Matrix*)
		return @set(arg->_a, arg->_c, arg->_b, arg->_d, arg->_tx, arg->_ty)

	Matrix* initMatrix()
		return @reset()

	/**
	 * Sets this transform to the matrix specified by the 6 values.
	 *
	 * @param {Number} a the scaleX coordinate of the transform
	 * @param {Number} c the shearY coordinate of the transform
	 * @param {Number} b the shearX coordinate of the transform
	 * @param {Number} d the scaleY coordinate of the transform
	 * @param {Number} tx the translateX coordinate of the transform
	 * @param {Number} ty the translateY coordinate of the transform
	 * @return {Matrix} this affine transform
	 */
	Matrix* set(a: int, c: int, b: int, d: int, tx: int, ty: int)
		@_a  = a
		@_c  = c
		@_b  = b
		@_d  = d
		@_tx = tx
		@_ty = ty
		return @

	string _serialize()
		// return Base.serialize(@getValues(), options);

	/**
	 * @return {Matrix} a copy of this transform
	 */
	Matrix* clone()
		newMatrix: Matrix*
		newMatrix = new Matrix()

		return newMatrix->init(@_a, @_c, @_b, @_d, @_tx, @_ty)

	/**
	 * Checks whether the two matrices describe the same transformation.
	 *
	 * @param {Matrix} matrix the matrix to compare this matrix to
	 * @return {Boolean} {@true if the matrices are equal}
	 */
	bool equals(mx: Matrix*)
		return mx == @ or mx and @_a == mx->_a and @_b == mx->._b
				and @_c  == mx->_c and @_d == mx->_d and @_tx == mx->_tx
				and @_ty == mx->_ty
				or false

	/**
	 * @return {String} a string representation of this transform
	 */
	string toString()
		ret: string

		ret << "[[" 
		ret << [f.number(@_a), f.number(@_b), f.number(@_tx)].join(', ')
		ret << "], ["
		ret << [f.number(@_c), f.number(@_d), f.number(@_ty)].join(', ') 
		ret << "]]"

		return ret

	/**
	 * "Resets" the matrix by setting its values to the ones of the identity
	 * matrix that results in no transformation.
	 */
	Matrix* reset()
		@_a = @_d = 1;
		@_c = @_b = @_tx = @_ty = 0;
		return @

	/**
	 * Concatenates this transform with a scaling transformation.
	 *
	 * @name Matrix#scale
	 * @function
	 * @param {Number} scale the scaling factor
	 * @param {Point} [center] the center for the scaling transformation
	 * @return {Matrix} this affine transform
	 */
	/**
	 * Concatenates this transform with a scaling transformation.
	 *
	 * @name Matrix#scale
	 * @function
	 * @param {Number} hor the horizontal scaling factor
	 * @param {Number} ver the vertical scaling factor
	 * @param {Point} [center] the center for the scaling transformation
	 * @return {Matrix} this affine transform
	 */
	Matrix* scale(scale: Point*, center: Point*)
	  // scale = Point.read(arguments),
		// center = Point.read(arguments, 0, 0, { readNull: true });
		
		@translate(center)

		@_a *= scale->x
		@_c *= scale->x
		@_b *= scale->y
		@_d *= scale->y

		@translate(center->negate())

		return @

	Matrix* scale(scale: Point*)
	  // scale = Point.read(arguments),
		// center = Point.read(arguments, 0, 0, { readNull: true });
		
		@_a *= scale->x
		@_c *= scale->x
		@_b *= scale->y
		@_d *= scale->y

		return @

	/**
	 * Concatenates this transform with a translate transformation.
	 *
	 * @name Matrix#translate
	 * @function
	 * @param {Number} dx the distance to translate in the x direction
	 * @param {Number} dy the distance to translate in the y direction
	 * @return {Matrix} this affine transform
	 */
	Matrix* translate(point: Point*)
		// point = Point.read(arguments);
	  x, y: int
		x = point->x
		y = point->y

		@_tx += x * @_a + y * @_b
		@_ty += x * @_c + y * @_d

		return @

	/**
	 * Concatenates this transform with a rotation transformation around an
	 * anchor point.
	 *
	 * @name Matrix#rotate
	 * @function
	 * @param {Number} angle the angle of rotation measured in degrees
	 * @param {Number} x the x coordinate of the anchor point
	 * @param {Number} y the y coordinate of the anchor point
	 * @return {Matrix} this affine transform
	 */
	Matrix* rotate(angle: int, center: Point*)
		// center = Point.read(arguments, 1);
		x, y, cos, sin, tx, ty, a, b, c, d: int
		angle = angle * PI / 180

		// Concatenate rotation matrix into this one
		x   = center->x
		y   = center->y
		cos = cos(angle)
		sin = sin(angle)
		tx  = x - x * cos + y * sin
		ty  = y - x * sin - y * cos

		a = @_a
		b = @_b
		c = @_c
		d = @_d

		@_a   = cos * a + sin * b
		@_b   = -sin * a + cos * b
		@_c   = cos * c + sin * d
		@_d   = -sin * c + cos * d
		@_tx += tx * a + ty * b
		@_ty += tx * c + ty * d

		return @

	/**
	 * Concatenates this transform with a shear transformation.
	 *
	 * @name Matrix#shear
	 * @function
	 * @param {Number} hor the horizontal shear factor
	 * @param {Number} ver the vertical shear factor
	 * @param {Point} [center] the center for the shear transformation
	 * @return {Matrix} this affine transform
	 */
	Matrix* shear(point: Point*)
		// point  = Point.read(arguments),
		// center = Point.read(arguments, 0, 0, { readNull: true })
		a, c: int

    a = @_a
		c = @_c

		@_a += point->y * @_b
		@_c += point->y * @_d
		@_b += point->x * a
		@_d += point->x * c

		return @

	Matrix* shear(point: Point*, center: Point*)
		// point  = Point.read(arguments),
		// center = Point.read(arguments, 0, 0, { readNull: true })
		a, c: int

		if (center)
			@translate(center)

    a = @_a
		c = @_c

		@_a += point->y * @_b
		@_c += point->y * @_d
		@_b += point->x * a
		@_d += point->x * c

		if (center)
			@translate(center->negate());

		return @

	/**
	 * Concatenates an affine transform to this transform.
	 *
	 * @param {Matrix} mx the transform to concatenate
	 * @return {Matrix} this affine transform
	 */
	Matrix* concatenate(mx: Matrix*)
	  a, b, c, d: int
		a = @_a
		b = @_b
		c = @_c
		d = @_d

		@_a = mx->_a * a + mx->_c * b
		@_b = mx->_b * a + mx->_d * b
		@_c = mx->_a * c + mx->_c * d
		@_d = mx->_b * c + mx->_d * d

		@_tx += mx->_tx * a + mx->_ty * b;
		@_ty += mx->_tx * c + mx->_ty * d;

		return @

	/**
	 * Pre-concatenates an affine transform to this transform.
	 *
	 * @param {Matrix} mx the transform to preconcatenate
	 * @return {Matrix} this affine transform
	 */
	Matrix* preConcatenate(mx: Matrix*)
	  a, b, c, d, tx, ty: int

		a  = @_a
		b  = @_b
		c  = @_c
		d  = @_d
		tx = @_tx
		ty = @_ty

		@_a  = mx->_a * a + mx->_b * c
		@_b  = mx->_a * b + mx->_b * d
		@_c  = mx->_c * a + mx->_d * c
		@_d  = mx->_c * b + mx->_d * d

		@_tx = mx->_a * tx + mx->_b * ty + mx->_tx
		@_ty = mx->_c * tx + mx->_d * ty + mx->_ty

		return @

	/**
	 * @return {Boolean} whether this transform is the identity transform
	 */
	bool isIdentity()
		return @_a == 1 and @_c == 0 and @_b == 0 and @_d == 1 and @_tx == 0 and @_ty == 0

	/**
	 * Returns whether the transform is invertible. A transform is not
	 * invertible if the determinant is 0 or any value is non-finite or NaN.
	 *
	 * @return {Boolean} whether the transform is invertible
	 */
	bool isInvertible()
		return !!@_getDeterminant()

	/**
	 * Checks whether the matrix is singular or not. Singular matrices cannot be
	 * inverted.
	 *
	 * @return {Boolean} whether the matrix is singular
	 */
	bool isSingular()
		return !@_getDeterminant()

	/**
	 * Transforms a point and returns the result.
	 *
	 * @name Matrix#transform
	 * @function
	 * @param {Point} point the point to be transformed
	 * @return {Point} the transformed point
	 */
	Point* transform(point: Point*)
		return @_transformPoint(point)

	/**
	 * Transforms an array of coordinates by this matrix and stores the results
	 * into the destination array, which is also returned.
	 *
	 * @name Matrix#transform
	 * @function
	 * @param {Number[]} src the array containing the source points
	 *        as x, y value pairs
	 * @param {Number} srcOff the offset to the first point to be transformed
	 * @param {Number[]} dst the array into which to store the transformed
	 *        point pairs
	 * @param {Number} dstOff the offset of the location of the first
	 *        transformed point in the destination array
	 * @param {Number} numPts the number of points to tranform
	 * @return {Number[]} the dst array, containing the transformed coordinates.
	 */
	int transform(src: int, srcOff: int, dst: int, dstOff: int, numPts: int)
		return @_transformCoordinates(src, srcOff, dst, dstOff, numPts)


	Point* _transformPoint(point: Point*, dest: Point*)
		return @_transformPoint(point, new Point(), false)

	Point* _transformPoint(point: Point*, dest: Point*)
		return @_transformPoint(point, dest, false)

	/**
	 * A faster version of transform that only takes one point and does not
	 * attempt to convert it.
	 */
	Point* _transformPoint(point: Point*, dest: Point*, dontNotify: bool)
		x = point->x
	  y = point->y

		return dest->set(x * @_a + y * @_b + @_tx, x * @_c + y * @_d + @_ty. dontNotify)
			
	vector<int> _transformCoordinates(src: vector<int>, srcOff: int, dst: vector<int>, dstOff: int, numPts: int)
	  i, j, srcEnd: int

		i = srcOff 
		j = dstOff
		srcEnd = srcOff + 2 * numPts

		while (i < srcEnd)
			x, y: int
			x = src[i++]
		  y = src[i++]

			dst[j++] = x * @_a + y * @_b + @_tx
			dst[j++] = x * @_c + y * @_d + @_ty

		return dst

	vector<int> _transformCorners(rect: Rectangle*)
	  x1, y1, x2, y2: int

		x1 = rect->x
		y1 = rect->y
		x2 = x1 + rect->width
		y2 = y1 + rect->height

		coords := [x1, y1, x2, y1, x2, y2, x1, y2] : vector<int>

		return @_transformCoordinates(coords, 0, coords, 0, 4)

	/**
	 * Returns the 'transformed' bounds rectangle by transforming each corner
	 * point and finding the new bounding box to these points. This is not
	 * really the transformed reactangle!
	 */
	Point* _transformBounds(bounds: Rectangle*)
		return @_transformBounds(bounds, new Rectangle(), false)

	Point* _transformBounds(bounds: Rectangle*, dest: Rectangle*)
		return @_transformBounds(bounds, dest, false)

	Point* _transformBounds(bounds: Rectangle*, dest: Rectangle*, dontNotify: bool)
	  dest: Rectangle*
		coords, min, max: vector<int>

	  coords = @_transformCorners(bounds)
		min = min(coords.begin(), coords.begin() + 2)
		max = max(coords.begin(), coords.end())

		for(i = 2; i < 8; i++)
			val, j: int
			val = coords[i]
			j = i & 1

			if val < min[j]
				min[j] = val
			else if val > max[j]
				max[j] = val

		return dest->set(min[0], min[1], max[0] - min[0], max[1] - min[1], dontNotify)

	/**
	 * Inverse transforms a point and returns the result.
	 *
	 * @param {Point} point the point to be transformed
	 */
	Point* inverseTransform(point: Point*)
		return @_inverseTransform(point)
		// return @_inverseTransform(Point.read(arguments));

	/**
	 * Returns the determinant of this transform, but only if the matrix is
	 * reversible, null otherwise.
	 */
	int _getDeterminant()
	  det: int*
		*det = @_a * @_d - @_b * @_c

		return @isFinite(*det) and !Numerical::isZero(*det) and @isFinite(@_tx) and @isFinite(@_ty) ? det : NULL
  
  Point* _inverseTransform(point: Point*)
	  @_inverseTransform(point, new Point(), false)

  Point* _inverseTransform(point: Point*, dest: Point*)
	  @_inverseTransform(point, dest, false)

	Point* _inverseTransform(point: Point*, dest: Point*, dontNotify: bool)
	  x, y, det: int
		det = @_getDeterminant()
		if !det
			return NULL

	  x = point->x - @_tx
	  y = point->y - @_ty

		return dest.set((x * @_d - y * @_b) / det, (y * @_a - x * @_c) / det, dontNotify)

	/**
	 * Attempts to decompose the affine transformation described by this matrix
	 * into {@code translation}, {@code scaling}, {@code rotation} and
	 * {@code shearing}, and returns an object with these properties if it
	 * succeeded, {@code null} otherwise.
	 *
	 * @return {Object} the decomposed matrix, or {@code null} if decomposition
	 * is not possible.
	 */
	decomposed* decompose()
	  a, b, c, d, scaleX, scaleY, shear: int
	  obj: decomposed*

		a = @_a, b = @_b, c = @_c, d = @_d

		if (a * d - b * c) == 0
			return NULL

		scaleX = sqrt(a * a + b * b)
		a /= scaleX;
		b /= scaleX;

		shear = a * c + b * d
		c -= a * shear
		d -= b * shear

		scaleY = sqrt(c * c + d * d);
		c /= scaleY
		d /= scaleY
		shear /= scaleY

		// a * d - b * c should now be 1 or -1
		if a * d < b * c
			a = -a
			b = -b

			shear  = -shear
			scaleX = -scaleX

		obj->translation = @getTranslation()
		obj->scaling     = new Point(scaleX, scaleY)
		obj->rotation    = -atan2(b, a) * 180 / PI
		obj->shearing    = shear

		return obj

	/**
	 * The scaling factor in the x-direction ({@code a}).
	 *
	 * @name Matrix#scaleX
	 * @type Number
	 */
	int getScaleX()
		return @_a

	Matrix* setScaleX(scaleX: int)
	  @_a = scaleX
		return @

	/**
	 * The scaling factor in the y-direction ({@code d}).
	 *
	 * @name Matrix#scaleY
	 * @type Number
	 */
	int getScaleY()
		return @_d

	Matrix* setScaleY(scaleY: int)
	  @_d = scaleY
		return @

	/**
	 * The shear factor in the x-direction ({@code b}).
	 *
	 * @name Matrix#shearX
	 * @type Number
	 */
 int getShearX()
		return @_b

	Matrix* setShearX(shearX: int)
	  @_b = shearX
		return @

	/**
	 * The shear factor in the y-direction ({@code c}).
	 *
	 * @name Matrix#shearY
	 * @type Number
	 */
	int getShearY()
		return @_c

	Matrix* setShearY(shearY: int)
	  @_c = shearY
		return @

	/**
	 * The translation in the x-direction ({@code tx}).
	 *
	 * @name Matrix#translateX
	 * @type Number
	 */
	int getTranslateX()
		return @_tx

	Matrix* setTranslateX(translateX: int)
	  @_tx = translateX
		return @

	/**
	 * The translation in the y-direction ({@code ty}).
	 *
	 * @name Matrix#translateY
	 * @type Number
	 */
	int getTranslateY()
		return @_ty

	Matrix* setTranslateY(translateY: int)
	  @_ty = translateY
		return @
		
	/**
	 * The transform values as an array, in the same sequence as they are passed
	 * to {@link #initialize(a, c, b, d, tx, ty)}.
	 *
	 * @type Number[]
	 * @bean
	 */
	int[5] getValues()
		return {@_a, @_c, @_b, @_d, @_tx, @_ty}

	/**
	 * The translation values of the matrix.
	 *
	 * @type Point
	 * @bean
	 */
	Point* getTranslation()
		// No decomposition is required to extract translation, so treat this
		return new Point(@_tx, @_ty)

	/**
	 * The scaling values of the matrix, if it can be decomposed.
	 *
	 * @type Point
	 * @bean
	 * @see Matrix#decompose()
	 */
	decomposed* getScaling()
	  obj: decomposed*
	  obj = @decompose()

	  if obj == NULL
	  	obj = new decomposed

		return obj->scaling

	/**
	 * The rotation angle of the matrix, if it can be decomposed.
	 *
	 * @type Number
	 * @bean
	 * @see Matrix#decompose()
	 */
	decomposed* getRotation()
		obj: decomposed*
	  obj = @decompose()

	  if obj == NULL
	  	obj = new decomposed

		return obj->rotation

	/**
	 * Inverts the transformation of the matrix. If the matrix is not invertible
	 * (in which case {@link #isSingular()} returns true), {@code null } is
	 * returned.
	 *
	 * @return {Matrix} the inverted matrix, or {@code null }, if the matrix is
	 *         singular
	 */
	Matrix* inverted()
		det: int
		det = @_getDeterminant()

		if det == NULL
			return NULL

		return new Matrix(@_d   / det,
											-@_c  / det,
											-@_b  / det,
											@_a   / det,
											(@_b * @_ty - @_d * @_tx) / det,
											(@_c * @_tx - @_a * @_ty) / det)


	Matrix* shiftless()
		return new Matrix(@_a, @_c, @_b, @_d, 0, 0)

	/**
	 * Applies this matrix to the specified Canvas Context.
	 *
	 * @param {CanvasRenderingContext2D} ctx
	 */
	int applyToContext(ctx: CanvasRenderingContext2D*)
		ctx->transform(@_a, @_c, @_b, @_d, @_tx, @_ty)
