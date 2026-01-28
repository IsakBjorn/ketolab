package no.ibear.ketolab.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import sh.ory.keto.ApiClient
import sh.ory.keto.api.PermissionApi
import sh.ory.keto.api.RelationshipApi

@Configuration
class KetoConfig(
    @Value("\${keto.read.url}") private val readUrl: String,
    @Value("\${keto.write.url}") private val writeUrl: String
) {

    @Bean
    fun permissionApi(): PermissionApi {
        val client = ApiClient()
        client.basePath = readUrl
        return PermissionApi(client)
    }

    @Bean
    fun relationshipApi(): RelationshipApi {
        val client = ApiClient()
        client.basePath = writeUrl
        return RelationshipApi(client)
    }
}
