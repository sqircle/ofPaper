namespace ofPaper
  /**
   * @name Line
   * 
   * @class The Line object represents..
   */
  class Line
    [private]
    _px, _py, _vx, _vy: int

    /** 
     * DOCS: document Line class and constructor
     * Creates a Line object.
     *
     * @param {Point} point1
     * @param {Point} point2
     */
    Line(point1: Point*, point2: Point*)
      @_px = point1->x
      @_py = point1->y

      @_vx = point2->x
      @_vy = point2->y

    /** 
     * DOCS: document Line class and constructor
     * Creates a Line object.
     *
     * @param {Point} point1
     * @param {Point} point2
     * @param {Boolean} asvector
     */
    Line(point1: Point*, point2: Point*, asVector: bool)
      @_px = point1->x
      @_py = point1->y

      @_vx = point2->x
      @_vy = point2->y

      @_vx -= @_px
      @_vy -= @_py
    
    /**
     * The starting point of the line
     * 
     * @name Line#point
     * @type Point
     */
    Point getPoint()
      return Point(@_px, @_py)

    /**
     * The vector of the line
     * 
     * @name Line#vector
     * @type Point
     **/
    Point getVector()
      return Point(@_vx, @_vy)

    /**
     * The length of the line
     * 
     * @name Line#length
     * @type Number
     */
    int getLength()
      return @getVector()->getLength()

    /**
     * @param {Line} line
     * @return {Point} the intersection point of the lines, {@code undefined}
     * if the two lines are colinear, or {@code null} if they don't intersect.
     */
    Point intersect(line: Line)
      return Line::intersect(@_px, @_py, @_vx, @_vy, line._px, line._py, line._vx, line._vy, true)
    
    /**
     * @param {Line} line
     * @param {Boolean} [isInfinite=false]
     * @return {Point} the intersection point of the lines, {@code undefined}
     * if the two lines are colinear, or {@code null} if they don't intersect.
     */
    Point intersect(line: Line, isInfinite: bool)
      return Line::intersect(@_px, @_py, @_vx, @_vy, line._px, line._py, line._vx, line._vy, true, isInfinite)
    
    /**
     * DOCS: document Line#getSide(point)
     * 
     * @param {Point} point
     * @return {Float}
     */
    int getSide(point: Point)
      return Line::getSide(@_px, @_py, @_vx, @_vy, point.x, point.y, true)

    /**
     * DOCS: document Line#getDistance(point)
     * @param {Point} point
     * @return {Int}
     */
    int getDistance(point: Point)
      return abs(Line::getSignedDistance(@_px, @_py, @_vx, @_vy, point.x, point.y, true))

    static Point intersect(apx: int, apy: int, avx: int, avy: int, bpx: int, bpy: int, bvx: int, bvy: int)
      return Line::intersect(apx, apy, avx, avy, bpx, bpy, bvx, bvy, false, false)

    static Point intersect(apx: int, apy: int, avx: int, avy: int, bpx: int, bpy: int, bvx: int, bvy: int, asVector: bool)
      return Line::intersect(apx, apy, avx, avy, bpx, bpy, bvx, bvy, asVector, false)

    static Point intersect(apx: int, apy: int, avx: int, avy: int, bpx: int, bpy: int, bvx: int, bvy: int, asVector: bool, isInfinite: bool)
      cross, dx, dy, ta, tb: int

      // Convert 2nd points to vectors if they are not specified as such.
      unless asVector 
        avx -= apx
        avy -= apy
        bvx -= bpx
        bvy -= bpy
       
      cross = bvy * avx - bvx * avy
      
      // Avoid divisions by 0, and errors when getting too close to 0
      unless Numerical::isZero(cross)
        dx = apx - bpx
        dy = apy - bpy
        ta = (bvx * dy - bvy * dx) / cross
        tb = (avx * dy - avy * dx) / cross
        
        // Check the ranges of t parameters if the line is not allowed
        // to extend beyond the definition points.
        return Point(apx + ta * avx, apy + ta * avy) if (isInfinite or 0 <= ta and ta <= 1) and (isInfinite or 0 <= tb and tb <= 1)

    static int getSide(px: int, py: int, vx: int, vy: int, x: int, y: int)
      return Line::getSide(px, py, vx, vy, x, y, false)

    static int getSide(px: int, py: int, vx: int, vy: int, x: int, y: int, asVector: bool)
      v2x, v2y, ccw: int
      unless asVector
        vx -= px
        vy -= py

      v2x = x - px
      v2y = y - py
      ccw = v2x * vy - v2y * vx 

      if ccw is 0
        ccw = v2x * vx + v2y * vy

        if ccw > 0
          v2x -= vx
          v2y -= vy
          ccw = v2x * vx + v2y * vy

          ccw = 0 if ccw < 0

      if ccw < 0 
        return -1 
      else 
        if ccw > 0 
          return 1 
        else 
          return 0

    static int getSignedDistance(px: int, py: int, vx: int, vy: int, x: int, y: int)
      return Line::getSignedDistance(px, py, vx, vy, x, y, false)

    static int getSignedDistance(px: int, py: int, vx: int, vy: int, x: int, y: int, asVector: bool)
      m, b: int
      
      unless asVector
        vx -= px
        vy -= py
      
      // Cache these values since they're used heavily in fatline code
      m = vy / vx
      b = py - (m * px)

      // Distance to the linear equation
      return (y - (m * x) - b) / sqrt(m * m + 1)
