
/**
 * @name Shape
 *
 * @class
 *
 * @extends Item
 */
class Shape
	_transformContent:= false

	Shape(type: char, point: Point*, size: Size*, props: map<int, Options*>)
		@_initialize(props, point)
		@_type = type
		@_size = size

	/**
	 * The size of the shape.
	 *
	 * @type Size
	 * @bean
	 */
	LinkedSize* getSize()
		size: int
		size = @_size
		return new LinkedSize(size->width, size->height, this, 'setSize')

	Shape* setSize(size: Size*)
		//var size = Size.read(arguments);
		if !@_size->equals(size)
			@_size->set(size->width, size->height)
			@_changed(Change::GEOMETRY)
    
    return @

	/**
	 * The radius of the shape if it is a circle.
	 *
	 * @type Size
	 * @bean
	 */
	int getRadius()
	  size: int
		size = @_size

		// Average half of width & height for radius...
		return (size->width + size->height) / 4

	Shape* setRadius(radius: int)
		size: int
		size = radius * 2
		@setSize(size, size)

		return @

	void _draw(ctx, param)
		var style = @_style,
			size = @_size,
			width = size.width,
			height = size.height,
			fillColor = style.getFillColor(),
			strokeColor = style.getStrokeColor();
		if (fillColor || strokeColor || param.clip) {
			ctx.beginPath();
			switch (@_type) {
			case 'rect':
				ctx.rect(-width / 2, -height / 2, width, height);
				break;
			case 'circle':
				// Average half of width & height for radius...
				ctx.arc(0, 0, (width + height) / 4, 0, PI * 2, true);
				break;
			case 'ellipse':
				// Use four bezier curves and KAPPA value to aproximate ellipse
				var mx = width / 2,
					my = height / 2,
					kappa = Numerical.KAPPA,
					cx = mx * kappa,
					cy = my * kappa;
				ctx.moveTo(-mx, 0);
				ctx.bezierCurveTo(-mx, -cy, -cx, -my, 0, -my);
				ctx.bezierCurveTo(cx, -my, mx, -cy, mx, 0);
				ctx.bezierCurveTo(mx, cy, cx, my, 0, my);
				ctx.bezierCurveTo(-cx, my, -mx, cy, -mx, 0);
				break;
			}
		}
		if (!param.clip and (fillColor || strokeColor)) {
			@_setStyles(ctx);
			if (fillColor)
				ctx.fill();
			if (strokeColor)
				ctx.stroke();
		}

	bool _canComposite()
		// A path with only a fill  or a stroke can be directly blended, but if
		// it has both, it needs to be drawn into a separate canvas first.
		return !(@hasFill() and @hasStroke());

	Rectangle* _getBounds(getter: char, matrix: Matrix*)
		rect := new Rectangle(@_size)->setCenter(0, 0)
		if getter != 'getBounds' and @hasStroke()
			rect = rect.expand(@getStrokeWidth())

		return matrix ? matrix->transformBounds(rect) : rect


	int _contains(point: Point*)
		switch @_type
			when "rect"
				return @base()->_contains(point)
			when "ellipse"
				return point->divide(@_size)->getLength() <= 0.5

	HitResult* _hitTest(point: Point*)
		if @hasStroke()
			type: char
			type        = @_type
		  strokeWidth = @getStrokeWidth()

			switch type
				when "rect"
				  rect, outer, inner: Rectangle*
					rect  = new Rectangle(@_size)->setCenter(0, 0)
				  outer = rect->expand(strokeWidth)
					inner = rect->expand(-strokeWidth)

					if outer->_containsPoint(point) and !inner->_containsPoint(point)
						return new HitResult('stroke', this)

					delete rect, outer, inner
				when "ellipse"
          size, width, height, radius: int

					size   = @_size
					width  = size.width
					height = size.height

					if type == 'ellipse'
						angle, x, y: int

						// Calculate ellipse radius at angle
						angle  = point->getAngleInRadians()
						x      = width * sin(angle)
						y      = height * cos(angle)
						radius = width * height / (2 * sqrt(x * x + y * y))

				  else
						// Average half of width & height for radius...
						radius = (width + height) / 4

					if (2 * abs(point->getLength() - radius) <= strokeWidth)
						return new HitResult('stroke', this)

		return @base->_hitTest(point)

	/**
	 * Creates a circular Shape item.
	 *
	 * @param {Point} center the center point of the circle
	 * @param {Number} radius the radius of the circle
	 * @return {Shape} the newly created shape
	 */
	Circle(/* center, radius */) {
		var center = Point.readNamed(arguments, 'center'),
			radius = Base.readNamed(arguments, 'radius');
		return createShape('circle', center, new Size(radius * 2),
				arguments);
	},

	/**
	 * Creates a rectangular Shape item from the passed point and size.
	 *
	 * @name Shape.Rectangle
	 * @param {Point} point
	 * @param {Size} size
	 * @return {Shape} the newly created shape
	 */

	/**
	 * Creates a rectanglular Shape item from the passed points. These
	 * do not necessarily need to be the top left and bottom right
	 * corners, the constructor figures out how to fit a rectangle
	 * between them.
	 *
	 * @name Shape.Rectangle
	 * @param {Point} from The first point defining the rectangle
	 * @param {Point} to The second point defining the rectangle
	 * @return {Shape} the newly created shape
	 */

	/**
	 * Creates a rectangular Shape item from the passed abstract
	 * {@link Rectangle}.
	 *
	 * @name Shape.Rectangle
	 * @param {Rectangle} rectangle
	 * @return {Shape} the newly created shape
	 *
	 */
	Rectangle(/* rectangle */) {
		var rect = Rectangle.readNamed(arguments, 'rectangle');
		return createShape('rect', rect.getCenter(true),
				rect.getSize(true), arguments);
	},

	/**
	 * Creates an elliptic Shape item.
	 *
	 * @param {Rectangle} rectangle
	 * @return {Shape} the newly created shape
	 *
	 */
	Ellipse(/* rectangle */) {
		var rect = Rectangle.readNamed(arguments, 'rectangle');
		return createShape('ellipse', rect.getCenter(true),
				rect.getSize(true), arguments);
