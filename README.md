# 📱 TravelTurkey – Android Uygulaması

> Türkiye’nin şehirlerini keşfet, önemli konumlarını incele, harita üzerinde yerlerini bul ve detaya ulaş!  
Bu Android uygulaması, Jetpack Compose kullanarak modern UI, MVVM ile yönetilen mimari ve Google Maps API entegrasyonu sunar.

---

## 📌 İçindekiler

- [Özellikler](#özellikler)
- [Ekran Görüntüleri](#ekran-görüntüleri)
- [Kullanılan Teknolojiler ve Mimariler](#kullanılan-teknolojiler-ve-mimariler)
- [Kurulum](#kurulum)
- [Proje Mimarisi](#proje-mimarisi)
- [Sayfalar ve Rotalar](#sayfalar-ve-rotalar)

---

## Özellikler

| Özellik | Açıklama |
|--------|----------|
| 🏠 Ana Sayfa | Şehirlerin listelendiği ekran, kart yapısı ile genişletilebilir |
| 📍 Şehir Harita Görünümü | Şehirdeki tüm konumları haritada ve yatay listede gösterir |
| 📌 Konum Detay Sayfası | Seçilen konuma ait açıklama ve görsel |
| 🗺️ Lokasyon Haritası | Tekil konum harita görünümü |
| 📲 Kullanıcı Konumu | Haritada kendi konumunuzu gösterme |
| ⚙️ İzin ve GPS Kontrolü | Konum izinleri ve sistem ayarlarına yönlendirme |
| 🔁 Sonsuz Scroll | Şehirler scroll ile yüklenmeye devam eder |

---

## Ekran Görüntüleri



## Kullanılan Teknolojiler ve Mimariler

| Kategori | Teknoloji |
|---------|-----------|
| UI | Jetpack Compose, Material3 |
| Navigasyon | Navigation-Compose |
| Harita | Google Maps Compose |
| DI | Hilt |
| Networking | Retrofit2 |
| Async | Kotlin Coroutines |
| Gelişim Modeli | MVVM + Clean Architecture |
| State Management | StateFlow, collectAsState() |
| Geri Bildirim | Dialog, Snackbar, Accompanist Permissions |
| Görsel | Coil (AsyncImage) |

---

## Kurulum

### 1️⃣ Projeyi klonla

```bash
git clone https://github.com/kullaniciadi/travelturkey.git
cd travelturkey
```
### 2️⃣ Google Maps API Anahtarını Ayarla
Uygulamada harita bileşenlerinin çalışabilmesi için bir Google Maps API anahtarına ihtiyacınız vardır.

📍 API Anahtarını local.properties dosyasına ekleyin
Proje dizininde yer alan local.properties dosyasını açın (veya yoksa oluşturun), aşağıdaki satırı ekleyin:
```bash
API_KEY=YOUR_GOOGLE_MAPS_API_KEY
```
### 3️⃣ Build Et & Çalıştır
 * Android Studio ile projeyi açın
 * Minimum SDK: API 24+
 * Compile SDK: 33+
 * Uygulamayı başlat: ▶️

---

## Proje Mimarisi
```bash
travelturkey/
├── common
├── data/
│   ├── remote/
│   │   ├── api
│   │   ├── model
│   │   └── repository
│   └── local/
│       ├── dao
│       ├── database
│       ├── entity
│       └── repository
├── di
├── ui/
│   ├── citymap
│   ├── component
│   ├── favorite
│   ├── home
│   ├── locationdetail
│   ├── locationmap
│   ├── navigation
│   ├── splash
│   └── theme
└── util
```

---

## Sayfalar ve Rotalar

| Sayfa             | Route                          | Açıklama                                      |
|-------------------|--------------------------------|-----------------------------------------------|
| **Splash**        | `splash`                       | Açılış animasyonu ekranı                      |
| **Home**          | `home`                         | Şehir listesi ekranı                          |
| **Location Detail** | `location_detail/{locationId}` | Seçilen konuma ait detay ekranı             |
| **Location Map**  | `location_map/{locationId}`    | Tek konum haritası görünümü                   |
| **City Map**      | `city_map/{cityIndex}`         | Şehirdeki tüm konumları ve carousel görünümü  |
| **Favorite**      | `favorite`                     | Favorilere eklenen konumların takip edildiği ekran |

