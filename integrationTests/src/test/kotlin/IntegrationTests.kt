import at.michaelfoidl.korm.integrationTests.database.Database
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable

class IntegrationTests {
    @Test
    @DisabledIfEnvironmentVariable(named = "ENV", matches = "gitlab-ci")
    fun creatingNewDatabase_shouldWork() {
        Database().create()
    }
}