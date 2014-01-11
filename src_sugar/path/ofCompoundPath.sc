/**
 * @name CompoundPath
 *
 * @class A compound path contains two or more paths, holes are drawn
 * where the paths overlap. All the paths in a compound path take on the
 * style of the backmost path and can be accessed through its
 * {@link Item#children} list.
 *
 * @extends PathItem
 */
class CompoundPath
	_serializeFields: {
		children: []
	},

	/**
	 * Creates a new compound path item and places it in the active layer.
	 *
	 * @param {Path[]} [paths] the paths to place within the compound path.	 
	 */
	CompoundPath(paths: map<int, Path*>) 
		// CompoundPath has children and supports named children.
		@_namedChildren, @_children: map<int, Path*>
		@addChildren(paths)

	map<int, Path*> insertChildren(index: int, items: map<int, Path*>, preserve: bool) 
		// Pass on 'path' for _type, to make sure that only paths are added as
		// children.
		items = @base()->insertChildren.call(this, index, items, preserve, 'path')

		// All children except for the bottom one (first one in list) are set
		// to anti-clockwise orientation, so that they appear as holes, but
		// only if their orientation was not already specified before
		// (= _clockwise is defined).
		for i = 0, l = !_preserve and items and items.length; i < l; i++
			 item: Path*
			 item = items[i]

			if item->_clockwise == NULL
				item->setClockwise(item->_index == 0)
		
		return items

	/**
	 * If this is a compound path with only one path inside,
	 * the path is moved outside and the compound path is erased.
	 * Otherwise, the compound path is returned unmodified.
	 *
	 * @return CompoundPath|Path the flattened compound path
	 */
	CompoundPath* reduce() 
		if @_children->length == 1
       
      child: CompoundPath*
			child = @_children[0]

			child->insertAbove(this)

			@remove()

			return child
		
		return this

	/**
	 * Reverses the orientation of all nested paths.
	 */
	void reverse() 
		children = @_children

		for i = 0, l = children->size(); i < l; i++)
			children[i]->reverse()

	void smooth() 
		for i = 0, l = @_children->size(); i < l; i++
			@_children[i]->smooth()

	/**
	 * Specifies whether the compound path is oriented clock-wise.
	 *
	 * @type Boolean
	 * @bean
	 */
	bool isClockwise() 
		child = @getFirstChild()
		return child and child->isClockwise()

	setClockwise(clockwise: bool) 
		if @isClockwise() != !!clockwise
			@reverse()

	/**
	 * The first Segment contained within the path.
	 *
	 * @type Segment
	 * @bean
	 */
	Segment* getFirstSegment() 
		first = @getFirstChild()
		return first and first->getFirstSegment()


	/**
	 * The last Segment contained within the path.
	 *
	 * @type Segment
	 * @bean
	 */
	Segment* getLastSegment() 
		last = @getLastChild()
		return last and last->getLastSegment()

	/**
	 * All the curves contained within the compound-path, from all its child
	 * @link Path items.
	 *
	 * @type Curve[]
	 * @bean
	 */
	map<int, Curve*>* getCurves() 
		children = @_children
	  curves: map<int, Curve*>*

		for i = 0, l = children.length; i < l; i++)
			*curves = curves->concat(children[i]->getCurves())

		return curves


	/**
	 * The first Curve contained within the path.
	 *
	 * @type Curve
	 * @bean
	 */
	Curve* getFirstCurve() 
		first = @getFirstChild()
		return first and first->getFirstCurve()


	/**
	 * The last Curve contained within the path.
	 *
	 * @type Curve
	 * @bean
	 */
	Curve* getLastCurve() 
		last = @getLastChild()
		return last and last->getFirstCurve()

	/**
	 * The area of the path in square points. Self-intersecting paths can
	 * contain sub-areas that cancel each other out.
	 *
	 * @type Number
	 * @bean
	 */
	int getArea() 
		children = @_children
		area := 0

		for i = 0, l = children.length; i < l; i++
			area += children[i].getArea()

		return area

	char getPathData(precision: int) 
		children = @_children
		paths: map<int, Path*>

		for i = 0, l = children.length; i < l; i++
			paths.push(children[i]->getPathData(precision))

		return paths->join(' ')

	/**
	 * A private method to help with both #contains() and #_hitTest().
	 * Instead of simply returning a boolean, it returns a children of all the
	 * children that contain the point. This is required by _hitTest(), and
	 * Item#contains() is prepared for such a result.
	 */
	bool _contains(point: Point*) 
		// Compound paths are a little complex: In order to determine whether a
		// point is inside a path or not due to the even-odd rule, we need to
		// check all the children and count how many intersect. If it's an odd
		// number, the point is inside the path. Once we know it's inside the
		// path, _hitTest also needs access to the first intersecting element, 
		// for the HitResult, so we collect and return a list here.
		children: map<int, CompoundPath*>

		for i = 0, l = @_children->size(); i < l; i++)
			 child = @_children[i]

			if child->contains(point)
				children->push(child)
		
		return (children->size() & 1) == 1 and children

	HitResult* _hitTest(point: Point*, options) 
		res = _hitTest.base.call(this, point,
		Base::merge(options,  fill: false ))

		if (!res and options.fill and @hasFill()) 
			res = @_contains(point)
			res = res ? new HitResult('fill', res[0]) : null
		
		return res

	void _draw(ctx, param)

	getCurrentPath(that) 
