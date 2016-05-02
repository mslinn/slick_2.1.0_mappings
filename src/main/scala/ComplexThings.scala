import slick.driver.PostgresDriver.simple._

class ComplexThings(tag: Tag) extends Table[ComplexThing](tag, "complex_thing") {
  def one = column[String]("one")
  def two = column[Int]("two")
  def id  = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  // blarg fields
  def x   = column[String]("x")
  def y   = column[Int]("y")

  def blarg = (x, y).<>[Blarg, (String, Int)](
    { case (x, y) => Blarg(x, y) },
    { case blarg: Blarg => Some(blarg.x, blarg.y) }
  )

  def * = (one, two, blarg, id) <> (ComplexThing.tupled, ComplexThing.unapply)
}

object complexThings extends TableQuery(new ComplexThings(_)) {
  import DB._

  implicit val session = db.createSession

  def apply(one: String, two: Int, blarg: Blarg, id: Option[Long]=None) = ComplexThing(one, two, blarg, id)

  def createTable(): Unit = this.ddl.create

  def deleteTable(): Unit = this.ddl.drop

  def findById(id: Option[Long]): Option[ComplexThing] =
    this.filter(_.id === id).firstOption

  protected def queryById(id: Long): Query[ComplexThings, ComplexThing, Seq] = filter(_.id === id)

  def findById(id: Long): Option[ComplexThing] = queryById(id).firstOption

  def insert(oauthProvider: ComplexThing): Int = this += oauthProvider
}
