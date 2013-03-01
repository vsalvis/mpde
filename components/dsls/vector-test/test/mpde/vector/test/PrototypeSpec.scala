package mpde.vector.test

import org.scalatest._
import collection.mutable.Stack
import dsl.la.norep._
import dsl.la._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PrototypeSpec extends FlatSpec with ShouldMatchers {

  private[this] val pfield = 1
  val field = 1
  var vfield = 1
  def method = 1
  def paramMethod(i: Int) = i

  "A shallow embedding of la" should "produce values" in {
    val x = /*laLift*/ {
      val v1 = DenseVector(1, 2, 3)
      (v1 + (DenseVector(3, 4, 5) * SparseVector[Int](6, 7, 8)): Vector[Int]).map(_ + 1)
    }

    x should equal(DenseVector(20, 31, 44))
  }

  it should "lift only fields, functions, and variables that are not bound in the DSL scope" in {
    import dsl.la._
    def y(i: Int) = { 1 }
    def function = 1
    val (t1, t2) = (1, 2)

    val dslResult = laLift {
      //      This does not work for now!!
      //      pfield
      //      field
      //      vfield
      //      method
      //      y(1) // we can leave this out for now
      def m = 1
      val in = 1
      in
      m
      function
      t1
    }
  }

  // Temporarily here until we lift all of the cases below:

  // Block(List(
  // ValDef(Modifiers(), newTermName("y"), TypeTree(),
  //   Apply(Apply(TypeApply(Select(Select(Select(Ident(dsl), dsl.la), dsl.la.DenseVector), newTermName("apply")), List(TypeTree())), List(Literal(Constant(1)))), List(Select(Select(This(newTypeName("math")), scala.math.Numeric), scala.math.Numeric.IntIsIntegral), Select(Ident(scala.reflect.ClassTag), newTermName("Int")))))
  // Import(Ident(dsl), List(ImportSelector(newTermName("la"), 1324, newTermName("la"), 1324)))
  // ValDef(Modifiers(), newTermName("z"), TypeTree(),
  //   Apply(Apply(TypeApply(Select(Select(Select(Ident(dsl), dsl.la), dsl.la.DenseVector), newTermName("apply")), List(TypeTree())), List(Literal(Constant(1)))), List(Select(Select(This(newTypeName("math")), scala.math.Numeric), scala.math.Numeric.IntIsIntegral), Select(Ident(scala.reflect.ClassTag), newTermName("Int")))))
  // Import(Select(Ident(dsl), dsl.la), List(ImportSelector(newTermName("DenseVector"), 1381, newTermName("DenseVector"), 1381)))
  // ValDef(Modifiers(), newTermName("dv"), TypeTree(),
  //   Apply(Apply(TypeApply(Select(Select(Select(Ident(dsl), dsl.la), dsl.la.DenseVector), newTermName("apply")), List(TypeTree())), List(Literal(Constant(1)))), List(Select(Select(This(newTypeName("math")), scala.math.Numeric), scala.math.Numeric.IntIsIntegral), Select(Ident(scala.reflect.ClassTag), newTermName("Int")))))
  //), Literal(Constant(()))))
  //
  //

  //  Apply(Apply(TypeApply(Select(Select(Select(Ident(dsl), dsl.la), dsl.la.DenseVector), newTermName("apply")), List(TypeTree())), List(Literal(Constant(1)))), List(Select(Select(This(newTypeName("math")), scala.math.Numeric), scala.math.Numeric.IntIsIntegral), Select(Ident(scala.reflect.ClassTag), newTermName("Int")))))
  //  Apply(Apply(TypeApply(Select(Select(Select(Ident(dsl), dsl.la), dsl.la.DenseVector), newTermName("apply")), List(TypeTree())), List(Literal(Constant(1)))), List(Select(Select(This(newTypeName("math")), scala.math.Numeric), scala.math.Numeric.IntIsIntegral), Select(Ident(scala.reflect.ClassTag), newTermName("Int")))))
  //  Apply(Apply(TypeApply(Select(Select(Select(Ident(dsl), dsl.la), dsl.la.DenseVector), newTermName("apply")), List(TypeTree())), List(Literal(Constant(1)))), List(Select(Select(This(newTypeName("math")), scala.math.Numeric), scala.math.Numeric.IntIsIntegral), Select(Ident(scala.reflect.ClassTag), newTermName("Int")))))

  //  Select(Select(This(newTypeName("$anon")), newTermName("DenseVector")), newTermName("apply"))

  // object out of a cake
  // DenseVector(1,2)
  // Select(Select(Select(Ident(dsl), dsl.la), dsl.la.DenseVector), newTermName("apply"))

  // new VectorDSL { def main = DenseVector(1,2) }
  // Select(Select(This(newTypeName("$anon")), newTermName("DenseVector")), newTermName("apply"))

  // (1,2,3)
  // Select(Select(Ident(scala), scala.Tuple4), newTermName("apply")

  // new Exception()
  // Select(New(Select(Select(Ident(scala), scala.package), newTypeName("Exception"))), nme.CONSTRUCTOR)

//    it should "rewire object applications with our own numeric and class tag" in {
//      val x = dsl.la.laLift {
//        val y = dsl.la.DenseVector(1, 2, 3)
//        import dsl.la
//        val z = la.DenseVector(1, 2, 3)
//        import dsl.la.TestObject
//        val dv = DenseVector(1, 2, 3)
//        ()
//      }
//    }

  it should "rewire language features to methods" in {
    val x = dsl.la.laLift {
      if (true) DenseVector(1, 2, 3) else { DenseVector(1, 2, 3) }
    }
    ()
  }

  it should "closures should be converted into a cake" in {
    val x = dsl.la.laLift {
      //      new VectorDSL {
      //        def main = {
      //          DenseVector(liftTerm(1)).map(x => x + 1)
      //        }
      //      }
      //            dsl.la.DenseVector(1,2,3).map(x => x + 1)
    }
    ()
  }

  it should "rewire assignments, definition of vars and vals and return statements" in {
    val x = dsl.la.laLift {
      def main: Int = {
        var a = 3;
        var b = 1;
        b = a + a;
        return b;
      }
      main
    }
    ()
  }

  it should "rewire while loops to imperativeDSL methods" in {
    val x = dsl.la.laLift {
      var b = 0
      while (true) {
        var a = 1
        b = a + a;
      }
      do {
        b = 1
      } while (false)
    }
    ()
  }
  
  it should "not break on shadowing" in {
    val x = dsl.la.laLift {
      val w = 1
      var y = w + 1
      var z = w + y + 3
    }
  }

}

