import slick.lifted.MappedTo

case class SimpleThing(
  email: String,
  provider: String,
  userId: String,
  doodle: InnerDoodle,
  id: Option[Long]=None
)

case class InnerDoodle(value: String) extends AnyVal with MappedTo[String] {
  override def toString = value
}
