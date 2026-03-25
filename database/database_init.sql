-- ==========================================
-- KHỞI TẠO DATABASE & SET TIẾNG VIỆT
-- ==========================================
CREATE DATABASE IF NOT EXISTS ngonlanh_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ngonlanh_db;

-- ==========================================
-- XÓA BẢNG CŨ (Hỗ trợ Backend chạy lại nhiều lần không lỗi)
-- ==========================================
DROP TABLE IF EXISTS Order_Details;
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Vouchers;
DROP TABLE IF EXISTS Product_Variants;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Categories;
DROP TABLE IF EXISTS Users;

-- ==========================================
-- 1. BẢNG TÀI KHOẢN 
-- ==========================================
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER') DEFAULT 'CUSTOMER',
    address TEXT,
    phone_number VARCHAR(15),
    is_deleted BOOLEAN DEFAULT FALSE, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==========================================
-- 2. BẢNG DANH MỤC 
-- ==========================================
CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    parent_id INT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (parent_id) REFERENCES Categories(category_id) ON DELETE SET NULL
);

-- ==========================================
-- 3. BẢNG SẢN PHẨM 
-- ==========================================
CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    old_price DECIMAL(10,2),
    current_price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);

-- ==========================================
-- 4. BẢNG BIẾN THỂ (Kích cỡ & Calo)
-- ==========================================
CREATE TABLE Product_Variants (
    variant_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    sku VARCHAR(50) UNIQUE NOT NULL,  
    size VARCHAR(50) NOT NULL, 
    calories INT NOT NULL,     
    stock_quantity INT DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

-- ==========================================
-- 5. BẢNG MÃ GIẢM GIÁ
-- ==========================================
CREATE TABLE Vouchers (
    voucher_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    discount_percent INT CHECK (discount_percent > 0 AND discount_percent <= 100),
    max_discount_amount DECIMAL(10,2),
    min_order_value DECIMAL(10,2) DEFAULT 0,
    usage_limit INT DEFAULT 100,      
    used_count INT DEFAULT 0,         
    valid_from DATETIME NOT NULL,
    valid_until DATETIME NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

-- ==========================================
-- 6. BẢNG ĐƠN HÀNG 
-- ==========================================
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    voucher_id INT NULL,
    payment_method ENUM('COD', 'BANK', 'MOMO') DEFAULT 'COD', 
    shipping_fee DECIMAL(10,2) DEFAULT 0,                     
    discount_amount DECIMAL(10,2) DEFAULT 0,                  
    total_amount DECIMAL(10,2) NOT NULL,                      
    status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    shipping_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (voucher_id) REFERENCES Vouchers(voucher_id)
);

-- ==========================================
-- 7. CHI TIẾT ĐƠN HÀNG
-- ==========================================
CREATE TABLE Order_Details (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    variant_id INT,
    quantity INT NOT NULL,
    price_at_purchase DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (variant_id) REFERENCES Product_Variants(variant_id)
);

-- ==========================================
-- 8. ĐÁNH GIÁ & BÌNH LUẬN
-- ==========================================
CREATE TABLE Reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    user_id INT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (product_id, user_id), 
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- ==========================================
-- TỐI ƯU HÓA HIỆU NĂNG (INDEX)
-- ==========================================
CREATE INDEX idx_products_category ON Products(category_id);
CREATE INDEX idx_variants_product ON Product_Variants(product_id);
CREATE INDEX idx_orders_user ON Orders(user_id);

-- ==========================================
-- DỮ LIỆU MẪU (MOCK DATA)
-- ==========================================
INSERT INTO Categories (category_name, parent_id) VALUES 
('Thực đơn chay', NULL),   
('Thực đơn kiêng', NULL),  
('Nước uống Detox', NULL);  

INSERT INTO Categories (category_name, parent_id) VALUES 
('Món chính chay', 1),      
('Salad Ức Gà/Cá Hồi', 2);     

INSERT INTO Products (category_id, name, description, old_price, current_price, image_url) VALUES 
(4, 'Cơm Gạo Lứt Nấm Bào Ngư', 'Thuần chay 100%, gạo lứt huyết rồng dẻo', 65000, 55000, 'com-chay-nam.jpg'),
(5, 'Salad Ức Gà Áp Chảo Sốt Mè', 'Ức gà ta chuẩn fit, nhiều protein, rau sạch farm', 85000, 79000, 'salad-uc-ga.jpg');

INSERT INTO Product_Variants (product_id, sku, size, calories, stock_quantity) VALUES 
(1, 'VEG-COM-01-STD', 'Phần Tiêu Chuẩn', 350, 50),
(1, 'VEG-COM-01-BIG', 'Phần Lớn (Thêm nấm)', 450, 20),
(2, 'EAT-SALAD-02-500', 'Hộp 500gr', 280, 30);

INSERT INTO Vouchers (code, discount_percent, max_discount_amount, min_order_value, usage_limit, valid_from, valid_until) VALUES 
('CHAYNGON20', 20, 50000, 150000, 50, '2026-03-01 00:00:00', '2026-04-30 23:59:59'),
('EATCLEAN10', 10, 30000, 100000, 100, '2026-03-01 00:00:00', '2026-04-30 23:59:59');