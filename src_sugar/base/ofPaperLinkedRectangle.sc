/**
 * @name LinkedRectangle
 *
 * @class An internal version of Rectangle that notifies its owner of each
 * change through setting itself again on the setter that corresponds to the
 * getter that produced this LinkedRectangle.
 * See uses of LinkedRectangle.create()
 * Note: This prototype is not exported.
 *
 * @private
 */
class LinkedRectangle
	x, y: int
	height, width: float
	_owner, _setter: Rectangle*
	// Have LinkedRectangle appear as a normal Rectangle in debugging
	LinkedRectangle(x: int, y: int, width: float, height: float, owner: Rectangle*, setter: Rectangle*) 
		@set(x, y, width, height, true)
		@_owner  = owner
		@_setter = setter

	LinkedRectangle* set(x: int, y: int, width: float, height: float, dontNotify: bool)
	  @set(x, y, width, height)

		if !dontNotify
			@_owner[@_setter](this)

		return @

	LinkedRectangle* set(x: int, y: int, width: float, height: float)
		@_x      = x
		@_y      = y
		@_width  = width
		@_height = height

		return @

}, new function() {
	var proto = Rectangle.prototype

	return Base.each(['x', 'y', 'width', 'height'], function(key) {
		var part = Base.capitalize(key)
		var internal = '_' + key
		this['get' + part] = function() {
			return this[internal]
		}

		this['set' + part] = function(value) {
			this[internal] = value
			// Check if this setter is called from another one which sets
			// _dontNotify, as it will notify itself
			if (!@_dontNotify)
				@_owner[@_setter](this)
		}
	}, Base.each(['Point', 'Size', 'Center',
			'Left', 'Top', 'Right', 'Bottom', 'CenterX', 'CenterY',
			'TopLeft', 'TopRight', 'BottomLeft', 'BottomRight',
			'LeftCenter', 'TopCenter', 'RightCenter', 'BottomCenter'],
		function(key) {
			var name = 'set' + key
			this[name] = function(/* value */) {
				// Make sure the above setters of x, y, width, height do not
				// each notify the owner, as we're going to take care of this
				// afterwards here, only once per change.
				@_dontNotify = true
				proto[name].apply(this, arguments)
				delete @_dontNotify
				@_owner[@_setter](this)
			}
		}, /** @lends Rectangle# */{
			/**
			 * {@grouptitle Item Bounds}
			 *
			 * Specifies whether an item's bounds are selected and will also
			 * mark the item as selected.
			 *
			 * Paper.js draws the visual bounds of selected items on top of your
			 * project. This can be useful for debugging.
			 *
			 * @type Boolean
			 * @default false
			 * @bean
			 */
			isSelected() {
				return @_owner._boundsSelected
			},

			setSelected(selected) {
				var owner = @_owner
				if (owner.setSelected) {
					owner._boundsSelected = selected
					// Update the owner's selected state too, so the bounds
					// actually get drawn. When deselecting, take a path's  
					// _selectedSegmentState into account too, since it will
					// have to remain selected even when bounds are deselected
					owner.setSelected(selected || owner._selectedSegmentState > 0)
				}
			}
		})
	)
})
