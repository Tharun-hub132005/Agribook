package com.example.agribook.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.agribook.data.local.*
import com.example.agribook.localization.AppLanguage
import com.example.agribook.repository.AgriBookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppTab {
    FEED, CONNECTIONS, CHAT_THREAD, NOTIFICATIONS, PROFILE, ADMIN
}

enum class AuthScreenState {
    LOGIN, REGISTER, LANGUAGE_PICKER_FIRST_TIME
}

class AgriBookViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AgriBookDatabase.getDatabase(application)
    val repository = AgriBookRepository(database)

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage = _currentLanguage.asStateFlow()

    private val _authScreenState = MutableStateFlow(AuthScreenState.LOGIN)
    val authScreenState = _authScreenState.asStateFlow()

    private val _currentTab = MutableStateFlow(AppTab.FEED)
    val currentTab = _currentTab.asStateFlow()

    private val _selectedCropFilter = MutableStateFlow("All")
    val selectedCropFilter = _selectedCropFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _activeChatPartner = MutableStateFlow<UserEntity?>(null)
    val activeChatPartner = _activeChatPartner.asStateFlow()

    private val _activeCommentPost = MutableStateFlow<PostEntity?>(null)
    val activeCommentPost = _activeCommentPost.asStateFlow()

    private val _reportTarget = MutableStateFlow<Pair<String, Long>?>(null)
    val reportTarget = _reportTarget.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage = _successMessage.asStateFlow()

    private val _filterNearbyOnly = MutableStateFlow(false)
    val filterNearbyOnly = _filterNearbyOnly.asStateFlow()

    // Real-time Flows
    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: StateFlow<List<PostEntity>> = _selectedCropFilter.flatMapLatest { crop ->
        if (crop == "All") repository.postDao.getAllPostsFlow()
        else repository.postDao.getPostsByCropFlow(crop)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val farmers: StateFlow<List<UserEntity>> = combine(_currentUser, _searchQuery) { user, query ->
        Pair(user?.id ?: 0L, query)
    }.flatMapLatest { (userId, query) ->
        if (query.isBlank()) repository.userDao.getAllFarmersFlow(userId)
        else repository.userDao.searchFarmersFlow(userId, query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val farmerCrops: StateFlow<List<CropDetailEntity>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else repository.getCropsForFarmerFlow(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val pendingRequests: StateFlow<List<ConnectionEntity>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else repository.connectionDao.getPendingRequestsFlow(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val acceptedConnections: StateFlow<List<ConnectionEntity>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else repository.connectionDao.getAcceptedConnectionsFlow(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val notifications: StateFlow<List<NotificationEntity>> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(emptyList())
        else repository.notificationDao.getNotificationsFlow(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val unreadNotificationsCount: StateFlow<Int> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(0)
        else repository.notificationDao.getUnreadNotificationCountFlow(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val unreadMessagesCount: StateFlow<Int> = _currentUser.flatMapLatest { user ->
        if (user == null) flowOf(0)
        else repository.messageDao.getUnreadMessageCountFlow(user.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentChatMessages: StateFlow<List<MessageEntity>> = combine(_currentUser, _activeChatPartner) { user, partner ->
        Pair(user, partner)
    }.flatMapLatest { (user, partner) ->
        if (user == null || partner == null) flowOf(emptyList())
        else repository.messageDao.getConversationFlow(user.id, partner.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentComments: StateFlow<List<CommentEntity>> = _activeCommentPost.flatMapLatest { post ->
        if (post == null) flowOf(emptyList())
        else repository.interactionDao.getCommentsForPostFlow(post.id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminReports: StateFlow<List<ReportEntity>> = repository.reportDao.getAllReportsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // Auth & Navigation
    fun setAuthScreenState(state: AuthScreenState) {
        _authScreenState.value = state
    }

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setSelectedCropFilter(crop: String) {
        _selectedCropFilter.value = crop
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setActiveChatPartner(partner: UserEntity?) {
        _activeChatPartner.value = partner
        if (partner != null) {
            _currentTab.value = AppTab.CHAT_THREAD
            val current = _currentUser.value
            if (current != null) {
                viewModelScope.launch {
                    repository.messageDao.markConversationRead(partner.id, current.id)
                }
            }
        }
    }

    fun setActiveCommentPost(post: PostEntity?) {
        _activeCommentPost.value = post
    }

    fun setReportTarget(target: Pair<String, Long>?) {
        _reportTarget.value = target
    }

    fun clearFeedback() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun showFeedback(message: String) {
        _successMessage.value = message
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
        val user = _currentUser.value
        if (user != null) {
            viewModelScope.launch {
                repository.updateLanguage(user.id, lang.code)
                _currentUser.value = user.copy(languageCode = lang.code)
            }
        }
    }

    fun login(identifier: String, passwordPlain: String) {
        viewModelScope.launch {
            clearFeedback()
            val result = repository.loginUser(identifier, passwordPlain)
            result.onSuccess { user ->
                _currentUser.value = user
                _currentLanguage.value = AppLanguage.fromCode(user.languageCode)
                _currentTab.value = AppTab.FEED
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Login failed"
            }
        }
    }

    fun register(
        username: String,
        fullName: String,
        identifier: String,
        passwordPlain: String,
        location: String,
        crops: String,
        farmSize: Double
    ) {
        viewModelScope.launch {
            clearFeedback()
            val result = repository.registerUser(
                username = username,
                fullName = fullName,
                phoneOrEmail = identifier,
                passwordPlain = passwordPlain,
                languageCode = _currentLanguage.value.code,
                location = location,
                crops = crops,
                farmSize = farmSize
            )
            result.onSuccess { user ->
                _currentUser.value = user
                _authScreenState.value = AuthScreenState.LANGUAGE_PICKER_FIRST_TIME
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Registration failed"
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _activeChatPartner.value = null
        _activeCommentPost.value = null
        _authScreenState.value = AuthScreenState.LOGIN
    }

    fun createPost(content: String, cropTag: String, mediaType: String, mediaResName: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.createPost(user, content, cropTag, mediaType, mediaResName)
            _successMessage.value = "Farming post shared successfully!"
        }
    }

    fun toggleLike(postId: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.toggleLike(postId, user.id)
        }
    }

    fun toggleBookmark(postId: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val saved = repository.toggleBookmark(postId, user.id)
            _successMessage.value = if (saved) "Tip saved to your bookmarks" else "Tip removed from bookmarks"
        }
    }

    fun addComment(postId: Long, text: String) {
        val user = _currentUser.value ?: return
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addComment(postId, user, text)
        }
    }

    fun sendConnectionRequest(target: UserEntity) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.sendConnectionRequest(user.id, user.fullName, target.id)
            _successMessage.value = "Connection request sent to ${target.fullName}"
        }
    }

    fun respondToConnection(connectionId: Long, accept: Boolean, requesterId: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.respondToConnection(connectionId, accept, requesterId, user.fullName)
            _successMessage.value = if (accept) "Connected to farmer network!" else "Request declined."
        }
    }

    fun blockUser(targetId: Long, targetName: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.blockUser(user.id, targetId)
            _successMessage.value = "Blocked $targetName. They can no longer contact you."
        }
    }

    fun sendMessage(content: String) {
        val user = _currentUser.value ?: return
        val partner = _activeChatPartner.value ?: return
        if (content.isBlank()) return
        viewModelScope.launch {
            val res = repository.sendMessage(user.id, partner.id, content)
            res.onSuccess {
                // If chatting with expert, simulate instant agronomy response if needed
                if (partner.role == "EXPERT" && content.contains("?")) {
                    repository.sendMessage(
                        senderId = partner.id,
                        receiverId = user.id,
                        content = "Thank you for asking! For ${partner.primaryCrops.split(",").firstOrNull() ?: "crops"}, ensure proper soil aeration, test pH, and keep drainage channels cleared."
                    )
                }
            }.onFailure { err ->
                _errorMessage.value = err.message
            }
        }
    }

    fun submitReport(reason: String) {
        val user = _currentUser.value ?: return
        val target = _reportTarget.value ?: return
        viewModelScope.launch {
            repository.submitReport(
                reporter = user,
                targetType = target.first,
                targetId = target.second,
                summary = "Reported by ${user.fullName}",
                reason = reason
            )
            _reportTarget.value = null
            _successMessage.value = "Report submitted to admin moderators."
        }
    }

    fun resolveAdminReport(reportId: Long, action: String, targetType: String, targetId: Long) {
        viewModelScope.launch {
            repository.resolveReport(reportId, action, targetType, targetId)
            _successMessage.value = "Admin action executed: $action"
        }
    }

    fun dismissAdminReport(reportId: Long, targetType: String, targetId: Long) {
        viewModelScope.launch {
            repository.dismissReport(reportId, targetType, targetId)
            _successMessage.value = "Report dismissed."
        }
    }

    fun updatePrivacySettings(whoCanMessage: String, profileVisibility: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updatePrivacy(user.id, whoCanMessage, profileVisibility)
            _currentUser.value = user.copy(whoCanMessage = whoCanMessage, profileVisibility = profileVisibility)
            _successMessage.value = "Privacy settings updated."
        }
    }

    fun markNotificationsAsRead() {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.notificationDao.markAllAsRead(user.id)
        }
    }

    fun setFilterNearbyOnly(enabled: Boolean) {
        _filterNearbyOnly.value = enabled
    }

    fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Radius of earth in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return Math.round(r * c * 10.0) / 10.0
    }

    fun updateFarmerProfile(
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
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            clearFeedback()
            val res = repository.updateFarmerProfile(
                userId = user.id,
                fullName = fullName,
                bio = bio,
                village = village,
                mandal = mandal,
                district = district,
                state = state,
                primaryCrops = primaryCrops,
                farmSizeAcres = farmSizeAcres,
                soilType = soilType,
                irrigationSource = irrigationSource,
                farmingInterests = farmingInterests
            )
            res.onSuccess { updated ->
                _currentUser.value = updated
                _successMessage.value = "Farmer profile updated successfully!"
            }.onFailure { err ->
                _errorMessage.value = err.message ?: "Failed to update profile"
            }
        }
    }

    fun updateFarmerLocation(lat: Double, lng: Double) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val updated = repository.updateLocation(user.id, lat, lng)
            if (updated != null) {
                _currentUser.value = updated
                _successMessage.value = "GPS Location updated! Nearby farmers synchronized."
            }
        }
    }

    fun addCrop(
        cropName: String,
        variety: String,
        season: String,
        areaAcres: Double,
        sowingDate: String,
        stage: String,
        soilHealthNotes: String,
        expectedYield: Double
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val crop = CropDetailEntity(
                farmerId = user.id,
                cropName = cropName,
                variety = variety,
                season = season,
                areaAcres = areaAcres,
                sowingDate = sowingDate,
                stage = stage,
                soilHealthNotes = soilHealthNotes,
                expectedYieldQuintals = expectedYield
            )
            repository.addCropDetail(crop)
            _successMessage.value = "$cropName details added to your farming record!"
        }
    }

    fun deleteCrop(cropId: Long) {
        viewModelScope.launch {
            repository.deleteCropDetail(cropId)
            _successMessage.value = "Crop record removed."
        }
    }
}
