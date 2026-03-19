CREATE DATABASE IF NOT EXISTS ecom_fashion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecom_fashion;


-- XÓA BẢNG CŨ NẾU ĐÃ TỒN TẠI 
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS Order_Details;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Product_Variants;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Categories;
DROP TABLE IF EXISTS Users;

-- 1. TẠO CẤU TRÚC CÁC BẢNG (TABLES)
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER') DEFAULT 'CUSTOMER',
    address TEXT,
    phone_number VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    parent_id INT NULL,
    FOREIGN KEY (parent_id) REFERENCES Categories(category_id)
);

CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT,
    sku VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    old_price DECIMAL(10,2),
    current_price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);

CREATE TABLE Product_Variants (
    variant_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    size VARCHAR(20) NOT NULL,
    color VARCHAR(50) NOT NULL,
    stock_quantity INT DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    shipping_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Order_Details (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    variant_id INT,
    quantity INT NOT NULL,
    price_at_purchase DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (variant_id) REFERENCES Product_Variants(variant_id)
);

CREATE TABLE Reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    user_id INT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);


-- 2. NHẬP DỮ LIỆU MẪU (MOCK DATA)
-- Danh mục Gốc (Level 1)
INSERT INTO Categories (category_name, parent_id) VALUES 
('Quần áo', NULL),   
('Phụ kiện', NULL),  
('Giày dép', NULL);  

-- Danh mục Con (Level 2)
INSERT INTO Categories (category_name, parent_id) VALUES 
('Áo thun', 1),      
('Áo sơ mi', 1),     
('Quần Tây', 1),     
('Thắt lưng', 2),    
('Túi xách', 2);     

-- Sản phẩm chi tiết (Level 3)
INSERT INTO Products (category_id, sku, name, description, old_price, current_price, image_url) VALUES 
(4, 'AT-BLUE-01', 'Áo thun Blue Sea Cổ Tròn', 'Chất liệu cotton 100% thoáng mát', 300000, 250000, 'ao-blue-sea.jpg'),
(6, 'QT-OFFICE-09', 'Quần Tây Nam Dáng Ôm', 'Phù hợp môi trường công sở', 500000, 450000, 'quan-tay-den.jpg'),
(8, 'TX-LEATHER-02', 'Túi xách nữ da bò thật', 'Thiết kế sang trọng cao cấp', 1200000, 990000, 'tui-xach-da.jpg');

-- Biến thể (Size, Màu, Tồn kho) (Level 4)
INSERT INTO Product_Variants (product_id, size, color, stock_quantity) VALUES 
(1, 'M', 'Xanh Biển', 50),
(1, 'L', 'Xanh Biển', 20),
(1, 'L', 'Trắng', 15),
(2, '32', 'Đen', 30),
(2, '33', 'Xám', 10),
(3, 'FreeSize', 'Nâu', 5);