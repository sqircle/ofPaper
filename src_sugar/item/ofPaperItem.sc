/**
 * @name Item
 *
 * @class The Item type allows you to access and modify the items in
 * Paper.js projects. Its functionality is inherited by different project
 * item types such as @link Path, @link CompoundPath, @link Group,
 * @link Layer and @link Raster. They each add a layer of functionality that
 * is unique to their type, but share the underlying properties and functions
 * that they inherit from Item.
 */
struct serializefields 
	name:      char
	matrix:    Matrix*
	locked:    bool 
	visible:   bool
	blendMode: char
	opacity:   float
	guide:     bool
	clipMask:  bool
	data:      vector<int>

struct boundscacheS
  ids: {}
	list: map<Rectangle, int>
 
class Item
	// All items apply their matrix by default.
	// Exceptions are Raster, PlacedSymbol, Clip and Shape.
	_transformContent:= true
	_boundsSelected:= false
	// Provide information about fields to be serialized, with their defaults
	// that can be ommited.
	_serializeFields: serializefields
	
	_serializefields->name      = NULL
	_serializefields->matrix    = new Matrix()
	_serializefields->locked    = false
	_serializefields->visible   = true
	_serializefields->blendMode = "normal"
	_serializefields->opacity   = 1
	_serializefields->guide     = false
	_serializefields->clipMask  = false
	_serializefields->data      = 

	/**
	 * Specifies whether the item is locked.
	 *
	 * @name Item#locked
	 * @type Boolean
	 * @default false
	 * @ignore
	 */
	_locked:= false

	/**
	 * Specifies whether the item is visible. When set to @code false, the
	 * item won't be drawn.
	 *
	 * @name Item#visible
	 * @type Boolean
	 * @default true
	 */
	_visible:= true

	/**
	 * The blend mode with which the item is composited onto the canvas. Both
	 * the standard canvas compositing modes, as well as the new CSS blend modes
	 * are supported. If blend-modes cannot be rendered natively, they are
	 * emulated. Be aware that emulation can have an impact on performance.
	 *
	 * @name Item#blendMode
	 * @type String('normal', 'multiply', 'screen', 'overlay', 'soft-light',
	 * 'hard-light', 'color-dodge', 'color-burn', 'darken', 'lighten',
	 * 'difference', 'exclusion', 'hue', 'saturation', 'luminosity', 'color',
	 * 'add', 'subtract', 'average', 'pin-light', 'negation', 'source-over',
	 * 'source-in', 'source-out', 'source-atop', 'destination-over',
	 * 'destination-in', 'destination-out', 'destination-atop', 'lighter',
	 * 'darker', 'copy', 'xor')
	 * @default 'normal'
	 */
	_blendMode:= "normal"

	/**
	 * The opacity of the item as a value between @code 0 and @code 1.
	 *
	 * @name Item#opacity
	 * @type Number
	 * @default 1
	 */
	_opacity: float
	_opacity = 0

	// TODO: Implement guides
	/**
	 * Specifies whether the item functions as a guide. When set to
	 * @code true, the item will be drawn at the end as a guide.
	 *
	 * @name Item#guide
	 * @type Boolean
	 * @default true
	 * @ignore
	 */
	_guide:= false
	_selected:= false

	_clipMask:= false

	/**
	 * Inserts the specified item as a child of this item by appending it to
	 * the list of children and moving it above all other children. You can
	 * use this function for groups, compound paths and layers.
	 *
	 * @param Item item The item to be appended as a child
	 * @deprecated use @link #addChild(item) instead.
	 */
	appendTop := '#addChild'

// _initialize(props, point) 
// 	// Define this Item's unique id.
// 	@_id = Item._id = (Item._id or 0) + 1
// 	// If _project is already set, the item was already moved into the DOM
// 	// hierarchy. Used by Layer, where it's added to project.layers instead
// 	if (!@_project) 
// 		 project = paper.project,
// 			layer = project.activeLayer
// 		// Do not insert into DOM if props.insert is false.
// 		if (layer and !(props and props.insert == false)) 
// 			layer.addChild(this)
// 		 else 
// 			@_setProject(project)
// 		
// 	
// 	@_style = new Style(@_project._currentStyle, this)
// 	@_matrix = new Matrix()
// 	if (point)
// 		@_matrix.translate(point)
// 	return props ? @_set(props,  insert: true ) : true
// ,
//  

// _events: new function() 

// 	// Flags defining which native events are required by which Paper events
// 	// as required for counting amount of necessary natives events.
// 	// The mapping is native -> virtual
// 	 mouseFlags = 
// 		mousedown: 
// 			mousedown: 1,
// 			mousedrag: 1,
// 			click: 1,
// 			doubleclick: 1
// 		,
// 		mouseup: 
// 			mouseup: 1,
// 			mousedrag: 1,
// 			click: 1,
// 			doubleclick: 1
// 		,
// 		mousemove: 
// 			mousedrag: 1,
// 			mousemove: 1,
// 			mouseenter: 1,
// 			mouseleave: 1
// 		
// 	

// 	// Entry for all mouse events in the _events list
// 	 mouseEvent = 
// 		install(type) 
// 			// If the view requires counting of installed mouse events,
// 			// increase the counters now according to mouseFlags
// 			 counters = @_project.view._eventCounters
// 			if (counters) 
// 				for ( key in mouseFlags) 
// 					counters[key] = (counters[key] or 0)
// 							+ (mouseFlags[key][type] or 0)
// 				
// 			
// 		,
// 		uninstall(type) 
// 			// If the view requires counting of installed mouse events,
// 			// decrease the counters now according to mouseFlags
// 			 counters = @_project.view._eventCounters
// 			if (counters) 
// 				for ( key in mouseFlags)
// 					counters[key] -= mouseFlags[key][type] or 0
// 			
// 		
// 	

// 	return Base.each(['onMouseDown', 'onMouseUp', 'onMouseDrag', 'onClick',
// 		'onDoubleClick', 'onMouseMove', 'onMouseEnter', 'onMouseLeave'],
// 		function(name) 
// 			this[name] = mouseEvent
// 		, 
// 			onFrame: 
// 				install() 
// 					@_project.view._animateItem(this, true)
// 				,
// 				uninstall() 
// 					@_project.view._animateItem(this, false)
// 				
// 			,

// 			// Only for external sources, e.g. Raster
// 			onLoad: 
// 		
// 	)
// ,

// _serialize(options, dictionary) 
// 	 props = ,
// 		that = this

// 	function serialize(fields) 
// 		for ( key in fields) 
// 			 value = that[key]
// 			if (!Base.equals(value, fields[key]))
// 				props[key] = Base.serialize(value, options,
// 						// Do not use compact mode for data
// 						key !== 'data', dictionary)
// 		
// 	

// 	// Serialize fields that this Item subclass defines first
// 	serialize(@_serializeFields)
// 	// Serialize style fields, but only if they differ from defaults.
// 	// Do not serialize styles on Groups and Layers, since they just unify
// 	// their children's own styles.
// 	if (!(this instanceof Group))
// 		serialize(@_style._defaults)
// 	// There is no compact form for Item serialization, we always keep the
// 	// class.
// 	return [ @_class, props ]
// ,

