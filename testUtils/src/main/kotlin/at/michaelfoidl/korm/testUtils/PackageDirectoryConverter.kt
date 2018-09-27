package at.michaelfoidl.korm.testUtils

object PackageDirectoryConverter {
    fun convertPackageToDirectoryStructure(packageName: String): String {
        return packageName.replace('.', '/')
    }

    fun convertDirectoryStructureToPackage(directoryPath: String): String {
        return directoryPath.replace('/', '.')
    }
}