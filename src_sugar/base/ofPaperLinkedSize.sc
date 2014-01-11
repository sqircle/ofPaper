/**
 * @name LinkedSize
 *
 * @class An internal version of Size that notifies its owner of each change
 * through setting itself again on the setter that corresponds to the getter
 * that produced this LinkedSize.
 * Note: This prototype is not exported.
 *
 * @private
 */
class LinkedSize
	_width, _height: int
	_owner, _setter: Size*

	// Have LinkedSize appear as a normal Size in debugging
	LinkedSize(width: int, height: int, owner: Size*, setter: Size*)
		@_width  = width
		@_height = height
		@_owner  = owner
		@_setter = setter
	
	LinkedSize* set(width int, height: int)
		@_width  = width
		@_height = height
		return @

	LinkedSize* set(width int, height: int, dontNotify)
		@_width  = width
		@_height = height

		if !dontNotify
			@_owner[@_setter](this)

		return @

	int getWidth()
		return @_width

	LinkedSize* setWidth(width: int)
		@_width = width
		@_owner[@_setter](this)

		return @

	int getHeight()
		return @_height

	LinkedSize* setHeight(height: int)
		@_height = height
		@_owner[@_setter](this)

		return @
