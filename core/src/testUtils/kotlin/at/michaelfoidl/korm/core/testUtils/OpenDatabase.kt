package at.michaelfoidl.korm.core.testUtils

import at.michaelfoidl.korm.core.Database

class OpenDatabase : Database("", "") {
    public override fun doConnect(connectionString: String, driver: String, user: String, password: String) {
        super.doConnect(connectionString, driver, user, password)
    }
}