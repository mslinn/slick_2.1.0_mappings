import slick.driver.PostgresDriver.simple._

class OAuthProviders1(tag: Tag) extends Table[OAuthProvider](tag, "oauthProvider") {
  def email    = column[String]("email")
  def provider = column[String]("provider")
  def userId   = column[String]("userid")
  def doodle   = column[InnerDoodle]("doodle")
  def id       = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  private type OAuthProviderTupleType = (String, String, String, InnerDoodle, Option[Long])

  private val toModel: OAuthProviderTupleType => OAuthProvider = { tuple =>
    OAuthProvider(
      email    = tuple._1,
      provider = tuple._2,
      userId   = tuple._3,
      doodle   = tuple._4,
      id       = tuple._5
  )}

  private val toTuple: OAuthProvider => Option[OAuthProviderTupleType] = { oap =>
    Some((
      oap.email,
      oap.provider,
      oap.userId,
      oap.doodle,
      oap.id
    ))
  }

  def * = (
    email, provider, userId, doodle, id
  ) <> (toModel, toTuple)
}

object DB {
  val Logger = org.slf4j.LoggerFactory.getLogger("db")

  def db = {
    import slick.jdbc.{StaticQuery => Q}
    import slick.driver.JdbcDriver.backend.Database

    val pgUser: String = sys.env("USERID")
    val pgPassword: String = sys.env("PGPASSWORD")
    val dbName = "oauthTest"
    val database = Database.forURL(s"jdbc:postgresql://localhost:5432/postgres", driver="org.postgresql.Driver", user=pgUser, password=pgPassword)
    database.withSession { implicit session =>
      Q.updateNA(s"drop database if exists $dbName; create database $dbName;").execute
    }
    database
  }
}

object oAuthProviders1 extends TableQuery(new OAuthProviders1(_)) {
  import DB._

  implicit val session = db.createSession

  def apply(email: String, provider: String, userId: String, doodle: InnerDoodle, id: Option[Long]=None) =
    OAuthProvider(email, provider, userId, doodle, id)

  def createTable(): Unit = this.ddl.create

  def deleteTable(): Unit = this.ddl.drop

  def findByUserId(userId: String): Option[OAuthProvider] =
    this.filter(p => p.userId === userId).firstOption

  protected def queryById(id: Long): Query[OAuthProviders1, OAuthProvider, Seq] = filter(_.id === id)

  def findById(id: Long): Option[OAuthProvider] = queryById(id).firstOption

  def insert(oauthProvider: OAuthProvider): Int = oAuthProviders1 += oauthProvider
}

object app1 extends App {
  import DB.Logger

  oAuthProviders1.createTable()
  val id: Int = oAuthProviders1.insert(OAuthProvider("x@y.com", "blah", "blah", InnerDoodle("doodley-doo!")))
  val foundOAP: Option[OAuthProvider] = oAuthProviders1.findById(id)
  Logger.info(s"foundOAP=$foundOAP")
  oAuthProviders1.deleteTable()
}
