package dsl.print

import ch.epfl.lamp.mpde._
import scala.language.experimental.macros
import scala.reflect.macros.Context
import scala.tools.reflect.ToolBoxFactory

object `package` {

  def liftPrint[T](block: ⇒ T): T = macro _liftPrint[T]
  def liftPrintDebug[T](block: ⇒ T): T = macro _liftPrintDebug[T]
  def _liftPrint[T](c: Context)(block: c.Expr[T]): c.Expr[T] =
    new MPDETransformer[c.type, T](c, "dsl.print.PrintDSL")(block)
  def _liftPrintDebug[T](c: Context)(block: c.Expr[T]): c.Expr[T] =
    new MPDETransformer[c.type, T](c, "dsl.print.PrintDSL", true)(block)

  // The only thing we declare here
  def println(x: Any) = ???

}