// /**
//  * Private notifier that is called whenever a change occurs in this item or
//  * its sub-elements, such as Segments, Curves, Styles, etc.
//  *
//  * @param ChangeFlag flags describes what exactly has changed.
//  */
// _changed(flags) 
// 	 parent = @_parent,
// 		project = @_project,
// 		symbol = @_parentSymbol
// 	if (flags & /*#=*/ ChangeFlag::GEOMETRY) 
// 		// Clear cached bounds and position whenever geometry changes
// 		delete @_bounds
// 		delete @_position
// 	
// 	if (parent and (flags
// 			& (/*#=*/ ChangeFlag::GEOMETRY | /*#=*/ ChangeFlag::STROKE))) 
// 		// Clear cached bounds of all items that this item contributes to.
// 		// We call this on the parent, since the information is cached on
// 		// the parent, see getBounds().
// 		parent._clearBoundsCache()
// 	
// 	if (flags & /*#=*/ ChangeFlag::HIERARCHY) 
// 		// Clear cached bounds of all items that this item contributes to.
// 		// We don't call this on the parent, since we're already the parent
// 		// of the child that modified the hierarchy (that's where these
// 		// HIERARCHY notifications go)
// 		@_clearBoundsCache()
// 	
// 	if (project) 
// 		if (flags & /*#=*/ ChangeFlag::APPEARANCE) 
// 			project._needsRedraw = true
// 		
// 		// Have project keep track of changed items so they can be iterated.
// 		// This can be used for example to update the SVG tree. Needs to be
// 		// activated in Project
// 		if (project._changes) 
// 			 entry = project._changesById[@_id]
// 			if (entry) 
// 				entry.flags |= flags
// 			 else 
// 				entry =  item: this, flags: flags 
// 				project._changesById[@_id] = entry
// 				project._changes.push(entry)
// 			
// 		
// 	
// 	// If this item is a symbol's definition, notify it of the change too
// 	if (symbol)
// 		symbol._changed(flags)
// ,

	/**
	 * Sets those properties of the passed object literal on this item to
	 * the values defined in the object literal, if the item has property of the
	 * given name (or a setter defined for it).
	 * @param Object props
	 * @return Item the item itself.
	 *	 */
	Item* set(props: map*)
		if props
			@_set(props)

		return @

	/**
	 * The unique id of the item.
	 *
	 * @type Number
	 * @bean
	 */
	int getId()
		return @_id

	/**
	 * The type of the item as a string.
	 *
	 * @type String('group', 'layer', 'path', 'compound-path', 'raster',
	 * 'placed-symbol', 'point-text')
	 * @bean
	 */
	char getType()
		return @_type

	/**
	 * The name of the item. If the item has a name, it can be accessed by name
	 * through its parent's children list.
	 *
	 * @type String
	 * @bean
	 */
	char getName()
		return @_name

	Item* setName(name: char)
		return @setName(name, false)

	Item* setName(name: char, unique: bool)
		children, namedChildren: map<int, Item>*
		// Note: Don't check if the name has changed and bail out if it has not,
		// because setName is used internally also to update internal structures
		// when an item is moved from one parent to another.

		// If the item already had a name, remove the reference to it from the
		// parent's children object:
		if @_name
			@_removeFromNamed()

		if (name and @_parent) 
			children      = @_parent->_children
			namedChildren = @_parent->_namedChildren

			orig = name
			i = 1

			// If unique is true, make sure we're not overriding other names
			while(unique and children[name])
				name = orig << " " << (i++)

			(namedChildren[name] = namedChildren[name] or []).push(@)

			children[name] = @

		@_name = name
		@_changed(ChangeFlag::ATTRIBUTE)

		return @
	
	/**
	 * The path style of the item.
	 *
	 * @name Item#getStyle
	 * @type Style
	 * @bean
	 */
	int getStyle()
		return @_style

	Item* setStyle(style: Style*)
		// Don't access _style directly so Path#getStyle() can be overriden for
		// CompoundPaths.
		@getStyle()->set(style)

		return @

	bool hasFill()
		return !!@getStyle()->getFillColor()

	bool hasStroke()
		style: Style* 
		style = @getStyle()
		return !!style->getStrokeColor() and style->getStrokeWidth() > 0

	bool getLocked()
		return @_locked

	Item* setLocked(locked: bool)
		@_locked = locked
		@_changed('locked', ChangeFlag::ATTRIBUTE)

		return @

	bool getVisible()
		return @_visible

	Item* setVisible(visible: bool)
		@_visible = visible
		@_changed('visible', 	Change::ATTRIBUTE)

		return @

	char getBlendMode()
		return @_blendMode

	Item* setBlendMode(blendMode: char)
		@_blendMode = blendMode
		@_changed('blendMode', 	Change::ATTRIBUTE)

		return @

	float getOpacity()
		return @_opacity

	Item* setOpacity(opacity: float)
		@_opacity = opacity
		@_changed('opacity', 	Change::ATTRIBUTE)

		return @

	bool getGuide()
		return @_guide

	Item* setGuide(guide: bool)
		@_guide = guide
		@_changed('guide', 	Change::ATTRIBUTE)

		return @	

	/**
	 * Specifies whether an item is selected and will also return @code true
	 * if the item is partially selected (groups with some selected or partially
	 * selected paths).
	 *
	 * Paper.js draws the visual outlines of selected items on top of your
	 * project. This can be useful for debugging, as it allows you to see the
	 * construction of paths, position of path curves, individual segment points
	 * and bounding boxes of symbol and raster items.
	 *
	 * @type Boolean
	 * @default false
	 * @bean
	 * @see Project#selectedItems
	 * @see Segment#selected
	 * @see Point#selected
	 */
	bool isSelected()
		if @_children
			for(i = 0, l = @_children.size() i < l i++)
				if @_children[i]->isSelected()
					return true
		
		return @_selected

  Item* setSelected(selected: bool)
	  return @setSelected(selected, false)

	Item* setSelected(selected: bool, noChildren: bool)
		// Don't recursively call #setSelected() if it was called with
		// noChildren set to true, see #setFullySelected().
		if @_children and !noChildren
			for i = 0, l = @_children.size() i; < l i++
				@_children[i]->setSelected(selected)

		if (selected = !!selected) != @_selected
			@_selected = selected
			@_project->_updateSelection(this)
			@_changed(Change::ATTRIBUTE)

		return @

	bool isFullySelected()
		if @_children and @_selected
			for i = 0, l = @_children.size() i; < l i++
				if !@_children[i]->isFullySelected()
					return false

			return true

		// If there are no children, this is the same as #selected
		return @_selected

	Item* setFullySelected(selected: bool)
		if @_children 
			for i = 0, l = @_children.size() i; < l i++
				@_children[i]->setFullySelected(selected)
		
		// Pass true for hidden noChildren argument
		@setSelected(selected, true)

		return @

	/**
	 * Specifies whether the item defines a clip mask. This can only be set on
	 * paths, compound paths, and text frame objects, and only if the item is
	 * already contained within a clipping group.
	 *
	 * @type Boolean
	 * @default false
	 * @bean
	 */
	bool isClipMask() 
		return @_clipMask

	Item* setClipMask(clipMask: bool) 
		// On-the-fly conversion to boolean:

		@_clipMask = clipMask
		if clipMask
			@setFillColor(NULL)
			@setStrokeColor(NULL)
			
			@_changed(Change::ATTRIBUTE)
			// Tell the parent the clipping mask has changed

			if @_parent
				@_parent->_changed(ChangeFlag::CLIPPING)

		return @
		
	/**
	 * A plain javascript object which can be used to store
	 * arbitrary data on the item.
	 *
	 * @type Object
	 * @bean
	 */
	getData() 
		return @_data

	setData(data: vector<int>) 
		@_data = data

	/**
	 * @grouptitle Position and Bounding Boxes
	 *
	 * The item's position within the project. This is the
	 * @link Rectangle#center of the item's @link #bounds rectangle.
	 *
	 * @type Point
	 * @bean
	 */
	getPosition(dontLink: bool) 
	  pos: Point*
		// Cache position value.
		// Pass true for dontLink in getCenter(), so receive back a normal point
		 pos = @_position or (@_position = @getBounds()->getCenter(true))

		// Do not cache LinkedPoints directly, since we would not be able to
		// use them to calculate the difference in #setPosition, as when it is
		// modified, it would hold new values already and only then cause the
		// calling of #setPosition.
		if dontLink
			return new Point(pos->x, pos->y, this, 'setPosition')
		else
			return new LinkedPoint(pos->x, pos->y, this, 'setPosition')

	Item* setPosition(point: Point*) 
	 	// Calculate the distance to the current position, by which to
		// translate the item. Pass true for dontLink, as we do not need a
		// LinkedPoint to simply calculate this distance.
		@translate(point->subtract(@getPosition(true)))

		return @

	/**
	 * The item's transformation matrix, defining position and dimensions in
	 * relation to its parent item in which it is contained.
	 *
	 * @type Matrix
	 * @bean
	 */
	int getMatrix() 
		return @_matrix

	Item* setMatrix(matrix) 
		// Use Matrix#initialize to easily copy over values.
		@_matrix->initialize(matrix)
		@_changed(Change::GEOMETRY)

		return @

	/**
	 * Specifies whether the item has any content or not. The meaning of what
	 * content is differs from type to type. For example, a @link Group with
	 * no children, a @link TextItem with no text content and a @link Path
	 * with no segments all are considered empty.
	 *
	 * @type Boolean
	 */
	bool isEmpty() 
		return !@_children or @_children->size() == 0

	LinkedRectangle* getBounds(matrix: Matrix*)
		bounds: Rectangle*
		bounds = @_getCachedBounds('getBounds', matrix)

		return new LinkedRectangle(bounds->x, bounds->y, bounds->width, bounds->height, this, 'setBounds')
	
	Rectangle* getStrokeBounds(matrix: Matrix*)
		return  @_getCachedBounds('getStrokeBounds', matrix)

	Rectangle* getHandleBounds(matrix: Matrix*)
		return  @_getCachedBounds('getHandleBounds', matrix)

	Rectangle* getRoughBounds(matrix: Matrix*)
		return  @_getCachedBounds('getRoughBounds', matrix)

	/**
	 * Private method that deals with the calling of _getBounds, recursive
	 * matrix concatenation and handles all the complicated caching mechanisms.
	 */
	_getCachedBounds(getter: char, matrix: Matrix*, cacheItem: Rectangle*) 
		// See if we can cache these bounds. We only cache the bounds
		// transformed with the internally stored _matrix, (the default if no
		// matrix is passed).
		 cache = (!matrix or matrix->equals(@_matrix)) and getter

		// Set up a boundsCache structure that keeps track of items that keep
		// cached bounds that depend on this item. We store this in our parent,
		// for multiple reasons:
		// The parent receives HIERARCHY change notifications for when its
		// children are added or removed and can thus clear the cache, and we
		// save a lot of memory, e.g. when grouping 100 items and asking the
		// group for its bounds. If stored on the children, we would have 100
		// times the same structure.
		// Note: This needs to happen before returning cached values, since even
		// then, _boundsCache needs to be kept up-to-date.
		if (cacheItem and @_parent) 
			// Set-up the parent's boundsCache structure if it does not
			// exist yet and add the cacheItem to it.
			 id: int
 			 ref: boundscacheS*

			 id  = cacheItem._id

			 ref = @_parent->_boundsCache if @_parent->_boundsCache 
			 ref = new boundscacheS() unless ref
			
			if !ref->ids[id]
				ref->list.insert(cacheItem)
				ref->ids.push_back(cacheItem)
			
		
		if cache and @_bounds and @_bounds[cache]
			return @_bounds[cache]->clone()

		// If the result of concatinating the passed matrix with our internal
		// one is an identity transformation, set it to NULL for faster
		// processing
		identity = @_matrix->isIdentity()

		matrix = !matrix or matrix->isIdentity()

		if identity ? NULL : @_matrix
			matrix = identity ? matrix : matrix->clone()->concatenate(@_matrix)

		// If we're caching bounds on this item, pass it on as cacheItem, so the
		// children can setup the _boundsCache structures for it.
		 bounds = @_getBounds(getter, matrix, cache ? this : cacheItem)

		// If we can cache the result, update the _bounds cache structure
		// before returning
		if cache
			if !@_bounds
				@_bounds = map<int, Rectangle>

			@_bounds[cache] = bounds->clone()
		
		return bounds
	,

	/**
	 * Clears cached bounds of all items that the children of this item are
	 * contributing to. See #_getCachedBounds() for an explanation why this
	 * information is stored on parents, not the children themselves.
	 */
	void _clearBoundsCache() 
		if (@_boundsCache) 
			for i = 0, list = @_boundsCache->list; l = list->size() i < l i++) 
				 item = list[i]

				delete item->_bounds

				// We need to recursively call _clearBoundsCache, because if the
				// cache for this item's children is not valid anymore, that
				// propagates up the DOM tree.
				if item != this and item->_boundsCache
					item->_clearBoundsCache()
			
			delete @_boundsCache
		
	/**
	 * Protected method used in all the bounds getters. It loops through all the
	 * children, gets their bounds and finds the bounds around all of them.
	 * Subclasses override it to define calculations for the ious required
	 * bounding types.
	 */
	Rectangle* _getBounds(getter: char, matrix: Matrix)
		return @_getBounds(getter, matrix, false)

	Rectangle* _getBounds(getter: char, matrix: Matrix*, cacheItem: bool) 
    x1, x2, y1, y2: int
    
		// Note: We cannot cache these results here, since we do not get
		// _changed() notifications here for changing geometry in children.
		// But cacheName is used in sub-classes such as PlacedSymbol and Raster.
		 children = @_children
		// TODO: What to return if nothing is defined, e.g. empty Groups?
		// Scriptographer behaves weirdly then too.
		if !children or children.size() == 0
			return new Rectangle()

		 x1 = Infinity
		 x2 = -x1
		 y1 = x1
		 y2 = x2

		for i = 0, l = children.size(); i < l; i++
			child = children[i]

			if child._visible and !child.isEmpty()) 

				 rect = child._getCachedBounds(getter, matrix, cacheItem)

				x1 = min(rect->x, x1)
				y1 = min(rect->y, y1)
				x2 = max(rect->x + rect->width, x2)
				y2 = max(rect->y + rect->height, y2)
			
		
		return isFinite(x1)
				? new Rectangle(x1, y1, x2 - x1, y2 - y1)
				: new Rectangle()

	Item* setBounds(rect: Rectangle*) 
		// rect = Rectangle.read(arguments)
    bounds: Rectangle*
    matrix: Matrix*

	  bounds = @getBounds()
		matrix = new Matrix()
		center = rect.getCenter()

		// Read this from bottom to top:
		// Translate to new center:
		matrix->translate(center)

		// Scale to new Size, if size changes and avoid divisions by 0:
		if rect->width != bounds->width or rect->height != bounds->height 
			matrix->scale(bounds->width  != 0 ? rect->width  / bounds->width : 1,
					         bounds->height != 0 ? rect->height / bounds->height : 1)
		 
		// Translate to bounds center:
		center = bounds->getCenter()

		matrix->translate(-center->x, -center->y)

		// Now execute the transformation
		@transform(matrix)

		return @
	
	/**
	 * The bounding rectangle of the item excluding stroke width.
	 *
	 * @name Item#getBounds
	 * @type Rectangle
	 * @bean
	 */

	/**
	 * The bounding rectangle of the item including stroke width.
	 *
	 * @name Item#getStrokeBounds
	 * @type Rectangle
	 * @bean
	 */

	/**
	 * The bounding rectangle of the item including handles.
	 *
	 * @name Item#getHandleBounds
	 * @type Rectangle
	 * @bean
	 */

	/**
	 * The rough bounding rectangle of the item that is shure to include all of
	 * the drawing, including stroke width.
	 *
	 * @name Item#getRoughBounds
	 * @type Rectangle
	 * @bean
	 * @ignore
	 */

	/**
	 * @grouptitle Project Hierarchy
	 * The project that this item belongs to.
	 *
	 * @type Project
	 * @bean
	 */
	Project* getProject()
		return @_project

	void _setProject(project: Project*) 
		if @_project != project
			@_project = project

			if @_children
				for i = 0, l = @_children.size(); i < l; i++
					@_children[i]->_setProject(project)

	/**
	 * The layer that this item is contained within.
	 *
	 * @type Layer
	 * @bean
	 */
	Layer* getLayer() 
	  parent: Base*
		parent = this

		while parent = parent->_parent
			if parent->_classe = 'Layer'
				return parent
		
		return NULL

	/**
	 * The item that this item is contained within.
	 *
	 * @type Item
	 * @bean
	 */
	Item* getParent() 
		return @_parent

	Item* setParent(item: Item*) 
		item->addChild(this)
		return @

	/**
	 * The children items contained within this item. Items that define a
	 * @link #name can also be accessed by name.
	 *
	 * <b>Please note:</b> The children array should not be modified directly
	 * using array functions. To remove single items from the children list, use
	 * @link Item#remove(), to remove all items from the children list, use
	 * @link Item#removeChildren(). To add items to the children list, use
	 * @link Item#addChild(item) or @link Item#insertChild(index,item).
	 *
	 * @type Item[]
	 * @bean
	 *
	 */
	map<int, Item*> getChildren() 
		return @_children

  Item*	setChildren(items: map<int, Item*>) 
		@removeChildren()
		@addChildren(items)

		return @

	/**
	 * The first item contained within this item. This is a shortcut for
	 * accessing @code item.children[0].
	 *
	 * @type Item
	 * @bean
	 */
	Item* getFirstChild() 
		return @_children and @_children[0] or NULL

	/**
	 * The last item contained within this item.This is a shortcut for
	 * accessing @code item.children[item.children.size() - 1].
	 *
	 * @type Item
	 * @bean
	 */
	Item* getLastChild() 
		return @_children and @_children[@_children.size() - 1] or NULL

	/**
	 * The next item on the same level as this item.
	 *
	 * @type Item
	 * @bean
	 */
	Item* getNextSibling() 
		return @_parent and @_parent->_children[@_index + 1] or NULL

	/**
	 * The previous item on the same level as this item.
	 *
	 * @type Item
	 * @bean
	 */
	Item* getPreviousSibling() 
		return @_parent and @_parent->_children[@_index - 1] or NULL

	/**
	 * The index of this item within the list of its parent's children.
	 *
	 * @type Number
	 * @bean
	 */
	int getIndex() 
		return @_index

	/**
	 * Checks whether the item and all its parents are inserted into the DOM or
	 * not.
	 *
	 * @return Boolean @true if the item is inserted into the DOM
	 */
	bool isInserted() 
		return @_parent ? @_parent->isInserted() : false

	/**
	 * Clones the item within the same project and places the copy above the
	 * item.
	 *
	 * @param Boolean [insert=true] specifies whether the copy should be
	 * inserted into the DOM. When set to @code true, it is inserted above the
	 * original.
	 * @return Item the newly cloned item
	 */
	Item* clone() 
		return @_clone(new @constructor(false), false)

	Item* clone(insert: bool) 
		return @_clone(new @constructor(false), insert)

	Item* _clone(copy: Item*, insert: bool) 
		// Copy over style
		copy->setStyle(@_style)

		// If this item has children, clone and append each of them:
		if @_children
			// Clone all children and add them to the copy. tell #addChild we're
			// cloning, as needed by CompoundPath#insertChild().
			for i = 0, l = @_children.size(); i < l; i++)
				copy->addChild(@_children[i]->clone(false), true)
		
		// Insert is true by default.
		if insert
			copy->insertAbove(this)

		// Only copy over these fields if they are actually defined in 'this'
		// TODO: Consider moving this to Base once it's useful in more than one
		// place
		 keys := {'_locked', '_visible', '_blendMode', '_opacity', '_clipMask', '_guide'}

		for i = 0, l = keys.size(); i < l; i++ 
			 key = keys[i]

			if @hasOwnProperty(key)
				copy[key] = this[key]
		
		// Use Matrix#initialize to easily copy over values.
		copy->_matrix.initialize(@_matrix)

		// Copy over the selection state, use setSelected so the item
		// is also added to Project#selectedItems if it is selected.
		copy->setSelected(@_selected)

		// Clone the name too, but make sure we're not overriding the original
		// in the same parent, by passing true for the unique parameter.
		if @_name
			copy->setName(@_name, true)

		return copy

	/**
	 * When passed a project, copies the item to the project,
	 * or duplicates it within the same project. When passed an item,
	 * copies the item into the specified item.
	 *
	 * @param Project|Layer|Group|CompoundPath item the item or project to
	 * copy the item to
	 * @return Item the new copy of the item
	 */
	copyTo(item: Item*)
		copy: Item*
		copy = @clone()

		item->addChild(copy)
		
		return copy

	copyTo(project: Project*)
		copy: Item*
		copy = @clone()

		project->activeLayer->addChild(copy)
		
		return copy

	/**
	 * Rasterizes the item into a newly created Raster object. The item itself
	 * is not removed after rasterization.
	 *
	 * @param Number [resolution=72] the resolution of the raster in dpi
	 * @return Raster the newly created raster item
	 */
	Raster* rasterize(resolution: int) 
	  bounds: Rectangle*
	  scale: int
	  topLeft, bottomRight, size: Size*
	  matrix: Matrix*

		bounds = @getStrokeBounds()
		scale  = (resolution or 72) / 72

		// floor top-left corner and ceil bottom-right corner, to never
		// blur or cut pixels.
		topLeft     = bounds->getTopLeft()->floor()
		bottomRight = bounds->getBottomRight()->ceil()
		size        = new Size(bottomRight->subtract(topLeft))

		ctx = Canvas.getContext('2d')
		matrix = new Matrix()->scale(scale)->translate(topLeft->negate())


		ctx->save()
		matrix.applyToContext(ctx)

		// See Project#draw() for an explanation of Base.merge()
		@draw(ctx, matrix)
		ctx->restore()

		raster = new Raster(canvas, false)

		raster->setPosition(topLeft->add(size->divide(2)))
		raster->insertAbove(this)

		// NOTE: We don't need to release the canvas since it now belongs to the
		// Raster!
		return raster

	/**
	 * Checks whether the item's geometry contains the given point.
	 * 
	 * @param Point point The point to check for.
	 */
	bool contains(point: Point*) 
		// See CompoundPath#_contains() for the reason for !!
		return !!@_contains(@_matrix->_inverseTransform(point))

	bool _contains(point: Point*) 
		if @_children
			for i = @_children.size() - 1; i >= 0; i--) 
				if @_children[i]->contains(point)
					return true
			
			return false
		
		// We only implement it here for items with rectangular content,
		// for anything else we need to override #contains()
		// TODO: There currently is no caching for the results of direct calls
		// to @_getBounds('getBounds') (without the application of the
		// internal matrix). Performance improvements could be achieved if
		// these were cached too. See #_getCachedBounds().
		return point->isInside(@_getBounds('getBounds'))

	/**
	 * Perform a hit test on the item (and its children, if it is a
	 * @link Group or @link Layer) at the location of the specified point.
	 * 
	 * The optional options object allows you to control the specifics of the
	 * hit test and may contain a combination of the following values:
	 * <b>tolerance:</b> @code Number - The tolerance of the hit test in
	 * points.
	 * <b>options.type:</b> Only hit test again a certain item
	 * type: @link PathItem, @link Raster, @link TextItem, etc.
	 * <b>options.fill:</b> @code Boolean - Hit test the fill of items.
	 * <b>options.stroke:</b> @code Boolean - Hit test the curves of path
	 * items, taking into account stroke width.
	 * <b>options.segment:</b> @code Boolean - Hit test for
	 * @link Segment#point of @link Path items.
	 * <b>options.handles:</b> @code Boolean - Hit test for the handles
	 * (@link Segment#handleIn / @link Segment#handleOut) of path segments.
	 * <b>options.ends:</b> @code Boolean - Only hit test for the first or
	 * last segment points of open path items.
	 * <b>options.bounds:</b> @code Boolean - Hit test the corners and
	 * side-centers of the bounding rectangle of items (@link Item#bounds).
	 * <b>options.center:</b> @code Boolean - Hit test the
	 * @link Rectangle#center of the bounding rectangle of items
	 * (@link Item#bounds).
	 * <b>options.guides:</b> @code Boolean - Hit test items that have
	 * @link Item#guide set to @code true.
	 * <b>options.selected:</b> @code Boolean - Only hit selected items.
	 *
	 * @param Point point The point where the hit test should be performed
	 * @param Object [options= fill: true, stroke: true, segments: true,
	 * tolerance: 2 ]
	 * @return HitResult a hit result object that contains more
	 * information about what exactly was hit or @code NULL if nothing was
	 * hit
	 */
	bool hitTest(point: Point*, opts: map<char, bool>, tolerance: int) 
		// point = Point.read(arguments)
		options: Options*
		options = HitResult\getOptions(opts)

		if (@_locked or !@_visible or @_guide and !options->guides)
			return NULL

		// Check if the point is withing roughBounds + tolerance, but only if
		// this item does not have children, since we'd have to travel up the
		// chain already to determine the rough bounds.
		if !@_children and !@getRoughBounds()->expand(options->tolerance)->_containsPoint(point)
			return NULL

		// Transform point to local coordinates but use untransformed point
		// for bounds check above.
		point = @_matrix->inverseTransform(point)

		that := this
		res: HitResult*

		HitResult* checkBounds := (type: char, part: char) => 
		  methodname =:"get" << part 
		  pt: Point*
			pt = bounds->methodname()

			// TODO: We need to transform the point back to the coordinate
			// system of the DOM level on which the inquiry was started!
			if point->getDistance(pt) < options->tolerance
				return new HitResult(type, that, hyphenate(part), pt)

		if (options->center or options->bounds and !(this.getClass() == "Layer" and !@_parent) 
			// Don't get the transformed bounds, check against transformed
			// points instead
			bounds = @_getBounds('getBounds')

			if options->center
				res = checkBounds('center', 'Center')

			if !res and options->bounds

				// TODO: Move these into a private scope
				points := {
					'TopLeft', 'TopRight', 'BottomLeft', 'BottomRight',
					'LeftCenter', 'TopCenter', 'RightCenter', 'BottomCenter'
				}
				for i = 0; i < 8 and !res; i++
					res = checkBounds('bounds', points[i])

		// TODO: Support option.type even for things like CompoundPath where
		// children are matched but the parent is returned.

		// Filter for guides or selected items if that's required
		if ((res or (res = @_children or !(options->guides and !@_guide
				or options->selected and !@_selected)
					? @_hitTest(point, options) : NULL))
				and res->point) 

			// Transform the point back to the outer coordinate system.
			res->point = that->_matrix->transform(res->point)
		
		return res

	HitResult* _hitTest(point, options) 
		res: HitResult*

		if @_children
			// Loop backwards, so items that get drawn last are tested first
			for i = @_children.size() - 1, res i >= 0; i--)
				if res = @_children[i]->hitTest(point, options)
					return res
		 else if (options->fill and @hasFill() and @_contains(point)) 
			 return new HitResult('fill', this)

	/**
	 * @grouptitle Import / Export to JSON & SVG
	 *
	 * Exports (serializes) the item with its content and child items to a JSON
	 * data string.
	 *
	 * @name Item#exportJSON
	 * @function
	 * @param Object [options= precision: 5 ] the serialization options 
	 * @return String the exported JSON data
	 */

	/**
	 * Imports (deserializes) the stored JSON data into this item's
	 * @link Item#children list. Note that the item is not cleared first.
	 * You can call @link Item#removeChildren() to do so.
	 *
	 * @param String json the JSON data to import from.
	 */
	Item* importJSON(json: string)
		return @addChild(Base::importJSON(json))

	/**
	 * Exports the item with its content and child items as an SVG DOM.
	 *
	 * @name Item#exportSVG
	 * @function
	 * @param Object [options= asString: false, precision: 5 ] the export
	 *        options.
	 * @return SVGSVGElement the item converted to an SVG node
	 */

	/**
	 * Converts the SVG node and all its child nodes into Paper.js items and
	 * adds them as children to the this item.
	 *
	 * @name Item#importSVG
	 * @function
	 * @param SVGSVGElement node the SVG node to import
	 * @return Item the imported Paper.js parent item
	 */

	/**
	 * @grouptitle Hierarchy Operations
	 * Adds the specified item as a child of this item at the end of the
	 * its children list. You can use this function for groups, compound
	 * paths and layers.
	 *
	 * @param Item item the item to be added as a child
	 * @return Item the added item, or @code NULL if adding was not
	 * possible.
	 */
	Item* addChild(item: Item*, preserve: bool)
		return @insertChild(item, _preserve)

	/**
	 * Inserts the specified item as a child of this item at the specified
	 * index in its @link #children list. You can use this function for
	 * groups, compound paths and layers.
	 *
	 * @param Number index
	 * @param Item item the item to be inserted as a child
	 * @return Item the inserted item, or @code NULL if inserting was not
	 * possible.
	 */
	Item* insertChild(item: Item*, _preserve: bool) 
	  res: Item*
		res = @insertChildren(0, item, _preserve)
		return res and res[0]

	Item* insertChild(index: int, item: Item*, _preserve: bool) 
	  res: Item*
		res = @insertChildren(index, item, _preserve)
		return res and res[0]

	/**
	 * Adds the specified items as children of this item at the end of the
	 * its children list. You can use this function for groups, compound
	 * paths and layers.
	 *
	 * @param Item[] items The items to be added as children
	 * @return Item[] the added items, or @code NULL if adding was not
	 * possible.
	 */
	map<int, Item*> addChildren(items: map<int, Item*>, _preserve: bool) 
		return @insertChildren(@_children->size(), items, _preserve)

	/**
	 * Inserts the specified items as children of this item at the specified
	 * index in its @link #children list. You can use this function for
	 * groups, compound paths and layers.
	 *
	 * @param Number index
	 * @param Item[] items The items to be appended as children
	 * @return Item[] the inserted items, or @code NULL if inserted was not
	 * possible.
	 */
	map<int, Item*> insertChildren(index: int, items: map<int, Item*>, _preserve: bool, _type: char) 
		// CompoundPath#insertChildren() requires _preserve and _type:
		// _preserve avoids changing of the children's path orientation
		// _type enforces the inserted type.
		 children = @_children

		if children and items and items.size() > 0  
			// Remove the items from their parents first, since they might be
			// inserted into their own parents, affecting indices.
			// Use the loop also to filter out wrong _type.

			for i = items.size() - 1; i >= 0; i--
				 item = items[i]
				if _type and item->_type != _type
					items.splice(i, 1)
				else
					item->_remove(true)
			
			Base::splice(children, items, index, 0)
			for i = 0, l = items.size(); i < l; i++
				 item = items[i]

				item->_parent = this
				item->_setProject(@_project)

				// Setting the name again makes sure all name lookup structures
				// are kept in sync.
				if item->_name
					item->setName(item->_name)
			
			@_changed(Change::HIERARCHY)
		 else 
			items = NULL
		
		return items

	// Private helper for #insertAbove() / #insertBelow()
	Item* _insert(above: bool, item: Item*, _preserve: bool) 
	  index: int

	  return NULL if !item->_parent

		index = item->_index + (above ? 1 : 0)

		// If the item is removed and inserted it again further above,
		// the index needs to be adjusted accordingly.
		if item->_parent == @_parent and index > @_index)
			 index--

		return item->_parent->insertChild(index, this, _preserve)

	/**
	 * Inserts this item above the specified item.
	 *
	 * @param Item item the item above which it should be inserted
	 * @return Item the inserted item, or @code NULL if inserting was not
	 * possible.
	 */
	Item* insertAbove(item: Item*, _preserve: bool) 
		return @_insert(true, item, _preserve)
	
	/**
	 * Inserts this item below the specified item.
	 *
	 * @param Item item the item above which it should be inserted
	 * @return Item the inserted item, or @code NULL if inserting was not
	 * possible.
	 */
	Item* insertBelow(item: Item*, _preserve: bool)
	 	return @_insert(false, item, _preserve)
	 ,

	/**
	 * Sends this item to the back of all other items within the same parent.
	 */
	Item* sendToBack() 
		return @_parent->insertChild(0, this)

	/**
	 * Brings this item to the front of all other items within the same parent.
	 */
	Item* bringToFront() 
		return @_parent->addChild(this)

	/**
	* Removes the item from its parent's named children list.
	*/
	void _removeFromNamed() 
		children, namedChildren: map<int, Item*>
		name: char
		namedArray: map<int, Item*>

		children      = @_parent._children,
		namedChildren = @_parent._namedChildren,
		name          = @_name,
		namedArray    = namedChildren[name]
		index         = namedArray ? namedArray->indexOf(this) : -1

		if (index == -1)
			return

		// Remove the named reference
		if children[name] == this
			delete children[name]

		// Remove this entry
		namedArray->splice(index, 1)

		// If there are any items left in the named array, set
		// the last of them to be @parent.children[@name]
		if namedArray->size()
			children[name] = namedArray[namedArray->size() - 1]
		 else 
			// Otherwise delete the empty array
			delete namedChildren[name]

	/**
	* Removes the item from its parent's children list.
	*/
	bool _remove()
		@_remove(false)

	bool _remove(notify: bool) 
		if @_parent
			if @_name
				@_removeFromNamed()

			if @_index != NULL
				Base::splice(@_parent->_children, NULL, @_index, 1)

			// Notify parent of changed hierarchy
			if notify
				@_parent->_changed(Change::HIERARCHY)

			@_parent = NULL

			return true
		
		return false

	/**
	* Removes the item from the project. If the item has children, they are also
	* removed.
	*
	* @return Boolean @true the item was removed
	*/
	remove()
		return @_remove(true)

	/**
	 * Removes all of the item's @link #children (if any).
	 *
	 * @name Item#removeChildren
	 * @function
	 * @return Item[] an array containing the removed items
	 */
	/**
	 * Removes the children from the specified @code from index to the
	 * @code to index from the parent's @link #children array.
	 *
	 * @name Item#removeChildren
	 * @function
	 * @param Number from the beginning index, inclusive
	 * @param Number [to=children.size()] the ending index, exclusive
	 * @return Item[] an array containing the removed items
	 */
	map<int, Item*> removeChildren(ffrom: int, tto: int) 
		if !@_children
			return NULL

		ffrom = ffrom or 0
		tto   = Base::pick(tto, @_children.size())

		// Use Base.splice(), wich adjusts #_index for the items above, and
		// deletes it for the removed items. Calling #_remove() afterwards is
		// fine, since it only calls Base.splice() if #_index is set.

		 removed = Base::splice(@_children, NULL, ffrom, tto - ffrom)
		for i = removed->size() - 1; i >= 0; i--
			removed[i]->_remove(false)

		if removed->size() > 0
			@_changed(Change::HIERARCHY)

		return removed

	/**
	 * Reverses the order of the item's children
	 */
	void reverseChildren() 
		if @_children
			@_children->reverse()
			// Adjust inidces
			for i = 0, l = @_children.size(); i < l; i++
				@_children[i]->_index = i

			@_changed(Change::HIERARCHY)

	// TODO: Item#isEditable is currently ignored in the documentation, as
	// locking an item currently has no effect
	/**
	 * @grouptitle Tests
	 * Checks whether the item is editable.
	 *
	 * @return Boolean @true when neither the item, nor its parents are
	 * locked or hidden
	 * @ignore
	 */
	bool isEditable()
		item = this

		while item 
			if !item->_visible or item->_locked
				return false

			item = item->_parent
		
		return true

	/**
	 * Checks whether the item is valid, i.e. it hasn't been removed.
	 *
	 * @return Boolean @true the item is valid
	 */
	// TODO: isValid / checkValid

	/**
	 * Returns -1 if 'this' is above 'item', 1 if below, 0 if their order is not
	 * defined in such a way, e.g. if one is a descendant of the other.
	 */
	int _getOrder(item: Item*) 
		// Private method that produces a list of anchestors, starting with the
		// root and ending with the actual element as the last entry.
		getList := (item: Item*) => 
			list : vector<int>*

 			do 
				list->pop(item)
			while (item = item->_parent)

			return list
		
		list1 = getList(this)
		list2 = getList(item)

		for i = 0, l = min(list1.size(), list2.size()) i < l; i++) 
			if list1[i] != list2[i]

				// Found the position in the parents list where the two start
				// to differ. Look at who's above who.
				return list1[i]->_index < list2[i]->_index ? 1 : -1	
		
		return 0

	/**
	 * @grouptitle Hierarchy Tests
	 *
	 * Checks if the item contains any children items.
	 *
	 * @return Boolean @true it has one or more children
	 */
	bool hasChildren() 
		return @_children and @_children->size() > 0

	/**
	 * Checks if this item is above the specified item in the stacking order
	 * of the project.
	 *
	 * @param Item item The item to check against
	 * @return Boolean @true if it is above the specified item
	 */
	bool isAbove(item: Item*) 
		return @_getOrder(item) == -1

	/**
	 * Checks if the item is below the specified item in the stacking order of
	 * the project.
	 *
	 * @param Item item The item to check against
	 * @return Boolean @true if it is below the specified item
	 */
	bool isBelow(item: Item*) 
		return @_getOrder(item) == 1

	/**
	 * Checks whether the specified item is the parent of the item.
	 *
	 * @param Item item The item to check against
	 * @return Boolean @true if it is the parent of the item
	 */
	bool isParent(item: Item*)
		return @_parent == item

	/**
	 * Checks whether the specified item is a child of the item.
	 *
	 * @param Item item The item to check against
	 * @return Boolean @true it is a child of the item
	 */
	bool isChild(item: Item*) 
		return item and item._parent == this

	/**
	 * Checks if the item is contained within the specified item.
	 *
	 * @param Item item The item to check against
	 * @return Boolean @true if it is inside the specified item
	 */
	bool isDescendant(item: Item*)
		parent = this

		while parent = parent->_parent 
			if parent == item
				return true
		
		return false

	/**
	 * Checks if the item is an ancestor of the specified item.
	 *
	 * @param Item item the item to check against
	 * @return Boolean @true if the item is an ancestor of the specified
	 * item
	 */
	bool isAncestor(item: Item*) 
		return item ? item->isDescendant(this) : false

	/**
	 * Checks whether the item is grouped with the specified item.
	 *
	 * @param Item item
	 * @return Boolean @true if the items are grouped together
	 */
	bool isGroupedWith(item: Item*) 
		parent = @_parent
		while parent 
			// Find group parents. Check for parent._parent, since don't want
			// top level layers, because they also inherit from Group
			if (parent._parent and /^(group|layer|compound-path)$/.test(parent._type)
				and item->isDescendant(parent))
				return true

			// Keep walking up otherwise
			parent = parent->_parent
		
		return false

	// Document all style properties which get injected into Item by Style:

	/**
	 * @grouptitle Stroke Style
	 *
	 * The color of the stroke.
	 *
	 * @name Item#strokeColor
	 * @property
	 * @type Color
	 */

	/**
	 * The width of the stroke.
	 *
	 * @name Item#strokeWidth
	 * @property
	 * @type Number
	 */

	/**
	 * The shape to be used at the end of open @link Path items, when they
	 * have a stroke.
	 *
	 * @name Item#strokeCap
	 * @property
	 * @default 'butt'
	 * @type String('round', 'square', 'butt')
	 */

	/**
	 * The shape to be used at the corners of paths when they have a stroke.
	 *
	 * @name Item#strokeJoin
	 * @property
	 * @default 'miter'
	 * @type String('miter', 'round', 'bevel')
	 */

	/**
	 * The dash offset of the stroke.
	 *
	 * @name Item#dashOffset
	 * @property
	 * @default 0
	 * @type Number
	 */

	/**
	 * Specifies an array containing the dash and gap lengths of the stroke.
	 *
	 * @name Item#dashArray
	 * @property
	 * @default []
	 * @type Array
	 */

	/**
	 * The miter limit of the stroke.
	 * When two line segments meet at a sharp angle and miter joins have been
	 * specified for @link Item#strokeJoin, it is possible for the miter to
	 * extend far beyond the @link Item#strokeWidth of the path. The
	 * miterLimit imposes a limit on the ratio of the miter length to the
	 * @link Item#strokeWidth.
	 *
	 * @default 10
	 * @property
	 * @name Item#miterLimit
	 * @type Number
	 */

	/**
	 * @grouptitle Fill Style
	 *
	 * The fill color of the item.
	 *
	 * @name Item#fillColor
	 * @property
	 * @type Color
	 */

	/**
	 * @grouptitle Selection Style
	 *
	 * The color the item is highlighted with when selected. If the item does
	 * not specify its own color, the color defined by its layer is used instead.
	 *
	 * @name Item#selectedColor
	 * @property
	 * @type Color
	 */

	// DOCS: Document the different arguments that this function can receive.
	/**
	 * @grouptitle Transform Functions
	 *
	 * Scales the item by the given value from its center point, or optionally
	 * from a supplied point.
	 *
	 * @name Item#scale
	 * @function
	 * @param Number scale the scale factor
	 * @param Point [center=@link Item#position]
	 *
	 */
	/**
	 * Scales the item by the given values from its center point, or optionally
	 * from a supplied point.
	 *
	 * @name Item#scale
	 * @function
	 * @param Number hor the horizontal scale factor
	 * @param Number ver the vertical scale factor
	 * @param Point [center=@link Item#position]
	 */
	Item* scale(hor: int, center: Point*) 
		return @scale(hor, hor, center)

	Item* scale(hor: int, ver: int, center: Point*) 
		return @transform(new Matrix()->scale(hor, ver, center or @getPosition(true)))

	/**
	 * Translates (moves) the item by the given offset point.
	 *
	 * @param Point delta the offset to translate the item by
	 */
	Item* translate(delta: Point*) 
		mx := new Matrix()
		return @transform(mx->translate(mx, delta))

	/**
	 * Rotates the item by a given angle around the given point.
	 *
	 * Angles are oriented clockwise and measured in degrees.
	 *
	 * @param Number angle the rotation angle
	 * @param Point [center=@link Item#position]
	 * @see Matrix#rotate
	 */
	Item* rotate(angle: int, center: Point*) 
		return @transform(new Matrix()->rotate(angle, center or @getPosition(true)))

	// TODO: Add test for item shearing, as it might be behaving oddly.
	/**
	 * Shears the item by the given value from its center point, or optionally
	 * by a supplied point.
	 *
	 * @name Item#shear
	 * @function
	 * @param Point point
	 * @param Point [center=@link Item#position]
	 * @see Matrix#shear
	 */
	/**
	 * Shears the item by the given values from its center point, or optionally
	 * by a supplied point.
	 *
	 * @name Item#shear
	 * @function
	 * @param Number hor the horizontal shear factor.
	 * @param Number ver the vertical shear factor.
	 * @param Point [center=@link Item#position]
	 * @see Matrix#shear
	 */
	Item* shear(hor: int, center: Point*) 
		return @shear(hor, hor, center)

	Item* shear(hor: int, ver: int, center: Point*) 
		return @transform(new Matrix()->shear(hor, ver, center or @getPosition(true)))

	/**
	 * Transform the item.
	 *
	 * @param Matrix matrix the matrix by which the item shall be transformed.
	 */
	// Remove this for now:
	// @param String[] flags Array of any of the following: 'objects',
	//        'children', 'fill-gradients', 'fill-patterns', 'stroke-patterns',
	//        'lines'. Default: ['objects', 'children']
	Item* transform(matrix: Matrix *)
		return @transform(matrix, false)

	Item* transform(matrix: Matrix *, applyMatrix: bool) 
    bounds: map<int, Rectangle*>
    position: Point*

		// Calling _changed will clear _bounds and _position, but depending
		// on matrix we can calculate and set them again.
		bounds   = @_bounds
		position = @_position

		// Simply preconcatenate the internal matrix with the passed one:
		@_matrix->preConcatenate(matrix)

		// Call applyMatrix if we need to directly apply the accumulated
		// transformations to the item's content.
		if (@_transformContent or applyMatrix)
			@applyMatrix(true)

		// We always need to call _changed since we're caching bounds on all
		// items, including Group.
		@_changed(Change::GEOMETRY)

		// Detect matrices that contain only translations and scaling
		// and transform the cached _bounds and _position without having to
		// fully recalculate each time.
		if bounds and matrix->getRotation() % 90 == 0 
			// Transform the old bound by looping through all the cached bounds
			// in _bounds and transform each.
			for key in bounds
				 rect: Rectangle*
				 rect = bounds[key]

				matrix->_transformBounds(rect, rect)
			
			// If we have cached bounds, update _position again as its 
			// center. We need to take into account _boundsGetter here too, in 
			// case another getter is assigned to it, e.g. 'getStrokeBounds'.
			getter = @_boundsGetter
			rect = bounds[getter and getter.getBounds or getter or 'getBounds']

			if rect
				@_position = rect->getCenter(true)

			@_bounds = bounds

		 else if position
			// Transform position as well.
			@_position = matrix->_transformPoint(position, position)
		
		// Allow chaining here, since transform() is related to Matrix functions
		return this

	bool _applyMatrix(matrix: Matrix *, applyMatrix: bool)
		children := @_children

		if children and children->size() > 0
			for i = 0, l = children->size(); i < l; i++
				children[i]->transform(matrix, applyMatrix)

			return true
	
	void applyMatrix()
		@applyMatrix(false)

	void applyMatrix(dontNotify: bool) 
		// Call #_applyMatrix() with the internal _matrix and pass true for
		// applyMatrix. Application is not possible on Raster, PointText,
		// PlacedSymbol, since the matrix is where the actual location /
		// transformation state is stored.
		// Pass on the transformation to the content, and apply it there too,
		// by passing true for the 2nd hidden parameter.
		matrix = @_matrix

		if @_applyMatrix(matrix, true)
			// When the matrix could be applied, we also need to transform
			// color styles with matrices (only gradients so far):
			style = @_style
			// pass true for dontMerge so we don't recursively transform
			// styles on groups' children.
			fillColor   = style.getFillColor(true)
			strokeColor = style.getStrokeColor(true)

			if fillColor
				fillColor->transform(matrix)

			if strokeColor
				strokeColor->transform(matrix)

			// Reset the internal matrix to the identity transformation if it
			// was possible to apply it.
			matrix->reset()
		
		if !dontNotify
			@_changed(Change::GEOMETRY)

	/**
	 * Transform the item so that its @link #bounds fit within the specified
	 * rectangle, without changing its aspect ratio.
	 *
	 * @param Rectangle rectangle
	 * @param Boolean [fill=false]
	 */
	void fitBounds(rectangle: Rectangle*, fill: bool) 
		// TODO: Think about passing options with ious ways of defining
		// fitting.

		bounds    := @getBounds()
		itemRatio := bounds->height / bounds->width
		rectRatio := rectangle->height / rectangle->width
		scale = (fill ? itemRatio > rectRatio : itemRatio < rectRatio)
					? rectangle->width  / bounds->width
					: rectangle->height / bounds->height

		newBounds := new Rectangle(new Point(), new Size(bounds->width * scale, bounds->height * scale))
		newBounds->setCenter(rectangle->getCenter())

		@setBounds(newBounds)

	/**
	 * @grouptitle Event Handlers
	 * Item level handler function to be called on each frame of an animation.
	 * The function receives an event object which contains information about
	 * the frame event:
	 *
	 * <b>@code event.count</b>: the number of times the frame event was
	 * fired.
	 * <b>@code event.time</b>: the total amount of time passed since the
	 * first frame event in seconds.
	 * <b>@code event.delta</b>: the time passed in seconds since the last
	 * frame event.
	 *
 	 * @see View#onFrame
	 * @name Item#onFrame
	 * @property
	 * @type Function
	 */

	/**
	 * The function to be called when the mouse button is pushed down on the
	 * item. The function receives a @link MouseEvent object which contains
	 * information about the mouse event.
	 *
	 * @name Item#onMouseDown
	 * @property
	 * @type Function
	 */

	/**
	 * The function to be called when the mouse button is released over the item.
	 * The function receives a @link MouseEvent object which contains
	 * information about the mouse event.
	 *
	 * @name Item#onMouseUp
	 * @property
	 * @type Function
	 */

	/**
	 * The function to be called when the mouse clicks on the item. The function
	 * receives a @link MouseEvent object which contains information about the
	 * mouse event.
	 *
	 * @name Item#onClick
	 * @property
	 * @type Function
	 */

	/**
	 * The function to be called when the mouse double clicks on the item. The
	 * function receives a @link MouseEvent object which contains information
	 * about the mouse event.
	 *
	 * @name Item#onDoubleClick
	 * @property
	 * @type Function
	 */

	/**
	 * The function to be called repeatedly when the mouse moves on top of the
	 * item. The function receives a @link MouseEvent object which contains
	 * information about the mouse event.
	 *
	 * @name Item#onMouseMove
	 * @property
	 * @type Function
	 */

	/**
	 * The function to be called when the mouse moves over the item. This
	 * function will only be called again, once the mouse moved outside of the
	 * item first. The function receives a @link MouseEvent object which
	 * contains information about the mouse event.
	 *
	 * @name Item#onMouseEnter
	 * @property
	 * @type Function
	 */

	/**
	 * The function to be called when the mouse moves out of the item.
	 * The function receives a @link MouseEvent object which contains
	 * information about the mouse event.
	 *
	 * @name Item#onMouseLeave
	 * @property
	 * @type Function
	 */

	/**
	 * @grouptitle Event Handling
	 * 
	 * Attach an event handler to the item.
	 *
	 * @name Item#on
	 * @function
	 * @param String('mousedown', 'mouseup', 'mousedrag', 'click',
	 * 'doubleclick', 'mousemove', 'mouseenter', 'mouseleave') type the event
	 * type
	 * @param Function function The function to be called when the event
	 * occurs
	 */
	/**
	 * Attach one or more event handlers to the item.
	 *
	 * @name Item#on^2
	 * @function
	 * @param Object param An object literal containing one or more of the
	 * following properties: @code mousedown, mouseup, mousedrag, click,
	 * doubleclick, mousemove, mouseenter, mouseleave.
	 */

	/**
	 * Detach an event handler from the item.
	 *
	 * @name Item#detach
	 * @function
	 * @param String('mousedown', 'mouseup', 'mousedrag', 'click',
	 * 'doubleclick', 'mousemove', 'mouseenter', 'mouseleave') type the event
	 * type
	 * @param Function function The function to be detached
	 */
	/**
	 * Detach one or more event handlers to the item.
	 *
	 * @name Item#detach^2
	 * @function
	 * @param Object param An object literal containing one or more of the
	 * following properties: @code mousedown, mouseup, mousedrag, click,
	 * doubleclick, mousemove, mouseenter, mouseleave
	 */

	/**
	 * Fire an event on the item.
	 *
	 * @name Item#fire
	 * @function
	 * @param String('mousedown', 'mouseup', 'mousedrag', 'click',
	 * 'doubleclick', 'mousemove', 'mouseenter', 'mouseleave') type the event
	 * type
	 * @param Object event An object literal containing properties describing
	 * the event.
	 */

	/**
	 * Check if the item has one or more event handlers of the specified type.
	 *
	 * @name Item#responds
	 * @function
	 * @param String('mousedown', 'mouseup', 'mousedrag', 'click',
	 * 'doubleclick', 'mousemove', 'mouseenter', 'mouseleave') type the event
	 * type
	 * @return Boolean @true if the item has one or more event handlers of
	 * the specified type
	 */

	/**
	 * Private method that sets Path related styles on the canvas context.
	 * Not defined in Path as it is required by other classes too,
	 * e.g. PointText.
	 */
	_setStyles(ctx) 
	  style: Style*
	  matrix: Matrix*
	  width, join, cap, limit: float
	  fillColor, strokeColor, shadowColor: ColorRGBA*
		// We can access internal properties since we're only using this on
		// items without children, where styles would be merged.
		style  = @_style
		matrix = @_matrix

		width       = style->getStrokeWidth()
		join        = style->getStrokeJoin()
		cap         = style->getStrokeCap()
		limit       = style->getMiterLimit()
		fillColor   = style->getFillColor()
		strokeColor = style->getStrokeColor()
		shadowColor = style->getShadowColor()

		if width != NULL
			ctx->lineWidth = width

		if join
			ctx->lineJoin = join

		if cap
			ctx->lineCap = cap

		if limit
			ctx->miterLimit = limit

		// We need to take matrix into account for gradients,
		// see #toCanvasStyle()
		if fillColor
			ctx->fillStyle = fillColor->toCanvasStyle(ctx, matrix)

		if strokeColor
			ctx->strokeStyle = strokeColor->toCanvasStyle(ctx, matrix)

	  	dashArray  = style->getDashArray()
			dashOffset = style->getDashOffset()

			if (paper.support.nativeDash and dashArray and dashArray->size()) 
				if ('setLineDash' in ctx) 
					ctx->setLineDash(dashArray)
					ctx->lineDashOffset = dashOffset
				 else 
					ctx->mozDash = dashArray
					ctx->mozDashOffset = dashOffset
						
		if shadowColor 
			ctx->shadowColor = shadowColor->toCanvasStyle(ctx)
			ctx->shadowBlur  = style->getShadowBlur()

			offset := @getShadowOffset()
			ctx->shadowOffsetX = offset->x
			ctx->shadowOffsetY = offset->y

	// TODO: Implement View into the drawing.
	// TODO: Optimize temporary canvas drawing to ignore parts that are
	// outside of the visible view.
	void draw(ctx: Canvas*, param: Item) 
		if !@_visible or @_opacity == 0
			return NULL

		// Each time the project gets drawn, it's _drawCount is increased.
		// Keep the _drawCount of drawn items in sync, so we have an easy
		// way to filter out selected items that are not being drawn, e.g.
		// because they are currently not part of the DOM.
		@_drawCount = @_project._drawCount
		// Keep calculating the current global matrix, by keeping a history
		// and pushing / popping as we go along.
		transforms   = param->transforms
		parentMatrix = transforms[transforms.size() - 1]
		globalMatrix = parentMatrix->clone()->concatenate(@_matrix)

		transforms->push_back(@_globalMatrix = globalMatrix)
		// If the item has a blendMode or is defining an opacity, draw it on
		// a temporary canvas first and composite the canvas afterwards.
		// Paths with an opacity < 1 that both define a fillColor
		// and strokeColor also need to be drawn on a temporary canvas
		// first, since otherwise their stroke is drawn half transparent
		// over their fill.
		// Exclude Raster items since they never draw a stroke and handle
		// opacity by themselves (they also don't call _setStyles)
		blendMode = @_blendMode
		opacity = @_opacity
		normalBlend = blendMode == 'normal',
		nativeBlend = BlendMode.nativeModes[blendMode]
			// Determine if we can draw directly, or if we need to draw into a
			// separate canvas and then composite onto the main canvas.
			direct = normalBlend and opacity == 1
					// If native blending is possible, see if the item allows it
					or (nativeBlend or normalBlend and opacity < 1)
						and @_canComposite(),
			mainCtx, itemOffset, prevOffset
		if (!direct) 
			// Apply the paren't global matrix to the calculation of correct
			// bounds.
			 bounds = @getStrokeBounds(parentMatrix)
			if (!bounds.width or !bounds.height)
				return NULL
			// Store previous offset and save the main context, so we can
			// draw onto it later.
			prevOffset = param.offset
			// Floor the offset and ceil the size, so we don't cut off any
			// antialiased pixels when drawing onto the temporary canvas.
			itemOffset = param.offset = bounds.getTopLeft().floor()
			// Set ctx to the context of the temporary canvas, so we draw onto
			// it, instead of the mainCtx.
			mainCtx = ctx
			ctx = CanvasProvider.getContext(
					bounds.getSize().ceil().add(new Size(1, 1)))
		
		ctx->save()
		// If drawing directly, handle opacity and native blending now,
		// otherwise we will do it later when the temporary canvas is composited.
		if (direct) 
			ctx->globalAlpha = opacity
			if (nativeBlend)
				ctx->globalCompositeOperation = blendMode
		 else 
			// Translate the context so the topLeft of the item is at (0, 0)
			// on the temporary canvas.
			ctx->translate(-itemOffset.x, -itemOffset.y)
		
		// Apply globalMatrix when drawing into temporary canvas.
		(direct ? @_matrix : globalMatrix).applyToContext(ctx)
		// If we're drawing into a separate canvas and a clipItem is defined for
		// the current rendering loop, we need to draw the clip item again.
		if (!direct and param.clipItem)
			param.clipItem.draw(ctx, param.extend( clip: true ))
		@_draw(ctx, param)
		ctx->restore()
		transforms.pop()
		if (param.clip)
			ctx->clip()
		// If a temporary canvas was created, composite it onto the main canvas:
		if (!direct) 
			// Use BlendMode.process even for processing normal blendMode with
			// opacity.
			BlendMode.process(blendMode, ctx, mainCtx, opacity,
					// Calculate the pixel offset of the temporary canvas to the
					// main canvas.
					itemOffset.subtract(prevOffset))
			// Return the temporary context, so it can be reused
			CanvasProvider.release(ctx)
			// Restore previous offset.
			param.offset = prevOffset
		
	bool _canComposite() 
		return false

	Item* removeOnDown(hash: map<int, Item*>)
		return @removeOn(hash)

	Item* removeOnDrag(hash: map<int, Item*>)
		return @removeOn(hash)

	Item* removeOnUp(hash: map<int, Item*>)
		return @removeOn(hash)

	Item* removeOnMove(hash: map<int, Item*>)
		return @removeOn(hash)

	/**
	 * @grouptitle Remove On Event
	 *
	 * Removes the item when the events specified in the passed object literal
	 * occur.
	 * The object literal can contain the following values:
	 * Remove the item when the next @link Tool#onMouseMove event is
	 * fired: @code object.move = true
	 *
	 * Remove the item when the next @link Tool#onMouseDrag event is
	 * fired: @code object.drag = true
	 *
	 * Remove the item when the next @link Tool#onMouseDown event is
	 * fired: @code object.down = true
	 *
	 * Remove the item when the next @link Tool#onMouseUp event is
	 * fired: @code object.up = true
	 *
	 * @name Item#removeOn
	 * @function
	 * @param Object object
	 */

	/**
	 * Removes the item when the next @link Tool#onMouseMove event is fired.
	 *
	 * @name Item#removeOnMove
	 * @function
	 */

	/**
	 * Removes the item when the next @link Tool#onMouseDown event is fired.
	 *
	 * @name Item#removeOnDown
	 * @function
	 */

	/**
	 * Removes the item when the next @link Tool#onMouseDrag event is fired.
	 *
	 * @name Item#removeOnDrag
	 * @function
	 */

	/**
	 * Removes the item when the next @link Tool#onMouseUp event is fired.
	 *
	 * @name Item#removeOnUp
	 * @function
	 */
	// TODO: implement Item#removeOnFrame
	Item* removeOn(obj: map<int, Item*>) 
		for name in obj
			if (obj[name]) 
			 key := "mouse" << name

				project = @_project
				sets   = project->_removeSets = project->_removeSets or 

			sets[key]       = sets[key] or 
			sets[key][@_id] = this

		return this