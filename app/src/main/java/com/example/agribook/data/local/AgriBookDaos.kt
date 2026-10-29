package com.example.agribook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE phoneOrEmail = :query OR username = :query LIMIT 1")
    suspend fun findByPhoneOrEmail(query: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserByIdFlow(id: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id != :currentUserId AND isBanned = 0 ORDER BY fullName ASC")
    fun getAllFarmersFlow(currentUserId: Long): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id != :currentUserId AND isBanned = 0 AND (fullName LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%' OR primaryCrops LIKE '%' || :query || '%')")
    fun searchFarmersFlow(currentUserId: Long, query: String): Flow<List<UserEntity>>

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun countUsers(): Int
}

@Dao
interface PostDao {
    @Insert
    suspend fun insertPost(post: PostEntity): Long

    @Query("SELECT * FROM posts WHERE isFlagged = 0 ORDER BY createdAt DESC")
    fun getAllPostsFlow(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE isFlagged = 0 AND cropTag = :cropTag ORDER BY createdAt DESC")
    fun getPostsByCropFlow(cropTag: String): Flow<List<PostEntity>>

    @Query("SELECT p.* FROM posts p INNER JOIN bookmarks b ON p.id = b.postId WHERE b.userId = :userId AND p.isFlagged = 0 ORDER BY p.createdAt DESC")
    fun getBookmarkedPostsFlow(userId: Long): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE authorId = :authorId ORDER BY createdAt DESC")
    fun getPostsByAuthorFlow(authorId: Long): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Long): PostEntity?

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deletePost(id: Long)

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun countPosts(): Int
}

@Dao
interface InteractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: PostLikeEntity)

    @Query("DELETE FROM post_likes WHERE postId = :postId AND userId = :userId")
    suspend fun removeLike(postId: Long, userId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    fun hasLikedFlow(postId: Long, userId: Long): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    suspend fun hasLiked(postId: Long, userId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE postId = :postId AND userId = :userId")
    suspend fun removeBookmark(postId: Long, userId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE postId = :postId AND userId = :userId)")
    fun isBookmarkedFlow(postId: Long, userId: Long): Flow<Boolean>

    @Insert
    suspend fun insertComment(comment: CommentEntity): Long

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY createdAt ASC")
    fun getCommentsForPostFlow(postId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlock(block: BlockEntity)

    @Query("DELETE FROM blocked_users WHERE blockerId = :blockerId AND blockedId = :blockedId")
    suspend fun removeBlock(blockerId: Long, blockedId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM blocked_users WHERE blockerId = :blockerId AND blockedId = :blockedId)")
    suspend fun isBlocked(blockerId: Long, blockedId: Long): Boolean

    @Query("SELECT blockedId FROM blocked_users WHERE blockerId = :blockerId")
    fun getBlockedUserIdsFlow(blockerId: Long): Flow<List<Long>>
}

@Dao
interface ConnectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConnection(conn: ConnectionEntity): Long

    @Query("UPDATE connections SET status = :status WHERE id = :id")
    suspend fun updateConnectionStatus(id: Long, status: String)

    @Query("SELECT * FROM connections WHERE (requesterId = :userId OR receiverId = :userId) AND status = 'ACCEPTED'")
    fun getAcceptedConnectionsFlow(userId: Long): Flow<List<ConnectionEntity>>

    @Query("SELECT * FROM connections WHERE receiverId = :userId AND status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingRequestsFlow(userId: Long): Flow<List<ConnectionEntity>>

    @Query("SELECT * FROM connections WHERE (requesterId = :u1 AND receiverId = :u2) OR (requesterId = :u2 AND receiverId = :u1) LIMIT 1")
    suspend fun findConnectionBetween(u1: Long, u2: Long): ConnectionEntity?

    @Query("SELECT * FROM connections WHERE (requesterId = :u1 AND receiverId = :u2) OR (requesterId = :u2 AND receiverId = :u1) LIMIT 1")
    fun getConnectionBetweenFlow(u1: Long, u2: Long): Flow<ConnectionEntity?>
}

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntity): Long

    @Query("SELECT * FROM messages WHERE (senderId = :u1 AND receiverId = :u2) OR (senderId = :u2 AND receiverId = :u1) ORDER BY timestamp ASC")
    fun getConversationFlow(u1: Long, u2: Long): Flow<List<MessageEntity>>

    @Query("UPDATE messages SET isRead = 1 WHERE senderId = :senderId AND receiverId = :receiverId")
    suspend fun markConversationRead(senderId: Long, receiverId: Long)

    @Query("SELECT COUNT(*) FROM messages WHERE receiverId = :userId AND isRead = 0")
    fun getUnreadMessageCountFlow(userId: Long): Flow<Int>
}

@Dao
interface NotificationDao {
    @Insert
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsFlow(userId: Long): Flow<List<NotificationEntity>>

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: Long)

    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    fun getUnreadNotificationCountFlow(userId: Long): Flow<Int>
}

@Dao
interface ReportDao {
    @Insert
    suspend fun insertReport(report: ReportEntity): Long

    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    fun getAllReportsFlow(): Flow<List<ReportEntity>>

    @Query("UPDATE reports SET status = :status WHERE id = :id")
    suspend fun updateReportStatus(id: Long, status: String)

    @Query("DELETE FROM reports WHERE id = :id")
    suspend fun deleteReport(id: Long)
}

@Dao
interface CropDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrop(crop: CropDetailEntity): Long

    @Query("SELECT * FROM crop_details WHERE farmerId = :farmerId ORDER BY createdAt DESC")
    fun getCropsForFarmerFlow(farmerId: Long): Flow<List<CropDetailEntity>>

    @Query("DELETE FROM crop_details WHERE id = :cropId")
    suspend fun deleteCrop(cropId: Long)

    @Update
    suspend fun updateCrop(crop: CropDetailEntity)
}
