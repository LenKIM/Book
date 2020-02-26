package com.nealford.ft.metafunctionaljava

import org.junit.Test
import static junit.framework.Assert.assertTrue
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertEquals

import fj.data.Stream
import static fj.data.Stream.cons

import com.nealford.ft.number_classifier.NumberClassifier

import static com.nealford.ft.allaboutlists.NumberClassifier.nextPerfectNumberAfter

/**
 * (probably) Copyright 2011 by Neal Ford. All rights reserved.
 */
class IntegerClassifierTest {
  static {
    //// 8-1
    Integer.metaClass.isPerfect = {->
      NumberClassifier.isPerfect(delegate)
    }

    Integer.metaClass.isAbundant = {->
      NumberClassifier.isAbundant(delegate)
    }

    Integer.metaClass.isDeficient = {->
      NumberClassifier.isDeficient(delegate)
    }
  }

  @Test
  void classifier_classifies_correctly() {
    assertTrue NumberClassifier.isPerfect(6)
    assertTrue NumberClassifier.isPerfect(496)
    assertTrue NumberClassifier.isDeficient(7)
    assertTrue NumberClassifier.isAbundant(12)
  }

  @Test
  void metaclass_classifiers() {
    def num = 28
    assertTrue num.isPerfect()
    assertTrue 7.isDeficient()
    assertTrue 6.isPerfect()
    assertTrue 12.isAbundant()
  }

  @Test
  void curry_to_create_classifier() {
    def perfect6 = NumberClassifier.&isPerfect.curry(6)
    def deficient6 = NumberClassifier.&isDeficient.curry(6)
    assertTrue perfect6()
    assertFalse deficient6()
  }

// BEGIN groovy_metafunctional_stream (8-2)
  static {
    Stream.metaClass.filter = { c -> delegate.filter(c as fj.F) }
//    Stream.metaClass.filter = { Closure c -> delegate.filter(c as fj.F) }
    Stream.metaClass.getAt = { n -> delegate.index(n) }
    Stream.metaClass.getAt = { Range r -> r.collect { delegate.index(it) } }
  }

  @Test
  void adding_methods_to_fj_classes() {
    def evens = Stream.range(0).filter { it % 2 == 0 }
    assertTrue(evens.take(5).asList() == [0, 2, 4, 6, 8])
    assertTrue((8..12).collect { evens[it] } == [16, 18, 20, 22, 24])
    assertTrue(evens[3..6] == [6, 8, 10, 12])
  }
// END groovy_metafunctional_stream

  @Test
  void next_perfect_number() {
    assertTrue nextPerfectNumberAfter(1) == 6
    assertTrue nextPerfectNumberAfter(6) == 28
    assertTrue nextPerfectNumberAfter(28) == 496
  }

// BEGIN groovy_add_fj_list (8-4)
  static {
    Stream.metaClass.asList = { delegate.toCollection().asList() }
//    Stream.metaClass.static.cons =
//      { head, Closure c -> delegate.cons(head, ['_1':c] as fj.P1)}
    Stream.metaClass.static.cons =
        { head, closure -> delegate.cons(head, closure as fj.P1) }
  }
// END groovy_add_fj_list

// BEGIN groovy_fj_infinite_perfect (8-5)
  def perfectNumbers(num) {
    cons(nextPerfectNumberAfter(num), { perfectNumbers(nextPerfectNumberAfter(num))})
  }

  @Test
  void infinite_stream_of_perfect_nums_using_funtional_java() {
    assertEquals([6, 28, 496], perfectNumbers(1).take(3).asList())
  }
// END groovy_fj_infinite_perfect

}
