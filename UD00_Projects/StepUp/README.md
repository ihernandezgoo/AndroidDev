# StepUp 🚶‍♂️
StepUp ohiturak eta jarduera fisikoa jarraitzeko Android aplikazio bat da, Androiden garapenaren oinarriak ikasteko hezkuntza-proiektu gisa garatua.

## 📱 Deskribapena
StepUp-ek erabiltzaileei euren eguneroko jarduera kontrolatzea ahalbidetzen die Android gailuetan integratutako ohituren kontagailuaren bidez. Aplikazioak interfaze intuitibo bat eskaintzen du eguneroko aurrerapena ikusteko eta bizimodu aktiboagoa sustatzeko.

## ✨ Ezaugarriak
- 📊 Ohituren kontagailua denbora errealean
- 📈 Eguneroko aurrerapenaren eta estatistiken bistaratzea aurrerapen-barrekin
- 💾 Datu historikoen tokiko biltegiratzea
- 🎨 Erabiltzaile-interfaze modernoa eta moldakorra

## 🛠️ Erabilitako Teknologiak
- **Hizkuntza:** Kotlin
- **IDE:** Android Studio
- **Arkitektura:** MVVM (Model-View-ViewModel)
- **UI:** XML Layouts / Jetpack Compose
- **Sentsoreak:** Android Sensor Framework
- **Iraunkortasuna:** SharedPreferences / Room Database
- **Osagaiak:** 
  - LiveData
  - ViewModel
  - Material Design Components

## 📋 Aurretiazko Eskakizunak
- Android Studio Arctic Fox edo berriagoa
- Android SDK 24 (Android 7.0) edo handiagoa
- Android gailua edo emuladorea pausoen sentsoredun
- Kotlin eta Android-en oinarrizko ezagutzak

## 🚀 Instalazioa eta Konfigurazioa
1. **Biltegia klonatu:**
```bash
git clone https://github.com/ihernandezgoo/AndroidDev.git
cd AndroidDev/UD00_Projects/StepUp
```

2. **Proiektua Android Studio-n ireki:**
   - Ireki Android Studio
   - Hautatu "Open an existing project"
   - Joan StepUp karpetara
   - Itxaron Gradle-k dependentziak sinkronizatu arte

3. **Gailua konfiguratu:**
   - Konektatu gailu fisiko bat USB arazketa aktibatuta duela
   - Edo konfiguratu Android emuladorea (AVD)

4. **Konpilatu eta exekutatu:**
   - Egin klik "Run" botoian (▶️) Android Studio-n
   - Hautatu zure gailua/emuladorea
   - Itxaron aplikazioa instalatu eta abiarazi arte

## 📱 Beharrezko Baimenak
```xml
<uses-permission android:name="android.permission.ACTIVITY_RECOGNITION" />
<uses-feature android:name="android.hardware.sensor.stepcounter" />
```

**Oharra:** Android 10 (API 29) eta berriagoetarako, exekuzio-denboran baimena behar da ACTIVITY_RECOGNITION-rako.

### Datuen Biltegiratzea
Datuak lokalean gordetzen dira erabiltzailearen jarduera-historiala mantentzeko aplikazioa itxita dagoenean ere.

### Jakinarazpenak
Aukerako gogorarazpen-sistema erabiltzailea bere eguneroko helburuak betetzen laguntzeko.

## 🧪 Probak
Unitate probak exekutatzeko:
```bash
./gradlew test
```

Instrumentatutako probak exekutatzeko:
```bash
./gradlew connectedAndroidTest
```

## 🐛 Arazo Ezagunak
- Pausoen sentsorea ez dago emulatzaile guztietan eskuragarri
- Kontagailuaren zehaztasuna gailuaren hardwarearen araberakoa da
- Bigarren planoan, sistemak sentsorearen sarbidea mugatu dezake bateria aurrezteko

## 🔮 Etorkizuneko Hobekuntzak
- [ ] Google Fit-ekin integrazioa
- [ ] Asteko/hileko aurrerapen-grafikoak
- [ ] Lorpen eta txapa sistema
- [ ] Lorpenak sare sozialetan partekatzea
- [ ] Erabiltzaileen arteko erronka modua
- [ ] Datuak CSV-ra esportatzea

## 👥 Ekarpenak
Ekarpenak ongi etorriak dira. Mesedez:

1. Egin biltegiko fork bat
2. Sortu adar bat zure ezaugarriarentzat (`git checkout -b feature/ezaugarri-berria`)
3. Egin zure aldaketak eta commit-a (`git commit -am 'Ezaugarri berria gehitzen'`)
4. Igo aldaketak zure adarrera (`git push origin feature/ezaugarri-berria`)
5. Ireki Pull Request bat

## 📄 Lizentzia
Proiektu hau kode irekikoa da eta [MIT Lizentziaren](LICENSE) pean eskuragarri dago.

## 👨‍💻 Egilea
**ihernandezgoo**
- GitHub: [@ihernandezgoo](https://github.com/ihernandezgoo)

⭐ Proiektu honek lagundu badizu, eman izar bat GitHub-en!
