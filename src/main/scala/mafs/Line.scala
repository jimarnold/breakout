package mafs

class Line(var p1: Vector, var p2: Vector) {

}

object Line {
  // Given three colinear points p, q, r, the function checks if
  // point q lies on line segment 'pr'
  def onSegment(p: Vector, q: Vector, r: Vector): Boolean = {
    q.x <= Math.max(p.x, r.x) &&
      q.x >= Math.min(p.x, r.x) &&
      q.y <= Math.max(p.y, r.y) &&
      q.y >= Math.min(p.y, r.y)
  }

  // To find orientation of ordered triplet (p, q, r).
  // The function returns following values
  // 0 --> p, q and r are colinear
  // 1 --> Clockwise
  // 2 --> Counterclockwise
  def orientation(p: Vector, q: Vector, r: Vector): Int = {
    val orientation = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y)
    if (orientation == 0) 0 // colinear
    else if (orientation > 0) 1
    else 2 // clock or counterclock wise
  }

  def doIntersect(l1: Line, l2: Line): Boolean = {
    doIntersect(l1.p1, l1.p2, l2.p1, l2.p2)
  }

  // The main function that returns true if line segment 'p1q1'
  // and 'p2q2' intersect.
  def doIntersect(p1: Vector, q1: Vector, p2: Vector, q2: Vector): Boolean = {
    // Find the four orientations needed for general and special cases
    val o1 = orientation(p1, q1, p2)
    val o2 = orientation(p1, q1, q2)
    val o3 = orientation(p2, q2, p1)
    val o4 = orientation(p2, q2, q1)
    // General case
    if (o1 != o2 && o3 != o4) return true
    // Special Cases
    // p1, q1 and p2 are colinear and p2 lies on segment p1q1
    if (o1 == 0 && onSegment(p1, p2, q1)) true
    // p1, q1 and q2 are colinear and q2 lies on segment p1q1
    else if (o2 == 0 && onSegment(p1, q2, q1)) true
    // p2, q2 and p1 are colinear and p1 lies on segment p2q2
    else if (o3 == 0 && onSegment(p2, p1, q2)) true
    // p2, q2 and q1 are colinear and q1 lies on segment p2q2
    else if (o4 == 0 && onSegment(p2, q1, q2)) true
    else false // Doesn't fall in any of the above cases
  }
}
