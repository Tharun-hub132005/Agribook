package com.example.agribook.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["phoneOrEmail"], unique = true), Index(value = ["username"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val fullName: String,
    val phoneOrEmail: String,
    val passwordHash: String,
    val salt: String,
    val languageCode: String = "en",
    val location: String = "Guntur, Andhra Pradesh",
    val village: String = "Angalakuduru",
    val mandal: String = "Tenali",
    val district: String = "Guntur",
    val state: String = "Andhra Pradesh",
    val latitude: Double = 16.24,
    val longitude: Double = 80.64,
    val primaryCrops: String = "Paddy, Cotton, Chili",
    val farmSizeAcres: Double = 5.0,
    val soilType: String = "Black Cotton Soil",
    val irrigationSource: String = "Canal & Borewell",
    val farmingInterests: String = "Organic Farming, SRI Technique, Bio-fertilizers, Drip Irrigation",
    val bio: String = "Passionate organic farmer dedicated to sustainable agriculture.",
    val avatarUrl: String = "",
    val role: String = "FARMER", // FARMER, EXPERT, ADMIN
    val isBanned: Boolean = false,
    val whoCanMessage: String = "ALL", // ALL, CONNECTIONS
    val profileVisibility: String = "PUBLIC", // PUBLIC, CONNECTIONS
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "crop_details",
    indices = [Index(value = ["farmerId"])]
)
data class CropDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val farmerId: Long,
    val cropName: String,
    val variety: String = "",
    val season: String = "Kharif", // Kharif, Rabi, Zaid
    val areaAcres: Double = 2.0,
    val sowingDate: String = "",
    val stage: String = "Vegetative", // Sown, Vegetative, Flowering, Maturity, Harvested
    val soilHealthNotes: String = "",
    val expectedYieldQuintals: Double = 20.0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "posts",
    indices = [Index(value = ["cropTag"]), Index(value = ["authorId"])]
)
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorId: Long,
    val authorName: String,
    val authorLocation: String,
    val authorCrop: String,
    val authorRole: String = "FARMER",
    val content: String,
    val cropTag: String, // Paddy, Cotton, Chili, Wheat, Sugarcane, Organic, Soil & Fertilizer, Pest Control
    val mediaType: String = "NONE", // NONE, IMAGE, VIDEO
    val mediaResName: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val bookmarksCount: Int = 0,
    val isFlagged: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "comments",
    indices = [Index(value = ["postId"])]
)
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val postId: Long,
    val authorId: Long,
    val authorName: String,
    val authorRole: String = "FARMER",
    val text: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "post_likes",
    primaryKeys = ["postId", "userId"]
)
data class PostLikeEntity(
    val postId: Long,
    val userId: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "bookmarks",
    primaryKeys = ["postId", "userId"]
)
data class BookmarkEntity(
    val postId: Long,
    val userId: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "connections",
    indices = [Index(value = ["requesterId", "receiverId"], unique = true)]
)
data class ConnectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val requesterId: Long,
    val receiverId: Long,
    val status: String = "PENDING", // PENDING, ACCEPTED, REJECTED
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "blocked_users",
    primaryKeys = ["blockerId", "blockedId"]
)
data class BlockEntity(
    val blockerId: Long,
    val blockedId: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "messages",
    indices = [Index(value = ["senderId"]), Index(value = ["receiverId"])]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderId: Long,
    val receiverId: Long,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(
    tableName = "notifications",
    indices = [Index(value = ["userId"])]
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val type: String, // LIKE, COMMENT, CONNECTION_REQUEST, MESSAGE, MODERATION
    val title: String,
    val body: String,
    val relatedId: Long = 0,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reporterId: Long,
    val reporterName: String,
    val targetType: String, // POST, USER
    val targetId: Long,
    val targetSummary: String,
    val reason: String,
    val status: String = "OPEN", // OPEN, RESOLVED, DISMISSED
    val createdAt: Long = System.currentTimeMillis()
)
