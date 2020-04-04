package h2demo

object Demo extends App {

  for {
    cfg <- H2Config.loadConfig()
    _   = H2Server.startMemoryServer()
    _   = H2Client.work(cfg)
  } yield ()

}
