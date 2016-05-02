import slick.driver.PostgresDriver.simple._

class _SimpleThings(tag: Tag) extends Table[SimpleThing](tag, "simple_thing") {
  def email    = column[String]("email")
  def provider = column[String]("provider")
  def userId   = column[String]("userid")
  def doodle   = column[InnerDoodle]("doodle")
  def id       = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def * = (email, provider, userId, doodle, id) <> (SimpleThing.tupled, SimpleThing.unapply)
}

object SimpleThings extends TableQuery(new _SimpleThings(_)) {
  import DB._

  implicit val session = db.createSession

  def apply(email: String, provider: String, userId: String, doodle: InnerDoodle, id: Option[Long]=None) =
    SimpleThing(email, provider, userId, doodle, id)

  def createTable(): Unit = this.ddl.create

  def deleteTable(): Unit = this.ddl.drop

  def findByUserId(userId: String): Option[SimpleThing] =
    this.filter(_.userId === userId).firstOption

  protected def queryById(id: Long): Query[_SimpleThings, SimpleThing, Seq] = filter(_.id === id)

  def findById(id: Long): Option[SimpleThing] = queryById(id).firstOption

  def insert(oauthProvider: SimpleThing): Int = this += oauthProvider
}
