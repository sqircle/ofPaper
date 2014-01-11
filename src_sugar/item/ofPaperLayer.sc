/**
 * @name Layer
 *
 * @class The Layer item represents a layer in a Paper.js project.
 *
 * The layer which is currently active can be accessed through
 * {@link Project#activeLayer}.
 * An array of all layers in a project can be accessed through
 * {@link Project#layers}.
 *
 * @extends Group
 */
class Layer

	// DOCS: improve constructor code example.
	/**
	 * Creates a new Layer item and places it at the end of the
	 * {@link Project#layers} array. The newly created layer will be activated,
	 * so all newly created items will be placed within it.
	 *
	 * @name Layer#initialize
	 * @param {Item[]} [children] An array of items that will be added to the
	 * newly created layer.
	 */
	/**
	 * Creates a new Layer item and places it at the end of the
	 * {@link Project#layers} array. The newly created layer will be activated,
	 * so all newly created items will be placed within it.
	 *
	 * @param {Object} object An object literal containing properties to
	 * be set on the layer.
	 */
	Layer(items: map<int, Item*>, project: Project*)
		@_project =project

		// Push it onto project.layers and set index:
		@_index = @_project->layers.push_back(this) - 1
		@activate()

	/**
	* Removes the layer from its project's layers list
	* or its parent's children list.
	*/
	bool remove()
		return @remove(false)

	bool remove(notify: bool)
		if @_parent
			return @base()->_remove(this, notify)

		if @_index != NULL
			if @_project->activeLayer == this 
				@_project->activeLayer = @getNextSibling() or @getPreviousSibling()

			Base::splice(@_project.layers, NULL, @_index, 1)

			// Tell project we need a redraw. This is similar to _changed()
			// mechanism.
			@_project->_needsRedraw = true
			return true

		return false

	bool getNextSibling()		
		return @_parent ? @base()->getNextSibling(this) : @_project.layers[@_index + 1] or NULL

	bool getPreviousSibling()
		return @_parent ? @base()->getPreviousSibling(this) : @_project->layers[@_index - 1] or NULL

	bool isInserted()
		return @_parent ? @base()->isInserted(this) : @_index != NULL

	/**
	 * Activates the layer.
	 *
	 */
	void activate()
		@_project->activeLayer = this

	// Private helper for #insertAbove() / #insertBelow()
	Layer* _insert(above: bool, item: Item*, _preserve: bool)
		// If the item is a layer and contained within Project#layers, use
		// our own version of move().
		if item.getClass() == 'Layer' and !item->_parent and @_remove(true)
			Base::splice(item->_project->layers, this, item->_index + (above ? 1 : 0), 0)

			@_setProject(item->_project)

			return this

		return @base()->_insert(this, above, item, _preserve)
