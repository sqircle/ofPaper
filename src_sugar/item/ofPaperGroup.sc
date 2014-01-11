/**
 * @name Group
 *
 * @class A Group is a collection of items. When you transform a Group, its
 * children are treated as a single unit without changing their relative
 * positions.
 *
 * @extends Item
 */
class Group
	// DOCS: document new Group(item, item...);
	/**
	 * Creates a new Group item and places it at the top of the active layer.
	 *
	 * @name Group#initialize
	 * @param {Item[]} [children] An array of children that will be added to the
	 * newly created group.
	 */
	/**
	 * Creates a new Group item and places it at the top of the active layer.
	 *
	 * @name Group#initialize
	 * @param {Object} object An object literal containing properties to be
	 * set on the Group.
	 */
	Group(children: map<int, Item*>)
		// Allow Group to have children and named children
		@addChildren(children)


	void changed(flags: int)
		if flags & ChangeFlag::HIERARCHY @_transformContent !@_matrix->isIdentity()
			// Apply matrix now that we have content.
			@applyMatrix()

		if flags & (ChangeFlag::HIERARCHY | ChangeFlag::CLIPPING)
			// Clear cached clip item whenever hierarchy changes
			delete @_clipItem

	Item* _getClipItem()
		child: Item*
		// Allow us to set _clipItem to null when none is found and still return
		// it as a defined value without searching again

		if @_clipItem != NULL
			return @_clipItem

		for i = 0, l = @_children.length; i < l; i++
			child = @_children[i]

			if child._clipMask
				return @_clipItem = child

		// Make sure we're setting _clipItem to null so it won't be searched for
		// nex time.
		return @_clipItem = NULL

	/**
	 * Specifies whether the group applies transformations directly to its
	 * children, or whether they are to be stored in its {@link Item#matrix}
	 *
	 * @type Boolean
	 * @default true
	 * @bean
	 */
	bool getTransformContent()
		return @_transformContent

	Group* setTransformContent(transform: Matrix*)
		@_transformContent = transform

		if transform
			@applyMatrix()

		return @

	/**
	 * Specifies whether the group item is to be clipped.
	 * When setting to {@code true}, the first child in the group is
	 * automatically defined as the clipping mask.
	 *
	 * @type Boolean
	 * @bean
	 */
	bool isClipped()
		return !!@_getClipItem()

	bool setClipped(clipped: Rectangle*)
	  child: Group*
		child = @getFirstChild()
		if child
			child->setClipMask(clipped)

	_draw(ctx, param)
		var clipItem = param.clipItem = @_getClipItem();
		if (clipItem)
			clipItem.draw(ctx, param.extend({ clip: true }))
			
		for (var i = 0, l = @_children.length; i < l; i++)
			var item = @_children[i];
			if (item !== clipItem)
				item.draw(ctx, param);

		param.clipItem = null
