package com.nealford.ft.template

class CustomerBlocksWithProtection {
    def plan, checkCredit, checkInventory, ship

    def CustomerBlocksWithProtection() {
        plan = []
    }
// BEGIN groovy_customer_blocks
def process() {
  checkCredit?.call()
  checkInventory?.call()
  ship?.call()
}
// END groovy_customer_blocks
}
