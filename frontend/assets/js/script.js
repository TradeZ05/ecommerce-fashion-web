// Đường dẫn API của bạn
const API_URL = "http://localhost:8080/api/products";

async function loadProducts() {
    try {
        const response = await fetch(API_URL);
        const products = await response.json();
        
        const productList = document.getElementById('product-list');
        productList.innerHTML = ''; // Xóa chữ "Đang tải..."

        products.forEach(product => {
            const productCard = `
                <div class="card">
                    <img src="${product.imageUrl || 'https://via.placeholder.com/150'}" alt="${product.name}">
                    <h3>${product.name}</h3>
                    <p class="category">${product.category ? product.category.name : 'Chưa phân loại'}</p>
                    <p class="price">${product.price.toLocaleString('vi-VN')} VNĐ</p>
                    <button>Thêm vào giỏ</button>
                </div>
            `;
            productList.innerHTML += productCard;
        });
    } catch (error) {
        console.error("Lỗi khi gọi API:", error);
        document.getElementById('product-list').innerHTML = "Không thể kết nối với Backend!";
    }
}

// Chạy hàm ngay khi mở trang web
loadProducts();