package org.bblfsh.client.cli

import org.bblfsh.client.BblfshClient

import scala.io.Source

object ScalaClientCLI extends App {
  // XXX path
  System.load("/home/juanjux/sync/work/sourced/client-scala/src/main/scala/org/bblfsh/client/libuast/Libuast.so")

  val cli = new CLI(args)
  if (args.length < 1) {
    println("Usage: [--host <bblfshServerHost> --port <bblfshServerPort>] -f <path to file>")
    cli.printHelp()
    System.exit(1)
  }
  cli.verify()

  val fileName = cli.file().getName
  val client = BblfshClient(cli.bblfshServerHost(), cli.bblfshServerPort())
  val fileContent = Source.fromFile(cli.file()).getLines.mkString("\n")

  val resp = client.parse(fileName, fileContent)

  if (resp.errors.isEmpty) {
    println(resp.uast.get)
  } else {
    println(s"Parsing failed with ${resp.errors.length} errors:")
    resp.errors.foreach(println)
  }

}
