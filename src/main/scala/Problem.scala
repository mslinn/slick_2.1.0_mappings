import slick.driver.PostgresDriver.simple._
import slick.lifted.ProvenShape

case class OAuthProvider(
  email: String,
  provider: String,
  userId: String,
  id: Option[Long]=None
)

class OAuthProviders(tag: Tag) extends Table[OAuthProvider](tag, "oauthProvider") {
  def email    = column[String]("email")
  def provider = column[String]("provider")
  def userId   = column[String]("userid")
  def id       = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def * : ProvenShape[(String, String, String, Option[Long])] = (email, provider, userId, id)
}

object oAuthProviders extends TableQuery(new OAuthProviders(_)) {
  val Logger = org.slf4j.LoggerFactory.getLogger("db")
  val db = Database.forURL("jdbc:postgresql:localhost:5432/slinnbooks", driver = "org.postgresql.Driver")
  implicit val session = db.createSession

  def createTable(): Unit = this.ddl.create

  def apply(email: String, provider: String, userId: String, id: Option[Long]=None) =
    OAuthProvider(email, provider, userId, id)

  def findByUserId(userId: String): Option[OAuthProvider] =
    this.filter(p => p.userId === userId).firstOption

  protected def queryById(id: Long): Query[OAuthProviders, OAuthProvider, Seq] = filter(_.id === id)

  def findById(id: Long): Option[OAuthProvider] = queryById(id).firstOption

  def insert(oauthProvider: OAuthProvider): Int = {
    oAuthProviders += OAuthProvider("x@y.com", "blah", "blah")
  }
}

object app extends App {
  import oAuthProviders.Logger

  oAuthProviders.createTable()
  val id: Int = oAuthProviders.insert(OAuthProvider("x@y.com", "blah", "blah"))
  oAuthProviders.findById(id).map { oAuthProvider =>
    Logger.info(s"Found $oAuthProvider")
    oAuthProvider
  }.orElse {
    Logger.info("Did not find the OAuthProvider")
    None
  }
}
