package com.nealford.ft.flyweight

import org.junit.Test
import static groovy.util.GroovyTestCase.assertTrue

class FlyweightTest {
// BEGIN groovy_flyweight_test (ex6-10)
  @Test
  public void comp_factory() {
    def bob = new AssignedComputer(
      CompFactory.instance.ofType("MacBookPro6_2"), "Bob")
    def steve = new AssignedComputer(
      CompFactory.instance.ofType("MacBookPro6_2"), "Steve")
    assertTrue(bob.computerType == steve.computerType)
  }
// END groovy_flyweight_test

// BEGIN groovy_memoization_of_flyweights (ex6-11)
  def computerOf = {type ->
    def of = [MacBookPro6_2: new Laptop(), SunTower: new Desktop()]
    return of[type]
  }

  def computerOfType = computerOf.memoize()
// END groovy_memoization_of_flyweights

// BEGIN groovy_flyweight_demo (ex6-12)
  @Test
  public void flyweight_computers() {
    def bob = new AssignedComputer(
      ComputerFactory.instance.ofType("MacBookPro6_2"), "Bob")
    def steve = new AssignedComputer(
      ComputerFactory.instance.ofType("MacBookPro6_2"), "Steve")
    assertTrue(bob.computerType == steve.computerType)

    def sally = new AssignedComputer(
      computerOfType("MacBookPro6_2"), "Sally")
    def betty = new AssignedComputer(
      computerOfType("MacBookPro6_2"), "Betty")
    assertTrue sally.computerType == betty.computerType
  }
// END groovy_flyweight_demo
}


// BEGIN groovy_flyweight_defs (ex6-7)
class Computer {
  def type
  def cpu
  def memory
  def hardDrive
  def cd
}

class Desktop extends Computer {
  def driveBays
  def fanWattage
  def videoCard
}

class Laptop extends Computer {
  def usbPorts
  def dockingBay
}

class AssignedComputer {
  def computerType
  def userId

  public AssignedComputer(computerType, userId) {
    this.computerType = computerType
    this.userId = userId
  }
}
// END groovy_flyweight_defs

/*
// BEGIN groovy_computer_factory (ex6-8)
class CompFactory {
  def types = [:]
  static def instance;

  private ComputerFactory() {
    def laptop = new Laptop()
    def tower = new Desktop()
    types.put("MacBookPro6_2", laptop)
    types.put("SunTower",  tower)
  }

  static def getInstance() {
    if (instance == null)
      instance = new CompFactory()
    instance
  }

  def ofType(computer) {
    types[computer]
  }
}
// END groovy_computer_factory
*/


// BEGIN groovy_factory_singleton (ex6-9)
@Singleton(strict=false) class ComputerFactory {
  def types = [:]

  private ComputerFactory() {
    def laptop = new Laptop()
    def tower = new Desktop()
    types.put("MacBookPro6_2", laptop)
    types.put("SunTower",  tower)
  }

  def ofType(computer) {
    types[computer]
  }
}
// END groovy_factory_singleton
