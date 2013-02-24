package dsl

import ch.epfl.lamp.mpde._
import scala.language.experimental.macros
import scala.reflect.macros.Context

package object la {

  // TODO (Vojin) should not return Unit but a value
  def laLift[T](block: ⇒ T): Unit = macro implementations.lift[T]
  def laDebug[T](block: ⇒ T): Unit = macro implementations.liftDebug[T]

  def laLiftRep[T](block: ⇒ T): Unit = macro implementations.liftRep[T]
  def laDebugRep[T](block: ⇒ T): Unit = macro implementations.liftDebugRep[T]
  def laLiftNoRep[T](block: ⇒ T): Unit = macro implementations.liftNoRep[T]
  def laDebugNoRep[T](block: ⇒ T): Unit = macro implementations.liftDebugNoRep[T]

  object implementations {
    def lift[T](c: Context)(block: c.Expr[T]): c.Expr[T] = new MPDETransformer[c.type, T](c, "dsl.la.norep.VectorDSL")(block)
    def liftDebug[T](c: Context)(block: c.Expr[T]): c.Expr[T] = new MPDETransformer[c.type, T](c, "dsl.la.norep.VectorDSL", debug = true)(block)

    def liftRep[T](c: Context)(block: c.Expr[T]): c.Expr[T] = new MPDETransformer[c.type, T](c, "dsl.la.rep.VectorDSL", debug = false, rep = true)(block)
    def liftDebugRep[T](c: Context)(block: c.Expr[T]): c.Expr[T] = new MPDETransformer[c.type, T](c, "dsl.la.rep.VectorDSL", debug = true, rep = true)(block)
    def liftNoRep[T](c: Context)(block: c.Expr[T]): c.Expr[T] = new MPDETransformer[c.type, T](c, "dsl.la.norep.VectorDSL", debug = false, rep = false)(block)
    def liftDebugNoRep[T](c: Context)(block: c.Expr[T]): c.Expr[T] = new MPDETransformer[c.type, T](c, "dsl.la.norep.VectorDSL", debug = true, rep = false)(block)
  }

  //  // TODO (Vojin) should not return Unit but a value
  //  def laLift[T](block: ⇒ T): Unit = macro implementations.lift[T]
  //  def laDebug[T](block: ⇒ T): Unit = macro implementations.liftDebug[T]
  //
  //  def laLiftRep[T](block: ⇒ T, isRep: Boolean = false): Unit = macro implementations.liftDSL[T]
  //  def laDebugRep[T](block: ⇒ T, isRep: Boolean = false): Unit = macro implementations.liftDebugDSL[T]
  //
  //  def laLiftNoRep[T](block: ⇒ T, isRep: Boolean = true): Unit = macro implementations.liftDSL[T]
  //  def laDebugNoRep[T](block: ⇒ T, isRep: Boolean = true): Unit = macro implementations.liftDebugDSL[T]
  //
  //  object implementations {
  //    def lift[T](c: Context)(block: c.Expr[T]): c.Expr[T] = new MPDETransformer[c.type, T](c, "dsl.la.norep.VectorDSL")(block)
  //    def liftDebug[T](c: Context)(block: c.Expr[T]): c.Expr[T] = new MPDETransformer[c.type, T](c, "dsl.la.norep.VectorDSL", debug = true)(block)
  //
  //    def liftDSL[T](c: Context)(block: c.Expr[T], isRep: c.Expr[Boolean]): c.Expr[T] =
  //      new MPDETransformer[c.type, T](c, "dsl.la.rep.VectorDSL", debug = false, rep = isRep.splice)(block)
  //    def liftDebugDSL[T](c: Context)(block: c.Expr[T], isRep: c.Expr[Boolean]): c.Expr[T] =
  //      new MPDETransformer[c.type, T](c, "dsl.la.rep.VectorDSL", debug = true, rep = isRep.splice)(block)
  //  }
}
