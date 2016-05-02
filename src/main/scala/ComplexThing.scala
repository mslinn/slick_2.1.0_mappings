case class Blarg(
  x: String,
  y: Int
)

case class ComplexThing(
  one: String,
  two: Int,
  blarg: Blarg,
  id: Option[Long] = None
)
