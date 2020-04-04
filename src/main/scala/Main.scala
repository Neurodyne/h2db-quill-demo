package h2demo

object Demo extends App {

  for {
    cfg <- H2Config.loadConfig()
    _   = println(s"Starting ${cfg.server.serverType} server")
    _   = H2Server.start(cfg)
    _   = H2Client.work(cfg)
  } yield ()

}
