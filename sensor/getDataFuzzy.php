<?php
// Sesuaikan dengan informasi database Anda
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "jaerku";

// Buat koneksi ke database
$conn = new mysqli($servername, $username, $password, $dbname);

// Periksa koneksi
if ($conn->connect_error) {
    die("Koneksi ke database gagal: " . $conn->connect_error);
}  

$result = $conn->query("SELECT * FROM sensor_value ORDER BY ID DESC LIMIT 1");

$data = array();
$response = array();

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    
    $fuzzyValue = $row['fuzzy'];

    // Logika penentuan kondisi
    if ($fuzzyValue == 1) {
        $kondisi = "normal";
        $keterangan = "Jika kualitas air tambak mujair normal, maka akan berdampak positif pada pertumbuhan dan perkembangan ikan mujair. Ikan mujair akan dapat tumbuh dan berkembang dengan baik, serta terhindar dari berbagai penyakit.
Secara lebih spesifik, berikut adalah dampak positif dari kualitas air normal pada tambak mujair:
    
1.Pertumbuhan ikan yang optimal
  Kualitas air yang normal akan mendukung pertumbuhan ikan mujair secara optimal. Ikan mujair akan dapat tumbuh dengan ukuran yang lebih besar dan bobot yang lebih berat.
        
2.Reproduksi ikan yang lancar
  Kualitas air yang normal juga akan mendukung reproduksi ikan mujair secara lancar. Ikan mujair akan dapat berkembang biak dengan baik dan menghasilkan keturunan yang berkualitas.
        
3.Ketahanan ikan terhadap penyakit
  Kualitas air yang normal akan meningkatkan ketahanan ikan mujair terhadap penyakit. Ikan mujair akan terhindar dari berbagai penyakit yang dapat menyebabkan kematian atau penurunan pertumbuhan.
        
Oleh karena itu, penting untuk menjaga kualitas air tambak mujair agar tetap normal. Hal ini dapat dilakukan dengan menerapkan pengelolaan tambak yang baik, seperti:
        
1.Menjaga kebersihan tambak
  Tambak harus dibersihkan secara rutin dari sedimen dan bahan organik. Hal ini dapat dilakukan dengan menggunakan mesin pengeruk atau secara manual.
        
2.Mengelola pakan ikan
  Pakan ikan harus diberikan dalam jumlah yang sesuai dan tidak berlebihan. Pakan ikan yang berlebihan dapat menyebabkan pencemaran air dan menurunkan kualitas air.
        
3.Melakukan pergantian air
 Air tambak harus diganti secara berkala untuk menjaga kualitas air. Frekuensi penggantian air tergantung pada kondisi tambak.
        ";
    } elseif ($fuzzyValue == 2) {
        $kondisi = "tidak normal";
        $keterangan = "Jika kualitas air tambak mujair tidak normal,maka akan berdampak negatif pada pertumbuhan dan perkembangan ikan mujair,berikut adalah dampak negatif dari kualitas air tidak normal pada tambak mujair : 

1.Penurunan pertumbuhan
  Ikan mujair membutuhkan air yang bersih dan jernih untuk fotosintesis. Fotosintesis menghasilkan oksigen yang dibutuhkan ikan untuk bernapas. Kekeruhan air dapat menghalangi penetrasi cahaya matahari ke dalam air, sehingga dapat menyebabkan penurunan kadar oksigen terlarut di dalam air. Hal ini dapat menyebabkan ikan mengalami stres dan dapat menurunkan pertumbuhan ikan.
        
2.Peningkatan risiko penyakit
  Air yang tidak bersih dan mengandung patogen, seperti bakteri dan virus dapat menyebabkan berbagai penyakit pada ikan. Ikan mujair yang terinfeksi penyakit dapat mengalami penurunan pertumbuhan, bahkan kematian.
        
3.Kematian
  Air yang tidak normal, seperti air yang terlalu asam atau basa, dapat menyebabkan kematian ikan.
        
Berikut adalah beberapa cara untuk mencegah dampak air tidak normal pada ikan mujair:
        
1.Menjaga kebersihan tambak
  Tambak harus dibersihkan secara rutin dari sedimen dan bahan organik. Hal ini dapat dilakukan dengan menggunakan mesin pengeruk atau secara manual.
        
2.Mengelola pakan ikan
  Pakan ikan harus diberikan dalam jumlah yang sesuai dan tidak berlebihan. Pakan ikan yang berlebihan dapat menyebabkan pencemaran air dan menurunkan kualitas air.
         
3.Melakukan pergantian air
  Air tambak harus diganti secara berkala untuk menjaga kualitas air. Frekuensi penggantian air tergantung pada kondisi tambak.
         
4.Menerapkan bioflok
  Bioflok adalah sistem budidaya ikan yang memanfaatkan bakteri untuk mengurai limbah organik. Sistem ini dapat membantu menjaga kualitas air dan meningkatkan ketahanan ikan terhadap penyakit.,";
    } else {
        $kondisi = "Terdapat Error";
        $keterangan = "Terdapat Error pada nilai fuzzy";
    }

    $data['id'] = $row['ID'];
    $data['TDS_Value'] = $row['TDS_Value'];
    $data['Turbidity_Value'] = $row['Turbidity_Value'];
    $data['pH_Value'] = $row['pH_Value'];
    $data['fuzzy'] = $fuzzyValue;
    $data['kondisi'] = $kondisi;
    $data['keterangan'] = $keterangan;

    $response['error'] = false;
    $response['message'] = "success";
    $response['sensorData'] = $data;
} else {
    $response['error'] = true;
    $response['message'] = "failed";
    $response['sensorData'] = null;
}
header('Content-Type: application/json');
echo json_encode($response);

$conn->close();
?>
