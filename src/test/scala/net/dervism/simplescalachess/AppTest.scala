package net.dervism.simplescalachess

import org.junit._
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AppTest extends FunSuite with ShouldMatchers {

    test("junit test method") {
      true should equal (true)
    }
    
}


