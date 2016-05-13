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

case class OptionalComplexThing(
  one: String,
  maybeInt: Option[Int],
  maybeBlarg: Option[Blarg],
  id: Option[Long] = None
)
