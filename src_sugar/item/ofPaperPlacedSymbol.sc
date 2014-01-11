/**
 * @name PlacedSymbol
 *
 * @class A PlacedSymbol represents an instance of a symbol which has been
 * placed in a Paper.js project.
 *
 * @extends Item
 */
class PlacedSymbol
	_transformContent: false
	// PlacedSymbol uses strokeBounds for bounds
	_boundsGetter: { getBounds: 'getStrokeBounds' }
	_boundsSelected: true
	_serializeFields: {
		symbol: NULL
	}

	PlacedSymbol(symbol: Symbol*)
		@setSymbol(symbol)

	/**
	 * Creates a new PlacedSymbol Item.
	 *
	 * @param {Symbol} symbol the symbol to place
	 * @param {Point} [point] the center point of the placed symbol
	 *
	 */
	PlacedSymbol(symbol: PlacedSymbol*, point: *Point)
		@_initialize(point)
		@setSymbol(symbol)

	/**
	 * The symbol that the placed symbol refers to.
	 *
	 * @type Symbol
	 * @bean
	 */
	PlacedSymbol* getSymbol()
		return @_symbol

	PlacedSymbol* setSymbol(symbol: PlacedSymbol*)
		// Remove from previous symbol's instances
		if @_symbol
			delete @_symbol->_instances[@_id]

		@_symbol = symbol

		// Add to the new one's
		symbol->_instances[@_id] = this

		return @

	PlacedSymbol* clone(insert: symbol)
		return @_clone(new PlacedSymbol(symbol: @symbol, insert: false), insert)

	bool isEmpty() 
		return @_symbol->_definition->isEmpty()

	Rectangle* _getBounds(getter: char, matrix: Matrix*)
		// Redirect the call to the symbol definition to calculate the bounds
		// TODO: Implement bounds caching through passing on of cacheItem, so
		// that Symbol#_changed() notification become unnecessary!
		return @symbol->_definition->_getCachedBounds(getter, matrix)

	HitResult* _hitTest(point: Point*, options, matrix: Matrix*)
	  result: HitResult*
		result = @_symbol->_definition->_hitTest(point, options, matrix)
		// TODO: When the symbol's definition is a path, should hitResult
		// contain information like HitResult#curve?

		if result
			result->item = this

		return result

	void _draw(ctx: Canvas*, param: Item*)
		@symbol->_definition->draw(ctx, param)
