-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: shopping
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bosuutap`
--

DROP TABLE IF EXISTS `bosuutap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bosuutap` (
  `MaSuuTap` bigint NOT NULL AUTO_INCREMENT,
  `MoTa` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `TenSuuTap` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`MaSuuTap`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bosuutap`
--

LOCK TABLES `bosuutap` WRITE;
/*!40000 ALTER TABLE `bosuutap` DISABLE KEYS */;
INSERT INTO `bosuutap` VALUES (1,'Bộ sưu tập mùa xuân hè',NULL,'2026-03-06 09:59:24.000000','Xuân Hè 2026'),(2,'Bộ sưu tập mùa thu đông',NULL,'2026-03-06 09:59:24.000000','Thu Đông 2026'),(3,'Dòng sản phẩm cơ bản',NULL,'2026-03-06 09:59:24.000000','Basic');
/*!40000 ALTER TABLE `bosuutap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chitietdonhang`
--

DROP TABLE IF EXISTS `chitietdonhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chitietdonhang` (
  `MaChiTietDonHang` bigint NOT NULL AUTO_INCREMENT,
  `GiaGiam` double DEFAULT NULL,
  `GiaSanPham` double DEFAULT NULL,
  `GiamGia` double DEFAULT NULL,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `SoLuong` int DEFAULT NULL,
  `ThanhTien` double DEFAULT NULL,
  `MaChiTietSanPham` bigint DEFAULT NULL,
  `MaDon` bigint DEFAULT NULL,
  PRIMARY KEY (`MaChiTietDonHang`),
  KEY `FKfs1efrfpiu7hvhu3uqdjnxqe5` (`MaChiTietSanPham`),
  KEY `FK5q9r33idppdxuth9h84qtwxiq` (`MaDon`),
  CONSTRAINT `FK5q9r33idppdxuth9h84qtwxiq` FOREIGN KEY (`MaDon`) REFERENCES `donhang` (`MaDon`),
  CONSTRAINT `FKfs1efrfpiu7hvhu3uqdjnxqe5` FOREIGN KEY (`MaChiTietSanPham`) REFERENCES `chitietsanpham` (`MaChiTietSanPham`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chitietdonhang`
--

LOCK TABLES `chitietdonhang` WRITE;
/*!40000 ALTER TABLE `chitietdonhang` DISABLE KEYS */;
INSERT INTO `chitietdonhang` VALUES (1,0,200,0,NULL,'2026-03-06 09:59:24.000000',2,400,1,1),(2,40,400,10,NULL,'2026-03-06 09:59:24.000000',1,360,3,1),(3,0,200,0,NULL,'2026-03-06 09:59:24.000000',2,400,2,2),(4,15,300,5,NULL,'2026-03-06 09:59:24.000000',1,285,4,3),(5,0,100,0,NULL,'2026-03-06 09:59:24.000000',2,200,5,4),(6,0,200,0,NULL,'2026-03-06 09:59:24.000000',3,600,1,5),(7,0,400,0,NULL,'2026-03-06 09:59:24.000000',1,400,3,5),(8,0,200,0,NULL,'2026-03-06 03:38:12.223814',2,400,1,6),(9,0,200,0,NULL,'2026-03-06 03:38:12.229173',2,400,2,6);
/*!40000 ALTER TABLE `chitietdonhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chitietgiohang`
--

DROP TABLE IF EXISTS `chitietgiohang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chitietgiohang` (
  `MaChiTietGioHang` bigint NOT NULL AUTO_INCREMENT,
  `SoLuong` int DEFAULT NULL,
  `MaChiTietSanPham` bigint DEFAULT NULL,
  `MaGioHang` bigint DEFAULT NULL,
  PRIMARY KEY (`MaChiTietGioHang`),
  KEY `FKlb78dfljs4a2n912d1te1q12o` (`MaChiTietSanPham`),
  KEY `FK1n1mhll9w2974r92bvsm7o7lf` (`MaGioHang`),
  CONSTRAINT `FK1n1mhll9w2974r92bvsm7o7lf` FOREIGN KEY (`MaGioHang`) REFERENCES `giohang` (`MaGioHang`),
  CONSTRAINT `FKlb78dfljs4a2n912d1te1q12o` FOREIGN KEY (`MaChiTietSanPham`) REFERENCES `chitietsanpham` (`MaChiTietSanPham`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chitietgiohang`
--

LOCK TABLES `chitietgiohang` WRITE;
/*!40000 ALTER TABLE `chitietgiohang` DISABLE KEYS */;
INSERT INTO `chitietgiohang` VALUES (1,2,3,1),(2,1,4,1),(3,1,1,2),(4,3,5,4),(5,2,2,5);
/*!40000 ALTER TABLE `chitietgiohang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chitietphieunhap`
--

DROP TABLE IF EXISTS `chitietphieunhap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chitietphieunhap` (
  `MaChiTietPhieuNhap` bigint NOT NULL AUTO_INCREMENT,
  `GhiTru` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GhiTruKiemHang` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `SoLuong` int DEFAULT NULL,
  `SoLuongThieu` int DEFAULT NULL,
  `TrangThai` int DEFAULT NULL,
  `MaChiTietSanPham` bigint DEFAULT NULL,
  `MaPhieuNhap` bigint DEFAULT NULL,
  PRIMARY KEY (`MaChiTietPhieuNhap`),
  KEY `FK8bedkdsavsca7qfqepatat0tq` (`MaChiTietSanPham`),
  KEY `FKjppln4irqnyngb4k95alvk1uu` (`MaPhieuNhap`),
  CONSTRAINT `FK8bedkdsavsca7qfqepatat0tq` FOREIGN KEY (`MaChiTietSanPham`) REFERENCES `chitietsanpham` (`MaChiTietSanPham`),
  CONSTRAINT `FKjppln4irqnyngb4k95alvk1uu` FOREIGN KEY (`MaPhieuNhap`) REFERENCES `phieunhap` (`MaPhieuNhap`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chitietphieunhap`
--

LOCK TABLES `chitietphieunhap` WRITE;
/*!40000 ALTER TABLE `chitietphieunhap` DISABLE KEYS */;
INSERT INTO `chitietphieunhap` VALUES (1,'thiếu hàng','','2026-03-06 03:10:51.743224','2026-03-06 09:59:24.000000',60,40,0,1,4),(2,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',30,NULL,1,2,1),(3,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',40,NULL,1,3,1),(4,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',25,NULL,1,4,2),(5,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',20,NULL,1,5,2),(6,'','',NULL,'2026-03-06 03:07:15.820308',100,NULL,1,1,4);
/*!40000 ALTER TABLE `chitietphieunhap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chitietsanpham`
--

DROP TABLE IF EXISTS `chitietsanpham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chitietsanpham` (
  `MaChiTietSanPham` bigint NOT NULL AUTO_INCREMENT,
  `GhiTru` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MaCuaHang` bigint DEFAULT NULL,
  `MaPhieuNhap` bigint DEFAULT NULL,
  `MoTa` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `SoLuong` int DEFAULT NULL,
  `TrangThai` int DEFAULT NULL,
  `MaKichThuoc` bigint DEFAULT NULL,
  `MaMauSac` bigint DEFAULT NULL,
  `MaSanPham` bigint DEFAULT NULL,
  PRIMARY KEY (`MaChiTietSanPham`),
  KEY `FKtfic1bb8erbhi1sicf95fsl5h` (`MaKichThuoc`),
  KEY `FK2noua7py987pah4eqo6qqdl5w` (`MaMauSac`),
  KEY `FK614uy6deyhtvthwkkb5nqx6i4` (`MaSanPham`),
  CONSTRAINT `FK2noua7py987pah4eqo6qqdl5w` FOREIGN KEY (`MaMauSac`) REFERENCES `mausac` (`MaMauSac`),
  CONSTRAINT `FK614uy6deyhtvthwkkb5nqx6i4` FOREIGN KEY (`MaSanPham`) REFERENCES `sanpham` (`MaSanPham`),
  CONSTRAINT `FKtfic1bb8erbhi1sicf95fsl5h` FOREIGN KEY (`MaKichThuoc`) REFERENCES `kichthuoc` (`MaKichThuoc`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chitietsanpham`
--

LOCK TABLES `chitietsanpham` WRITE;
/*!40000 ALTER TABLE `chitietsanpham` DISABLE KEYS */;
INSERT INTO `chitietsanpham` VALUES (1,NULL,1,4,NULL,'2026-03-06 03:38:12.243310','2026-03-06 09:59:24.000000',158,1,2,1,1),(2,NULL,1,NULL,NULL,'2026-03-06 03:38:12.257329','2026-03-06 09:59:24.000000',0,1,3,1,1),(3,NULL,1,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',NULL,1,2,2,2),(4,NULL,2,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',NULL,1,1,3,3),(5,NULL,2,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',NULL,1,3,2,4),(7,NULL,NULL,NULL,'Màu xanh size XL',NULL,'2026-03-07 14:18:40.798212',NULL,1,4,4,1);
/*!40000 ALTER TABLE `chitietsanpham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuahang`
--

DROP TABLE IF EXISTS `cuahang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuahang` (
  `MaCuaHang` bigint NOT NULL AUTO_INCREMENT,
  `DiaChi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SoDienThoai` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TenCuaHang` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` int DEFAULT NULL,
  `ViTri` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`MaCuaHang`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuahang`
--

LOCK TABLES `cuahang` WRITE;
/*!40000 ALTER TABLE `cuahang` DISABLE KEYS */;
INSERT INTO `cuahang` VALUES (1,'123 Nguyễn Huệ, Q.1, TP.HCM','q1@shop.com','02812345678','Chi nhánh Quận 1',1,'10.7769,106.7009'),(2,'456 Võ Văn Tần, Q.3, TP.HCM','q3@shop.com','02812345679','Chi nhánh Quận 3',1,'10.7756,106.6873'),(3,'789 Phan Văn Trị, Gò Vấp, HCM','gv@shop.com','02812345680','Chi nhánh Gò Vấp',1,'10.8384,106.6498');
/*!40000 ALTER TABLE `cuahang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `danhgiasanpham`
--

DROP TABLE IF EXISTS `danhgiasanpham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `danhgiasanpham` (
  `MaDanhGia` bigint NOT NULL,
  `GhiTru` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `HinhAnh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `SoSao` int DEFAULT NULL,
  `MaDon` bigint DEFAULT NULL,
  `MaKhachHang` bigint DEFAULT NULL,
  `MaSanPham` bigint DEFAULT NULL,
  `Json` text COLLATE utf8mb4_unicode_ci,
  `MaChiTietDonHang` bigint DEFAULT NULL,
  PRIMARY KEY (`MaDanhGia`),
  KEY `FKhauap4sfela5ksyal89394970` (`MaKhachHang`),
  KEY `FK6xl1xj6nsae1mvsxn5rujcu9b` (`MaSanPham`),
  KEY `FK34byr1uh0sbv7fgcgt80oafbx` (`MaDon`),
  KEY `FKjfpincn76n5ku9k2tnkxno6na` (`MaChiTietDonHang`),
  CONSTRAINT `FK34byr1uh0sbv7fgcgt80oafbx` FOREIGN KEY (`MaDon`) REFERENCES `chitietdonhang` (`MaChiTietDonHang`),
  CONSTRAINT `FK6xl1xj6nsae1mvsxn5rujcu9b` FOREIGN KEY (`MaSanPham`) REFERENCES `sanpham` (`MaSanPham`),
  CONSTRAINT `FKggtmxbykadk1luip3honf4ecm` FOREIGN KEY (`MaDon`) REFERENCES `donhang` (`MaDon`),
  CONSTRAINT `FKhauap4sfela5ksyal89394970` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`),
  CONSTRAINT `FKjfpincn76n5ku9k2tnkxno6na` FOREIGN KEY (`MaChiTietDonHang`) REFERENCES `chitietdonhang` (`MaChiTietDonHang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `danhgiasanpham`
--

LOCK TABLES `danhgiasanpham` WRITE;
/*!40000 ALTER TABLE `danhgiasanpham` DISABLE KEYS */;
INSERT INTO `danhgiasanpham` VALUES (1,'Váy rất đẹp, đúng size, vải mát',NULL,NULL,'2026-03-06 09:59:24.000000',5,3,3,3,NULL,NULL),(2,'Áo Oxford chất lượng tốt',NULL,NULL,'2026-03-06 09:59:24.000000',4,5,5,1,NULL,NULL),(3,'Quần Jean hơi chật một chút',NULL,NULL,'2026-03-06 09:59:24.000000',3,5,5,2,NULL,NULL),(4,'Sản phẩm rất đẹp, chất lượng tốt!',NULL,NULL,'2026-03-06 03:41:36.733772',5,6,6,1,NULL,NULL);
/*!40000 ALTER TABLE `danhgiasanpham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donhang`
--

DROP TABLE IF EXISTS `donhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donhang` (
  `MaDon` bigint NOT NULL AUTO_INCREMENT,
  `DiaChi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `HinhThucDonHang` int DEFAULT NULL,
  `MaKhuyenMaiDiem` bigint DEFAULT NULL,
  `MaKhuyenMaiHoaDon` bigint DEFAULT NULL,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `Sdt` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TienGiam` int DEFAULT NULL,
  `TongTien` int DEFAULT NULL,
  `TongTienGiam` int DEFAULT NULL,
  `TongTienTra` int DEFAULT NULL,
  `TrangThai` int DEFAULT NULL,
  `TrangThaiThanhToan` int DEFAULT NULL,
  `MaCuaHang` bigint DEFAULT NULL,
  `MaKhachHang` bigint DEFAULT NULL,
  `MaNhanVien` bigint DEFAULT NULL,
  PRIMARY KEY (`MaDon`),
  KEY `FKp4oj2r4akc9whfnkauidjnh9h` (`MaCuaHang`),
  KEY `FK6ahk88935wl9aafbwmewvc3j6` (`MaKhachHang`),
  KEY `FKl0gtb3r68pr51q4pclsoh50kh` (`MaNhanVien`),
  CONSTRAINT `FK6ahk88935wl9aafbwmewvc3j6` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`),
  CONSTRAINT `FKl0gtb3r68pr51q4pclsoh50kh` FOREIGN KEY (`MaNhanVien`) REFERENCES `nhanvien` (`MaNhanVien`),
  CONSTRAINT `FKp4oj2r4akc9whfnkauidjnh9h` FOREIGN KEY (`MaCuaHang`) REFERENCES `cuahang` (`MaCuaHang`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donhang`
--

LOCK TABLES `donhang` WRITE;
/*!40000 ALTER TABLE `donhang` DISABLE KEYS */;
INSERT INTO `donhang` VALUES (1,'123 Nguyễn Trãi, Q.1, TP.HCM',1,NULL,NULL,NULL,'2026-03-06 09:59:24.000000','0911000001',0,600,0,600,1,1,1,1,5),(2,'456 Lê Lợi, Q.3, TP.HCM',1,NULL,NULL,NULL,'2026-03-06 09:59:24.000000','0922000002',40,400,40,360,2,1,1,2,2),(3,'Mua tại cửa hàng',0,NULL,NULL,NULL,'2026-03-06 09:59:24.000000','0933000003',15,300,15,285,3,1,2,3,3),(4,'789 Trần Hưng Đạo, Q.5, TP.HCM',1,NULL,NULL,NULL,'2026-03-06 09:59:24.000000','0944000004',0,200,0,200,0,0,1,4,5),(5,'Mua tại cửa hàng',0,NULL,NULL,NULL,'2026-03-06 09:59:24.000000','0955000005',100,1000,100,900,3,1,2,5,4),(6,'123 Nguyễn Trãi, Q.1, TP.HCM',1,NULL,NULL,'2026-03-06 03:39:44.478691','2026-03-06 03:38:12.215602','0901234567',0,800,0,800,5,0,2,6,5);
/*!40000 ALTER TABLE `donhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `giohang`
--

DROP TABLE IF EXISTS `giohang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `giohang` (
  `MaGioHang` bigint NOT NULL AUTO_INCREMENT,
  `NgayTao` datetime(6) DEFAULT NULL,
  `MaKhachHang` bigint NOT NULL,
  PRIMARY KEY (`MaGioHang`),
  UNIQUE KEY `UK2w7piknpgm5h0eqqf02w6s3pl` (`MaKhachHang`),
  CONSTRAINT `FK3ub9bs34oaea1agxqd7t7x69l` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `giohang`
--

LOCK TABLES `giohang` WRITE;
/*!40000 ALTER TABLE `giohang` DISABLE KEYS */;
INSERT INTO `giohang` VALUES (1,'2026-03-06 09:59:24.000000',1),(2,'2026-03-06 09:59:24.000000',2),(3,'2026-03-06 09:59:24.000000',3),(4,'2026-03-06 09:59:24.000000',4),(5,'2026-03-06 09:59:24.000000',5),(6,'2026-03-06 03:36:45.195830',6);
/*!40000 ALTER TABLE `giohang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hinhanh`
--

DROP TABLE IF EXISTS `hinhanh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hinhanh` (
  `MaHinhAnh` bigint NOT NULL AUTO_INCREMENT,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `TenHinhAnh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MaChiTietSanPham` bigint DEFAULT NULL,
  PRIMARY KEY (`MaHinhAnh`),
  KEY `FKiyfhv8loas9ue9g5mei6gmpoa` (`MaChiTietSanPham`),
  CONSTRAINT `FKiyfhv8loas9ue9g5mei6gmpoa` FOREIGN KEY (`MaChiTietSanPham`) REFERENCES `chitietsanpham` (`MaChiTietSanPham`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hinhanh`
--

LOCK TABLES `hinhanh` WRITE;
/*!40000 ALTER TABLE `hinhanh` DISABLE KEYS */;
INSERT INTO `hinhanh` VALUES (1,NULL,'2026-03-06 09:59:24.000000','ao-oxford-trang-m-1.jpg',1),(2,NULL,'2026-03-06 09:59:24.000000','ao-oxford-trang-m-2.jpg',1),(3,NULL,'2026-03-06 09:59:24.000000','ao-oxford-trang-l-1.jpg',2),(4,NULL,'2026-03-06 09:59:24.000000','quan-jean-den-m-1.jpg',3),(5,NULL,'2026-03-06 09:59:24.000000','vay-hoa-do-s-1.jpg',4),(6,NULL,'2026-03-07 14:18:41.052266','/storage/71e87c77-da1f-48e6-a309-05617dd77546.jpg',7),(7,NULL,'2026-03-07 14:18:41.057998','/storage/6915f5fd-2378-462d-8640-9a9474f585d8.jpg',7);
/*!40000 ALTER TABLE `hinhanh` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khachhang`
--

DROP TABLE IF EXISTS `khachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khachhang` (
  `MaKhachHang` bigint NOT NULL AUTO_INCREMENT,
  `DiemTichLuy` int DEFAULT NULL,
  `Email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `RefreshToken` mediumtext COLLATE utf8mb4_unicode_ci,
  `Sdt` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TenKhachHang` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`MaKhachHang`),
  UNIQUE KEY `UKj4nrs6rblh0e0wuucscxiwwnf` (`Sdt`),
  KEY `FK8jxmnaysuax2nw0mjismbp70v` (`role_id`),
  CONSTRAINT `FK8jxmnaysuax2nw0mjismbp70v` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khachhang`
--

LOCK TABLES `khachhang` WRITE;
/*!40000 ALTER TABLE `khachhang` DISABLE KEYS */;
INSERT INTO `khachhang` VALUES (1,10,'lan@g.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsYW5AZy5jb20iLCJleHAiOjE3ODE1MzI4NDcsImlhdCI6MTc3Mjg5Mjg0NywidXNlciI6eyJpZCI6MSwiZW1haWwiOiJsYW5AZy5jb20iLCJuYW1lIjoiTGFuIn19.urbIT9-FRmZQiboBFZXugNg4tzFdCtgwmScOVb04FhCCpTt2lImLNmGbP2782wOPBkfLkrrkRNhczEajalOBKw','0911000001','Lan',4),(2,0,'m@g.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW',NULL,'0922000002','Minh',4),(3,100,'h@g.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW',NULL,'0933000003','Hoa',4),(4,5,'t@g.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW',NULL,'0944000004','Tuấn',4),(5,50,'y@g.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW',NULL,'0955000005','Yến',4),(6,NULL,'test@gmail.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTc4MTQwNzkxNSwiaWF0IjoxNzcyNzY3OTE1LCJ1c2VyIjp7ImlkIjo2LCJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwibmFtZSI6Ik5ndXnhu4VuIFRlc3QifX0.i6EYvqCOBa6W9vq2IXJkjtbcjmJhLx9-o0XJs614CS7gDAdTLLHaUOxH2kI33qpshmCR1wNSXy8W_p68wuTqpg','0999999999','Nguyễn Test',4);
/*!40000 ALTER TABLE `khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khuyenmaitheodiem`
--

DROP TABLE IF EXISTS `khuyenmaitheodiem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khuyenmaitheodiem` (
  `MaKhuyenMaiDiem` bigint NOT NULL AUTO_INCREMENT,
  `DiemToiThieu` int DEFAULT NULL,
  `GiamToiDa` int DEFAULT NULL,
  `HinhThuc` int DEFAULT NULL,
  `HoaDonToiDa` int DEFAULT NULL,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `PhanTramGiam` double DEFAULT NULL,
  `SoLuong` int DEFAULT NULL,
  `TenKhuyenMai` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ThoiGianBatDau` datetime(6) DEFAULT NULL,
  `ThoiGianKetThuc` datetime(6) DEFAULT NULL,
  `TrangThai` int DEFAULT NULL,
  PRIMARY KEY (`MaKhuyenMaiDiem`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khuyenmaitheodiem`
--

LOCK TABLES `khuyenmaitheodiem` WRITE;
/*!40000 ALTER TABLE `khuyenmaitheodiem` DISABLE KEYS */;
INSERT INTO `khuyenmaitheodiem` VALUES (1,50,200000,1,800000,NULL,'2026-03-06 09:59:24.000000',15,100,'Đổi 50 điểm giảm 15%','2026-01-01 00:00:00.000000','2026-12-31 23:59:59.000000',1),(2,100,500000,1,1500000,NULL,'2026-03-06 09:59:24.000000',25,50,'Đổi 100 điểm giảm 25%','2026-01-01 00:00:00.000000','2026-12-31 23:59:59.000000',1),(3,20,80000,0,300000,NULL,'2026-03-06 09:59:24.000000',5,200,'Đổi 20 điểm giảm 5%','2026-03-01 00:00:00.000000','2026-06-30 23:59:59.000000',1);
/*!40000 ALTER TABLE `khuyenmaitheodiem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khuyenmaitheohoadon`
--

DROP TABLE IF EXISTS `khuyenmaitheohoadon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khuyenmaitheohoadon` (
  `MaKhuyenMaiHoaDon` bigint NOT NULL AUTO_INCREMENT,
  `GiamToiDa` int DEFAULT NULL,
  `HinhThuc` int DEFAULT NULL,
  `HoaDonToiDa` int DEFAULT NULL,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `PhanTramGiam` double DEFAULT NULL,
  `SoLuong` int DEFAULT NULL,
  `TenKhuyenMai` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ThoiGianBatDau` datetime(6) DEFAULT NULL,
  `ThoiGianKetThuc` datetime(6) DEFAULT NULL,
  `TrangThai` int DEFAULT NULL,
  PRIMARY KEY (`MaKhuyenMaiHoaDon`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khuyenmaitheohoadon`
--

LOCK TABLES `khuyenmaitheohoadon` WRITE;
/*!40000 ALTER TABLE `khuyenmaitheohoadon` DISABLE KEYS */;
INSERT INTO `khuyenmaitheohoadon` VALUES (1,100000,1,500000,NULL,'2026-03-06 09:59:24.000000',10,100,'Giảm 10% đơn từ 500K','2026-01-01 00:00:00.000000','2026-06-30 23:59:59.000000',1),(2,300000,1,1000000,NULL,'2026-03-06 09:59:24.000000',20,50,'Giảm 20% đơn từ 1 triệu','2026-02-01 00:00:00.000000','2026-04-30 23:59:59.000000',1),(3,50000,0,200000,NULL,'2026-03-06 09:59:24.000000',5,200,'Giảm 5% tất cả đơn','2026-03-01 00:00:00.000000','2026-12-31 23:59:59.000000',1);
/*!40000 ALTER TABLE `khuyenmaitheohoadon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kichthuoc`
--

DROP TABLE IF EXISTS `kichthuoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kichthuoc` (
  `MaKichThuoc` bigint NOT NULL AUTO_INCREMENT,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `TenKichThuoc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`MaKichThuoc`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kichthuoc`
--

LOCK TABLES `kichthuoc` WRITE;
/*!40000 ALTER TABLE `kichthuoc` DISABLE KEYS */;
INSERT INTO `kichthuoc` VALUES (1,NULL,'2026-03-06 09:59:24.000000','S'),(2,NULL,'2026-03-06 09:59:24.000000','M'),(3,NULL,'2026-03-06 09:59:24.000000','L'),(4,NULL,'2026-03-06 09:59:24.000000','XL'),(5,NULL,'2026-03-06 09:59:24.000000','XXL');
/*!40000 ALTER TABLE `kichthuoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kieusanpham`
--

DROP TABLE IF EXISTS `kieusanpham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kieusanpham` (
  `MaKieuSanPham` bigint NOT NULL AUTO_INCREMENT,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `TenKieuSanPham` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`MaKieuSanPham`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kieusanpham`
--

LOCK TABLES `kieusanpham` WRITE;
/*!40000 ALTER TABLE `kieusanpham` DISABLE KEYS */;
INSERT INTO `kieusanpham` VALUES (1,NULL,'2026-03-06 09:59:24.000000','Áo'),(2,NULL,'2026-03-06 09:59:24.000000','Quần'),(3,NULL,'2026-03-06 09:59:24.000000','Váy'),(4,NULL,'2026-03-06 09:59:24.000000','Phụ kiện');
/*!40000 ALTER TABLE `kieusanpham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mausac`
--

DROP TABLE IF EXISTS `mausac`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mausac` (
  `MaMauSac` bigint NOT NULL AUTO_INCREMENT,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `TenMauSac` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`MaMauSac`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mausac`
--

LOCK TABLES `mausac` WRITE;
/*!40000 ALTER TABLE `mausac` DISABLE KEYS */;
INSERT INTO `mausac` VALUES (1,NULL,'2026-03-06 09:59:24.000000','Trắng'),(2,NULL,'2026-03-06 09:59:24.000000','Đen'),(3,NULL,'2026-03-06 09:59:24.000000','Đỏ'),(4,NULL,'2026-03-06 09:59:24.000000','Xanh'),(5,NULL,'2026-03-06 09:59:24.000000','Vàng');
/*!40000 ALTER TABLE `mausac` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhacungcap`
--

DROP TABLE IF EXISTS `nhacungcap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhacungcap` (
  `MaNhaCungCap` bigint NOT NULL AUTO_INCREMENT,
  `DiaChi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GhiTru` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `SoDienThoai` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TenNhaCungCap` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` int DEFAULT NULL,
  PRIMARY KEY (`MaNhaCungCap`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhacungcap`
--

LOCK TABLES `nhacungcap` WRITE;
/*!40000 ALTER TABLE `nhacungcap` DISABLE KEYS */;
INSERT INTO `nhacungcap` VALUES (1,'100 Lý Thường Kiệt, Q.10, HCM','vaiviet@ncc.com',NULL,NULL,'2026-03-06 09:59:24.000000','02838001001','Công ty TNHH Vải Việt',1),(2,'200 Cách Mạng Tháng 8, Q.3, HCM','maysaigon@ncc.com',NULL,NULL,'2026-03-06 09:59:24.000000','02838002002','Công ty CP May Sài Gòn',1),(3,'50 Hàng Bông, Hoàn Kiếm, HN','mayhanoi@ncc.com',NULL,NULL,'2026-03-06 09:59:24.000000','02438003003','Xưởng may Hà Nội',1);
/*!40000 ALTER TABLE `nhacungcap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhanvien`
--

DROP TABLE IF EXISTS `nhanvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhanvien` (
  `MaNhanVien` bigint NOT NULL AUTO_INCREMENT,
  `Email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MatKhau` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayBatDauLam` datetime(6) DEFAULT NULL,
  `NgayKetThucLam` datetime(6) DEFAULT NULL,
  `RefreshToken` mediumtext COLLATE utf8mb4_unicode_ci,
  `SoDienThoai` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TenNhanVien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` int DEFAULT NULL,
  `MaCuaHang` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`MaNhanVien`),
  KEY `FKf6wc74119h7r83atdp4ra1qmv` (`MaCuaHang`),
  KEY `FKnvl7582n1jdme7q4dk66bbs3c` (`role_id`),
  CONSTRAINT `FKf6wc74119h7r83atdp4ra1qmv` FOREIGN KEY (`MaCuaHang`) REFERENCES `cuahang` (`MaCuaHang`),
  CONSTRAINT `FKnvl7582n1jdme7q4dk66bbs3c` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhanvien`
--

LOCK TABLES `nhanvien` WRITE;
/*!40000 ALTER TABLE `nhanvien` DISABLE KEYS */;
INSERT INTO `nhanvien` VALUES (1,'an@s.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW',NULL,NULL,NULL,'0901000001','An',1,1,2),(2,'b@s.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW',NULL,NULL,NULL,'0901000002','Bình',1,1,3),(3,'c@s.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW',NULL,NULL,NULL,'0901000003','Chi',1,2,3),(4,'d@s.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW',NULL,NULL,NULL,'0901000004','Danh',1,2,2),(5,'h@s.com','$2a$10$5NZkLGXCo9Uq5f6jz1ZPiecRqL92Pv8ve9z5lZ4aHjUAWKJDyQ/hW',NULL,NULL,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoQHMuY29tIiwiZXhwIjoxNzgxNTMzMDAyLCJpYXQiOjE3NzI4OTMwMDIsInVzZXIiOnsiaWQiOjUsImVtYWlsIjoiaEBzLmNvbSIsIm5hbWUiOiJIw7luZyJ9fQ.JJn5YFyGgMkmdXJ4RV5ozZTJStSTK24JqONbpiGHVTfo2OOaw4S96EYFwG3dery-ox71xu6knjJhpwII2L7izQ','0901000005','Hùng',1,1,1);
/*!40000 ALTER TABLE `nhanvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission_role`
--

DROP TABLE IF EXISTS `permission_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission_role` (
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  KEY `FK6mg4g9rc8u87l0yavf8kjut05` (`permission_id`),
  KEY `FK3vhflqw0lwbwn49xqoivrtugt` (`role_id`),
  CONSTRAINT `FK3vhflqw0lwbwn49xqoivrtugt` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FK6mg4g9rc8u87l0yavf8kjut05` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission_role`
--

LOCK TABLES `permission_role` WRITE;
/*!40000 ALTER TABLE `permission_role` DISABLE KEYS */;
INSERT INTO `permission_role` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34),(1,35),(1,36),(1,37),(1,38),(1,39),(1,40),(1,41),(1,42),(1,43),(1,44),(1,45),(1,46),(1,47),(1,48),(1,49),(1,50),(1,51),(1,52),(1,53),(1,54),(1,55),(1,56),(1,57),(1,58),(1,59),(1,60),(1,61),(1,62),(1,63),(1,64),(1,65),(1,66),(1,67),(1,68),(1,69),(1,70),(1,71),(1,72),(1,73),(1,74),(1,75),(1,76),(1,77),(1,78),(1,79),(1,80),(1,81),(1,82),(1,83),(1,84),(1,85),(1,86),(1,87),(1,88),(1,89),(1,90),(1,91),(1,92),(1,93),(1,94),(1,95),(1,96),(1,97),(1,98),(1,99),(1,100),(1,101),(1,102),(1,103),(1,104),(1,105),(1,106),(3,1),(3,2),(3,6),(3,7),(3,11),(3,12),(3,16),(3,17),(3,18),(3,25),(3,26),(3,30),(3,31),(3,35),(3,36),(3,40),(3,41),(3,42),(3,46),(3,47),(3,66),(3,67),(3,68),(3,69),(3,105),(3,71),(3,72),(3,73),(3,74),(3,75),(3,77),(3,78),(3,80),(3,81),(3,83),(3,84),(3,85),(3,89),(3,90),(3,94),(3,95),(3,99),(3,100),(3,101),(4,1),(4,2),(4,6),(4,7),(4,11),(4,12),(4,16),(4,17),(4,18),(4,22),(4,23),(4,24),(4,25),(4,26),(4,30),(4,31),(4,35),(4,36),(4,40),(4,41),(4,42),(4,46),(4,47),(4,77),(4,78),(4,79),(4,81),(4,83),(4,84),(4,85),(4,89),(4,90),(4,94),(4,95),(4,99),(4,100),(4,101),(4,102),(4,103),(4,104),(4,106);
/*!40000 ALTER TABLE `permission_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apiPath` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdAt` datetime(6) DEFAULT NULL,
  `createdBy` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `method` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `module` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updatedAt` datetime(6) DEFAULT NULL,
  `updatedBy` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES (1,'/api/v1/san-pham','2026-03-06 09:59:24.000000',NULL,'GET','SAN_PHAM','Xem tất cả sản phẩm',NULL,NULL),(2,'/api/v1/san-pham/{id}','2026-03-06 09:59:24.000000',NULL,'GET','SAN_PHAM','Xem chi tiết sản phẩm',NULL,NULL),(3,'/api/v1/san-pham','2026-03-06 09:59:24.000000',NULL,'POST','SAN_PHAM','Tạo sản phẩm',NULL,NULL),(4,'/api/v1/san-pham','2026-03-06 09:59:24.000000',NULL,'PUT','SAN_PHAM','Cập nhật sản phẩm',NULL,NULL),(5,'/api/v1/san-pham/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','SAN_PHAM','Xóa sản phẩm',NULL,NULL),(6,'/api/v1/mau-sac','2026-03-06 09:59:24.000000',NULL,'GET','MAU_SAC','Xem tất cả màu sắc',NULL,NULL),(7,'/api/v1/mau-sac/{id}','2026-03-06 09:59:24.000000',NULL,'GET','MAU_SAC','Xem chi tiết màu sắc',NULL,NULL),(8,'/api/v1/mau-sac','2026-03-06 09:59:24.000000',NULL,'POST','MAU_SAC','Tạo màu sắc',NULL,NULL),(9,'/api/v1/mau-sac','2026-03-06 09:59:24.000000',NULL,'PUT','MAU_SAC','Cập nhật màu sắc',NULL,NULL),(10,'/api/v1/mau-sac/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','MAU_SAC','Xóa màu sắc',NULL,NULL),(11,'/api/v1/kich-thuoc','2026-03-06 09:59:24.000000',NULL,'GET','KICH_THUOC','Xem tất cả kích thước',NULL,NULL),(12,'/api/v1/kich-thuoc/{id}','2026-03-06 09:59:24.000000',NULL,'GET','KICH_THUOC','Xem chi tiết kích thước',NULL,NULL),(13,'/api/v1/kich-thuoc','2026-03-06 09:59:24.000000',NULL,'POST','KICH_THUOC','Tạo kích thước',NULL,NULL),(14,'/api/v1/kich-thuoc','2026-03-06 09:59:24.000000',NULL,'PUT','KICH_THUOC','Cập nhật kích thước',NULL,NULL),(15,'/api/v1/kich-thuoc/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','KICH_THUOC','Xóa kích thước',NULL,NULL),(16,'/api/v1/chi-tiet-san-pham','2026-03-06 09:59:24.000000',NULL,'GET','CHI_TIET_SP','Xem tất cả CTSP',NULL,NULL),(17,'/api/v1/chi-tiet-san-pham/{id}','2026-03-06 09:59:24.000000',NULL,'GET','CHI_TIET_SP','Xem CTSP theo id',NULL,NULL),(18,'/api/v1/chi-tiet-san-pham/san-pham/{sanPhamId}','2026-03-06 09:59:24.000000',NULL,'GET','CHI_TIET_SP','Xem CTSP theo sản phẩm',NULL,NULL),(19,'/api/v1/chi-tiet-san-pham','2026-03-06 09:59:24.000000',NULL,'POST','CHI_TIET_SP','Tạo CTSP',NULL,NULL),(20,'/api/v1/chi-tiet-san-pham','2026-03-06 09:59:24.000000',NULL,'PUT','CHI_TIET_SP','Cập nhật CTSP',NULL,NULL),(21,'/api/v1/chi-tiet-san-pham/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','CHI_TIET_SP','Xóa CTSP',NULL,NULL),(22,'/api/v1/gio-hang/them-san-pham','2026-03-06 09:59:24.000000',NULL,'POST','GIO_HANG','Thêm SP vào giỏ hàng',NULL,NULL),(23,'/api/v1/gio-hang/cua-toi','2026-03-06 09:59:24.000000',NULL,'GET','GIO_HANG','Xem giỏ hàng của tôi',NULL,NULL),(24,'/api/v1/gio-hang/chi-tiet/{maChiTietGioHang}','2026-03-06 09:59:24.000000',NULL,'DELETE','GIO_HANG','Xóa SP khỏi giỏ hàng',NULL,NULL),(25,'/api/v1/kieu-san-pham','2026-03-06 09:59:24.000000',NULL,'GET','KIEU_SAN_PHAM','Xem tất cả kiểu sản phẩm',NULL,NULL),(26,'/api/v1/kieu-san-pham/{id}','2026-03-06 09:59:24.000000',NULL,'GET','KIEU_SAN_PHAM','Xem chi tiết kiểu sản phẩm',NULL,NULL),(27,'/api/v1/kieu-san-pham','2026-03-06 09:59:24.000000',NULL,'POST','KIEU_SAN_PHAM','Tạo kiểu sản phẩm',NULL,NULL),(28,'/api/v1/kieu-san-pham','2026-03-06 09:59:24.000000',NULL,'PUT','KIEU_SAN_PHAM','Cập nhật kiểu sản phẩm',NULL,NULL),(29,'/api/v1/kieu-san-pham/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','KIEU_SAN_PHAM','Xóa kiểu sản phẩm',NULL,NULL),(30,'/api/v1/bo-suu-tap','2026-03-06 09:59:24.000000',NULL,'GET','BO_SUU_TAP','Xem tất cả bộ sưu tập',NULL,NULL),(31,'/api/v1/bo-suu-tap/{id}','2026-03-06 09:59:24.000000',NULL,'GET','BO_SUU_TAP','Xem chi tiết bộ sưu tập',NULL,NULL),(32,'/api/v1/bo-suu-tap','2026-03-06 09:59:24.000000',NULL,'POST','BO_SUU_TAP','Tạo bộ sưu tập',NULL,NULL),(33,'/api/v1/bo-suu-tap','2026-03-06 09:59:24.000000',NULL,'PUT','BO_SUU_TAP','Cập nhật bộ sưu tập',NULL,NULL),(34,'/api/v1/bo-suu-tap/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','BO_SUU_TAP','Xóa bộ sưu tập',NULL,NULL),(35,'/api/v1/thuong-hieu','2026-03-06 09:59:24.000000',NULL,'GET','THUONG_HIEU','Xem tất cả thương hiệu',NULL,NULL),(36,'/api/v1/thuong-hieu/{id}','2026-03-06 09:59:24.000000',NULL,'GET','THUONG_HIEU','Xem chi tiết thương hiệu',NULL,NULL),(37,'/api/v1/thuong-hieu','2026-03-06 09:59:24.000000',NULL,'POST','THUONG_HIEU','Tạo thương hiệu',NULL,NULL),(38,'/api/v1/thuong-hieu','2026-03-06 09:59:24.000000',NULL,'PUT','THUONG_HIEU','Cập nhật thương hiệu',NULL,NULL),(39,'/api/v1/thuong-hieu/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','THUONG_HIEU','Xóa thương hiệu',NULL,NULL),(40,'/api/v1/hinh-anh','2026-03-06 09:59:24.000000',NULL,'GET','HINH_ANH','Xem tất cả hình ảnh',NULL,NULL),(41,'/api/v1/hinh-anh/{id}','2026-03-06 09:59:24.000000',NULL,'GET','HINH_ANH','Xem hình ảnh theo id',NULL,NULL),(42,'/api/v1/hinh-anh/chi-tiet-san-pham/{chiTietSanPhamId}','2026-03-06 09:59:24.000000',NULL,'GET','HINH_ANH','Xem hình ảnh theo CTSP',NULL,NULL),(43,'/api/v1/hinh-anh','2026-03-06 09:59:24.000000',NULL,'POST','HINH_ANH','Tạo hình ảnh',NULL,NULL),(44,'/api/v1/hinh-anh','2026-03-06 09:59:24.000000',NULL,'PUT','HINH_ANH','Cập nhật hình ảnh',NULL,NULL),(45,'/api/v1/hinh-anh/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','HINH_ANH','Xóa hình ảnh',NULL,NULL),(46,'/api/v1/cua-hang','2026-03-06 09:59:24.000000',NULL,'GET','CUA_HANG','Xem tất cả cửa hàng',NULL,NULL),(47,'/api/v1/cua-hang/{id}','2026-03-06 09:59:24.000000',NULL,'GET','CUA_HANG','Xem chi tiết cửa hàng',NULL,NULL),(48,'/api/v1/cua-hang','2026-03-06 09:59:24.000000',NULL,'POST','CUA_HANG','Tạo cửa hàng',NULL,NULL),(49,'/api/v1/cua-hang','2026-03-06 09:59:24.000000',NULL,'PUT','CUA_HANG','Cập nhật cửa hàng',NULL,NULL),(50,'/api/v1/cua-hang/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','CUA_HANG','Xóa cửa hàng',NULL,NULL),(51,'/api/v1/roles','2026-03-06 09:59:24.000000',NULL,'GET','ROLES','Xem tất cả vai trò',NULL,NULL),(52,'/api/v1/roles/{id}','2026-03-06 09:59:24.000000',NULL,'GET','ROLES','Xem chi tiết vai trò',NULL,NULL),(53,'/api/v1/roles','2026-03-06 09:59:24.000000',NULL,'POST','ROLES','Tạo vai trò',NULL,NULL),(54,'/api/v1/roles','2026-03-06 09:59:24.000000',NULL,'PUT','ROLES','Cập nhật vai trò',NULL,NULL),(55,'/api/v1/roles/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','ROLES','Xóa vai trò',NULL,NULL),(56,'/api/v1/permissions','2026-03-06 09:59:24.000000',NULL,'GET','PERMISSIONS','Xem tất cả quyền',NULL,NULL),(57,'/api/v1/permissions/{id}','2026-03-06 09:59:24.000000',NULL,'GET','PERMISSIONS','Xem chi tiết quyền',NULL,NULL),(58,'/api/v1/permissions','2026-03-06 09:59:24.000000',NULL,'POST','PERMISSIONS','Tạo quyền',NULL,NULL),(59,'/api/v1/permissions','2026-03-06 09:59:24.000000',NULL,'PUT','PERMISSIONS','Cập nhật quyền',NULL,NULL),(60,'/api/v1/permissions/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','PERMISSIONS','Xóa quyền',NULL,NULL),(61,'/api/v1/nha-cung-cap','2026-03-06 09:59:24.000000',NULL,'GET','NHA_CUNG_CAP','Xem tất cả nhà cung cấp',NULL,NULL),(62,'/api/v1/nha-cung-cap/{id}','2026-03-06 09:59:24.000000',NULL,'GET','NHA_CUNG_CAP','Xem chi tiết nhà cung cấp',NULL,NULL),(63,'/api/v1/nha-cung-cap','2026-03-06 09:59:24.000000',NULL,'POST','NHA_CUNG_CAP','Tạo nhà cung cấp',NULL,NULL),(64,'/api/v1/nha-cung-cap','2026-03-06 09:59:24.000000',NULL,'PUT','NHA_CUNG_CAP','Cập nhật nhà cung cấp',NULL,NULL),(65,'/api/v1/nha-cung-cap/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','NHA_CUNG_CAP','Xóa nhà cung cấp',NULL,NULL),(66,'/api/v1/phieu-nhap','2026-03-06 09:59:24.000000',NULL,'GET','PHIEU_NHAP','Xem tất cả phiếu nhập',NULL,NULL),(67,'/api/v1/phieu-nhap/{id}','2026-03-06 09:59:24.000000',NULL,'GET','PHIEU_NHAP','Xem chi tiết phiếu nhập',NULL,NULL),(68,'/api/v1/phieu-nhap','2026-03-06 09:59:24.000000',NULL,'POST','PHIEU_NHAP','Tạo phiếu nhập',NULL,NULL),(69,'/api/v1/phieu-nhap','2026-03-06 09:59:24.000000',NULL,'PUT','PHIEU_NHAP','Cập nhật phiếu nhập',NULL,NULL),(70,'/api/v1/phieu-nhap/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','PHIEU_NHAP','Xóa phiếu nhập',NULL,NULL),(71,'/api/v1/chi-tiet-phieu-nhap','2026-03-06 09:59:24.000000',NULL,'GET','CHI_TIET_PHIEU_NHAP','Xem tất cả CTPN',NULL,NULL),(72,'/api/v1/chi-tiet-phieu-nhap/{id}','2026-03-06 09:59:24.000000',NULL,'GET','CHI_TIET_PHIEU_NHAP','Xem CTPN theo id',NULL,NULL),(73,'/api/v1/chi-tiet-phieu-nhap/phieu-nhap/{phieuNhapId}','2026-03-06 09:59:24.000000',NULL,'GET','CHI_TIET_PHIEU_NHAP','Xem CTPN theo phiếu nhập',NULL,NULL),(74,'/api/v1/chi-tiet-phieu-nhap','2026-03-06 09:59:24.000000',NULL,'POST','CHI_TIET_PHIEU_NHAP','Tạo CTPN',NULL,NULL),(75,'/api/v1/chi-tiet-phieu-nhap','2026-03-06 09:59:24.000000',NULL,'PUT','CHI_TIET_PHIEU_NHAP','Cập nhật CTPN',NULL,NULL),(76,'/api/v1/chi-tiet-phieu-nhap/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','CHI_TIET_PHIEU_NHAP','Xóa CTPN',NULL,NULL),(77,'/api/v1/don-hang','2026-03-06 09:59:24.000000',NULL,'GET','DON_HANG','Xem tất cả đơn hàng',NULL,NULL),(78,'/api/v1/don-hang/{id}','2026-03-06 09:59:24.000000',NULL,'GET','DON_HANG','Xem chi tiết đơn hàng',NULL,NULL),(79,'/api/v1/don-hang/online','2026-03-06 09:59:24.000000',NULL,'POST','DON_HANG','Tạo đơn hàng online',NULL,NULL),(80,'/api/v1/don-hang/tai-quay','2026-03-06 09:59:24.000000',NULL,'POST','DON_HANG','Tạo đơn hàng tại quầy',NULL,NULL),(81,'/api/v1/don-hang','2026-03-06 09:59:24.000000',NULL,'PUT','DON_HANG','Cập nhật đơn hàng',NULL,NULL),(82,'/api/v1/don-hang/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','DON_HANG','Xóa đơn hàng',NULL,NULL),(83,'/api/v1/chi-tiet-don-hang','2026-03-06 09:59:24.000000',NULL,'GET','CHI_TIET_DON_HANG','Xem tất cả CTDH',NULL,NULL),(84,'/api/v1/chi-tiet-don-hang/don-hang/{donHangId}','2026-03-06 09:59:24.000000',NULL,'GET','CHI_TIET_DON_HANG','Xem CTDH theo mã đơn',NULL,NULL),(85,'/api/v1/chi-tiet-don-hang/{id}','2026-03-06 09:59:24.000000',NULL,'GET','CHI_TIET_DON_HANG','Xem CTDH theo id',NULL,NULL),(86,'/api/v1/chi-tiet-don-hang','2026-03-06 09:59:24.000000',NULL,'POST','CHI_TIET_DON_HANG','Tạo CTDH',NULL,NULL),(87,'/api/v1/chi-tiet-don-hang','2026-03-06 09:59:24.000000',NULL,'PUT','CHI_TIET_DON_HANG','Cập nhật CTDH',NULL,NULL),(88,'/api/v1/chi-tiet-don-hang/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','CHI_TIET_DON_HANG','Xóa CTDH',NULL,NULL),(89,'/api/v1/khuyen-mai-theo-hoa-don','2026-03-06 09:59:24.000000',NULL,'GET','KHUYEN_MAI_HOA_DON','Xem tất cả KMHD',NULL,NULL),(90,'/api/v1/khuyen-mai-theo-hoa-don/{id}','2026-03-06 09:59:24.000000',NULL,'GET','KHUYEN_MAI_HOA_DON','Xem KMHD theo id',NULL,NULL),(91,'/api/v1/khuyen-mai-theo-hoa-don','2026-03-06 09:59:24.000000',NULL,'POST','KHUYEN_MAI_HOA_DON','Tạo KMHD',NULL,NULL),(92,'/api/v1/khuyen-mai-theo-hoa-don','2026-03-06 09:59:24.000000',NULL,'PUT','KHUYEN_MAI_HOA_DON','Cập nhật KMHD',NULL,NULL),(93,'/api/v1/khuyen-mai-theo-hoa-don/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','KHUYEN_MAI_HOA_DON','Xóa KMHD',NULL,NULL),(94,'/api/v1/khuyen-mai-theo-diem','2026-03-06 09:59:24.000000',NULL,'GET','KHUYEN_MAI_DIEM','Xem tất cả KMD',NULL,NULL),(95,'/api/v1/khuyen-mai-theo-diem/{id}','2026-03-06 09:59:24.000000',NULL,'GET','KHUYEN_MAI_DIEM','Xem KMD theo id',NULL,NULL),(96,'/api/v1/khuyen-mai-theo-diem','2026-03-06 09:59:24.000000',NULL,'POST','KHUYEN_MAI_DIEM','Tạo KMD',NULL,NULL),(97,'/api/v1/khuyen-mai-theo-diem','2026-03-06 09:59:24.000000',NULL,'PUT','KHUYEN_MAI_DIEM','Cập nhật KMD',NULL,NULL),(98,'/api/v1/khuyen-mai-theo-diem/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','KHUYEN_MAI_DIEM','Xóa KMD',NULL,NULL),(99,'/api/v1/danh-gia-san-pham','2026-03-06 09:59:24.000000',NULL,'GET','DANH_GIA_SP','Xem tất cả đánh giá SP',NULL,NULL),(100,'/api/v1/danh-gia-san-pham/{id}','2026-03-06 09:59:24.000000',NULL,'GET','DANH_GIA_SP','Xem đánh giá SP theo id',NULL,NULL),(101,'/api/v1/danh-gia-san-pham/san-pham/{sanPhamId}','2026-03-06 09:59:24.000000',NULL,'GET','DANH_GIA_SP','Xem đánh giá theo sản phẩm',NULL,NULL),(102,'/api/v1/danh-gia-san-pham/cua-toi','2026-03-06 09:59:24.000000',NULL,'GET','DANH_GIA_SP','Xem đánh giá của tôi',NULL,NULL),(103,'/api/v1/danh-gia-san-pham','2026-03-06 09:59:24.000000',NULL,'POST','DANH_GIA_SP','Tạo đánh giá SP',NULL,NULL),(104,'/api/v1/danh-gia-san-pham/{id}','2026-03-06 09:59:24.000000',NULL,'DELETE','DANH_GIA_SP','Xóa đánh giá SP',NULL,NULL),(105,'/api/v1/phieu-nhap/kiem-ke/{id}','2026-03-06 09:59:24.000000',NULL,'PUT','PHIEU_NHAP','Kiểm kê phiếu nhập',NULL,NULL),(106,'/api/v1/danh-gia-san-pham','2026-03-06 09:59:24.000000',NULL,'PUT','DANH_GIA_SP','Cập nhật đánh giá SP',NULL,NULL);
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phieunhap`
--

DROP TABLE IF EXISTS `phieunhap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phieunhap` (
  `MaPhieuNhap` bigint NOT NULL AUTO_INCREMENT,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayDatHang` datetime(6) DEFAULT NULL,
  `NgayNhanHang` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `TenPhieuNhap` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` int DEFAULT NULL,
  `MaCuaHang` bigint DEFAULT NULL,
  `MaNhaCungCap` bigint DEFAULT NULL,
  PRIMARY KEY (`MaPhieuNhap`),
  KEY `FKoppqt1xsv8kvgjgxhyf5rdwea` (`MaCuaHang`),
  KEY `FKt0gihtrjrxp63hi9jhn04kmdd` (`MaNhaCungCap`),
  CONSTRAINT `FKoppqt1xsv8kvgjgxhyf5rdwea` FOREIGN KEY (`MaCuaHang`) REFERENCES `cuahang` (`MaCuaHang`),
  CONSTRAINT `FKt0gihtrjrxp63hi9jhn04kmdd` FOREIGN KEY (`MaNhaCungCap`) REFERENCES `nhacungcap` (`MaNhaCungCap`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phieunhap`
--

LOCK TABLES `phieunhap` WRITE;
/*!40000 ALTER TABLE `phieunhap` DISABLE KEYS */;
INSERT INTO `phieunhap` VALUES (1,NULL,'2026-02-20 10:00:00.000000',NULL,'2026-03-06 09:59:24.000000','Nhập hàng đợt 1 - CN Q.1',1,1,1),(2,NULL,'2026-02-22 10:00:00.000000',NULL,'2026-03-06 09:59:24.000000','Nhập hàng đợt 1 - CN Q.3',1,2,2),(3,NULL,'2026-03-01 10:00:00.000000',NULL,'2026-03-06 09:59:24.000000','Nhập bổ sung áo phao',0,1,3),(4,'2026-03-06 03:11:53.032407','2026-03-06 03:06:50.759161','2026-03-06 03:07:33.240289','2026-03-06 03:06:50.786039','Nhập hàng đợt 1 - CN Q.1 (Updated)',4,1,1);
/*!40000 ALTER TABLE `phieunhap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `createdAt` datetime(6) DEFAULT NULL,
  `createdBy` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updatedAt` datetime(6) DEFAULT NULL,
  `updatedBy` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,_binary '','2026-03-06 09:59:24.000000',NULL,'Quản trị viên toàn quyền','ADMIN',NULL,NULL),(2,_binary '','2026-03-06 09:59:24.000000',NULL,'Quản lý cửa hàng','QUAN_LY',NULL,NULL),(3,_binary '','2026-03-06 09:59:24.000000',NULL,'Nhân viên bán hàng','NHAN_VIEN',NULL,NULL),(4,_binary '','2026-03-06 09:59:24.000000',NULL,'Khách hàng mua sắm','KHACH_HANG',NULL,NULL);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sanpham`
--

DROP TABLE IF EXISTS `sanpham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sanpham` (
  `MaSanPham` bigint NOT NULL AUTO_INCREMENT,
  `GiaBan` double DEFAULT NULL,
  `GiaGiam` int DEFAULT NULL,
  `GiaVon` double DEFAULT NULL,
  `HinhAnhChinh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MoTa` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `SoLuong` int DEFAULT NULL,
  `TenSanPham` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` int DEFAULT NULL,
  `MaSuuTap` bigint DEFAULT NULL,
  `MaKieuSanPham` bigint DEFAULT NULL,
  `MaThuongHieu` bigint DEFAULT NULL,
  PRIMARY KEY (`MaSanPham`),
  KEY `FKnnx9p014xcsmtvakw9u6ev70n` (`MaSuuTap`),
  KEY `FKon5jx6m13s4eskvmprg94k334` (`MaKieuSanPham`),
  KEY `FK1rf9os29e4bdto1x02lamtoar` (`MaThuongHieu`),
  CONSTRAINT `FK1rf9os29e4bdto1x02lamtoar` FOREIGN KEY (`MaThuongHieu`) REFERENCES `thuonghieu` (`MaThuongHieu`),
  CONSTRAINT `FKnnx9p014xcsmtvakw9u6ev70n` FOREIGN KEY (`MaSuuTap`) REFERENCES `bosuutap` (`MaSuuTap`),
  CONSTRAINT `FKon5jx6m13s4eskvmprg94k334` FOREIGN KEY (`MaKieuSanPham`) REFERENCES `kieusanpham` (`MaKieuSanPham`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sanpham`
--

LOCK TABLES `sanpham` WRITE;
/*!40000 ALTER TABLE `sanpham` DISABLE KEYS */;
INSERT INTO `sanpham` VALUES (1,200,0,100,NULL,NULL,'2026-03-06 03:38:12.257329','2026-03-06 09:59:24.000000',158,'Áo Oxford',1,3,1,1),(2,400,10,200,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',NULL,'Quần Jean',1,3,2,1),(3,300,5,150,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',NULL,'Váy Hoa',1,1,3,2),(4,100,0,50,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',NULL,'Nịt Da',1,3,4,1),(5,600,15,300,NULL,NULL,NULL,'2026-03-06 09:59:24.000000',NULL,'Áo Phao',0,2,1,2),(6,180,0,80,NULL,NULL,NULL,'2026-03-07 14:17:37.897063',0,'Hoan',1,3,1,1);
/*!40000 ALTER TABLE `sanpham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thuonghieu`
--

DROP TABLE IF EXISTS `thuonghieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thuonghieu` (
  `MaThuongHieu` bigint NOT NULL AUTO_INCREMENT,
  `NgayCapNhat` datetime(6) DEFAULT NULL,
  `NgayTao` datetime(6) DEFAULT NULL,
  `TenThuongHieu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThaiHienThi` int DEFAULT NULL,
  `TrangThaiHoatDong` int DEFAULT NULL,
  PRIMARY KEY (`MaThuongHieu`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thuonghieu`
--

LOCK TABLES `thuonghieu` WRITE;
/*!40000 ALTER TABLE `thuonghieu` DISABLE KEYS */;
INSERT INTO `thuonghieu` VALUES (1,NULL,'2026-03-06 09:59:24.000000','Shop House',1,1),(2,NULL,'2026-03-06 09:59:24.000000','Urban Style',1,1),(3,NULL,'2026-03-06 09:59:24.000000','Classic Wear',0,1);
/*!40000 ALTER TABLE `thuonghieu` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-07 21:25:37
