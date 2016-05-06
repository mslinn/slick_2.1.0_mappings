case class Blarg(
  x: String,
  y: Int
)

case class ComplexThing(
  one: String,
  maybeInt: Option[Int],
  blarg: Blarg,
  id: Option[Long] = None
)
