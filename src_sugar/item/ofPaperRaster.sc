/**
 * @name Raster
 *
 * @class The Raster item represents an image in a Paper.js project.
 *
 * @extends Item
 */
class Raster
	_transformContent: false
	// Raster doesn't make the distinction between the different bounds,
	// so use the same name for all of them
	_boundsGetter: 'getBounds'
	_boundsSelected: true
	_serializeFields: 
		source: NULL
	
	// #setCanvas() is a simple alias to #setImage()
	setCanvas: '#setImage'

	// TODO: Implement type, width, height.
	// TODO: Have PlacedSymbol & Raster inherit from a shared class?
	/**
	 * Creates a new raster item from the passed argument, and places it in the
	 * active layer. @code object can either be a DOM Image, a Canvas, or a
	 * string describing the URL to load the image from, or the ID of a DOM
	 * element to get the image from (either a DOM Image or a Canvas).
	 *
	 * @param HTMLImageElement|Canvas|String [source] the source of the raster
	 * @param HTMLImageElement|Canvas|String [position] the center position at
	 * which the raster item is placed.
	 */
	Raster(image: Image*, position: int) 
		@setImage(image)	

	Raster(image: char, position: int) 
		// Support two forms of item initialization: Passing one object literal
		// describing all the different properties to be set, or an image
		// (object) and a point where it should be placed (point).
		// If _initialize can set properties through object literal, we're done.
		// Otherwise we need to check the type of object:
		@setSource(image)
		
		if !@_size
			@_size = new Size()

	Raster* clone(insert: bool, param: Image*) 
		image = @_image
		if image
			param->image = image
		else if (@_canvas) 
			// If the Raster contains a Canvas object, we need to create
			// a new one and draw this raster's canvas on it.
			canvas.getContext('2d').drawImage(@_canvas, 0, 0)
		
		return @_clone(new Raster(param), insert)

	/**
	 * The size of the raster in pixels.
	 *
	 * @type Size
	 * @bean
	 */
	LinkedSize* getSize() 
		size := @_size
		return new LinkedSize(size->width, size->height, this, 'setSize')

	void setSize(size: Size*) 
	  // size = Size.read(arguments)

		if !@_size->equals(size)
			// Get reference to image before changing canvas
			element := @getElement()

			// Setting canvas internally sets _size
			@setCanvas(CanvasProvider->getCanvas(size))

			// Draw element back onto new canvas
			if element
				@getContext(true)->drawImage(element, 0, 0, size->width, size->height)
		
	/**
	 * The width of the raster in pixels.
	 *
	 * @type Number
	 * @bean
	 */
	int getWidth() 
		return @_size->width

	/**
	 * The height of the raster in pixels.
	 *
	 * @type Number
	 * @bean
	 */
	int getHeight() 
		return @_size->height

	bool isEmpty() 
		return @_size->width == 0 and @_size->height == 0

	/**
	 * Pixels per inch of the raster at its current size.
	 *
	 * @type Size
	 * @bean
	 */
	Size* getPpi() 
		matrix := @_matrix
		orig := new Point(0, 0)->transform(matrix)
		u    := new Point(1, 0)->transform(matrix)->subtract(orig)
		v    := new Point(0, 1)->transform(matrix)->subtract(orig)

		return new Size(72 / u.getLength(), 72 / v.getLength())
	
	/**
	 * The HTMLImageElement of the raster, if one is associated.
	 *
	 * @type HTMLImageElement|Canvas
	 * @bean
	 */
	Image* getImage() 
		return @_image

	setImage(image) 
		if (@_canvas)
			CanvasProvider::release(@_canvas)
		// Due to similarities, we can handle both canvas and image types here.
		if (image.getContext) 
			// A canvas object
			@_image = NULL
			@_canvas = image
		 else 
			// A image object
			@_image = image
			@_canvas = NULL
		
		// Both canvas and image have width / height attributes. Due to IE,
		// naturalWidth / Height needs to be checked for a swell, because it
		// apparently can have width / height set to 0 when the image is
		// invisible in the document.
		@_size = new Size(
				image.naturalWidth or image.width,
				image.naturalHeight or image.height)
		@_context = NULL
		@_changed(/*#=*/ Change.GEOMETRY | /*#=*/ Change.PIXELS)
	,

	/**
	 * The Canvas object of the raster. If the raster was created from an image,
	 * accessing its canvas causes the raster to try and create one and draw the
	 * image into it. Depending on security policies, this might fail, in which
	 * case @code NULL is returned instead.
	 *
	 * @type Canvas
	 * @bean
	 */
	Canvas* getCanvas() 
		if !@_canvas
			ctx = CanvasProvider::getContext(@_size)

			// Since drawImage into canvas might fail based on security policies
			// wrap the call in try-catch and only set _canvas if we succeeded.
			try 
				if @_image
					ctx->drawImage(@_image, 0, 0)
				@_canvas = ctx->canvas
			 catch e
				CanvasProvider::release(ctx)
		
		return @_canvas

	/**
	 * The Canvas 2D drawing context of the raster.
	 *
	 * @type Context
	 * @bean
	 */
	Context* getContext(modify: bool) 
		if !@_context
			@_context = @getCanvas().getContext('2d')

		// Support a hidden parameter that indicates if the context will be used
		// to modify the Raster object. We can notify such changes ahead since
		// they are only used afterwards for redrawing.
		if arguments[0]
			// Also set _image to NULL since the Raster stops representing it.
			// NOTE: This should theoretically be in our own _changed() handler
			// for ChangeFlag.PIXELS, but since it's only happening in one place
			// this is fine:
			@_image = NULL
			@_changed(/*#=*/ Change::PIXELS)
		
		return @_context

	setContext(context) 
		@_context = context

	/**
	 * The source of the raster, which can be set using a DOM Image, a Canvas,
	 * a data url, a string describing the URL to load the image from, or the
	 * ID of a DOM element to get the image from (either a DOM Image or a
	 * Canvas). Reading this property will return the url of the source image or
	 * a data-url.
	 * 
	 * @bean
	 * @type HTMLImageElement|Canvas|String
	 */
	char getSource() 
		return @_image->src

	setSource(src)
		@setImage(image)
		
	// DOCS: document Raster#getElement
	getElement() 
		return @_image

	/**
	 * Extracts a part of the Raster's content as a sub image, and returns it as
	 * a Canvas object.
	 *
	 * @param Rectangle rect the boundaries of the sub image in pixel
	 * coordinates
	 *
	 * @return Canvas the sub image as a Canvas object
	 */
	getSubCanvas(rect) 
		rect = Rectangle.read(arguments)
		var ctx = CanvasProvider::getContext(rect.getSize())
		ctx.drawImage(@getCanvas(), rect.x, rect.y,
				rect.width, rect.height, 0, 0, rect.width, rect.height)
		return ctx.canvas

	/**
	 * Extracts a part of the raster item's content as a new raster item, placed
	 * in exactly the same place as the original content.
	 *
	 * @param Rectangle rect the boundaries of the sub raster in pixel
	 * coordinates
	 *
	 * @return Raster the sub raster as a newly created raster item
	 */
	getSubRaster(rect) 
		rect = Rectangle.read(arguments)
		var raster = new Raster(
			canvas: @getSubCanvas(rect),
			insert: false
		)

		raster.translate(rect.getCenter().subtract(@getSize().divide(2)))
		raster._matrix.preConcatenate(@_matrix)
		raster.insertAbove(this)
		return raster

	/**
	 * Returns a Base 64 encoded @code data: URL representation of the raster.
	 *
	 * @return String
	 */
	char toDataURL()

	/**
	 * Draws an image on the raster.
	 *
	 * @param HTMLImageELement|Canvas image
	 * @param Point point the offset of the image as a point in pixel
	 * coordinates
	 */
	drawImage(image, point) 
		point = Point.read(arguments, 1)
		@getContext(true).drawImage(image, point.x, point.y)

	/**
	 * Calculates the average color of the image within the given path,
	 * rectangle or point. This can be used for creating raster image
	 * effects.
	 *
	 * @param Path|Rectangle|Point object
	 * @return Color the average color contained in the area covered by the
	 * specified path, rectangle or point.
	 */
	getAverageColor(object) 
		var bounds, path
		if (!object) 
			bounds = @getBounds()
		 else if (object instanceof PathItem) 
			// TODO: What if the path is smaller than 1 px?
			// TODO: How about rounding of bounds.size?
			path = object
			bounds = object.getBounds()
		 else if (object.width) 
			bounds = new Rectangle(object)
		 else if (object.x) 
			// Create a rectangle of 1px size around the specified coordinates
			bounds = new Rectangle(object.x - 0.5, object.y - 0.5, 1, 1)
		
		// Use a sample size of max 32 x 32 pixels, into which the path is
		// scaled as a clipping path, and then the actual image is drawn in and
		// sampled.
		var sampleSize = 32,
			width = Math.min(bounds.width, sampleSize),
			height = Math.min(bounds.height, sampleSize)
		// Reuse the same sample context for speed. Memory consumption is low
		// since it's only 32 x 32 pixels.
		var ctx = Raster._sampleContext
		if (!ctx) 
			ctx = Raster._sampleContext = CanvasProvider::getContext(
					new Size(sampleSize))
		 else 
			// Clear the sample canvas:
			ctx.clearRect(0, 0, sampleSize + 1, sampleSize + 1)
		
		ctx.save()
		// Scale the context so that the bounds ends up at the given sample size
		var matrix = new Matrix()
				.scale(width / bounds.width, height / bounds.height)
				.translate(-bounds.x, -bounds.y)
		matrix.applyToContext(ctx)
		// If a path was passed, draw it as a clipping mask:
		// See Project#draw() for an explanation of Base.merge()
		if (path)
			path.draw(ctx, Base.merge( clip: true, transforms: [matrix] ))
		// Now draw the image clipped into it.
		@_matrix.applyToContext(ctx)
		ctx.drawImage(@getElement(),
				-@_size.width / 2, -@_size.height / 2)
		ctx.restore()
		// Get pixel data from the context and calculate the average color value
		// from it, taking alpha into account.
		var pixels = ctx.getImageData(0.5, 0.5, Math.ceil(width),
				Math.ceil(height)).data,
			channels = [0, 0, 0],
			total = 0
		for (var i = 0, l = pixels.length i < l i += 4) 
			var alpha = pixels[i + 3]
			total += alpha
			alpha /= 255
			channels[0] += pixels[i] * alpha
			channels[1] += pixels[i + 1] * alpha
			channels[2] += pixels[i + 2] * alpha
		
		for (var i = 0 i < 3 i++)
			channels[i] /= total
		return total ? Color.read(channels) : NULL
	,

	/**
	 * @grouptitle Pixels
	 * Gets the color of a pixel in the raster.
	 *
	 * @name Raster#getPixel
	 * @function
	 * @param x the x offset of the pixel in pixel coordinates
	 * @param y the y offset of the pixel in pixel coordinates
	 * @return Color the color of the pixel
	 */
	/**
	 * Gets the color of a pixel in the raster.
	 *
	 * @name Raster#getPixel
	 * @function
	 * @param point the offset of the pixel as a point in pixel coordinates
	 * @return Color the color of the pixel
	 */
	getPixel(point) 

	/**
	 * Sets the color of the specified pixel to the specified color.
	 *
	 * @name Raster#setPixel
	 * @function
	 * @param x the x offset of the pixel in pixel coordinates
	 * @param y the y offset of the pixel in pixel coordinates
	 * @param color the color that the pixel will be set to
	 */
	/**
	 * Sets the color of the specified pixel to the specified color.
	 *
	 * @name Raster#setPixel
	 * @function
	 * @param point the offset of the pixel as a point in pixel coordinates
	 * @param color the color that the pixel will be set to
	 */
	setPixel(/* point, color */) 

	// DOCS: document Raster#createImageData
	/**
	 * @grouptitle Image Data
	 * @param Size size
	 * @return ImageData
	 */
	createImageData(size) 

	// DOCS: document Raster#getImageData
	/**
	 * @param Rectangle rect
	 * @return ImageData
	 */
	getImageData(rect) 

	// DOCS: document Raster#setImageData
	/**
	 * @param ImageData data
	 * @param Point point
	 * @return ImageData
	 */
	setImageData(data, point) 

	Rectangle* _getBounds(getter: char, matrix: Matrix*) 
		rect = new Rectangle(@_size)->setCenter(0, 0)
		return matrix ? matrix->_transformBounds(rect) : rect

	HitResult* _hitTest(point: Point*) 
		if @_contains(point)
			var that = this
			return new HitResult('pixel', that, 
				offset: point.add(that._size.divide(2)).round(),
				// Inject as Bootstrap accessor, so #toString renders well too
				color: 
					get() 
						return that.getPixel(@offset)
			)

	_draw(ctx) 

	bool _canComposite() 
		return true
	
