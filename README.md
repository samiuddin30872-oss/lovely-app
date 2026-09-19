# Lovely Enterprises — Android App (Source Code)

यह एक तैयार Android app project है जो आपकी website (CCTV, AC, फ्रिज, वॉशिंग मशीन व इलेक्ट्रिकल सर्विस) को app के अंदर दिखाता है, साथ ही:
- नीचे-दाईं तरफ हमेशा दिखने वाला **Call Now** button (सीधे डायलर खोलता है)
- **WhatsApp** button (सीधे WhatsApp चैट खोलता है)
- इंटरनेट न होने पर "No internet" स्क्रीन + Retry button
- नीचे खींचकर page refresh करने की सुविधा

## इसे कौन इस्तेमाल कर सकता है
यह raw source code है — इसे चलाने/बनाने (build करने) के लिए **Android Studio** चाहिए। अगर आपको या आपकी जान-पहचान में किसी को coding नहीं आती, तो यह folder किसी local Android developer या freelancer को दे दें — यह एक बहुत छोटा, आसान project है (ज़्यादातर developer इसे 1-2 घंटे में तैयार कर देंगे)।

## Build करने के step (Developer के लिए)

1. **Android Studio** डाउनलोड करें: https://developer.android.com/studio
2. इस पूरे folder (`lovely-app`) को Android Studio में **Open** करें
3. Android Studio अपने आप ज़रूरी चीज़ें (Gradle) डाउनलोड कर लेगा (internet चाहिए)
4. `app/src/main/res/values/strings.xml` में:
   - `website_url` — अभी इसमें आपका artifact link डला है। अगर आगे चलकर आप अपना खुद का domain खरीदते हैं (जैसे `lovelyenterprises.in`), तो यहां वही नया लिंक डाल दें।
   - फ़ोन नंबर पहले से भरे हैं, ज़रूरत हो तो बदल सकते हैं।
5. App icon अभी एक साधारण placeholder (amber रंग का bolt icon) है — असली logo लगाने के लिए Android Studio के **Image Asset** tool का उपयोग करें (Right-click `res` → New → Image Asset)।
6. Menu में **Build → Generate Signed Bundle / APK** चुनें, एक नया **signing key** बनाएं (इसे सुरक्षित रखें — भविष्य में हर update के लिए यही key चाहिए होगी)
7. इससे एक `.aab` (Android App Bundle) फाइल बनेगी — यही फाइल Play Store पर अपलोड होती है

## Google Play Store पर publish करने के step

1. https://play.google.com/console पर जाकर **Google Play Developer account** बनाएं — **$25 (एक बार का शुल्क)**, Google/Debit-Credit card चाहिए
2. एक नया App बनाएं, नाम दें: "Lovely Enterprises"
3. **Privacy Policy** का लिंक देना ज़रूरी है — इस folder में `PRIVACY_POLICY.md` का draft है, इसे अपनी website पर एक page के रूप में डालें और वह लिंक Play Console में दें
4. App के screenshots, short description, icon अपलोड करें
5. Content rating questionnaire भरें (यह एक सामान्य business/utility app है)
6. बनी हुई `.aab` फाइल अपलोड करें और **Submit for review** करें
7. Google आमतौर पर **3-7 दिन** में review करके app को live कर देता है

## ज़रूरी बात
Google का एक नियम है — अगर app सिर्फ website को दिखाता है और कोई extra काम नहीं करता, तो कभी-कभी reject हो सकता है ("minimum functionality" policy)। इसलिए इस app में already Call button, WhatsApp button, offline screen जैसी असली app वाली चीज़ें जोड़ी गई हैं। फिर भी अगर पहली बार में reject हो, तो Google जो वजह बताएगा उसे ठीक करके दोबारा भेजा जा सकता है — यह आम बात है।

## 📱 Phone se hi build karna hai? (bina laptop ke)

Android Studio computer/laptop maangta hai — phone par nahi chalta. Lekin **GitHub** ki free service use karke, poora build phone ke browser se ho sakta hai (main is project me woh setup pehle se kar chuka hoon — `.github/workflows/build-apk.yml` file).

**Phone se karne ke steps:**

1. Phone ke browser me **github.com** par jaakar free account banayein
2. Upar-right "+" icon se **"New repository"** banayein — naam dein jaise `lovely-app`, aur **"Public"** ya "Private" select karein → Create
3. Us naye repository ke andar **"Add file" → "Upload files"** par click karein
4. Is zip ke andar ke saare files/folders (poora `lovely-app` folder ka content — `app`, `.github`, `gradle`, `build.gradle`, `settings.gradle`, `gradlew`, waghera sab) select karke upload karein, neeche "Commit changes" dabayein
   - *(Zip ko phone me pehle "Files" app se Extract/Unzip karna hoga taaki andar ke files individually select ho sakein — zyada tar phone file-manager apps me yeh "Extract" option milta hai)*
5. Upload hote hi upar **"Actions"** tab me jaayein — wahan build apne aap chalna shuru ho jaayega (2-5 minute lagte hain, green ✅ dikhega complete hone par)
6. Build complete hone ke baad usi Actions run ke andar niche **"Artifacts"** section me `lovely-enterprises-debug-apk` milega — usspar tap karke download kar lein
7. Yeh ek `.apk` file hai — seedhe apne phone par install karke **test** kar sakte hain (Settings me "Install unknown apps" allow karna padega pehli baar)

⚠️ **Zaroori baat:** Yeh tareeka sirf **testing ke liye APK** banata hai (unsigned/debug version) — apne phone par install karke dekhne ke liye bilkul theek hai. Lekin **Google Play Store par publish** karne ke liye "release" build par apni signing key (keystore) lagani padti hai — uske liye ya to kisi developer ki madad lein, ya GitHub par "Secrets" me apni keystore surakshit rakh kar workflow ko thoda aur badhana padega (yeh thoda technical step hai, chahen to iske liye bhi main aapko guide kar sakta hoon).

## Folder में क्या है
```
lovely-app/
├── .github/workflows/build-apk.yml → phone/cloud se auto-build (GitHub Actions)
├── app/
│   ├── build.gradle              → app की settings (version, permissions)
│   └── src/main/
│       ├── AndroidManifest.xml   → permissions व app info
│       ├── java/.../MainActivity.kt → पूरा app logic
│       └── res/                  → icon, रंग, text, layout
├── gradlew / gradlew.bat         → build चलाने वाली script
├── PRIVACY_POLICY.md             → Play Store के लिए ज़रूरी privacy policy draft
└── README.md                     → यही फाइल
```
