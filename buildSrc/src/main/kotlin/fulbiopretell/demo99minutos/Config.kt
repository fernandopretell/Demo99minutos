package com.fulbiopretell.demo99minutos

object Config {
    const val packageName = "com.fulbiopretell.retobcp"
    const val name = "RetoBCP"
    val isCiServer = System.getenv().containsKey("CI")

    /*object Signing {
        const val env = "key.properties"
        const val storeFile = "store.file"
        const val storePassword = "store.password"
        const val keyAlias = "key.alias"
        const val keyPassword = "key.password"
    }*/

    object Packaging {
        val excludes = arrayOf(
            "LICENSE.txt",
            "META-INF/DEPENDENCIES",
            "META-INF/ASL2.0",
            "META-INF/NOTICE",
            "META-INF/LICENSE",
            "META-INF/core_release.kotlin_module"
        )
    }
}