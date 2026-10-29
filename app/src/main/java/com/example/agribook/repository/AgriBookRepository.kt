package com.example.agribook.repository

import com.example.agribook.data.local.*
import com.example.agribook.security.SecurityService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AgriBookRepository(private val database: AgriBookDatabase) {
    val userDao = database.userDao()
    val postDao = database.postDao()
    val interactionDao = database.interactionDao()
    val connectionDao = database.connectionDao()
    val messageDao = database.messageDao()
    val notificationDao = database.notificationDao()
    val reportDao = database.reportDao()
    val cropDetailDao = database.cropDetailDao()

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        if (userDao.countUsers() == 0) {
            // Seed Admin User
            val adminSalt = SecurityService.generateSalt()
            val adminHash = SecurityService.hashPassword("admin123", adminSalt)
            val adminId = userDao.insertUser(
                UserEntity(
                    username = "agri_admin",
                    fullName = "Dr. K. Swaminathan (Agronomy Director)",
                    phoneOrEmail = "admin@agribook.org",
                    passwordHash = adminHash,
                    salt = adminSalt,
                    languageCode = "en",
                    location = "Patancheru, Sangareddy, Telangana",
                    village = "Patancheru",
                    mandal = "Ramachandrapuram",
                    district = "Sangareddy",
                    state = "Telangana",
                    latitude = 17.51,
                    longitude = 78.27,
                    primaryCrops = "All Crops, Soil Health, IPM",
                    farmSizeAcres = 25.0,
                    soilType = "Red Sandy Loam",
                    irrigationSource = "Canal & Treated Watershed",
                    farmingInterests = "Integrated Pest Management, Watershed Development, Soil Health",
                    bio = "Senior agricultural scientist and community moderator at Agribook. Helping farmers adopt sustainable practices.",
                    role = "ADMIN"
                )
            )

            // Seed Expert User
            val expertSalt = SecurityService.generateSalt()
            val expertHash = SecurityService.hashPassword("expert123", expertSalt)
            val expertId = userDao.insertUser(
                UserEntity(
                    username = "prof_ananya",
                    fullName = "Prof. Ananya Rao",
                    phoneOrEmail = "ananya@agri.edu",
                    passwordHash = expertHash,
                    salt = expertSalt,
                    languageCode = "te",
                    location = "Lam Farm, Guntur, Andhra Pradesh",
                    village = "Lam Farm",
                    mandal = "Tadikonda",
                    district = "Guntur",
                    state = "Andhra Pradesh",
                    latitude = 16.30,
                    longitude = 80.44,
                    primaryCrops = "Chili, Cotton, Paddy",
                    farmSizeAcres = 12.0,
                    soilType = "Black Cotton Soil",
                    irrigationSource = "Borewell & Drip Fertigation",
                    farmingInterests = "Biological Pest Control, Trichoderma, Pheromone Trapping",
                    bio = "Professor in Entomology & Plant Pathology. Sharing eco-friendly pest management guides.",
                    role = "EXPERT"
                )
            )

            // Seed Farmers
            val farmer1Salt = SecurityService.generateSalt()
            val farmer1Hash = SecurityService.hashPassword("farmer123", farmer1Salt)
            val farmer1Id = userDao.insertUser(
                UserEntity(
                    username = "ramesh_farmer",
                    fullName = "Ramesh Babu",
                    phoneOrEmail = "ramesh@farm.org",
                    passwordHash = farmer1Hash,
                    salt = farmer1Salt,
                    languageCode = "te",
                    location = "Tenali, Guntur, Andhra Pradesh",
                    village = "Angalakuduru",
                    mandal = "Tenali",
                    district = "Guntur",
                    state = "Andhra Pradesh",
                    latitude = 16.24,
                    longitude = 80.64,
                    primaryCrops = "Paddy (BPT 5204), Black Gram",
                    farmSizeAcres = 8.5,
                    soilType = "Deep Black Clay Soil",
                    irrigationSource = "Krishna Western Delta Canal",
                    farmingInterests = "System of Rice Intensification (SRI), Vermicomposting, Jeevamrutham",
                    bio = "Practicing organic SRI paddy cultivation and zero-budget natural farming for 15 years.",
                    role = "FARMER"
                )
            )

            // Seed Nearby Farmer (Chebrolu - 9 km from Ramesh)
            val farmerNearSalt = SecurityService.generateSalt()
            val farmerNearHash = SecurityService.hashPassword("farmer123", farmerNearSalt)
            val farmerNearId = userDao.insertUser(
                UserEntity(
                    username = "srinivas_chebrolu",
                    fullName = "K. Srinivas",
                    phoneOrEmail = "srinivas@farm.org",
                    passwordHash = farmerNearHash,
                    salt = farmerNearSalt,
                    languageCode = "te",
                    location = "Chebrolu, Guntur, Andhra Pradesh",
                    village = "Chebrolu",
                    mandal = "Chebrolu",
                    district = "Guntur",
                    state = "Andhra Pradesh",
                    latitude = 16.20,
                    longitude = 80.53,
                    primaryCrops = "Chili (Teja), Cotton, Turmeric",
                    farmSizeAcres = 6.0,
                    soilType = "Black Cotton Soil",
                    irrigationSource = "Solar Drip Irrigation",
                    farmingInterests = "Drip Fertigation, Chili Disease Management, Solar Pumping",
                    bio = "Guntur chili and cotton grower exploring low-cost solar automation and natural soil conditioners.",
                    role = "FARMER"
                )
            )

            val farmer2Salt = SecurityService.generateSalt()
            val farmer2Hash = SecurityService.hashPassword("farmer123", farmer2Salt)
            val farmer2Id = userDao.insertUser(
                UserEntity(
                    username = "balwinder_punjab",
                    fullName = "Sardar Balwinder Singh",
                    phoneOrEmail = "balwinder@farm.org",
                    passwordHash = farmer2Hash,
                    salt = farmer2Salt,
                    languageCode = "hi",
                    location = "Ludhiana, Punjab",
                    village = "Samrala",
                    mandal = "Samrala",
                    district = "Ludhiana",
                    state = "Punjab",
                    latitude = 30.90,
                    longitude = 75.85,
                    primaryCrops = "Wheat, Mustard, Basmati",
                    farmSizeAcres = 18.0,
                    soilType = "Alluvial Loam",
                    irrigationSource = "Tube well & Canal",
                    farmingInterests = "Direct Seeded Rice (DSR), Happy Seeder, Stubble Management",
                    bio = "Wheat farmer using Happy Seeder and laser land leveling. Focused on stubble management.",
                    role = "FARMER"
                )
            )

            val farmer3Salt = SecurityService.generateSalt()
            val farmer3Hash = SecurityService.hashPassword("farmer123", farmer3Salt)
            val farmer3Id = userDao.insertUser(
                UserEntity(
                    username = "muthu_tn",
                    fullName = "Muthu Kumaran",
                    phoneOrEmail = "muthu@farm.org",
                    passwordHash = farmer3Hash,
                    salt = farmer3Salt,
                    languageCode = "ta",
                    location = "Thanjavur, Tamil Nadu",
                    village = "Kumbakonam",
                    mandal = "Kumbakonam",
                    district = "Thanjavur",
                    state = "Tamil Nadu",
                    latitude = 10.78,
                    longitude = 79.13,
                    primaryCrops = "Sugarcane, Banana, Coconut",
                    farmSizeAcres = 6.0,
                    soilType = "Delta Alluvial Clay",
                    irrigationSource = "Cauvery River Channel & Borewell",
                    farmingInterests = "Multi-tier Cropping, Bio-fertilizers, Farm Mechanization",
                    bio = "Cauvery delta farmer passionate about drip fertigation and bio-fertilizers.",
                    role = "FARMER"
                )
            )

            // Seed Initial Crop Details for Ramesh Babu
            cropDetailDao.insertCrop(
                CropDetailEntity(
                    farmerId = farmer1Id,
                    cropName = "Paddy",
                    variety = "BPT 5204 (Samba Masuri)",
                    season = "Kharif",
                    areaAcres = 5.0,
                    sowingDate = "June 2026",
                    stage = "Flowering / Grain Filling",
                    soilHealthNotes = "Enriched with 5 carts of Farmyard Manure & Neem cake",
                    expectedYieldQuintals = 160.0
                )
            )

            cropDetailDao.insertCrop(
                CropDetailEntity(
                    farmerId = farmer1Id,
                    cropName = "Black Gram",
                    variety = "LBG 752",
                    season = "Rabi",
                    areaAcres = 3.5,
                    sowingDate = "October 2026",
                    stage = "Vegetative",
                    soilHealthNotes = "Relies on residual soil moisture after paddy harvest",
                    expectedYieldQuintals = 28.0
                )
            )

            // Seed Initial Posts
            val post1Id = postDao.insertPost(
                PostEntity(
                    authorId = farmer1Id,
                    authorName = "Ramesh Babu",
                    authorLocation = "Tenali, Andhra Pradesh",
                    authorCrop = "Paddy & Natural Farming",
                    authorRole = "FARMER",
                    content = "SRI (System of Rice Intensification) Results: Transplanted 10-day single seedlings with 25x25cm spacing. Tiller count reached 38 tillers per hill! Saved 40% water using alternate wetting and drying method. Happy to share seed soaking preparation steps if anyone is interested.",
                    cropTag = "Paddy",
                    mediaType = "IMAGE",
                    mediaResName = "crop_sample_paddy",
                    likesCount = 24,
                    commentsCount = 3,
                    bookmarksCount = 11
                )
            )

            val post2Id = postDao.insertPost(
                PostEntity(
                    authorId = expertId,
                    authorName = "Prof. Ananya Rao",
                    authorLocation = "Guntur, Andhra Pradesh",
                    authorCrop = "Entomology & IPM",
                    authorRole = "EXPERT",
                    content = "Alert for Chili and Cotton Growers: High humidity creates favorable conditions for Black Thrips and Leaf Curl. Recommended Organic Control: Spray 5ml Neem oil (10,000 ppm) + soap nut solution per liter every 7 days. Install 15 blue and yellow sticky traps per acre immediately to monitor infestation early.",
                    cropTag = "Pest Control",
                    mediaType = "NONE",
                    likesCount = 42,
                    commentsCount = 5,
                    bookmarksCount = 29
                )
            )

            val post3Id = postDao.insertPost(
                PostEntity(
                    authorId = farmer2Id,
                    authorName = "Sardar Balwinder Singh",
                    authorLocation = "Ludhiana, Punjab",
                    authorCrop = "Wheat & Soil Health",
                    authorRole = "FARMER",
                    content = "Soil fertility tip: Before sowing winter wheat, incorporation of summer moong crop residue and application of Jeevamrutham has improved soil organic carbon from 0.42% to 0.78% over 3 seasons. Micro-nutrient deficiency symptoms have completely disappeared.",
                    cropTag = "Soil & Fertilizer",
                    mediaType = "IMAGE",
                    mediaResName = "agribook_banner",
                    likesCount = 31,
                    commentsCount = 2,
                    bookmarksCount = 18
                )
            )

            // Seed Comments
            interactionDao.insertComment(
                CommentEntity(
                    postId = post1Id,
                    authorId = expertId,
                    authorName = "Prof. Ananya Rao",
                    authorRole = "EXPERT",
                    text = "Excellent tiller development Ramesh ji! Keep water level at 2cm until flowering stage to maximize grain filling."
                )
            )
            interactionDao.insertComment(
                CommentEntity(
                    postId = post1Id,
                    authorId = farmer3Id,
                    authorName = "Muthu Kumaran",
                    authorRole = "FARMER",
                    text = "Did you use cono-weeder at 10th and 20th day? How was weed control without herbicides?"
                )
            )

            // Seed Connections
            connectionDao.insertConnection(
                ConnectionEntity(
                    requesterId = farmer1Id,
                    receiverId = expertId,
                    status = "ACCEPTED"
                )
            )
            connectionDao.insertConnection(
                ConnectionEntity(
                    requesterId = farmer2Id,
                    receiverId = farmer1Id,
                    status = "PENDING"
                )
            )

            // Seed Notifications for farmer1
            notificationDao.insertNotification(
                NotificationEntity(
                    userId = farmer1Id,
                    type = "LIKE",
                    title = "Your Paddy post received 20+ likes",
                    body = "Farmers across Andhra Pradesh found your SRI tiller method helpful.",
                    relatedId = post1Id
                )
            )
            notificationDao.insertNotification(
                NotificationEntity(
                    userId = farmer1Id,
                    type = "CONNECTION_REQUEST",
                    title = "New Connection Request",
                    body = "Sardar Balwinder Singh sent you a connection request.",
                    relatedId = farmer2Id
                )
            )
        }
    }

    // Authentication & User Profile
    suspend fun registerUser(
        username: String,
        fullName: String,
        phoneOrEmail: String,
        passwordPlain: String,
        languageCode: String,
        location: String,
        crops: String,
        farmSize: Double
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val sanitizedPhone = SecurityService.sanitizeInput(phoneOrEmail)
        val sanitizedUser = SecurityService.sanitizeInput(username)

        if (!SecurityService.checkRateLimit("reg_$sanitizedPhone")) {
            return@withContext Result.failure(Exception("Too many registration attempts. Please wait 1 minute."))
        }

        val (validPass, errorPass) = SecurityService.validatePasswordStrength(passwordPlain)
        if (!validPass) {
            return@withContext Result.failure(Exception(errorPass))
        }

        val existing = userDao.findByPhoneOrEmail(sanitizedPhone)
        if (existing != null) {
            return@withContext Result.failure(Exception("An account with this phone or email already exists."))
        }

        val salt = SecurityService.generateSalt()
        val hash = SecurityService.hashPassword(passwordPlain, salt)

        val newUser = UserEntity(
            username = sanitizedUser,
            fullName = SecurityService.sanitizeInput(fullName),
            phoneOrEmail = sanitizedPhone,
            passwordHash = hash,
            salt = salt,
            languageCode = languageCode,
            location = SecurityService.sanitizeInput(location),
            primaryCrops = SecurityService.sanitizeInput(crops),
            farmSizeAcres = farmSize,
            role = "FARMER"
        )

        val id = userDao.insertUser(newUser)
        SecurityService.resetRateLimit("reg_$sanitizedPhone")
        Result.success(newUser.copy(id = id))
    }

    suspend fun loginUser(phoneOrEmail: String, passwordPlain: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val sanitized = SecurityService.sanitizeInput(phoneOrEmail)
        if (!SecurityService.checkRateLimit("login_$sanitized")) {
            return@withContext Result.failure(Exception("Too many failed attempts. Rate limited for 60 seconds."))
        }

        val user = userDao.findByPhoneOrEmail(sanitized)
        if (user == null) {
            return@withContext Result.failure(Exception("User not found with provided phone or email."))
        }

        if (user.isBanned) {
            return@withContext Result.failure(Exception("This account has been suspended for violating community guidelines."))
        }

        val matches = SecurityService.verifyPassword(passwordPlain, user.salt, user.passwordHash)
        if (!matches) {
            return@withContext Result.failure(Exception("Invalid password. Please verify and try again."))
        }

        SecurityService.resetRateLimit("login_$sanitized")
        Result.success(user)
    }

    suspend fun updateUser(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.updateUser(user)
    }

    suspend fun updateFarmerProfile(
        userId: Long,
        fullName: String,
        bio: String,
        village: String,
        mandal: String,
        district: String,
        state: String,
        primaryCrops: String,
        farmSizeAcres: Double,
        soilType: String,
        irrigationSource: String,
        farmingInterests: String
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId) ?: return@withContext Result.failure(Exception("User not found"))
        val updated = user.copy(
            fullName = SecurityService.sanitizeInput(fullName),
            bio = SecurityService.sanitizeInput(bio),
            village = SecurityService.sanitizeInput(village),
            mandal = SecurityService.sanitizeInput(mandal),
            district = SecurityService.sanitizeInput(district),
            state = SecurityService.sanitizeInput(state),
            location = "${SecurityService.sanitizeInput(village)}, ${SecurityService.sanitizeInput(district)}, ${SecurityService.sanitizeInput(state)}",
            primaryCrops = SecurityService.sanitizeInput(primaryCrops),
            farmSizeAcres = farmSizeAcres,
            soilType = SecurityService.sanitizeInput(soilType),
            irrigationSource = SecurityService.sanitizeInput(irrigationSource),
            farmingInterests = SecurityService.sanitizeInput(farmingInterests)
        )
        userDao.updateUser(updated)
        Result.success(updated)
    }

    suspend fun updateLocation(userId: Long, lat: Double, lng: Double): UserEntity? = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId) ?: return@withContext null
        val updated = user.copy(latitude = lat, longitude = lng)
        userDao.updateUser(updated)
        updated
    }

    // Crop Management
    suspend fun addCropDetail(crop: CropDetailEntity): Long = withContext(Dispatchers.IO) {
        cropDetailDao.insertCrop(
            crop.copy(
                cropName = SecurityService.sanitizeInput(crop.cropName),
                variety = SecurityService.sanitizeInput(crop.variety),
                season = SecurityService.sanitizeInput(crop.season),
                sowingDate = SecurityService.sanitizeInput(crop.sowingDate),
                stage = SecurityService.sanitizeInput(crop.stage),
                soilHealthNotes = SecurityService.sanitizeInput(crop.soilHealthNotes)
            )
        )
    }

    suspend fun deleteCropDetail(cropId: Long) = withContext(Dispatchers.IO) {
        cropDetailDao.deleteCrop(cropId)
    }

    fun getCropsForFarmerFlow(farmerId: Long): Flow<List<CropDetailEntity>> {
        return cropDetailDao.getCropsForFarmerFlow(farmerId)
    }

    suspend fun updateLanguage(userId: Long, langCode: String) = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId)
        if (user != null) {
            userDao.updateUser(user.copy(languageCode = langCode))
        }
    }

    suspend fun updatePrivacy(userId: Long, whoCanMessage: String, profileVisibility: String) = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId)
        if (user != null) {
            userDao.updateUser(user.copy(whoCanMessage = whoCanMessage, profileVisibility = profileVisibility))
        }
    }

    // Posts & Interactions
    suspend fun createPost(
        author: UserEntity,
        content: String,
        cropTag: String,
        mediaType: String = "NONE",
        mediaResName: String = ""
    ): Long = withContext(Dispatchers.IO) {
        val newPost = PostEntity(
            authorId = author.id,
            authorName = author.fullName,
            authorLocation = author.location,
            authorCrop = author.primaryCrops,
            authorRole = author.role,
            content = SecurityService.sanitizeInput(content),
            cropTag = cropTag,
            mediaType = mediaType,
            mediaResName = mediaResName
        )
        postDao.insertPost(newPost)
    }

    suspend fun toggleLike(postId: Long, userId: Long): Boolean = withContext(Dispatchers.IO) {
        val hasLiked = interactionDao.hasLiked(postId, userId)
        val post = postDao.getPostById(postId) ?: return@withContext false

        if (hasLiked) {
            interactionDao.removeLike(postId, userId)
            postDao.updatePost(post.copy(likesCount = (post.likesCount - 1).coerceAtLeast(0)))
            false
        } else {
            interactionDao.insertLike(PostLikeEntity(postId = postId, userId = userId))
            postDao.updatePost(post.copy(likesCount = post.likesCount + 1))
            if (post.authorId != userId) {
                notificationDao.insertNotification(
                    NotificationEntity(
                        userId = post.authorId,
                        type = "LIKE",
                        title = "New reaction on your farm post",
                        body = "A farmer found your advice on ${post.cropTag} helpful.",
                        relatedId = postId
                    )
                )
            }
            true
        }
    }

    suspend fun toggleBookmark(postId: Long, userId: Long): Boolean = withContext(Dispatchers.IO) {
        val post = postDao.getPostById(postId) ?: return@withContext false
        val currentlyBookmarked = interactionDao.hasLiked(postId, userId) // check bookmark
        // We use query or check directly
        // Let's do check with flow or direct query
        val isSaved = try {
            interactionDao.insertBookmark(BookmarkEntity(postId, userId))
            postDao.updatePost(post.copy(bookmarksCount = post.bookmarksCount + 1))
            true
        } catch (e: Exception) {
            interactionDao.removeBookmark(postId, userId)
            postDao.updatePost(post.copy(bookmarksCount = (post.bookmarksCount - 1).coerceAtLeast(0)))
            false
        }
        isSaved
    }

    suspend fun addComment(postId: Long, author: UserEntity, text: String) = withContext(Dispatchers.IO) {
        val comment = CommentEntity(
            postId = postId,
            authorId = author.id,
            authorName = author.fullName,
            authorRole = author.role,
            text = SecurityService.sanitizeInput(text)
        )
        interactionDao.insertComment(comment)
        val post = postDao.getPostById(postId)
        if (post != null) {
            postDao.updatePost(post.copy(commentsCount = post.commentsCount + 1))
            if (post.authorId != author.id) {
                notificationDao.insertNotification(
                    NotificationEntity(
                        userId = post.authorId,
                        type = "COMMENT",
                        title = "New comment from ${author.fullName}",
                        body = text.take(60),
                        relatedId = postId
                    )
                )
            }
        }
    }

    // Connections & Networking
    suspend fun sendConnectionRequest(senderId: Long, senderName: String, receiverId: Long) = withContext(Dispatchers.IO) {
        connectionDao.insertConnection(
            ConnectionEntity(requesterId = senderId, receiverId = receiverId, status = "PENDING")
        )
        notificationDao.insertNotification(
            NotificationEntity(
                userId = receiverId,
                type = "CONNECTION_REQUEST",
                title = "New Connection Request",
                body = "$senderName wants to connect with your farm network.",
                relatedId = senderId
            )
        )
    }

    suspend fun respondToConnection(connectionId: Long, accept: Boolean, requesterId: Long, responderName: String) = withContext(Dispatchers.IO) {
        val status = if (accept) "ACCEPTED" else "REJECTED"
        connectionDao.updateConnectionStatus(connectionId, status)
        if (accept) {
            notificationDao.insertNotification(
                NotificationEntity(
                    userId = requesterId,
                    type = "CONNECTION_REQUEST",
                    title = "Connection Request Accepted",
                    body = "$responderName accepted your connection request. You can now chat directly!",
                    relatedId = connectionId
                )
            )
        }
    }

    suspend fun blockUser(blockerId: Long, blockedId: Long) = withContext(Dispatchers.IO) {
        interactionDao.insertBlock(BlockEntity(blockerId, blockedId))
    }

    suspend fun unblockUser(blockerId: Long, blockedId: Long) = withContext(Dispatchers.IO) {
        interactionDao.removeBlock(blockerId, blockedId)
    }

    // Messaging
    suspend fun sendMessage(senderId: Long, receiverId: Long, content: String): Result<Long> = withContext(Dispatchers.IO) {
        // Authorization check: Verify if blocked
        if (interactionDao.isBlocked(receiverId, senderId)) {
            return@withContext Result.failure(Exception("Cannot send message. This user has blocked communication."))
        }
        val receiver = userDao.getUserById(receiverId)
        if (receiver != null && receiver.whoCanMessage == "CONNECTIONS") {
            val conn = connectionDao.findConnectionBetween(senderId, receiverId)
            if (conn?.status != "ACCEPTED") {
                return@withContext Result.failure(Exception("This farmer only accepts messages from connected network."))
            }
        }

        val msgId = messageDao.insertMessage(
            MessageEntity(senderId = senderId, receiverId = receiverId, content = SecurityService.sanitizeInput(content))
        )
        Result.success(msgId)
    }

    // Moderation & Reports
    suspend fun submitReport(reporter: UserEntity, targetType: String, targetId: Long, summary: String, reason: String) = withContext(Dispatchers.IO) {
        reportDao.insertReport(
            ReportEntity(
                reporterId = reporter.id,
                reporterName = reporter.fullName,
                targetType = targetType,
                targetId = targetId,
                targetSummary = summary,
                reason = reason
            )
        )
        if (targetType == "POST") {
            val post = postDao.getPostById(targetId)
            if (post != null) {
                postDao.updatePost(post.copy(isFlagged = true))
            }
        }
    }

    suspend fun resolveReport(reportId: Long, action: String, targetType: String, targetId: Long) = withContext(Dispatchers.IO) {
        reportDao.updateReportStatus(reportId, "RESOLVED ($action)")
        if (action == "DELETE" && targetType == "POST") {
            postDao.deletePost(targetId)
        } else if (action == "BAN" && targetType == "USER") {
            val user = userDao.getUserById(targetId)
            if (user != null) {
                userDao.updateUser(user.copy(isBanned = true))
            }
        }
    }

    suspend fun dismissReport(reportId: Long, targetType: String, targetId: Long) = withContext(Dispatchers.IO) {
        reportDao.updateReportStatus(reportId, "DISMISSED")
        if (targetType == "POST") {
            val post = postDao.getPostById(targetId)
            if (post != null) {
                postDao.updatePost(post.copy(isFlagged = false))
            }
        }
    }
}
