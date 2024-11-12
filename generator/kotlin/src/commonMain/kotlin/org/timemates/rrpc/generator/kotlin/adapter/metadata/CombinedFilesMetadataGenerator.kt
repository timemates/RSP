package org.timemates.rrpc.generator.kotlin.adapter.metadata

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent
import org.timemates.rrpc.codegen.typemodel.LibClassNames
import org.timemates.rrpc.common.schema.RSResolver
import org.timemates.rrpc.generator.kotlin.adapter.internal.ext.newline
import kotlin.random.Random

public object CombinedFilesMetadataGenerator {
    public fun generate(
        name: String?,
        scoped: Boolean,
        resolver: RSResolver
    ): FileSpec {
        val isPrivate = name == null
        // assign random value if name wasn't provided
        val name = name ?: "MetadataLookupGroup${Random.nextInt(0, 999999)}"

        return FileSpec.builder(ClassName("org.timemates.rrpc.generated", name))
            .addType(
                TypeSpec.objectBuilder(name)
                    .addSuperinterface(
                        superinterface = LibClassNames.RM.Server.MetadataLookup,
                        delegate = buildCodeBlock {
                            add("RMResolver(")
                            withIndent {
                                resolver.resolveAvailableFiles().forEach { file ->
                                    newline()
                                    add(FileMetadataGenerator.generate(file, resolver))
                                    add(",")
                                }
                            }
                            addStatement(")")
                        }
                    )
                    .apply {
                        if (!scoped) {
                            addInitializerBlock(buildCodeBlock {
                                add("%T.register(this)", LibClassNames.RM.Server.MetadataLookup.nestedClass("Global"))
                            })
                        }

                        if (isPrivate)
                            addModifiers(KModifier.PRIVATE)
                    }
                    .build()
            )
            .build()
    }
}