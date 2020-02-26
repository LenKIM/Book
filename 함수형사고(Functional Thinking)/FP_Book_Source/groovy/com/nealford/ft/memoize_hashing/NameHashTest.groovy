package com.nealford.ft.memoize_hashing

class NameHashTest extends GroovyTestCase {
  void testHash() {
    assertEquals("ubzre", NameHash.hash.call("homer")) }
}
