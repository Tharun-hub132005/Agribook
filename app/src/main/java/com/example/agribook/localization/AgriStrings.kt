package com.example.agribook.localization

object AgriStrings {
    fun get(key: String, lang: AppLanguage): String {
        return translations[key]?.get(lang) ?: translations[key]?.get(AppLanguage.ENGLISH) ?: key
    }

    private val translations: Map<String, Map<AppLanguage, String>> = mapOf(
        "app_name" to mapOf(
            AppLanguage.ENGLISH to "Agribook",
            AppLanguage.TELUGU to "అగ్రిబుక్",
            AppLanguage.HINDI to "एग्रीबुक",
            AppLanguage.KANNADA to "ಅಗ್ರಿಬುಕ್",
            AppLanguage.TAMIL to "அக்ரிபுக்",
            AppLanguage.MALAYALAM to "അഗ്രിബുക്ക്"
        ),
        "tagline" to mapOf(
            AppLanguage.ENGLISH to "Farming Social Network & Crop Knowledge Community",
            AppLanguage.TELUGU to "రైతుల సాంఘిక నెట్‌వర్క్ & పంటల విజ్ఞాన వేదిక",
            AppLanguage.HINDI to "किसान सोशल नेटवर्क एवं फसल ज्ञान मंच",
            AppLanguage.KANNADA to "ರೈತರ ಸಾಮಾಜಿಕ ಜಾಲತಾಣ ಮತ್ತು ಬೆಳೆ ಜ್ಞಾನ ವೇದಿಕೆ",
            AppLanguage.TAMIL to "விவசாயிகள் சமூக வலைப்பின்னல் & பயிர் அறிவு தளம்",
            AppLanguage.MALAYALAM to "കർഷക സോഷ്യൽ നെറ്റ്‌വർക്കും വിള വിജ്ഞാന പ്ലാറ്റ്‌ഫോമും"
        ),
        "login" to mapOf(
            AppLanguage.ENGLISH to "Log In",
            AppLanguage.TELUGU to "లాగిన్ అవ్వండి",
            AppLanguage.HINDI to "लॉग इन करें",
            AppLanguage.KANNADA to "ಲಾಗಿನ್ ಮಾಡಿ",
            AppLanguage.TAMIL to "உள்நுழைக",
            AppLanguage.MALAYALAM to "ലോഗിൻ ചെയ്യുക"
        ),
        "register" to mapOf(
            AppLanguage.ENGLISH to "Create Farmer Account",
            AppLanguage.TELUGU to "రైతు ఖాతా తెరవండి",
            AppLanguage.HINDI to "किसान खाता बनाएं",
            AppLanguage.KANNADA to "ರೈತ ಖಾತೆ ತೆರೆಯಿರಿ",
            AppLanguage.TAMIL to "விவசாயி கணக்கை உருவாக்குக",
            AppLanguage.MALAYALAM to "കർഷക അക്കൗണ്ട് ഉണ്ടാക്കുക"
        ),
        "phone_or_email" to mapOf(
            AppLanguage.ENGLISH to "Phone or Email",
            AppLanguage.TELUGU to "ఫోన్ నంబర్ లేదా ఈమెయిల్",
            AppLanguage.HINDI to "फ़ोन नंबर या ईमेल",
            AppLanguage.KANNADA to "ದೂರವಾಣಿ ಅಥವಾ ಇಮೇಲ್",
            AppLanguage.TAMIL to "தொலைபேசி அல்லது மின்னஞ்சல்",
            AppLanguage.MALAYALAM to "ഫോൺ അല്ലെങ്കിൽ ഇമെയിൽ"
        ),
        "password" to mapOf(
            AppLanguage.ENGLISH to "Password",
            AppLanguage.TELUGU to "పాస్‌వర్డ్",
            AppLanguage.HINDI to "पासवर्ड",
            AppLanguage.KANNADA to "ಪಾಸ್‌ವರ್ಡ್",
            AppLanguage.TAMIL to "கடவுச்சொல்",
            AppLanguage.MALAYALAM to "പാസ്‌വേഡ്"
        ),
        "full_name" to mapOf(
            AppLanguage.ENGLISH to "Farmer Full Name",
            AppLanguage.TELUGU to "రైతు పూర్తి పేరు",
            AppLanguage.HINDI to "किसान का पूरा नाम",
            AppLanguage.KANNADA to "ರೈತನ ಪೂರ್ಣ ಹೆಸರು",
            AppLanguage.TAMIL to "விவசாயியின் முழுப் பெயர்",
            AppLanguage.MALAYALAM to "കർഷകന്റെ മുഴുവൻ പേര്"
        ),
        "location" to mapOf(
            AppLanguage.ENGLISH to "District / State",
            AppLanguage.TELUGU to "జిల్లా / రాష్ట్రం",
            AppLanguage.HINDI to "जिला / राज्य",
            AppLanguage.KANNADA to "ಜಿಲ್ಲೆ / ರಾಜ್ಯ",
            AppLanguage.TAMIL to "மாவட்டம் / மாநிலம்",
            AppLanguage.MALAYALAM to "ജില്ല / സംസ്ഥാനം"
        ),
        "primary_crops" to mapOf(
            AppLanguage.ENGLISH to "Primary Crops (e.g., Paddy, Cotton, Chili)",
            AppLanguage.TELUGU to "ప్రధాన పంటలు (ఉదా. వరి, పత్తి, మిర్చి)",
            AppLanguage.HINDI to "मुख्य फसलें (जैसे धान, कपास, मिर्च)",
            AppLanguage.KANNADA to "ಮುಖ್ಯ ಬೆಳೆಗಳು (ಉದಾ. ಭತ್ತ, ಹತ್ತಿ, ಮೆಣಸಿನಕಾಯಿ)",
            AppLanguage.TAMIL to "முக்கிய பயிர்கள் (எ.கா. நெல், பருத்தி, மிளகாய்)",
            AppLanguage.MALAYALAM to "പ്രധാന വിളകൾ (ഉദാ. നെല്ല്, പരുത്തി, മുളക്)"
        ),
        "farm_size" to mapOf(
            AppLanguage.ENGLISH to "Farm Size (Acres)",
            AppLanguage.TELUGU to "వ్యవసాయ భూమి (ఎకరాలు)",
            AppLanguage.HINDI to "खेत का आकार (एकड़)",
            AppLanguage.KANNADA to "ಜಮೀನಿನ ವಿಸ್ತೀರ್ಣ (ಎಕರೆಗಳು)",
            AppLanguage.TAMIL to "நில அளவு (ஏக்கர்)",
            AppLanguage.MALAYALAM to "കൃഷിഭൂമി (ഏക്കർ)"
        ),
        "select_language" to mapOf(
            AppLanguage.ENGLISH to "Select Your Language",
            AppLanguage.TELUGU to "మీ భాషను ఎంచుకోండి",
            AppLanguage.HINDI to "अपनी भाषा चुनें",
            AppLanguage.KANNADA to "ನಿಮ್ಮ ಭಾಷೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ",
            AppLanguage.TAMIL to "உங்கள் மொழியைத் தேர்ந்தெடுக்கவும்",
            AppLanguage.MALAYALAM to "നിങ്ങളുടെ ഭാഷ തിരഞ്ഞെടുക്കുക"
        ),
        "continue_btn" to mapOf(
            AppLanguage.ENGLISH to "Continue",
            AppLanguage.TELUGU to "కొనసాగించండి",
            AppLanguage.HINDI to "आगे बढ़ें",
            AppLanguage.KANNADA to "ಮುಂದುವರಿಯಿರಿ",
            AppLanguage.TAMIL to "தொடரவும்",
            AppLanguage.MALAYALAM to "തുടരുക"
        ),
        "feed" to mapOf(
            AppLanguage.ENGLISH to "Farm Feed",
            AppLanguage.TELUGU to "రైతు వార్తలు",
            AppLanguage.HINDI to "किसान फ़ीड",
            AppLanguage.KANNADA to "ರೈತ ಫೀಡ್",
            AppLanguage.TAMIL to "பயிர் பதிவுகள்",
            AppLanguage.MALAYALAM to "കൃഷി ഫീഡ്"
        ),
        "connections" to mapOf(
            AppLanguage.ENGLISH to "Farmers",
            AppLanguage.TELUGU to "స్నేహితులు",
            AppLanguage.HINDI to "किसान नेटवर्क",
            AppLanguage.KANNADA to "ಸಂಪರ್ಕಗಳು",
            AppLanguage.TAMIL to "விவசாயிகள்",
            AppLanguage.MALAYALAM to "കർഷകർ"
        ),
        "messages" to mapOf(
            AppLanguage.ENGLISH to "Chats",
            AppLanguage.TELUGU to "సందేశాలు",
            AppLanguage.HINDI to "बातचीत",
            AppLanguage.KANNADA to "ಸಂದೇಶಗಳು",
            AppLanguage.TAMIL to "செய்திகள்",
            AppLanguage.MALAYALAM to "സന്ദേശങ്ങൾ"
        ),
        "notifications" to mapOf(
            AppLanguage.ENGLISH to "Alerts",
            AppLanguage.TELUGU to "నోటీసులు",
            AppLanguage.HINDI to "सूचनाएं",
            AppLanguage.KANNADA to "ಸೂಚನೆಗಳು",
            AppLanguage.TAMIL to "அறிவிப்புகள்",
            AppLanguage.MALAYALAM to "അറിയിപ്പുകൾ"
        ),
        "profile" to mapOf(
            AppLanguage.ENGLISH to "Profile & Settings",
            AppLanguage.TELUGU to "ప్రొఫైల్ & సెట్టింగులు",
            AppLanguage.HINDI to "प्रोफ़ाइल व सेटिंग्स",
            AppLanguage.KANNADA to "ಪ್ರೊಫೈಲ್ ಮತ್ತು ಸೆಟ್ಟಿಂಗ್ಗಳು",
            AppLanguage.TAMIL to "சுயவிவரம் & அமைப்புகள்",
            AppLanguage.MALAYALAM to "പ്രൊഫൈൽ & ക്രമീകരണങ്ങൾ"
        ),
        "create_post" to mapOf(
            AppLanguage.ENGLISH to "Share Farming Experience or Query",
            AppLanguage.TELUGU to "పంట అనుభవం లేదా సందేహం పోస్ట్ చేయండి",
            AppLanguage.HINDI to "खेती का अनुभव या सवाल साझा करें",
            AppLanguage.KANNADA to "ಕೃಷಿ ಅನುಭವ ಅಥವಾ ಪ್ರಶ್ನೆಯನ್ನು ಹಂಚಿಕೊಳ್ಳಿ",
            AppLanguage.TAMIL to "விவசாய அனுபவம் அல்லது கேள்வியைப் பகிர்க",
            AppLanguage.MALAYALAM to "കൃഷി അനുഭവമോ സംശയമോ പങ്കുവെക്കുക"
        ),
        "post_btn" to mapOf(
            AppLanguage.ENGLISH to "Post Update",
            AppLanguage.TELUGU to "పోస్ట్ చేయండి",
            AppLanguage.HINDI to "पोस्ट करें",
            AppLanguage.KANNADA to "ಪೋಸ್ಟ್ ಮಾಡಿ",
            AppLanguage.TAMIL to "பதிவிடுக",
            AppLanguage.MALAYALAM to "പോസ്റ്റ് ചെയ്യുക"
        ),
        "crop_category" to mapOf(
            AppLanguage.ENGLISH to "Crop / Topic Category",
            AppLanguage.TELUGU to "పంట / అంశం విభాగం",
            AppLanguage.HINDI to "फसल / विषय श्रेणी",
            AppLanguage.KANNADA to "ಬೆಳೆ / ವಿಷಯ ವಿಭಾಗ",
            AppLanguage.TAMIL to "பயிர் / தலைப்பு வகை",
            AppLanguage.MALAYALAM to "വിള / വിഷയം വിഭാഗം"
        ),
        "all_crops" to mapOf(
            AppLanguage.ENGLISH to "All Crops",
            AppLanguage.TELUGU to "అన్ని పంటలు",
            AppLanguage.HINDI to "सभी फसलें",
            AppLanguage.KANNADA to "ಎಲ್ಲಾ ಬೆಳೆಗಳು",
            AppLanguage.TAMIL to "அனைத்து பயிர்கள்",
            AppLanguage.MALAYALAM to "എല്ലാ വിളകളും"
        ),
        "like" to mapOf(
            AppLanguage.ENGLISH to "Helpful",
            AppLanguage.TELUGU to "ఉపయోగకరం",
            AppLanguage.HINDI to "मददगार",
            AppLanguage.KANNADA to "ಉಪಯುಕ್ತ",
            AppLanguage.TAMIL to "பயனுள்ளது",
            AppLanguage.MALAYALAM to "ഉപകാരപ്രദം"
        ),
        "comment" to mapOf(
            AppLanguage.ENGLISH to "Advice / Comment",
            AppLanguage.TELUGU to "సలహా / వ్యాఖ్య",
            AppLanguage.HINDI to "सुझाव / टिप्पणी",
            AppLanguage.KANNADA to "ಸಲಹೆ / ಟಿಪ್ಪಣಿ",
            AppLanguage.TAMIL to "ஆலோசனை / கருத்து",
            AppLanguage.MALAYALAM to "അഭിപ്രായം"
        ),
        "bookmark" to mapOf(
            AppLanguage.ENGLISH to "Save Tip",
            AppLanguage.TELUGU to "భద్రపరుచు",
            AppLanguage.HINDI to "सुरक्षित करें",
            AppLanguage.KANNADA to "ಉಳಿಸಿ",
            AppLanguage.TAMIL to "சேமிக்க",
            AppLanguage.MALAYALAM to "സേവ് ചെയ്യുക"
        ),
        "connect" to mapOf(
            AppLanguage.ENGLISH to "Connect",
            AppLanguage.TELUGU to "కనెక్ట్ అవ్వండి",
            AppLanguage.HINDI to "जुड़ें",
            AppLanguage.KANNADA to "ಸಂಪರ್ಕಿಸಿ",
            AppLanguage.TAMIL to "இணையவும்",
            AppLanguage.MALAYALAM to "ബന്ധപ്പെടുക"
        ),
        "connected" to mapOf(
            AppLanguage.ENGLISH to "Connected",
            AppLanguage.TELUGU to "కనెక్ట్ అయ్యారు",
            AppLanguage.HINDI to "जुड़े हुए हैं",
            AppLanguage.KANNADA to "ಸಂಪರ್ಕಿಸಲಾಗಿದೆ",
            AppLanguage.TAMIL to "இணைக்கப்பட்டது",
            AppLanguage.MALAYALAM to "ബന്ധപ്പെട്ടു"
        ),
        "pending" to mapOf(
            AppLanguage.ENGLISH to "Pending Request",
            AppLanguage.TELUGU to "పెండింగ్‌లో ఉంది",
            AppLanguage.HINDI to "अनुरोध भेजा गया",
            AppLanguage.KANNADA to "ಬಾಕಿ ವಿನಂತಿ",
            AppLanguage.TAMIL to "நிலுவையில் உள்ளது",
            AppLanguage.MALAYALAM to "അപേക്ഷ അയച്ചു"
        ),
        "accept" to mapOf(
            AppLanguage.ENGLISH to "Accept",
            AppLanguage.TELUGU to "అంగీకరించండి",
            AppLanguage.HINDI to "स्वीकारें",
            AppLanguage.KANNADA to "ಸ್ವೀಕರಿಸಿ",
            AppLanguage.TAMIL to "ஏற்கவும்",
            AppLanguage.MALAYALAM to "സ്വീകരിക്കുക"
        ),
        "reject" to mapOf(
            AppLanguage.ENGLISH to "Decline",
            AppLanguage.TELUGU to "తిరస్కరించండి",
            AppLanguage.HINDI to "अस्वीकार करें",
            AppLanguage.KANNADA to "ತಿರಸ್ಕರಿಸಿ",
            AppLanguage.TAMIL to "நிராகரிக்க",
            AppLanguage.MALAYALAM to "നിരസിക്കുക"
        ),
        "block" to mapOf(
            AppLanguage.ENGLISH to "Block Farmer",
            AppLanguage.TELUGU to "బ్లాక్ చేయండి",
            AppLanguage.HINDI to "ब्लॉक करें",
            AppLanguage.KANNADA to "ನಿರ್ಬಂಧಿಸಿ",
            AppLanguage.TAMIL to "முடக்கவும்",
            AppLanguage.MALAYALAM to "ബ്ലോക്ക് ചെയ്യുക"
        ),
        "report" to mapOf(
            AppLanguage.ENGLISH to "Report Content",
            AppLanguage.TELUGU to "ఫిర్యాదు చేయండి",
            AppLanguage.HINDI to "रिपोर्ट करें",
            AppLanguage.KANNADA to "ವರದಿ ಮಾಡಿ",
            AppLanguage.TAMIL to "புகாரளிக்கவும்",
            AppLanguage.MALAYALAM to "റിപ്പോർട്ട് ചെയ്യുക"
        ),
        "admin_moderation" to mapOf(
            AppLanguage.ENGLISH to "Admin Moderation Desk",
            AppLanguage.TELUGU to "అడ్మిన్ నియంత్రణ కేంద్రం",
            AppLanguage.HINDI to "प्रशासक मॉडरेशन डेस्क",
            AppLanguage.KANNADA to "ನಿರ್ವಾಹಕ ಮಾಡರೇಶನ್",
            AppLanguage.TAMIL to "நிர்வாகி மதிப்பாய்வு",
            AppLanguage.MALAYALAM to "അഡ്മിൻ മോഡറേഷൻ"
        ),
        "privacy_settings" to mapOf(
            AppLanguage.ENGLISH to "Privacy & Security",
            AppLanguage.TELUGU to "గోప్యత & భద్రత",
            AppLanguage.HINDI to "गोपनीयता और सुरक्षा",
            AppLanguage.KANNADA to "ಗೌಪ್ಯತೆ ಮತ್ತು ಭದ್ರತೆ",
            AppLanguage.TAMIL to "தனியுரிமை & பாதுகாப்பு",
            AppLanguage.MALAYALAM to "സ്വകാര്യത & സുരക്ഷ"
        ),
        "search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search farmers, crops, or districts...",
            AppLanguage.TELUGU to "రైతులు, పంటలు లేదా జిల్లాల కోసం శోధించండి...",
            AppLanguage.HINDI to "किसान, फसल या जिला खोजें...",
            AppLanguage.KANNADA to "ರೈತರು, ಬೆಳೆಗಳು ಅಥವಾ ಜಿಲ್ಲೆಗಳನ್ನು ಹುಡುಕಿ...",
            AppLanguage.TAMIL to "விவசாயிகள், பயிர்களைத் தேடுங்கள்...",
            AppLanguage.MALAYALAM to "കർഷകരെയും വിളകളെയും തിരയുക..."
        ),
        "send_message" to mapOf(
            AppLanguage.ENGLISH to "Type farming message or crop advice...",
            AppLanguage.TELUGU to "వ్యవసాయ సందేశం లేదా సలహా రాయండి...",
            AppLanguage.HINDI to "खेती का संदेश या सलाह लिखें...",
            AppLanguage.KANNADA to "ಕೃಷಿ ಸಂದೇಶ ಅಥವಾ ಸಲಹೆಯನ್ನು ಬರೆಯಿರಿ...",
            AppLanguage.TAMIL to "செய்தியை தட்டச்சு செய்யவும்...",
            AppLanguage.MALAYALAM to "സന്ദേശം ടൈപ്പ് ചെയ്യുക..."
        ),
        "logout" to mapOf(
            AppLanguage.ENGLISH to "Sign Out",
            AppLanguage.TELUGU to "లాగౌట్",
            AppLanguage.HINDI to "साइन आउट",
            AppLanguage.KANNADA to "ಸೈನ್ ಔಟ್",
            AppLanguage.TAMIL to "வெளியேறு",
            AppLanguage.MALAYALAM to "പുറത്തുകടക്കുക"
        ),
        "edit_profile" to mapOf(
            AppLanguage.ENGLISH to "Edit Profile",
            AppLanguage.TELUGU to "ప్రొఫైల్ సవరించండి",
            AppLanguage.HINDI to "प्रोफ़ाइल संपादित करें",
            AppLanguage.KANNADA to "ಪ್ರೊಫೈಲ್ ಸಂಪಾದಿಸಿ",
            AppLanguage.TAMIL to "சுயவிவரத்தைத் திருத்து",
            AppLanguage.MALAYALAM to "പ്രൊഫൈൽ എഡിറ്റ് ചെയ്യുക"
        ),
        "save_profile" to mapOf(
            AppLanguage.ENGLISH to "Save Profile",
            AppLanguage.TELUGU to "ప్రొఫైల్ సేవ్ చేయండి",
            AppLanguage.HINDI to "प्रोफ़ाइल सहेजें",
            AppLanguage.KANNADA to "ಪ್ರೊಫೈಲ್ ಉಳಿಸಿ",
            AppLanguage.TAMIL to "சுயவிவரத்தை சேமி",
            AppLanguage.MALAYALAM to "പ്രൊഫൈൽ സേവ് ചെയ്യുക"
        ),
        "farming_interests" to mapOf(
            AppLanguage.ENGLISH to "Farming Interests & Specialties",
            AppLanguage.TELUGU to "వ్యవసాయ ఆసక్తులు & నైపుణ్యాలు",
            AppLanguage.HINDI to "खेती की रुचियां व विशेषताएं",
            AppLanguage.KANNADA to "ಕೃಷಿ ಆಸಕ್ತಿಗಳು ಮತ್ತು ವಿಶೇಷತೆಗಳು",
            AppLanguage.TAMIL to "விவசாய ஆர்வங்கள் & சிறப்புகள்",
            AppLanguage.MALAYALAM to "കൃഷി താൽപ്പര്യങ്ങളും സവിശേഷതകളും"
        ),
        "soil_type" to mapOf(
            AppLanguage.ENGLISH to "Soil Type",
            AppLanguage.TELUGU to "నేల రకం",
            AppLanguage.HINDI to "मिट्टी का प्रकार",
            AppLanguage.KANNADA to "ಮಣ್ಣಿನ ವಿಧ",
            AppLanguage.TAMIL to "மண் வகை",
            AppLanguage.MALAYALAM to "മണ്ണ് തരം"
        ),
        "irrigation_source" to mapOf(
            AppLanguage.ENGLISH to "Irrigation Source",
            AppLanguage.TELUGU to "సాగునీటి వనరు",
            AppLanguage.HINDI to "सिंचाई स्रोत",
            AppLanguage.KANNADA to "ನೀರಾವರಿ ಮೂಲ",
            AppLanguage.TAMIL to "பாசன ஆதாரம்",
            AppLanguage.MALAYALAM to "ജലസേചന സ്രോതസ്സ്"
        ),
        "my_crops" to mapOf(
            AppLanguage.ENGLISH to "My Crops & Fields",
            AppLanguage.TELUGU to "నా పంటలు & పొలాలు",
            AppLanguage.HINDI to "मेरी फसलें और खेत",
            AppLanguage.KANNADA to "ನನ್ನ ಬೆಳೆಗಳು ಮತ್ತು ಜಮೀನು",
            AppLanguage.TAMIL to "என் பயிர்கள் மற்றும் வயல்கள்",
            AppLanguage.MALAYALAM to "എന്റെ വിളകളും വയലുകളും"
        ),
        "add_crop" to mapOf(
            AppLanguage.ENGLISH to "Add Crop Detail",
            AppLanguage.TELUGU to "పంట వివరాలను జోడించండి",
            AppLanguage.HINDI to "फसल विवरण जोड़ें",
            AppLanguage.KANNADA to "ಬೆಳೆ ವಿವರವನ್ನು ಸೇರಿಸಿ",
            AppLanguage.TAMIL to "பயிர் விவரத்தைச் சேர்க்கவும்",
            AppLanguage.MALAYALAM to "വിള വിവരങ്ങൾ ചേർക്കുക"
        ),
        "crop_name" to mapOf(
            AppLanguage.ENGLISH to "Crop Name",
            AppLanguage.TELUGU to "పంట పేరు",
            AppLanguage.HINDI to "फसल का नाम",
            AppLanguage.KANNADA to "ಬೆಳೆಯ ಹೆಸರು",
            AppLanguage.TAMIL to "பயிர் பெயர்",
            AppLanguage.MALAYALAM to "വിളയുടെ പേര്"
        ),
        "variety" to mapOf(
            AppLanguage.ENGLISH to "Variety / Seed Type",
            AppLanguage.TELUGU to "వంగడం / విత్తన రకం",
            AppLanguage.HINDI to "किस्म / बीज का प्रकार",
            AppLanguage.KANNADA to "ತಳಿ / ಬೀಜದ ಪ್ರಕಾರ",
            AppLanguage.TAMIL to "வகை / விதை வகை",
            AppLanguage.MALAYALAM to "ഇനം / വിത്ത് തരം"
        ),
        "season" to mapOf(
            AppLanguage.ENGLISH to "Season",
            AppLanguage.TELUGU to "రుతువు",
            AppLanguage.HINDI to "मौसम",
            AppLanguage.KANNADA to "ಋತು",
            AppLanguage.TAMIL to "பருவம்",
            AppLanguage.MALAYALAM to "സീസൺ"
        ),
        "sowing_date" to mapOf(
            AppLanguage.ENGLISH to "Sowing Month / Date",
            AppLanguage.TELUGU to "విత్తిన నెల / తేదీ",
            AppLanguage.HINDI to "बुवाई का महीना / तिथि",
            AppLanguage.KANNADA to "ಬಿತ್ತನೆ ತಿಂಗಳು / ದಿನಾಂಕ",
            AppLanguage.TAMIL to "விதைத்த மாதம் / தேதி",
            AppLanguage.MALAYALAM to "വിതച്ച മാസം / തീയതി"
        ),
        "growth_stage" to mapOf(
            AppLanguage.ENGLISH to "Growth Stage",
            AppLanguage.TELUGU to "పంట పెరుగుదల దశ",
            AppLanguage.HINDI to "फसल विकास चरण",
            AppLanguage.KANNADA to "ಬೆಳವಣಿಗೆಯ ಹಂತ",
            AppLanguage.TAMIL to "வளர்ச்சி நிலை",
            AppLanguage.MALAYALAM to "വളർച്ച ഘട്ടം"
        ),
        "expected_yield" to mapOf(
            AppLanguage.ENGLISH to "Expected Yield (Quintals)",
            AppLanguage.TELUGU to "అంచనా దిగుబడి (క్వింటాళ్లు)",
            AppLanguage.HINDI to "अनुमानित उपज (क्विंटल)",
            AppLanguage.KANNADA to "ನಿರೀಕ್ಷಿತ ಇಳುವರಿ (ಕ್ವಿಂಟಾಲ್)",
            AppLanguage.TAMIL to "எதிர்பார்க்கப்படும் மகசூல் (குவிண்டால்)",
            AppLanguage.MALAYALAM to "പ്രതീക്ഷിക്കുന്ന വിളവ് (ക്വിന്റൽ)"
        ),
        "nearby_farmers" to mapOf(
            AppLanguage.ENGLISH to "Nearby Farmers",
            AppLanguage.TELUGU to "సమీపంలోని రైతులు",
            AppLanguage.HINDI to "आस-पास के किसान",
            AppLanguage.KANNADA to "ಹತ್ತಿರದ ರೈತರು",
            AppLanguage.TAMIL to "அருகிலுள்ள விவசாயிகள்",
            AppLanguage.MALAYALAM to "അടുത്തുള്ള കർഷകർ"
        ),
        "detect_location" to mapOf(
            AppLanguage.ENGLISH to "Detect GPS Location",
            AppLanguage.TELUGU to "జీపీఎస్ లొకేషన్ గుర్తించండి",
            AppLanguage.HINDI to "जीपीएस स्थान खोजें",
            AppLanguage.KANNADA to "ಜಿಪಿಎಸ್ ಸ್ಥಳ ಗುರುತಿಸಿ",
            AppLanguage.TAMIL to "ஜிபிஎஸ் இருப்பிடத்தை கண்டறிக",
            AppLanguage.MALAYALAM to "ജിപിഎസ് സ്ഥാനം കണ്ടെത്തുക"
        ),
        "filter_nearby" to mapOf(
            AppLanguage.ENGLISH to "Near Me (<50 km)",
            AppLanguage.TELUGU to "నాకు దగ్గరగా (<50 కి.మీ)",
            AppLanguage.HINDI to "मेरे पास (<50 किमी)",
            AppLanguage.KANNADA to "ನನ್ನ ಹತ್ತಿರ (<50 ಕಿ.ಮೀ)",
            AppLanguage.TAMIL to "எனக்கு அருகில் (<50 கி.மீ)",
            AppLanguage.MALAYALAM to "എന്റെ അടുത്ത് (<50 കി.മീ)"
        ),
        "all_farmers" to mapOf(
            AppLanguage.ENGLISH to "All Farmers",
            AppLanguage.TELUGU to "అందరు రైతులు",
            AppLanguage.HINDI to "सभी किसान",
            AppLanguage.KANNADA to "ಎಲ್ಲಾ ರೈತರು",
            AppLanguage.TAMIL to "அனைத்து விவசாயிகள்",
            AppLanguage.MALAYALAM to "എല്ലാ കർഷകരും"
        ),
        "distance_away" to mapOf(
            AppLanguage.ENGLISH to "km away",
            AppLanguage.TELUGU to "కి.మీ దూరంలో",
            AppLanguage.HINDI to "किमी दूर",
            AppLanguage.KANNADA to "ಕಿ.ಮೀ ದೂರದಲ್ಲಿ",
            AppLanguage.TAMIL to "கி.மீ தூரத்தில்",
            AppLanguage.MALAYALAM to "കി.മീ അകലെ"
        ),
        "share_app" to mapOf(
            AppLanguage.ENGLISH to "Share Agribook",
            AppLanguage.TELUGU to "అగ్రిబుక్ షేర్ చేయండి",
            AppLanguage.HINDI to "एग्रीबुक शेयर करें",
            AppLanguage.KANNADA to "ಅಗ್ರಿಬುಕ್ ಹಂಚಿಕೊಳ್ಳಿ",
            AppLanguage.TAMIL to "அக்ரிபுக் பகிரவும்",
            AppLanguage.MALAYALAM to "അഗ്രിബുക്ക് ഷെയർ ചെയ്യുക"
        ),
        "share_post" to mapOf(
            AppLanguage.ENGLISH to "Share Advice",
            AppLanguage.TELUGU to "సలహా షేర్ చేయండి",
            AppLanguage.HINDI to "सलाह साझा करें",
            AppLanguage.KANNADA to "ಸಲಹೆ ಹಂಚಿಕೊಳ್ಳಿ",
            AppLanguage.TAMIL to "ஆலோசனை பகிர்க",
            AppLanguage.MALAYALAM to "ഉപദേശം പങ്കിടുക"
        ),
        "share_profile" to mapOf(
            AppLanguage.ENGLISH to "Share Farm Profile",
            AppLanguage.TELUGU to "రైతు ప్రొఫైల్ షేర్ చేయండి",
            AppLanguage.HINDI to "कृषि प्रोफ़ाइल साझा करें",
            AppLanguage.KANNADA to "ರೈತ ಪ್ರೊಫೈಲ್ ಹಂಚಿಕೊಳ್ಳಿ",
            AppLanguage.TAMIL to "சுயவிவரத்தைப் பகிரவும்",
            AppLanguage.MALAYALAM to "പ്രൊഫൈൽ പങ്കിടുക"
        ),
        "copy_link" to mapOf(
            AppLanguage.ENGLISH to "Copy Public Link",
            AppLanguage.TELUGU to "పబ్లిక్ లింక్ కాపీ చేయండి",
            AppLanguage.HINDI to "सार्वजनिक लिंक कॉपी करें",
            AppLanguage.KANNADA to "ಸಾರ್ವಜನಿಕ ಲಿಂಕ್ ನಕಲಿಸಿ",
            AppLanguage.TAMIL to "இணைப்பை நகலெடு",
            AppLanguage.MALAYALAM to "പബ്ലിക് ലിങ്ക് കോപ്പി ചെയ്യുക"
        ),
        "link_copied" to mapOf(
            AppLanguage.ENGLISH to "Link copied to clipboard!",
            AppLanguage.TELUGU to "లింక్ క్లిప్‌బోర్డ్‌కు కాపీ చేయబడింది!",
            AppLanguage.HINDI to "లింక్ క్లిప్‌బోర్డ్‌కు కాపీ చేయబడింది!",
            AppLanguage.KANNADA to "ಲಿಂಕ್ ನಕಲಿಸಲಾಗಿದೆ!",
            AppLanguage.TAMIL to "இணைப்பு நகலெடுக்கப்பட்டது!",
            AppLanguage.MALAYALAM to "ലിങ്ക് കോപ്പി ചെയ്തു!"
        ),
        "invite_friends" to mapOf(
            AppLanguage.ENGLISH to "Invite Fellow Farmers",
            AppLanguage.TELUGU to "తోటి రైతులకు ఆహ్వానం పంపండి",
            AppLanguage.HINDI to "साथी किसानों को आमंत्रित करें",
            AppLanguage.KANNADA to "ಸಹ ರೈತರನ್ನು ಆಹ್ವಾನಿಸಿ",
            AppLanguage.TAMIL to "சக விவசாயிகளை அழைக்கவும்",
            AppLanguage.MALAYALAM to "സഹ കർഷകരെ ക്ഷണിക്കുക"
        )
    )
}
