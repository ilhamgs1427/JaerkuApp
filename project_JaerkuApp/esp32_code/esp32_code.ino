#include <Arduino.h>
#include "CQRobotTDS.h"
#include <WiFi.h>
#include <HTTPClient.h>

#define TDS_SENSOR_PIN 27
const int pinSensorTurbidity = 35;
const int ph_pin = 34;

void setup() {
  pinMode(ph_pin, INPUT);
  Serial.begin(115200);
  konfigurasiWiFi("R Dosen TIF", "Gakpakepassword");
}

void loop() {
  int nilaiADC = analogRead(pinSensorTurbidity);
  float nilaiNTU = konversiADCKeNTU(nilaiADC); // Nilai Turbidity

  Serial.println("=========================");
  Serial.print("Nilai NTU: ");
  Serial.println(nilaiNTU);

  float tdsValue = readTDS(nilaiADC); // Nilai TDS
  Serial.print("TDS Value: ");
  Serial.println(tdsValue);

  int nilai_analog_pH = analogRead(ph_pin); // Nilai pH
  Serial.print("Nilai ADC pH: ");
  Serial.println(nilai_analog_pH);

  float teganganPH = (3.3 / 4095.0) * nilai_analog_pH;
  Serial.print("Tegangan pH: ");
  Serial.println(teganganPH, 3);

  float pH_step = 0.0; // Variable untuk perhitungan pH (disesuaikan dengan kalibrasi)
  float phValue = 0.0; // Nilai pH Cairan

  // Perhitungan nilai pH
  // Disesuaikan dengan kalibrasi nilai pH yang tepat
  float PH4 = 3.16; // Perbaiki nilai PH4 sesuai kalibrasi
  float PH7 = 2.66; // Perbaiki nilai PH7 sesuai kalibrasi
  float PH9 = 2.33; // Perbaiki nilai PH9 sesuai kalibrasi

  pH_step = (PH4 - PH7) / 3;
  phValue = 7.00 - ((teganganPH - PH7) / pH_step);

  Serial.print("Nilai pH Cairan: ");
  Serial.println(phValue, 2);

  String fuzzyValue = fuzzyLogic(phValue, nilaiNTU, tdsValue);
  Serial.println("Hasil Fuzzy: " + fuzzyValue);


  // Kirim nilai NTU, TDS, dan pH ke API PHP
  kirimKeAPI("http://172.17.2.121/sensor/api.php?nilaiNTU=" + String(nilaiNTU) + "&nilaiTDS=" + String(tdsValue) + "&nilaiPH=" + String(phValue) + "&nilaiFuzzy="  + String(fuzzyValue));


  Serial.println("=========================");
  delay(60000);
}

float konversiADCKeNTU(int nilaiADC) {
  float faktorKonversi = 0.1;
  float offset = 0.0;
  float nilaiNTU = (float)nilaiADC * faktorKonversi + offset;
  return nilaiNTU;
}

float readTDS(int nilaiADC) {
  // Ganti nilai-nilai berikut dengan karakteristik sensor TDS Anda
  int nilaiMinADC = 1204; // Nilai ADC minimum yang dibaca dari sensor
  int nilaiMaxADC = 4095; // Nilai ADC maksimum yang dibaca dari sensor
  float tdsMinValue = 100.0; // Nilai TDS minimum yang diukur oleh sensor
  float tdsMaxValue = 1500.0; // Nilai TDS maksimum yang diukur oleh sensor
  
  // Lakukan konversi nilai ADC ke nilai TDS menggunakan metode yang sesuai
  float tdsValue = map(nilaiADC, nilaiMinADC, nilaiMaxADC, tdsMinValue, tdsMaxValue);
  return tdsValue;
}


void konfigurasiWiFi(const char *ssid, const char *password) {
  Serial.print("Menghubungkan ke WiFi...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(5000);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi tersambung");
  Serial.println("IP address: " + WiFi.localIP().toString());
}

void kirimKeAPI(String url) {
  HTTPClient httpClient;
  // Set URL API PHP
  httpClient.begin(url);
  // Lakukan permintaan HTTP GET
  int httpCode = httpClient.GET();
  // Periksa kode respons HTTP
  if (httpCode == 200) {
    // Data berhasil dikirim
    Serial.println("Data berhasil dikirim");
  } else {
    // Terjadi kesalahan
    Serial.println("Gagal mengirim data");
    Serial.println("Kode respons: " + String(httpCode));
  }
  // Tutup koneksi HTTP
  httpClient.end();
}
float lowMembership(float value, float low, float mid) {
    return fmax(0.0f, 1.0f - (value - low) / (mid - low));
}

float midMembership(float value, float low, float mid, float high) {
    float min1 = fmin(1.0f, (value - low) / (mid - low));
    float min2 = fmin(1.0f, (value - mid) / (high - mid));
    return fmax(0.0f, fmin(min1, min2));
}

float highMembership(float value, float mid, float high) {
    return fmax(0.0f, 1.0f - (value - mid) / (high - mid));
}

// Logika Fuzzy Mamdani
String fuzzyLogic(float pH, float turbidity, float tds) {
    // Aturan fuzzy
    float rule1 = fmin(fmin(lowMembership(pH, 7, 8), highMembership(turbidity, 30, 255)), lowMembership(tds, 0, 500));
    float rule2 = fmin(fmin(highMembership(pH, 7, 8), highMembership(turbidity, 30, 255)), highMembership(tds, 1500, 4095));
    float rule3 = fmin(fmin(midMembership(pH, 7, 8, 8), highMembership(turbidity, 30, 255)), midMembership(tds, 500, 1500, 1500));
    float rule4 = fmin(fmin(lowMembership(pH, 7, 8), midMembership(turbidity, 0, 30, 30)), lowMembership(tds, 0, 500));
    float rule5 = fmin(fmin(highMembership(pH, 7, 8), midMembership(turbidity, 0, 30, 30)), highMembership(tds, 1500, 4095));
    float rule6 = fmin(fmin(midMembership(pH, 7, 8, 8), midMembership(turbidity, 0, 30, 30)), midMembership(tds, 500, 1500, 1500));
    float rule7 = fmin(fmin(lowMembership(pH, 7, 8), highMembership(turbidity, 30, 255)), highMembership(tds, 1500, 4095));
    float rule8 = fmin(fmin(highMembership(pH, 7, 8), highMembership(turbidity, 30, 255)), lowMembership(tds, 0, 500));
    float rule9 = fmin(fmin(midMembership(pH, 7, 8, 8), highMembership(turbidity, 30, 255)), highMembership(tds, 500, 1500));
 

    // Menghitung hasil inferensi
    float tidaknormal = fmax(rule1, fmax(rule2, fmax(rule3, fmax(rule4, fmax(rule7, fmax(rule8, rule9))))));
    float normal = fmax(rule5, fmax(rule6, rule9));
    // Menentukan label berdasarkan hasil inferensi
    String label = "";
    if (normal > 0.5) {
        label = "1";
    } else if (tidaknormal > 0.5) {
        label = "2";
    } 
    
    Serial.println("Hasil Inferensi:");
    Serial.println("Buruk: " + String(normal, 2));
    Serial.println("Normal: " + String(tidaknormal, 2));
    return label;
}

