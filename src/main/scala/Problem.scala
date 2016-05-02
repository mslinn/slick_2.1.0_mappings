import slick.driver.PostgresDriver.simple._
import slick.lifted.{Column, ProvenShape}

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
  val db = Database.forURL("jdbc:postgresql:localhost:5432/slinnbooks", driver = "org.postgresql.Driver")
  implicit val session = db.createSession

  val Logger = org.slf4j.LoggerFactory.getLogger("db")

  def createTable(): Unit = this.ddl.create

  def apply(email: String, provider: String, userId: String, id: Option[Long]=None) =
    OAuthProvider(email, provider, userId, id)

  def findByUserId(userId: Long): Option[OAuthProvider] =
    this.filter(p => p.provider === userId && p.userId === userId).firstOption

  protected def queryById(id: Long): Query[OAuthProviders, OAuthProvider, Seq] = filter(_.id === id)

  def findById(id: Long): Option[OAuthProvider] = queryById(id).firstOption
}

