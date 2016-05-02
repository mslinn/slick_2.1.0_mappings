case class OAuthProvider(
  email: String,
  provider: String,
  userId: String,
  id: Option[Long]=None
)
