import slick.driver.PostgresDriver.simple._

class _OptionalComplexThings(tag: Tag) extends Table[OptionalComplexThing](tag, "optional_complex_thing") {
  def one      = column[String]("one")
  def maybeInt = column[Option[Int]]("maybe_int") // specifying O.Nullable makes no difference
  def id       = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  // Blarg fields need to have their lifted types remapped according to MappedProjection usage.
  // Notice OptionalComplexThing's maybeBlarg property is an Option[Blarg].
  def x   = column[String]("x")  // want to map to column[Option[String]]("x")
  def y   = column[Int]("y")     // want to map to column[Option[String]]("y")

  // Blarg sub-projection mapping done the hard way:
  /*def blarg = (x, y).<>[Blarg, (String, Int)](
    { case tuple@(x, y) => Blarg.tupled(tuple) },
    { case blarg: Blarg => Blarg.unapply(blarg) }
  )*/

  // The easy way to specify a Blarg sub-projection mapping:
  def blarg = (x, y) <> (Blarg.tupled, Blarg.unapply)

  // I want to be able to transform the blarg MappedProjection so all its Columns are Options.
  // I don't want to hard-code this example - there should be a generic way to do this using combinators.
  // FIXME no idea how to make this work!
  def * = (one, maybeInt, Option(blarg), id) <> (OptionalComplexThing.tupled, OptionalComplexThing.unapply)
  /* Error:(23, 46) No matching Shape found.
  Slick does not know how to map the given types.
  Possible causes: T in Table[T] does not match your * projection. Or you use an unsupported type in a Query (e.g. scala List).
    Required level: scala.slick.lifted.FlatShapeLevel
       Source type: (scala.slick.lifted.Column[String], scala.slick.lifted.Column[Option[Int]], Option[scala.slick.lifted.MappedProjection[Blarg,(String, Int)]], scala.slick.lifted.Column[Option[Long]])
     Unpacked type: (String, Option[Int], Option[Blarg], Option[Long])
       Packed type: Any
    def * = (one, maybeInt, Option(blarg), id) <> (OptionalComplexThing.tupled, OptionalComplexThing.unapply)
                                               ^ */
}

object OptionalComplexThings extends TableQuery(new _OptionalComplexThings(_)) {
  import DB._

  implicit val session = db.createSession

  def apply(one: String, two: Option[Int], maybeBlarg: Option[Blarg], id: Option[Long]=None) =
    OptionalComplexThing(one, two, maybeBlarg, id)

  def createTable(): Unit = this.ddl.create

  def deleteTable(): Unit = this.ddl.drop

  def findAll: List[OptionalComplexThing] = OptionalComplexThings.list

  def findById(id: Option[Long]): Option[OptionalComplexThing] =
    OptionalComplexThings.filter(_.id === id).firstOption

  def findById(id: Long): Option[OptionalComplexThing] = queryById(id).firstOption

  /** Compares Option[Int] with Column[Option[Int]] and does the right thing */
  def findByMaybeInt(maybeInt: Option[Int]): List[OptionalComplexThing] =
    OptionalComplexThings.filter(_.maybeInt === maybeInt).list

  /** Compares Int with Column[Option[Int]] and does the right thing */
  def findByMaybeInt2(int: Int): List[OptionalComplexThing] =
    OptionalComplexThings.filter(_.maybeInt === int).list

  protected def queryById(id: Long): Query[_OptionalComplexThings, OptionalComplexThing, Seq] =
    OptionalComplexThings.filter(_.id === id)

  def insert(oauthProvider: OptionalComplexThing): Int = this.insert(oauthProvider)
}
