/**
 * @name LinkedPoint
 *
 * @class An internal version of Point that notifies its owner of each change
 * through setting itself again on the setter that corresponds to the getter
 * that produced this LinkedPoint.
 * Note: This prototype is not exported.
 *
 * @ignore
 */
class LinkedPoint
	[public]
	_x, _y: int
	_setter, _owner: Point*

	// Have LinkedPoint appear as a normal Point in debugging
	Point(x: int, y: int, owner: Point*, setter: Point*)
		@_x = x;
		@_y = y;
		@_owner  = owner
		@_setter = setter

	set(x: int, y: int)
		return @set(x, y, false)

	Point* set(x: int, y: int, dontNotify: bool)
		@_x = x
		@_y = y
		if !dontNotify
			@_owner->@_setter(@)

		return @

	int getX()
		return @_x

	void setX(x: int) 
		@_x = x
		@_owner->@_setter(@)

	int getY()
		return @_y

	void setY(y: int)
		@_y = y
	  @_owner->@_setter(@)
