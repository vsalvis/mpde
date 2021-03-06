package ch.epfl.yinyang.typetransformers

import scala.reflect.macros.Context

/**
 * Base class for all type transformers.
 */
abstract class TypeTransformer[C <: Context](val c: C) {
  trait TypeContext
  case object TypeApplyCtx extends TypeContext
  case object OtherCtx extends TypeContext

  def rewiredToThis(s: String) = s == "package" || s == "Predef" // TODO DRY
  def transform(ctx: TypeContext, t: c.universe.Type): c.universe.Tree
  var className: String = _
}