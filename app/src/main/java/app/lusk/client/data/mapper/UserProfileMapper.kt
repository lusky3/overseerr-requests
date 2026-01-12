package app.lusk.client.data.mapper

import app.lusk.client.data.remote.model.ApiPermissions
import app.lusk.client.data.remote.model.ApiUserProfile
import app.lusk.client.domain.model.Permissions
import app.lusk.client.domain.model.UserProfile

/**
 * Maps API user profile model to domain user profile model.
 */
fun ApiUserProfile.toDomain(): UserProfile {
    return UserProfile(
        id = id,
        email = email,
        displayName = displayName,
        avatar = avatar,
        requestCount = requestCount,
        permissions = permissions.toDomain()
    )
}

/**
 * Maps API permissions model to domain permissions model.
 */
fun ApiPermissions.toDomain(): Permissions {
    return Permissions(
        canRequest = canRequest,
        canManageRequests = canManageRequests,
        canViewRequests = canViewRequests,
        isAdmin = isAdmin
    )
}
