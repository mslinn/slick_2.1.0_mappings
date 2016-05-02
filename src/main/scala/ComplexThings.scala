import slick.driver.PostgresDriver.simple._

class _ComplexThings(tag: Tag) extends Table[ComplexThing](tag, "complex_thing") {
  def one = column[String]("one")
  def two = column[Int]("two")
  def id  = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  // Blarg fields
  def x   = column[String]("x")
  def y   = column[Int]("y")

  // Blarg sub-projection mappings done the hard way:
  /*def blarg = (x, y).<>[Blarg, (String, Int)](
    { case tuple@(x, y) => Blarg.tupled(tuple) },
    { case blarg: Blarg => Blarg.unapply(blarg) }
  )*/

  // the easy way:
  def blarg = (x, y) <> (Blarg.tupled, Blarg.unapply)

  def * = (one, two, blarg, id) <> (ComplexThing.tupled, ComplexThing.unapply)
}

object ComplexThings extends TableQuery(new _ComplexThings(_)) {
  import DB._

  implicit val session = db.createSession

  def apply(one: String, two: Int, blarg: Blarg, id: Option[Long]=None) = ComplexThing(one, two, blarg, id)

  def createTable(): Unit = this.ddl.create

  def deleteTable(): Unit = this.ddl.drop

  def findById(id: Option[Long]): Option[ComplexThing] =
    this.filter(_.id === id).firstOption

  protected def queryById(id: Long): Query[_ComplexThings, ComplexThing, Seq] = filter(_.id === id)

  def findById(id: Long): Option[ComplexThing] = queryById(id).firstOption

  def insert(oauthProvider: ComplexThing): Int = this += oauthProvider
}
