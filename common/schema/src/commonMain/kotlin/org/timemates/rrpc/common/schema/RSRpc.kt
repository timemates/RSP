package org.timemates.rrpc.common.schema

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import org.timemates.rrpc.common.schema.annotations.NonPlatformSpecificAccess
import org.timemates.rrpc.common.schema.value.RMDeclarationUrl

@Serializable
public class RSRpc(
    /**
     * The name of RPC.
     *
     * Marked with [NonPlatformSpecificAccess] because code-generation should
     * adapt the name to the language's naming convention. For example, for Java
     * and Kotlin, it should start with a lowercase letter instead of uppercase as in the
     * ProtoBuf naming convention. If we generate some kind of metadata to the service, it's
     * absolutely legal and required to keep name as in original `.proto` definition, otherwise
     * it may break cross-language support.
     *
     * @see languageSpecificName
     */
    @ProtoNumber(1)
    @NonPlatformSpecificAccess
    public val name: String,

    /**
     * Denotes the input of RPC that comes from client to server. The type
     * might be a stream, make sure you made a check.
     */
    @ProtoNumber(2)
    public val requestType: StreamableRMTypeUrl,

    /**
     * Denotes the out of RPC that comes from server to client. The type
     * might be a stream, make sure you made a check.
     */
    @ProtoNumber(3)
    public val responseType: StreamableRMTypeUrl,

    /**
     * The options that are specified for given RPC.
     */
    @ProtoNumber(4)
    public val options: RSOptions,
    @ProtoNumber(5)
    override val documentation: String?,
) : RSNode, Documentable {
    public fun languageSpecificName(language: Language): String {
        @OptIn(NonPlatformSpecificAccess::class)
        return when (language) {
            Language.JAVA, Language.KOTLIN, Language.PHP, Language.PYTHON ->
                name.replaceFirstChar { it.lowercase() }
            else -> TODO()
        }
    }
}

public fun RSRpc.javaName(): String = languageSpecificName(Language.JAVA)
public fun RSRpc.kotlinName(): String = languageSpecificName(Language.KOTLIN)

public val RSRpc.isRequestResponse: Boolean get() = !requestType.isStreaming && !responseType.isStreaming
public val RSRpc.isRequestStream: Boolean get() = !requestType.isStreaming && responseType.isStreaming
public val RSRpc.isRequestChannel: Boolean get() = requestType.isStreaming && responseType.isStreaming
public val RSRpc.isFireAndForget: Boolean
    get() = requestType.type != RMDeclarationUrl.ACK && responseType.type == RMDeclarationUrl.ACK
public val RSRpc.isMetadataPush: Boolean
    get() = requestType.type == RMDeclarationUrl.ACK && responseType.type == RMDeclarationUrl.ACK